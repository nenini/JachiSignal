package com.example.jachisignal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.jachisignal.databinding.ActivityNaviBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NaviActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        BottomNavigationView navigationBarView = findViewById(R.id.menu_bottom_navigation);
        transferTo(FragmentHome.newInstance("param1", "param2"));

        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.homeFragment) {
                    // Respond to navigation item 1 click
                    transferTo(FragmentHome.newInstance("param1", "param2"));
                    return true;
                }

                if (itemId == R.id.communityFragment) {
                    // Respond to navigation item 2 click
                    transferTo(FragmentCommunity.newInstance("param1", "param2"));
                    return true;
                }

                if (itemId == R.id.myPageFragment) {
                    // Respond to navigation item 3 click
                    transferTo(FragmentMyPage.newInstance("param1", "param2"));
                    return true;
                }

                if (itemId == R.id.settingFragment) {
                    // Respond to navigation item 4 click
                    transferTo(FragmentSetting.newInstance("param1", "param2"));
                    return true;
                }

                return false;
            }
        });

        navigationBarView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });
    }

    private void transferTo(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.menu_frame_layout, fragment);
        fragmentTransaction.commit();
    }

}