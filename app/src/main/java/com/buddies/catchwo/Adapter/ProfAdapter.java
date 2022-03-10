package com.buddies.catchwo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buddies.catchwo.Model.ProfModel;
import com.buddies.catchwo.R;
import com.buddies.catchwo.Support.Common;
import com.buddies.catchwo.Support.IRecItemSelected;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ProfAdapter extends RecyclerView.Adapter<ProfAdapter.Holder> {

    Context context;
    List<ProfModel> list;
    List<CardView> cardViews;
    LocalBroadcastManager localBroadcastManager;

    public ProfAdapter(Context context, List<ProfModel> list) {
        this.context = context;
        this.list = list;
        cardViews = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pro_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.name.setText(list.get(position).getProf());
        holder.prof.setText(list.get(position).getAddress());

        Picasso.get().load(list.get(position).getImge()).into(holder.image);

        if(!cardViews.contains(holder.card)){
            cardViews.add(holder.card);
        }

        holder.setiRecItemSelected(new IRecItemSelected() {
            @Override
            public void onItemSelected(View view, int position) {

                for(CardView cardView:cardViews){
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                }

                holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.line));

                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_PROF_PERSON,list.get(position));
                intent.putExtra(Common.KEY_STEP,1);
                localBroadcastManager.sendBroadcast(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name,prof;
        CardView card;

        IRecItemSelected iRecItemSelected;


        public void setiRecItemSelected(IRecItemSelected iRecItemSelected) {
            this.iRecItemSelected = iRecItemSelected;
        }

        public Holder(@NonNull View itemView) {
            super(itemView);

            prof = itemView.findViewById(R.id.prof);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            card = itemView.findViewById(R.id.card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRecItemSelected.onItemSelected(v,getAdapterPosition());
                }
            });

        }
    }
}
