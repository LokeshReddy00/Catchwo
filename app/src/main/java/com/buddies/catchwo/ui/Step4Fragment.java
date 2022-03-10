package com.buddies.catchwo.ui;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buddies.catchwo.Model.BookingInfoModel;
import com.buddies.catchwo.R;
import com.buddies.catchwo.SignUp3rdClass;
import com.buddies.catchwo.SplashScreen;
import com.buddies.catchwo.Support.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Step4Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step4Fragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static Step4Fragment instance;
    SimpleDateFormat simpleDateFormat;
    String url = "http://catchwo.com/appointinfo.php";
    TextInputEditText query;
    Button comfirm;
    String name, phone,gmail;
    LottieAnimationView loading;
    LocalBroadcastManager localBroadcastManager;
    private TextView time, prof, name_per, call, location;
    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setDataText();
        }
    };
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Step4Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Step4Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Step4Fragment newInstance(String param1, String param2) {
        Step4Fragment fragment = new Step4Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Step4Fragment getInstance() {

        if (instance == null) {
            instance = new Step4Fragment();
        }

        return instance;
    }

    private void setDataText() {

        name_per.setText(Common.CurrentPer.getName() + " " + Common.currentProf.getProf());
        location.setText(Common.CurrentPer.getAddress());
        time.setText(new StringBuilder(Common.convertTimeSlotToString(Common.CurrentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(Common.BookingTime.getTime())));

        call.setText(Common.CurrentPer.getPhone());
        prof.setText(name);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver, new IntentFilter(Common.KEY_CONFIRM_BOOKING));
    }

    @Override
    public void onDestroy() {

        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);

        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_step4, container, false);


        time = root.findViewById(R.id.time);
        prof = root.findViewById(R.id.prof);
        name_per = root.findViewById(R.id.name_per);
        call = root.findViewById(R.id.call);
        location = root.findViewById(R.id.location);
        comfirm = root.findViewById(R.id.comfirm);
        query = root.findViewById(R.id.query);
        loading = root.findViewById(R.id.loading);

        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addDataToServer();

                loading.setVisibility(View.VISIBLE);


                String startTime = Common.convertTimeSlotToString(Common.CurrentTimeSlot);
                String[] convertTime = startTime.split("-");
                String[] startTimeConvert = convertTime[0].split(":");
                int startHourInt = Integer.parseInt(startTimeConvert[0].trim());
                int startMinInt = Integer.parseInt(startTimeConvert[1].trim());

                Calendar bookingDateWithourHouse = Calendar.getInstance();
                bookingDateWithourHouse.setTimeInMillis(Common.BookingTime.getTimeInMillis());
                bookingDateWithourHouse.set(Calendar.HOUR_OF_DAY, startHourInt);
                bookingDateWithourHouse.set(Calendar.MINUTE, startMinInt);

                Timestamp timestamp = new Timestamp(bookingDateWithourHouse.getTime());


                BookingInfoModel bookingInfoModel = new BookingInfoModel();

                bookingInfoModel.setTimestamp(timestamp);

                bookingInfoModel.setDone(false);
                bookingInfoModel.setPersonId(Common.CurrentPer.getProfessionId());
                bookingInfoModel.setPersonName(Common.CurrentPer.getName());
                bookingInfoModel.setPersonAddress(Common.CurrentPer.getAddress());
                bookingInfoModel.setProfId(Common.currentProf.getName());
                bookingInfoModel.setProfName(Common.currentProf.getProf());
                bookingInfoModel.setPersonName(Common.CurrentPer.getName());
                bookingInfoModel.setCustomerName(name);
                bookingInfoModel.setCustomerPhone(phone);
                if (!TextUtils.isEmpty(query.getText().toString())) {
                    bookingInfoModel.setQuery(query.getText().toString());
                } else {
                    bookingInfoModel.setQuery("Non");
                }
                bookingInfoModel.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.CurrentTimeSlot))
                        .append(" at ")
                        .append(simpleDateFormat.format(bookingDateWithourHouse.getTime())).toString());
                bookingInfoModel.setSlot(Long.valueOf(Common.CurrentTimeSlot));

                DocumentReference reference = FirebaseFirestore.getInstance()
                        .collection("AllAppoints")
                        .document(Common.city)
                        .collection("Appoints")
                        .document(Common.currentProf.getName())
                        .collection("Professionals")
                        .document(Common.CurrentPer.getProfessionId())
                        .collection(Common.sampleDataFormat.format(Common.BookingTime.getTime()))
                        .document(String.valueOf(Common.CurrentTimeSlot));

                reference.set(bookingInfoModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

//                        addDataToServer();

                        addToUserBooking(bookingInfoModel);


                        // Add the Data to Calendar

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        return root;
    }

    private void addToUserBooking(BookingInfoModel bookingInfoModel) {

        CollectionReference userbooking = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(phone)
                .collection("Booking");

        userbooking.whereEqualTo("done", false)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.getResult().isEmpty()) {
                    userbooking.document().set(bookingInfoModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            loading.setVisibility(View.GONE);

                            addToCalender(Common.BookingTime, Common.convertTimeSlotToString(Common.CurrentTimeSlot));
//                            addDataToServer();
                            resetStaticData();
                            getActivity().finish();
                            Toast.makeText(getActivity(), "Appointment Successful", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loading.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
//                    addDataToServer();
                    loading.setVisibility(View.GONE);
                    resetStaticData();
                    getActivity().finish();
                    Toast.makeText(getActivity(), "Appointment Successful", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    private void addDataToServer() {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                getToken();
//                Intent intent = new Intent(SignUp3rdClass.this, SplashScreen.class);
//                startActivity(intent);
//
                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity() ,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                String startTime = Common.convertTimeSlotToString(Common.CurrentTimeSlot);
                String[] convertTime = startTime.split("-");
                String[] startTimeConvert = convertTime[0].split(":");
                int startHourInt = Integer.parseInt(startTimeConvert[0].trim());
                int startMinInt = Integer.parseInt(startTimeConvert[1].trim());

                Calendar bookingDateWithourHouse = Calendar.getInstance();
                bookingDateWithourHouse.setTimeInMillis(Common.BookingTime.getTimeInMillis());
                bookingDateWithourHouse.set(Calendar.HOUR_OF_DAY, startHourInt);
                bookingDateWithourHouse.set(Calendar.MINUTE, startMinInt);

                Timestamp timestamp = new Timestamp(bookingDateWithourHouse.getTime());



                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("Cuid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                hashMap.put("customername",name);
                hashMap.put("customerphone",phone);
                hashMap.put("done","false");
                hashMap.put("personname",Common.CurrentPer.getName());
                hashMap.put("peronaddress",Common.CurrentPer.getAddress());
                hashMap.put("profid",Common.currentProf.getName());
                hashMap.put("profname",Common.currentProf.getProf());
                hashMap.put("slot", String.valueOf(Common.CurrentTimeSlot));
                hashMap.put("time",new StringBuilder(Common.convertTimeSlotToString(Common.CurrentTimeSlot))
                        .append(" at ")
                        .append(simpleDateFormat.format(bookingDateWithourHouse.getTime())).toString());
                hashMap.put("timestamp", String.valueOf(timestamp));
                hashMap.put("place",Common.city);
                hashMap.put("uid",Common.CurrentPer.getUid());
                hashMap.put("Date",simpleDateFormat.format(bookingDateWithourHouse.getTime()));
                if(Objects.requireNonNull(query.getText()).toString().isEmpty()){
                    hashMap.put("query","NON");
                }else {
                    hashMap.put("query",query.getText().toString());
                }


                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);




    }

    private void addToCalender(Calendar bookingTime, String startDate) {

        String startTime = Common.convertTimeSlotToString(Common.CurrentTimeSlot);
        String[] convertTime = startTime.split("-");
        String[] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim());
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim());

        String[] endTimeConvert = convertTime[1].split(":");
        int endHourInt = Integer.parseInt(endTimeConvert[0].trim());
        int endMinInt = Integer.parseInt(endTimeConvert[1].trim());

        Calendar startEvent = Calendar.getInstance();
        startEvent.setTimeInMillis(bookingTime.getTimeInMillis());
        startEvent.set(Calendar.HOUR_OF_DAY, startHourInt);
        startEvent.set(Calendar.MINUTE, startMinInt);

        Calendar endEvent = Calendar.getInstance();
        endEvent.setTimeInMillis(bookingTime.getTimeInMillis());
        endEvent.set(Calendar.HOUR_OF_DAY, endHourInt);
        endEvent.set(Calendar.MINUTE, endMinInt);

        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
        String startEventTime = calendarDateFormat.format(startEvent.getTime());
        String endEventTime = calendarDateFormat.format(endEvent.getTime());

        addToDeviceCalendar(startEventTime, endEventTime, "Appointment Booking",
                new StringBuilder(Common.currentProf.getProf()).append(" From ").append(startTime)
                        .append(" With ").append(Common.CurrentPer.getName()).append(" at "),
                new StringBuilder("Address: ").append(Common.CurrentPer.getAddress()).toString(), startDate);
    }

    private void addToDeviceCalendar(String startEventTime, String endEventTime, String title, StringBuilder append, String location, String startDate) {

        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");

        try {
            Date start = calendarDateFormat.parse(startEventTime);
            Date end = calendarDateFormat.parse(endEventTime);

            ContentValues event = new ContentValues();

            event.put(CalendarContract.Events.CALENDAR_ID, getCalendar(getContext()));
            event.put(CalendarContract.Events.TITLE, title);
            event.put(CalendarContract.Events.DESCRIPTION, String.valueOf(append));
            event.put(CalendarContract.Events.EVENT_LOCATION, location);


            event.put(CalendarContract.Events.DTSTART, start.getTime());
            event.put(CalendarContract.Events.DTEND, end.getTime());
//            event.put(CalendarContract.Events.ALL_DAY, 0);
            event.put(CalendarContract.Events.HAS_ALARM, 1);
//            event.put(CalendarContract.Events.ORIGINAL_ALL_DAY, startDate);

            String timeZone = TimeZone.getDefault().getID();
            event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);

            Uri calender;
            if (Build.VERSION.SDK_INT > 8) {
                calender = Uri.parse("content://com.android.calendar/calendars");
            } else {
                calender = Uri.parse("content://calendar/events");
            }


            getActivity().getContentResolver().insert(calender, event);


        } catch (ParseException e) {
            e.printStackTrace();
        }




    }

    private String getCalendar(Context context) {

        String gmailIdCalender = gmail;
        String[] projection = {"_id", "calendar_displayName"};
        Uri calender = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = context.getContentResolver();

        Cursor managesCursor = contentResolver.query(calender, projection, null, null, null);

        if (managesCursor.moveToFirst()) {

            String callName;
            int nameCol = managesCursor.getColumnIndex(projection[1]);
            int idCol = managesCursor.getColumnIndex(projection[0]);
            do {
                callName = managesCursor.getString(nameCol);
                if (callName.contains("@gmail.com")) {

                    gmailIdCalender = managesCursor.getString(idCol);
                    break;
                }
            } while (managesCursor.moveToNext());
            managesCursor.close();

        }

        return gmailIdCalender;
    }

    private void resetStaticData() {

        Common.step = 0;
        Common.currentProf = null;
        Common.CurrentPer = null;
        Common.CurrentTimeSlot = -1;
        Common.BookingTime.add(Calendar.DATE, 0);

    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                name = (String) snapshot.child("name").getValue();
                phone = (String) snapshot.child("phone").getValue();
                gmail = (String) snapshot.child("email").getValue();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}