package com.example.jachisignal.PostActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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

import com.bumptech.glide.Glide;
import com.example.jachisignal.AppUser;
import com.example.jachisignal.Doc.Chat;
import com.example.jachisignal.Doc.ChatHolder;
import com.example.jachisignal.Doc.CommunityDoc;
import com.example.jachisignal.Doc.NestedChat;
import com.example.jachisignal.Doc.NestedChatHolder;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.Doc.UserInfHolder;
import com.example.jachisignal.Doc.UserInfHolderCommunity;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInsideCommunityBinding;
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

public class Post_Inside_Community extends AppCompatActivity {
    String uid = getUidOfCurrentUser();
    AppUser appUser;


    private CommunityDoc communityDoc;
    FirebaseFirestore db;
    ActivityPostInsideCommunityBinding binding;
    String documentName;
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
        binding = ActivityPostInsideCommunityBinding.inflate(getLayoutInflater());
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
                        AppUser appUser = documentSnapshot.toObject(AppUser.class);
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

                LinearLayoutManager layoutManager = new LinearLayoutManager(Post_Inside_Community.this);
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

        binding.chatRecyclerViewCommunity.setLayoutManager(new LinearLayoutManager(this));
        binding.chatRecyclerViewCommunity.setAdapter(adapter);


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

        binding.nicknameCommunityPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();

            }
        });

    }

    private void showCustomDialog() {
        Dialog dialog = new Dialog(Post_Inside_Community.this);
        dialog.setContentView(R.layout.custom_user);

        ImageView dialogImageView = dialog.findViewById(R.id.user_inf_img);
        TextView dialogUserNickname = dialog.findViewById(R.id.user_inf_nickname);
        ImageView dialogCloseClick = dialog.findViewById(R.id.closeClick_img);
        TextView dialogHz = dialog.findViewById(R.id.hz);


        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("communityWritings").document(documentName);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                writeID = communityDoc.getWriteId();
                communityDoc = documentSnapshot.toObject(CommunityDoc.class);
                int writeSize = appUser.getMyWrite().size();
                dialogHz.setText(writeSize + " HZ");
                dialogUserNickname.setText(communityDoc.getNickname());
                DocumentReference docRef2 = db.collection("users").document(communityDoc.getWriteId());
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
                                    Glide.with(Post_Inside_Community.this).load(uri).into(dialogImageView);}
                            });
                        }
                    }
                });
                RecyclerView dialogRecentPostsRecyclerView = dialog.findViewById(R.id.user_inf_recyclerView);

                Query query = FirebaseFirestore.getInstance()
                        .collection("communityWritings")
                        .whereEqualTo("writeId",writeID)
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .limit(3);

                FirestoreRecyclerOptions<CommunityDoc> options = new FirestoreRecyclerOptions.Builder<CommunityDoc>()
                        .setQuery(query, CommunityDoc.class)
                        .build();

                adapter_user = new FirestoreRecyclerAdapter<CommunityDoc, UserInfHolderCommunity>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserInfHolderCommunity holder, int position, @NonNull CommunityDoc model) {
                        holder.bind(model);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("KSM", "Gggg");
                                String title = holder.getmTitle().getText().toString();

                                Log.d("KSM", "eeeee");
                                Intent intent = new Intent(v.getContext(), Post_Inside_Community.class);
                                intent.putExtra("COLLECTION","communityWritings");
                                Log.d("KSM", title);
                                intent.putExtra("DOCUMENT",title);
                                Log.d("KSM", title);
                                startActivity(intent);
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public UserInfHolderCommunity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        Log.d("ksh", "onCreateViewHolder: 들어옴");
                        View view= LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_user_inf,parent,false);
                        return new UserInfHolderCommunity(view);
                    }
                };
                adapter_user.startListening();

                dialogRecentPostsRecyclerView.setLayoutManager(new LinearLayoutManager(Post_Inside_Community.this));
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
