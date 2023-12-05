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

public class GongguWritingActivity extends AppCompatActivity {
    String path;
    private FirebaseFirestore db;
    private Uri uri;
    private String imgLink;

    AppUser appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGongguWritingBinding binding = ActivityGongguWritingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                            binding.gongguWriteBody.getText().toString(), "1", binding.gongguWriteCategory.getText().toString(), imgLink, new ArrayList<String>(),new ArrayList<String>(), binding.gongguWriteItemName.getText().toString(), binding.gongguWritePrice.getText().toString(), "1/" + binding.gongguWritePeopleCount.getText().toString(), binding.gongguWriteChatLink.getText().toString());
                    db.collection("gongu1Writings").document(binding.gongguWriteItemName.getText().toString()).set(gongguDoc);
                    finish();
                } else if (!binding.gongguFaceCheckBox.isChecked()&&binding.gongguDeliCheckBox.isChecked()) {
                    GongguDoc2 gongguDoc2 = new GongguDoc2(appUser.getNickname(), "2_"+binding.gongguWriteItemName.getText().toString(), user.getEmail(), binding.gongguWriteTitle.getText().toString(),
                            binding.gongguWriteBody.getText().toString(), "1", binding.gongguWriteCategory.getText().toString(), imgLink, new ArrayList<String>(), new ArrayList<String>(), binding.gongguWriteItemName.getText().toString(), binding.gongguWritePrice.getText().toString(), "1/" + binding.gongguWritePeopleCount.getText().toString(), binding.gongguWriteChatLink.getText().toString());
                    db.collection("gongu2Writings").document(binding.gongguWriteItemName.getText().toString()).set(gongguDoc2);
                    finish();
                } else if (binding.gongguFaceCheckBox.isChecked()&&binding.gongguDeliCheckBox.isChecked()) {
                    GongguDoc gongguDoc = new GongguDoc(appUser.getNickname(), "1_"+binding.gongguWriteItemName.getText().toString(), user.getEmail(), binding.gongguWriteTitle.getText().toString(),
                            binding.gongguWriteBody.getText().toString(), "1", binding.gongguWriteCategory.getText().toString(), imgLink, new ArrayList<String>(),new ArrayList<String>(), binding.gongguWriteItemName.getText().toString(), binding.gongguWritePrice.getText().toString(), "1/" + binding.gongguWritePeopleCount.getText().toString(), binding.gongguWriteChatLink.getText().toString());
                    db.collection("gongu1Writings").document(binding.gongguWriteItemName.getText().toString()).set(gongguDoc);
                    GongguDoc2 gongguDoc2 = new GongguDoc2(appUser.getNickname(), "2_"+binding.gongguWriteItemName.getText().toString(), user.getEmail(), binding.gongguWriteTitle.getText().toString(),
                            binding.gongguWriteBody.getText().toString(), "1", binding.gongguWriteCategory.getText().toString(), imgLink, new ArrayList<String>(),new ArrayList<String>(), binding.gongguWriteItemName.getText().toString(), binding.gongguWritePrice.getText().toString(), "1/" + binding.gongguWritePeopleCount.getText().toString(), binding.gongguWriteChatLink.getText().toString());
                    db.collection("gongu2Writings").document(binding.gongguWriteItemName.getText().toString()).set(gongguDoc2);
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
}