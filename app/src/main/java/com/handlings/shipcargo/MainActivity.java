package com.handlings.shipcargo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {
    private Button btn_login,btn_signup_login;
    private EditText username_place_login,password_place_login;
    Utility utility;
    SessionManager sessionManager;
    ProgressBar progressBarSubmitLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager =  new SessionManager(getApplicationContext());
        sessionManager.checkLogin();



        utility = new Utility(getApplicationContext(),sessionManager,MainActivity.this);
        btn_login =  findViewById(R.id.btn_login);
        password_place_login =  findViewById(R.id.password_place_login);
        username_place_login =  findViewById(R.id.username_place_login);
        btn_signup_login = findViewById(R.id.btn_signup_login);
        progressBarSubmitLogin =  findViewById(R.id.progressBarSubmitLogin);

        btn_signup_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(i);


            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarSubmitLogin.setVisibility(View.VISIBLE);
//                Intent i =  new Intent(getApplicationContext(),HomeActivity.class);
//                startActivity(i);

                runConenctMethod();

            }
        });
    }

    private void runConenctMethod() {
        String username = username_place_login.getText().toString();
        String password  =   password_place_login.getText().toString();
        if(username.isEmpty() || password.isEmpty()){
           utility.createSnackbar("Please fill in all details!!");
        }else{
            RequestBody formBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password",password)
                    .build();
            utility.sendPost(Contants.LOGINURL,formBody,Contants.LOGINTYPE,progressBarSubmitLogin);
//            System.out.println("response " + response);

        }
    }



}