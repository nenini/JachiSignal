package com.example.jachisignal.PostActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.jachisignal.Doc.GongguDoc;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInside09Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Post_Inside_09 extends AppCompatActivity {
    private GongguDoc gongguDoc;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPostInside09Binding binding=ActivityPostInside09Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String collectionName = intent.getStringExtra("COLLECTION");
        String documentName = intent.getStringExtra("DOCUMENT");
        db = FirebaseFirestore.getInstance();

        Log.d("KSM", collectionName + "  " + documentName);

        DocumentReference docRef = db.collection(collectionName).document(documentName);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                gongguDoc = documentSnapshot.toObject(GongguDoc.class);
                Log.d("KSM", "onSuccess: ");
                binding.title09.setText(gongguDoc.getContentTitle());
                Log.d("KSM", "onSuccess: 1");
                binding.text09.setText(gongguDoc.getText());
                Log.d("KSM", "onSuccess: 2");
                binding.heartCount09.setText(Integer.toString(gongguDoc.getLikeList().size()) + "개");
                Log.d("KSM", "onSuccess: 3");
                binding.nickname09.setText(gongguDoc.getNickname());
                Log.d("KSM", "onSuccess: 4");
                binding.itemName09.setText(gongguDoc.getItemName());
                Log.d("KSM", "onSuccess: 5");
                binding.price09.setText(gongguDoc.getPrice());
            }
        });
    }
}