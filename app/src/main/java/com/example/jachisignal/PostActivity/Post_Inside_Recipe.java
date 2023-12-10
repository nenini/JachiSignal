package com.example.jachisignal.PostActivity;

import static android.app.PendingIntent.getActivity;

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
import com.example.jachisignal.Doc.NestedChat;
import com.example.jachisignal.Doc.NestedChatHolder;
import com.example.jachisignal.Doc.RecipeDoc;
import com.example.jachisignal.Doc.RecipeDocHolder;
import com.example.jachisignal.R;
import com.example.jachisignal.databinding.ActivityPostInsideCommunityBinding;
import com.example.jachisignal.databinding.ActivityPostInsideRecipeBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Post_Inside_Recipe extends AppCompatActivity {
    private static final String TAG = "KSM";
    private Uri uri;

    private RecipeDoc recipeDoc;
    String documentName;
    FirebaseFirestore db;
    ActivityPostInsideRecipeBinding binding;
    String uid = getUidOfCurrentUser();
    AppUser appUser;

    private FirestoreRecyclerAdapter adapter;
    private FirestoreRecyclerAdapter adapterNestedChat;

    private FirestoreRecyclerAdapter adapter_user;

    String Id;
    String writeID;



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

                // 세현
                Id = recipeDoc.getWriteId();
                //세현
                binding.titleRecipePost.setText(recipeDoc.getContentTitle());

                binding.textRecipePost.setText(recipeDoc.getText());

                binding.heartCountRecipePost.setText(Integer.toString(recipeDoc.getLikeList().size()) + "개");

                binding.nicknameRecipePost.setText(recipeDoc.getNickname());
                DocumentReference docRef2 = db.collection("users").document(recipeDoc.getWriteId());
                docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        appUser = documentSnapshot.toObject(AppUser.class);
                        if(appUser.getImg()!=null){downloadImageToUser(appUser.getImg());}
                    }
                });


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

                LinearLayoutManager layoutManager = new LinearLayoutManager(Post_Inside_Recipe.this);
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

        binding.chatRecyclerViewRecipe.setLayoutManager(new LinearLayoutManager(this));
        binding.chatRecyclerViewRecipe.setAdapter(adapter);


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

        binding.nicknameRecipePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();

            }
        });
    }
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String writingNum[];
    String text1="99";
    String text2="99";
    String text3="99";
    private void showCustomDialog() {
        Dialog dialog = new Dialog(Post_Inside_Recipe.this); // 다이얼로그 생성
        dialog.setContentView(R.layout.custom_user); // 사용자 지정 다이얼로그 레이아웃 설정

        ImageView dialogImageView = dialog.findViewById(R.id.user_inf_img);
        TextView dialogUserNickname = dialog.findViewById(R.id.user_inf_nickname);
        ImageView dialogCloseClick = dialog.findViewById(R.id.closeClick_img);
        TextView dialogUserText1 = dialog.findViewById(R.id.text1);
        TextView dialogUserText2 = dialog.findViewById(R.id.text2);
        TextView dialogUserText3 = dialog.findViewById(R.id.text3);


        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(user.getEmail());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                appUser = documentSnapshot.toObject(AppUser.class);
                List<String> list = appUser.getMyWrite();
                int size = list.size();
                Log.d("test",Integer.toString(size));
                int num = size - 3;
                for (int i = size; i > num; i--) {
                    if (i <=0) {
                        break;
                    } else if (i == num+3) {
                        writingNum = list.get(i - 1).split("_");
                        dialogUserText1.setText(writingNum[1]);
                        text1=writingNum[0];
                    } else if (i == num +2) {
                        writingNum = list.get(i - 1).split("_");
                        dialogUserText2.setText(writingNum[1]);
                        text2=writingNum[0];
                    } else if (i == num+1) {
                        writingNum = list.get(i - 1).split("_");
                        dialogUserText3.setText(writingNum[1]);
                        text3=writingNum[0];
                    }
                }
                dialogUserText1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(text1.compareTo("1")==0){
                            Intent intent = new Intent(v.getContext(), Post_Inside_09.class);
                            intent.putExtra("COLLECTION", "gongu1Writings");
                            intent.putExtra("DOCUMENT", dialogUserText1.getText().toString());
                            v.getContext().startActivity(intent);
                        } else if (text1.compareTo("2")==0) {
                            Intent intent = new Intent(v.getContext(), Post_Inside_09_2.class);
                            intent.putExtra("COLLECTION", "gongu2Writings");
                            intent.putExtra("DOCUMENT", dialogUserText1.getText().toString());
                            v.getContext().startActivity(intent);
                        }else if (text1.compareTo("3")==0) {
                            Intent intent = new Intent(v.getContext(), Post_Inside_Playing.class);
                            intent.putExtra("COLLECTION", "leisureWritings");
                            intent.putExtra("DOCUMENT", dialogUserText1.getText().toString());
                            v.getContext().startActivity(intent);
                        }
                        else if (text1.compareTo("4")==0) {
                            Intent intent = new Intent(v.getContext(), Post_Inside_Community.class);
                            intent.putExtra("COLLECTION", "communityWritings");
                            intent.putExtra("DOCUMENT", dialogUserText1.getText().toString());
                            v.getContext().startActivity(intent);
                        }else if (text1.compareTo("5")==0) {
                            Intent intent = new Intent(v.getContext(), Post_Inside_Jachitem.class);
                            intent.putExtra("COLLECTION", "jachitemWritings");
                            intent.putExtra("DOCUMENT", dialogUserText1.getText().toString());
                            v.getContext().startActivity(intent);
                        }else if (text1.compareTo("6")==0) {
                            Intent intent = new Intent(v.getContext(), Post_Inside_Recipe.class);
                            intent.putExtra("COLLECTION", "recipeWritings");
                            intent.putExtra("DOCUMENT", dialogUserText1.getText().toString());
                            v.getContext().startActivity(intent);
                        }
                        else ;

                    }
                });
                dialogUserText2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(text2.compareTo("1")==0){
                            Intent intent = new Intent(v.getContext(), Post_Inside_09.class);
                            intent.putExtra("COLLECTION", "gongu1Writings");
                            intent.putExtra("DOCUMENT", dialogUserText2.getText().toString());
                            v.getContext().startActivity(intent);
                        } else if (text2.compareTo("2")==0) {
                            Intent intent = new Intent(v.getContext(), Post_Inside_09_2.class);
                            intent.putExtra("COLLECTION", "gongu2Writings");
                            intent.putExtra("DOCUMENT", dialogUserText2.getText().toString());
                            v.getContext().startActivity(intent);
                        }else if (text2.compareTo("3")==0) {
                            Intent intent = new Intent(v.getContext(), Post_Inside_Playing.class);
                            intent.putExtra("COLLECTION", "leisureWritings");
                            intent.putExtra("DOCUMENT", dialogUserText2.getText().toString());
                            v.getContext().startActivity(intent);
                        }
                        else if (text2.compareTo("4")==0) {
                            Intent intent = new Intent(v.getContext(), Post_Inside_Community.class);
                            intent.putExtra("COLLECTION", "communityWritings");
                            intent.putExtra("DOCUMENT", dialogUserText2.getText().toString());
                            v.getContext().startActivity(intent);
                        }else if (text2.compareTo("5")==0) {
                            Intent intent = new Intent(v.getContext(), Post_Inside_Jachitem.class);
                            intent.putExtra("COLLECTION", "jachitemWritings");
                            intent.putExtra("DOCUMENT", dialogUserText2.getText().toString());
                            v.getContext().startActivity(intent);
                        }else if (text2.compareTo("6")==0) {
                            Intent intent = new Intent(v.getContext(), Post_Inside_Recipe.class);
                            intent.putExtra("COLLECTION", "recipeWritings");
                            intent.putExtra("DOCUMENT", dialogUserText2.getText().toString());
                            v.getContext().startActivity(intent);
                        }else;
                    }
                });
                dialogUserText3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(text3.compareTo("1")==0){
                            Intent intent = new Intent(v.getContext(), Post_Inside_09.class);
                            intent.putExtra("COLLECTION", "gongu1Writings");
                            intent.putExtra("DOCUMENT", dialogUserText3.getText().toString());
                            v.getContext().startActivity(intent);
                        } else if (text3.compareTo("2")==0) {
                            Intent intent = new Intent(v.getContext(), Post_Inside_09_2.class);
                            intent.putExtra("COLLECTION", "gongu2Writings");
                            intent.putExtra("DOCUMENT", dialogUserText3.getText().toString());
                            v.getContext().startActivity(intent);
                        }else if (text3.compareTo("3")==0) {
                            Intent intent = new Intent(v.getContext(), Post_Inside_Playing.class);
                            intent.putExtra("COLLECTION", "leisureWritings");
                            intent.putExtra("DOCUMENT", dialogUserText3.getText().toString());
                            v.getContext().startActivity(intent);
                        }
                        else if (text3.compareTo("4")==0) {
                            Intent intent = new Intent(v.getContext(), Post_Inside_Community.class);
                            intent.putExtra("COLLECTION", "communityWritings");
                            intent.putExtra("DOCUMENT", dialogUserText3.getText().toString());
                            v.getContext().startActivity(intent);
                        }else if (text3.compareTo("5")==0) {
                            Intent intent = new Intent(v.getContext(), Post_Inside_Jachitem.class);
                            intent.putExtra("COLLECTION", "jachitemWritings");
                            intent.putExtra("DOCUMENT", dialogUserText3.getText().toString());
                            v.getContext().startActivity(intent);
                        }else if (text3.compareTo("6")==0) {
                            Intent intent = new Intent(v.getContext(), Post_Inside_Recipe.class);
                            intent.putExtra("COLLECTION", "recipeWritings");
                            intent.putExtra("DOCUMENT", dialogUserText3.getText().toString());
                            v.getContext().startActivity(intent);
                        } else;
                    }
                });

            }

        });
        dialog.show();


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
    private void downloadImageToUser(String uri) {
        // Get a default Storage bucket
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a reference to a file from a Google Cloud Storage URI
        StorageReference gsReference = storage.getReferenceFromUrl(uri); // from gs://~~~
        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Post_Inside_Recipe.this).load(uri).into(binding.userImgRecipePost);
            }
        });
    }

    //좋아요함수
    private void likeEvent(){
        //눌렸는데 uid가 있었던 경우->uid 삭제
        if(recipeDoc.getLikeList().contains(uid)){
            recipeDoc.getLikeList().remove(uid);
            recipeDoc.setLikeListCount(recipeDoc.getLikeListCount() - 1);
            db.collection("recipeWritings").document(documentName).set(recipeDoc);
        }
        else{//uid 없었음->uid 추가
            recipeDoc.getLikeList().add(uid);
            recipeDoc.setLikeListCount(recipeDoc.getLikeListCount() + 1);
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