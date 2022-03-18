package com.example.project.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.example.project.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakeOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeOrderFragment extends Fragment  implements DatePickerDialog.OnDateSetListener {

    View view ;
    private FirebaseDatabase database ;
    private DatabaseReference mRef ;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_make_order, container, false);
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
        geocoder = new Geocoder(getActivity(), Locale.getDefault()) ;
//        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());


        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("tempLocation");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Address> addressResult ;
                if(snapshot.getValue() != null){
                    LatLng latLng = new LatLng(snapshot.child("latitude").getValue(Double.class),snapshot.child("longitude").getValue(Double.class)) ;
                    try {
                        addressResult = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) ;
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
        final DatePickerDialog  pickUpDateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                pickUpDateEdt.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        final DatePickerDialog  dropOffDateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dropOffDateEdt.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        // set Min Calendar
        pickUpDateDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());
        // 1 day service is default
        Calendar nextDay = Calendar.getInstance() ;
        nextDay.add(Calendar.HOUR_OF_DAY,+2);
        dropOffDateDialog.getDatePicker().setMinDate(nextDay.getTimeInMillis());
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
                DateFormat dateFormat = new SimpleDateFormat("HH:mm") ;
                DateFormat outFormat = new SimpleDateFormat( "HH:mm");
                try {
                    Date date = dateFormat.parse(time) ;
                    if(date != null){
                        // service start from 8:00 -> 19:00)
                        if(hourOfDay >= 8 && hourOfDay <= 19){
                            if(hourOfDay == 19 && minute == 0){
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
                DateFormat dateFormat = new SimpleDateFormat("HH:mm") ;
                DateFormat outFormat = new SimpleDateFormat( "HH:mm");
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
                dropOffDateDialog.show();
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
                if(isSelectService == true){

                }
                else{
                    onSelectServiceClear();
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

}