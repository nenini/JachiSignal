package com.example.jachisignal.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jachisignal.Doc.Chat;
import com.example.jachisignal.Doc.GongguDoc;
import com.example.jachisignal.Doc.GongguDoc2;
import com.example.jachisignal.Doc.LeisureDoc;
import com.example.jachisignal.Doc.PreviewHolder1;
import com.example.jachisignal.Doc.PreviewHolder2;
import com.example.jachisignal.Doc.PreviewHolder3;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.FragmentHomeBinding;
import com.example.jachisignal.fragmentHome.FragmentHome1;
import com.example.jachisignal.fragmentHome.FragmentHome2;
import com.example.jachisignal.fragmentHome.FragmentHome3;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment{

    private FirestoreRecyclerAdapter adapter1,adapter2,adapter3;
    FirebaseFirestore db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FragmentHomeBinding binding;

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
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

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

        makeRecyclerView1();
        makeRecyclerView2();
        makeRecyclerView3();
    }

    public void makeRecyclerView1(){
        Query query = db.collection("gongu1Writings")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(3);
        Log.d("KSM", "퀴리 문제 없음");
        FirestoreRecyclerOptions<GongguDoc> options = new FirestoreRecyclerOptions.Builder<GongguDoc>()
                .setQuery(query, GongguDoc.class)
                .build();
        adapter1 = new FirestoreRecyclerAdapter<GongguDoc, PreviewHolder1>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PreviewHolder1 holder, int position, @NonNull GongguDoc model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public PreviewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_home1, parent, false);
                return new PreviewHolder1(view);
            }
        };
        binding.homeRecyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.homeRecyclerView1.setAdapter(adapter1);
    }
    public void makeRecyclerView2(){
        Query query = db.collection("gongu2Writings")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(3);
        Log.d("KSM", "퀴리 문제 없음");
        FirestoreRecyclerOptions<GongguDoc2> options = new FirestoreRecyclerOptions.Builder<GongguDoc2>()
                .setQuery(query, GongguDoc2.class)
                .build();

        adapter2 = new FirestoreRecyclerAdapter<GongguDoc2, PreviewHolder2>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PreviewHolder2 holder, int position, @NonNull GongguDoc2 model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public PreviewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_home2, parent, false);
                return new PreviewHolder2(view);
            }
        };
        binding.homeRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.homeRecyclerView2.setAdapter(adapter2);
    }

    public void makeRecyclerView3(){
        Query query = db.collection("leisureWritings")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(3);
        Log.d("KSM", "퀴리 문제 없음");
        FirestoreRecyclerOptions<LeisureDoc> options = new FirestoreRecyclerOptions.Builder<LeisureDoc>()
                .setQuery(query, LeisureDoc.class)
                .build();

        adapter3 = new FirestoreRecyclerAdapter<LeisureDoc, PreviewHolder3>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PreviewHolder3 holder, int position, @NonNull LeisureDoc model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public PreviewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_home3, parent, false);
                return new PreviewHolder3(view);
            }
        };
        binding.homeRecyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.homeRecyclerView3.setAdapter(adapter3);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter1.startListening();
        adapter2.startListening();
        adapter3.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter1.startListening();
        adapter2.startListening();
        adapter3.startListening();
    }
}