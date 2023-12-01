package com.example.jachisignal.fragmentCommunity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.jachisignal.Doc.JachiDoc;
import com.example.jachisignal.Doc.JachiDocHolder;
import com.example.jachisignal.R;
import com.example.jachisignal.WritingActivity.CommunityWritingActivity;
import com.example.jachisignal.WritingActivity.JachitemWritingActivity;
import com.example.jachisignal.WritingActivity.RecipeWritingActivity;
import com.example.jachisignal.databinding.FragmentCommunity2Binding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCommunity2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCommunity2 extends Fragment {
    private FirestoreRecyclerAdapter adapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCommunity2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCommunity2.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCommunity2 newInstance(String param1, String param2) {
        FragmentCommunity2 fragment = new FragmentCommunity2();
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
    FragmentCommunity2Binding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentCommunity2Binding.inflate(inflater,container,false);
        Query query= FirebaseFirestore.getInstance()
                .collection("jachitemWritings")
                .orderBy("timestamp")
                .limit(50);
        FirestoreRecyclerOptions<JachiDoc> options=new FirestoreRecyclerOptions.Builder<JachiDoc>()
                .setQuery(query,JachiDoc.class)
                .build();
        adapter=new FirestoreRecyclerAdapter<JachiDoc, JachiDocHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull JachiDocHolder holder, int position, @NonNull JachiDoc model) {
                holder.bind(model);
            }
            @NonNull
            @Override
            public JachiDocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_jachi,parent,false);
                return new JachiDocHolder(view);
            }
        };

        binding.community2RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.community2RecyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return binding.getRoot();

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        ImageButton jachitem_BTN=view.findViewById(R.id.community2_write_btn);
        jachitem_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), JachitemWritingActivity.class));
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
        adapter.startListening();    }



}