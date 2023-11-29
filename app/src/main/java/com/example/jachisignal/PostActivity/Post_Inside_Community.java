package com.example.jachisignal.PostActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInsideCommunityBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Post_Inside_Community extends AppCompatActivity {
    private RecipeDoc recipeDoc;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPostInsideCommunityBinding binding = ActivityPostInsideCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        Intent intent = getIntent();
//        String collectionName = intent.getStringExtra("COLLECTION");
//        String documentName = intent.getStringExtra("DOCUMENT");
//        Log.d("KSM", collectionName + "  " + documentName);
//
//        db = FirebaseFirestore.getInstance();
//
//        DocumentReference docRef = db.collection("communityWritings").document(documentName);
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                recipeDoc = documentSnapshot.toObject(RecipeDoc.class);
//                Log.d("KSM", "onSuccess: ");
//                binding.titleCommunityPost.setText(recipeDoc.getContentTitle());
//
//                binding.textCommunityPost.setText(recipeDoc.getText());
//
//                binding.heartCountCommunityPost.setText(Integer.toString(recipeDoc.getLikeList().size()) + "개");
//
//                binding.nicknameCommunityPost.setText(recipeDoc.getNickname());
//            }
//        });


    }
}