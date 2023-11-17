package com.example.jachisignal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.jachisignal.fragmentCommunity.FragmentCommunity1;
import com.example.jachisignal.fragmentCommunity.FragmentCommunity2;
import com.example.jachisignal.fragmentCommunity.FragmentCommunity3;
import com.example.jachisignal.fragmentHome.FragmentHome1;
import com.example.jachisignal.fragmentHome.FragmentHome2;
import com.example.jachisignal.fragmentHome.FragmentHome3;


public class FragmentCommunity extends Fragment {
    FragmentCommunity1 fragmentCommunity1;
    FragmentCommunity2 fragmentCommunity2;
    FragmentCommunity3 fragmentCommunity3;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCommunity() {
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
    public static FragmentCommunity newInstance(String param1, String param2) {
        FragmentCommunity fragment = new FragmentCommunity();
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
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_community,container,false) ;
        fragmentCommunity1=new FragmentCommunity1();
        fragmentCommunity2=new FragmentCommunity2();
        fragmentCommunity3=new FragmentCommunity3();

        ImageView commu=rootView.findViewById(R.id.community_commu_btn);
        commu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().replace(R.id.fragment_community_tab, fragmentCommunity1).commit();
            }
        });
        ImageView item=rootView.findViewById(R.id.community_jachiItem_btn);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().replace(R.id.fragment_community_tab, fragmentCommunity2).commit();
            }
        });
        ImageView cook=rootView.findViewById(R.id.community_cook_btn);
        cook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().replace(R.id.fragment_community_tab, fragmentCommunity3).commit();
            }
        });


        return rootView;
    }
}