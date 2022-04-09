package com.example.project.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project.MapsActivity;
import com.example.project.OrderCustomerInfo;
import com.example.project.R;
import com.example.project.model.Order;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MakeOrderFragment extends Fragment  implements DatePickerDialog.OnDateSetListener {

    View view ;
    private FirebaseDatabase database ;
    private DatabaseReference mRef ;

    LatLng latlngFinal ;
    Calendar pickUpTimeFinal ;
    Calendar dropOffTimeFinal ;

    Geocoder geocoder ;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MakeOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MakeOrder.
     */
    // TODO: Rename and change types and number of parameters
    public static MakeOrderFragment newInstance(String param1, String param2) {
        MakeOrderFragment fragment = new MakeOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ImageView full,washfold,washiron,dry,iron,duvet ;
    boolean isSelectService = false ;
    int i = 1 ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_make_order, container, false);
        orderNumberGen() ;
        EditText addressEdt = view.findViewById(R.id.addressEdt) ;
        EditText pickUpDateEdt = view.findViewById(R.id.pickUpDateEdt) ;
        EditText pickUpTimeEdt = view.findViewById(R.id.pickUpTimeEdt) ;
        EditText dropOffDateEdt = view.findViewById(R.id.dropOffDateEdt) ;
        EditText dropOffTimeEdt = view.findViewById(R.id.dropOffTimeEdt) ;
        EditText serviceEdt = view.findViewById(R.id.serviceEdt) ;
        ImageButton dropOffTimeBtn = view.findViewById(R.id.dropOffTimeBtn) ;
        ImageButton dropOffDateBtn = view.findViewById(R.id.dropOffDateBtn) ;
        ImageButton openMapBtn = view.findViewById(R.id.openMapBtn) ;
        ImageButton pickUpDate = view.findViewById(R.id.pickUpDate) ;
        ImageButton pickUpTime = view.findViewById(R.id.pickUpTimeButton) ;
        Button makeOrderBtn = view.findViewById(R.id.makeOrderBtn) ;
        geocoder = new Geocoder(getActivity(), Locale.getDefault()) ;

//        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        pickUpTimeFinal = Calendar.getInstance() ;
        dropOffTimeFinal = Calendar.getInstance() ;

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("tempLocation");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Address> addressResult ;
                if(snapshot.getValue() != null){
                    LatLng latLng = new LatLng(snapshot.child("latitude").getValue(Double.class),
                            snapshot.child("longitude").getValue(Double.class)) ;
                    try {
                        addressResult = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) ;
                        latlngFinal = latLng ;
                        addressEdt.setText(addressResult.get(0).getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        openMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class) ;
                startActivity(intent);
            }
        });
        //
        final Calendar newCalendar = Calendar.getInstance();
        final boolean[] isSelectDate = {false};
        final DatePickerDialog  dropOffDateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dropOffDateEdt.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        final DatePickerDialog  pickUpDateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                pickUpDateEdt.setText(dateFormatter.format(newDate.getTime()));
                // 1 day service is default
                isSelectDate[0] = true ;
                Calendar nextDay = newDate ;
                nextDay.add(Calendar.DAY_OF_WEEK,+1);
                dropOffDateDialog.getDatePicker().setMinDate(nextDay.getTimeInMillis());

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        // set Min Calendar
        pickUpDateDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());
//        // 1 day service is default
//        Calendar nextDay = Calendar.getInstance() ;
//        nextDay.add(Calendar.HOUR_OF_DAY,+2);
//        dropOffDateDialog.getDatePicker().setMinDate(nextDay.getTimeInMillis());
        // set Max Calendar (Max service is 30 day)
        Calendar nextMonth = Calendar.getInstance() ;
        nextMonth.add(Calendar.MONTH,+1);
        pickUpDateDialog.getDatePicker().setMaxDate(nextMonth.getTimeInMillis());
        nextMonth.add(Calendar.HOUR_OF_DAY,+1);
        dropOffDateDialog.getDatePicker().setMaxDate(nextMonth.getTimeInMillis());
        final TimePickerDialog pickUpTimeDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay+":"+minute ;
                Calendar c = Calendar.getInstance(Locale.getDefault());
