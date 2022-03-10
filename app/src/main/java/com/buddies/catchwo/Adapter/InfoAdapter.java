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

import com.buddies.catchwo.Model.InfoModel;
import com.buddies.catchwo.R;
import com.buddies.catchwo.WebActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.Holder> {

    Context context;
    ArrayList<InfoModel> arrayList;

    public InfoAdapter(Context context, ArrayList<InfoModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.info, parent, false);

        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder,int position) {

        holder.post_desc.setText(arrayList.get(position).getDesc());
        holder.title.setText(arrayList.get(position).getTitle());

        String lik = arrayList.get(position).getLink();

        Glide.with(context).load(arrayList.get(position).getImage()).into(holder.post);


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WebActivity.class);
            intent.putExtra("url",lik);
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class Holder extends RecyclerView.ViewHolder {

        private final ImageView post;
        private final TextView title;
        private final TextView post_desc;

        public Holder(@NonNull View itemView) {
            super(itemView);

            post_desc = itemView.findViewById(R.id.post_desc);
            post = itemView.findViewById(R.id.post);
            title = itemView.findViewById(R.id.title);

        }
    }
}
