package com.example.jachisignal.PostActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.jachisignal.Doc.JachiDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInsideJachitemBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Post_Inside_Jachitem extends AppCompatActivity {
    FirebaseFirestore db;
    JachiDoc jachiDoc;
    ActivityPostInsideJachitemBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostInsideJachitemBinding.inflate(getLayoutInflater());
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
                jachiDoc = documentSnapshot.toObject(JachiDoc.class);
                binding.titleJachitem.setText(jachiDoc.getContentTitle());
                binding.textJachitem.setText(jachiDoc.getText());
                binding.heartCountJachitem.setText(Integer.toString(jachiDoc.getLikeList().size()) + "개");
                binding.nicknameJachitem.setText(jachiDoc.getNickname());
            }
        });
    }
}