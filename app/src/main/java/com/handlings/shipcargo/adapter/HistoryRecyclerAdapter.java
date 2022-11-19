package com.handlings.shipcargo.adapter;

import static com.handlings.shipcargo.Contants.BUTTONVISIBLE;
import static com.handlings.shipcargo.Contants.CARGONAME;
import static com.handlings.shipcargo.Contants.DESTINATION;
import static com.handlings.shipcargo.Contants.FALSE;
import static com.handlings.shipcargo.Contants.LOCATIONDESCRIPTION;
import static com.handlings.shipcargo.Contants.PHONE;
import static com.handlings.shipcargo.Contants.PICKUP;
import static com.handlings.shipcargo.Contants.PICKUPDATE;
import static com.handlings.shipcargo.Contants.PRICE;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.handlings.shipcargo.BookingDetailedFragment;
import com.handlings.shipcargo.R;
import com.handlings.shipcargo.models.CargoModel;

import java.util.ArrayList;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.MyViewClass> {

    ArrayList<CargoModel> models;
    Context context;

    public HistoryRecyclerAdapter(ArrayList<CargoModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_schedule_2,parent,false);
        MyViewClass myViewClass =  new MyViewClass(view);
        return myViewClass;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewClass holder, int position) {
        holder.cargoNameTwo.setText(models.get(position).getCargoName());
        holder.vehicleTypeTwo.setText(models.get(position).getVehicleType());
        holder.pickUpTwo.setText(models.get(position).getPickUp());
        holder.destinationTwo.setText(models.get(position).getDestination());
        holder.priceTwo.setText("Ksh " +models.get(position).getPrice());
        holder.createdAtTwo.setText(models.get(position).getCreatedTime());

        holder.hllauud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b =  new Bundle();

                b.putString(CARGONAME,models.get(position).getCargoName());
                b.putString(PRICE,models.get(position).getPrice());
                b.putString(PHONE,models.get(position).getPhone());
                b.putString(DESTINATION,models.get(position).getDestination());
                b.putString(PICKUP,models.get(position).getPickUp());
                b.putString(PICKUPDATE,models.get(position).getCreatedTime());
                b.putString(LOCATIONDESCRIPTION,models.get(position).getLocationDescription());
                b.putString(BUTTONVISIBLE,FALSE);

                replaceFragment(new BookingDetailedFragment(),b);
//                Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void replaceFragment (Fragment fragment, Bundle bundle){
        AppCompatActivity activity = (AppCompatActivity) context;

        FragmentManager fragmentManager =  activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container,fragment);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();

    }
    @Override
    public int getItemCount() {
        return models.size();
    }

    public class  MyViewClass extends  RecyclerView.ViewHolder{

        TextView cargoNameTwo,vehicleTypeTwo,pickUpTwo,destinationTwo,createdAtTwo,priceTwo;
        CardView hllauud;

        public MyViewClass(@NonNull View itemView) {
            super(itemView);
            cargoNameTwo =  itemView.findViewById(R.id.cargoNameTwo);
            vehicleTypeTwo =  itemView.findViewById(R.id.vehicleTypeTwo);
            pickUpTwo =  itemView.findViewById(R.id.pickUpTwo);
            destinationTwo =  itemView.findViewById(R.id.destinationTwo);
            createdAtTwo =  itemView.findViewById(R.id.createdAtTwo);
            priceTwo =  itemView.findViewById(R.id.priceTwo);
            hllauud =  itemView.findViewById(R.id.hllauud);

        }
    }
}
