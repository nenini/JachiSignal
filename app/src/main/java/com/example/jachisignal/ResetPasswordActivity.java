package com.example.jachisignal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jachisignal.databinding.ActivityResetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.rxjava3.annotations.NonNull;

public class ResetPasswordActivity extends AppCompatActivity {

    String TAG = "ResetPasswordActivity";
    AppUser appUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityResetPasswordBinding binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
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

        binding.resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String presentPassword = binding.presentPW.getText().toString();
                String newPassword = binding.newPW.getText().toString();
                String passwordCheck = binding.newPW2.getText().toString();

                if(!appUser.getPw().equals(presentPassword)) {
                    Toast.makeText(getApplicationContext(), "현재 비밀번호가 틀렸습니다.",
                            Toast.LENGTH_SHORT).show();
                }
                else if(newPassword.equals(passwordCheck)) {
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "비밀번호 확인이 틀렸습니다.");
                                        finish();
                                        docRef.update("pw", newPassword)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error updating document", e);
                                                    }
                                                });

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