package com.handlings.shipcargo.fragment;

import static com.handlings.shipcargo.Contants.CREATECARGO;
import static com.handlings.shipcargo.Contants.HISTORY;
import static com.handlings.shipcargo.Contants.TODAY;
import static com.handlings.shipcargo.Contants.UPDATECARGO;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.handlings.shipcargo.Contants;
import com.handlings.shipcargo.R;
import com.handlings.shipcargo.SessionManager;
import com.handlings.shipcargo.Utility;
import com.handlings.shipcargo.adapter.HistoryRecyclerAdapter;
import com.handlings.shipcargo.adapter.TodayRecycler;
import com.handlings.shipcargo.models.CargoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CyclicBarrier;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressBar MainMenuProgressSchedule;

    SessionManager sessionManager;
    Utility utility;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    HistoryRecyclerAdapter todayRecycler;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_schedule, container, false);

        MainMenuProgressSchedule =  root.findViewById(R.id.MainMenuProgressSchedule);

        sessionManager =  new SessionManager(getContext());
        utility =  new Utility(getContext(),sessionManager,getActivity());

        recyclerView =  root.findViewById(R.id.recycleridView2);

        getItems();


        return  root;
    }

    void getItems() {
        CyclicBarrier barrier = new CyclicBarrier(2);
//        utility.getData(UPDATECARGO,HISTORY);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Authorization","Token "+sessionManager.getUserDetails().get(Contants.TOKEN))
                .url(CREATECARGO)
                .build();

        Call j =  client.newCall(request);
        j.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainMenuProgressSchedule.setVisibility(View.GONE);
                        Toast.makeText(getContext(),"An error occured",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String ju =  response.body().string();
                ArrayList<CargoModel> cargoModels = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(ju);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject =  jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String cargoName = jsonObject.getString("cargo_name");
                        String pickUp = jsonObject.getString("pick_up");
                        JSONObject vehicle =  jsonObject.getJSONObject("vehicle_type");
                        String vehicleType = vehicle.getString("category_name");
                        String destination =  jsonObject.getString("desitnation");
                        String createdTime =  jsonObject.getString("created_at");
                        String Price =  jsonObject.getString("price");
                        String location_description = jsonObject.getString("location_description");
                        String phone = jsonObject.getString("phone_number");
                        Boolean accepted =  jsonObject.getBoolean("accepted");
                        Boolean arrived = jsonObject.getBoolean("arrived");
                        if(arrived == true){
                            cargoModels.add(new CargoModel(id,cargoName,pickUp,vehicleType,destination,createdTime,Price,location_description,phone,accepted,arrived));

                        }



                    }
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainMenuProgressSchedule.setVisibility(View.GONE);
                            linearLayoutManager =  new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(linearLayoutManager);
                            todayRecycler = new HistoryRecyclerAdapter(cargoModels,getContext());
                            recyclerView.setAdapter(todayRecycler);
                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });




//
//        ArrayList<CargoModel> cargoModels = new ArrayList<>();
//        HashMap response = sessionManager.getDetails(HISTORY);
//        String today = (String) response.get(HISTORY);
//        System.out.println("data in history : " + today);
//        if(today != null){
//            try {
//
//                JSONArray jsonArray = new JSONArray(today);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject =  jsonArray.getJSONObject(i);
//                    int id = jsonObject.getInt("id");
//                    String cargoName = jsonObject.getString("cargo_name");
//                    String pickUp = jsonObject.getString("pick_up");
//                    JSONObject vehicle =  jsonObject.getJSONObject("vehicle_type");
//                    String vehicleType = vehicle.getString("category_name");
//                    String destination =  jsonObject.getString("desitnation");
//                    String createdTime =  jsonObject.getString("created_at");
//                    String Price =  jsonObject.getString("price");
//                    String location_description = jsonObject.getString("location_description");
//                    String phone = jsonObject.getString("phone_number");
//                    cargoModels.add(new CargoModel(id,cargoName,pickUp,vehicleType,destination,createdTime,Price,location_description,phone));
//
////
//                }
//
//                linearLayoutManager =  new LinearLayoutManager(getContext());
//                recyclerView.setLayoutManager(linearLayoutManager);
//                todayRecycler = new HistoryRecyclerAdapter(cargoModels,getContext());
//                recyclerView.setAdapter(todayRecycler);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }



    }
}