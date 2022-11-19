package com.handlings.shipcargo;

import static com.handlings.shipcargo.Contants.TODAY;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Utility {
    Context context;
    Activity activity;
    SessionManager sessionManager;

    public Utility(Context context, SessionManager sessionManager,Activity activity) {
        this.context = context;
        this.activity =  activity;
        this.sessionManager = sessionManager;
    }

    public   void  createSnackbar(String whatever){
        Toast.makeText(context,whatever,Toast.LENGTH_SHORT).show();
    }

//    public void replaceFragment (Fragment fragment){
//
//        FragmentManager fragmentManager =  activity.getFragmentManager().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_container,fragment);
//        fragmentTransaction.commit();
//
//    }

    public void getData(String url,String type){

        final String[] data = {""};
        OkHttpClient client = new OkHttpClient();
        System.out.println("Token "+sessionManager.getUserDetails().get(Contants.TOKEN));

        if(sessionManager.isLoggedIn()){
            Request request = new Request.Builder()
                    .header("Authorization","Token "+sessionManager.getUserDetails().get(Contants.TOKEN))
                    .url(url)
                    .build();

            Call j =  client.newCall(request);
            j.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("failed terribly");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() ==  200){
                        String ju =  response.body().string();
//                        if (type == TODAY){
                            sessionManager.createTodayLogs(type,ju);
                            System.out.println("the da "+ju);

//                        }


                    }else{
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                createSnackbar("Error while getting data!!");

                            }
                        });
                    }

                }
            });
        }else{
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Call  m = client.newCall(request);
            m.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("failed vibaya sana");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code() == 200){
                        if (type == TODAY){
                            String ju =  response.body().string();
                            sessionManager.createTodayLogs(TODAY,ju);
                            System.out.println("the da "+ju);

                        }


                    }else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                createSnackbar("Error while getting data");
                            }
                        });
                    }

                }
            });
        }



    }

    public void sendPost(String urlll, RequestBody formBody, String type, ProgressBar progressBar){
        OkHttpClient client = new OkHttpClient();

        Request request ;
        if(sessionManager.isLoggedIn()){
            request =  new Request.Builder()
                    .header("Authorization","Token "+sessionManager.getUserDetails().get(Contants.TOKEN))
                    .url(urlll)
                    .post(formBody)
                    .build();
        }else{
           request  = new Request.Builder()
                    .url(urlll)
                    .post(formBody)
                    .build();
        }


        Call k = client.newCall(request);
        k.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("there is a failure in the systeem");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (type == Contants.LOGINTYPE){
                    if(response.code() == 200){
                        System.out.println("code : "+ response.code());
//                        System.out.println("body : " + response.body().string());
                        String body = String.valueOf(response.body().string());


                        try {
                            JSONObject jsonObject =  new JSONObject(body);
                            String tok =  jsonObject.getString("auth_token");
                            System.out.println("token "+tok);
                            sessionManager.createLoginSession(Contants.TOKEN,tok);

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sessionManager.checkLogin();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else {

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                createSnackbar("Error while loggin In");
                            }
                        });
                    }

                }else  if(type == Contants.SAVETODB){
                    if(response.code() == 200){
                        String body = String.valueOf(response.body().string());
                        try {
                            JSONObject jsonObject =  new JSONObject(body);
                            String messgae = jsonObject.getString("message");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    createSnackbar(messgae);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }else if(type == Contants.SIGNUPUSER){
                    if(response.code() == 201){
                        createSnackbar("User created sign in User");
                        Intent i =  new Intent(activity,MainActivity.class);
                        activity.startActivity(i);
                    }

                }



            }
        });


    }
}
