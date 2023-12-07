package com.example.jachisignal.fragmentCommunity;

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
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.jachisignal.Doc.JachiDoc;
import com.example.jachisignal.Doc.JachiDocHolder;
import com.example.jachisignal.Doc.JachiRankHolder;
import com.example.jachisignal.PostActivity.Post_Inside_Jachitem;
import com.example.jachisignal.R;
import com.example.jachisignal.WritingActivity.JachitemWritingActivity;
import com.example.jachisignal.databinding.FragmentCommunity2Binding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCommunity2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCommunity2 extends Fragment {
    private FirestoreRecyclerAdapter adapter;
    private FirestoreRecyclerAdapter adapter_rank;

    private FirestoreRecyclerOptions<JachiDoc> options;
    private FirestoreRecyclerOptions<JachiDoc> options_rank;


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
        Log.d("ksh", "onCreate: 여기인가");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    FragmentCommunity2Binding binding;

    EditText frag2_search_text;

    ImageButton search_BTN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCommunity2Binding.inflate(inflater, container, false);

        search_BTN = binding.searchBtnRecipe;
        frag2_search_text = binding.searchText;


        search_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuery(frag2_search_text.getText().toString());
            }

        });

        updateQuery("");

        Log.d("ksh", "onCreateView: 맨처음 다 뜨게");

        return binding.getRoot();
    }

    public void updateQuery(String text) {
        Log.d("ksh", "updateQuery"+text);

        Query baseQuery= FirebaseFirestore.getInstance()
                .collection("jachitemWritings")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        if(text.getBytes().length > 0) {
            Log.d("ksh", "updateQuery: text 들어옴");
            baseQuery = baseQuery.where(Filter.or(
                    Filter.arrayContains("contentArray",text),
                    Filter.equalTo("category",text)
            ));
        }
        Query finalQuery = baseQuery.limit(50);

        Query rankQuery= FirebaseFirestore.getInstance()
                .collection("jachitemWritings")
                .orderBy("likeListCount", Query.Direction.DESCENDING)
                .limit(3);

        FirestoreRecyclerOptions<JachiDoc> options=new FirestoreRecyclerOptions.Builder<JachiDoc>()
                .setQuery(finalQuery,JachiDoc.class)
                .build();

        FirestoreRecyclerOptions<JachiDoc> options_rank=new FirestoreRecyclerOptions.Builder<JachiDoc>()
                .setQuery(rankQuery,JachiDoc.class)
                .build();

        updateAdapter(options,options_rank);

    }


    private void updateAdapter(FirestoreRecyclerOptions<JachiDoc> options,FirestoreRecyclerOptions<JachiDoc> options_rank) {
        Log.d("ksh", "updateAdapter: 들어옴");
        if (adapter == null&&adapter_rank==null) {
            Log.d("ksh", "updateAdapter: 들어옴 null");
            adapter = new FirestoreRecyclerAdapter<JachiDoc, JachiDocHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull JachiDocHolder holder, int position, @NonNull JachiDoc model) {
                    holder.bind(model);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String title = holder.getmTitle().getText().toString();

                            Intent intent = new Intent(v.getContext(), Post_Inside_Jachitem.class);
                            intent.putExtra("COLLECTION", "jachitemWritings");
                            intent.putExtra("DOCUMENT", title);
                            Log.d("KSM", "인텐트 전달");
                            startActivity(intent);
                        }
                    });
                }

                @NonNull
                @Override
                public JachiDocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    Log.d("ksh", "onCreateViewHolder_jachidocholder: 들어옴");
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_jachi, parent, false);
                    return new JachiDocHolder(view);
                }
            };

            Log.d("ksh", "updateAdapter_rank: 들어옴 null");
            adapter_rank = new FirestoreRecyclerAdapter<JachiDoc, JachiRankHolder>(options_rank) {
                @Override
                protected void onBindViewHolder(@NonNull JachiRankHolder holder, int position, @NonNull JachiDoc model) {
                    holder.bind(model);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String title = holder.getmTitle().getText().toString();

                            Intent intent = new Intent(v.getContext(), Post_Inside_Jachitem.class);
                            intent.putExtra("COLLECTION", "jachitemWritings");
                            intent.putExtra("DOCUMENT", title);
                            Log.d("KSM", "인텐트 전달");
                            startActivity(intent);
                        }
                    });
                }

                @NonNull
                @Override
                public JachiRankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    Log.d("ksh", "onCreateViewHolder_jachirankholder: 들어옴");

                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_c2, parent, false);
                    return new JachiRankHolder(view);
                }
            };

            binding.community2RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.community2RecyclerView.setAdapter(adapter);
            binding.community2RankRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            binding.community2RankRecyclerView.setAdapter(adapter_rank);
        } else {
            adapter.updateOptions(options);
            binding.community2RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.community2RecyclerView.setAdapter(adapter);
            binding.community2RankRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            binding.community2RankRecyclerView.setAdapter(adapter_rank);
        }
    }

    //    public void updateQuery_rank() {
//        Log.d("ksh", "updateQuery_rank");
//
//        Query rackQuery= FirebaseFirestore.getInstance()
//                .collection("jachitemWritings")
//                .orderBy("likeList", Query.Direction.DESCENDING)
//                .limit(3);
//
//        FirestoreRecyclerOptions<JachiDoc> options=new FirestoreRecyclerOptions.Builder<JachiDoc>()
//                .setQuery(rackQuery,JachiDoc.class)
//                .build();
//
//        updateAdapter_rank(options);
//    }
//
//
//    private void updateAdapter_rank(FirestoreRecyclerOptions<JachiDoc> options) {
//        Log.d("ksh", "updateAdapter_rank");
//        if (adapter_rank == null) {
//            Log.d("ksh", "updateAdapter_rank: 들어옴 null");
//            adapter_rank = new FirestoreRecyclerAdapter<JachiDoc, JachiRankHolder>(options) {
//                @Override
//                protected void onBindViewHolder(@NonNull JachiRankHolder holder, int position, @NonNull JachiDoc model) {
//                    holder.bind(model);
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            String title = holder.getmTitle().getText().toString();
//
//                            Intent intent = new Intent(v.getContext(), Post_Inside_Jachitem.class);
//                            intent.putExtra("COLLECTION", "jachitemWritings");
//                            intent.putExtra("DOCUMENT", title);
//                            Log.d("KSM", "인텐트 전달");
//                            startActivity(intent);
//                        }
//                    });
//                }
//
//                @NonNull
//                @Override
//                public JachiRankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                    Log.d("ksh", "onCreateViewHolder_jachirankholder: 들어옴");
//
//                    View view = LayoutInflater.from(parent.getContext())
//                            .inflate(R.layout.item_c2, parent, false);
//                    return new JachiRankHolder(view);
//                }
//            };
//
//            binding.community2RankRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
//            binding.community2RankRecyclerView.setAdapter(adapter_rank);
//        } else {
//            adapter.updateOptions(options);
//            binding.community2RankRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
//            binding.community2RankRecyclerView.setAdapter(adapter_rank);
//        }
//    }
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
        adapter_rank.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
        adapter_rank.startListening();
    }



}
