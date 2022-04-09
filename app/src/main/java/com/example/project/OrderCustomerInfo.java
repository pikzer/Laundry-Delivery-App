package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.model.Order;
import com.example.project.utility.ImgFilter;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderCustomerInfo extends AppCompatActivity {
    ImageView pickupImg, inprogressImg, dropoffImg, doneImg;
    TextView orderNumInfoTV, addressDetailTV, pickupTimeInfoTV, serviceInfoImTV,
            addressDetail2, dropofftimeInfoTV, doneTV, serviceInfoTV;
    Button cancelInfoBtn;

    FirebaseDatabase database;
    DatabaseReference mRef;
    String orderNo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_customer_info);
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Order");
        Bundle extra = getIntent().getExtras();
        orderNo = extra.getString("orderKey123") ;

        pickupImg = findViewById(R.id.pickupImg);
        inprogressImg = findViewById(R.id.inprogressImg);
        dropoffImg = findViewById(R.id.dropoffImg);
        doneImg = findViewById(R.id.doneImg);

        orderNumInfoTV = findViewById(R.id.orderNumInfoTV);
        serviceInfoTV = findViewById(R.id.serviceInfoTV);
        addressDetailTV = findViewById(R.id.addressDetailTV);
        pickupTimeInfoTV = findViewById(R.id.pickupTimeInfoTV);
        serviceInfoImTV = findViewById(R.id.serviceInfoImTV);
        addressDetail2 = findViewById(R.id.addressDetail2);
        dropofftimeInfoTV = findViewById(R.id.dropofftimeInfoTV);
        doneTV = findViewById(R.id.doneTV);

        cancelInfoBtn = findViewById(R.id.cancelInfoBtn) ;

        if (extra != null) {
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String o ;
                    if(orderNo.length() ==5){
                        o = orderNo ;
                    }
                    else {
                        o = orderNo.substring(1) ;
                    }
                    Log.i("xxx",o) ;
                    DataSnapshot snapshot1 = snapshot.child(o);
                    ArrayList<String> tempOrder = new ArrayList<>();
                    for (DataSnapshot s : snapshot1.getChildren()) {
                        tempOrder.add(s.getValue().toString());
                    }
                    String[] spit = tempOrder.get(6).split(",");
                    String[] latLngStr = {"", ""};
                    String[] lat = spit[0].split("=");
                    String[] lng = spit[1].split("=");
                    lng[1] = lng[1].substring(0, lng[1].length() - 1);
                    latLngStr[0] = lat[1];
                    latLngStr[1] = lng[1];
                    LatLng latLng = new LatLng(Double.parseDouble(latLngStr[0]),
                            Double.parseDouble(latLngStr[1]));
                    Order order = new Order(tempOrder.get(9), tempOrder.get(3), latLng, tempOrder.get(2),
                            tempOrder.get(4), tempOrder.get(5), tempOrder.get(0), tempOrder.get(1),
                            tempOrder.get(7), tempOrder.get(8));
                    showOrder(order);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    }


    public void showOrder(Order order) {
        ImgFilter.unFilter(dropoffImg);
        ImgFilter.unFilter(pickupImg);
        ImgFilter.unFilter(inprogressImg);
        ImgFilter.unFilter(doneImg);
        orderNumInfoTV.setText("#" + order.getOrderNo());
        serviceInfoImTV.setText(order.getService());
        serviceInfoTV.setText(order.getService());
        addressDetailTV.setText(order.getLocationDetail());
        addressDetail2.setText(order.getLocationDetail());
        pickupTimeInfoTV.setText(order.getPickUpDate() + " " + order.getPickUpTime());
        dropofftimeInfoTV.setText(order.getDropOffDate() + " " + order.getDropOffTime());
        cancelInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doneTV.getText().toString().equals("Canceled")){
                    Toast.makeText(OrderCustomerInfo.this, "This order has been canceled.", Toast.LENGTH_SHORT).show();
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(OrderCustomerInfo.this).create();
                    alertDialog.setTitle("Confirm to cancel order");
                    alertDialog.setMessage("Are you sure to cancel order?");
                    alertDialog.setIcon(R.drawable.ic_baseline_cancel_24);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mRef.child(order.getOrderNo()+"/status").setValue("Canceled") ;
                            finish();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });
        if (order.getStatus().equals("pick up")) {
            ImgFilter.grayscaleFilter(doneImg);
            ImgFilter.grayscaleFilter(inprogressImg);
            ImgFilter.grayscaleFilter(dropoffImg);
        } else if (order.getStatus().equals("in progress")) {
            ImgFilter.grayscaleFilter(doneImg);
            ImgFilter.grayscaleFilter(pickupImg);
            ImgFilter.grayscaleFilter(dropoffImg);
        } else if (order.getStatus().equals("drop off")) {
            ImgFilter.grayscaleFilter(doneImg);
            ImgFilter.grayscaleFilter(pickupImg);
            ImgFilter.grayscaleFilter(inprogressImg);
        } else if (order.getStatus().equals("done")) {
            ImgFilter.grayscaleFilter(dropoffImg);
            ImgFilter.grayscaleFilter(pickupImg);
            ImgFilter.grayscaleFilter(inprogressImg);
        } else if (order.getStatus().equals("Canceled")) {
            ImgFilter.grayscaleFilter(dropoffImg);
            ImgFilter.grayscaleFilter(pickupImg);
            ImgFilter.grayscaleFilter(inprogressImg);
            doneTV.setText("Canceled");
            cancelInfoBtn.setBackgroundColor(Color.GRAY);
            int ph = 0 ;
            ph = R.drawable.ic_baseline_cancel_24 ;
            doneImg.setImageResource(ph);
        }
        addressDetailTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "geo:" ;
                uri += String.valueOf(order.getPickupLocation().latitude) ;
                uri += ",";
                uri += String.valueOf(order.getPickupLocation().longitude) ;
                uri += "?q=" + order.getLocationDetail() ;
                Uri gmmIntentUri = Uri.parse(uri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
        addressDetail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "geo:" ;
                uri += String.valueOf(order.getPickupLocation().latitude) ;
                uri += ",";
                uri += String.valueOf(order.getPickupLocation().longitude) ;
                Uri gmmIntentUri = Uri.parse(uri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(this,MainActivity.class) ;
        startActivity(home);
        super.onBackPressed();
    }
}