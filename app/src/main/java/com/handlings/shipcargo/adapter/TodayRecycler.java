package com.handlings.shipcargo.adapter;

import static com.handlings.shipcargo.Contants.BUTTONVISIBLE;
import static com.handlings.shipcargo.Contants.CARGOID;
import static com.handlings.shipcargo.Contants.CARGONAME;
import static com.handlings.shipcargo.Contants.DESTINATION;
import static com.handlings.shipcargo.Contants.FALSE;
import static com.handlings.shipcargo.Contants.LOCATIONDESCRIPTION;
import static com.handlings.shipcargo.Contants.PHONE;
import static com.handlings.shipcargo.Contants.PICKUP;
import static com.handlings.shipcargo.Contants.PICKUPDATE;
import static com.handlings.shipcargo.Contants.PRICE;
import static com.handlings.shipcargo.Contants.TRUE;

import android.content.Context;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

public class TodayRecycler extends RecyclerView.Adapter<TodayRecycler.MyViewClass> {

    ArrayList<CargoModel> models;
    Context context;

    public TodayRecycler(ArrayList<CargoModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_schedule,parent,false);
        MyViewClass myViewClass =  new MyViewClass(view);
        return myViewClass;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewClass holder, int position) {

        System.out.println("this is started " +  models.get(position).getAccepted());

        if(models.get(position).getAccepted()){
            holder.cargoNameOne.setText(models.get(position).getCargoName());
            holder.vehicleTypeOne.setText(models.get(position).getVehicleType());
            holder.pickUpOne.setText(models.get(position).getPickUp());
            holder.destinationOne.setText(models.get(position).getDestination());
            holder.priceOne.setText("Ksh " +models.get(position).getPrice());
            holder.createdAtOne.setText(models.get(position).getCreatedTime());
            holder.linearLayoutHolderMain.setBackground(context.getDrawable(R.drawable.btn_ll));
            holder.cardviewClicked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b =  new Bundle();

                    b.putString(CARGONAME,models.get(position).getCargoName());
                    b.putInt(CARGOID,models.get(position).getId());
                    b.putString(PRICE,models.get(position).getPrice());
                    b.putString(PHONE,models.get(position).getPhone());
                    b.putString(DESTINATION,models.get(position).getDestination());
                    b.putString(PICKUP,models.get(position).getPickUp());
                    b.putString(PICKUPDATE,models.get(position).getCreatedTime());
                    b.putString(LOCATIONDESCRIPTION,models.get(position).getLocationDescription());
                    b.putString(BUTTONVISIBLE,TRUE);

                    replaceFragment(new BookingDetailedFragment(),b);
//                Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            holder.cargoNameOne.setText(models.get(position).getCargoName());
            holder.vehicleTypeOne.setText(models.get(position).getVehicleType());
            holder.pickUpOne.setText(models.get(position).getPickUp());
            holder.destinationOne.setText(models.get(position).getDestination());
            holder.priceOne.setText("Ksh " +models.get(position).getPrice());
            holder.linearLayoutHolderMain.setBackground(context.getDrawable(R.drawable.btn_l));
            holder.createdAtOne.setText(models.get(position).getCreatedTime());
            holder.cardviewClicked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b =  new Bundle();

                    b.putString(CARGONAME,models.get(position).getCargoName());
                    b.putInt(CARGOID,models.get(position).getId());
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

        TextView cargoNameOne,vehicleTypeOne,pickUpOne,destinationOne,createdAtOne,priceOne;
        CardView cardviewClicked;
        LinearLayout linearLayoutHolderMain;

        public MyViewClass(@NonNull View itemView) {
            super(itemView);
            cargoNameOne =  itemView.findViewById(R.id.cargoNameOne);
            vehicleTypeOne =  itemView.findViewById(R.id.vehicleTypeOne);
            pickUpOne =  itemView.findViewById(R.id.pickUpOne);
            destinationOne =  itemView.findViewById(R.id.destinationOne);
            createdAtOne =  itemView.findViewById(R.id.createdAtOne);
            priceOne =  itemView.findViewById(R.id.priceOne);
            cardviewClicked = itemView.findViewById(R.id.cardviewClicked);
            linearLayoutHolderMain =  itemView.findViewById(R.id.linearLayoutHolderMain);

        }
    }
}
