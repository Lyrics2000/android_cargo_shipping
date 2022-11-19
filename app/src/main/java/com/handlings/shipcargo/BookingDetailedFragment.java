package com.handlings.shipcargo;

import static com.handlings.shipcargo.Contants.CARGOID;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.handlings.shipcargo.fragment.ViewtrackFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookingDetailedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookingDetailedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView priceUpNoneJodetailed,phoneNumberDetails,pickupdetails,destinationAdd,locationDESCIPR,pickupDATEtIME;

    public BookingDetailedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookingDetailedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookingDetailedFragment newInstance(String param1, String param2) {
        BookingDetailedFragment fragment = new BookingDetailedFragment();
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
    Calendar calendar;
    private  TextView calendayDAY ,theMonth,today_day;
    private Button btn_decline,btn_accept;
    private View view1,view2;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_booking_detailed, container, false);
        priceUpNoneJodetailed =  root.findViewById(R.id.priceUpNoneJodetailed);
        phoneNumberDetails =  root.findViewById(R.id.phoneNumberDetails);
        pickupdetails =  root.findViewById(R.id.pickupdetails);
        destinationAdd =  root.findViewById(R.id.destinationAdd);
        locationDESCIPR =  root.findViewById(R.id.locationDESCIPR);
        pickupDATEtIME =  root.findViewById(R.id.pickupDATEtIME);
        calendayDAY = root.findViewById(R.id.calendayDAY_detailed);
        theMonth = root.findViewById(R.id.theMonth_detailed);
        today_day =  root.findViewById(R.id.today_day_detailed);
        btn_decline =  root.findViewById(R.id.btn_decline);
        btn_accept =  root.findViewById(R.id.btn_accept);
        view1 =  root.findViewById(R.id.view1);
        view2 =  root.findViewById(R.id.view2);

        bundle =  getArguments();

        priceUpNoneJodetailed.setText("Ksh "+ bundle.getString(Contants.PRICE));
        phoneNumberDetails.setText(bundle.getString(Contants.PHONE));
        pickupdetails.setText(bundle.getString(Contants.PICKUP));
        destinationAdd.setText(bundle.getString(Contants.DESTINATION));
        locationDESCIPR.setText(bundle.getString(Contants.LOCATIONDESCRIPTION));
        calendar = Calendar.getInstance();
        pickupDATEtIME.setText(bundle.getString(Contants.PICKUPDATE));


        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b =  new Bundle();
                b.putInt(CARGOID,bundle.getInt(CARGOID));

                replaceFragment(new ViewtrackFragment(),b);
            }
        });

        if(bundle.getString(Contants.BUTTONVISIBLE) == Contants.FALSE){
            btn_accept.setVisibility(View.GONE);
//            btn_decline.setVisibility(View.GONE);
//            view2.setVisibility(View.GONE);
//            view1.setVisibility(View.GONE);
        }else  if(bundle.getString(Contants.BUTTONVISIBLE) == Contants.TRUE){
            btn_accept.setVisibility(View.VISIBLE);
        }

        getToday();

        return  root;
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


    public void replaceFragment (Fragment fragment, Bundle bundle){
        AppCompatActivity activity = (AppCompatActivity) getContext();

        FragmentManager fragmentManager =  activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container,fragment);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();

    }

}