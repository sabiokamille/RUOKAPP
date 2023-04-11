package com.example.ruokapp.Adapter;

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
import com.example.ruokapp.Model.Preferences;
import com.example.ruokapp.R;

import java.util.ArrayList;
import java.util.List;

public class MyPreferencesAdapter extends RecyclerView.Adapter<MyPreferencesAdapter.MyViewHolder> {

    Context context;
    List<Preferences> preferencesList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyPreferencesAdapter(Context context, List<Preferences> preferencesList) {
        this.context = context;
        this.preferencesList = preferencesList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_preferences,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_preferences_name.setText(preferencesList.get(position).getName());
        holder.txt_preferences_address.setText(preferencesList.get(position).getAddress());

        if(!cardViewList.contains(holder.card_preferences))
            cardViewList.add(holder.card_preferences);

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Set white background for cards not selected
                for(CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));

                //Set selected color for item(THIS COULD BE SOMETHING IF SOMETHING IS WONKY)
                holder.card_preferences.setCardBackgroundColor(ContextCompat.getColor(context, R.color.pink));

                //Send Broadcast to tell Booking Activity enable Button next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_PREFERENCES_STORE, preferencesList.get(pos));
                intent.putExtra(Common.KEY_STEP,1);
                localBroadcastManager.sendBroadcast(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return preferencesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_preferences_name, txt_preferences_address;
        CardView card_preferences;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_preferences = (CardView)itemView.findViewById(R.id.card_preferences);

            txt_preferences_address = (TextView) itemView.findViewById(R.id.txt_preferences_address);
            txt_preferences_name = (TextView) itemView.findViewById(R.id.txt_preferences_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v, getAbsoluteAdapterPosition()); //Check this out if something doesn't work out


        }
    }
}
