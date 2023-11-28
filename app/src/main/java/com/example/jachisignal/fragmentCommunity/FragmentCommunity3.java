package com.example.jachisignal.fragmentCommunity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.jachisignal.FindPasswordActivity;
import com.example.jachisignal.MainActivity;
import com.example.jachisignal.R;
import com.example.jachisignal.RecipeDoc;
import com.example.jachisignal.RecipeDocHolder;
import com.example.jachisignal.WritingActivity.RecipeWritingActivity;
import com.example.jachisignal.databinding.FragmentCommunity3Binding;
import com.example.jachisignal.databinding.ItemBinding;
import com.example.jachisignal.fragmentHome.FragmentHome2;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCommunity3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCommunity3 extends Fragment {
    private FirestoreRecyclerAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCommunity3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCommunity3.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCommunity3 newInstance(String param1, String param2) {
        FragmentCommunity3 fragment = new FragmentCommunity3();
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
    FragmentCommunity3Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentCommunity3Binding.inflate(inflater,container,false);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        //글쓰기 버튼 click
        ImageButton recipe_BTN=view.findViewById(R.id.community3_write_btn);
        recipe_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RecipeWritingActivity.class));
            }
        });

        Query query= FirebaseFirestore.getInstance()
                        .collection("recipeWritings")
                        .orderBy("timestamp")
                        .limit(50);
        FirestoreRecyclerOptions<RecipeDoc> options=new FirestoreRecyclerOptions.Builder<RecipeDoc>()
                .setQuery(query,RecipeDoc.class)
                        .build();
        adapter=new FirestoreRecyclerAdapter<RecipeDoc, RecipeDocHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RecipeDocHolder holder, int position, @NonNull RecipeDoc model) {
                holder.bind(model);
            }
            @NonNull
            @Override
            public RecipeDocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item,parent,false);
                return new RecipeDocHolder(view);
            }
        };

        binding.community3RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.community3RecyclerView.setAdapter(adapter);
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        private ItemBinding binding;


        private  MyViewHolder(ItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        ArrayList<RecipeDoc> recipeDocArrayList;

        public MyAdapter(ArrayList<RecipeDoc> recipeDocArrayList){
            this.recipeDocArrayList=recipeDocArrayList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemBinding binding=ItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            RecipeDoc recipeDoc = recipeDocArrayList.get(position);
            holder.binding.category.setText(recipeDoc.getCategory());
            holder.binding.nickname.setText(recipeDoc.getNickname());
            holder.binding.heartCount.setText(Integer.toString(recipeDoc.getLikeList().size())+"개");

        }

        @Override
        public int getItemCount() {
            return recipeDocArrayList.size();
        }
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