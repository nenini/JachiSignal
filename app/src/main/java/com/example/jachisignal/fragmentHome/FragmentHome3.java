package com.example.jachisignal.fragmentHome;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.jachisignal.Doc.LeisureDoc;
import com.example.jachisignal.Doc.LeisureDocHolder;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.Doc.RecipeDocHolder;
import com.example.jachisignal.PostActivity.Post_Inside_Jachitem;
import com.example.jachisignal.PostActivity.Post_Inside_Playing;
import com.example.jachisignal.R;
import com.example.jachisignal.WritingActivity.LeisureWritingActivity;
import com.example.jachisignal.WritingActivity.RecipeWritingActivity;
import com.example.jachisignal.databinding.FragmentCommunity3Binding;
import com.example.jachisignal.databinding.FragmentHome3Binding;
import com.example.jachisignal.databinding.Item2Binding;
import com.example.jachisignal.databinding.ItemBinding;
import com.example.jachisignal.fragmentCommunity.FragmentCommunity3;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome3 extends Fragment {
    private FirestoreRecyclerAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentHome3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome3.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome3 newInstance(String param1, String param2) {
        FragmentHome3 fragment = new FragmentHome3();
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
    FragmentHome3Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentHome3Binding.inflate(inflater,container,false);
        Query query= FirebaseFirestore.getInstance()
                .collection("leisureWritings")
                .orderBy("timestamp")
                .limit(50);
        FirestoreRecyclerOptions<LeisureDoc> options=new FirestoreRecyclerOptions.Builder<LeisureDoc>()
                .setQuery(query,LeisureDoc.class)
                .build();
        adapter=new FirestoreRecyclerAdapter<LeisureDoc, LeisureDocHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LeisureDocHolder holder, int position, @NonNull LeisureDoc model) {
                holder.bind(model);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = holder.getmTitle().getText().toString();

                        Intent intent = new Intent(v.getContext(), Post_Inside_Playing.class);
                        intent.putExtra("COLLECTION","leisureWritings");
                        intent.putExtra("DOCUMENT",title);
                        Log.d("KSM", "인텐트 전달");
                        startActivity(intent);
                    }
                });
            }
            @NonNull
            @Override
            public LeisureDocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item2,parent,false);
                return new LeisureDocHolder(view);
            }
        };

        binding.home3RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.home3RecyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        //글쓰기 버튼 click
        ImageButton leisure_BTN=view.findViewById(R.id.home3_write_btn);
        leisure_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LeisureWritingActivity.class));
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();

    }
}