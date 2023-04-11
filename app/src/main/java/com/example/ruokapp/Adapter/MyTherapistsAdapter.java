package com.example.ruokapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.ruokapp.Common.Common;
import com.example.ruokapp.Interface.IRecyclerItemSelectedListener;
import com.example.ruokapp.Model.Therapists;
import com.example.ruokapp.R;

import java.util.ArrayList;
import java.util.List;

public class MyTherapistsAdapter extends RecyclerView.Adapter<MyTherapistsAdapter.MyViewHolder> {

    Context context;
    List<Therapists> therapistsList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyTherapistsAdapter(Context context, List<Therapists> therapistsList) {
        this.context = context;
        this.therapistsList = therapistsList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_therapists,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_therapists_name.setText(therapistsList.get(position).getName());
        holder.ratingBar.setRating((float)therapistsList.get(position).getRating());

        if(!cardViewList.contains(holder.card_therapist))
            cardViewList.add(holder.card_therapist);

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {

                for(CardView cardView : cardViewList)
                {
                    cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
                }

                //SET BACKGROUND FOR CHOICE (WONKY MAYBE)
                holder.card_therapist.setCardBackgroundColor(ContextCompat.getColor(context, R.color.pink));

                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_THERAPIST_SELECTED, therapistsList.get(pos));
                intent.putExtra(Common.KEY_STEP,2);
                localBroadcastManager.sendBroadcast(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return therapistsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_therapists_name;
        RatingBar ratingBar;

        CardView card_therapist;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_therapist = (CardView)itemView.findViewById(R.id.card_therapist);

            txt_therapists_name = (TextView)itemView.findViewById(R.id.txt_therapists_name);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rtb_therapist);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v,getAbsoluteAdapterPosition());
        }
    }
}
