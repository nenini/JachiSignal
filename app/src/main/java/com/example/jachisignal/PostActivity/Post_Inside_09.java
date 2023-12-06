package com.example.jachisignal.PostActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.GongguDoc;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.R;
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

public class Post_Inside_09 extends AppCompatActivity {
    String documentName;
    private GongguDoc gongguDoc;
    FirebaseFirestore db;
    AppUser appUser;
    String uid = getUidOfCurrentUser();
    private ActivityPostInside09Binding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPostInside09Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String collectionName = intent.getStringExtra("COLLECTION");
        documentName = intent.getStringExtra("DOCUMENT");
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
                binding.peopleCount09.setText(gongguDoc.getJoinList().size()+"/"+gongguDoc.getPeopleCount());
                Log.d("KSM", "onSuccess: 5");
                binding.price09.setText(gongguDoc.getPrice());
                DocumentReference docRef2 = db.collection("users").document(gongguDoc.getWriteId());
                docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        appUser = documentSnapshot.toObject(AppUser.class);
                        if(appUser.getImg()!=null){downloadImageToUser(appUser.getImg());}
                    }
                });
                if (gongguDoc.getImageLink() != null) {
                    downloadImageTo(gongguDoc.getImageLink());
                }
                if(gongguDoc.getJoinList().size()>=Integer.parseInt(gongguDoc.getPeopleCount())){
                    if(!gongguDoc.getJoinList().contains(uid)){
                        binding.getRoot().setEnabled(false);
                    }
                }
                else binding.getRoot().setEnabled(true);


                //하트 색 바뀜
                if (gongguDoc.getLikeList().contains(uid)) {
                    binding.heart09Post.setImageResource(R.drawable.heartcount);
                } else binding.heart09Post.setImageResource(R.drawable.heart);
                //스크랩 색 바뀜
                if (gongguDoc.getScrapList().contains(uid)) {
                    binding.star09Post.setImageResource(R.drawable.star);
                } else binding.star09Post.setImageResource(R.drawable.star_blank);

                //참여하기 글자 바뀜
                if (gongguDoc.getJoinList().contains(uid)) {
                    binding.join09Post.setText("참여 취소하기");
                } else binding.join09Post.setText("참여하기");

                //좋아요 버튼 click
                binding.heart09Post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        likeEvent();
                        Log.d("KYR", "09.getLikeList():" + gongguDoc.getLikeList());
                        if (gongguDoc.getLikeList().contains(uid)) {
                            binding.heart09Post.setImageResource(R.drawable.heartcount);
                            Log.d("KYR", "heart로 바꾸기");
                        } else
                            binding.heart09Post.setImageResource(R.drawable.heart);
                    }
                });

                //스크랩 버튼 click
                binding.star09Post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scrapEvent();
                    }
                });
                //참여하기 버튼
                binding.join09Post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        joinEvent();
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
                Glide.with(Post_Inside_09.this).load(uri).into(binding.downloadedImageview);
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
                Glide.with(Post_Inside_09.this).load(uri).into(binding.userImg09Post);
            }
        });
    }

    private void likeEvent() {
        //눌렸는데 uid가 있었던 경우->uid 삭제
        if (gongguDoc.getLikeList().contains(uid)) {
            gongguDoc.getLikeList().remove(uid);
            db.collection("gongu1Writings").document(documentName).set(gongguDoc);
        } else {//uid 없었음->uid 추가
            gongguDoc.getLikeList().add(uid);
            db.collection("gongu1Writings").document(documentName).set(gongguDoc);
        }
        binding.heartCount09.setText(Integer.toString(gongguDoc.getLikeList().size()) + "개");
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
                if (gongguDoc.getScrapList().contains(uid)) {
                    gongguDoc.getScrapList().remove(uid);
                    db.collection("gongu1Writings").document(documentName).set(gongguDoc);
                    //눌렸는데 uid가 있었던 경우->게시물 id 삭제
                    List<String> scrap_id = appUser.getScrap();
                    scrap_id.remove(gongguDoc.getId());
                    appUser.setScrap(scrap_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                } else {//눌렸는데 uid가 없었던 경우->uid 추가
                    gongguDoc.getScrapList().add(uid);
                    db.collection("gongu1Writings").document(documentName).set(gongguDoc);
                    List<String> scrap_id = appUser.getScrap();
                    scrap_id.add(gongguDoc.getId());
                    appUser.setScrap(scrap_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                }
                Log.d("KYR", "09.scrapList():" + gongguDoc.getScrapList());
                if (gongguDoc.getScrapList().contains(uid)) {
                    binding.star09Post.setImageResource(R.drawable.star);
                    Log.d("KYR", "star 바꾸기");
                } else
                    binding.star09Post.setImageResource(R.drawable.star_blank);
            }
        });
    }
    private void joinEvent() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = db.collection("users").document(user.getEmail());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                appUser = documentSnapshot.toObject(AppUser.class);
                //눌렸는데 uid가 있었던 경우->uid 삭제
                if (gongguDoc.getJoinList().contains(uid)) {
                    gongguDoc.getJoinList().remove(uid);
                    db.collection("gongu1Writings").document(documentName).set(gongguDoc);
                    //눌렸는데 uid가 있었던 경우->게시물 id 삭제
                    List<String> join_id = appUser.getMyGonggu();
                    join_id.remove(gongguDoc.getId());
                    appUser.setMyGonggu(join_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                } else {//눌렸는데 uid가 없었던 경우->uid 추가
                    Toast.makeText(getApplicationContext(), "클립보드에 오픈채팅링크 복사됨.",
                            Toast.LENGTH_SHORT).show();
                    ClipboardManager clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("label", gongguDoc.getChatLink());
                    clipboardManager.setPrimaryClip(clipData);
                    gongguDoc.getJoinList().add(uid);
                    db.collection("gongu1Writings").document(documentName).set(gongguDoc);
                    List<String> join_id = appUser.getMyGonggu();
                    join_id.add(gongguDoc.getId());
                    appUser.setMyGonggu(join_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                }
                Log.d("KYR", "09.joinLsit():" + gongguDoc.getJoinList());
                if (gongguDoc.getJoinList().contains(uid)) {
                    binding.join09Post.setText("참여 취소하기");
                    binding.peopleCount09.setText(gongguDoc.getJoinList().size()+"/"+gongguDoc.getPeopleCount());
                    Log.d("KYR", "star 바꾸기");
                } else{
                    binding.join09Post.setText("참여하기");
                    binding.peopleCount09.setText(gongguDoc.getJoinList().size()+"/"+gongguDoc.getPeopleCount());
                }
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