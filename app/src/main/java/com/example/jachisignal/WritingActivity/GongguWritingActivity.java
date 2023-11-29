package com.example.jachisignal.WritingActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.GongguDoc;
import com.example.jachisignal.Doc.GongguDoc2;
import com.example.jachisignal.Doc.LeisureDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityGongguWritingBinding;
import com.example.jachisignal.databinding.ActivityLeisureWritingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GongguWritingActivity extends AppCompatActivity {
    private FirebaseFirestore db;
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
                    GongguDoc gongguDoc = new GongguDoc(appUser.getNickname(), "1", user.getEmail(), binding.gongguWriteTitle.getText().toString(),
                            binding.gongguWriteBody.getText().toString(), "1", binding.gongguWriteCategory.getText().toString(), "1", new ArrayList<String>(), binding.gongguWriteItemName.getText().toString(), binding.gongguWritePrice.getText().toString(), "1/" + binding.gongguWritePeopleCount.getText().toString(), binding.gongguWriteChatLink.getText().toString());
                    db.collection("gongu1Writings").document(binding.gongguWriteTitle.getText().toString()).set(gongguDoc);
                    finish();
                } else if (!binding.gongguFaceCheckBox.isChecked()&&binding.gongguDeliCheckBox.isChecked()) {
                    GongguDoc2 gongguDoc2 = new GongguDoc2(appUser.getNickname(), "1", user.getEmail(), binding.gongguWriteTitle.getText().toString(),
                            binding.gongguWriteBody.getText().toString(), "1", binding.gongguWriteCategory.getText().toString(), "1", new ArrayList<String>(), binding.gongguWriteItemName.getText().toString(), binding.gongguWritePrice.getText().toString(), "1/" + binding.gongguWritePeopleCount.getText().toString(), binding.gongguWriteChatLink.getText().toString());
                    db.collection("gongu2Writings").document(binding.gongguWriteTitle.getText().toString()).set(gongguDoc2);
                    finish();
                } else if (binding.gongguFaceCheckBox.isChecked()&&binding.gongguDeliCheckBox.isChecked()) {
                    GongguDoc gongguDoc = new GongguDoc(appUser.getNickname(), "1", user.getEmail(), binding.gongguWriteTitle.getText().toString(),
                            binding.gongguWriteBody.getText().toString(), "1", binding.gongguWriteCategory.getText().toString(), "1", new ArrayList<String>(), binding.gongguWriteItemName.getText().toString(), binding.gongguWritePrice.getText().toString(), "1/" + binding.gongguWritePeopleCount.getText().toString(), binding.gongguWriteChatLink.getText().toString());
                    db.collection("gongu1Writings").document(binding.gongguWriteTitle.getText().toString()).set(gongguDoc);
                    GongguDoc2 gongguDoc2 = new GongguDoc2(appUser.getNickname(), "1", user.getEmail(), binding.gongguWriteTitle.getText().toString(),
                            binding.gongguWriteBody.getText().toString(), "1", binding.gongguWriteCategory.getText().toString(), "1", new ArrayList<String>(), binding.gongguWriteItemName.getText().toString(), binding.gongguWritePrice.getText().toString(), "1/" + binding.gongguWritePeopleCount.getText().toString(), binding.gongguWriteChatLink.getText().toString());
                    db.collection("gongu2Writings").document(binding.gongguWriteTitle.getText().toString()).set(gongguDoc2);
                    finish();
                }
            }
        });
    }
}