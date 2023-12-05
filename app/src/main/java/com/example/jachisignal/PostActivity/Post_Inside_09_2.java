package com.example.jachisignal.PostActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.GongguDoc;
import com.example.jachisignal.Doc.GongguDoc2;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInside092Binding;
import com.example.jachisignal.databinding.ActivityPostInside09Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class Post_Inside_09_2 extends AppCompatActivity {
    String documentName;

    private GongguDoc2 gongguDoc2;
    FirebaseFirestore db;
    AppUser appUser;
    String uid = getUidOfCurrentUser();
    ActivityPostInside092Binding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityPostInside092Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String collectionName = intent.getStringExtra("COLLECTION");
        documentName = intent.getStringExtra("DOCUMENT");
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
                DocumentReference docRef2 = db.collection("users").document(gongguDoc2.getWriteId());
                docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        appUser = documentSnapshot.toObject(AppUser.class);
                        if(appUser.getImg()!=null){downloadImageToUser(appUser.getImg());}
                    }
                });
                if (gongguDoc2.getImageLink() != null) {
                    downloadImageTo(gongguDoc2.getImageLink());
                }

                //하트 색 바뀜
                if (gongguDoc2.getLikeList().contains(uid)) {
                    binding.heart092Post.setImageResource(R.drawable.heartcount);
                } else binding.heart092Post.setImageResource(R.drawable.heart);
                //스크랩 색 바뀜
                if (gongguDoc2.getScrapList().contains(uid)) {
                    binding.star092Post.setImageResource(R.drawable.star);
                } else binding.star092Post.setImageResource(R.drawable.star_blank);

                //좋아요 버튼 click
                binding.heart092Post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        likeEvent();
                        Log.d("KYR", "09.getLikeList():" + gongguDoc2.getLikeList());
                        if (gongguDoc2.getLikeList().contains(uid)) {
                            binding.heart092Post.setImageResource(R.drawable.heartcount);
                            Log.d("KYR", "heart로 바꾸기");
                        } else
                            binding.heart092Post.setImageResource(R.drawable.heart);
                    }
                });

                //스크랩 버튼 click
                binding.star092Post.setOnClickListener(new View.OnClickListener() {
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
                Log.d("KYR", "포스트레시피 url" + uri.toString());
                Glide.with(Post_Inside_09_2.this).load(uri).into(binding.downloadedImageview);
            }
        });
    }
    private void downloadImageToUser(String uri) {
        // Get a default Storage bucket
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a reference to a file from a Google Cloud Storage URI
        StorageReference gsReference = storage.getReferenceFromUrl(uri); // from gs://~~~
        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Post_Inside_09_2.this).load(uri).into(binding.userImg092Post);
            }
        });
    }

    private void likeEvent() {
        //눌렸는데 uid가 있었던 경우->uid 삭제
        if (gongguDoc2.getLikeList().contains(uid)) {
            gongguDoc2.getLikeList().remove(uid);
            db.collection("gongu2Writings").document(documentName).set(gongguDoc2);
        } else {//uid 없었음->uid 추가
            gongguDoc2.getLikeList().add(uid);
            db.collection("gongu2Writings").document(documentName).set(gongguDoc2);
        }
        binding.heartCount092.setText(Integer.toString(gongguDoc2.getLikeList().size()) + "개");
    }

    //스크랩 함수
    private void scrapEvent() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = db.collection("users").document(user.getEmail());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                appUser = documentSnapshot.toObject(AppUser.class);
                //눌렸는데 uid가 있었던 경우->uid 삭제
                if (gongguDoc2.getScrapList().contains(uid)) {
                    gongguDoc2.getScrapList().remove(uid);
                    db.collection("gongu2Writings").document(documentName).set(gongguDoc2);
                    //눌렸는데 uid가 있었던 경우->게시물 id 삭제
                    List<String> scrap_id = appUser.getScrap();
                    scrap_id.remove(gongguDoc2.getId());
                    appUser.setScrap(scrap_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                } else {//눌렸는데 uid가 없었던 경우->uid 추가
                    gongguDoc2.getScrapList().add(uid);
                    db.collection("gongu2Writings").document(documentName).set(gongguDoc2);
                    List<String> scrap_id = appUser.getScrap();
                    scrap_id.add(gongguDoc2.getId());
                    appUser.setScrap(scrap_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                }
                Log.d("KYR", "092.scrapList():" + gongguDoc2.getScrapList());
                if (gongguDoc2.getScrapList().contains(uid)) {
                    binding.star092Post.setImageResource(R.drawable.star);
                    Log.d("KYR", "star 바꾸기");
                } else
                    binding.star092Post.setImageResource(R.drawable.star_blank);
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