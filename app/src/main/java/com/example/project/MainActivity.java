package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.common.Common;
import com.example.project.databinding.ActivityMainBinding;
import com.example.project.fragment.AccountFragment;
import com.example.project.fragment.BookingFragment;
import com.example.project.fragment.HomeFragment;
import com.example.project.fragment.SettingFragment;
import com.example.project.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMinBinding binding ;
    private SharedPreferences preferences ;
    private String userKey ;
    private FirebaseDatabase database ;
    private DatabaseReference mRef ;
    public static User userObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        // get current user
        preferences  = getSharedPreferences("CurrentUser",MODE_PRIVATE) ;
        userKey = preferences.getString("CurrentUser","") ;
        // init database
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("User");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userObj = new User(snapshot.child(userKey+"/name").getValue().toString(),
                        snapshot.child(userKey+"/email").getValue().toString(),
                        snapshot.child(userKey+"/phone").getValue().toString(),
                        snapshot.child(userKey+"/password").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home_nav:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.booking_nav:
                    replaceFragment(new BookingFragment());
                    break;
                case R.id.account_nav:
                    Bundle b = new Bundle() ;
                    b.putString("username",userObj.getName());
                    b.putString("useremail",userObj.getEmail());
                    b.putString("userphone",userObj.getPhone());
                    b.putString("userpass",userObj.getPassword());
                    AccountFragment a = new AccountFragment() ;
                    a.setArguments(b);
                    replaceFragment(a);
                    break;
                case R.id.setting_nav:
                    replaceFragment(new SettingFragment());
                    break;
            }
            return true ;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment) ;
        fragmentTransaction.commit();
    }
}