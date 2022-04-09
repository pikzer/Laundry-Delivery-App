package com.example.project.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.EditOrderDashboard;
import com.example.project.R;
import com.example.project.adapter.Adapter;
import com.example.project.model.OrderRecycle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookingFragment extends Fragment implements Adapter.OnNoteListener {
    View view ;
    SharedPreferences sharedPreferences ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BookingFragment() {
        // Required empty public constructor
    }

    ArrayList<OrderRecycle> orderRecycleArrayList ;
    // TODO: Rename and change types and number of parameters
    public static BookingFragment newInstance(String param1, String param2) {
        BookingFragment fragment = new BookingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        getActivity().setContentView(R.layout.fragment_booking);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    RecyclerView recyclerView ;
    Adapter adapter ;
    LinearLayoutManager layoutManager ;
    List<OrderRecycle> orderRecycleList ;
    FirebaseDatabase database;
    DatabaseReference mRef ;
    String currentUser ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_booking, container, false);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Order");
        initData();
        Bundle bundle = getArguments() ;
        if(bundle != null){
            currentUser = bundle.getString("currentuser");
        }
        return view ;
    }

    public void initData(){
        orderRecycleList = new ArrayList<>();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> c = new ArrayList<>() ;
                ArrayList<OrderRecycle> o = new ArrayList<>() ;
                int i = 0 ;

                for (DataSnapshot s:snapshot.getChildren()) {
                    if(!s.child("userKey").getValue().toString().equals(currentUser)){
                        continue;
                    }
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
        orderRecycleArrayList = orderRecycles ;
        recyclerView = view.findViewById(R.id.recycleView) ;
        layoutManager = new LinearLayoutManager(getActivity()) ;
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(orderRecycles, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }





    @Override
    public void onNoteClick(int pos) {
        Intent orderDashBoard = new Intent(getActivity(), com.example.project.OrderCustomerInfo.class);
        orderDashBoard.putExtra("orderKey123",orderRecycleArrayList.get(pos).getOrderNumTV()) ;
        startActivity(orderDashBoard);
    }
}