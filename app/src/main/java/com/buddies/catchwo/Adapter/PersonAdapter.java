package com.buddies.catchwo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buddies.catchwo.Model.Professional;
import com.buddies.catchwo.R;
import com.buddies.catchwo.Support.Common;
import com.buddies.catchwo.Support.IRecItemSelected;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.Holder> {

    Context context;
    ArrayList<Professional> arrayList;
    List<CardView>  cardViews;
    LocalBroadcastManager localBroadcastManager;

    public PersonAdapter(Context context, ArrayList<Professional> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        cardViews = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.person_lay,parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

         holder.name.setText(arrayList.get(position).getName());
         holder.rate.setRating((float) arrayList.get(position).getRating());
         Picasso.get().load(arrayList.get(position).getImage()).into(holder.prof_image);


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
                intent.putExtra(Common.KEY_PROF_PERSON_SELECTED,arrayList.get(position));
                intent.putExtra(Common.KEY_STEP,2);
                localBroadcastManager.sendBroadcast(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        ImageView prof_image;
        TextView name;
        RatingBar rate;
        CardView card;

        IRecItemSelected iRecItemSelected;


        public void setiRecItemSelected(IRecItemSelected iRecItemSelected) {
            this.iRecItemSelected = iRecItemSelected;
        }

        public Holder(@NonNull View itemView) {
            super(itemView);

            rate = itemView.findViewById(R.id.rate);
            prof_image = itemView.findViewById(R.id.prof_image);
            name = itemView.findViewById(R.id.name);
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
