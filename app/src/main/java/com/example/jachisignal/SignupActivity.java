package com.example.jachisignal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.jachisignal.Fragment.FragmentSetting;
import com.example.jachisignal.databinding.ActivitySignupBinding;
import com.example.jachisignal.databinding.FragmentSettingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    String task_snap;

    private static final String TAG = "DocSnippets";

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailEditTxt.getText().toString();
                String password = binding.passwordEditTxt.getText().toString();

                initializeCloudFirestore();

//                uploadFromDataInMemory();
                signUp(email, password);
            }
        });



    }
    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseStorage storage = FirebaseStorage.getInstance();

                            // Points to the root reference
                            StorageReference storageRef = storage.getReference();

                            // Create a reference for a new image
                            StorageReference mountainImagesRef = storageRef.child(getPath("jpg"));
                            Log.d("KYR","test1");

                            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.human);

                            Bitmap bitmap = drawableToBitmap(drawable);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //0-100
                            byte[] data = baos.toByteArray();

                            UploadTask uploadTask = mountainImagesRef.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    Log.d("KYR", "이미지뷰의 이미지 업로드 실패");
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                    // ...
                                    Log.d("KYR", "이미지뷰의 이미지 업로드 성공");
                                    task_snap = taskSnapshot.getMetadata().getReference().toString();
                                    Log.d("KYR","task_snap");
//                                    Toast.makeText(getApplicationContext(), task_snap, Toast.LENGTH_LONG).show();
                                    List<String> scrap=new ArrayList<>();
                                    List<String> mywrite=new ArrayList<>();
                                    List<String> myGonggu=new ArrayList<>();
                                    CollectionReference users = db.collection("users");
                                    Map<String, Object> data1 = new HashMap<>();
                                    data1.put("myGonggu",myGonggu);
                                    data1.put("myWrite",mywrite);
                                    data1.put("scrap",scrap);
                                    data1.put("img",task_snap);
                                    data1.put("email", binding.emailEditTxt.getText().toString());
                                    data1.put("pw", binding.passwordEditTxt.getText().toString());
                                    data1.put("nickname",binding.nicknameEditTxt.getText().toString());
                                    data1.put("name", binding.nameEditTxt.getText().toString());
                                    data1.put("phone", binding.phoneEditTxt.getText().toString());
                                    data1.put("address", binding.addressEditTxt.getText().toString());
                                    users.document(binding.emailEditTxt.getText().toString()).set(data1);

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Log.d("TAG", "createUserWithEmail:failure");
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, NaviActivity.class);
            intent.putExtra("USER_PROFILE", "email: " + user.getEmail() + "\n" + "uid: " + user.getUid());
            intent.putExtra("task_message",task_snap);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }

    private void initializeCloudFirestore() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    private String getPath(String extension) {
        String uid = getUidOfCurrentUser();

        String dir = (uid != null) ? uid : "public";

        String fileName = (uid != null) ? (uid + "_" + "first" + "." + extension)
                : ("anonymous" + "_" + "first" + "." + extension);

        return dir + "/" + fileName;
    }

    private boolean hasSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null ? true : false;
    }

    private String getUidOfCurrentUser() {
        return hasSignedIn() ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
    }


}