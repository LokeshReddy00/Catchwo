package com.buddies.catchwo.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.buddies.catchwo.InformationActivity;
import com.buddies.catchwo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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

    TextView verify,appoint;
    Switch app_sw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);

        verify = view.findViewById(R.id.verify);
        appoint = view.findViewById(R.id.appoint);
        app_sw = view.findViewById(R.id.app_sw);

        app_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(app_sw.isChecked()){
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String app = ""+snapshot.child("appoint").getValue();

                                    if(app.equals("") || app.isEmpty()){
                                        Intent intent = new Intent(getActivity(), AppointFragment.class);
                                        startActivity(intent);
                                    }else{

                                        HashMap<String, Object> has = new HashMap<>();
                                        has.put("appoints","Open");

                                        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference();
                                        reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(has);
                                    }



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }else if(!app_sw.isChecked()){
                    HashMap<String, Object> has = new HashMap<>();
                    has.put("appoints","Closed");

                    DatabaseReference reference  = FirebaseDatabase.getInstance().getReference();
                    reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(has);
                }
            }
        });



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String app = ""+snapshot.child("appoints").getValue();

                        if(app.equals("Open")){
                           app_sw.setChecked(true);
                        }else{
                            app_sw.setChecked(false);
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}