//                SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm");
                DateFormat dateFormat = new SimpleDateFormat("HH:mm") ;
                DateFormat outFormat = new SimpleDateFormat( "HH:mm");
                try {
                    Date date = dateFormat.parse(time) ;
                    String current = dateFormat.format(c.getTime()) ;
                    Date date1 = dateFormat.parse(current) ;
//                    Date currentTime = Calendar.getInstance(Locale.getDefault());
                    if(date != null){
                        // service start from 8:00 -> 19:00)
                        if(hourOfDay >= 8 && hourOfDay <= 19){
                            if(date.before(date1)){
                                Toast.makeText(getActivity(),"Please select correct time.",Toast.LENGTH_SHORT).show();
                            }
                            else if(hourOfDay == 19 && minute == 0){
                                time = outFormat.format(date) ;
                                pickUpTimeEdt.setText(time);
                            }
                            else if(hourOfDay != 19){
                                time = outFormat.format(date) ;
                                pickUpTimeEdt.setText(time);
                            }
                            else{
                                Toast.makeText(getActivity(),"Time service is 8:00 - 19:00",Toast.LENGTH_SHORT).show(); ;
                            }
                        }
                        else {
                            Toast.makeText(getActivity(),"Time service is 8:00 - 19:00",Toast.LENGTH_SHORT).show(); ;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY),newCalendar.get(Calendar.MINUTE),true) ;

        final TimePickerDialog dropOffTimePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay+":"+minute ;
                String pattern = "HH:mm" ;
                DateFormat dateFormat = new SimpleDateFormat(pattern) ;
                DateFormat outFormat = new SimpleDateFormat(pattern);
                try {
                    Date date = dateFormat.parse(time) ;
                    if(date != null){
                        // service start from 8:00 -> 19:00)
                        if(hourOfDay >= 8 && hourOfDay <= 19){
                            if(hourOfDay == 19 && minute == 0){
                                time = outFormat.format(date) ;
                                dropOffTimeEdt.setText(time);
                            }
                            else if(hourOfDay != 19){
                                time = outFormat.format(date) ;
                                dropOffTimeEdt.setText(time);
                            }
                            else{
                                Toast.makeText(getActivity(),"Time service is 8:00 - 19:00",Toast.LENGTH_SHORT).show(); ;
                            }
                        }
                        else {
                            Toast.makeText(getActivity(),"Time service is 8:00 - 19:00",Toast.LENGTH_SHORT).show(); ;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY),newCalendar.get(Calendar.MINUTE),true) ;

        Dialog dialog = new Dialog(getActivity()) ;
        dialog.setContentView(R.layout.servicetypedialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        pickUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickUpDateDialog.show();
            }
        });
        pickUpTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickUpTimeDialog.show();
            }
        });
        dropOffDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSelectDate[0]){
                    Toast.makeText(getActivity(),"Please select pick up date",Toast.LENGTH_SHORT).show();
                }
                else{
                    dropOffDateDialog.show();
                }
            }
        });
        dropOffTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropOffTimePickerDialog.show();
            }
        });
        serviceEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.show();
            }
        });

        Button selectServiceTypeBtn  = dialog.findViewById(R.id.selectServiceTypeBtn) ;
        full = dialog.findViewById(R.id.fullImg) ;
        washfold = dialog.findViewById(R.id.washfoldImg) ;
        washiron = dialog.findViewById(R.id.washironImg) ;
        dry = dialog.findViewById(R.id.dryImg) ;
        iron = dialog.findViewById(R.id.ironImg) ;
        duvet = dialog.findViewById(R.id.duvetImg) ;
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(isSelectService){
                    dialog.dismiss();
                }
                else{
                    serviceEdt.setText("");
                }
            }
        });

        full.setOnClickListener(v -> {
            onSelectServiceClear();
            isSelectService = true ;
            full.setBackgroundColor(Color.MAGENTA);
            serviceEdt.setText("Full-Services");
        });
        washfold.setOnClickListener(v -> {
            onSelectServiceClear();
            isSelectService = true ;
            washfold.setBackgroundColor(Color.MAGENTA);
            serviceEdt.setText("Wash & Fold");
        });
        washiron.setOnClickListener(v -> {
            onSelectServiceClear();
            isSelectService = true ;
            washiron.setBackgroundColor(Color.MAGENTA);
            serviceEdt.setText("Wash & Iron");
        });
        dry.setOnClickListener(v -> {
            onSelectServiceClear();
            isSelectService = true ;
            dry.setBackgroundColor(Color.MAGENTA);
            serviceEdt.setText("Dry Cleaning");
        });
        iron.setOnClickListener(v -> {
            onSelectServiceClear();
            isSelectService = true ;
            iron.setBackgroundColor(Color.MAGENTA);
            serviceEdt.setText("Ironing");
        });
        duvet.setOnClickListener(v -> {
            onSelectServiceClear();
            isSelectService = true ;
            duvet.setBackgroundColor(Color.MAGENTA);
            serviceEdt.setText("Duvet Cleaning");
        });

        selectServiceTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        makeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!addressEdt.getText().toString().equals("") && !pickUpDateEdt.getText().toString().equals("")
                && !pickUpTimeEdt.getText().toString().equals("") && !dropOffDateEdt.getText().toString().equals("")
                && !dropOffTimeEdt.getText().toString().equals("") && !serviceEdt.getText().toString().equals("")){
                    SharedPreferences preferences  = getActivity().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
                    String userKey = preferences.getString("CurrentUser","") ;
                    FirebaseDatabase firebaseOrder = FirebaseDatabase.getInstance();
                    DatabaseReference orderDbRef = firebaseOrder.getReference("Order");
                    if(latlngFinal!=null){
//                         int id = Order.gen() ;
                        orderNumberGen();
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog.setTitle("Confirm your pick up order");
                                    alertDialog.setMessage("Are you sure to make pick up order?");
                                    alertDialog.setIcon(R.drawable.ic_baseline_done_24);
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Order order = new Order(userKey,idd,latlngFinal,addressEdt.getText().toString(),
                                                    pickUpDateEdt.getText().toString(),pickUpTimeEdt.getText().toString(),
                                                    dropOffDateEdt.getText().toString(),dropOffTimeEdt.getText().toString(),serviceEdt.getText().toString(),
                                                    "pick up") ;
                                            orderDbRef.child(order.getOrderNo()).setValue(order) ;
                                            Bundle bundle = new Bundle() ;
                                            bundle.putString("currentuser",userKey);
                                            BookingFragment bookingFragment = new BookingFragment() ;
                                            bookingFragment.setArguments(bundle);
                                            replaceFragment(bookingFragment);
                                            getActivity().getFragmentManager().popBackStack();
                                            Toast.makeText(getActivity(), "Your order has been add", Toast.LENGTH_LONG).show();
//                                            if (i > 10){
//                                                i = 1 ;
//                                            }
//                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                                                NotificationChannel notificationChannel = new NotificationChannel("MyLuanNoti"
//                                                        ,"MyLuanNoti", NotificationManager.IMPORTANCE_HIGH) ;
//                                                NotificationManager manager = getActivity().getSystemService(NotificationManager.class) ;
//                                                manager.createNotificationChannel(notificationChannel);
//                                            }
//                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(),"MyLuanNoti") ;
//                                            builder.setContentTitle("Order #"+order.getOrderNo() + "is submitted.") ;
//                                            builder.setContentText("Your order will pick up at " + order.getPickUpDate() +" " + order.getPickUpTime()) ;
//                                            builder.setSmallIcon(R.drawable.pickup_ic) ;
//                                            builder.setAutoCancel(true) ;
//                                            Intent intent = new Intent(getActivity(), OrderCustomerInfo.class);
//                                            intent.putExtra("orderKey",order.getOrderNo()) ;
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
//                                            builder.setContentIntent(pendingIntent) ;
//                                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getActivity()) ;
//                                            managerCompat.notify(i,builder.build());
//                                            i++ ;
                                        }
                                    });
                                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            alertDialog.dismiss();
                                        }
                                    });
                                    alertDialog.show();
