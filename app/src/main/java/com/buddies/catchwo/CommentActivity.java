package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.buddies.catchwo.Adapter.CommentAdapter;
import com.buddies.catchwo.Adapter.PostAdapter;
import com.buddies.catchwo.Model.CommentModel;
import com.buddies.catchwo.Model.PostModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CommentActivity extends AppCompatActivity {

    ReadMoreTextView post_desc;
    String PUid,Pid,PDesc;
    ImageView user_img;
    TextView name;
    EditText messageBox;
    LottieAnimationView sendBtn;

    RecyclerView recyclerView;
    ArrayList<CommentModel> arrayList;
    CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        post_desc = findViewById(R.id.post_desc);
        user_img = findViewById(R.id.profile);
        name = findViewById(R.id.user_name);
        messageBox = findViewById(R.id.messageBox);
        sendBtn = findViewById(R.id.sendBtn);
        recyclerView = findViewById(R.id.recyclerView);

        Intent intent = getIntent();
         PUid = intent.getStringExtra("uid");
         Pid = intent.getStringExtra("id");
         PDesc = intent.getStringExtra("desc");

         arrayList = new ArrayList<>();

        post_desc.setText(PDesc);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(messageBox.getText().toString())){
                    Toast.makeText(CommentActivity.this, "Please Comment On the Post", Toast.LENGTH_SHORT).show();
                }else {
                    UploadComment();
                }
            }
        });

        LoadComments();

    }

    private void LoadComments() {

        arrayList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child("Comments").child(Pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();

                for(DataSnapshot ds: snapshot.getChildren()){
                    CommentModel postModel = ds.getValue(CommentModel.class);
                    arrayList.add(postModel);
                }

                adapter = new CommentAdapter(CommentActivity.this,arrayList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void UploadComment() {

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        Long times = System.currentTimeMillis();



        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("id",Pid);
        hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("comment",messageBox.getText().toString());
        hashMap.put("Cid",String.valueOf(times));
        hashMap.put("time",time);

        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference();
        reference.child("Comments").child(Pid).child(String.valueOf(times)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                messageBox.setText("");
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        StringRequest request = new StringRequest(Request.Method.POST, "http://catchwo.com/retriverdate.php", new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
//                arrayList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("sucess");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String id_sql = object.getString("id");
                            String name_sql = object.getString("name");
                            String image_sql = object.getString("image");
                            String uid2 = object.getString("uid");
                            String time_sql = object.getString("time");
                            String email_sql = object.getString("email");
                            String dob_sql = object.getString("dob");
                            String gender_sql = object.getString("gender");
                            String phone_sql = object.getString("phone");
                            String cover_sql = object.getString("cover");
                            String about_sql = object.getString("about");



                            if(uid2.equals(PUid)){

                                String image = "http://catchwo.com/images/"+image_sql;
                                Glide.with(CommentActivity.this).load(image).into(user_img);
                                name.setText(name_sql);

                            }

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CommentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(CommentActivity.this);
        requestQueue.add(request);



    }
}