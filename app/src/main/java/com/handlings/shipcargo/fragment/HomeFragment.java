package com.handlings.shipcargo.fragment;

import static android.service.controls.ControlsProviderService.TAG;
import static com.handlings.shipcargo.Contants.CREATECARGO;
import static com.handlings.shipcargo.Contants.CURRENTLOCATION;
import static com.handlings.shipcargo.Contants.TODAY;
import static com.handlings.shipcargo.Contants.TOKEN;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handlings.shipcargo.Contants;
import com.handlings.shipcargo.MainActivity;
import com.handlings.shipcargo.R;
import com.handlings.shipcargo.SessionManager;
import com.handlings.shipcargo.Utility;
import com.handlings.shipcargo.adapter.TodayRecycler;
import com.handlings.shipcargo.models.CargoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView today_day, calendayDAY, theMonth, priceUpNoneJo;
    Calendar calendar;
    ProgressBar MainMenuProgress;

    private static int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static int MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66;
    private static int PERMISSION_ALL = 1;

    public HomeFragment() {
        // Required empty public constructor
    }

    SessionManager sessionManager;
    Utility utility;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    TodayRecycler todayRecycler;

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        sessionManager = new SessionManager(getContext());
        utility = new Utility(getContext(), sessionManager, getActivity());

        MainMenuProgress = root.findViewById(R.id.MainMenuProgress);

        recyclerView = root.findViewById(R.id.recycleridView);
        today_day = root.findViewById(R.id.today_day);
        calendayDAY = root.findViewById(R.id.calendayDAY);
        theMonth = root.findViewById(R.id.theMonth);
        priceUpNoneJo = root.findViewById(R.id.priceUpNoneJo);
        calendar = Calendar.getInstance();
