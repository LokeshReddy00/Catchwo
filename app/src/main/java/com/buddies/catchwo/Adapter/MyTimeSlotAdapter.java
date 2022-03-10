package com.buddies.catchwo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buddies.catchwo.Model.TimeSlot;
import com.buddies.catchwo.R;
import com.buddies.catchwo.Support.Common;
import com.buddies.catchwo.Support.IRecItemSelected;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.Holder> {

    Context context;
    ArrayList<TimeSlot> timeSlots;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlots = new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, ArrayList<TimeSlot> timeSlots) {
        this.context = context;
        this.timeSlots = timeSlots;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_slot,parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.txt_time.setText(new StringBuilder(Common.convertTimeSlotToString(position)).toString());
        if(timeSlots.size() == 0){
            holder.txt_time_ava.setText("Available");
            holder.txt_time_ava.setTextColor(context.getResources().getColor(R.color.black));
            holder.txt_time.setTextColor(context.getResources().getColor(R.color.black));
            holder.time_slot.setCardBackgroundColor(context.getResources().getColor(R.color.white));

        }else {

            for(TimeSlot slotValues : timeSlots){
                int slot = Integer.parseInt(slotValues.getSlot().toString());
                if(slot == position){

                    holder.time_slot.setTag(Common.DISABLE_TAG);

                    holder.txt_time_ava.setText("Booked");
                    holder.txt_time_ava.setTextColor(context.getResources().getColor(R.color.pink));
                    holder.txt_time.setTextColor(context.getResources().getColor(R.color.pink));
                    holder.time_slot.setCardBackgroundColor(context.getResources().getColor(R.color.app));

                }
            }


        }

        if(!cardViewList.contains(holder.time_slot)){
            cardViewList.add(holder.time_slot);
        }


        if(!timeSlots.contains(position)){
            holder.setiRecItemSelected(new IRecItemSelected() {
                @Override
                public void onItemSelected(View view, int position) {
                    for(CardView cardView:cardViewList){

                        if(cardView.getTag() == null){
                            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                        }
                    }

                    holder.time_slot.setCardBackgroundColor(context.getResources().getColor(R.color.line));

                    Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                    intent.putExtra(Common.KEY_TIME_SLOT,position);
                    intent.putExtra(Common.KEY_STEP,3);
                    localBroadcastManager.sendBroadcast(intent);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class Holder extends RecyclerView.ViewHolder {

        CardView time_slot;
        TextView txt_time,txt_time_ava;

        IRecItemSelected iRecItemSelected;


        public void setiRecItemSelected(IRecItemSelected iRecItemSelected) {
            this.iRecItemSelected = iRecItemSelected;
        }



        public Holder(@NonNull View itemView) {
            super(itemView);

            txt_time_ava = (TextView) itemView.findViewById(R.id.txt_time_ava);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            time_slot =  itemView.findViewById(R.id.time_slot);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRecItemSelected.onItemSelected(v,getAdapterPosition());
                }
            });

        }
    }
}
