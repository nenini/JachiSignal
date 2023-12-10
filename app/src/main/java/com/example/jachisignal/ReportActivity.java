package com.example.jachisignal;

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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jachisignal.Doc.ReportDoc;
import com.example.jachisignal.databinding.ActivityReportBinding;
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

public class ReportActivity extends AppCompatActivity {
    String path;

    private FirebaseFirestore db;
    AppUser appUser;
    private Uri uri;
    private String imgLink;
    ArrayAdapter<String> adapter;
    String[] categories={"전체","악플","공구사기","명의도용","기타"};
    private String choice_category="전체";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityReportBinding binding=ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Spinner spin=binding.spinnerReport;
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adapter.getItem(position).equals("전체")){
                    choice_category="전체";
                } else if (adapter.getItem(position).equals("악플")) {
                    choice_category="악플";
                }else if (adapter.getItem(position).equals("공구사기")) {
                    choice_category="공구사기";
                }else if (adapter.getItem(position).equals("명의도용")) {
                    choice_category="명의도용";
                }else if (adapter.getItem(position).equals("기타")) {
                    choice_category="기타";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        binding.writeBtnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportDoc reportDoc=new ReportDoc(choice_category,appUser.getEmail(),binding.titleReport.getText().toString(),binding.contentReport.getText().toString(),imgLink);
                db.collection("reportWritings").document(appUser.getEmail()+"_"+System.currentTimeMillis()).set(reportDoc);
                finish();
            }
        });
        binding.imgBtnReport.setOnClickListener(new View.OnClickListener() {
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
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("jachiWriteIMG");
        storageRef.child(getPath()).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    Log.d("KYR","업로드에 성공했습니다.");
                    Log.d("KYR",path);
                    imgLink="gs://jachisignal-c6bd9.appspot.com/reportWriteIMG/"+path;
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
}