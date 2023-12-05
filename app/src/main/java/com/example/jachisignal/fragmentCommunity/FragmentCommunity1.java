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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jachisignal.Doc.CommunityDoc;
import com.example.jachisignal.Doc.CommunityDocHolder;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.PostActivity.Post_Inside_Community;
import com.example.jachisignal.PostActivity.Post_Inside_Jachitem;
import com.example.jachisignal.R;
import com.example.jachisignal.WritingActivity.CommunityWritingActivity;
import com.example.jachisignal.databinding.FragmentCommunity1Binding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCommunity1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCommunity1 extends Fragment {

    private FirestoreRecyclerAdapter adapter;
    private FirestoreRecyclerOptions<CommunityDoc> options;

    ArrayAdapter<CharSequence> adspin1,adspin2;
    String choice_si_show = "전체";
    String choice_gu_show ="전체";

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

    EditText frag1_search_text;

//    ImageButton search_BTN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentCommunity1Binding.inflate(inflater,container,false);
        checkBox = binding.communityQuestionShow;
        update_BTN = binding.communityUpdate;

        Spinner spin1_show = binding.spinnerShow;
        Spinner spin2_show = binding.spinner2Show;

        adspin1 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do, android.R.layout.simple_spinner_dropdown_item);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1_show.setAdapter(adspin1);
        spin1_show.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adspin1.getItem(position).equals("전체")) {
                    choice_si_show = "전체";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(),R.array.spinner_do_entire, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2_show.setAdapter(adspin2);
                    spin2_show.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu_show = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: 전체임"+choice_si_show+choice_gu_show);
                            updateQuery(checkBox.isChecked(), frag1_search_text.getText().toString(),choice_si_show,choice_gu_show);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if(adspin1.getItem(position).equals("서울")) {
                    choice_si_show = "서울";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_seoul, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2_show.setAdapter(adspin2);
                    spin2_show.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu_show = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: 서울임"+choice_si_show+choice_gu_show);
                            updateQuery(checkBox.isChecked(), frag1_search_text.getText().toString(),choice_si_show,choice_gu_show);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("인천")) {
                    choice_si_show = "인천";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_incheon, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2_show.setAdapter(adspin2);
                    spin2_show.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu_show = adspin2.getItem(position).toString();
                            updateQuery(checkBox.isChecked(), frag1_search_text.getText().toString(),choice_si_show,choice_gu_show);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("ksh", "onCheckedChanged: 질문글 체크");
                updateQuery(isChecked, frag1_search_text.getText().toString(),choice_si_show,choice_gu_show);
            }
        });

        update_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateQuery(checkBox.isChecked(), frag1_search_text.getText().toString(),choice_si_show,choice_gu_show);


            }
        });



        ImageButton search_BTN = binding.searchBtn;
        frag1_search_text = binding.searchWord;
        search_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuery(checkBox.isChecked(), frag1_search_text.getText().toString(),choice_si_show,choice_gu_show);
            }

        });

        updateQuery(false,"",choice_si_show,choice_gu_show);
        // Inflate the layout for this fragment

        Log.d("ksh", "onCreateView: 맨처음 다 뜨게");
        return binding.getRoot();
    }


    public void updateQuery(boolean isChecked, String text,String si,String gu) {
        Log.d("ksh", "updateQuery"+text);

        Query baseQuery = FirebaseFirestore.getInstance()
                .collection("communityWritings")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        if(isChecked) {
            Log.d("ksh", "updateQuery: 나중에 질문글 체크했을 때");
            baseQuery = baseQuery.whereEqualTo("isQuestion",true);
        }
        Log.d("ksh", "updateQuery: text 안 들어옴");
        if(text.getBytes().length>0) {
            Log.d("ksh", "updateQuery: text 들어옴");
            baseQuery = baseQuery.whereEqualTo("contentTitle",text);
        }
        if(si.equals("전체")){
            Log.d("ksh", "updateQuery: 전체"+si+gu);
        }
        if(!si.equals("전체")) {
            Log.d("ksh", "updateQuery:시 전체 아님"+si+gu);
            baseQuery = baseQuery.whereEqualTo("siName",si);
            if(!gu.equals("전체")) {
                Log.d("ksh", "updateQuery: 구 전체 아님"+si+gu);
                baseQuery = baseQuery
                        .whereEqualTo("guName",gu);
            }
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
                    Log.d("ksh", "onCreateViewHolder: 들어옴");
                    View view= LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_community,parent,false);
                    return new CommunityDocHolder(view);
                }
            };
            Log.d("ksh", "updateAdapter: setLayoutManager");
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


