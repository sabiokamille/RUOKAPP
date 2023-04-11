package com.example.ruokapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ruokapp.Common.Common;
import com.example.ruokapp.Interface.IRecyclerItemSelectedListener;
import com.example.ruokapp.Model.TimeSlot;
import com.example.ruokapp.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {

    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;


    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();

        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();

    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;

        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position) {

        holder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(position).toString()));

        if(timeSlotList.size() == 0)
        {
            holder.card_time_slot.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.txt_time_slot_description.setText("Available");
            holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.black, context.getTheme())); //THIS COULD BE A PROBLEM IF SOMETHING DOESN'T WORK
            holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black, context.getTheme())); //THIS AS WELL :/


        }
        else //POSITION IS FULLY BOOKED
        {
            for(TimeSlot slotValue:timeSlotList)
            {
                int slot = Integer.parseInt(slotValue.getSlot().toString());
                if(slot == position)
                {
                    holder.card_time_slot.setTag(Common.DISABLE_TAG);

                    holder.card_time_slot.setCardBackgroundColor(ContextCompat.getColor(context, R.color.deep_pink));
                    holder.txt_time_slot_description.setText("Full");
                    holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.white, context.getTheme()));
                    holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.white, context.getTheme()));
                }
            }
        }

        if(!cardViewList.contains(holder.card_time_slot))
            cardViewList.add(holder.card_time_slot);

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {

                //LOOP ALL CARD IN CARD LIST
                for(CardView cardView:cardViewList)
                {
                    if(cardView.getTag() == null) //ONLY AVAILABLE
                        cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
                }

                holder.card_time_slot.setCardBackgroundColor(ContextCompat.getColor(context, R.color.pink));

                //SEND BROADCAST TO ENABLE BUTTON NEXT
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                //THE PROBLEM CAN BE HERE INCASE SOMETHING HAPPENS
                intent.putExtra(Common.KEY_TIME_SLOT,position);
                intent.putExtra(Common.KEY_STEP,3); //I THOUGHT WE WOULD GO TO STEP 4
                localBroadcastManager.sendBroadcast(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_time_slot, txt_time_slot_description;
        CardView card_time_slot;
        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = (CardView)itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = (TextView)itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_description = (TextView)itemView.findViewById(R.id.txt_time_slot_description);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v,getAbsoluteAdapterPosition());
        }
    }
}
