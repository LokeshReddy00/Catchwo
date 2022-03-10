package com.buddies.catchwo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.buddies.catchwo.Model.AppointinfoModel;
import com.buddies.catchwo.ProfileActivity;
import com.buddies.catchwo.R;

import java.util.ArrayList;

public class AppointInfoAdapter extends RecyclerView.Adapter<AppointInfoAdapter.Holder> {

    Context context;
    ArrayList<AppointinfoModel> arrayList;

    public AppointInfoAdapter(Context context, ArrayList<AppointinfoModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appoint_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.name.setText(arrayList.get(position).getPersonname());
        holder.prof.setText(arrayList.get(position).getProfname());
        holder.query.setText(arrayList.get(position).getQuery());
        holder.time.setText(arrayList.get(position).getTime());

        String uid = arrayList.get(position).getUid();

        holder.name.setOnClickListener(v -> {
            // open profile
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("uid",uid);
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        private TextView name,prof,time;
        private ReadMoreTextView query;

        public Holder(@NonNull View itemView) {
            super(itemView);

            query = itemView.findViewById(R.id.query);
            name = itemView.findViewById(R.id.name);
            prof = itemView.findViewById(R.id.prof);
            time = itemView.findViewById(R.id.time);
        }
    }
}
