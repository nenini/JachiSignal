package com.example.jachisignal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jachisignal.databinding.ActivityResetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.rxjava3.annotations.NonNull;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityResetPasswordBinding binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String presentPassword = binding.presentPW.getText().toString();
                String newPassword = binding.newPW.getText().toString();
                String passwordCheck = binding.newPW2.getText().toString();

//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                CollectionReference users = db.collection("users");
//                DocumentReference userDoc = users.document(user.getEmail());
//                if(userDoc.get()) {
//
//                }
                if(newPassword.equals(passwordCheck)) {
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User password updated.");
                                    }
                                }
                            });
                }
                else {
                    Log.d("TAG", "User password update failed.");
                    Toast.makeText(getApplicationContext(), "Two passwords are not same.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}