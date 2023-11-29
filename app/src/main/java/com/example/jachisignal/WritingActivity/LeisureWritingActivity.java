package com.example.jachisignal.WritingActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.LeisureDoc;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityLeisureWritingBinding;
import com.example.jachisignal.databinding.ActivityRecipeWritingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class LeisureWritingActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    AppUser appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLeisureWritingBinding binding = ActivityLeisureWritingBinding.inflate(getLayoutInflater());
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

        binding.leisureWriteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeisureDoc leisureDoc = new LeisureDoc(appUser.getNickname(),"1",user.getEmail(),binding.leisureWriteTitle.getText().toString(),
                        binding.leisureWriteBody.getText().toString(),"1",binding.leisureWriteCategory.getText().toString(),"1",new ArrayList<String>());
                db.collection("leisureWritings").document(binding.leisureWriteTitle.getText().toString()).set(leisureDoc);
                finish();
            }
        });
    }


}