//                        orderDbRef.child(order.
//                        getOrderNo()).setValue(order) ;
//                        orderDbRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if(snapshot.child(idd).exists()){
//                                    Toast.makeText(getActivity(),"Something wrong please try again!",Toast.LENGTH_SHORT).show();
//                                }
//                                else{
//                                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//                                    alertDialog.setTitle("Confirm your pick up order");
//                                    alertDialog.setMessage("Are you sure to make pick up order?");
//                                    alertDialog.setIcon(R.drawable.ic_baseline_done_24);
//                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            Order order = new Order(userKey,idd,latlngFinal,addressEdt.getText().toString(),
//                                                    pickUpDateEdt.getText().toString(),pickUpTimeEdt.getText().toString(),
//                                                    dropOffDateEdt.getText().toString(),dropOffTimeEdt.getText().toString(),serviceEdt.getText().toString(),
//                                                    "pick up") ;
//                                            orderDbRef.child(order.getOrderNo()).setValue(order) ;
//                                            Bundle bundle = new Bundle() ;
//                                            bundle.putString("currentuser",userKey);
//                                            BookingFragment bookingFragment = new BookingFragment() ;
//                                            bookingFragment.setArguments(bundle);
//                                            replaceFragment(bookingFragment);
//                                            getActivity().getFragmentManager().popBackStack();
//                                            Toast.makeText(getActivity(), "Your order has been add", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            alertDialog.dismiss();
//                                        }
//                                    });
//
//                                }
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
                    }
                }
                else{
                    Toast.makeText(getActivity(),"Pleas insert all information!",Toast.LENGTH_SHORT).show() ;
                }
            }
        });
        return view ;
    }

    public void onSelectServiceClear(){
        isSelectService = false ;
        full.setBackgroundColor(Color.TRANSPARENT);
        washfold.setBackgroundColor(Color.TRANSPARENT);
        washiron.setBackgroundColor(Color.TRANSPARENT);
        dry.setBackgroundColor(Color.TRANSPARENT);
        duvet.setBackgroundColor(Color.TRANSPARENT);
        iron.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    public void orderNumberGen(){
        FirebaseDatabase db ;
        DatabaseReference dbRef;
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("Order");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s = String.format("%05d",snapshot.getChildrenCount()) ;
                getOrderNum(s) ;
//                Log.i("xxx",String.valueOf(snapshot.getChildrenCount())) ;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    String idd ;
    public void getOrderNum(String s){
        idd = s ;
    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment).addToBackStack("HomeFragment") ;
        fragmentTransaction.commit();
    }
}