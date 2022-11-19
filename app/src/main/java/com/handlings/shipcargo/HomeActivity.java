package com.handlings.shipcargo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.handlings.shipcargo.fragment.AddFragment;
import com.handlings.shipcargo.fragment.HomeFragment;
import com.handlings.shipcargo.fragment.ProfileFragment;
import com.handlings.shipcargo.fragment.ScheduleFragment;

public class HomeActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        replaceFragment(new HomeFragment());

        bottomNavigationView =  findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.home_menu:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.menu_add:
                    replaceFragment(new AddFragment());
                    break;

                case R.id.menu_schedule:
                    replaceFragment(new ScheduleFragment());
                    break;
//                case R.id.menu_profilee:
//                    replaceFragment(new ProfileFragment());
//                    break;
                default:
                    Toast.makeText(getApplicationContext(),"Please select and id to continue",Toast.LENGTH_SHORT).show();

            }


            return  true;
        });



    }

    public void replaceFragment (Fragment fragment){

        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container,fragment);
        fragmentTransaction.commit();

    }
}