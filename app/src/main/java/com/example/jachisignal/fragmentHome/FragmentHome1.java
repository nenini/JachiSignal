package com.example.jachisignal.fragmentHome;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.jachisignal.Doc.GongguDoc;
import com.example.jachisignal.Doc.GongguDocHolder;
import com.example.jachisignal.Doc.JachiDoc;
import com.example.jachisignal.Doc.LeisureDoc;
import com.example.jachisignal.Doc.LeisureDocHolder;
import com.example.jachisignal.PostActivity.Post_Inside_09;
import com.example.jachisignal.PostActivity.Post_Inside_Playing;
import com.example.jachisignal.R;
import com.example.jachisignal.WritingActivity.GongguWritingActivity;
import com.example.jachisignal.WritingActivity.LeisureWritingActivity;
import com.example.jachisignal.databinding.FragmentHome1Binding;
import com.example.jachisignal.databinding.FragmentHome3Binding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome1 extends Fragment {
    private FirestoreRecyclerAdapter adapter;

    private FirestoreRecyclerOptions<GongguDoc> options;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentHome1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome1.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome1 newInstance(String param1, String param2) {
        FragmentHome1 fragment = new FragmentHome1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    FragmentHome1Binding binding;

    EditText home1_search_text;

    ImageButton search_BTN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHome1Binding.inflate(inflater, container, false);

        search_BTN = binding.searchBtnHome1;
        home1_search_text = binding.searchTextHome1;

        search_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuery(home1_search_text.getText().toString());
            }

        });

        updateQuery("");

        return binding.getRoot();
    }

    public void updateQuery(String text) {

        Query baseQuery = FirebaseFirestore.getInstance()
                .collection("gongu1Writings")
                .orderBy("timestamp");

        if (text.getBytes().length > 0) {
            Log.d("ksh", "updateQuery: text 들어옴");
            baseQuery = baseQuery.where(Filter.or(
                    Filter.equalTo("contentTitle", text),
                    Filter.equalTo("category", text),
                    Filter.equalTo("itemName", text)
            ));
        }
        Query finalQuery = baseQuery.limit(50);


        FirestoreRecyclerOptions<GongguDoc> options = new FirestoreRecyclerOptions.Builder<GongguDoc>()
                .setQuery(finalQuery, GongguDoc.class)
                .build();

        updateAdapter(options);
    }

    private void updateAdapter(FirestoreRecyclerOptions<GongguDoc> options) {
        if(adapter == null) {
            adapter = new FirestoreRecyclerAdapter<GongguDoc, GongguDocHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull GongguDocHolder holder, int position, @NonNull GongguDoc model) {
                    holder.bind(model);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String title = holder.getmTitle().getText().toString();

                            Intent intent = new Intent(v.getContext(), Post_Inside_09.class);
                            intent.putExtra("COLLECTION", "gongu1Writings");
                            intent.putExtra("DOCUMENT", title);
                            Log.d("KSM", "인텐트 전달");
                            startActivity(intent);
                        }
                    });
                }

                @NonNull
                @Override
                public GongguDocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item09, parent, false);
                    return new GongguDocHolder(view);
                }
            };

            binding.home1RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.home1RecyclerView.setAdapter(adapter);
        }else {
            adapter.updateOptions(options);
            binding.home1RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.home1RecyclerView.setAdapter(adapter);
        }


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        //글쓰기 버튼 click
        ImageButton gonggu_BTN=view.findViewById(R.id.home1_write_btn);
        gonggu_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GongguWritingActivity.class));
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