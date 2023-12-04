package com.example.jachisignal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.jachisignal.databinding.ActivityFindPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FindPasswordActivity extends AppCompatActivity {
    FirebaseFirestore db;
    String name;
    String email;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFindPasswordBinding binding=ActivityFindPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        db=FirebaseFirestore.getInstance();

        binding.passwordFindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=binding.nameEditTxt.getText().toString();
                email=binding.emailEditTxt.getText().toString();
                phone=binding.phoneEditTxt.getText().toString();
//
//                DocumentReference docRef=db.collection("users").document(email);
//                docRef.get().
            }
        });

    }
}