package com.buddies.catchwo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buddies.catchwo.ChatActivity;
import com.buddies.catchwo.Filter.FriendFilter;
import com.buddies.catchwo.Model.FriendModel;
import com.buddies.catchwo.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ShareWithAdapter extends RecyclerView.Adapter<ShareWithAdapter.Holder> {

    Context context;
    public ArrayList<FriendModel> arrayList,FilterList;
    private FriendFilter filter;
    String toke;

    public ShareWithAdapter(Context context, ArrayList<FriendModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend, parent, false);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        String uidto = arrayList.get(position).getUidto();
        String image = arrayList.get(position).getImage();
        String name = arrayList.get(position).getName();
        String uidfrom = arrayList.get(position).getUidfrom();
        String senttime = arrayList.get(position).getSenttime();
        String acctime = arrayList.get(position).getAcctime();
        String task = arrayList.get(position).getTask();

        holder.name.setText(name);
        Glide.with(context)
                .load(image).into(holder.image);

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        Long times = System.currentTimeMillis();

        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("title",name + " Shared a post with you");
                hashMap.put("uid","");
                hashMap.put("id",String.valueOf(times));
                hashMap.put("type","post");
                hashMap.put("time",time);

                FirebaseDatabase.getInstance().getReference().child("Notifications").child(uidto).setValue("");


            }
        });


    }

    @Override
    public int getItemCount() {
        return  arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        Button message;

        public Holder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
        }
    }
}
