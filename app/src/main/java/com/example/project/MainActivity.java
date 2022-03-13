package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.project.common.Common;
import com.example.project.databinding.ActivityMainBinding;
import com.example.project.fragment.AccountFragment;
import com.example.project.fragment.BookingFragment;
import com.example.project.fragment.HomeFragment;
import com.example.project.fragment.SettingFragment;
import com.example.project.model.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

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
                    replaceFragment(new AccountFragment());
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