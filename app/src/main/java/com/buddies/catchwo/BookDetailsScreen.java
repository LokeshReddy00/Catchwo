package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.buddies.catchwo.Model.BookModel;
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

import java.util.HashMap;
import java.util.Objects;

public class BookDetailsScreen extends AppCompatActivity {

//    RatingBar simpleRatingBar;
    private ImageView image;
    private Button read,message;
    private TextView book,written,user_name;
    private ReadMoreTextView about;

    String url =  "http://catchwo.com/retrivecompletedbooks.php";

    String value,book_,pdf,type,uid,id,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details_screen);

        Intent intent = getIntent();
        value = intent.getStringExtra("id");
        type = intent.getStringExtra("type");

        written = findViewById(R.id.written);
        image = findViewById(R.id.image);
        read = findViewById(R.id.read);
        user_name = findViewById(R.id.user_name);
        message = findViewById(R.id.message);
        about = findViewById(R.id.about);
        book = findViewById(R.id.book);

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BookDetailsScreen.this, CommentActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("uid",uid);
                intent.putExtra("desc",name);
                startActivity(intent);
            }
        });

//        message.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                    Toast.makeText(BookDetailsScreen.this, "You are the publisher of the book", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            HashMap<String, Object> hashMap = new HashMap<>();
//
//                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                    hashMap.put("name", "" + snapshot.child("name").getValue());
//                                    hashMap.put("image", "" + snapshot.child("pic").getValue());
//                                    hashMap.put("token", "" + snapshot.child("fdmToken").getValue());
//                                    hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//                                    FirebaseDatabase.getInstance().getReference().child("Book_chat").child(uid).child(id).updateChildren(hashMap);
//
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
//
//                            hashMap.put("name", "" + snapshot.child("name").getValue());
//                            hashMap.put("image", "" + snapshot.child("pic").getValue());
//                            hashMap.put("token", "" + snapshot.child("fdmToken").getValue());
//                            hashMap.put("uid", uid);
//
//                            FirebaseDatabase.getInstance().getReference().child("Book_chat").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id).updateChildren(hashMap);
//
//                            Intent intent1 = new Intent(BookDetailsScreen.this,ChatActivity.class);
//                            intent1.putExtra("name",""+ snapshot.child("name").getValue());
//                            intent1.putExtra("image","" + snapshot.child("pic").getValue());
//                            intent1.putExtra("token",""+ snapshot.child("fdmToken").getValue());
//                            intent1.putExtra("uid",uid);
//                            startActivity(intent1);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//
//
//                }
//            }
//        });


        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(book_.isEmpty()){
//                    Intent intent1 = new Intent(BookDetailsScreen.this,WebActivity.class);
//                    intent1.putExtra("url",pdf.replace("",""));
////                    intent1.putExtra("type","complete");
////                    intent1.putExtra("uid",uid);
//                    startActivity(intent1);

                    Intent intentr=new Intent(Intent.ACTION_VIEW);
                    intentr.setData(Uri.parse(pdf));
                    startActivity(intentr);

                }else {
                    Intent intent1 = new Intent(BookDetailsScreen.this,BookPDFActivity.class);
                    intent1.putExtra("pdf",book_);
                    intent1.putExtra("type","ongoing");
                    intent1.putExtra("uid",uid);
                    intent1.putExtra("id",id);
                    startActivity(intent1);
                }
            }
        });



//        simpleRatingBar = (RatingBar) findViewById(R.id.simpleRatingBar); // initiate a rating bar
//        Float ratingNumber = simpleRatingBar.getRating();
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadData(value);
    }

    private void loadData(String value) {


        FirebaseDatabase.getInstance().getReference().child(type).child(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                written.setText("Written by: " +  snapshot.child("written").getValue());
                Glide.with(BookDetailsScreen.this).load(snapshot.child("image").getValue()).placeholder(R.drawable.book).into(image);
                about.setText(""+snapshot.child("description").getValue());
                book.setText("Title: "+  snapshot.child("name").getValue());

                uid = ""+snapshot.child("uid").getValue();

                pdf = ""+snapshot.child("pdf").getValue();
                id = ""+snapshot.child("bookID").getValue();

                book_ = ""+snapshot.child("book").getValue();

                name = ""+snapshot.child("name").getValue();




                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        user_name.setText(""+snapshot.child("name").getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
//                                String  bid_ = object.getString("bookID");
//
//                                if(bid_.equals(value)){
//
//                                    String id_ = object.getString("id");
//                                     name_ = object.getString("name");
//                                    String written_ = object.getString("written");
//                                    String time_ = object.getString("time");
//                                    book_ = object.getString("book");
//                                    String cat_ = object.getString("category");
//
//                                    String image_D = object.getString("image");
//                                    String desc_ = object.getString("description");
//                                    String type_ = object.getString("type");
//
//                                    String image_ = "http://catchwo.com/images/" + image_D;
//
//
//                                    uid_ = object.getString("uid");
//                                    written.setText("Written by: " +  written_);
//                                    Glide.with(BookDetailsScreen.this).load(image_).into(image);
//                                    about.setText(desc_);
//                                    book.setText("Title: "+book_);
//                                    user_name.setText("  "+name_);
//
//                                 }
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
//                Toast.makeText(BookDetailsScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(BookDetailsScreen.this);
//        requestQueue.add(request);


//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        reference.child("Books").child(uid_).child(value).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if(snapshot.exists()) {
//                    pdf = "" + snapshot.child("pdf").getValue();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }
}