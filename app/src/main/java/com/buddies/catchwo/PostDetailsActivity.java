package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class PostDetailsActivity extends AppCompatActivity {

    ImageView user_img,post_img,play,like_text,dislike,share,save;
    TextView name,like_count_text,dislikes;
    LottieAnimationView verfied,dropdown_menu,loading,comment;
    TextView post_time;
    ReadMoreTextView post_desc;
    VideoView exoplayer_item_post;
    CardView lay;

    boolean isPLaying = true;

    private ProgressDialog progressDialog;
    Button shareToPublic,delete;
    int count;
    String type,uid,id,post,time,desc,types;

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        shareToPublic = findViewById(R.id.shareToPublic);
        lay = findViewById(R.id.lay);
        exoplayer_item_post = findViewById(R.id.exoplayer_item_post);
        post_desc = findViewById(R.id.post_desc);
        post_time = findViewById(R.id.post_time);
        user_img = findViewById(R.id.user_img);
        post_img = findViewById(R.id.post_img);
        like_text = findViewById(R.id.star);
        play = findViewById(R.id.play_pause);
        dislike = findViewById(R.id.dislike);
        share = findViewById(R.id.share);
        save = findViewById(R.id.save);
        name = findViewById(R.id.name);
        like_count_text = findViewById(R.id.like_count_text);
        dislikes = findViewById(R.id.dislikes);
        verfied = findViewById(R.id.verfied);
        dropdown_menu = findViewById(R.id.dropdown_menu);
        loading = findViewById(R.id.loading);
        delete = findViewById(R.id.delete);
        comment = findViewById(R.id.comment);
        progressDialog = new ProgressDialog(this);


        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        type = intent.getStringExtra("type");
        id = intent.getStringExtra("id");
        post = intent.getStringExtra("post");
        time = intent.getStringExtra("time");
        desc = intent.getStringExtra("about_");
        types = intent.getStringExtra("types");


        if(uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

            delete.setVisibility(View.VISIBLE);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference().child("Posts").child(id).removeValue();
                    onBackPressed();
                }
            });
        }else {
            delete.setVisibility(View.GONE);
        }







        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        Long times = System.currentTimeMillis();


        shareToPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Please wait ....");
                progressDialog.show();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("type",type);
                hashMap.put("time",time);
                hashMap.put("desc", desc);
                hashMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                hashMap.put("post",post);
                hashMap.put("id",id);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Posts").child(String.valueOf(times)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(PostDetailsActivity.this, HomeScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        progressDialog.dismiss();
                    }
                });
            }
        });






        post_time.setText(time);
        post_desc.setText(desc);

        if(types.equals("memories")){
            if(uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                shareToPublic.setVisibility(View.VISIBLE);
            }

        }else if(types.equals("post")){
            shareToPublic.setVisibility(View.GONE);
        }





        isLikedT(id, like_text);
        isLiked(id, dislike);
        isSaved(id, save);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("txt")){
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/*");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
                    intent.putExtra(Intent.EXTRA_TEXT, desc + "\n" + "app Link");
                    startActivity(Intent.createChooser(intent, "Share Via"));
                }else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/*");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
                    intent.putExtra(Intent.EXTRA_TEXT, desc + " " + post + "\n" + "app Link");
                    startActivity(Intent.createChooser(intent, "Share Via"));
                }
            }
        });


        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PostDetailsActivity.this, CommentActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("uid",uid);
                intent.putExtra("desc",desc);
                startActivity(intent);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id",id);
                hashMap.put("uid",uid);
                hashMap.put("savedUid",FirebaseAuth.getInstance().getCurrentUser().getUid());

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Saved").child(uid).child(id).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        save.setImageResource(R.drawable.ic_saved);
                        save.setClickable(false);
                    }
                });
            }
        });

        like_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( like_text.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(String.valueOf(id)).
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(true);
                    like_text.setImageResource(R.drawable.ic_star);
                    like_text.setTag("liked");
                    String title = "New Like";
                    String Meg = "Check Out you got a link on your post";

                    FirebaseDatabase.getInstance().getReference().child("disLikes")
                            .child(String.valueOf(id))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .removeValue();
                    dislike.setImageResource(R.drawable.ic_like);
                    dislike.setTag("like");

//                        Notifiy(title,Meg);


                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(String.valueOf(id))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .removeValue();
                    like_text.setImageResource(R.drawable.ic_dis_star);
                    like_text.setTag("like");
//
//                    FirebaseDatabase.getInstance().getReference().child("disLikes")
//                            .child(String.valueOf(id)).
//                            child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .setValue(true);
//                    dislike.setImageResource(R.drawable.ic_dis_like);
//                    dislike.setTag("liked");
//                    String title = "New Like";
//                    String Meg = "Check Out you got a link on your post";
                }
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( dislike.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("disLikes")
                            .child(String.valueOf(id)).
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(true);
                    dislike.setImageResource(R.drawable.ic_dis_like);
                    dislike.setTag("liked");
                    String title = "New Like";
                    String Meg = "Check Out you got a link on your post";

                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(String.valueOf(id))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .removeValue();
                    like_text.setImageResource(R.drawable.ic_dis_star);
                    like_text.setTag("like");

//                        Notifiy(title,Meg);


                } else {
                    FirebaseDatabase.getInstance().getReference().child("disLikes")
                            .child(String.valueOf(id))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .removeValue();
                    dislike.setImageResource(R.drawable.ic_like);
                    dislike.setTag("like");

//                    FirebaseDatabase.getInstance().getReference().child("Likes")
//                            .child(String.valueOf(id)).
//                            child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .setValue(true);
//                    like_text.setImageResource(R.drawable.ic_star);
//                    like_text.setTag("liked");
//                    String title = "New Like";
//                    String Meg = "Check Out you got a link on your post";
                }
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference.child("disLikes").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                float rCtWt = Float.parseFloat(String.valueOf(""+snapshot.getChildrenCount()));
                float mk = Float.parseFloat(String.valueOf(count));
                String finalRWt = (rCtWt / mk)*100 + "%";
                dislikes.setText(finalRWt);

//                like_count_text.setText(snapshot.getChildrenCount() + " Likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("Likes").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                float rCtWt = Float.parseFloat(String.valueOf(""+snapshot.getChildrenCount()));
                float mk = Float.parseFloat(String.valueOf(count));
                String finalRWt = (rCtWt / mk)*100 + "%";
                like_count_text.setText(finalRWt);

//                like_count_text.setText(snapshot.getChildrenCount() + " Likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if (type.equals("iv")) {
            Picasso.get().load(post).into(post_img);
           exoplayer_item_post.setVisibility(View.GONE);
           post_img.setVisibility(View.VISIBLE);
           play.setVisibility(View.GONE);
           loading.setVisibility(View.GONE);
            post_desc.setText(desc);
        }else
        if (type.equals("vv")) {
           exoplayer_item_post.setVisibility(View.VISIBLE);
           play.setVisibility(View.VISIBLE);
           post_img.setVisibility(View.GONE);


            post_desc.setText(desc);
            MediaController mc = new MediaController(PostDetailsActivity.this);
            mc.setAnchorView(exoplayer_item_post);
            mc.setMediaPlayer(exoplayer_item_post);


           exoplayer_item_post.setVideoURI(Uri.parse(post));
           exoplayer_item_post.requestFocus();
           exoplayer_item_post.start();
           exoplayer_item_post.stopPlayback();
           isPLaying = true;
           play.setImageResource(R.drawable.pasue);

//           exoplayer_item_post.setScaleX(exoplayer_item_post.getHeight());

           exoplayer_item_post.setOnPreparedListener(m -> {
                try {
                    if (m.isPlaying()) {
                        m.stop();
                        m.release();
                        m = new MediaPlayer();
                    }
//                    m.setVolume(0f, 0f);
                    m.setLooping(true);
                    m.start();

//                    float videoRatio = m.getVideoWidth() / (float) m.getVideoHeight();
//                    float screenRatio =exoplayer_item_post.getWidth() / (float)exoplayer_item_post.getHeight();
//                    float scaleRatio = videoRatio / screenRatio;
//                    if (scaleRatio >= 1f) {
//                       exoplayer_item_post.setScaleX(scaleRatio);
//                    } else {
//                       exoplayer_item_post.setScaleY(1f / scaleRatio);
//                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

//               exoplayer_item_post.setOnHoverListener(new View.OnHoverListener() {
//                    @Override
//                    public boolean onHover(View v, MotionEvent event) {
//                        switch (event.getAction()) {
//                            case MotionEvent.ACTION_HOVER_ENTER:
//                            case MotionEvent.ACTION_HOVER_MOVE:
//                                ShowDilogBox(Uri.parse(url));
//                                break;
//                            case MotionEvent.ACTION_HOVER_EXIT:
//                               dialog.dismiss();
//                                break;
//                        }
//                        return false;
//                    }
//                });

//           exoplayer_item_post.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ShowDilogBox(Uri.parse(url));
//                }
//            });


           exoplayer_item_post.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {

                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                       loading.setVisibility(View.VISIBLE);
                    } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                       loading.setVisibility(View.GONE);
                    } else {
                       loading.setVisibility(View.GONE);
                    }

                    return false;
                }
            });



           play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isPLaying) {
                       exoplayer_item_post.pause();
                       isPLaying = false;
                       play.setImageResource(R.drawable.play);
                    } else {
                       exoplayer_item_post.start();
                       isPLaying = true;
                       play.setImageResource(R.drawable.pasue);
                    }
                }
            });

        }else if(type.equals("txt")){
           post_desc.setText(desc);
           exoplayer_item_post.setVisibility(View.GONE);
           post_img.setVisibility(View.GONE);
           play.setVisibility(View.GONE);
           loading.setVisibility(View.GONE);
           lay.setVisibility(View.GONE);

        }
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailsActivity.this,ProfileActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });


    }

    private void isSaved(String id, ImageView save) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Saved").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assert user != null;
                if (snapshot.hasChild(user.getUid())) {
                    save.setImageResource(R.drawable.ic_saved);
                    save.setClickable(false);
                } else if (!snapshot.hasChild(user.getUid())) {
                    save.setImageResource(R.drawable.ic_save);
                    save.setClickable(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void isLikedT(String id, ImageView like_text) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Likes").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assert user != null;
                if (snapshot.hasChild(user.getUid())) {
                    like_text.setImageResource(R.drawable.ic_star);
                    like_text.setTag("liked");
                } else if (!snapshot.hasChild(user.getUid())) {
                    like_text.setImageResource(R.drawable.ic_dis_star);
                    like_text.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void isLiked(String id, ImageView like_text) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("disLikes").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assert user != null;
                if (snapshot.hasChild(user.getUid())) {
                    like_text.setImageResource(R.drawable.ic_dis_like);
                    like_text.setTag("liked");
                } else if (!snapshot.hasChild(user.getUid())) {
                    like_text.setImageResource(R.drawable.ic_like);
                    like_text.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

//        StringRequest request = new StringRequest(Request.Method.POST, "http://catchwo.com/retriverdate.php", new Response.Listener<String>() {
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
//                            String uid2 = object.getString("uid");
//                            String time_sql = object.getString("time");
//                            String email_sql = object.getString("email");
//                            String dob_sql = object.getString("dob");
//                            String gender_sql = object.getString("gender");
//                            String phone_sql = object.getString("phone");
//                            String cover_sql = object.getString("cover");
//                            String about_sql = object.getString("about");
//
//
//
//                            if(uid2.equals(uid)){
//
//                                String image = "http://catchwo.com/images/"+image_sql;
//                                Glide.with(PostDetailsActivity.this).load(image).into(user_img);
//                                name.setText(name_sql);
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
//                Toast.makeText(PostDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(PostDetailsActivity.this);
//        requestQueue.add(request);

        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                name.setText(""+snapshot.child("name").getValue());
//                Picasso.get().load(""+snapshot.child("pic").getValue()).placeholder(R.drawable.name).into(user_img);
                Glide.with(PostDetailsActivity.this).load(""+snapshot.child("pic").getValue()).placeholder(R.drawable.name).into(user_img);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}