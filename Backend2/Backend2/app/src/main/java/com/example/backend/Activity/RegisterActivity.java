package com.example.backend.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.backend.R;
import DAO.Impl.UserRepoImpl;


public class RegisterActivity extends AppCompatActivity {
    private EditText et_email;
    private EditText et_password;
    private EditText et_reenterpassword;
    private Button btn_register;
    private UserRepoImpl userService=new UserRepoImpl();
    String email;
    String password;

    // check the information not null and correct in form
    private   boolean validateForm(){
        boolean result = true;
        if (TextUtils.isEmpty(et_email.getText().toString())) {
            et_email.setError("Required");
            result = false;
        }
        else if(!et_email.getText().toString().contains("@")){
            et_email.setError("Invalid Email Address!");
            result = false;
        }else  if (TextUtils.isEmpty(et_password.getText().toString())) {
            et_password.setError("Required");
            result = false;
        }else  if (TextUtils.isEmpty(et_reenterpassword.getText().toString())) {
            et_reenterpassword.setError("Required");
            result = false;
        }

        else if (et_password.getText().toString().length()<6){
            et_password.setError("At Least 6 Digit!");
            result = false;
        }
        else {
            et_password.setError(null);
        }
        return result;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_email = (EditText)findViewById(R.id.et_email);
     //   et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        et_reenterpassword = (EditText)findViewById(R.id.et_reenterpassword);
        btn_register = (Button)findViewById(R.id.btn_register);
        et_email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // make sure the input is valid not null etc
                if(!validateForm()) return;
                if (et_password.getText().toString().equals( et_reenterpassword.getText().toString())) {
                    email = et_email.getText().toString();
                    password = et_password.getText().toString();
                   userService.register(email,password,(Activity) RegisterActivity.this);


                }
                // if password and re-enter password doesn't match, error and won't register
                else if (!et_password.getText().toString().equals(et_reenterpassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Your password entries don't match. Please verify again.", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }
}
