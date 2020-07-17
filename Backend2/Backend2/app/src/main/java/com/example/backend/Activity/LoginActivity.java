package com.example.backend.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.backend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import DAO.Impl.UserRepoImpl;

public class LoginActivity extends AppCompatActivity {
    private EditText et_email;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;

    private   boolean validateForm(){

        // validate and sanitize inputs
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
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // if log in info matches records in the database, successfully login and go to main page
        if (firebaseUser != null&&firebaseUser.isEmailVerified()) {
            Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_email = (EditText)findViewById(R.id.et_email);
        et_password = (EditText)findViewById(R.id.et_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_register = (Button)findViewById(R.id.btn_register);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm())
                    // check input validation
                new UserRepoImpl().login(et_email.getText().toString(),et_password.getText().toString(),LoginActivity.this);

            }
        });


        //
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
