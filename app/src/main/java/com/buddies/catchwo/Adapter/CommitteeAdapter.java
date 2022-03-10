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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buddies.catchwo.CommitteeProfileActivity;
import com.buddies.catchwo.Filter.CommitteeFilter;
import com.buddies.catchwo.Filter.UserSearch;
import com.buddies.catchwo.Model.CommitteeModel;
import com.buddies.catchwo.ProfileActivity;
import com.buddies.catchwo.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class CommitteeAdapter extends RecyclerView.Adapter<CommitteeAdapter.Holder> implements Filterable {

    Context context;
    public ArrayList<CommitteeModel> arrayList,FilterList;
    private CommitteeFilter filter;

    public CommitteeAdapter(Context context, ArrayList<CommitteeModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.FilterList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_row,parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String uid = arrayList.get(position).getUid();
        String ids = arrayList.get(position).getId();
        String createdBy = arrayList.get(position).getCreatedBy();
        String time = arrayList.get(position).getTime();
        String email = arrayList.get(position).getEmail();
        String comm_name = arrayList.get(position).getComm_name();
        String desc_comm = arrayList.get(position).getDesc_comm();
        String phone = arrayList.get(position).getPhone();
        String about = arrayList.get(position).getAbout();
        holder.name.setText(arrayList.get(position).getComm_name());
        String images = arrayList.get(position).getImage();
        String pri = arrayList.get(position).getType();
        Glide.with(context).load(images).into(holder.image);
        String id = arrayList.get(position).getcomId();
        holder.catcho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommitteeProfileActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("uid",uid);
                context.startActivity(intent);
            }
        });

        if(pri.equals("Private")){
            holder.pri.setVisibility(View.VISIBLE);
            holder.pri.setText("Private");
        }else if(pri.equals("Public")){
            holder.pri.setVisibility(View.VISIBLE);
            holder.pri.setText("Public");
        }
        holder.lay.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CommitteeProfileActivity.class);
            intent.putExtra("id",id);
            intent.putExtra("uid",uid);
            intent.putExtra("comId",ids);
            intent.putExtra("image",images);
            intent.putExtra("create",createdBy);
            intent.putExtra("time",time);
            intent.putExtra("email",email);
            intent.putExtra("comm_name",comm_name);
            intent.putExtra("desc",desc_comm);
            intent.putExtra("phone",phone);
            intent.putExtra("about",about);
            intent.putExtra("type",pri);
            context.startActivity(intent);
        });

//        if(arrayList.get(position).getUid().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())){
//
//            holder.lay.setVisibility(View.GONE);
//            holder.itemView.setOnClickListener(v -> {
//                Intent intent = new Intent(context, CommitteeProfileActivity.class);
//                intent.putExtra("id",id);
//                intent.putExtra("uid",uid);
//                intent.putExtra("comId",ids);
//                intent.putExtra("image",images);
//                intent.putExtra("create",createdBy);
//                intent.putExtra("time",time);
//                intent.putExtra("email",email);
//                intent.putExtra("comm_name",comm_name);
//                intent.putExtra("desc",desc_comm);
//                intent.putExtra("phone",phone);
//                intent.putExtra("about",about);
//                intent.putExtra("type",pri);
//                context.startActivity(intent);
//            });
//
//        }else {
//            holder.lay.setVisibility(View.GONE);
//            holder.request.setVisibility(View.GONE);
//
//        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new CommitteeFilter(this,FilterList);
        }

        return filter;
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name,pri;
        private Button catcho,request;
        private LinearLayout lay;

        public Holder(@NonNull View itemView) {
            super(itemView);

            request = itemView.findViewById(R.id.request);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            catcho = itemView.findViewById(R.id.catcho);
            pri = itemView.findViewById(R.id.pri);
            lay = itemView.findViewById(R.id.lay);

        }
    }
}
