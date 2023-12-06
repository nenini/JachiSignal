package com.example.jachisignal.WritingActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.CommunityDoc;
import com.example.jachisignal.Doc.JachiDoc;
import com.example.jachisignal.MainActivity;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityCommunityWritingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommunityWritingActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    AppUser appUser;
    ActivityCommunityWritingBinding binding;

    ArrayAdapter<CharSequence> adspin1,adspin2;
    String choice_si = "전체";
    String choice_gu ="전체";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = ActivityCommunityWritingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Spinner spin1 = binding.spinner;
        Spinner spin2 = binding.spinner2;

        adspin1 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do, android.R.layout.simple_spinner_dropdown_item);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adspin1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adspin1.getItem(position).equals("전체")) {
                    choice_si = "전체";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(),R.array.spinner_do_entire, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if(adspin1.getItem(position).equals("서울특별시")) {
                    choice_si = "서울";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_seoul, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("부산광역시")) {
                    choice_si = "부산광역시";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_busan, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("대구광역시")) {
                    choice_si = "대구광역시";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_daegu, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("인천광역시")) {
                    choice_si = "인천광역시";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_incheon, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("광주광역시")) {
                    choice_si = "광주광역시";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_gwangju, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("대전광역시")) {
                    choice_si = "대전광역시";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_daejeon, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("울산광역시")) {
                    choice_si = "울산광역시";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_ulsan, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("세종특별자치시")) {
                    choice_si = "세종특별자치시";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_sejong, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("경기도")) {
                    choice_si = "경기도";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_gyeonggi, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("강원도")) {
                    choice_si = "강원도";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_gangwon, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("충청북도")) {
                    choice_si = "충청북도";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_chung_buk, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("충청남도")) {
                    choice_si = "충청남도";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_chung_nam, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("전라북도")) {
                    choice_si = "전라북도";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_jeon_buk, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("전라남도")) {
                    choice_si = "전라남도";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_jeon_nam, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("경상북도")) {
                    choice_si = "경상북도";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_gyeong_buk, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("경상남도")) {
                    choice_si = "경상남도";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_gyeong_nam, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(adspin1.getItem(position).equals("제주특별자치도")) {
                    choice_si = "제주특별자치도";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_jeju, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_gu = adspin2.getItem(position).toString();
                            Log.d("ksh", "onItemSelected: "+adspin2.getItem(position).toString());
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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(user.getEmail());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                appUser = documentSnapshot.toObject(AppUser.class);
            }
        });

        binding.communityWriteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] words = binding.communityWriteTitle.getText().toString().split(" ");
                ArrayList<String> wordList = new ArrayList<>(Arrays.asList(words));
                if (binding.communityCheckbox.isChecked()) {
                    CommunityDoc communityDoc = new CommunityDoc(appUser.getNickname(), "4_"+binding.communityWriteTitle.getText().toString(),user.getEmail(), binding.communityWriteTitle.getText().toString(),
                            binding.communityWriteBody.getText().toString(), "1", "1", "1", new ArrayList<String>(), true,choice_si,choice_gu,wordList);
                    db.collection("communityWritings").document(binding.communityWriteTitle.getText().toString()).set(communityDoc);
                    finish();
                } else {
                    CommunityDoc communityDoc = new CommunityDoc(appUser.getNickname(), "4_"+binding.communityWriteTitle.getText().toString(),user.getEmail(), binding.communityWriteTitle.getText().toString(),
                            binding.communityWriteBody.getText().toString(), "1", "1", "1", new ArrayList<String>(), false,choice_si,choice_gu,wordList);
                    db.collection("communityWritings").document(binding.communityWriteTitle.getText().toString()).set(communityDoc);
                    finish();
                }
            }

        });
    }
    private void communityMyWrite(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef1 = db.collection("users").document(user.getEmail());
        docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                appUser = documentSnapshot.toObject(AppUser.class);
                List<String> myWrite=appUser.getMyWrite();
                myWrite.add("4_"+binding.communityWriteTitle.getText().toString());
                appUser.setMyWrite(myWrite);
                db.collection("users").document(user.getEmail()).set(appUser);
            }
        });
    }

    private boolean hasSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null ? true : false;
    }

    private String getUidOfCurrentUser() {
        return hasSignedIn() ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
    }

}





