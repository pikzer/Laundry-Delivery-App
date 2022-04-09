package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.common.Common;
import com.example.project.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private EditText lPhoneEdt, lPassEdt ;
    private Button lLoginBtn,lSignupBtn ;
    private FirebaseDatabase database ;
    private DatabaseReference mRef ;

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        lPhoneEdt = findViewById(R.id.lPhoneEdt) ;
        lPassEdt = findViewById(R.id.lPassEdt) ;
        lLoginBtn = findViewById(R.id.lLoginBtn) ;
        lSignupBtn = findViewById(R.id.lSignupBtn) ;
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("User");

        lLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String ph = lPhoneEdt.getText().toString(), pas = lPassEdt.getText().toString() ;
                        // if no enter Text
                        if(ph.equals("") || pas.equals("")){
                            Toast.makeText(Login.this,"Wrong phone number/password!",
                                    Toast.LENGTH_SHORT).show();
                            hideKeyboardFrom(Login.this,v);
                        }
                        // is User name Existed
                        else if(!snapshot.child(ph).exists()){
                            Toast.makeText(Login.this,"Wrong phone number/password!",
                                    Toast.LENGTH_SHORT).show();
                            hideKeyboardFrom(Login.this,v);

                        }
                        // Login Complete
                        else if(snapshot.child(ph).exists() &&
                                snapshot.child(ph+"/password").getValue(String.class).equals(pas)){
                            Toast.makeText(Login.this,"Log in complete!",
                                    Toast.LENGTH_SHORT).show();
                            Common.currentUser = new User(snapshot.child(ph+"/name").getValue().toString(),
                                    snapshot.child(ph+"/email").getValue().toString(),ph) ;
                            // TO DEL
                            SharedPreferences preferences1 = getSharedPreferences("isLogin",MODE_PRIVATE) ;
                            SharedPreferences.Editor editor1 = preferences1.edit() ;
                            SharedPreferences preferences2  = getSharedPreferences("CurrentUser",MODE_PRIVATE) ;
                            SharedPreferences.Editor editor2 = preferences2.edit() ;
                            hideKeyboardFrom(Login.this,v);

                            if(ph.equals("1234") && pas.equals("admin")){
                                editor1.putString("isLogin","adminMode");
                                editor1.apply();
                                editor2.putString("CurrentUser",Common.currentUser.getPhone()) ;
                                editor2.apply();
                                Intent dashboard = new Intent(Login.this,Dashboard.class) ;
                                finish();
                                startActivity(dashboard);
                            }
                            else{
                                editor1.putString("isLogin","true");
                                editor1.apply();
                                editor2.putString("CurrentUser",Common.currentUser.getPhone()) ;
                                editor2.apply();
                                Intent mainActivity = new Intent(Login.this,MainActivity.class) ;
                                finish();
                                startActivity(mainActivity);
                            }
                        }
                        // Password Wrong
                        else{
                            Toast.makeText(Login.this,"Wrong phone number/password!",
                                    Toast.LENGTH_SHORT).show();
                            hideKeyboardFrom(Login.this,v);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        lSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(Login.this,Signup.class) ;
                startActivity(signup);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent welcome = new Intent(Login.this,Welcome.class) ;
        startActivity(welcome);
        finish();
    }
}