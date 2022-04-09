package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.project.common.Common;

public class Welcome extends AppCompatActivity {
    private Button hLoginBtn, hSignupBtn ;
    public SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
        hLoginBtn = findViewById(R.id.hLoginBtn) ;
        hSignupBtn = findViewById(R.id.hSignupBtn) ;
        SharedPreferences sharedPreferencesLoginStatus =
                getSharedPreferences("isLogin", MODE_PRIVATE);
        SharedPreferences preferences = getSharedPreferences("NotificationMode",
                Context.MODE_PRIVATE);
        String isLogin =  sharedPreferencesLoginStatus.getString("isLogin","") ;
        if(isLogin.equals("true")){
            Intent mainActivity = new Intent(Welcome.this,MainActivity.class) ;
            startActivity(mainActivity);
            finish();
        }
        else if(isLogin.equals("adminMode")){
            Intent dashboard = new Intent(Welcome.this,Dashboard.class) ;
            startActivity(dashboard);
            finish();
        }
        SharedPreferences.Editor editor1 = preferences.edit() ;
        editor1.putString("NotificationMode","true") ;
        MainActivity.noti = true ;
        editor1.apply();


        hLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Welcome.this,Login.class) ;
                startActivity(login);
                finish();
            }
        });
        hSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(Welcome.this,Signup.class) ;
                startActivity(signup);
                finish();
            }
        });
    }
}