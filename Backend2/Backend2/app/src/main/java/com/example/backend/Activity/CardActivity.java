package com.example.backend.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.backend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.india.Payu.PayuUtils;
import com.payu.india.PostParams.PaymentPostParams;

import java.util.Calendar;

import DAO.Impl.ItemRepoImpl;
import Model.Item;

public class CardActivity extends AppCompatActivity {//implements View.OnClickListener {

    private Button payNowButton;
    private EditText cardNameEditText;
    private EditText cardNumberEditText;
    private EditText cardCvvEditText;
    private EditText cardExpiryMonthEditText;
    private EditText cardExpiryYearEditText;
    private Bundle bundle;

    private String cardName;
    private String cardNumber;
    private String cvv;
    private String expiryMonth;
    private String expiryYear;

    private PayuHashes mPayuHashes;
    private PaymentParams mPaymentParams;
    private PostData postData;

    private TextView amountTextView;
    private TextView transactionIdTextView;
    private PayuConfig payuConfig;

    private PayuUtils payuUtils;
    private ItemRepoImpl itemservice=new ItemRepoImpl();

    Item item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

       // (payNowButton = (Button) findViewById(R.id.button_card_make_payment)).setOnClickListener(this);
        payNowButton = (Button) findViewById(R.id.button_card_make_payment);
        cardNameEditText = (EditText) findViewById(R.id.edit_text_name_on_card);
        cardNumberEditText = (EditText) findViewById(R.id.edit_text_card_number);
        cardCvvEditText = (EditText) findViewById(R.id.edit_text_card_cvv);
        cardExpiryMonthEditText = (EditText) findViewById(R.id.edit_text_expiry_month);
        cardExpiryYearEditText = (EditText) findViewById(R.id.edit_text_expiry_year);

