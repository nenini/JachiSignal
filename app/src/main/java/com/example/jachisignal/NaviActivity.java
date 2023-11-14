package com.example.jachisignal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.jachisignal.databinding.ActivityNaviBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NaviActivity extends AppCompatActivity {
    private FragmentManager fragmentManager=getSupportFragmentManager();
    private FragmentHome fragmentHome=new FragmentHome();
    private FragmentCommunity fragmentCommunity=new FragmentCommunity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.menu_frame_layout, fragmentHome).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new ItemSelectedListener());

    }
    class ItemSelectedListener implements BottomNavigationView.OnItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if(menuItem.getItemId()==R.id.homeFragment){
                transaction.replace(R.id.menu_frame_layout, fragmentHome).commitAllowingStateLoss();
            }
            if(menuItem.getItemId()==R.id.communityFragment){
                transaction.replace(R.id.menu_frame_layout, fragmentCommunity).commitAllowingStateLoss();
            }

            return true;
        }
    }

}