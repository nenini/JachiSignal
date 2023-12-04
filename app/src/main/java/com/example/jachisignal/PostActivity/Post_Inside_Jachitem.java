package com.example.jachisignal.PostActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.JachiDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInsideJachitemBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class Post_Inside_Jachitem extends AppCompatActivity {
    private Uri uri;
    private JachiDoc jachiDoc;
    String documentName;
    FirebaseFirestore db;
    ActivityPostInsideJachitemBinding binding;
    String uid = getUidOfCurrentUser();
    AppUser appUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostInsideJachitemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String collectionName = intent.getStringExtra("COLLECTION");
        documentName = intent.getStringExtra("DOCUMENT");
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
                if(jachiDoc.getImageLink()!=null){ downloadImageTo(jachiDoc.getImageLink());}

                //하트 색 바뀜
                if(jachiDoc.getLikeList().contains(uid)){
                    binding.heartJachiPost.setImageResource(R.drawable.heartcount);
                }
                else binding.heartJachiPost.setImageResource(R.drawable.heart);
                //스크랩 색 바뀜
                if(jachiDoc.getScrapList().contains(uid)){
                    binding.starJachiPost.setImageResource(R.drawable.star);
                }
                else binding.starJachiPost.setImageResource(R.drawable.star_blank);

                //좋아요 버튼 click
                binding.heartJachiPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        likeEvent();
                        Log.d("KYR","jachiDoc.getLikeList():" +jachiDoc.getLikeList());
                        if(jachiDoc.getLikeList().contains(uid)){
                            binding.heartJachiPost.setImageResource(R.drawable.heartcount);
                            Log.d("KYR","heart로 바꾸기");
                        }
                        else
                            binding.heartJachiPost.setImageResource(R.drawable.heart);
                    }
                });

                //스크랩 버튼 click
                binding.starJachiPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scrapEvent();
                    }
                });
            }
        });
    }

    private void downloadImageTo(String uri) {
        // Get a default Storage bucket
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a reference to a file from a Google Cloud Storage URI
        Log.d("KYR", "포스트 레시피");
        StorageReference gsReference = storage.getReferenceFromUrl(uri); // from gs://~~~
        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("KYR", "포스트레시피 url"+uri.toString());
                Glide.with(Post_Inside_Jachitem.this).load(uri).into(binding.downloadedImageview);
            }
        });
    }
    private void likeEvent(){
        //눌렸는데 uid가 있었던 경우->uid 삭제
        if(jachiDoc.getLikeList().contains(uid)){
            jachiDoc.getLikeList().remove(uid);
            db.collection("jachitemWritings").document(documentName).set(jachiDoc);
        }
        else{//uid 없었음->uid 추가
            jachiDoc.getLikeList().add(uid);
            db.collection("jachitemWritings").document(documentName).set(jachiDoc);
        }
        binding.heartCountJachitem.setText(Integer.toString(jachiDoc.getLikeList().size())+"개");
    }
    //스크랩 함수
    private void scrapEvent(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = db.collection("users").document(user.getEmail());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                appUser = documentSnapshot.toObject(AppUser.class);
                //눌렸는데 uid가 있었던 경우->uid 삭제
                if(jachiDoc.getScrapList().contains(uid)){
                    jachiDoc.getScrapList().remove(uid);
                    db.collection("jachitemWritings").document(documentName).set(jachiDoc);
                    //눌렸는데 uid가 있었던 경우->게시물 id 삭제
                    List<String> scrap_id=appUser.getScrap();
                    scrap_id.remove(jachiDoc.getId());
                    appUser.setScrap(scrap_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                }
                else {//눌렸는데 uid가 없었던 경우->uid 추가
                    jachiDoc.getScrapList().add(uid);
                    db.collection("jachitemWritings").document(documentName).set(jachiDoc);
                    List<String> scrap_id=appUser.getScrap();
                    scrap_id.add(jachiDoc.getId());
                    appUser.setScrap(scrap_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                }
                Log.d("KYR","jachiDoc.scrapList():" +jachiDoc.getScrapList());
                if(jachiDoc.getScrapList().contains(uid)){
                    binding.starJachiPost.setImageResource(R.drawable.star);
                    Log.d("KYR","star 바꾸기");
                }
                else
                    binding.starJachiPost.setImageResource(R.drawable.star_blank);
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