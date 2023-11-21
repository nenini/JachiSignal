package com.example.jachisignal.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jachisignal.R;
import com.example.jachisignal.fragmentHome.FragmentHome1;
import com.example.jachisignal.fragmentHome.FragmentHome2;
import com.example.jachisignal.fragmentHome.FragmentHome3;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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
//        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//                // We use a String here, but any type that can be put in a Bundle is supported
//                String resultString = result.getString("bundleKey");
//                // Do something with the result...
//                textView.setText(resultString);
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView gonggu_face=view.findViewById(R.id.home_gonggu_face_btn);
        gonggu_face.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.menu_frame_layout, FragmentHome1.newInstance("param1","param2")).addToBackStack(null).commit();


            }
        });
        ImageView gonggu_del=view.findViewById(R.id.home_gonggu_deli_btn);
        gonggu_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.menu_frame_layout, FragmentHome2.newInstance("param1","param2")).addToBackStack(null).commit();
            }
        });
        ImageView playing=view.findViewById(R.id.home_playing_btn);
        playing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.menu_frame_layout, FragmentHome3.newInstance("param1","param2")).addToBackStack(null).commit();
            }
        });
        LinearLayout gongguPage=view.findViewById(R.id.home_gonggu_pageBTN);
        gongguPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.menu_frame_layout, FragmentHome1.newInstance("param1","param2")).addToBackStack(null).commit();


            }
        });
        LinearLayout gongguPage_deli=view.findViewById(R.id.home_gonggu_deli_pageBTN);
        gongguPage_deli.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.menu_frame_layout, FragmentHome2.newInstance("param1","param2")).addToBackStack(null).commit();


            }
        });
        LinearLayout playingPage=view.findViewById(R.id.home_playing_pageBTN);
        playingPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.menu_frame_layout, FragmentHome3.newInstance("param1","param2")).addToBackStack(null).commit();


            }
        });

    }
}