package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.project.adapter.Adapter;
import com.example.project.common.Common;
import com.example.project.model.OrderRecycle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dashboard extends AppCompatActivity implements Adapter.OnNoteListener {

    RecyclerView recyclerView ;
    Adapter adapter ;
    LinearLayoutManager layoutManager ;
    ArrayList<OrderRecycle> orderRecycleList ;
    FirebaseDatabase database;
    DatabaseReference mRef ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Order");
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        intiData() ;
    }

    public void onclk(View view){
        SharedPreferences preferences = getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        Common.currentUser = null;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("isLogin", "false");
        editor.apply();
        finish();
        Intent intent = new Intent(Dashboard.this, Welcome.class);
        startActivity(intent);
    }

    public void intiData(){
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> c = new ArrayList<>() ;
                ArrayList<OrderRecycle> o = new ArrayList<>() ;
                int i = 0 ;
                for (DataSnapshot s:snapshot.getChildren()) {
                    for (DataSnapshot a :s.getChildren()){
                        c.add(a.getValue().toString()) ;
                    }
                    int ph = 0 ;
                    String a="" ;
                    if(c.get(8+i).equals("pick up")){
                        a = "pick up at:" ;
                        ph = R.drawable.pickup_ic ;
                    }
                    else if(c.get(8+i).equals("drop off")){
                        a = "drop of at:" ;
                        ph = R.drawable.dropoff_ic ;
                    }
                    else if(c.get(8+i).equals("in progress")){
                        a ="drop of at:" ;
                        ph = R.drawable.inprogress_ic ;
                    }
                    else if(c.get(8+i).equals("done")){
                        a = "done" ;
                        ph = R.drawable.done_ic ;
                    }
                    else if(c.get(8+i).equals("Canceled")){
                        a = "Canceled" ;
                        ph = R.drawable.ic_baseline_cancel_24 ;
                    }
                    else{
                        ph = R.mipmap.ic_launcher ;
                    }
                    String time="" ;
                    if(c.get(8+i).equals("pick up")){
                        time = c.get(4+i) + " " + c.get(5+i) ;
                    }
                    else{
                        time = c.get(0+i) + " " + c.get(1+i) ;
                    }
                    o.add(new OrderRecycle("#"+c.get(3+i),c.get(7+i),a,
                            time,c.get(8+i),ph)) ;
                    i+=10 ;
                }
                initRecyclerview(o);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void initRecyclerview(ArrayList<OrderRecycle> orderRecycles){
        Collections.reverse(orderRecycles);
        orderRecycleList = orderRecycles ;
        recyclerView = findViewById(R.id.recycleView2) ;
        layoutManager = new LinearLayoutManager(this) ;
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(orderRecycles,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(int pos) {
        Intent EditOrderDashboard = new Intent(this, EditOrderDashboard.class);
        EditOrderDashboard.putExtra("orderKey",orderRecycleList.get(pos).getOrderNumTV()) ;
        startActivity(EditOrderDashboard);
    }
}