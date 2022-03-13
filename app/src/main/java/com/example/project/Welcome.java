package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

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
        String isLogin =  sharedPreferencesLoginStatus.getString("isLogin","") ;
        if(isLogin.equals("true")){
            Intent mainActivity = new Intent(Welcome.this,MainActivity.class) ;
            startActivity(mainActivity);
            finish();
        }

        hLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Welcome.this,Login.class) ;
                startActivity(login);
            }
        });
        hSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(Welcome.this,Signup.class) ;
                startActivity(signup);
            }
        });
    }
}