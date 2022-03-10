package com.buddies.catchwo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.buddies.catchwo.Adapter.MyViewPagerAdapter;
import com.buddies.catchwo.Model.Professional;
import com.buddies.catchwo.R;
import com.buddies.catchwo.Support.Common;
import com.buddies.catchwo.Support.NonSwipViewPager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    StepView step;
    NonSwipViewPager view;
    Button pre, nxt;
    LottieAnimationView loading;
    LocalBroadcastManager localBroadcastManager;
    CollectionReference reference;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int step = intent.getIntExtra(Common.KEY_STEP,0);
            if(step == 1){
                Common.currentProf = intent.getParcelableExtra(Common.KEY_PROF_PERSON);
            }else if(step == 2){
                Common.CurrentPer = intent.getParcelableExtra(Common.KEY_PROF_PERSON_SELECTED);
            }else if(step == 3){
                Common.CurrentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT, -1);
            }

            nxt.setEnabled(true);
            setColorButton();
        }
    };
    public ProFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProFragment newInstance(String param1, String param2) {
        ProFragment fragment = new ProFragment();
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
    public void onDestroy() {

        localBroadcastManager.unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pro, container, false);

        step = root.findViewById(R.id.step);
        view = root.findViewById(R.id.view);
        pre = root.findViewById(R.id.pre);
        nxt = root.findViewById(R.id.nxt);
        loading = root.findViewById(R.id.loading);

        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(broadcastReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), ""+Common.currentProf.getName(), Toast.LENGTH_SHORT).show();

                if (Common.step < 3 || Common.step == 0) {
                    Common.step++;
                    if (Common.step == 1) {
                        if (Common.currentProf != null) {
                            loadProf(Common.currentProf.getName());
                        }

                    } else if (Common.step == 2) {
                        if (Common.CurrentPer != null) {
                            loadTimeSlot(Common.CurrentPer.getProfessionId());
                        }
                    }else if (Common.step == 3) {
                        if (Common.CurrentTimeSlot != -1) {
                            confirmBooking();
                        }
                    }
                    view.setCurrentItem(Common.step);
//                    nxt.setEnabled(false);
                }

            }
        });

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.step == 3 || Common.step > 0) {
                    Common.step--;
                    view.setCurrentItem(Common.step);

                    if(Common.step < 3){

                        nxt.setEnabled(true);
                        setColorButton();

                    }

                }
            }
        });

        setupStepView();
        setColorButton();


        //View

        view.setAdapter(new MyViewPagerAdapter(getFragmentManager()));
        view.setOffscreenPageLimit(4);
        view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i) {

            }

            @Override
            public void onPageSelected(int position) {

                step.go(position, true);

                if(position == 0){
                    pre.setEnabled(false);
                }else{
                    pre.setEnabled(true);
                }

                nxt.setEnabled(false);

                setColorButton();

            }

            @Override
            public void onPageScrollStateChanged(int position) {

            }
        });


        return root;
    }

    private void confirmBooking() {

        Intent intent = new Intent(Common.KEY_CONFIRM_BOOKING);
        localBroadcastManager.sendBroadcast(intent);

    }

    private void loadTimeSlot(String professionId) {

        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);

    }

    private void loadProf(String id) {

        loading.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(Common.city)) {
            reference = FirebaseFirestore.getInstance()
                    .collection("AllAppoints")
                    .document(Common.city)
                    .collection("Appoints")
                    .document(id)
                    .collection("Professionals");

            reference.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            ArrayList<Professional> professionals = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                Professional profModel = documentSnapshot.toObject(Professional.class);
                                profModel.setPassword("");
                                profModel.setProfessionId(documentSnapshot.getId());

                                professionals.add(profModel);

                            }

                            Intent intent = new Intent(Common.KEY_PROF_LOAD_DONE);
                            intent.putParcelableArrayListExtra(Common.KEY_PROF_LOAD_DONE, professionals);
                            localBroadcastManager.sendBroadcast(intent);
                            loading.setVisibility(View.GONE);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loading.setVisibility(View.GONE);
                }
            });

        }

    }

    private void setColorButton() {

        if (nxt.isEnabled()) {
            nxt.setBackgroundResource(R.color.teal_700);
        } else {
            nxt.setBackgroundResource(R.color.teal_200);
        }

        if (pre.isEnabled()) {
            pre.setBackgroundResource(R.color.pink);
        } else {
            pre.setBackgroundResource(R.color.app);
        }
    }


    private void setupStepView() {

        List<String> stepList = new ArrayList<>();
        stepList.add("Place");
        stepList.add("Appoints");
        stepList.add("Time");
        stepList.add("Confirm");
        step.setSteps(stepList);


    }
}