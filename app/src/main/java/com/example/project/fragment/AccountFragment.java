package com.example.project.fragment;

import static com.example.project.Login.hideKeyboardFrom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.project.Login;
import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.Welcome;
import com.example.project.common.Common;
import com.example.project.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AccountFragment extends Fragment implements View.OnClickListener {

    View view;
    Button logoutBtn, editProfileBtn;
    EditText aNameEdt, aEmailEdt, aPhoneEdt, aOldPassEdt, aNewPassEdt;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    User userObj;

    private static final String USER_NAME_KEY = "param1";
    private static final String USER_EMAIL_KEY = "param2";
    private static final String USER_PHONE_KEY = "param3";
    private static final String USER_PASS_KEY = "param4";

    String mParam1;
    String mParam2;
    String mParam3;
    String mParam4;

    public AccountFragment() {
        // Required empty public constructor
    }


    public static AccountFragment newInstance(String param1, String param2, String param3, String param4) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(USER_NAME_KEY, param1);
        args.putString(USER_EMAIL_KEY, param2);
        args.putString(USER_PHONE_KEY, param3);
        args.putString(USER_PASS_KEY, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(USER_NAME_KEY);
            mParam2 = getArguments().getString(USER_EMAIL_KEY);
            mParam3 = getArguments().getString(USER_PHONE_KEY);
            mParam4 = getArguments().getString(USER_PASS_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);
        // init widget option
        logoutBtn = view.findViewById(R.id.logoutBtn);
        editProfileBtn = view.findViewById(R.id.editProfileBtn);
        final TextView username = view.findViewById(R.id.username), userTel = view.findViewById(R.id.userTel);
        logoutBtn.setOnClickListener(this);
        editProfileBtn.setOnClickListener(this);
        aNameEdt = view.findViewById(R.id.aNameEdt);
        aEmailEdt = view.findViewById(R.id.aEmailEdt);
        aPhoneEdt = view.findViewById(R.id.aPhoneEdt);
        aOldPassEdt = view.findViewById(R.id.aOldPassEdt);
        aNewPassEdt = view.findViewById(R.id.aNewPassEdt);

        aPhoneEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Can't change phone number.",Toast.LENGTH_SHORT).show(); ;
            }
        });


        Bundle bundle = getArguments();
        userObj = new User(bundle.getString("username"), bundle.getString("useremail"),
                bundle.getString("userphone"), bundle.getString("userpass"));
        username.setText(userObj.getName());
        userTel.setText(userObj.getPhone());
        aPhoneEdt.setText(userObj.getPhone());
        aNameEdt.setText(userObj.getName());
        aEmailEdt.setText(userObj.getEmail());

//        return inflater.inflate(R.layout.fragment_account, container, false);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutBtn:
                SharedPreferences preferences = this.getActivity().getSharedPreferences("isLogin", Context.MODE_PRIVATE);
                Common.currentUser = null;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("isLogin", "false");
                editor.apply();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), Welcome.class);
                startActivity(intent);
                break;
            case R.id.editProfileBtn:
                database = FirebaseDatabase.getInstance();
                mRef = database.getReference("User");
                Bundle b = getArguments();

                // all empty
                if (aNameEdt.getText().toString().equals("") || aEmailEdt.getText().toString().equals("") ||
                        aPhoneEdt.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please insert all information.",
                            Toast.LENGTH_SHORT).show();
                }
                // information full no old-password
                else if (!(aNameEdt.getText().toString().equals("") || aEmailEdt.getText().toString().equals("") ||
                        aPhoneEdt.getText().toString().equals("")) && aOldPassEdt.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Need your password to change your profile!",
                            Toast.LENGTH_SHORT).show();
                }
                // information full with old-password
                else {
                    // No new Password
                    if (aNewPassEdt.getText().toString().equals("") && !aOldPassEdt.getText().toString().equals("")) {
                        // check password
                        if (userObj.getPassword().equals(aOldPassEdt.getText().toString())) {
                            // Not change phone
                            if (userObj.getPhone().equals(aPhoneEdt.getText().toString())) {
                                mRef.child(userObj.getPhone() + "/name").setValue(aNameEdt.getText().toString());
                                mRef.child(userObj.getPhone() + "/email").setValue(aEmailEdt.getText().toString());
                                aOldPassEdt.setText("");
                                aNewPassEdt.setText("");
                                Intent mainActivity = new Intent(getActivity(),MainActivity.class) ;
                                startActivity(mainActivity);
                                getActivity().finish();
                            }
                            // Change phone(Not use now)
                            else if (!userObj.getPhone().equals(aPhoneEdt.getText().toString())) {
                                // new user with new phone/key
                                User user = new User(aNameEdt.getText().toString(), aEmailEdt.getText().toString(),
                                        aPhoneEdt.getText().toString(), userObj.getPassword());
                                // push new user to firebase
                                mRef.child(user.getPhone()).setValue(user);
                                // change current user in app
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences
                                        ("currentUser",Context.MODE_PRIVATE) ;
                                editor = sharedPreferences.edit();
                                editor.putString("CurrentUser",user.getPhone()) ;
                                editor.apply();
                                // remove old phone/key
                                mRef.child(userObj.getPhone()).removeValue();
                                // reset Activity
                                Intent intent1 = getActivity().getIntent() ;
                                getActivity().finish();
                                startActivity(intent1);
                            }
                        }else{
                            Toast.makeText(getActivity(),"Incorrect password please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (!aNewPassEdt.getText().toString().equals("") &&
                            !aOldPassEdt.getText().toString().equals("")) {
                        // check password
                        if (userObj.getPassword().equals(aOldPassEdt.getText().toString())) {
                            // Not change phone
                            if (userObj.getPhone().equals(aPhoneEdt.getText().toString())) {
                                mRef.child(userObj.getPhone() + "/name").setValue(aNameEdt.getText().toString());
                                mRef.child(userObj.getPhone() + "/email").setValue(aEmailEdt.getText().toString());
                                mRef.child(userObj.getPhone() + "/password").setValue(aNewPassEdt.getText().toString()) ;
                                aOldPassEdt.setText("");
                                aNewPassEdt.setText("");

                                Intent mainActivity = new Intent(getActivity(),MainActivity.class) ;
                                startActivity(mainActivity);
                                getActivity().finish();
                            }
                            // Change phone(Not use now)
                            else if (!userObj.getPhone().equals(aPhoneEdt.getText().toString())) {
                                mRef.child(userObj.getPhone()).removeValue();
                                User user = new User(aNameEdt.getText().toString(), aEmailEdt.getText().toString(),
                                        aPhoneEdt.getText().toString(), aNewPassEdt.getText().toString());
                                mRef.child(user.getPhone()).setValue(user);
                            }
                        }else{
                            Toast.makeText(getActivity(),"Incorrect password please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
        }
    }


}