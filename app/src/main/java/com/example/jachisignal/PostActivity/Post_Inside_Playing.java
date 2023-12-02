package com.example.jachisignal.PostActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.jachisignal.Doc.LeisureDoc;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInsidePlayingBinding;
import com.example.jachisignal.databinding.ActivityPostInsideRecipeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Post_Inside_Playing extends AppCompatActivity {
    private LeisureDoc leisureDoc;
    private static final String TAG = "KSM";
    FirebaseFirestore db;

    ActivityPostInsidePlayingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostInsidePlayingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String collectionName = intent.getStringExtra("COLLECTION");
        String documentName = intent.getStringExtra("DOCUMENT");
        Log.d("KSM", collectionName + "  " + documentName);

        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection(collectionName).document(documentName);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                leisureDoc = documentSnapshot.toObject(LeisureDoc.class);
                binding.placeLeisure.setText(leisureDoc.getPlace());
                binding.meetingTimeLeisure.setText(leisureDoc.getDate());
                binding.nicknameLeisure.setText(leisureDoc.getNickname());
                binding.heartCountLeisure.setText(Integer.toString(leisureDoc.getLikeList().size()) + "개");
                binding.textLeisure.setText(leisureDoc.getText());
                binding.titleLeisure.setText(leisureDoc.getContentTitle());
            }
        });
    }
}