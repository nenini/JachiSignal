package com.example.jachisignal.Doc;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jachisignal.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MyPageWriteAdapter extends RecyclerView.Adapter<MyPageWriteAdapter.MyPageWriteDocHolder> {
    String uid = getUidOfCurrentUser();

    private ArrayList<MyPageWriteDoc> listData = new ArrayList<>();
    @NonNull
    @Override
    public MyPageWriteAdapter.MyPageWriteDocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mywrite, parent, false);
        return new MyPageWriteAdapter.MyPageWriteDocHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPageWriteAdapter.MyPageWriteDocHolder holder, int position) {
        holder.bind(listData.get(position));
        //여기에서 제목 intent 넘기기

    }

    @Override
    public int getItemCount() {
        Log.d("KYR","getItemCount: "+listData.size());
        return listData.size();
    }
    public void addItem(MyPageWriteDoc myPageWriteDoc) {
        // 외부에서 item을 추가시킬 함수입니다.
        Log.d("KYR","---addItem---");
        listData.add(myPageWriteDoc);
    }
    class MyPageWriteDocHolder extends RecyclerView.ViewHolder{

        private final TextView mText;
        private final TextView mHeartcount;
        private final TextView mTitle;
        private final ImageView mhaert;
        private final ImageView mStar;




        public MyPageWriteDocHolder(@NonNull View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.community_text);
            mHeartcount = itemView.findViewById(R.id.heart_count);
            mTitle = itemView.findViewById(R.id.title);
            mhaert=itemView.findViewById(R.id.heart);
            mStar=itemView.findViewById(R.id.star);

        }

        public void bind(@NonNull MyPageWriteDoc myPageWriteDoc) {
            setText(myPageWriteDoc.getText());
            setHeartCount(Integer.toString(myPageWriteDoc.getLikeList().size()) + "개");
            setTitle(myPageWriteDoc.getContentTitle());
            setHeart(myPageWriteDoc.getLikeList().contains(uid));
            setStar(myPageWriteDoc.getScrapList().contains(uid));
        }
        private void setHeart(@Nullable boolean heart_TF){
            if(heart_TF){
                mhaert.setImageResource(R.drawable.heartcount);
            }
            else mhaert.setImageResource(R.drawable.heart);
        }
        private void setStar(@Nullable boolean star_TF){
            if(star_TF){
                mStar.setImageResource(R.drawable.star);
            }
            else mStar.setImageResource(R.drawable.star_blank);
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
