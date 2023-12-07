package com.example.jachisignal.MyPageActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.CommunityDoc;
import com.example.jachisignal.Doc.GongguDoc;
import com.example.jachisignal.Doc.GongguDoc2;
import com.example.jachisignal.Doc.GongguDocHolder2;
import com.example.jachisignal.Doc.JachiDoc;
import com.example.jachisignal.Doc.LeisureDoc;
import com.example.jachisignal.Doc.LeisureDocHolder;
import com.example.jachisignal.Doc.MyGongguDoc;
import com.example.jachisignal.Doc.MyGongguDocAdapter;
import com.example.jachisignal.Doc.MyPageWriteAdapter;
import com.example.jachisignal.Doc.MyPageWriteDoc;
import com.example.jachisignal.Doc.MypageGongguAdapter;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.PostActivity.Post_Inside_Playing;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityMypageGongguBinding;
import com.example.jachisignal.databinding.ActivityMypageMywriteBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class mypage_gonggu extends AppCompatActivity {
    private MyGongguDocAdapter adapter;
    AppUser appUser;
    private FirebaseFirestore db;
    MyGongguDoc myGongguDoc;
    String writingNum[];
    private GongguDoc gongguDoc;
    private GongguDoc2 gongguDoc2;
    private LeisureDoc leisureDoc;
    List<String> user_myGonggu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMypageGongguBinding binding=ActivityMypageGongguBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getEmail());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                appUser = documentSnapshot.toObject(AppUser.class);
                user_myGonggu=appUser.getMyGonggu();
                binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        DocumentReference docRef = db.collection("users").document(user.getEmail());
                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                appUser = documentSnapshot.toObject(AppUser.class);
                                user_myGonggu=appUser.getMyGonggu();
                                updateRecyclerView(user_myGonggu);
                                // 종료
                                if (binding.refreshLayout != null) {
                                    binding.refreshLayout.setRefreshing(false);
                                }
                            }
                        });
                    }
                });
                init();
                processUserMyWrite(0, user_myGonggu);
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
                        myGongguDoc = new MyGongguDoc(gongguDoc.getNickname(),gongguDoc.getId(),gongguDoc.getWriteId(),gongguDoc.getItemName(),gongguDoc.getText(),"1",gongguDoc.getCategory(),gongguDoc.getImageLink(),gongguDoc.getLikeList(),gongguDoc.getScrapList(),gongguDoc.getJoinList(),gongguDoc.getItemName(),gongguDoc.getPrice(),gongguDoc.getPeopleCount(),gongguDoc.getChatLink(),new ArrayList<String>(),"gongu1Writings");
                        Log.d("KYR", "init()" + writingNum[1]);
                        adapter.addItem(myGongguDoc);
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
                        myGongguDoc = new MyGongguDoc(gongguDoc2.getNickname(),gongguDoc2.getId(),gongguDoc2.getWriteId(),gongguDoc2.getItemName(),gongguDoc2.getText(),"1",gongguDoc2.getCategory(),gongguDoc2.getImageLink(),gongguDoc2.getLikeList(),gongguDoc2.getScrapList(),gongguDoc2.getJoinList(),gongguDoc2.getItemName(),gongguDoc2.getPrice(),gongguDoc2.getPeopleCount(),gongguDoc2.getChatLink(),new ArrayList<String>(),"gongu2Writings");
                        Log.d("KYR", "init()" + writingNum[1]);
                        adapter.addItem(myGongguDoc);
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
                        myGongguDoc = new MyGongguDoc(leisureDoc.getNickname(),leisureDoc.getId(),leisureDoc.getWriteId(),leisureDoc.getContentTitle(),leisureDoc.getText(),"1",leisureDoc.getCategory(),leisureDoc.getImageLink(),leisureDoc.getLikeList(),leisureDoc.getScrapList(),leisureDoc.getJoinList(),leisureDoc.getContentTitle(),leisureDoc.getDate(),leisureDoc.getPeopleCount(),leisureDoc.getChatLink(),new ArrayList<String>(),"leisureWritings");
                        Log.d("KYR", "init()" + writingNum[1]);
                        adapter.addItem(myGongguDoc);
                        adapter.notifyDataSetChanged();
                        Log.d("KYR", "addItem()" + writingNum[1]);
                        // 다음 항목을 처리하기 위해 재귀 호출
                        processUserMyWrite(index + 1, userWrite);
                    }
                });
            }else {
                // 다음 항목을 처리하기 위해 재귀 호출
                processUserMyWrite(index + 1, userWrite);
            }

        }
    }
    private void updateRecyclerView(List<String> user_myWrite) {
        // RecyclerView를 업데이트하는 코드를 여기에 추가하세요.
        // 예를 들어, 어댑터를 초기화하고 사용자의 쓴 글 목록으로 다시 채우는 코드를 넣을 수 있습니다.
        init();
        processUserMyWrite(0, user_myWrite);
    }
    private void init(){
        RecyclerView recyclerView=findViewById(R.id.mypage_myGonggu_recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new MyGongguDocAdapter();
        recyclerView.setAdapter(adapter);
    }
}