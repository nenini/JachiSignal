package com.example.jachisignal.PostActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.jachisignal.Doc.GongguDoc;
import com.example.jachisignal.Doc.GongguDoc2;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInside092Binding;
import com.example.jachisignal.databinding.ActivityPostInside09Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Post_Inside_09_2 extends AppCompatActivity {


    private GongguDoc2 gongguDoc2;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPostInside092Binding binding= ActivityPostInside092Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String collectionName = intent.getStringExtra("COLLECTION");
        String documentName = intent.getStringExtra("DOCUMENT");
        db = FirebaseFirestore.getInstance();

        Log.d("KSM", collectionName + "  " + documentName);

        DocumentReference docRef = db.collection("gongu2Writings").document(documentName);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                gongguDoc2 = documentSnapshot.toObject(GongguDoc2.class);
                Log.d("KSM", "onSuccess: ");
                binding.title092.setText(gongguDoc2.getContentTitle());

                binding.text092.setText(gongguDoc2.getText());

                binding.heartCount092.setText(Integer.toString(gongguDoc2.getLikeList().size()) + "개");

                binding.nickname092.setText(gongguDoc2.getNickname());

                binding.itemName092.setText(gongguDoc2.getItemName());

                binding.price092.setText(gongguDoc2.getPrice());
            }
        });
    }
}