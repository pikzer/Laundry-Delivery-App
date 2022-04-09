package com.example.project.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.example.project.MainActivity;
import com.example.project.R;




public class SettingFragment extends Fragment {
    View view ;

    Switch switch1 ;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_setting, container, false);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("NotificationMode",
                Context.MODE_PRIVATE);
        String isNotify =   preferences.getString("NotificationMode","") ;
        Log.i("xxx",isNotify) ;
        switch1 = view.findViewById(R.id.switch1) ;
        if(isNotify.equals("true")||isNotify.equals("")){
//            switch1.setText("ON");
            switch1.setChecked(true);
        }
        else if(isNotify.equals("false")){
//            switch1.setText("OFF");
            switch1.setChecked(false);
        }
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switch1.isChecked()){
//                    Log.i("xxx","check") ;
                    SharedPreferences.Editor editor1 = preferences.edit() ;
                    editor1.putString("NotificationMode","true") ;
                    editor1.apply();
                    MainActivity.noti = true ;
                }
                else{
                    MainActivity.noti = false ;
                    SharedPreferences.Editor editor1 = preferences.edit() ;
                    editor1.putString("NotificationMode","false") ;
                    editor1.apply();
//                    Log.i("xxx","notcheck");
                }
            }
        });

        return view ;
    }
}