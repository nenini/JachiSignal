package com.example.jachisignal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.jachisignal.databinding.ActivityFindPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FindPasswordActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String name;
    String email;
    String phone;
    private AppUser appUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFindPasswordBinding binding=ActivityFindPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        binding.passwordFindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=binding.emailEditTxt.getText().toString();
                name=binding.nameEditTxt.getText().toString();
                phone=binding.phoneEditTxt.getText().toString();
                firestore.collection("users")
                        .document(email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        //해달 이메일 존재
                                        String firestoreName = document.getString("name");
                                        String firestorePhone = document.getString("phone");
                                        //이메일 이름 전화번호 일치 여부 확인
                                        if (name.equals(firestoreName) && phone.equals(firestorePhone)) {
                                            auth.sendPasswordResetEmail(email)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(FindPasswordActivity.this, "비밀번호 재설정 이메일을 전송했습니다.", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(FindPasswordActivity.this,MainActivity.class));
                                                                finish();
                                                            } else {
                                                                Toast.makeText(FindPasswordActivity.this, "이메일 전송에 실패했습니다.", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(FindPasswordActivity.this, "이메일, 이름, 전화번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // Firestore에 해당 이메일이 존재하지 않는 경우
                                        Toast.makeText(FindPasswordActivity.this, "해당 이메일이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Firestore에서 데이터 가져오기 실패
                                    Toast.makeText(FindPasswordActivity.this, "데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
            }
        });
    }
}