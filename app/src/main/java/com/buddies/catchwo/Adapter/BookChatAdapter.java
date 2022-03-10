package com.buddies.catchwo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buddies.catchwo.ChatActivity;
import com.buddies.catchwo.Filter.BookChatFilter;
import com.buddies.catchwo.Filter.CFriendFilter;
import com.buddies.catchwo.Model.BookChatModel;
import com.buddies.catchwo.Model.FriendModel;
import com.buddies.catchwo.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookChatAdapter extends RecyclerView.Adapter<BookChatAdapter.Holder> implements Filterable {

    Context context;
    public ArrayList<BookChatModel> arrayList,FilterList;
    private BookChatFilter filter;
    String toke;

    public BookChatAdapter(Context context, ArrayList<BookChatModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.FilterList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_row, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        try {
            Glide.with(context).load(arrayList.get(position).getImage()).into(holder.profile_img);
        }catch (Exception e){
            holder.profile_img.setImageResource(R.drawable.app);
        }


        holder.user_name.setText(arrayList.get(position).getName());

        String name = arrayList.get(position).getName();
        String profile = arrayList.get(position).getImage();
        String token = arrayList.get(position).getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(token).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                toke = ""+snapshot.child("fdmToken").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
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

        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String room = user + token;

//        database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);

        FirebaseDatabase.getInstance().getReference().child("chats").child(room).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String time = ""+snapshot.child("lastMsgTime").getValue();
                String message = ""+snapshot.child("lastMsg").getValue();
                if(!time.isEmpty() && !message.isEmpty()){

                    holder.last_message.setVisibility(View.VISIBLE);
                    holder.post_time.setVisibility(View.VISIBLE);

                    holder.last_message.setText(message);
                    holder.post_time.setText(time);
                }else {
                    holder.last_message.setVisibility(View.GONE);
                    holder.post_time.setVisibility(View.GONE);
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
            filter = new BookChatFilter(this,FilterList);
        }

        return filter;
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView profile_img,seen;
        private TextView last_message,user_name,post_time;

        public Holder(@NonNull View itemView) {
            super(itemView);

            seen = itemView.findViewById(R.id.seen);
            profile_img = itemView.findViewById(R.id.profile_img);
            last_message = itemView.findViewById(R.id.last_message);
            user_name = itemView.findViewById(R.id.user_name);
            post_time = itemView.findViewById(R.id.post_time);

            seen.setVisibility(View.GONE);
        }
    }
}
