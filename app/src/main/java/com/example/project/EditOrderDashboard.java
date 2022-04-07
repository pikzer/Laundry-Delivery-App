package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.project.model.Order;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.util.ArrayList;

public class EditOrderDashboard extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference mRef ;

    EditText orderNoDashEdt, addressDashEdt, pickupDashEdt, dropOffDashEdt, serviceDashEdt,statusNowDashEdt ;
    Button cancelBtn, inprogressBtn, dropOffBtn, doneBtn, getDirectBtn,pickupDashBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order_dashboard);
        Bundle extra = getIntent().getExtras() ;
        getSupportActionBar().hide();

        orderNoDashEdt = findViewById(R.id.OrderNoDashEdt) ;
        addressDashEdt = findViewById(R.id.addressDashEdt) ;
        pickupDashEdt = findViewById(R.id.pickupDashEdt) ;
        dropOffDashEdt = findViewById(R.id.dropOffDashEdt) ;
        serviceDashEdt = findViewById(R.id.serviceDashEdt) ;
        statusNowDashEdt = findViewById(R.id.statusNowDashEdt) ;

        pickupDashBtn = findViewById(R.id.pickupDashBtn) ;
        cancelBtn = findViewById(R.id.cancelBtn) ;
        inprogressBtn = findViewById(R.id.inprogressBtn) ;
        dropOffBtn = findViewById(R.id.dropOffBtn) ;
        doneBtn = findViewById(R.id.doneBtn) ;
        getDirectBtn = findViewById(R.id.getDirectBtn) ;

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Order");
        if(extra != null){
            String orderNo = extra.getString("orderKey") ;
            String o = orderNo.substring(1) ;
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    DataSnapshot snapshot1 = snapshot.child(o) ;
                    ArrayList<String> tempOrder = new ArrayList<>() ;
                    for(DataSnapshot s : snapshot1.getChildren()){
                        tempOrder.add(s.getValue().toString()) ;
                    }
                    String [] spit = tempOrder.get(6).split(",");
                    String [] latLngStr = {"",""} ;
                    String[] lat = spit[0].split("=") ;
                    String[] lng = spit[1].split("=") ;
                    lng[1] = lng[1].substring(0,lng[1].length()-1) ;
                    latLngStr[0] = lat[1] ;
                    latLngStr[1] = lng[1] ;
                    LatLng latLng = new LatLng(Double.parseDouble(latLngStr[0]),
                            Double.parseDouble(latLngStr[1])) ;
                    Order order = new Order(tempOrder.get(9),tempOrder.get(3),latLng,tempOrder.get(2),
                            tempOrder.get(4),tempOrder.get(5),tempOrder.get(0),tempOrder.get(1),
                            tempOrder.get(7),tempOrder.get(8)) ;
                    showOrder(order);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void showOrder(Order order){
        orderNoDashEdt.setText(order.getOrderNo());
        addressDashEdt.setText(order.getLocationDetail());
        pickupDashEdt.setText(order.getPickUpDate()+ " at " +order.getPickUpTime());
        dropOffDashEdt.setText(order.getDropOffDate() + " at " + order.getDropOffTime());
        serviceDashEdt.setText(order.getService());
        statusNowDashEdt.setText(order.getStatus());
        getDirectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pars = "google.navigation:q=" + (order.getPickupLocation().latitude)+
                        ","+ order.getPickupLocation().longitude +"&mode=d" ;
                Intent navActivity = new Intent(Intent.ACTION_VIEW, Uri.parse(pars)) ;
                navActivity.setPackage("com.google.android.apps.maps") ;
                if(navActivity.resolveActivity(getPackageManager())!= null){
                    startActivity(navActivity);
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child(order.getOrderNo()+"/status").setValue("Canceled") ;
            }
        });
        pickupDashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child(order.getOrderNo()+"/status").setValue("pick up") ;
            }
        });
        inprogressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child(order.getOrderNo()+"/status").setValue("in progress") ;
            }
        });
        dropOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child(order.getOrderNo()+"/status").setValue("drop off") ;
            }
        });
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child(order.getOrderNo()+"/status").setValue("done") ;
            }
        });
    }

}