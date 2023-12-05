package com.example.jachisignal.MyPageActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.CommunityDoc;
import com.example.jachisignal.Doc.GongguDoc;
import com.example.jachisignal.Doc.GongguDoc2;
import com.example.jachisignal.Doc.JachiDoc;
import com.example.jachisignal.Doc.LeisureDoc;
import com.example.jachisignal.Doc.MyPageScrapAdapter;
import com.example.jachisignal.Doc.MyPageScrapDoc;
import com.example.jachisignal.Doc.MyPageWriteAdapter;
import com.example.jachisignal.Doc.MyPageWriteDoc;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityMypageMywriteBinding;
import com.example.jachisignal.databinding.ActivityMypageScrapBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class mypage_mywrite extends AppCompatActivity {
    private MyPageWriteAdapter adapter;
    AppUser appUser;
    private FirebaseFirestore db;
    String writingNum[];
    MyPageWriteDoc myPageWriteDoc;
    private RecipeDoc recipeDoc;
    private GongguDoc gongguDoc;
    private GongguDoc2 gongguDoc2;
    private LeisureDoc leisureDoc;
    private CommunityDoc communityDoc;
    private JachiDoc jachiDoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMypageMywriteBinding binding=ActivityMypageMywriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getEmail());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                appUser = documentSnapshot.toObject(AppUser.class);
                List<String> user_myWrite=appUser.getMyWrite();
                init();
                processUserMyWrite(0, user_myWrite);
            }
        });
    }



    private  void processUserMyWrite(final int index, final  List<String>userWrite){
        if(index<userWrite.size()){
            writingNum = userWrite.get(index).split("_");
            Log.d("KYR", "userWrite: " + userWrite.get(index));
            Log.d("KYR", "writingNum[0]: " + writingNum[0]);
            Log.d("KYR", "writingNum[0]_writingNum[1]: " + writingNum[0] + "_" + writingNum[1]);

            if(writingNum[0].compareTo("1")==0){
                Log.d("KYR", "writingNum[0]_writingNum[1]: " + writingNum[0] + "_" + writingNum[1]);
                DocumentReference docRef_1 = db.collection("gongu1Writings").document(writingNum[1]);
                docRef_1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        gongguDoc = documentSnapshot.toObject(GongguDoc.class);
                        myPageWriteDoc = new MyPageWriteDoc(gongguDoc.getItemName(), gongguDoc.getText(), gongguDoc.getCategory(), gongguDoc.getLikeList(),gongguDoc.getScrapList());
                        Log.d("KYR", "init()" + writingNum[1]);
                        adapter.addItem(myPageWriteDoc);
                        adapter.notifyDataSetChanged();
                        Log.d("KYR", "addItem()" + writingNum[1]);
                        // 다음 항목을 처리하기 위해 재귀 호출
                        processUserMyWrite(index + 1, userWrite);
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
                        myPageWriteDoc = new MyPageWriteDoc(gongguDoc2.getItemName(), gongguDoc2.getText(), gongguDoc2.getCategory(), gongguDoc2.getLikeList(),gongguDoc2.getScrapList());
                        Log.d("KYR", "init()" + writingNum[1]);
                        adapter.addItem(myPageWriteDoc);
                        adapter.notifyDataSetChanged();
                        Log.d("KYR", "addItem()" + writingNum[1]);
                        // 다음 항목을 처리하기 위해 재귀 호출
                        processUserMyWrite(index + 1, userWrite);
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
                        myPageWriteDoc = new MyPageWriteDoc(leisureDoc.getContentTitle(), leisureDoc.getText(), leisureDoc.getCategory(), leisureDoc.getLikeList(),leisureDoc.getScrapList());
                        Log.d("KYR", "init()" + writingNum[1]);
                        adapter.addItem(myPageWriteDoc);
                        adapter.notifyDataSetChanged();
                        Log.d("KYR", "addItem()" + writingNum[1]);
                        // 다음 항목을 처리하기 위해 재귀 호출
                        processUserMyWrite(index + 1, userWrite);
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
                        myPageWriteDoc = new MyPageWriteDoc(communityDoc.getContentTitle(), communityDoc.getText(), communityDoc.getCategory(), communityDoc.getLikeList(),communityDoc.getLikeList());
                        Log.d("KYR", "init()" + writingNum[1]);
                        adapter.addItem(myPageWriteDoc);
                        adapter.notifyDataSetChanged();
                        Log.d("KYR", "addItem()" + writingNum[1]);
                        // 다음 항목을 처리하기 위해 재귀 호출
                        processUserMyWrite(index + 1, userWrite);
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
                        myPageWriteDoc = new MyPageWriteDoc(jachiDoc.getContentTitle(), jachiDoc.getText(), jachiDoc.getCategory(), jachiDoc.getLikeList(),jachiDoc.getScrapList());
                        Log.d("KYR", "init()" + writingNum[1]);
                        adapter.addItem(myPageWriteDoc);
                        adapter.notifyDataSetChanged();
                        Log.d("KYR", "addItem()" + writingNum[1]);
                        // 다음 항목을 처리하기 위해 재귀 호출
                        processUserMyWrite(index + 1, userWrite);
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
                        myPageWriteDoc = new MyPageWriteDoc(recipeDoc.getContentTitle(), recipeDoc.getText(), recipeDoc.getCategory(), recipeDoc.getLikeList(),recipeDoc.getScrapList());
                        Log.d("KYR", "init()" + writingNum[1]);
                        adapter.addItem(myPageWriteDoc);
                        adapter.notifyDataSetChanged();
                        Log.d("KYR", "addItem()" + writingNum[1]);

                        // 다음 항목을 처리하기 위해 재귀 호출
                        processUserMyWrite(index + 1, userWrite);
                    }
                });
            } else {
                // 다음 항목을 처리하기 위해 재귀 호출
                processUserMyWrite(index + 1, userWrite);
            }

        }
    }
    private void init(){
        RecyclerView recyclerView=findViewById(R.id.mypage_myWrite_recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new MyPageWriteAdapter();
        recyclerView.setAdapter(adapter);
    }
}