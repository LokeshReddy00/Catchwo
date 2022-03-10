package com.buddies.catchwo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buddies.catchwo.ChatActivity;
import com.buddies.catchwo.Filter.FriendFilter;
import com.buddies.catchwo.Filter.UserSearch;
import com.buddies.catchwo.Model.FriendModel;
import com.buddies.catchwo.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.Holder> implements Filterable {


    Context context;
    public ArrayList<FriendModel> arrayList,FilterList;
    private FriendFilter filter;
    String toke;

    public FriendAdapter(Context context, ArrayList<FriendModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.FilterList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_row, parent, false);
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

        String url = "http://catchwo.com/images/" + image;



//        String name = arrayList.get(position).getName();
        String profile = arrayList.get(position).getImage();
        String token = arrayList.get(position).getUidfrom();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(token).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                toke = ""+snapshot.child("fdmToken").getValue();
                holder.name.setText(""+snapshot.child("name").getValue());
                Glide.with(context)
                        .load(""+snapshot.child("pic").getValue()).placeholder(R.drawable.name).into(holder.image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("image",profile);
                intent.putExtra("token",toke);
                intent.putExtra("uid",token);
                context.startActivity(intent);
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
            filter = new FriendFilter(this,FilterList);
        }

        return filter;
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
