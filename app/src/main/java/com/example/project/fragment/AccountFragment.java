package com.example.project.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.Welcome;
import com.example.project.common.Common;
import com.example.project.model.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {

     View view ;
     Button logoutBtn,editProfileBtn  ;
     EditText aNameEdt, aEmailEdt, aPhoneEdt, aOldPassEdt, aNewPassEdt ;
//     TextView nameTextview, phoneTextview ;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);
        // init widget option
        logoutBtn = view.findViewById(R.id.logoutBtn) ;
        editProfileBtn = view.findViewById(R.id.editProfileBtn) ;
        final TextView username = view.findViewById(R.id.username), userTel = view.findViewById(R.id.userTel) ;
        logoutBtn.setOnClickListener(this);
        editProfileBtn.setOnClickListener(this);
        aNameEdt = view.findViewById(R.id.aNameEdt) ;
        aEmailEdt = view.findViewById(R.id.aEmailEdt) ;
        aPhoneEdt = view.findViewById(R.id.aPhoneEdt) ;
        aOldPassEdt = view.findViewById(R.id.aOldPassEdt) ;
        aNewPassEdt = view.findViewById(R.id.aNewPassEdt ) ;
        // Load Profile
        username.setText("0");
        userTel.setText("2");
//        Log.i("xxx",userdata.get(0)) ;
//        User user = new User(userdata.get(0),userdata.get(1),userdata.get(3)) ;
//        username.setText(user.getName());
//        userTel.setText(user.getPhone());



//        return inflater.inflate(R.layout.fragment_account, container, false);
        return view ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logoutBtn:
                SharedPreferences preferences = this.getActivity().getSharedPreferences("isLogin", Context.MODE_PRIVATE);
                Common.currentUser = null ;
                SharedPreferences.Editor editor = preferences.edit() ;
                editor.putString("isLogin","false");
                editor.apply();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), Welcome.class);
                startActivity(intent);
                break;
            case R.id.editProfileBtn:
                // TODO
                break;
        }
    }
}