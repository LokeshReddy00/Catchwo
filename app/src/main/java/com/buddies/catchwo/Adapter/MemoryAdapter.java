package com.buddies.catchwo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.buddies.catchwo.CommentActivity;
import com.buddies.catchwo.Filter.MemoryFilter;
import com.buddies.catchwo.Filter.UserSearch;
import com.buddies.catchwo.Model.PostModel;
import com.buddies.catchwo.PostDetailsActivity;
import com.buddies.catchwo.ProfileActivity;
import com.buddies.catchwo.R;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.Holder> implements Filterable {

    Context context;
    public ArrayList<PostModel> arrayList,FilterList;
    private MemoryFilter filter;
    int count;

    public MemoryAdapter(Context context, ArrayList<PostModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.FilterList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);

        return new Holder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {


        String uid = arrayList.get(position).getUid();


        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProfileActivity.class);
                intent.putExtra("uid",uid);
                context.startActivity(intent);
            }
        });


        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Glide.with(context).load(""+snapshot.child("pic").getValue()).into(holder.user_img);
                holder.name.setText(""+snapshot.child("name").getValue());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
//                                Glide.with(context).load(image).into(holder.user_img);
//                                holder.name.setText(name_sql);
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
//                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(request);


        holder.post_time.setText(arrayList.get(position).getTime());
        holder.post_desc.setText(arrayList.get(position).getDesc());

        String type = arrayList.get(position).getType();
        String url = arrayList.get(position).getPost();

        if (type.equals("iv")) {
            Picasso.get().load(url).into(holder.post_img);
            holder.exoplayer_item_post.setVisibility(View.GONE);
            holder.post_img.setVisibility(View.VISIBLE);
            holder.play.setVisibility(View.GONE);
            holder.loading.setVisibility(View.GONE);
        }else
        if (type.equals("vv")) {
            holder.exoplayer_item_post.setVisibility(View.VISIBLE);
            holder.play.setVisibility(View.VISIBLE);
            holder.post_img.setVisibility(View.GONE);


            MediaController mc = new MediaController(context);
            mc.setAnchorView( holder.exoplayer_item_post);
            mc.setMediaPlayer( holder.exoplayer_item_post);


            holder.exoplayer_item_post.setVideoURI(Uri.parse(url));
            holder.exoplayer_item_post.requestFocus();
            holder.exoplayer_item_post.setRevealOnFocusHint(true);
            holder.exoplayer_item_post.start();
            holder.exoplayer_item_post.stopPlayback();
            holder.isPLaying = true;
            holder.play.setImageResource(R.drawable.pasue);

//            holder.exoplayer_item_post.setScaleX(holder.exoplayer_item_post.getHeight());

            holder.exoplayer_item_post.setOnPreparedListener(m -> {
                try {
                    if (m.isPlaying()) {
                        m.stop();
                        m.release();
                        m = new MediaPlayer();
                    }
                    m.setVolume(0f, 0f);
                    m.setLooping(true);
                    m.start();

//                    float videoRatio = m.getVideoWidth() / (float) m.getVideoHeight();
//                    float screenRatio = holder.exoplayer_item_post.getWidth() / (float) holder.exoplayer_item_post.getHeight();
//                    float scaleRatio = videoRatio / screenRatio;
//                    if (scaleRatio >= 1f) {
//                        holder.exoplayer_item_post.setScaleX(scaleRatio);
//                    } else {
//                        holder.exoplayer_item_post.setScaleY(1f / scaleRatio);
//                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

//                holder.exoplayer_item_post.setOnHoverListener(new View.OnHoverListener() {
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

//            holder.exoplayer_item_post.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ShowDilogBox(Uri.parse(url));
//                }
//            });


            holder.exoplayer_item_post.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {

                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                        holder.loading.setVisibility(View.VISIBLE);
                    } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                        holder.loading.setVisibility(View.GONE);
                    } else {
                        holder.loading.setVisibility(View.GONE);
                    }

                    return false;
                }
            });



            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.isPLaying) {
                        holder.exoplayer_item_post.pause();
                        holder.isPLaying = false;
                        holder.play.setImageResource(R.drawable.play);
                    } else {
                        holder.exoplayer_item_post.start();
                        holder.isPLaying = true;
                        holder.play.setImageResource(R.drawable.pasue);
                    }
                }
            });

        }else if(type.equals("txt")){
            holder.post_desc.setText(arrayList.get(position).getDesc());
            holder.exoplayer_item_post.setVisibility(View.GONE);
            holder.post_img.setVisibility(View.GONE);
            holder.play.setVisibility(View.GONE);
            holder.loading.setVisibility(View.GONE);
            holder.lay.setVisibility(View.GONE);

        }

        PostModel model = arrayList.get(position);
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

//        holder.dislike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, ""+ count, Toast.LENGTH_SHORT).show();
//            }
//        });

//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Likes").child(model.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                float rCtWt = Float.parseFloat(String.valueOf(""+snapshot.getChildrenCount()));
                float mk = Float.parseFloat(String.valueOf(count));
                String finalRWt = (rCtWt / mk)*100 + "%";
                holder.like_count_text.setText(finalRWt);

//                like_count_text.setText(snapshot.getChildrenCount() + " Likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("disLikes").child(model.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                float rCtWt = Float.parseFloat(String.valueOf(""+snapshot.getChildrenCount()));
                float mk = Float.parseFloat(String.valueOf(count));
                String finalRWt = (rCtWt / mk)*100 + "%";
                holder.dislikes.setText(finalRWt);

//                like_count_text.setText(snapshot.getChildrenCount() + " Likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        isLikedT(model.getId(), holder.like_text);
        isLiked(model.getId(), holder.dislike);
        isSaved(model.getId(), holder.save);
