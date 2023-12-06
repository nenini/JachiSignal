package com.example.jachisignal.PostActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.CommunityDoc;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInsideCommunityBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class Post_Inside_Community extends AppCompatActivity {
    String uid = getUidOfCurrentUser();
    AppUser appUser;


    private CommunityDoc communityDoc;
    FirebaseFirestore db;
    ActivityPostInsideCommunityBinding binding;
    String documentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostInsideCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String collectionName = intent.getStringExtra("COLLECTION");
        documentName = intent.getStringExtra("DOCUMENT");
        Log.d("KSM", collectionName + "  " + documentName);

        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("communityWritings").document(documentName);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                communityDoc = documentSnapshot.toObject(CommunityDoc.class);
                Log.d("KSM", "onSuccess: ");
                binding.titleCommunityPost.setText(communityDoc.getContentTitle());

                binding.textCommunityPost.setText(communityDoc.getText());

                binding.heartCountCommunityPost.setText(Integer.toString(communityDoc.getLikeList().size()) + "개");

                binding.nicknameCommunityPost.setText(communityDoc.getNickname());
                DocumentReference docRef2 = db.collection("users").document(communityDoc.getWriteId());
                docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        appUser = documentSnapshot.toObject(AppUser.class);
                        if(appUser.getImg()!=null){downloadImageToUser(appUser.getImg());}
                    }
                });

                if(communityDoc.getImageLink()!=null){ downloadImageTo(communityDoc.getImageLink());}

                //하트 색 바뀜
                if(communityDoc.getLikeList().contains(uid)){
                    binding.heartCommunityPost.setImageResource(R.drawable.heartcount);
                }
                else binding.heartCommunityPost.setImageResource(R.drawable.heart);
                //스크랩 색 바뀜
                if(communityDoc.getScrapList().contains(uid)){
                    binding.starCommunityPost.setImageResource(R.drawable.star);
                }
                else binding.starCommunityPost.setImageResource(R.drawable.star_blank);

                //좋아요 버튼 click
                binding.heartCommunityPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        likeEvent();
                        Log.d("KYR","community.getLikeList():" +communityDoc.getLikeList());
                        if(communityDoc.getLikeList().contains(uid)){
                            binding.heartCommunityPost.setImageResource(R.drawable.heartcount);
                            Log.d("KYR","heart로 바꾸기");
                        }
                        else
                            binding.heartCommunityPost.setImageResource(R.drawable.heart);
                    }
                });

                //스크랩 버튼 click
                binding.starCommunityPost.setOnClickListener(new View.OnClickListener() {
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
        Log.d("KYR", "포스트 커뮤니티");
        StorageReference gsReference = storage.getReferenceFromUrl(uri); // from gs://~~~
        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("KYR", "포스트레시피 url"+uri.toString());
                Glide.with(Post_Inside_Community.this).load(uri).into(binding.downloadedImageview);
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
                Glide.with(Post_Inside_Community.this).load(uri).into(binding.userImgCommunityPost);
            }
        });
    }

    //좋아요함수
    private void likeEvent(){
        //눌렸는데 uid가 있었던 경우->uid 삭제
        if(communityDoc.getLikeList().contains(uid)){
            communityDoc.getLikeList().remove(uid);
            db.collection("communityWritings").document(documentName).set(communityDoc);
        }
        else{//uid 없었음->uid 추가
            communityDoc.getLikeList().add(uid);
            db.collection("communityWritings").document(documentName).set(communityDoc);
        }
        binding.heartCountCommunityPost.setText(Integer.toString(communityDoc.getLikeList().size())+"개");
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
                if(communityDoc.getScrapList().contains(uid)){
                    communityDoc.getScrapList().remove(uid);
                    db.collection("communityWritings").document(documentName).set(communityDoc);
                    //눌렸는데 uid가 있었던 경우->게시물 id 삭제
                    List<String> scrap_id=appUser.getScrap();
                    scrap_id.remove(communityDoc.getId());
                    appUser.setScrap(scrap_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                }
                else {//눌렸는데 uid가 없었던 경우->uid 추가
                    communityDoc.getScrapList().add(uid);
                    db.collection("communityWritings").document(documentName).set(communityDoc);
                    List<String> scrap_id=appUser.getScrap();
                    scrap_id.add(communityDoc.getId());
                    appUser.setScrap(scrap_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                }
                Log.d("KYR","recipeDoc.scrapList():" +communityDoc.getScrapList());
                if(communityDoc.getScrapList().contains(uid)){
                    binding.starCommunityPost.setImageResource(R.drawable.star);
                    Log.d("KYR","star 바꾸기");
                }
                else
                    binding.starCommunityPost.setImageResource(R.drawable.star_blank);
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