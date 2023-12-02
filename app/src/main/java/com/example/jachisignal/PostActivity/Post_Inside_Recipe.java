package com.example.jachisignal.PostActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInsideCommunityBinding;
import com.example.jachisignal.databinding.ActivityPostInsideRecipeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Post_Inside_Recipe extends AppCompatActivity {
    private Uri uri;

    private RecipeDoc recipeDoc;
    String documentName;
    FirebaseFirestore db;
    ActivityPostInsideRecipeBinding binding;
    String uid = getUidOfCurrentUser();
    DocumentReference docRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostInsideRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String collectionName = intent.getStringExtra("COLLECTION");
        documentName = intent.getStringExtra("DOCUMENT");
        Log.d("KSM", collectionName + "  " + documentName);
//        binding.heartRecipePost.setOnClickListener(new View.OnClickListener() {
//            //좋아요 버튼 click
//            @Override
//            public void onClick(View v) {
//                likeEvent();
//                Log.d("KYR","recipeDoc.getLikeList():" +recipeDoc.getLikeList());
//
//                if(recipeDoc.getLikeList().contains(uid)){
//                    binding.heartRecipePost.setImageResource(R.drawable.heart);
//                }
//                else
//                    binding.heartRecipePost.setImageResource(R.drawable.heartcount);
//            }
//        });
//        binding.heartRecipePost.setOnClickListener(new View.OnClickListener() {
//            //좋아요 버튼 click
//            @Override
//            public void onClick(View v) {
//                likeEvent();
//                Log.d("KYR","recipeDoc.getLikeList():" +recipeDoc.getLikeList());
////                Log.d("KYR", String.valueOf(recipeDoc.getLikeList().contains(uid)));
//
//                if(recipeDoc.getLikeList().contains(uid)){
//                    binding.heartRecipePost.setImageResource(R.drawable.heartcount);
//                    Log.d("KYR","heart로 바꾸기");
//                }
//                else
//                    binding.heartRecipePost.setImageResource(R.drawable.heart);
//            }
//        });
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("recipeWritings").document(documentName);
//        docRef.update("likelist", FieldValue.arrayUnion(uid));
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
                if(recipeDoc.getLikeList().contains(uid)){
                    binding.heartRecipePost.setImageResource(R.drawable.heartcount);
                }
                else binding.heartRecipePost.setImageResource(R.drawable.heart);


                binding.heartRecipePost.setOnClickListener(new View.OnClickListener() {
                    //좋아요 버튼 click
                    @Override
                    public void onClick(View v) {
                        likeEvent();
                        Log.d("KYR","recipeDoc.getLikeList():" +recipeDoc.getLikeList());
//                Log.d("KYR", String.valueOf(recipeDoc.getLikeList().contains(uid)));

                        if(recipeDoc.getLikeList().contains(uid)){
                            binding.heartRecipePost.setImageResource(R.drawable.heartcount);
                            Log.d("KYR","heart로 바꾸기");
                        }
                        else
                            binding.heartRecipePost.setImageResource(R.drawable.heart);
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
        //눌렸는데 uid가 있었던 경우
        if(recipeDoc.getLikeList().contains(uid)){
            recipeDoc.getLikeList().remove(uid);
            db.collection("recipeWritings").document(documentName).set(recipeDoc);
//            docRef.update("likelist",FieldValue.arrayRemove(uid));

        }
        else{
            recipeDoc.getLikeList().add(uid);
            db.collection("recipeWritings").document(documentName).set(recipeDoc);
        }
        binding.heartCountRecipePost.setText(Integer.toString(recipeDoc.getLikeList().size())+"개");


//            docRef.update("likelist",FieldValue.arrayUnion(uid));
    }

    private boolean hasSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null ? true : false;
    }

    private String getUidOfCurrentUser() {
        return hasSignedIn() ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
    }
}
