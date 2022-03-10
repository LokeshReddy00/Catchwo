package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buddies.catchwo.Adapter.PostAdapter;
import com.buddies.catchwo.Model.CommitteeModel;
import com.buddies.catchwo.Model.PostModel;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommitteeProfileActivity extends AppCompatActivity {

    private ImageView cover_comm;
    private TextView textView6,about,home,video,member,note,settings,create;
    private Button mess,membership;
    String url = "http://catchwo.com/retrivecommitteeinfo.php";
    String uid,id,type,image,abouts,createdBy,time,email,comm_name,desc_comm,phone;
    String upUrl = "http://catchwo.com/updatecmminfo.php";
    AlertDialog dialog;
    String value;
    private RecyclerView post_rec;

    PostAdapter adapter;
    ArrayList<PostModel> arrayList;

    LottieAnimationView loading;

    private static final int TOTAL_ITEM_EACH_LOAD = 8;
    private int currentPage = 1;
    Button more;
    long initial;
    private TextView nothing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee_profile);

        cover_comm = findViewById(R.id.cover_comm);
        create = findViewById(R.id.create);
        textView6 = findViewById(R.id.textView6);
        about = findViewById(R.id.about);
        home = findViewById(R.id.home);
        video = findViewById(R.id.video);
        member = findViewById(R.id.member);
        note = findViewById(R.id.note);
        settings = findViewById(R.id.settings);
        mess = findViewById(R.id.mess);
        membership = findViewById(R.id.membership);
        post_rec = findViewById(R.id.post_rec);
        more =findViewById(R.id.more);
        nothing =findViewById(R.id.nothing);


        Intent intent = getIntent();
        value = intent.getStringExtra("id");
        String valu1e = intent.getStringExtra("uid");
//        img = intent.getStringExtra("image");

//        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();


        findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CommitteeProfileActivity.this,AddPostActivity.class);
                intent1.putExtra("Id",value);
                intent1.putExtra("value","committee");
                intent1.putExtra("name",comm_name);
                startActivity(intent1);
            }
        });

        loadCommitteeData(value);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    i++;
                }
                initial = i;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        loadPost();
        arrayList = new ArrayList<>();

        more.setOnClickListener(v -> loadMoreData());



            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(valu1e.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        ShowDialogBox();
                    }
                }
            });




        cover_comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(CommitteeProfileActivity.this, EditActivity.class);
               intent.putExtra("edit","committee_pic");
               intent.putExtra("id",id);
               intent.putExtra("type",type);
               intent.putExtra("image",image);
               intent.putExtra("abouts",abouts);
               intent.putExtra("createdBy",createdBy);
               intent.putExtra("CTime",time);
               intent.putExtra("email",email);
               intent.putExtra("comm_name",comm_name);
               intent.putExtra("desc_comm",desc_comm);
               intent.putExtra("phone",phone);
               intent.putExtra("ComID",value);

               if(valu1e.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                   startActivity(intent);
               }

