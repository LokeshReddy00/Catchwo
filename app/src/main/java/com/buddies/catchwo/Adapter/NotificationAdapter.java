package com.buddies.catchwo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buddies.catchwo.NotificationModel;
import com.buddies.catchwo.ProfileActivity;
import com.buddies.catchwo.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Holder> {

    Context context;
    ArrayList<NotificationModel> arrayList;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.notification_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {


        holder.title.setText(arrayList.get(position).getTitle());
        holder.post_time.setText(arrayList.get(position).getTime());

        String title = arrayList.get(position).getTitle();
        String uid = arrayList.get(position).getUid();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.contains("Sent you a friend request")){
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("uid",uid);
                    intent.putExtra("type","request");
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        private TextView title,post_time;
        private ImageView image;

        public Holder(@NonNull View itemView) {
            super(itemView);

            post_time = itemView.findViewById(R.id.post_time);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
        }
    }
}
