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
    private Button lLoginBtn ;
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
                            SharedPreferences preferences = getSharedPreferences("isLogin",MODE_PRIVATE) ;
                            SharedPreferences.Editor editor = preferences.edit() ;
                            editor.putString("isLogin","true");
                            editor.apply();
                            preferences  = getSharedPreferences("CurrentUser",MODE_PRIVATE) ;
                            editor = preferences.edit();
                            editor.putString("CurrentUser",Common.currentUser.getPhone()) ;
                            editor.apply();
                            hideKeyboardFrom(Login.this,v);
                            Intent mainActivity = new Intent(Login.this,MainActivity.class) ;
                            startActivity(mainActivity);
                            finish();
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
    }
}