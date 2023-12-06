package com.example.jachisignal.PostActivity;

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
import com.example.jachisignal.Doc.LeisureDoc;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInsidePlayingBinding;
import com.example.jachisignal.databinding.ActivityPostInsideRecipeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class Post_Inside_Playing extends AppCompatActivity {
    private LeisureDoc leisureDoc;
    private static final String TAG = "KSM";
    FirebaseFirestore db;
    AppUser appUser;
    String documentName;

    String uid = getUidOfCurrentUser();


    ActivityPostInsidePlayingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostInsidePlayingBinding.inflate(getLayoutInflater());
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
                leisureDoc = documentSnapshot.toObject(LeisureDoc.class);
                binding.placeLeisure.setText(leisureDoc.getPlace());
                binding.meetingTimeLeisure.setText(leisureDoc.getDate());
                binding.nicknameLeisure.setText(leisureDoc.getNickname());
                binding.heartCountLeisure.setText(Integer.toString(leisureDoc.getLikeList().size()) + "개");
                binding.textLeisure.setText(leisureDoc.getText());
                binding.titleLeisure.setText(leisureDoc.getContentTitle());
                binding.peopleCountLeisure.setText(leisureDoc.getJoinList().size()+"/"+leisureDoc.getPeopleCount());
                DocumentReference docRef2 = db.collection("users").document(leisureDoc.getWriteId());
                docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        appUser = documentSnapshot.toObject(AppUser.class);
                        if(appUser.getImg()!=null){downloadImageToUser(appUser.getImg());}
                    }
                });
                if (leisureDoc.getImageLink() != null) {
                    downloadImageTo(leisureDoc.getImageLink());
                }
                if(leisureDoc.getJoinList().size()>=Integer.parseInt(leisureDoc.getPeopleCount())){
                    if(!leisureDoc.getJoinList().contains(uid)){
                        binding.joinLeisurePost.setEnabled(false);
                    }
                }
                else binding.joinLeisurePost.setEnabled(true);

                //하트 색 바뀜
                if (leisureDoc.getLikeList().contains(uid)) {
                    binding.heartLeisurePost.setImageResource(R.drawable.heartcount);
                } else binding.heartLeisurePost.setImageResource(R.drawable.heart);
                //스크랩 색 바뀜
                if (leisureDoc.getScrapList().contains(uid)) {
                    binding.starLeisurePost.setImageResource(R.drawable.star);
                } else binding.starLeisurePost.setImageResource(R.drawable.star_blank);
                //참여하기 글자 바뀜
                if (leisureDoc.getJoinList().contains(uid)) {
                    binding.joinLeisurePost.setText("참여 취소하기");
                } else binding.joinLeisurePost.setText("참여하기");

                //좋아요 버튼 click
                binding.heartLeisurePost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        likeEvent();
                        Log.d("KYR", "jachiDoc.getLikeList():" + leisureDoc.getLikeList());
                        if (leisureDoc.getLikeList().contains(uid)) {
                            binding.heartLeisurePost.setImageResource(R.drawable.heartcount);
                            Log.d("KYR", "heart로 바꾸기");
                        } else
                            binding.heartLeisurePost.setImageResource(R.drawable.heart);
                    }
                });

                //스크랩 버튼 click
                binding.starLeisurePost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scrapEvent();
                    }
                });
                //참여하기 버튼
                binding.joinLeisurePost.setOnClickListener(new View.OnClickListener() {
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
                Glide.with(Post_Inside_Playing.this).load(uri).into(binding.downloadedImageview);
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
                Glide.with(Post_Inside_Playing.this).load(uri).into(binding.userImgLeisurePost);
            }
        });
    }

    private void likeEvent() {
        //눌렸는데 uid가 있었던 경우->uid 삭제
        if (leisureDoc.getLikeList().contains(uid)) {
            leisureDoc.getLikeList().remove(uid);
            db.collection("leisureWritings").document(documentName).set(leisureDoc);
        } else {//uid 없었음->uid 추가
            leisureDoc.getLikeList().add(uid);
            db.collection("leisureWritings").document(documentName).set(leisureDoc);
        }
        binding.heartCountLeisure.setText(Integer.toString(leisureDoc.getLikeList().size()) + "개");
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
                if (leisureDoc.getScrapList().contains(uid)) {
                    leisureDoc.getScrapList().remove(uid);
                    db.collection("leisureWritings").document(documentName).set(leisureDoc);
                    //눌렸는데 uid가 있었던 경우->게시물 id 삭제
                    List<String> scrap_id = appUser.getScrap();
                    scrap_id.remove(leisureDoc.getId());
                    appUser.setScrap(scrap_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                } else {//눌렸는데 uid가 없었던 경우->uid 추가
                    leisureDoc.getScrapList().add(uid);
                    db.collection("leisureWritings").document(documentName).set(leisureDoc);
                    List<String> scrap_id = appUser.getScrap();
                    scrap_id.add(leisureDoc.getId());
                    appUser.setScrap(scrap_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                }
                Log.d("KYR", "leisure.scrapList():" + leisureDoc.getScrapList());
                if (leisureDoc.getScrapList().contains(uid)) {
                    binding.starLeisurePost.setImageResource(R.drawable.star);
                    Log.d("KYR", "star 바꾸기");
                } else
                    binding.starLeisurePost.setImageResource(R.drawable.star_blank);
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
                if (leisureDoc.getJoinList().contains(uid)) {
                    leisureDoc.getJoinList().remove(uid);
                    db.collection("leisureWritings").document(documentName).set(leisureDoc);
                    //눌렸는데 uid가 있었던 경우->게시물 id 삭제
                    List<String> join_id = appUser.getMyGonggu();
                    join_id.remove(leisureDoc.getId());
                    appUser.setMyGonggu(join_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                } else {//눌렸는데 uid가 없었던 경우->uid 추가
                    Toast.makeText(getApplicationContext(), "클립보드에 오픈채팅링크 복사됨.",
                            Toast.LENGTH_SHORT).show();
                    ClipboardManager clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("label", leisureDoc.getChatLink());
                    clipboardManager.setPrimaryClip(clipData);
                    leisureDoc.getJoinList().add(uid);
                    db.collection("leisureWritings").document(documentName).set(leisureDoc);
                    List<String> join_id = appUser.getMyGonggu();
                    join_id.add(leisureDoc.getId());
                    appUser.setMyGonggu(join_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                }
                Log.d("KYR", "092.joinLsit():" + leisureDoc.getJoinList());
                if (leisureDoc.getJoinList().contains(uid)) {
                    binding.joinLeisurePost.setText("참여 취소하기");
                    binding.peopleCountLeisure.setText(leisureDoc.getJoinList().size()+"/"+leisureDoc.getPeopleCount());
                    Log.d("KYR", "star 바꾸기");
                } else{
                    binding.joinLeisurePost.setText("참여하기");
                    binding.peopleCountLeisure.setText(leisureDoc.getJoinList().size()+"/"+leisureDoc.getPeopleCount());
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