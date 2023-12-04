package com.example.jachisignal.PostActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInsideCommunityBinding;
import com.example.jachisignal.databinding.ActivityPostInsideRecipeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Post_Inside_Recipe extends AppCompatActivity {
    private Uri uri;

    private RecipeDoc recipeDoc;
    String documentName;
    FirebaseFirestore db;
    ActivityPostInsideRecipeBinding binding;
    String uid = getUidOfCurrentUser();
    AppUser appUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostInsideRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String collectionName = intent.getStringExtra("COLLECTION");
        documentName = intent.getStringExtra("DOCUMENT");
        Log.d("KSM", collectionName + "  " + documentName);
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("recipeWritings").document(documentName);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                recipeDoc = documentSnapshot.toObject(RecipeDoc.class);
                Log.d("KSM", "onSuccess: ");
                binding.titleRecipePost.setText(recipeDoc.getContentTitle());

                binding.textRecipePost.setText(recipeDoc.getText());

                binding.heartCountRecipePost.setText(Integer.toString(recipeDoc.getLikeList().size()) + "개");

                binding.nicknameRecipePost.setText(recipeDoc.getNickname());
                if(recipeDoc.getImageLink()!=null){ downloadImageTo(recipeDoc.getImageLink());}

                //하트 색 바뀜
                if(recipeDoc.getLikeList().contains(uid)){
                    binding.heartRecipePost.setImageResource(R.drawable.heartcount);
                }
                else binding.heartRecipePost.setImageResource(R.drawable.heart);
                //스크랩 색 바뀜
                if(recipeDoc.getScrapList().contains(uid)){
                    binding.starRecipePost.setImageResource(R.drawable.star);
                }
                else binding.starRecipePost.setImageResource(R.drawable.star_blank);

                //좋아요 버튼 click
                binding.heartRecipePost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        likeEvent();
                        Log.d("KYR","recipeDoc.getLikeList():" +recipeDoc.getLikeList());
                        if(recipeDoc.getLikeList().contains(uid)){
                            binding.heartRecipePost.setImageResource(R.drawable.heartcount);
                            Log.d("KYR","heart로 바꾸기");
                        }
                        else
                            binding.heartRecipePost.setImageResource(R.drawable.heart);
                    }
                });

                //스크랩 버튼 click
                binding.starRecipePost.setOnClickListener(new View.OnClickListener() {
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
                Glide.with(Post_Inside_Recipe.this).load(uri).into(binding.downloadedImageview);
            }
        });
    }

    //좋아요함수
    private void likeEvent(){
        //눌렸는데 uid가 있었던 경우->uid 삭제
        if(recipeDoc.getLikeList().contains(uid)){
            recipeDoc.getLikeList().remove(uid);
            db.collection("recipeWritings").document(documentName).set(recipeDoc);
        }
        else{//uid 없었음->uid 추가
            recipeDoc.getLikeList().add(uid);
            db.collection("recipeWritings").document(documentName).set(recipeDoc);
        }
        binding.heartCountRecipePost.setText(Integer.toString(recipeDoc.getLikeList().size())+"개");
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
                if(recipeDoc.getScrapList().contains(uid)){
                    recipeDoc.getScrapList().remove(uid);
                    db.collection("recipeWritings").document(documentName).set(recipeDoc);
                    //눌렸는데 uid가 있었던 경우->게시물 id 삭제
                    List<String> scrap_id=appUser.getScrap();
                    scrap_id.remove(recipeDoc.getId());
                    appUser.setScrap(scrap_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                }
                else {//눌렸는데 uid가 없었던 경우->uid 추가
                    recipeDoc.getScrapList().add(uid);
                    db.collection("recipeWritings").document(documentName).set(recipeDoc);
                    List<String> scrap_id=appUser.getScrap();
                    scrap_id.add(recipeDoc.getId());
                    appUser.setScrap(scrap_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                }
                Log.d("KYR","recipeDoc.scrapList():" +recipeDoc.getScrapList());
                if(recipeDoc.getScrapList().contains(uid)){
                    binding.starRecipePost.setImageResource(R.drawable.star);
                    Log.d("KYR","star 바꾸기");
                }
                else
                    binding.starRecipePost.setImageResource(R.drawable.star_blank);
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