//                Toast.makeText(CommitteeProfileActivity.this, createdBy, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMoreData() {
        currentPage++;
        loadPost();
    }

    private void loadPost() {

        arrayList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child("Posts").limitToLast(currentPage*TOTAL_ITEM_EACH_LOAD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();

                for(DataSnapshot ds: snapshot.getChildren()) {

                    String cont = "" + ds.child("comt").getValue();

                    if (cont.equals(value)) {

                        PostModel postModel = ds.getValue(PostModel.class);
                        arrayList.add(postModel);
                        adapter = new PostAdapter(CommitteeProfileActivity.this, arrayList);
                        post_rec.setAdapter(adapter);
//                        loading.setVisibility(View.GONE);

                        if (adapter.getItemCount() == 0) {
//                            loading.setVisibility(View.GONE);
                            post_rec.setVisibility(View.GONE);
                            nothing.setVisibility(View.VISIBLE);
                        } else {
//                            loading.setVisibility(View.GONE);
                            post_rec.setVisibility(View.VISIBLE);
                            nothing.setVisibility(View.GONE);
                            if (adapter.getItemCount() == initial) {
                                more.setVisibility(View.GONE);
                                currentPage--;
                            } else {
                                more.setVisibility(View.VISIBLE);
                            }

                        }

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommitteeProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void ShowDialogBox() {

        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(CommitteeProfileActivity.this, android.R.style.Theme_Material_Dialog_Alert);

        } else {
            alert = new AlertDialog.Builder(CommitteeProfileActivity.this);
        }

        View view = LayoutInflater.from(CommitteeProfileActivity.this).inflate(R.layout.edittext_alert, null);



        TextInputEditText about = view.findViewById(R.id.about);
        Button update = view.findViewById(R.id.update_text);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(about.getText().toString())){
                    Toast.makeText(CommitteeProfileActivity.this, "Please Fill something about", Toast.LENGTH_SHORT).show();
                }else
                {
                    UpdateServer(about.getText().toString());
                    dialog.dismiss();
//                    Toast.makeText(CommitteeProfileActivity.this, about.getText().toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        alert.setView(view);

        alert.setCancelable(true);



        dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();



    }

    private void UpdateServer(String toString) {


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("about",toString);

        FirebaseDatabase.getInstance().getReference().child("Committee").child(value).updateChildren(hashMap);

//        StringRequest request = new StringRequest(Request.Method.POST, upUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//
////                finish();
//                dialog.dismiss();
//
//                Toast.makeText(CommitteeProfileActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
//
//                Toast.makeText(CommitteeProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        }){
//
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//
//
//                Map<String, String> hashMap = new HashMap<String, String>();
//
//                hashMap.put("about",toString);
//                hashMap.put("uid", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
//                hashMap.put("createdBy",createdBy);
//                hashMap.put("time",time);
//                hashMap.put("email",email);
//                hashMap.put("comm_name",textView6.getText().toString());
//                hashMap.put("desc_comm",desc_comm);
//                hashMap.put("phone",phone);
//                hashMap.put("ComID",value );
//                hashMap.put("image", image);
//                hashMap.put("id", id);
//                hashMap.put("type", type);
//
//
//                return hashMap;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(CommitteeProfileActivity.this);
//        requestQueue.add(request);
//
//


    }

    private void loadCommitteeData(String value) {

//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onResponse(String response) {
//
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
//
//                            String ComID = object.getString("ComID");
//
//
//
//                            if(ComID.equals(value)){
//                                 uid = object.getString("uid");
//                                 id = object.getString("id");
//                                 type = object.getString("type");
//                                 image = object.getString("image");
//                                 abouts = object.getString("about");
//                                 createdBy = object.getString("createdBy");
//                                 time = object.getString("time");
//                                 email = object.getString("email");
//                                 comm_name = object.getString("comm_name");
//                                 desc_comm = object.getString("desc_comm");
//                                 phone = object.getString("phone");
//
////                                String urlImage= "http://catchwo.com/images/"+image;
//                                String urlImage= "http://catchwo.com/images/"+image;
//                                textView6.setText(comm_name);
//                                create.setText("Created By "+createdBy);
//                                Glide.with(CommitteeProfileActivity.this).load(urlImage).into(cover_comm);
//                                about.setText(abouts);
//
////                                if(uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
////
////                                   about.setTag("admin");
////
////                                }
//                            }
//
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
//                Toast.makeText(CommitteeProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(CommitteeProfileActivity.this);
//        requestQueue.add(request);

        FirebaseDatabase.getInstance().getReference().child("Committee").child(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                comm_name = ""+snapshot.child("comm_name").getValue();

                textView6.setText(""+snapshot.child("comm_name").getValue());
                create.setText("Created By "+snapshot.child("createdBy").getValue());
                Glide.with(CommitteeProfileActivity.this).load(snapshot.child("image").getValue()).into(cover_comm);
                about.setText(""+snapshot.child("about").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}