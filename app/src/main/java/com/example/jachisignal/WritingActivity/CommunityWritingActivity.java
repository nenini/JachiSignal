package com.example.jachisignal.WritingActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.CommunityDoc;
import com.example.jachisignal.Doc.JachiDoc;
import com.example.jachisignal.databinding.ActivityCommunityWritingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CommunityWritingActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    AppUser appUser;
    ActivityCommunityWritingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = ActivityCommunityWritingBinding.inflate(getLayoutInflater());
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

        binding.communityWriteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.communityCheckbox.isChecked()) {
                    CommunityDoc communityDoc = new CommunityDoc(appUser.getNickname(), "4_"+binding.communityWriteTitle.getText().toString(),user.getEmail(), binding.communityWriteTitle.getText().toString(),
                            binding.communityWriteBody.getText().toString(), "1", "1", "1", new ArrayList<String>(), true);
                    db.collection("communityWritings").document(binding.communityWriteTitle.getText().toString()).set(communityDoc);
                    finish();
                } else {
                    CommunityDoc communityDoc = new CommunityDoc(appUser.getNickname(), "4_"+binding.communityWriteTitle.getText().toString(),user.getEmail(), binding.communityWriteTitle.getText().toString(),
                            binding.communityWriteBody.getText().toString(), "1", "1", "1", new ArrayList<String>(), false);
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





