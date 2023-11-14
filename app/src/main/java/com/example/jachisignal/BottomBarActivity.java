package com.example.jachisignal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ImageView;

public class BottomBarActivity extends AppCompatActivity {
    private FragmentHome fragmentHome;
    private FragmentCommunity fragmentCommunity;
    private ImageView buttonHome;
    private ImageView buttonCommunity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentHome=new FragmentHome();
        fragmentCommunity=new FragmentCommunity();

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentFrame,fragmentHome);
        fragmentTransaction.commit();

        buttonHome = findViewById(R.id.bar_home_btn);
        buttonCommunity = findViewById(R.id.bar_community_btn);

        buttonHome.setOnClickListener(v->{
            FragmentManager fm1=getSupportFragmentManager();
            FragmentTransaction ft1=fragmentManager.beginTransaction();
            ft1.replace(R.id.fragmentFrame,fragmentHome);
            ft1.commit();
        });
        buttonCommunity.setOnClickListener(v->{
            FragmentManager fm2=getSupportFragmentManager();
            FragmentTransaction ft2=fragmentManager.beginTransaction();
            ft2.replace(R.id.fragmentFrame,fragmentCommunity);
            ft2.commit();
        });
    }
}