package com.example.jachisignal.WritingActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.jachisignal.AppUser;
import com.example.jachisignal.R;
import com.example.jachisignal.RecipeDoc;
import com.example.jachisignal.databinding.ActivityRecipeWritingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeWritingActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    AppUser appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRecipeWritingBinding binding = ActivityRecipeWritingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db = FirebaseFirestore.getInstance();

//        DocumentReference docRef = db.collection("users").document(user.getEmail());

//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                appUser = documentSnapshot.toObject(AppUser.class);
//            }
//        });

        binding.recipeWriteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeDoc recipeDoc = new RecipeDoc("",user.getEmail(),binding.recipeWriteTitle.getText().toString(),
                        binding.recipeWriteBody.getText().toString(),"","","",new ArrayList<String>());
                db.collection("recipeWritings").document(binding.recipeWriteTitle.getText().toString()).set(recipeDoc);
                finish();
            }
        });
    }

}