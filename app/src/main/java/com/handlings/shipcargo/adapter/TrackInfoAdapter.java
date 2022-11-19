package com.handlings.shipcargo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.handlings.shipcargo.R;
import com.handlings.shipcargo.models.TrackModel;

import java.util.ArrayList;

public class TrackInfoAdapter extends RecyclerView.Adapter<TrackInfoAdapter.MyViewClass>{

    ArrayList<TrackModel> trackModels;
    Context context;

    public TrackInfoAdapter(ArrayList<TrackModel> trackModels, Context context) {
        this.trackModels = trackModels;
        this.context = context;
    }



    @NonNull
    @Override
    public TrackInfoAdapter.MyViewClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_info_layout,parent,false);
        TrackInfoAdapter.MyViewClass myViewClass =  new TrackInfoAdapter.MyViewClass(view);
        return myViewClass;
    }

    @Override
    public void onBindViewHolder(@NonNull TrackInfoAdapter.MyViewClass holder, int position) {
        holder.trackDescription.setText(trackModels.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return trackModels.size();
    }


    public class  MyViewClass extends  RecyclerView.ViewHolder{

        TextView trackDescription;


        public MyViewClass(@NonNull View itemView) {
            super(itemView);
            trackDescription =  itemView.findViewById(R.id.trackDescription);


        }
    }


}
