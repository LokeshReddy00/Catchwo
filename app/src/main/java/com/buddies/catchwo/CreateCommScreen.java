package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateCommScreen extends AppCompatActivity {

    private CheckBox public_comm,private_comm;
    private TextInputEditText comm_desc,name_comm,phone,email;
    private String name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comm_screen);

        private_comm = findViewById(R.id.private_comm);
        comm_desc = findViewById(R.id.comm_desc);
        public_comm = findViewById(R.id.public_comm);
        name_comm = findViewById(R.id.name_comm);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        Button profile = findViewById(R.id.profile);
        Button create = findViewById(R.id.create);

        private_comm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(public_comm.isChecked()) {
                public_comm.setChecked(false);
            }
        });

        public_comm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(private_comm.isChecked()) {
                private_comm.setChecked(false);
            }
        });

        profile.setOnClickListener(v -> loadUserData());

        create.setOnClickListener(v -> CreateCommittee());

    }

    private void CreateCommittee() {

        if(TextUtils.isEmpty(Objects.requireNonNull(phone.getText()).toString()) && TextUtils.isEmpty(Objects.requireNonNull(email.getText()).toString()) &&
                TextUtils.isEmpty(Objects.requireNonNull(comm_desc.getText()).toString()) && TextUtils.isEmpty(Objects.requireNonNull(name_comm.getText()).toString())) {
            Toast.makeText(CreateCommScreen.this, "Fill All the Details", Toast.LENGTH_SHORT).show();
        }else if(!private_comm.isChecked() && !public_comm.isChecked()) {
            Toast.makeText(CreateCommScreen.this, "Please Select the Committee Type", Toast.LENGTH_SHORT).show();
        }else {
            Calendar cdate = Calendar.getInstance();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

            final String savedate = currentdates.format(cdate.getTime());

            Calendar ctime = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

            final String savetime = currenttime.format(ctime.getTime());

            String time = savedate + ":" + savetime;

            Long times = System.currentTimeMillis();


            Map<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("uid", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            hashMap.put("createdBy",name);
            hashMap.put("time",time);
            hashMap.put("email",email.getText().toString());
            hashMap.put("comm_name",name_comm.getText().toString());
            hashMap.put("desc_comm",comm_desc.getText().toString());
            hashMap.put("phone",phone.getText().toString());
            hashMap.put("comId", String.valueOf(times));
            hashMap.put("id", String.valueOf(times));

            if(public_comm.isChecked()){
                hashMap.put("type","Public");

            }else if(private_comm.isChecked()){
                hashMap.put("type","Private");
            }

            FirebaseDatabase.getInstance().getReference().child("Committee").child(String.valueOf(times)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    onBackPressed();
                }
            });

//            StringRequest request = new StringRequest(Request.Method.POST, "http://catchwo.com/createCommittee.php", new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//
////                    getToken();
////                    Intent intent = new Intent(CreateCommScreen.this,SplashScreen.class);
////                    startActivity(intent);
//                    onBackPressed();
//
//                    Toast.makeText(CreateCommScreen.this, response.toString(), Toast.LENGTH_SHORT).show();
//
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                    Toast.makeText(CreateCommScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//
//                }
//            }){
//
//                @Nullable
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//
//
//
//                    Map<String, String> hashMap = new HashMap<String, String>();
//                    hashMap.put("uid", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
//                    hashMap.put("createdBy",name);
//                    hashMap.put("time",time);
//                    hashMap.put("email",email.getText().toString());
//                    hashMap.put("comm_name",name_comm.getText().toString());
//                    hashMap.put("desc_comm",comm_desc.getText().toString());
//                    hashMap.put("phone",phone.getText().toString());
//                    hashMap.put("ComID", String.valueOf(times));
//
//                    if(public_comm.isChecked()){
//                        hashMap.put("type","Public");
//
//                    }else if(private_comm.isChecked()){
//                        hashMap.put("type","Private");
//                    }
//                    return hashMap;
//                }
//            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(CreateCommScreen.this);
//            requestQueue.add(request);
//


        }

    }

    private void loadUserData() {

//        StringRequest request = new StringRequest(Request.Method.POST, "http://catchwo.com/retriverdate.php", new com.android.volley.Response.Listener<String>() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onResponse(String response) {
////                arrayList.clear();
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("sucess");
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//                    if (success.equals("1")) {
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//
//                            String id_sql = object.getString("id");
//                            String name_sql = object.getString("name");
//                            String image_sql = object.getString("image");
//                            String uid = object.getString("uid");
//                            String time_sql = object.getString("time");
//                            String email_sql = object.getString("email");
//                            String dob_sql = object.getString("dob");
//                            String gender_sql = object.getString("gender");
//                            String phone_sql = object.getString("phone");
//                            String cover_sql = object.getString("cover");
//
//
//
//
//                            if(uid.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())){
//
//                                phone.setText(phone_sql);
//                                email.setText(email_sql);
//                                name = name_sql;
//                            }
//
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(CreateCommScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

//        RequestQueue requestQueue = Volley.newRequestQueue(CreateCommScreen.this);
//        requestQueue.add(request);

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                name = ""+snapshot.child("name").getValue();
                email.setText(""+snapshot.child("email").getValue());
                phone.setText(""+snapshot.child("phone").getValue());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

        loadUserData();

    }
}