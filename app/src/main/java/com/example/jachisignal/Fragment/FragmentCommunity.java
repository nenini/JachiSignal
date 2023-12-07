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

import com.example.jachisignal.Doc.CommunityDoc;
import com.example.jachisignal.Doc.GongguDoc;
import com.example.jachisignal.Doc.GongguDoc2;
import com.example.jachisignal.Doc.JachiDoc;
import com.example.jachisignal.Doc.LeisureDoc;
import com.example.jachisignal.Doc.PreviewHolder1;
import com.example.jachisignal.Doc.PreviewHolder2;
import com.example.jachisignal.Doc.PreviewHolder3;
import com.example.jachisignal.Doc.PreviewHolder4;
import com.example.jachisignal.Doc.PreviewHolder5;
import com.example.jachisignal.Doc.PreviewHolder6;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.FragmentCommunityBinding;
import com.example.jachisignal.fragmentCommunity.FragmentCommunity1;
import com.example.jachisignal.fragmentCommunity.FragmentCommunity2;
import com.example.jachisignal.fragmentCommunity.FragmentCommunity3;
import com.example.jachisignal.fragmentHome.FragmentHome1;
import com.example.jachisignal.fragmentHome.FragmentHome2;
import com.example.jachisignal.fragmentHome.FragmentHome3;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.checkerframework.checker.units.qual.C;


public class FragmentCommunity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private FirestoreRecyclerAdapter adapter1,adapter2,adapter3;
    FirebaseFirestore db;

    FragmentCommunityBinding binding;
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
        binding = FragmentCommunityBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db=FirebaseFirestore.getInstance();

        ImageView commu=view.findViewById(R.id.community_commu_btn);
        commu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.menu_frame_layout, FragmentCommunity1.newInstance("param1","param2")).addToBackStack(null).commit();


            }
        });
        ImageView jachiItem=view.findViewById(R.id.community_jachiItem_btn);
        jachiItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.menu_frame_layout, FragmentCommunity2.newInstance("param1","param2")).addToBackStack(null).commit();
            }
        });
        ImageView cooking=view.findViewById(R.id.community_cook_btn);
        cooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.menu_frame_layout, FragmentCommunity3.newInstance("param1","param2")).addToBackStack(null).commit();
            }
        });


        LinearLayout commuPage=view.findViewById(R.id.community_commu_pageBTN);
        commuPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.menu_frame_layout, FragmentCommunity1.newInstance("param1","param2")).addToBackStack(null).commit();


            }
        });
        LinearLayout jachiItemPage=view.findViewById(R.id.community_jachiItem_pageBTN);
        jachiItemPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.menu_frame_layout, FragmentCommunity2.newInstance("param1","param2")).addToBackStack(null).commit();


            }
        });
        LinearLayout cookPage=view.findViewById(R.id.community_cook_pageBTN);
        cookPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.menu_frame_layout, FragmentCommunity3.newInstance("param1","param2")).addToBackStack(null).commit();


            }
        });

        makeRecyclerView1();
        makeRecyclerView2();
        makeRecyclerView3();
    }

    public void makeRecyclerView1(){
        Query query = db.collection("communityWritings")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(3);
        Log.d("KSM", "퀴리 문제 없음");
        FirestoreRecyclerOptions<CommunityDoc> options = new FirestoreRecyclerOptions.Builder<CommunityDoc>()
                .setQuery(query, CommunityDoc.class)
                .build();
        adapter1 = new FirestoreRecyclerAdapter<CommunityDoc, PreviewHolder4>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PreviewHolder4 holder, int position, @NonNull CommunityDoc model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public PreviewHolder4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_community1, parent, false);
                return new PreviewHolder4(view);
            }
        };
        binding.communityRecyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.communityRecyclerView1.setAdapter(adapter1);
    }
    public void makeRecyclerView2(){
        Query query = db.collection("jachitemWritings")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(3);
        Log.d("KSM", "퀴리 문제 없음");
        FirestoreRecyclerOptions<JachiDoc> options = new FirestoreRecyclerOptions.Builder<JachiDoc>()
                .setQuery(query, JachiDoc.class)
                .build();

        adapter2 = new FirestoreRecyclerAdapter<JachiDoc, PreviewHolder5>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PreviewHolder5 holder, int position, @NonNull JachiDoc model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public PreviewHolder5 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_community2, parent, false);
                return new PreviewHolder5(view);
            }
        };
        binding.communityRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.communityRecyclerView2.setAdapter(adapter2);
    }

    public void makeRecyclerView3(){
        Query query = db.collection("recipeWritings")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(3);
        Log.d("KSM", "퀴리 문제 없음");
        FirestoreRecyclerOptions<RecipeDoc> options = new FirestoreRecyclerOptions.Builder<RecipeDoc>()
                .setQuery(query, RecipeDoc.class)
                .build();

        adapter3 = new FirestoreRecyclerAdapter<RecipeDoc, PreviewHolder6>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PreviewHolder6 holder, int position, @NonNull RecipeDoc model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public PreviewHolder6 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_community3, parent, false);
                return new PreviewHolder6(view);
            }
        };
        binding.communityRecyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.communityRecyclerView3.setAdapter(adapter3);
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