package com.example.jachisignal.MyPageActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.CommunityDoc;
import com.example.jachisignal.Doc.GongguDoc;
import com.example.jachisignal.Doc.GongguDoc2;
import com.example.jachisignal.Doc.JachiDoc;
import com.example.jachisignal.Doc.LeisureDoc;
import com.example.jachisignal.Doc.MyPageScrapAdapter;
import com.example.jachisignal.Doc.MyPageScrapDoc;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityMypageScrapBinding;
import com.example.jachisignal.databinding.ItemCommunityBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class mypage_scrap extends AppCompatActivity {
    private MyPageScrapAdapter adapter;
    AppUser appUser;
    private FirebaseFirestore db;
    String writingNum[];
    MyPageScrapDoc myPageScrapDoc;
    private RecipeDoc recipeDoc;
    private GongguDoc gongguDoc;
    private GongguDoc2 gongguDoc2;
    private LeisureDoc leisureDoc;
    private CommunityDoc communityDoc;
    private JachiDoc jachiDoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMypageScrapBinding binding=ActivityMypageScrapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getEmail());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                appUser = documentSnapshot.toObject(AppUser.class);
                List<String> user_scrap=appUser.getScrap();
                init();
                processUserScrap(0, user_scrap);
            }
        });
    }



    private  void processUserScrap(final int index, final  List<String>userScrap){
        if(index<userScrap.size()){
            writingNum = userScrap.get(index).split("_");
            Log.d("KYR", "user_scrap: " + userScrap.get(index));
            Log.d("KYR", "writingNum[0]: " + writingNum[0]);
            Log.d("KYR", "writingNum[0]_writingNum[1]: " + writingNum[0] + "_" + writingNum[1]);

            if(writingNum[0].compareTo("1")==0){
                Log.d("KYR", "writingNum[0]_writingNum[1]: " + writingNum[0] + "_" + writingNum[1]);
                DocumentReference docRef_1 = db.collection("gongu1Writings").document(writingNum[1]);
                docRef_1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        gongguDoc = documentSnapshot.toObject(GongguDoc.class);
                        myPageScrapDoc = new MyPageScrapDoc(gongguDoc.getContentTitle(), gongguDoc.getText(), gongguDoc.getCategory(), gongguDoc.getLikeList());
                        Log.d("KYR", "init()" + writingNum[1]);
                        adapter.addItem(myPageScrapDoc);
                        adapter.notifyDataSetChanged();
                        Log.d("KYR", "addItem()" + writingNum[1]);
                        // 다음 항목을 처리하기 위해 재귀 호출
                        processUserScrap(index + 1, userScrap);
                    }
                });
            }
            else if(writingNum[0].compareTo("2")==0){
                Log.d("KYR", "writingNum[0]_writingNum[1]: " + writingNum[0] + "_" + writingNum[1]);
                DocumentReference docRef_1 = db.collection("gongu2Writings").document(writingNum[1]);
                docRef_1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        gongguDoc2 = documentSnapshot.toObject(GongguDoc2.class);
                        myPageScrapDoc = new MyPageScrapDoc(gongguDoc2.getContentTitle(), gongguDoc2.getText(), gongguDoc2.getCategory(), gongguDoc.getLikeList());
                        Log.d("KYR", "init()" + writingNum[1]);
                        adapter.addItem(myPageScrapDoc);
                        adapter.notifyDataSetChanged();
                        Log.d("KYR", "addItem()" + writingNum[1]);
                        // 다음 항목을 처리하기 위해 재귀 호출
                        processUserScrap(index + 1, userScrap);
                    }
                });
            }
            else if(writingNum[0].compareTo("3")==0){
                Log.d("KYR", "writingNum[0]_writingNum[1]: " + writingNum[0] + "_" + writingNum[1]);
                DocumentReference docRef_1 = db.collection("leisureWritings").document(writingNum[1]);
                docRef_1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        leisureDoc = documentSnapshot.toObject(LeisureDoc.class);
                        myPageScrapDoc = new MyPageScrapDoc(leisureDoc.getContentTitle(), leisureDoc.getText(), leisureDoc.getCategory(), leisureDoc.getLikeList());
                        Log.d("KYR", "init()" + writingNum[1]);
                        adapter.addItem(myPageScrapDoc);
                        adapter.notifyDataSetChanged();
                        Log.d("KYR", "addItem()" + writingNum[1]);
                        // 다음 항목을 처리하기 위해 재귀 호출
                        processUserScrap(index + 1, userScrap);
                    }
                });
            }
            else if(writingNum[0].compareTo("4")==0){
                Log.d("KYR", "writingNum[0]_writingNum[1]: " + writingNum[0] + "_" + writingNum[1]);
                DocumentReference docRef_1 = db.collection("communityWritings").document(writingNum[1]);
                docRef_1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        communityDoc = documentSnapshot.toObject(CommunityDoc.class);
                        myPageScrapDoc = new MyPageScrapDoc(communityDoc.getContentTitle(), communityDoc.getText(), communityDoc.getCategory(), communityDoc.getLikeList());
                        Log.d("KYR", "init()" + writingNum[1]);
                        adapter.addItem(myPageScrapDoc);
                        adapter.notifyDataSetChanged();
                        Log.d("KYR", "addItem()" + writingNum[1]);
                        // 다음 항목을 처리하기 위해 재귀 호출
                        processUserScrap(index + 1, userScrap);
                    }
                });
            }
            else if(writingNum[0].compareTo("5")==0){
                Log.d("KYR", "writingNum[0]_writingNum[1]: " + writingNum[0] + "_" + writingNum[1]);
                DocumentReference docRef_1 = db.collection("jachitemWritings").document(writingNum[1]);
                docRef_1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        jachiDoc = documentSnapshot.toObject(JachiDoc.class);
                        myPageScrapDoc = new MyPageScrapDoc(jachiDoc.getContentTitle(), jachiDoc.getText(), jachiDoc.getCategory(), jachiDoc.getLikeList());
                        Log.d("KYR", "init()" + writingNum[1]);
                        adapter.addItem(myPageScrapDoc);
                        adapter.notifyDataSetChanged();
                        Log.d("KYR", "addItem()" + writingNum[1]);
                        // 다음 항목을 처리하기 위해 재귀 호출
                        processUserScrap(index + 1, userScrap);
                    }
                });
            }
            else if(writingNum[0].compareTo("6")==0){
                Log.d("KYR", "writingNum[0]_writingNum[1]: " + writingNum[0] + "_" + writingNum[1]);
                DocumentReference docRef_1 = db.collection("recipeWritings").document(writingNum[1]);
                docRef_1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        recipeDoc = documentSnapshot.toObject(RecipeDoc.class);
                        myPageScrapDoc = new MyPageScrapDoc(recipeDoc.getContentTitle(), recipeDoc.getText(), recipeDoc.getCategory(), recipeDoc.getLikeList());
                        Log.d("KYR", "init()" + writingNum[1]);
                        adapter.addItem(myPageScrapDoc);
                        adapter.notifyDataSetChanged();
                        Log.d("KYR", "addItem()" + writingNum[1]);

                        // 다음 항목을 처리하기 위해 재귀 호출
                        processUserScrap(index + 1, userScrap);
                    }
                });
            } else {
                // 다음 항목을 처리하기 위해 재귀 호출
                processUserScrap(index + 1, userScrap);
            }

        }
    }
    private void init(){
        RecyclerView recyclerView=findViewById(R.id.mypage_scrap_recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new MyPageScrapAdapter();
        recyclerView.setAdapter(adapter);
    }
}