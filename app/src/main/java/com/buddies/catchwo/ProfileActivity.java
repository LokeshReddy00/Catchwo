package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buddies.catchwo.ui.home.HomeFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    ImageView cover,profile;
    TextView name,email,about;
    String image,name_;
    Button books,prof,post,reels,comm,sur,Accepted;
    String url = "http://catchwo.com/retriverdate.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Intent intent = getIntent();
        String value = intent.getStringExtra("uid");
        String type = intent.getStringExtra("type");

        loaddata(value);

        reels = findViewById(R.id.reels);
        prof = findViewById(R.id.prof);
        cover = findViewById(R.id.cover);
        profile = findViewById(R.id.profile);
        Accepted = findViewById(R.id.Accepted);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        about = findViewById(R.id.about);
        books = findViewById(R.id.books);
//        request = findViewById(R.id.request);
        post = findViewById(R.id.post);
        comm = findViewById(R.id.comm);
        sur = findViewById(R.id.sur);

//        request.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Long timestamp = System.currentTimeMillis();
//                Calendar cdate = Calendar.getInstance();
//
//                SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");
//
//                final String savedate = currentdates.format(cdate.getTime());
//
//                Calendar ctime = Calendar.getInstance();
//                SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
//
//                final String savetime = currenttime.format(ctime.getTime());
//
//                String time = savedate + ":" + savetime;
//
//                String uid1  =FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                HashMap<String, Object> hashMap = new HashMap<>();
//                hashMap.put("uidto", value);
//                hashMap.put("image", image);
//                hashMap.put("name", name);
//                hashMap.put("uidfrom", uid1);
//                hashMap.put("senttime", time);
//                hashMap.put("id", String.valueOf(timestamp));
//                hashMap.put("acctime", "");
//                hashMap.put("task", "Requested");
//
////        FirebaseDatabase.getInstance().getReference().child("FRequests").child(String.valueOf(timestamp)).updateChildren(hashMap);
//                FirebaseDatabase.getInstance().getReference().child("Friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(value).updateChildren(hashMap);
//
//
//                FirebaseDatabase.getInstance().getReference().child("Users").child(uid1).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        HashMap<String, Object> hashMap1 = new HashMap<>();
//                        hashMap1.put("title",snapshot.child("name").getValue() + " Sent you a friend request");
//                        hashMap1.put("uid",uid1);
//                        hashMap1.put("id",String.valueOf(timestamp));
//                        hashMap1.put("type","Request");
//                        hashMap1.put("time",time);
//
//                        FirebaseDatabase.getInstance().getReference().child("Notifications").child(value).child(String.valueOf(timestamp)).updateChildren(hashMap1);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//
//
//
//
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//                reference.child("Queries").child(uid1).child(value).setValue(true);
//
//
//            }
//        });

        if(type.equals("request")){
            Accepted.setVisibility(View.VISIBLE);

            FirebaseDatabase.getInstance().getReference().child("Friends").child(value).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("task").getValue().equals("Accepted")){
                        Accepted.setClickable(false);
                        Accepted.setText("Accepted Request");
                    }else {
                        Accepted.setClickable(true);
                        Accepted.setText("Accept Request");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Accepted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    HashMap<String, Object> hashMap = new HashMap<>();
//                    hashMap.put("task","Accepted");
//                    FirebaseDatabase.getInstance().getReference().child("Friends").child(value).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);
                    Accepted.setText("Accepted Request");
                    Accepted.setClickable(false);


                    Long timestamp = System.currentTimeMillis();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("task","Accepted");
                    FirebaseDatabase.getInstance().getReference().child("Friends").child(value).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);


                    HashMap<String, Object> hashMap1 = new HashMap<>();
                    hashMap1.put("uidto", value);
                    hashMap1.put("image", image);
                    hashMap1.put("name", name.getText().
                            toString());
                    hashMap1.put("uidfrom", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    hashMap1.put("senttime", "");
                    hashMap1.put("id", String.valueOf(timestamp));
                    hashMap1.put("acctime", "");
                    hashMap1.put("task", "Accepted");

//        FirebaseDatabase.getInstance().getReference().child("FRequests").child(String.valueOf(timestamp)).updateChildren(hashMap);
                    FirebaseDatabase.getInstance().getReference().child("Friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(value).updateChildren(hashMap1);

                    Calendar cdate = Calendar.getInstance();

                    SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

                    final String savedate = currentdates.format(cdate.getTime());

                    Calendar ctime = Calendar.getInstance();
                    SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

                    final String savetime = currenttime.format(ctime.getTime());

                    String times = savedate + ":" + savetime;


                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap<String, Object> hashMap1 = new HashMap<>();
                            hashMap1.put("title",snapshot.child("name").getValue() + " Accepted your a friend request");
                            hashMap1.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            hashMap1.put("id",String.valueOf(timestamp));
                            hashMap1.put("type","Request");
                            hashMap1.put("time",times);

                            FirebaseDatabase.getInstance().getReference().child("Notifications").child(value).child(String.valueOf(timestamp)).updateChildren(hashMap1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            });
        }else  {
            Accepted.setVisibility(View.GONE);
        }

        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ProfileActivity.this,UserActivity.class);
                intent1.putExtra("value","book");
                intent1.putExtra("uid",value);
                startActivity(intent1);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ProfileActivity.this,UserActivity.class);
                intent1.putExtra("value","post");
                intent1.putExtra("uid",value);
                startActivity(intent1);
            }
        });

        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ProfileActivity.this,UserActivity.class);
                intent1.putExtra("value","prof");
                intent1.putExtra("uid",value);
                startActivity(intent1);
            }
        });

        comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ProfileActivity.this,UserActivity.class);
                intent1.putExtra("value","comm");
                intent1.putExtra("uid",value);
                startActivity(intent1);
            }
        });

        reels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ProfileActivity.this,UserActivity.class);
                intent1.putExtra("value","reels");
                intent1.putExtra("uid",value);
                startActivity(intent1);
            }
        });

        sur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ProfileActivity.this,UserActivity.class);
                intent1.putExtra("value","sur");
                intent1.putExtra("uid",value);
                startActivity(intent1);
            }
        });




    }

    private void loaddata(String value) {

//        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
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
//                            String about_sql = object.getString("about");
//
//
//                            String img = "http://catchwo.com/images/"+image_sql;
//
//
//                            if(uid.equals(value)){
//
//                                name.setText(name_sql);
//                                email.setText(email_sql);
//                                about.setText(about_sql);
//                                Glide.with(ProfileActivity.this).load(img).into(profile);
//
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
//                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
//        requestQueue.add(request);

        FirebaseDatabase.getInstance().getReference()
                .child("Users").child(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                image = ""+snapshot.child("pic").getValue();
                name_ = ""+snapshot.child("name").getValue();
                name.setText(""+snapshot.child("name").getValue());
                email.setText(""+snapshot.child("email").getValue());
                about.setText(""+snapshot.child("about").getValue());
                Glide.with(ProfileActivity.this).load(""+snapshot.child("pic").getValue()).placeholder(R.drawable.name).into(profile);
                Glide.with(ProfileActivity.this).load(""+snapshot.child("cover").getValue()).placeholder(R.drawable.name).into(cover);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}