package com.buddies.catchwo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.buddies.catchwo.Adapter.MyTimeSlotAdapter;
import com.buddies.catchwo.Model.TimeSlot;
import com.buddies.catchwo.R;
import com.buddies.catchwo.Support.Common;
import com.buddies.catchwo.Support.ITimeSlotLoadListener;
import com.buddies.catchwo.Support.SpaceItenDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Step3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step3Fragment extends Fragment implements ITimeSlotLoadListener {


    DocumentReference reference;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    RecyclerView time_slot;
    LottieAnimationView loading;
    HorizontalCalendarView calender;
    LocalBroadcastManager localBroadcastManager;
    SimpleDateFormat s;

    BroadcastReceiver displayTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE,0);
            loadAvaailableTimeSlote(Common.CurrentPer.getProfessionId(),s.format(date.getTime()));

        }
    };

    private void loadAvaailableTimeSlote(String professionId, String format) {

        loading.setVisibility(View.VISIBLE);

        // /AllAppoints/Tirupati/Appoints/5KYPLy20oZyweUUcG0dR/Professionals/YbwvExdWGgpMhnrRBdYq

        reference = FirebaseFirestore.getInstance()
                .collection("AllAppoints")
                .document(Common.city)
                .collection("Appoints")
                .document(Common.currentProf.getName())
                .collection("Professionals")
                .document(Common.CurrentPer.getProfessionId());

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    DocumentSnapshot documentReference = task.getResult();
                    if(documentReference.exists()){

                        CollectionReference date = FirebaseFirestore.getInstance()
                                .collection("AllAppoints")
                                .document(Common.city)
                                .collection("Appoints")
                                .document(Common.currentProf.getName())
                                .collection("Professionals")
                                .document(Common.CurrentPer.getProfessionId())
                                .collection(format);

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful()){
                                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                                    if(queryDocumentSnapshots.isEmpty()){
                                        iTimeSlotLoadListener.TimeslotEmpty();
                                    }else {
                                        ArrayList<TimeSlot> timeSlots = new ArrayList<>();
                                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                        timeSlots.add(documentSnapshot.toObject(TimeSlot.class));
                                        iTimeSlotLoadListener.ITimeSlotLoadListenerSuccess(timeSlots);
                                        }
                                    }
                                }



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotLoadListener.ITimeSlotLoadListenerFailed(e.getMessage());
                            }
                        });

                    }
                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Step3Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Step3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Step3Fragment newInstance(String param1, String param2) {
        Step3Fragment fragment = new Step3Fragment();
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

        iTimeSlotLoadListener = this;

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTime, new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));

        s = new SimpleDateFormat("dd_MM_yyyy");

//        Common.currentTime = Calendar.getInstance();
//        Common.currentTime.add(Calendar.DATE,0);

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTime);
        super.onDestroy();
    }

    static Step3Fragment instance;

    public static Step3Fragment getInstance(){

        if(instance == null){
            instance = new Step3Fragment();
        }

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_step3, container, false);


        calender = root.findViewById(R.id.calender);
        time_slot = root.findViewById(R.id.time_slot);
        loading = root.findViewById(R.id.loading);

        init(root);

    return root;
    }

    private void init(View root) {

        time_slot.setHasFixedSize(true);
        GridLayoutManager llm = new GridLayoutManager(getActivity(),3);
        time_slot.setLayoutManager(llm);
        time_slot.addItemDecoration(new SpaceItenDecoration(8));

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE,2);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(root, R.id.calender)
                .range(startDate,endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                if(Common.BookingTime.getTimeInMillis() != date.getTimeInMillis())
                {

                    Common.BookingTime = date;
                    loadAvaailableTimeSlote(Common.CurrentPer.getProfessionId(),
                            s.format(date.getTime()));

                }


            }
        });

    }

    @Override
    public void ITimeSlotLoadListenerSuccess(ArrayList<TimeSlot> areaNameList) {

        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getActivity(), areaNameList);
        time_slot.setAdapter(adapter);

        loading.setVisibility(View.GONE);

    }

    @Override
    public void ITimeSlotLoadListenerFailed(String message) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void TimeslotEmpty() {


        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getActivity());
        time_slot.setAdapter(adapter);

        loading.setVisibility(View.GONE);

    }
}