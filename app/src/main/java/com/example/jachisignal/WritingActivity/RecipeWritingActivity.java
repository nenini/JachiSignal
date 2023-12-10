package com.example.jachisignal.WritingActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.databinding.ActivityRecipeWritingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeWritingActivity extends AppCompatActivity {

    String path;

    private FirebaseFirestore db;
    AppUser appUser;
    private Uri uri;
    private String imgLink;
    ActivityRecipeWritingBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeWritingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(user.getEmail());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                appUser = documentSnapshot.toObject(AppUser.class);
            }
        });

        binding.recipeWriteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] words = binding.recipeWriteTitle.getText().toString().split(" ");
                ArrayList<String> wordList = new ArrayList<>(Arrays.asList(words));
                RecipeDoc recipeDoc = new RecipeDoc(appUser.getNickname(),"6_"+binding.recipeWriteTitle.getText().toString(),user.getEmail(),binding.recipeWriteTitle.getText().toString(),
                        binding.recipeWriteBody.getText().toString(),"1",binding.recipeWriteCategory.getText().toString(),imgLink,new ArrayList<String>(),new ArrayList<String>(),wordList,0);
                db.collection("recipeWritings").document(binding.recipeWriteTitle.getText().toString()).set(recipeDoc);
                recipeMyWrite();
                finish();
            }
        });
        binding.recipeWriteImgBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select();
            }
        });
    }
    private boolean select() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT );
        intent.setType("image/*");
        launcher.launch(intent);
        Log.d("KYR","select()");
        return true;
    }
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();
                        Log.d("KYR", uri.toString());
                        upload();

                    }
                }
            });
    private void upload() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("recipeWriteIMG");
        storageRef.child(getPath()).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    Log.d("KYR","업로드에 성공했습니다.");
                    Log.d("KYR",path);
                    imgLink="gs://jachisignal-c6bd9.appspot.com/recipeWriteIMG/"+path;
                }
                else {
                    Log.d("KYR","업로드에 실패했습니다.");
                }
            }
        });
    }
    private String getPath() {
        String uid = getUidOfCurrentUser();

        String dir = uid;

        String fileName ="_" + System.currentTimeMillis();
        path=dir + "/" + fileName;
        return dir + "/" + fileName;
    }
    private void recipeMyWrite(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef1 = db.collection("users").document(user.getEmail());
        docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                appUser = documentSnapshot.toObject(AppUser.class);
                List<String> myWrite=appUser.getMyWrite();
                myWrite.add("6_"+binding.recipeWriteTitle.getText().toString());
                appUser.setMyWrite(myWrite);
                db.collection("users").document(user.getEmail()).set(appUser);
            }
        });
    }
    private boolean hasSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null ? true : false;
    }

    private String getUidOfCurrentUser() {
        return hasSignedIn() ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
    }


}