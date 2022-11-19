package com.handlings.shipcargo.fragment;

import static com.handlings.shipcargo.Contants.TODAY;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.handlings.shipcargo.Contants;
import com.handlings.shipcargo.MainActivity;
import com.handlings.shipcargo.R;
import com.handlings.shipcargo.SessionManager;
import com.handlings.shipcargo.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import kotlin.jvm.internal.PropertyReference0Impl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<String> country = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner spinenr;

    private  String spinenerText = "";

    SessionManager sessionManager;
    ProgressBar progressBarSubmit;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    private EditText cargoNameMmM,phoneMmmmm,pickUpOneUppp,destinatiimmma,locationdescsiis;
    private Button btn_submitiii;
    private  Utility utility;
    private  ArrayAdapter aa;
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
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
        View root =  inflater.inflate(R.layout.fragment_add, container, false);
        sessionManager =  new SessionManager(getContext());
        getVehicleTypes();
        utility = new Utility(getContext(),sessionManager, getActivity());

        spinenr = root.findViewById(R.id.spinenr);

        progressBarSubmit =  root.findViewById(R.id.progressBarSubmit);
        cargoNameMmM =  root.findViewById(R.id.cargoNameMmM);
        phoneMmmmm =  root.findViewById(R.id.phoneMmmmm);
        pickUpOneUppp =  root.findViewById(R.id.pickUpOneUppp);
        destinatiimmma =  root.findViewById(R.id.destinatiimmma);
        locationdescsiis =  root.findViewById(R.id.locationdescsiis);
        btn_submitiii =  root.findViewById(R.id.btn_submitiii);


        aa = new ArrayAdapter(getContext(),R.layout.spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinenr.setAdapter(aa);
        spinenr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                aa.notifyDataSetChanged();
                System.out.println("clicked...");

                int pos = adapterView.getSelectedItemPosition();
                Toast.makeText(getContext(),country.get(pos) , Toast.LENGTH_LONG).show();
//                ((TextView) adapterView.getChildAt(0)).setTextColor(getContext().getResources().getColor(R.color.black));
//                spinenerText = country.get(i);

//                System.out.println("this is selected "+country);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_submitiii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarSubmit.setVisibility(View.VISIBLE);
                String cargoName =  cargoNameMmM.getText().toString();
                String phone = phoneMmmmm.getText().toString();
                String pickUp =  pickUpOneUppp.getText().toString();
                String destination =  destinatiimmma.getText().toString();
                String locationDescription =  locationdescsiis.getText().toString();

                if(cargoName.isEmpty() || phone.isEmpty() || pickUp.isEmpty() || destination.isEmpty() || locationDescription.isEmpty() ){
                    progressBarSubmit.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Fill in all details",Toast.LENGTH_SHORT).show();
                }else{
//                    int uuro = Integer.parseInt(spinenerText.split(".")[0]);

                    sendToDb(cargoName,phone,pickUp,destination,locationDescription,1);
                }



            }
        });

        return  root;
    }



    private void sendToDb(String cargoName, String phone, String pickUp, String destination, String locationDescription, int spinenerText) {
        RequestBody formBody = new FormBody.Builder()
                .add("cargo_name", cargoName)
                .add("phone_number",phone)
                .add("pick_up",pickUp)
                .add("destinanition",destination)
                .add("location_description",locationDescription)
                .add("vehicle_type", String.valueOf(spinenerText))
                .build();
        utility.sendPost(Contants.CREATECARGO,formBody,Contants.SAVETODB,progressBarSubmit);

    }

    void getVehicleTypes(){

             OkHttpClient client = new OkHttpClient();
             System.out.println("Token "+sessionManager.getUserDetails().get(Contants.TOKEN));

             if(sessionManager.isLoggedIn()){
                 Request request = new Request.Builder()
                         .header("Authorization","Token "+sessionManager.getUserDetails().get(Contants.TOKEN))
                         .url(Contants.ALLCATEGORY)
                         .build();

                 Call j =  client.newCall(request);
                 j.enqueue(new Callback() {
                     @Override
                     public void onFailure(Call call, IOException e) {
                         System.out.println("failed terribly");
                     }

                     @Override
                     public void onResponse(Call call, Response response) throws IOException {
                         if (response.code() ==  200){
                             String ju =  response.body().string();
//
                             try {
                                 JSONArray jsonArray = new JSONArray(ju);
                                 for (int i = 0; i < jsonArray.length(); i++) {
                                     JSONObject jsonObject = jsonArray.getJSONObject(i);
                                     String categoryName = jsonObject.getString("category_name");
                                     int id = jsonObject.getInt("id");
                                     country.add(String.valueOf(id) + "."+ " "+ categoryName);

                                 }
                             } catch (JSONException e) {
                                 e.printStackTrace();
                             }


                         }else{
                             getActivity().runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                    Toast.makeText(getContext(),"error occured",Toast.LENGTH_SHORT).show();
                                 }
                             });
                         }

                     }
                 });
             }else{
                 Request request = new Request.Builder()
                         .url(Contants.ALLCATEGORY)
                         .build();

                 Call  m = client.newCall(request);
                 m.enqueue(new Callback() {
                     @Override
                     public void onFailure(Call call, IOException e) {
                         System.out.println("failed vibaya sana");
                     }

                     @Override
                     public void onResponse(Call call, Response response) throws IOException {
                         if(response.code() == 200){
                             String ju =  response.body().string();
                             try {
                                 JSONArray jsonArray = new JSONArray(ju);
                                 for (int i = 0; i < jsonArray.length(); i++) {
                                     JSONObject jsonObject = jsonArray.getJSONObject(i);
                                     String categoryName = jsonObject.getString("category_name");
                                     country.add(categoryName);

                                 }
                             } catch (JSONException e) {
                                 e.printStackTrace();
                             }

                         }else {
                             getActivity().runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     Toast.makeText(getContext(),"error occured",Toast.LENGTH_SHORT).show();
                                 }
                             });
                         }

                     }
                 });
             }





     }
}