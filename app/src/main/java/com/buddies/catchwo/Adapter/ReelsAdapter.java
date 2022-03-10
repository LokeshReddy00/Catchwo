package com.buddies.catchwo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.buddies.catchwo.AddReels;
import com.buddies.catchwo.CommentActivity;
import com.buddies.catchwo.Model.ReelsModel;
import com.buddies.catchwo.ProfileActivity;
import com.buddies.catchwo.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReelsAdapter extends RecyclerView.Adapter<ReelsAdapter.Holder> {

    Context context;
    ArrayList<ReelsModel> arrayList;
    int count;

    public ReelsAdapter(Context context, ArrayList<ReelsModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.reel_row, parent, false);
        return new Holder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.description.setText(arrayList.get(position).getDesc());

        MediaController mc = new MediaController(context);
        mc.setAnchorView( holder.exoplayer_item_post);
        mc.setMediaPlayer( holder.exoplayer_item_post);

        String url = arrayList.get(position).getPost();

        holder.exoplayer_item_post.setVideoURI(Uri.parse(url));
        holder.exoplayer_item_post.requestFocus();
        holder.exoplayer_item_post.setRevealOnFocusHint(true);
        holder.exoplayer_item_post.start();
        holder.exoplayer_item_post.stopPlayback();

        holder.exoplayer_item_post.setOnPreparedListener(m -> {
            try {
                if (m.isPlaying()) {
                    m.stop();
                    m.release();
                    m = new MediaPlayer();
                }
//                m.setVolume(0f, 0f);
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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

            }
        });



        holder.exoplayer_item_post.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {

                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.loading.setVisibility(View.VISIBLE);
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.loading.setVisibility(View.GONE);
                } else {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.loading.setVisibility(View.GONE);
                }

                return false;
            }
        });

        String uid = arrayList.get(position).getUid();

        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                holder.user_id.setText(""+snapshot.child("name").getValue());
                Glide.with(context).load(""+snapshot.child("pic").getValue()).placeholder(R.drawable.name).into(holder.pic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, AddReels.class));
            }
        });

        String desc = arrayList.get(position).getDesc();
        String id = arrayList.get(position).getId();

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

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/*");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
                    intent.putExtra(Intent.EXTRA_TEXT, desc + " " + url + "\n" + "app Link");
                    context.startActivity(Intent.createChooser(intent, "Share Via"));
            }
        });

        holder.user_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("uid",uid);
                intent.putExtra("type","view");
                context.startActivity(intent);

            }
        });

        isLikedT(arrayList.get(position).getId(), holder.like);


         ReelsModel model = arrayList.get(position);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( holder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(String.valueOf(model.getId())).
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(true);
                    holder.like.setImageResource(R.drawable.ic_star);
                    holder.like.setTag("liked");
                    String title = "New Like";
                    String Meg = "Check Out you got a link on your post";

//                    FirebaseDatabase.getInstance().getReference().child("disLikes")
//                            .child(String.valueOf(arrayList.get(position).getId()))
//                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .removeValue();
//                    holder.dislike.setImageResource(R.drawable.ic_like);
//                    holder.dislike.setTag("like");

//                        Notifiy(title,Meg);


                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(String.valueOf(model.getId()))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .removeValue();
                    holder.like.setImageResource(R.drawable.star);
                    holder.like.setTag("like");
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

        reference.child("Likes").child(model.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                float rCtWt = Float.parseFloat(String.valueOf(""+snapshot.getChildrenCount()));
                float mk = Float.parseFloat(String.valueOf(count));
                String finalRWt = (rCtWt / mk)*100 + "%";
                holder.like_count.setText(finalRWt);

//                like_count_text.setText(snapshot.getChildrenCount() + " Likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void isLikedT(String id, ImageView like) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Likes").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assert user != null;
                if (snapshot.hasChild(user.getUid())) {
                    like.setImageResource(R.drawable.ic_star);
                    like.setTag("liked");
                } else if (!snapshot.hasChild(user.getUid())) {
                    like.setImageResource(R.drawable.star);
                    like.setTag("like");
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


    public class Holder extends RecyclerView.ViewHolder {

        private  VideoView exoplayer_item_post;
        private TextView user_id,description,like_count;
        private ImageView share,comment,like,disc,pic;
        private ProgressBar progressBar;
        private LottieAnimationView loading;


        public Holder(@NonNull View itemView) {
            super(itemView);

            disc = itemView.findViewById(R.id.disc);
            like_count = itemView.findViewById(R.id.like_count);
            exoplayer_item_post = itemView.findViewById(R.id.video);
            user_id = itemView.findViewById(R.id.user_id);
            description = itemView.findViewById(R.id.description);
            share = itemView.findViewById(R.id.share);
            comment = itemView.findViewById(R.id.comment);
            like = itemView.findViewById(R.id.like);
            progressBar = itemView.findViewById(R.id.progressBar);
            loading = itemView.findViewById(R.id.loading);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
