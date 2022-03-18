package com.example.project;

import static com.example.project.Login.hideKeyboardFrom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {

    private FirebaseDatabase database ;
    private DatabaseReference mRef ;

    private EditText sPhoneEdt, sPassEdt, sNameEdt, sEmailEdt ;
    private Button sSignupBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        sPhoneEdt = findViewById(R.id.sPhoneEdt) ;
        sPassEdt = findViewById(R.id.sPassEdt) ;
        sNameEdt = findViewById(R.id.sNameEdt) ;
        sSignupBtn = findViewById(R.id.sSignupBtn) ;
        sEmailEdt = findViewById(R.id.sEmailEdt) ;

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("User");

        sSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String ph = sPhoneEdt.getText().toString(),pas = sPassEdt.getText().toString(),
                                name = sNameEdt.getText().toString(), email = sEmailEdt.getText().toString() ;
                        // if null edittext
                        if(ph.equals("")||pas.equals("")||name.equals("")||email.equals("")){
                            Toast.makeText(Signup.this,"Please insert information!",
                                    Toast.LENGTH_SHORT).show();
                            hideKeyboardFrom(Signup.this,v);
                        }
                        // if phone is already existed
                        else if(snapshot.child(ph).exists()){
                            Toast.makeText(Signup.this,"Phone Number is already used!",
                                    Toast.LENGTH_SHORT).show();
                            hideKeyboardFrom(Signup.this,v);
                        }
                        // if login success
                        else if(!snapshot.child(ph).exists()){
                            User user = new User(name,email,ph,pas) ;
                            mRef.child(user.getPhone()).setValue(user) ;
                            Toast.makeText(Signup.this,"Create Account Complete!",
                                    Toast.LENGTH_SHORT).show();
                            hideKeyboardFrom(Signup.this,v);
                            Intent login = new Intent(Signup.this,Login.class) ;
                            startActivity(login);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent welcome = new Intent(Signup.this,Welcome.class) ;
        startActivity(welcome);
        finish();
    }
}