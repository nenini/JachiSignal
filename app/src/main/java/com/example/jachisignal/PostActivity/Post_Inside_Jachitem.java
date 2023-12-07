package com.example.jachisignal.PostActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.Chat;
import com.example.jachisignal.Doc.ChatHolder;
import com.example.jachisignal.Doc.JachiDoc;
import com.example.jachisignal.Doc.NestedChat;
import com.example.jachisignal.Doc.NestedChatHolder;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInsideJachitemBinding;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.DocumentMask;

import java.util.List;


public class Post_Inside_Jachitem extends AppCompatActivity {
    private static final String TAG = "KSM";
    private Uri uri;
    private JachiDoc jachiDoc;
    String documentName;
    FirebaseFirestore db;
    ActivityPostInsideJachitemBinding binding;
    String uid = getUidOfCurrentUser();
    AppUser appUser;
    private FirestoreRecyclerAdapter adapter;

    private FirestoreRecyclerAdapter adapterNestedChat;


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
                DocumentReference docRef2 = db.collection("users").document(jachiDoc.getWriteId());
                docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        appUser = documentSnapshot.toObject(AppUser.class);
                        if(appUser.getImg()!=null){downloadImageToUser(appUser.getImg());}
                    }
                });
                if (jachiDoc.getImageLink() != null) {
                    downloadImageTo(jachiDoc.getImageLink());
                }

                //하트 색 바뀜
                if (jachiDoc.getLikeList().contains(uid)) {
                    binding.heartJachiPost.setImageResource(R.drawable.heartcount);
                } else binding.heartJachiPost.setImageResource(R.drawable.heart);
                //스크랩 색 바뀜
                if (jachiDoc.getScrapList().contains(uid)) {
                    binding.starJachiPost.setImageResource(R.drawable.star);
                } else binding.starJachiPost.setImageResource(R.drawable.star_blank);

                //좋아요 버튼 click
                binding.heartJachiPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        likeEvent();
                        Log.d("KYR", "jachiDoc.getLikeList():" + jachiDoc.getLikeList());
                        if (jachiDoc.getLikeList().contains(uid)) {
                            binding.heartJachiPost.setImageResource(R.drawable.heartcount);
                            Log.d("KYR", "heart로 바꾸기");
                        } else
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
        /*--- 여기부터 채팅 관련 코드  ---*/
        Query query = docRef.collection("chats")
                .orderBy("timestamp")
                .limit(50);
        Log.d("KSM", "퀴리 문제 없음");
        FirestoreRecyclerOptions<Chat> options = new FirestoreRecyclerOptions.Builder<Chat>()
                .setQuery(query, Chat.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Chat, ChatHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull Chat model) {
                holder.bind(model);

                Log.d("KSM","리사이클러 문제 없음1");


                String id = holder.getmNickname().getText().toString()+"_"+holder.getmText().getText().toString();


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DocumentReference userDoc = db.collection("users").document(user.getEmail());

                holder.getmNestedChatBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.nestedChatBox.setVisibility(View.VISIBLE);
                        binding.nestedCancelBtn.setVisibility(View.VISIBLE);


                        userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                appUser = documentSnapshot.toObject(AppUser.class);
                                binding.nestedSendBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String text = binding.nestedChatText.getText().toString();
                                        NestedChat newNestedChat = new NestedChat(appUser.getNickname()+"_"+text, appUser.getNickname(), text);
                                        docRef.collection("chats").document(id).collection("nestedChats")
                                                .document(appUser.getNickname()+"_"+text).set(newNestedChat);
                                        binding.nestedChatText.setText(null);
                                        binding.nestedChatBox.setVisibility(View.INVISIBLE);
                                        binding.nestedCancelBtn.setVisibility(View.INVISIBLE);
                                        new Handler().postDelayed(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                adapterNestedChat.startListening();
                                                Log.d(TAG, "흠ㅏ");
                                            }
                                        }, 1000);
                                    }
                                });
                            }
                        });

                        binding.nestedCancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                binding.nestedChatBox.setVisibility(View.INVISIBLE);
                                binding.nestedCancelBtn.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });

                /*--- 중첩리사이클러뷰 ---*/
                Query query1 = docRef.collection("chats").document(id).collection("nestedChats")
                        .orderBy("timestamp")
                        .limit(50);
                Log.d("KSM", query1.toString());

                FirestoreRecyclerOptions<NestedChat> options = new FirestoreRecyclerOptions.Builder<NestedChat>()
                        .setQuery(query1, NestedChat.class)
                        .build();

                adapterNestedChat = new FirestoreRecyclerAdapter<NestedChat, NestedChatHolder>(options) {

                    @NonNull
                    @Override
                    public NestedChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_nested_chat,parent,false);
                        return new NestedChatHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull NestedChatHolder holder, int position, @NonNull NestedChat model) {
                        holder.bind(model);
                    }
                };

                LinearLayoutManager layoutManager = new LinearLayoutManager(Post_Inside_Jachitem.this);
                holder.getmNestedRecyclerView().setLayoutManager(layoutManager);
                holder.getmNestedRecyclerView().setAdapter(adapterNestedChat);
                adapterNestedChat.startListening();
            }


            @NonNull
            @Override
            public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat,parent,false);

                Log.d("KSM","리사이클러 문제 없음2");
                return new ChatHolder(view);
            }
        };

        binding.chatRecyclerViewJachitem.setLayoutManager(new LinearLayoutManager(this));
        binding.chatRecyclerViewJachitem.setAdapter(adapter);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference userDoc = db.collection("users").document(user.getEmail());
        userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                appUser = documentSnapshot.toObject(AppUser.class);
                binding.sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = binding.chatText.getText().toString();
                        Chat newChat = new Chat(appUser.getNickname()+"_"+text, appUser.getNickname(), text);
                        docRef.collection("chats").document(appUser.getNickname()+"_"+text).set(newChat);
                        binding.chatText.setText(null);
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
                Glide.with(Post_Inside_Jachitem.this).load(uri).into(binding.downloadedImageview);
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
                Glide.with(Post_Inside_Jachitem.this).load(uri).into(binding.userImgJachiPost);
            }
        });
    }

    private void likeEvent() {
        //눌렸는데 uid가 있었던 경우->uid 삭제
        if (jachiDoc.getLikeList().contains(uid)) {
            jachiDoc.getLikeList().remove(uid);
            jachiDoc.setLikeListCount(jachiDoc.getLikeListCount() - 1);
            db.collection("jachitemWritings").document(documentName).set(jachiDoc);
        } else {//uid 없었음->uid 추가
            jachiDoc.getLikeList().add(uid);
            jachiDoc.setLikeListCount(jachiDoc.getLikeListCount() + 1);
            db.collection("jachitemWritings").document(documentName).set(jachiDoc);
        }
        binding.heartCountJachitem.setText(Integer.toString(jachiDoc.getLikeList().size()) + "개");
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
                if (jachiDoc.getScrapList().contains(uid)) {
                    jachiDoc.getScrapList().remove(uid);
                    db.collection("jachitemWritings").document(documentName).set(jachiDoc);
                    //눌렸는데 uid가 있었던 경우->게시물 id 삭제
                    List<String> scrap_id = appUser.getScrap();
                    scrap_id.remove(jachiDoc.getId());
                    appUser.setScrap(scrap_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                } else {//눌렸는데 uid가 없었던 경우->uid 추가
                    jachiDoc.getScrapList().add(uid);
                    db.collection("jachitemWritings").document(documentName).set(jachiDoc);
                    List<String> scrap_id = appUser.getScrap();
                    scrap_id.add(jachiDoc.getId());
                    appUser.setScrap(scrap_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                }
                Log.d("KYR", "jachiDoc.scrapList():" + jachiDoc.getScrapList());
                if (jachiDoc.getScrapList().contains(uid)) {
                    binding.starJachiPost.setImageResource(R.drawable.star);
                    Log.d("KYR", "star 바꾸기");
                } else
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
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }
}