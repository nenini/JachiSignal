package com.example.jachisignal.PostActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.Chat;
import com.example.jachisignal.Doc.ChatHolder;
import com.example.jachisignal.Doc.GongguDoc;
import com.example.jachisignal.Doc.GongguDoc2;
import com.example.jachisignal.Doc.NestedChat;
import com.example.jachisignal.Doc.NestedChatHolder;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.Doc.UserInfHolder;
import com.example.jachisignal.Doc.UserInfHolderGonggu2;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInside092Binding;
import com.example.jachisignal.databinding.ActivityPostInside09Binding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
    private static final String TAG = "KSM";
    private FirestoreRecyclerAdapter adapter;
    private FirestoreRecyclerAdapter adapterNestedChat;

    private FirestoreRecyclerAdapter adapter_user;

    String Id;
    String writeID;

    int writeSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityPostInside092Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                binding.peopleCount092.setText(gongguDoc2.getJoinList().size()+"/"+gongguDoc2.getPeopleCount());

                binding.price092.setText(gongguDoc2.getPrice());
                DocumentReference docRef2 = db.collection("users").document(gongguDoc2.getWriteId());
                docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        AppUser appUser = documentSnapshot.toObject(AppUser.class);
                        if(appUser.getImg()!=null){downloadImageToUser(appUser.getImg());}
                    }
                });
                if (gongguDoc2.getImageLink() != null) {
                    downloadImageTo(gongguDoc2.getImageLink());
                }
                if(gongguDoc2.getJoinList().size()>=Integer.parseInt(gongguDoc2.getPeopleCount())){
                    if(!gongguDoc2.getJoinList().contains(uid)){
                        binding.join092Post.setEnabled(false);
                    }
                }
                else binding.join092Post.setEnabled(true);


                //하트 색 바뀜
                if (gongguDoc2.getLikeList().contains(uid)) {
                    binding.heart092Post.setImageResource(R.drawable.heartcount);
                } else binding.heart092Post.setImageResource(R.drawable.heart);
                //스크랩 색 바뀜
                if (gongguDoc2.getScrapList().contains(uid)) {
                    binding.star092Post.setImageResource(R.drawable.star);
                } else binding.star092Post.setImageResource(R.drawable.star_blank);
                //참여하기 글자 바뀜
                if (gongguDoc2.getJoinList().contains(uid)) {
                    binding.join092Post.setText("참여 취소하기");
                } else binding.join092Post.setText("참여하기");

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
                //참여하기 버튼
                binding.join092Post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        joinEvent();
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

                LinearLayoutManager layoutManager = new LinearLayoutManager(Post_Inside_09_2.this);
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

        binding.chatRecyclerView092.setLayoutManager(new LinearLayoutManager(this));
        binding.chatRecyclerView092.setAdapter(adapter);


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

        binding.nickname092.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();

            }
        });
    }

    private void showCustomDialog() {
        Dialog dialog = new Dialog(Post_Inside_09_2.this);
        dialog.setContentView(R.layout.custom_user);

        ImageView dialogImageView = dialog.findViewById(R.id.user_inf_img);
        TextView dialogUserNickname = dialog.findViewById(R.id.user_inf_nickname);
        ImageView dialogCloseClick = dialog.findViewById(R.id.closeClick_img);
        TextView dialogHz = dialog.findViewById(R.id.hz);


        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("gongu2Writings").document(documentName);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                writeID = gongguDoc2.getWriteId();
                gongguDoc2 = documentSnapshot.toObject(GongguDoc2.class);
                int writeSize = appUser.getMyWrite().size();
                dialogHz.setText(writeSize + " HZ");
                dialogUserNickname.setText(gongguDoc2.getNickname());
                DocumentReference docRef2 = db.collection("users").document(gongguDoc2.getWriteId());
                docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        appUser = documentSnapshot.toObject(AppUser.class);
                        if(appUser.getImg()!=null){
                            String uri = appUser.getImg();
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference gsReference = storage.getReferenceFromUrl(uri); // from gs://~~~
                            gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(Post_Inside_09_2.this).load(uri).into(dialogImageView);}
                            });
                        }
                    }
                });
                RecyclerView dialogRecentPostsRecyclerView = dialog.findViewById(R.id.user_inf_recyclerView);

                Query query = FirebaseFirestore.getInstance()
                        .collection("gongu2Writings")
                        .whereEqualTo("writeId",writeID)
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .limit(3);

                FirestoreRecyclerOptions<GongguDoc2> options = new FirestoreRecyclerOptions.Builder<GongguDoc2>()
                        .setQuery(query, GongguDoc2.class)
                        .build();

                adapter_user = new FirestoreRecyclerAdapter<GongguDoc2, UserInfHolderGonggu2>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserInfHolderGonggu2 holder, int position, @NonNull GongguDoc2 model) {
                        holder.bind(model);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("KSM", "Gggg");
                                String title = holder.getmTitle().getText().toString();

                                Log.d("KSM", "eeeee");
                                Intent intent = new Intent(v.getContext(), Post_Inside_09_2.class);
                                intent.putExtra("COLLECTION","gongu2Writings");
                                Log.d("KSM", title);
                                intent.putExtra("DOCUMENT",title);
                                Log.d("KSM", title);
                                startActivity(intent);
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public UserInfHolderGonggu2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        Log.d("ksh", "onCreateViewHolder: 들어옴");
                        View view= LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_user_inf,parent,false);
                        return new UserInfHolderGonggu2(view);
                    }
                };
                adapter_user.startListening();

                dialogRecentPostsRecyclerView.setLayoutManager(new LinearLayoutManager(Post_Inside_09_2.this));
                dialogRecentPostsRecyclerView.setAdapter(adapter_user);

                dialogCloseClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
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
    private void joinEvent() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = db.collection("users").document(user.getEmail());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                appUser = documentSnapshot.toObject(AppUser.class);
                //눌렸는데 uid가 있었던 경우->uid 삭제
                if (gongguDoc2.getJoinList().contains(uid)) {
                    gongguDoc2.getJoinList().remove(uid);
                    db.collection("gongu2Writings").document(documentName).set(gongguDoc2);
                    //눌렸는데 uid가 있었던 경우->게시물 id 삭제
                    List<String> join_id = appUser.getMyGonggu();
                    join_id.remove(gongguDoc2.getId());
                    appUser.setMyGonggu(join_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                } else {//눌렸는데 uid가 없었던 경우->uid 추가
                    Toast.makeText(getApplicationContext(), "클립보드에 오픈채팅링크 복사됨.",
                            Toast.LENGTH_SHORT).show();
                    ClipboardManager clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("label", gongguDoc2.getChatLink());
                    clipboardManager.setPrimaryClip(clipData);
                    gongguDoc2.getJoinList().add(uid);
                    db.collection("gongu2Writings").document(documentName).set(gongguDoc2);
                    List<String> join_id = appUser.getMyGonggu();
                    join_id.add(gongguDoc2.getId());
                    appUser.setMyGonggu(join_id);
                    db.collection("users").document(user.getEmail()).set(appUser);
                }
                Log.d("KYR", "092.joinLsit():" + gongguDoc2.getJoinList());
                if (gongguDoc2.getJoinList().contains(uid)) {
                    binding.join092Post.setText("참여 취소하기");
                    binding.peopleCount092.setText(gongguDoc2.getJoinList().size()+"/"+gongguDoc2.getPeopleCount());
                    Log.d("KYR", "star 바꾸기");
                } else{
                    binding.join092Post.setText("참여하기");
                    binding.peopleCount092.setText(gongguDoc2.getJoinList().size()+"/"+gongguDoc2.getPeopleCount());
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