        bundle = getIntent().getExtras();
        item = bundle.getParcelable("payitem");

        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateForm()) {

                mPaymentParams.setHash(mPayuHashes.getPaymentHash());
//                System.out.println(mPayuHashes.getPaymentHash());

                // lets try to get the post params

                postData = null;
                // lets get the current card number, card name, expiration date, and card cvv
                cardNumber = String.valueOf(cardNumberEditText.getText());
                cardName = cardNameEditText.getText().toString();
                expiryMonth = cardExpiryMonthEditText.getText().toString();
                expiryYear = cardExpiryYearEditText.getText().toString();
                cvv = cardCvvEditText.getText().toString();

//                Log.d("mytag", cardNumber);
//                Log.d("mytag", cardName);
//                Log.d("mytag", expiryMonth);
//                Log.d("mytag", expiryYear);
//                Log.d("mytag", cvv);

                // put payment parameters into payU to be passed to the server later
                mPaymentParams.setCardNumber(cardNumber);
                mPaymentParams.setCardName(cardName);
                mPaymentParams.setNameOnCard(cardName);
                mPaymentParams.setExpiryMonth(expiryMonth);
                mPaymentParams.setExpiryYear(expiryYear);
                mPaymentParams.setCvv(cvv);
                try {
                    postData = new PaymentPostParams(mPaymentParams, PayuConstants.CC).getPaymentPostParams();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                long longCardNumber = Long.parseLong(cardNumber);
                boolean cardNumberValid = isValid(longCardNumber);
                boolean cardDateValid = validateExpiryDate(expiryMonth, expiryYear);
                boolean cvvNumberValid = cvvValid(cvv);

                // check if the card number, expiration data are valid
                    // check if the cvv is a valid under the consideration of cvvs
                  if (cardNumberValid && cardDateValid && cvvNumberValid) {
                    String curname = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    item.setBuyerId(id);
                    item.setBuyerName(curname);
                    item.setStatus("1");
                    itemservice.update(item, 1);

                    // successful payment gets directed to the main page
                    Toast.makeText(getApplicationContext(), "Payment Successfully Made.", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(CardActivity.this, MainPageActivity.class);
                    startActivity(in);
                } else {
                    // didn't sell the item, put item back to items listview and mark it as unsold
                    Toast.makeText(getApplicationContext(), "Incorrect Card Credentials. Please proceed again.", Toast.LENGTH_SHORT).show();
                }
            }

            }
        });


        // lets get payment default params and hashes
        mPayuHashes = bundle.getParcelable(PayuConstants.PAYU_HASHES);
        mPaymentParams = bundle.getParcelable(PayuConstants.PAYMENT_PARAMS);
        payuConfig = bundle.getParcelable(PayuConstants.PAYU_CONFIG);
        payuConfig = null != payuConfig ? payuConfig : new PayuConfig();

        (amountTextView = (TextView) findViewById(R.id.text_view_amount)).setText(PayuConstants.AMOUNT + ": " + mPaymentParams.getAmount());
        (transactionIdTextView = (TextView) findViewById(R.id.text_view_transaction_id)).setText(PayuConstants.TXNID + ": " + mPaymentParams.getTxnId());


        payuUtils = new PayuUtils();


        cardNumberEditText.addTextChangedListener(new TextWatcher() {
            String issuer;
            Drawable issuerDrawable;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 5){ // to confirm rupay card we need min 6 digit.
                    if(null == issuer) issuer = payuUtils.getIssuer(charSequence.toString());
                    if (issuer != null && issuer.length() > 1 && issuerDrawable == null){
                        issuerDrawable = getIssuerDrawable(issuer);
                        if(issuer.contentEquals(PayuConstants.SMAE)){ // hide cvv and expiry
                            cardExpiryMonthEditText.setVisibility(View.GONE);
                            cardExpiryYearEditText.setVisibility(View.GONE);
                            cardCvvEditText.setVisibility(View.GONE);
                        }else{ //show cvv and expiry
                            cardExpiryMonthEditText.setVisibility(View.VISIBLE);
                            cardExpiryYearEditText.setVisibility(View.VISIBLE);
                            cardCvvEditText.setVisibility(View.VISIBLE);
                        }
                    }
                }else{
                    issuer = null;
                    issuerDrawable = null;
                }
                cardNumberEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, issuerDrawable, null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    // Validate and sanitize inputs
    private   boolean validateForm(){
        boolean result = true;
        if (TextUtils.isEmpty(cardNumberEditText.getText().toString())) {
            cardNumberEditText.setError("Required");
            result = false;
        }
        else if(TextUtils.isEmpty(cardNameEditText.getText().toString())){
            cardNameEditText.setError("Required");
            result = false;
        }else  if (TextUtils.isEmpty(cardExpiryMonthEditText.getText().toString())) {
            cardExpiryMonthEditText.setError("Required");
            result = false;
        }else  if (TextUtils.isEmpty(cardExpiryYearEditText.getText().toString())) {
            cardExpiryYearEditText.setError("Required");
            result = false;
        }
        else if(TextUtils.isEmpty(cardCvvEditText.getText().toString())) {
            cardCvvEditText.setError("Required");
            result = false;

        }

        return result;
    }

/*
    @Override
    public void onClick(View v) {
        // Oh crap! Resource IDs cannot be used in a switch statement in Android library modules less... (Ctrl+F1)
        // Validates using resource IDs in a switch statement in Android library module
        // we cant not use switch and gotta use simple if else
        if (v.getId() == R.id.button_card_make_payment) {
            // setup the hash
            mPaymentParams.setHash(mPayuHashes.getPaymentHash());
            System.out.println(mPayuHashes.getPaymentHash());

            // lets try to get the post params

            postData = null;
            // lets get the current card number;
            cardNumber = String.valueOf(cardNumberEditText.getText());
            cardName = cardNameEditText.getText().toString();
            expiryMonth = cardExpiryMonthEditText.getText().toString();
            expiryYear = cardExpiryYearEditText.getText().toString();
            cvv = cardCvvEditText.getText().toString();

            // lets not worry about ui validations.
            mPaymentParams.setCardNumber(cardNumber);
            mPaymentParams.setCardName(cardName);
            mPaymentParams.setNameOnCard(cardName);
            mPaymentParams.setExpiryMonth(expiryMonth);
            mPaymentParams.setExpiryYear(expiryYear);
            mPaymentParams.setCvv(cvv);
            try {
                postData = new PaymentPostParams(mPaymentParams, PayuConstants.CC).getPaymentPostParams();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (postData.getCode() == PayuErrors.NO_ERROR) {
                // okay good to go.. lets make a transaction
                // launch webview
                payuConfig.setData(postData.getResult());
                Intent intent = new Intent(this, PaymentsActivity.class);
                intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
                startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
            } else {
                Toast.makeText(this, postData.getResult(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, postData.getResult(), Toast.LENGTH_LONG).show();
        }
    }

 */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            setResult(resultCode, data);
            finish();
        }
    }

    // functions for credit card valifation checking
    public static boolean isValid(long number)
    {
        return (getSize(number) >= 13 &&
                getSize(number) <= 16) &&
                (prefixMatched(number, 4) ||
                        prefixMatched(number, 5) ||
                        prefixMatched(number, 37) ||
                        prefixMatched(number, 6)) &&
                ((sumOfDoubleEvenPlace(number) +
                        sumOfOddPlace(number)) % 10 == 0);
    }

    // Get the result from Step 2
    public static int sumOfDoubleEvenPlace(long number)
    {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 2; i >= 0; i -= 2)
            sum += getDigit(Integer.parseInt(num.charAt(i) + "") * 2);

        return sum;
    }

    // Return this number if it is a single digit, otherwise,
    // return the sum of the two digits
    public static int getDigit(int number)
    {
        if (number < 9)
            return number;
        return number / 10 + number % 10;
    }

    // Return sum of odd-place digits in number
    public static int sumOfOddPlace(long number)
    {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 1; i >= 0; i -= 2)
            sum += Integer.parseInt(num.charAt(i) + "");
        return sum;
    }

    // Return true if the digit d is a prefix for number
    public static boolean prefixMatched(long number, int d)
    {
        return getPrefix(number, getSize(d)) == d;
    }

    // Return the number of digits in d
    public static int getSize(long d)
    {
        String num = d + "";
        return num.length();
    }

    // Return the first k number of digits from
    // number. If the number of digits in number
    // is less than k, return number.
    public static long getPrefix(long number, int k)
    {
        if (getSize(number) > k) {
            String num = number + "";
            return Long.parseLong(num.substring(0, k));
        }
        return number;
    }


    // check if the expiration date is valid for the current card
    public static boolean validateExpiryDate(String month, String year) {
        if (year.length() != 4 && year.length() != 2) {
            return false;
        }
        int iMonth, iYear;
        try {
            iMonth = Integer.parseInt(month);
            iYear = Integer.parseInt(year);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return validateExpiryDate(iMonth, iYear);
    }

    /**
     * Checks if the card is still valid
     * @param month int containing the expiring month of the card
     * @param year int containing the expiring year of the card
     * @return boolean containing the result of the verification
     */
    public static boolean validateExpiryDate(int month, int year) {
        if (month < 1 || year < 1) return false;
        Calendar cal = Calendar.getInstance();
        int curMonth = cal.get(Calendar.MONTH) + 1;
        int curYear = cal.get(Calendar.YEAR);
        if(year < 100) curYear -= 2000;
        return (curYear == year) ? curMonth <= month : curYear < year;
    }


    // check if the cvv digit if valid
    // because of security reasons we didn't check the validation of cvv to a specific card number
    public static boolean cvvValid(String cvv) {
        return ((cvv.length() == 3) || (cvv.length() == 4));
    }

    // render card issuer's pictures based on the card number information
    private Drawable getIssuerDrawable(String issuer){

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            switch (issuer) {
                case PayuConstants.VISA:
                    return getResources().getDrawable(R.drawable.visa);
                case PayuConstants.LASER:
                    return getResources().getDrawable(R.drawable.laser);
                case PayuConstants.DISCOVER:
                    return getResources().getDrawable(R.drawable.discover);
                case PayuConstants.MAES:
                    return getResources().getDrawable(R.drawable.maestro);
                case PayuConstants.MAST:
                    return getResources().getDrawable(R.drawable.master);
                case PayuConstants.AMEX:
                    return getResources().getDrawable(R.drawable.amex);
                case PayuConstants.DINR:
                    return getResources().getDrawable(R.drawable.diner);
                case PayuConstants.JCB:
                    return getResources().getDrawable(R.drawable.jcb);
                case PayuConstants.SMAE:
                    return getResources().getDrawable(R.drawable.maestro);
                case PayuConstants.RUPAY:
                    return getResources().getDrawable(R.drawable.rupay);
            }
            return null;
        }else {

            switch (issuer) {
                case PayuConstants.VISA:
                    return getResources().getDrawable(R.drawable.visa, null);
                case PayuConstants.LASER:
                    return getResources().getDrawable(R.drawable.laser, null);
                case PayuConstants.DISCOVER:
                    return getResources().getDrawable(R.drawable.discover, null);
                case PayuConstants.MAES:
                    return getResources().getDrawable(R.drawable.maestro, null);
                case PayuConstants.MAST:
                    return getResources().getDrawable(R.drawable.master, null);
                case PayuConstants.AMEX:
                    return getResources().getDrawable(R.drawable.amex, null);
                case PayuConstants.DINR:
                    return getResources().getDrawable(R.drawable.diner, null);
                case PayuConstants.JCB:
                    return getResources().getDrawable(R.drawable.jcb, null);
                case PayuConstants.SMAE:
                    return getResources().getDrawable(R.drawable.maestro, null);
                case PayuConstants.RUPAY:
                    return getResources().getDrawable(R.drawable.rupay, null);
            }
            return null;
        }
    }
}
