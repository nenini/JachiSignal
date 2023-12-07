package com.example.jachisignal.Doc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jachisignal.PostActivity.Post_Inside_09;
import com.example.jachisignal.PostActivity.Post_Inside_09_2;
import com.example.jachisignal.PostActivity.Post_Inside_Community;
import com.example.jachisignal.PostActivity.Post_Inside_Jachitem;
import com.example.jachisignal.PostActivity.Post_Inside_Playing;
import com.example.jachisignal.PostActivity.Post_Inside_Recipe;
import com.example.jachisignal.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MyPageScrapAdapter extends RecyclerView.Adapter<MyPageScrapAdapter.MyPageScrapDocHolder> {
    String uid = getUidOfCurrentUser();

    private ArrayList<MyPageScrapDoc> listData = new ArrayList<>();
    @NonNull
    @Override
    public MyPageScrapDocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scrap, parent, false);
        return new MyPageScrapDocHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPageScrapDocHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(listData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPageScrapDoc clickedItem=listData.get(position);
                String contentTitle=clickedItem.getContentTitle();
                String post=clickedItem.getPost();
                if(post.compareTo("gongu1Writings")==0){
                    Intent intent = new Intent(v.getContext(), Post_Inside_09.class);
                    intent.putExtra("COLLECTION", "gongu1Writings");
                    intent.putExtra("DOCUMENT", contentTitle);
                    v.getContext().startActivity(intent);
                } else if (post.compareTo("gongu2Writings")==0) {
                    Intent intent = new Intent(v.getContext(), Post_Inside_09_2.class);
                    intent.putExtra("COLLECTION", "gongu2Writings");
                    intent.putExtra("DOCUMENT", contentTitle);
                    v.getContext().startActivity(intent);
                }else if (post.compareTo("leisureWritings")==0) {
                    Intent intent = new Intent(v.getContext(),Post_Inside_Playing.class);
                    intent.putExtra("COLLECTION", "leisureWritings");
                    intent.putExtra("DOCUMENT", contentTitle);
                    v.getContext().startActivity(intent);
                }
                else if (post.compareTo("communityWritings")==0) {
                    Intent intent = new Intent(v.getContext(), Post_Inside_Community.class);
                    intent.putExtra("COLLECTION", "communityWritings");
                    intent.putExtra("DOCUMENT", contentTitle);
                    v.getContext().startActivity(intent);
                }else if (post.compareTo("jachitemWritings")==0) {
                    Intent intent = new Intent(v.getContext(), Post_Inside_Jachitem.class);
                    intent.putExtra("COLLECTION", "jachitemWritings");
                    intent.putExtra("DOCUMENT", contentTitle);
                    v.getContext().startActivity(intent);
                }else if (post.compareTo("recipeWritings")==0) {
                    Intent intent = new Intent(v.getContext(), Post_Inside_Recipe.class);
                    intent.putExtra("COLLECTION", "recipeWritings");
                    intent.putExtra("DOCUMENT", contentTitle);
                    v.getContext().startActivity(intent);
                }

            }
        });
        //여기에서 제목 intent 넘기기

    }

    @Override
    public int getItemCount() {
        Log.d("KYR","getItemCount: "+listData.size());
        return listData.size();
    }
    public void addItem(MyPageScrapDoc myPageScrapDoc) {
        // 외부에서 item을 추가시킬 함수입니다.
        Log.d("KYR","---addItem---");
        listData.add(myPageScrapDoc);
    }
    class MyPageScrapDocHolder extends RecyclerView.ViewHolder{

        private final TextView mText;
        private final TextView mHeartcount;
        private final TextView mTitle;
        private final ImageView mhaert;




        public MyPageScrapDocHolder(@NonNull View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.community_text);
            mHeartcount = itemView.findViewById(R.id.heart_count);
            mTitle = itemView.findViewById(R.id.title);
            mhaert=itemView.findViewById(R.id.heart);
        }

        public void bind(@NonNull MyPageScrapDoc myPageScrapDoc) {
            setText(myPageScrapDoc.getText());
            setHeartCount(Integer.toString(myPageScrapDoc.getLikeList().size()) + "개");
            setTitle(myPageScrapDoc.getContentTitle());
            setHeart(myPageScrapDoc.getLikeList().contains(uid));
        }
        private void setHeart(@Nullable boolean heart_TF){
            if(heart_TF){
                mhaert.setImageResource(R.drawable.heartcount);
            }
            else mhaert.setImageResource(R.drawable.heart);
        }

        private void setText(@Nullable String text) {
            mText.setText(text);
        }

        private void setHeartCount(@Nullable String heartCount) {
            mHeartcount.setText(heartCount);
        }

        private void setTitle(@Nullable String title) {
            mTitle.setText(title);
        }


        public TextView getmHeartcount() {
            return mHeartcount;
        }

        public TextView getmTitle() {
            return mTitle;
        }

        public TextView getmText() {
            return mText;
        }

    }
    private boolean hasSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null ? true : false;
    }

    private String getUidOfCurrentUser() {
        return hasSignedIn() ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
    }


}
