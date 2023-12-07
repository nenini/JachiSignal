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

import com.example.jachisignal.Doc.RecipeRankHolder;
import com.example.jachisignal.PostActivity.Post_Inside_Recipe;
import com.example.jachisignal.R;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.WritingActivity.RecipeWritingActivity;
import com.example.jachisignal.databinding.FragmentCommunity3Binding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCommunity3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCommunity3 extends Fragment {

    private FirestoreRecyclerAdapter adapter;
    private FirestoreRecyclerAdapter adapter_rank;
    private FirestoreRecyclerOptions<RecipeDoc> options;
    private FirestoreRecyclerOptions<RecipeDoc> options_rank;

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

    EditText frag3_search_text;

    ImageButton search_BTN;
    //String search_option_string = "title";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentCommunity3Binding.inflate(inflater,container,false);

//        spinner = binding.spinner;
//        // spinner.setOnItemSelectedListener(this);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch(spinner.getItemAtPosition(position).toString()) {
//                    case "title" :
//                        search_option_string = "title";
//                        break;
//
//                    case "category" :
//                        search_option_string = "category";
//                        break;
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

        search_BTN = binding.searchBtnRecipe;
        frag3_search_text= binding.searchText;



        search_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuery(frag3_search_text.getText().toString());
            }

        });


        updateQuery("");

        Log.d("ksh", "onCreateView: 맨처음 다 뜨게");



        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void updateQuery(String text) {
        Log.d("ksh", "updateQuery"+text);

        Query baseQuery= FirebaseFirestore.getInstance()
                .collection("recipeWritings")
                .orderBy("timestamp",Query.Direction.DESCENDING);

        if(text.getBytes().length > 0) {
            Log.d("ksh", "updateQuery: text 들어옴");
            baseQuery = baseQuery.where(Filter.or(
                    Filter.arrayContains("contentArray",text),
                    Filter.equalTo("category",text)
            ));
        }
        Query finalQuery = baseQuery.limit(50);

        Query rankQuery= FirebaseFirestore.getInstance()
                .collection("recipeWritings")
                .orderBy("likeList", Query.Direction.DESCENDING)
                .limit(3);

        FirestoreRecyclerOptions<RecipeDoc> options=new FirestoreRecyclerOptions.Builder<RecipeDoc>()
                .setQuery(finalQuery,RecipeDoc.class)
                .build();

        FirestoreRecyclerOptions<RecipeDoc> options_rank=new FirestoreRecyclerOptions.Builder<RecipeDoc>()
                .setQuery(rankQuery,RecipeDoc.class)
                .build();

        updateAdapter(options,options_rank);

    }

    private  void updateAdapter(FirestoreRecyclerOptions<RecipeDoc> options,FirestoreRecyclerOptions<RecipeDoc> options_rank) {
        Log.d("ksh", "updateAdapter: 들어옴1");

        if(adapter == null&&adapter_rank==null) {
            Log.d("ksh", "updateAdapter: 들어옴 null");
            adapter=new FirestoreRecyclerAdapter<RecipeDoc, RecipeRankHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull RecipeRankHolder holder, int position, @NonNull RecipeDoc model) {
                    holder.bind(model);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("KSM", "Gggg");
                            String title = holder.getmTitle().getText().toString();

                            Log.d("KSM", "eeeee");
                            Intent intent = new Intent(v.getContext(), Post_Inside_Recipe.class);
                            intent.putExtra("COLLECTION","recipeWritings");
                            Log.d("KSM", title);
                            intent.putExtra("DOCUMENT",title);
                            Log.d("KSM", title);
                            startActivity(intent);
                        }
                    });
                }
                @NonNull
                @Override
                public RecipeRankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    Log.d("ksh", "onCreateViewHolder: 들어옴");
                    View view= LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item,parent,false);
                    return new RecipeRankHolder(view);
                }
            };

            Log.d("ksh", "updateAdapter: 들어옴 null");
            adapter_rank=new FirestoreRecyclerAdapter<RecipeDoc, RecipeRankHolder>(options_rank) {
                @Override
                protected void onBindViewHolder(@NonNull RecipeRankHolder holder, int position, @NonNull RecipeDoc model) {
                    holder.bind(model);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("KSM", "Gggg");
                            String title = holder.getmTitle().getText().toString();

                            Log.d("KSM", "eeeee");
                            Intent intent = new Intent(v.getContext(), Post_Inside_Recipe.class);
                            intent.putExtra("COLLECTION","recipeWritings");
                            Log.d("KSM", title);
                            intent.putExtra("DOCUMENT",title);
                            Log.d("KSM", title);
                            startActivity(intent);
                        }
                    });
                }
                @NonNull
                @Override
                public RecipeRankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    Log.d("ksh", "onCreateViewHolder: 들어옴");
                    View view= LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_c3,parent,false);
                    return new RecipeRankHolder(view);
                }
            };


            Log.d("ksh", "updateAdapter: setLayoutManager");
            binding.community3RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.community3RecyclerView.setAdapter(adapter);
            binding.community3RankRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            binding.community3RankRecyclerView.setAdapter(adapter_rank);
        } else {
            Log.d("ksh", "updateAdapter: 들어옴 else");
            adapter.updateOptions(options);
            binding.community3RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.community3RecyclerView.setAdapter(adapter);
            binding.community3RankRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            binding.community3RankRecyclerView.setAdapter(adapter_rank);
        }
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



    }

//    public class MyViewHolder extends  RecyclerView.ViewHolder{
//        private ItemBinding binding;
//
//
//        private  MyViewHolder(ItemBinding binding){
//            super(binding.getRoot());
//            this.binding=binding;
//        }
//    }
//
//    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
//        ArrayList<RecipeDoc> recipeDocArrayList;
//
//        public MyAdapter(ArrayList<RecipeDoc> recipeDocArrayList){
//            this.recipeDocArrayList=recipeDocArrayList;
//        }
//
//        @NonNull
//        @Override
//        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            ItemBinding binding=ItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
//            return new MyViewHolder(binding);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//            RecipeDoc recipeDoc = recipeDocArrayList.get(position);
//            holder.binding.category.setText(recipeDoc.getCategory());
//            holder.binding.nickname.setText(recipeDoc.getNickname());
//            holder.binding.heartCount.setText(Integer.toString(recipeDoc.getLikeList().size())+"개");
//        }
//
//        @Override
//        public int getItemCount() {
//            return recipeDocArrayList.size();
//        }
//
//    }


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
