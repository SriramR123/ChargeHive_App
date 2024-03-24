package com.citsri.chargify;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.widget.RatingBar;
import android.widget.TextView;

import com.citsri.chargify.api.body.EVStation;


public class EVRecyAdapter extends RecyclerView.Adapter<EVRecyAdapter.ViewHolder> {

    Context context;

    ArrayList<EVStation> evStationList;



    public EVRecyAdapter(Context context,ArrayList<EVStation> evStationList){
        this.context = context;
        this.evStationList = evStationList;
    }


    @NonNull
    @Override
    public EVRecyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.ev_station_card_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EVRecyAdapter.ViewHolder holder, int position) {
        holder.evStationLocation.setText(evStationList.get(position).getSvStationAddress());
        holder.evStationName.setText(evStationList.get(position).getEvStationName());
        if(evStationList.get(position).getStatus().equals("closed")){
            holder.evStatusStatus.setTextColor(Color.RED);
        }else{
            holder.evStatusStatus.setTextColor(Color.GREEN);
        }
        holder.evStatusStatus.setText(evStationList.get(position).getStatus());
        holder.ratingBar.setRating((float) evStationList.get(position).getRating());

        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, EVstationFullDetails.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("evDetails",evStationList.get(position));
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return evStationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView evStationName,evStationLocation,evStatusStatus;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            evStationName = itemView.findViewById(R.id.ev_station_name);
            evStationLocation = itemView.findViewById(R.id.ev_station_loc);
            evStatusStatus = itemView.findViewById(R.id.ev_station_status);
            ratingBar = itemView.findViewById(R.id.ev_station_rating);

        }
    }
}
