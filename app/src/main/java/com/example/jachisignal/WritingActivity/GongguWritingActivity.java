package com.example.jachisignal.WritingActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.GongguDoc;
import com.example.jachisignal.Doc.GongguDoc2;
import com.example.jachisignal.Doc.LeisureDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityGongguWritingBinding;
import com.example.jachisignal.databinding.ActivityLeisureWritingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class GongguWritingActivity extends AppCompatActivity {
    String path;
    private FirebaseFirestore db;
    private Uri uri;
    private String imgLink;

    AppUser appUser;
    ActivityGongguWritingBinding binding;

    ArrayAdapter<CharSequence> adspin1,adspin2;
    String choice_si = "전체";
    String choice_gu ="전체";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGongguWritingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Spinner spin1 = binding.spinnerGonggu;
        Spinner spin2 = binding.spinner2Gonggu;

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
                else if(adspin1.getItem(position).equals("서울")) {
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
                }else if(adspin1.getItem(position).equals("인천")) {
                    choice_si = "인천";
                    adspin2 = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.spinner_do_incheon, android.R.layout.simple_spinner_dropdown_item);
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

        binding.gongguWriteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.gongguFaceCheckBox.isChecked()&&!binding.gongguDeliCheckBox.isChecked()) {
                    Log.d("KYR","Doc");
                    GongguDoc gongguDoc = new GongguDoc(appUser.getNickname(), "1_"+binding.gongguWriteItemName.getText().toString(), user.getEmail(), binding.gongguWriteTitle.getText().toString(),
                            binding.gongguWriteBody.getText().toString(), "1", binding.gongguWriteCategory.getText().toString(), imgLink, new ArrayList<String>(),new ArrayList<String>(), binding.gongguWriteItemName.getText().toString(), binding.gongguWritePrice.getText().toString(), "1/" + binding.gongguWritePeopleCount.getText().toString(), binding.gongguWriteChatLink.getText().toString(),choice_si,choice_gu);
                    db.collection("gongu1Writings").document(binding.gongguWriteItemName.getText().toString()).set(gongguDoc);
                    gogguMyWrite();
                    finish();
                } else if (!binding.gongguFaceCheckBox.isChecked()&&binding.gongguDeliCheckBox.isChecked()) {
                    GongguDoc2 gongguDoc2 = new GongguDoc2(appUser.getNickname(), "2_"+binding.gongguWriteItemName.getText().toString(), user.getEmail(), binding.gongguWriteTitle.getText().toString(),
                            binding.gongguWriteBody.getText().toString(), "1", binding.gongguWriteCategory.getText().toString(), imgLink, new ArrayList<String>(), new ArrayList<String>(), binding.gongguWriteItemName.getText().toString(), binding.gongguWritePrice.getText().toString(), "1/" + binding.gongguWritePeopleCount.getText().toString(), binding.gongguWriteChatLink.getText().toString());
                    db.collection("gongu2Writings").document(binding.gongguWriteItemName.getText().toString()).set(gongguDoc2);
                    goggu2MyWrite();
                    finish();
                } else if (binding.gongguFaceCheckBox.isChecked()&&binding.gongguDeliCheckBox.isChecked()) {
                    GongguDoc gongguDoc = new GongguDoc(appUser.getNickname(), "1_"+binding.gongguWriteItemName.getText().toString(), user.getEmail(), binding.gongguWriteTitle.getText().toString(),
                            binding.gongguWriteBody.getText().toString(), "1", binding.gongguWriteCategory.getText().toString(), imgLink, new ArrayList<String>(),new ArrayList<String>(), binding.gongguWriteItemName.getText().toString(), binding.gongguWritePrice.getText().toString(), "1/" + binding.gongguWritePeopleCount.getText().toString(), binding.gongguWriteChatLink.getText().toString(),choice_si,choice_gu);
                    db.collection("gongu1Writings").document(binding.gongguWriteItemName.getText().toString()).set(gongguDoc);
                    GongguDoc2 gongguDoc2 = new GongguDoc2(appUser.getNickname(), "2_"+binding.gongguWriteItemName.getText().toString(), user.getEmail(), binding.gongguWriteTitle.getText().toString(),
                            binding.gongguWriteBody.getText().toString(), "1", binding.gongguWriteCategory.getText().toString(), imgLink, new ArrayList<String>(),new ArrayList<String>(), binding.gongguWriteItemName.getText().toString(), binding.gongguWritePrice.getText().toString(), "1/" + binding.gongguWritePeopleCount.getText().toString(), binding.gongguWriteChatLink.getText().toString());
                    db.collection("gongu2Writings").document(binding.gongguWriteItemName.getText().toString()).set(gongguDoc2);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DocumentReference docRef1 = db.collection("users").document(user.getEmail());
                    docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            appUser = documentSnapshot.toObject(AppUser.class);
                            List<String> myWrite=appUser.getMyWrite();
                            myWrite.add("1_"+binding.gongguWriteItemName.getText().toString());
                            myWrite.add("2_"+binding.gongguWriteItemName.getText().toString());
                            appUser.setMyWrite(myWrite);
                            db.collection("users").document(user.getEmail()).set(appUser);
                        }
                    });
                    finish();
                }
            }
        });
        binding.gongguWriteImgBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select();

            }
        });
    }
    private boolean select() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT );
        intent.setType("image/*");
        launcher.launch(intent);
        Log.d("KYR","select()");
        return true;
    }
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();
                        Log.d("KYR", uri.toString());
                        upload();

                    }
                }
            });
    private void upload() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("gongguWriteIMG");
        storageRef.child(getPath()).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    Log.d("KYR","업로드에 성공했습니다.");
                    Log.d("KYR",path);
                    imgLink="gs://jachisignal-c6bd9.appspot.com/gongguWriteIMG/"+path;
                }
                else {
                    Log.d("KYR","업로드에 실패했습니다.");
                }
            }
        });
    }
    private String getPath() {
        String uid = getUidOfCurrentUser();

        String dir = uid;

        String fileName ="_" + System.currentTimeMillis();
        path=dir + "/" + fileName;
        return dir + "/" + fileName;
    }
    private boolean hasSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null ? true : false;
    }

    private String getUidOfCurrentUser() {
        return hasSignedIn() ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
    }
    private void gogguMyWrite(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef1 = db.collection("users").document(user.getEmail());
        docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("KYR","집가고싶다 ");
                appUser = documentSnapshot.toObject(AppUser.class);
                List<String> myWrite=appUser.getMyWrite();
                myWrite.add("1_"+binding.gongguWriteItemName.getText().toString());
                appUser.setMyWrite(myWrite);
                db.collection("users").document(user.getEmail()).set(appUser);
            }
        });
    }
    private void goggu2MyWrite(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef1 = db.collection("users").document(user.getEmail());
        docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("KYR","집가고싶다 22");
                appUser = documentSnapshot.toObject(AppUser.class);
                List<String> myWrite=appUser.getMyWrite();
                myWrite.add("2_"+binding.gongguWriteItemName.getText().toString());
                appUser.setMyWrite(myWrite);
                db.collection("users").document(user.getEmail()).set(appUser);
            }
        });
    }
}