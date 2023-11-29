package com.example.jachisignal.PostActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInsideCommunityBinding;
import com.example.jachisignal.databinding.ActivityPostInsideRecipeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Post_Inside_Recipe extends AppCompatActivity {

    private RecipeDoc recipeDoc;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPostInsideRecipeBinding binding = ActivityPostInsideRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String collectionName = intent.getStringExtra("COLLECTION");
        String documentName = intent.getStringExtra("DOCUMENT");
        Log.d("KSM", collectionName + "  " + documentName);

        db = FirebaseFirestore.getInstance();


        DocumentReference docRef = db.collection("recipeWritings").document(documentName);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                recipeDoc = documentSnapshot.toObject(RecipeDoc.class);
                Log.d("KSM", "onSuccess: ");
                binding.titleRecipePost.setText(recipeDoc.getContentTitle());

                binding.textRecipePost.setText(recipeDoc.getText());

                binding.heartCountRecipePost.setText(Integer.toString(recipeDoc.getLikeList().size()) + "개");

                binding.nicknameRecipePost.setText(recipeDoc.getNickname());
            }
        });

    }
}
