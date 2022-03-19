package com.example.project.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.R;
import com.example.project.adapter.Adapter;
import com.example.project.model.Order;
import com.example.project.model.OrderRecycle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookingFragment extends Fragment {
    View view ;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookingFragment.
     */
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_booking, container, false);


        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Order");
        initData();
        initRecyclerview() ;
        return view ;

    }



    public void initData(){
        orderRecycleList = new ArrayList<>();
//        List<Order> orders = new ArrayList<>() ;
        orderRecycleList.add(new OrderRecycle("00000","Ironing","Pick up at",
                "22/22/22 11:44","In progress",R.drawable.book_ic)) ;
        orderRecycleList.add(new OrderRecycle("00001","Full-Service","Drop off at",
                "22/22/22 11:44","Waiting for pick up",R.drawable.ic_baseline_calendar_today_24)) ;
        orderRecycleList.add(new OrderRecycle("00002","Ironing","Pick up at",
                "22/22/22 11:44","Waiting for drop off",R.drawable.ic_baseline_add_location_24)) ;
        orderRecycleList.add(new OrderRecycle("00002","Ironing","Pick up at",
                "22/22/22 11:44","Done",R.drawable.ic_baseline_push_pin_24)) ;
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public void initRecyclerview(){
        recyclerView = view.findViewById(R.id.recycleView) ;
        layoutManager = new LinearLayoutManager(getActivity()) ;
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(orderRecycleList );
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}