//        nrLikesT(holder.like_count_text, model.getId(),"" +count);
//        nrCom(com_count_text, model.getId());

        holder.like_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( holder.like_text.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(String.valueOf(model.getId())).
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(true);
                    holder.like_text.setImageResource(R.drawable.ic_star);
                    holder.like_text.setTag("liked");
                    String title = "New Like";
                    String Meg = "Check Out you got a link on your post";

                    FirebaseDatabase.getInstance().getReference().child("disLikes")
                            .child(String.valueOf(model.getId()))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .removeValue();
                    holder.dislike.setImageResource(R.drawable.ic_like);
                    holder.dislike.setTag("like");

//                        Notifiy(title,Meg);


                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(String.valueOf(model.getId()))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .removeValue();
                    holder.like_text.setImageResource(R.drawable.ic_dis_star);
                    holder.like_text.setTag("like");
//
//                    FirebaseDatabase.getInstance().getReference().child("disLikes")
//                            .child(String.valueOf(model.getId())).
//                            child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .setValue(true);
//                    holder.dislike.setImageResource(R.drawable.ic_dis_like);
//                    holder.dislike.setTag("liked");
//                    String title = "New Like";
//                    String Meg = "Check Out you got a link on your post";
                }
            }
        });

        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( holder.dislike.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("disLikes")
                            .child(String.valueOf(model.getId())).
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(true);
                    holder.dislike.setImageResource(R.drawable.ic_dis_like);
                    holder.dislike.setTag("liked");
                    String title = "New Like";
                    String Meg = "Check Out you got a link on your post";

                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(String.valueOf(model.getId()))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .removeValue();
                    holder.like_text.setImageResource(R.drawable.ic_dis_star);
                    holder.like_text.setTag("like");

//                        Notifiy(title,Meg);


                } else {
                    FirebaseDatabase.getInstance().getReference().child("disLikes")
                            .child(String.valueOf(model.getId()))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .removeValue();
                    holder.dislike.setImageResource(R.drawable.ic_like);
                    holder.dislike.setTag("like");

//                    FirebaseDatabase.getInstance().getReference().child("Likes")
//                            .child(String.valueOf(model.getId())).
//                            child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .setValue(true);
//                    holder.like_text.setImageResource(R.drawable.ic_star);
//                    holder.like_text.setTag("liked");
//                    String title = "New Like";
//                    String Meg = "Check Out you got a link on your post";
                }
            }
        });

        String id = arrayList.get(position).getId();

        holder.save.setOnClickListener(new View.OnClickListener() {
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
                        holder.save.setImageResource(R.drawable.ic_saved);
                        holder.save.setClickable(false);
                    }
                });
            }
        });

        String desc = arrayList.get(position).getDesc();

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("uid",uid);
                intent.putExtra("desc",desc);
                context.startActivity(intent);

            }
        });

        String post = arrayList.get(position).getPost();

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("txt")){
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/*");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
                    intent.putExtra(Intent.EXTRA_TEXT, desc + "\n" + "app Link");
                    context.startActivity(Intent.createChooser(intent, "Share Via"));
                }else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/*");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
                    intent.putExtra(Intent.EXTRA_TEXT, desc + " " + post + "\n" + "app Link");
                    context.startActivity(Intent.createChooser(intent, "Share Via"));
                }
            }
        });

        String time = arrayList.get(position).getTime();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("uid",uid);
                intent.putExtra("type",type);
                intent.putExtra("id",id);
                intent.putExtra("post",post);
                intent.putExtra("time",time);
                intent.putExtra("about_",desc);
                intent.putExtra("types","memories");

                context.startActivity(intent);

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

    private void nrLikesT(TextView like_count_text, String id, String count) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Likes").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                float rCtWt = Float.parseFloat(String.valueOf(""+snapshot.getChildrenCount()));
                float mk = Float.parseFloat(count);
                String finalRWt = (rCtWt * mk) + "";
                like_count_text.setText(finalRWt);

//                like_count_text.setText(snapshot.getChildrenCount() + " Likes");
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
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new MemoryFilter(this,FilterList);
        }

        return filter;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        ImageView user_img,post_img,play,like_text,dislike,share,save;
        TextView name,like_count_text,dislikes;
        LottieAnimationView verfied,dropdown_menu,loading,comment;
        TextView post_time;
        ReadMoreTextView post_desc;
        VideoView exoplayer_item_post;
        CardView lay;

        boolean isPLaying = true;

        public Holder(@NonNull View itemView) {
            super(itemView);

            save = itemView.findViewById(R.id.save);
            dislikes = itemView.findViewById(R.id.dislikes);
            like_count_text = itemView.findViewById(R.id.like_count_text);
            lay = itemView.findViewById(R.id.lay);
            share = itemView.findViewById(R.id.share);
            dislike = itemView.findViewById(R.id.dislike);
            like_text = itemView.findViewById(R.id.star);
            play = itemView.findViewById(R.id.play_pause);
            post_img = itemView.findViewById(R.id.post_img);
            user_img = itemView.findViewById(R.id.user_img);
            name = itemView.findViewById(R.id.name);
            verfied = itemView.findViewById(R.id.verfied);
            dropdown_menu = itemView.findViewById(R.id.dropdown_menu);
            loading = itemView.findViewById(R.id.loading);
            comment = itemView.findViewById(R.id.comment);
            post_time = itemView.findViewById(R.id.post_time);
            post_desc = itemView.findViewById(R.id.post_desc);
            exoplayer_item_post = itemView.findViewById(R.id.exoplayer_item_post);
        }
    }
}
