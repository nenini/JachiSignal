package com.example.jachisignal.fragmentCommunity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jachisignal.Doc.CommunityDoc;
import com.example.jachisignal.Doc.CommunityDocHolder;
import com.example.jachisignal.PostActivity.Post_Inside_Community;
import com.example.jachisignal.PostActivity.Post_Inside_Jachitem;
import com.example.jachisignal.R;
import com.example.jachisignal.WritingActivity.CommunityWritingActivity;
import com.example.jachisignal.databinding.FragmentCommunity1Binding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCommunity1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCommunity1 extends Fragment {

    private FirestoreRecyclerAdapter adapter;
    private FirestoreRecyclerOptions<CommunityDoc> options;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCommunity1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCommunity1.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCommunity1 newInstance(String param1, String param2) {
        FragmentCommunity1 fragment = new FragmentCommunity1();
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

    FragmentCommunity1Binding binding;
    CheckBox checkBox;

    Button update_BTN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentCommunity1Binding.inflate(inflater,container,false);
        checkBox = binding.communityQuestionShow;
        update_BTN = binding.communityUpdate;

//        String str;
//        Bundle bundle = getArguments();
//        if(bundle != null) {
//            str = bundle.getString("checked");
//            Log.d("ksh", "onCreateView: bundle 들어옴 "+ str);
//            if("true".equals(str)){
//                updateQuery(true);
//            }
//        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateQuery(isChecked);
            }

            public void checkCheck(boolean isChecked) {
                updateQuery(isChecked);
            }
        });

        update_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuery(checkBox.isChecked());
            }
        });



        updateQuery(false);
        // Inflate the layout for this fragment

        Log.d("ksh", "onCreateView: 맨처음 다 뜨게");
        return binding.getRoot();
    }


    public void updateQuery(boolean isChecked) {
        Log.d("ksh", "updateQuery");
        Query baseQuery = FirebaseFirestore.getInstance()
                .collection("communityWritings")
                .orderBy("timestamp");

        if(isChecked) {
            Log.d("ksh", "updateQuery: 나중에 질문글 체크했을때");
            baseQuery = baseQuery.whereEqualTo("isQuestion",true);
        }
        Query finalQuery = baseQuery.limit(50);

        FirestoreRecyclerOptions<CommunityDoc> options=new FirestoreRecyclerOptions.Builder<CommunityDoc>()
                .setQuery(finalQuery,CommunityDoc.class)
                .build();

        updateAdapter(options);


    }

    private void updateAdapter(FirestoreRecyclerOptions<CommunityDoc> options) {
        Log.d("ksh", "updateAdapter: 들어옴1");
        if(adapter == null) {
            Log.d("ksh", "updateAdapter: 들어옴 null");
            adapter=new FirestoreRecyclerAdapter<CommunityDoc, CommunityDocHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull CommunityDocHolder holder, int position, @NonNull CommunityDoc model) {
                    holder.bind(model);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String title = holder.getmTitle().getText().toString();

                            Intent intent = new Intent(v.getContext(), Post_Inside_Community.class);
                            intent.putExtra("COLLECTION","communityWritings");
                            intent.putExtra("DOCUMENT",title);
                            Log.d("KSM", "인텐트 전달");
                            startActivity(intent);
                        }
                    });
                }

                @NonNull
                @Override
                public CommunityDocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view= LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_community,parent,false);
                    return new CommunityDocHolder(view);
                }
            };

            binding.community1RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.community1RecyclerView.setAdapter(adapter);
        } else {
            Log.d("ksh", "updateAdapter: 들어옴 else");
            adapter.updateOptions(options);
            binding.community1RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.community1RecyclerView.setAdapter(adapter);

        }
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        ImageButton community_BTN = view.findViewById(R.id.community1_write_btn);
        community_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CommunityWritingActivity.class));
            }
        });


//        binding.getRoot().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("ksh", "onClick: 클릭함");
//                updateQuery(checkBox.isChecked());
//            }
//        });

//        binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    updateQuery(checkBox.isChecked());
//                }
//                return false;
//            }
//        });



    }


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


