package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.common.Common;
import com.example.project.databinding.ActivityMainBinding;
import com.example.project.fragment.AccountFragment;
import com.example.project.fragment.BookingFragment;
import com.example.project.fragment.HomeFragment;
import com.example.project.fragment.MakeOrderFragment;
import com.example.project.fragment.SettingFragment;
import com.example.project.model.OrderRecycle;
import com.example.project.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding ;
    private SharedPreferences preferences ;
    private String userKey ;
    private FirebaseDatabase database ;
    private DatabaseReference mRef ;
    public static User userObj;
    public static boolean noti ;
    int i  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        i = 0 ;
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
                    Bundle bb = new Bundle() ;
                    bb.putString("currentuser",userObj.getPhone());
                    HomeFragment homeFragment = new HomeFragment() ;
                    homeFragment.setArguments(bb);
                    replaceFragment(homeFragment);
                    break;
                case R.id.booking_nav:
                    Bundle bundle = new Bundle() ;
                    bundle.putString("currentuser",userObj.getPhone());
                    BookingFragment bookingFragment = new BookingFragment() ;
                    bookingFragment.setArguments(bundle);
                    replaceFragment(bookingFragment);
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
        notificationPush();
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment) ;
        fragmentTransaction.commit();
    }

    public void notificationPush(){
        FirebaseDatabase db ;
        DatabaseReference ref ;
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Order");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Log.i("xxx", Objects.requireNonNull(snapshot.getValue()).toString() );
//                Log.i("xxx",previousChildName) ;
                if(snapshot.child("userKey").getValue().toString().equals(userObj.getPhone())){
                    if(snapshot.child("status").getValue().toString().equals("pick up")){
                        notification("Update Order #"+ snapshot.child("orderNo").getValue().toString(),
                                "Your order going to " + snapshot.child("status").getValue().toString() +" at " +
                                        snapshot.child("pickUpDate").getValue().toString() + " " +
                                        snapshot.child("pickUpTime").getValue().toString()
                                ,R.drawable.pickup_ic, snapshot.child("orderNo").getValue().toString());
                    }
                    else if(snapshot.child("status").getValue().toString().equals("in progress")){
                        notification("Update Order #"+ snapshot.child("orderNo").getValue().toString(),
                                "Your order is " + snapshot.child("status").getValue().toString() + " now."
                                ,R.drawable.inprogress_ic,snapshot.child("orderNo").getValue().toString());
                    }
                    else if(snapshot.child("status").getValue().toString().equals("drop off")){
                        notification("Update Order #"+ snapshot.child("orderNo").getValue().toString(),
                                "Your order going to " + snapshot.child("status").getValue().toString() +" at " +
                                        snapshot.child("dropOffDate").getValue().toString() + " " +
                                        snapshot.child("dropOffTime").getValue().toString()
                                ,R.drawable.dropoff_ic,snapshot.child("orderNo").getValue().toString());
                    }
                    else if(snapshot.child("status").getValue().toString().equals("Canceled")){
                        notification("Update Order #"+ snapshot.child("orderNo").getValue().toString(),
                                "Your order is Canceled"
                                ,R.drawable.ic_baseline_cancel_24,snapshot.child("orderNo").getValue().toString());
                    }
                    else if(snapshot.child("status").getValue().toString().equals("done")){
                        notification("Update Order #"+ snapshot.child("orderNo").getValue().toString(),
                                "Your order is successfully drop off."
                                ,R.drawable.done_ic,snapshot.child("orderNo").getValue().toString());
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public  void notification(String title,String text,int icon,String orderNum){
        if(!noti){
            return;
        }
        if (i > 10){
            i = 1 ;
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("MyLuanNoti"
                    ,"MyLuanNoti", NotificationManager.IMPORTANCE_DEFAULT) ;
            NotificationManager manager = getSystemService(NotificationManager.class) ;
            manager.createNotificationChannel(notificationChannel);
        }
        Intent intent = new Intent(this, OrderCustomerInfo.class);


        intent.putExtra("orderKey123",orderNum) ;
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"MyLuanNoti") ;
        builder.setContentTitle(title) ;
        builder.setContentText(text) ;
        builder.setSmallIcon(icon) ;
        builder.setAutoCancel(true) ;
        builder.setContentIntent(pendingIntent) ;
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this) ;
        managerCompat.notify(i,builder.build());
        i++ ;
    }
}