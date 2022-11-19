package com.handlings.shipcargo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class SignUpActivity extends AppCompatActivity {
    private Button btn_login_signup,btn_signUp_signup;
    private EditText edt_s_username,edt_s_email,edt_s_password;
    SessionManager sessionManager;
    Utility utility;

    ProgressBar progressBarSubmitLoginssN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btn_login_signup  = findViewById(R.id.btn_login_signup);
        edt_s_password =  findViewById(R.id.edt_s_password);
        edt_s_username =  findViewById(R.id.edt_s_username);
        edt_s_email =  findViewById(R.id.edt_s_email);
        btn_signUp_signup = findViewById(R.id.btn_signUp_signup);
        progressBarSubmitLoginssN =  findViewById(R.id.progressBarSubmitLoginssN);

        sessionManager =  new SessionManager(getApplicationContext());
        utility = new Utility(getApplicationContext(),sessionManager, SignUpActivity.this);

        btn_signUp_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarSubmitLoginssN.setVisibility(View.VISIBLE);
                createUser();
            }
        });
        
        btn_login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void createUser() {

        String username =  edt_s_username.getText().toString();
        String email = edt_s_email.getText().toString();
        String password =  edt_s_password.getText().toString();

        if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
            createMessage("Fill in all details");
        }else{

            RequestBody formBody = new FormBody.Builder()
                    .add("username", username)
                    .add("email",email)
                    .add("password",password)
                    .build();
            utility.sendPost(Contants.SIGNUPURL,formBody,Contants.SIGNUPUSER,progressBarSubmitLoginssN);

        }


    }

    private void createMessage(String fill_in_all_details) {

        Toast.makeText(getApplicationContext(),fill_in_all_details,Toast.LENGTH_SHORT).show();
    }
}