//        getVehiclePrice();
//        getToday();
//
//
//
//
//        getItems();
//
//
        checkLocationPermission();
        return root;
    }


    void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                requestLocationPermission();

                System.out.println("permission granted");
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission();
            }
        } else {
            getVehiclePrice();
            getToday();
            getItems();

            getLocationForUser();
        }
    }

    private void getLocationForUser() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            Toast.makeText(getContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(TAG, latitude);

            saveToDbLocation(loc.getLatitude(),loc.getLongitude());



            /*------- To get city name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName;

            System.out.println("address  "+ s);

        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    private void saveToDbLocation(double latitude, double longitude) {

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("lat", String.valueOf(latitude))
                .add("lon", String.valueOf(longitude))

                .build();

        Request request ;
        if(sessionManager.isLoggedIn()){
            request =  new Request.Builder()
                    .header("Authorization","Token "+sessionManager.getUserDetails().get(Contants.TOKEN))
                    .url(CURRENTLOCATION)
                    .post(formBody)
                    .build();
        }else{
            request  = new Request.Builder()
                    .url(CURRENTLOCATION)
                    .post(formBody)
                    .build();
        }


        Call k = client.newCall(request);
        k.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("there is a failure in the systeem");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                System.out.println("saved info");



            }
        });

    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    void requestLocationPermission() {
        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,

        };

        if (!hasPermissions(getContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }
    }


    void  checkBackgroundLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }else {
            getVehiclePrice();
            getToday();




            getItems();
        }
    }

    void getVehiclePrice(){


        OkHttpClient client = new OkHttpClient();
        System.out.println("Token "+sessionManager.getUserDetails().get(Contants.TOKEN));

        if(sessionManager.isLoggedIn()){
            System.out.println("user logged in");
            Request request = new Request.Builder()
                    .header("Authorization","Token "+sessionManager.getUserDetails().get(Contants.TOKEN))
                    .url(Contants.GETPRICE)
                    .build();

            Call j =  client.newCall(request);
            j.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("failed terribly hhhhya");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                    if (response.code() ==  200){
                        String ju =  response.body().string();
                        System.out.println("the body "+ju);
//
                        try {

                                JSONObject jsonObject = new JSONObject(ju);
                                JSONObject total = jsonObject.getJSONObject("total");

                                double price__sum =  total.getDouble("price__sum");
                                System.out.println("the price " + price__sum);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        priceUpNoneJo.setText("Ksh "+ String.valueOf(price__sum));


                                    }
                                });



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


//                    }else{
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getContext(),"error occured",Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }

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
                                JSONObject total = jsonObject.getJSONObject("total");
                                double price__sum =  total.getDouble("price__sum");
                                System.out.println("the price " + price__sum);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        priceUpNoneJo.setText("Ksh "+ String.valueOf(price__sum));


                                    }
                                });


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
    void getToday() {
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH);
        String Year = String.valueOf(calendar.get(Calendar.YEAR));
        switch (day) {
            case Calendar.SUNDAY:
                // Current day is Sunday
                calendayDAY.setText("Sunday");
                break;
            case Calendar.MONDAY:
                // Current day is Monday
                calendayDAY.setText("Monday");
                break;
            case Calendar.TUESDAY:
                calendayDAY.setText("Tuesday");
                // etc.
                break;
            case Calendar.WEDNESDAY:
                calendayDAY.setText("Wednesday");
                break;
            case Calendar.THURSDAY:
                calendayDAY.setText("Thursday");
                break;

            case Calendar.FRIDAY:
                calendayDAY.setText("Friday");
                break;


            case  Calendar.SATURDAY:
                calendayDAY.setText("Saturday");
                break;
        }

        switch (month){
            case Calendar.JANUARY:
                theMonth.setText("January "+Year );
                break;
            case Calendar.FEBRUARY:
                theMonth.setText("february "+Year );
                break;
            case  Calendar.MARCH:
                theMonth.setText("March "+Year );
                break;
            case  Calendar.APRIL:
                theMonth.setText("April "+Year );
                break;
            case  Calendar.MAY:
                theMonth.setText("May "+Year );
                break;
            case  Calendar.JUNE:
                theMonth.setText("June "+Year );
                break;
            case Calendar.JULY:
                theMonth.setText("July "+Year );
                break;
            case Calendar.AUGUST:
                theMonth.setText("August "+Year );
                break;
            case Calendar.SEPTEMBER:
                theMonth.setText("September "+Year );
                break;
            case Calendar.OCTOBER:
                theMonth.setText("October "+Year );
                break;
            case  Calendar.NOVEMBER:
                theMonth.setText("November "+Year );
                break;
            case  Calendar.DECEMBER:
                theMonth.setText("December "+Year );
                break;


        }

        Date c = Calendar.getInstance().getTime();


        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        String d = formattedDate.split("-")[0];
        System.out.println("Current time => " + d);
        today_day.setText(d);

    }

     void getItems() {
         CyclicBarrier barrier = new CyclicBarrier(2);
//         utility.getData(CREATECARGO,TODAY);

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
                         Toast.makeText(getContext(),"Error getting data",Toast.LENGTH_SHORT).show();
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

                     System.out.println("qtttruurbbgavfsgfvdgs " + arrived);

                     cargoModels.add(new CargoModel(id,cargoName,pickUp,vehicleType,destination,createdTime,Price,location_description,phone,accepted,arrived));


                 }
                     requireActivity().runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             MainMenuProgress.setVisibility(View.GONE);
                             linearLayoutManager =  new LinearLayoutManager(getContext());
                             recyclerView.setLayoutManager(linearLayoutManager);
                             todayRecycler = new TodayRecycler(cargoModels,getContext());
                             recyclerView.setAdapter(todayRecycler);
                         }
                     });



                 } catch (JSONException e) {
                     e.printStackTrace();
                 }


             }
         });






//         ArrayList<CargoModel> cargoModels = new ArrayList<>();
//         HashMap response = sessionManager.getDetails(TODAY);
//        String today = (String) response.get(TODAY);
//         try {
//             System.out.println("data in : " + today);
//             if (today != null){
//                 JSONArray jsonArray = new JSONArray(today);
//                 for (int i = 0; i < jsonArray.length(); i++) {
//                     JSONObject jsonObject =  jsonArray.getJSONObject(i);
//                     int id = jsonObject.getInt("id");
//                     String cargoName = jsonObject.getString("cargo_name");
//                     String pickUp = jsonObject.getString("pick_up");
//                     JSONObject vehicle =  jsonObject.getJSONObject("vehicle_type");
//                     String vehicleType = vehicle.getString("category_name");
//                     String destination =  jsonObject.getString("desitnation");
//                     String createdTime =  jsonObject.getString("created_at");
//                     String Price =  jsonObject.getString("price");
//                     String location_description = jsonObject.getString("location_description");
//                     String phone = jsonObject.getString("phone_number");
//                     cargoModels.add(new CargoModel(id,cargoName,pickUp,vehicleType,destination,createdTime,Price,location_description,phone));
//
//
//                 }
//
//                 linearLayoutManager =  new LinearLayoutManager(getContext());
//                 recyclerView.setLayoutManager(linearLayoutManager);
//                 todayRecycler = new TodayRecycler(cargoModels,getContext());
//                 recyclerView.setAdapter(todayRecycler);
//             }
//
//         } catch (JSONException e) {
//             e.printStackTrace();
//         }


    }
}