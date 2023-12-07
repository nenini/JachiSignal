package com.example.jachisignal.Doc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jachisignal.PostActivity.Post_Inside_09;
import com.example.jachisignal.PostActivity.Post_Inside_09_2;
import com.example.jachisignal.PostActivity.Post_Inside_Community;
import com.example.jachisignal.PostActivity.Post_Inside_Jachitem;
import com.example.jachisignal.PostActivity.Post_Inside_Playing;
import com.example.jachisignal.PostActivity.Post_Inside_Recipe;
import com.example.jachisignal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MyGongguDocAdapter extends RecyclerView.Adapter<MyGongguDocAdapter.MyGongguDocHolder> {

    private ArrayList<MyGongguDoc> listData = new ArrayList<>();
    @NonNull
    @Override
    public MyGongguDocAdapter.MyGongguDocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item09_1, parent, false);
        return new MyGongguDocAdapter.MyGongguDocHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyGongguDocAdapter.MyGongguDocHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(listData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyGongguDoc clickedItem=listData.get(position);
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
                    Intent intent = new Intent(v.getContext(), Post_Inside_Playing.class);
                    intent.putExtra("COLLECTION", "leisureWritings");
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
    public void addItem(MyGongguDoc myGongguDoc) {
        // 외부에서 item을 추가시킬 함수입니다.
        Log.d("KYR","---addItem---");
        listData.add(myGongguDoc);
    }
    class MyGongguDocHolder extends RecyclerView.ViewHolder {
        String uid = getUidOfCurrentUser();
        private final TextView mNickname;
        private final TextView mHeartcount;
        private final TextView mTitle;
        private final TextView mCategory;
        private final TextView mPeopleCount;
        private final ImageView mImg;
        private final ImageView mhaert;
        private final ImageView mStar;


        public MyGongguDocHolder(@NonNull View itemView) {
            super(itemView);
            mNickname = itemView.findViewById(R.id.nickname);
            mHeartcount = itemView.findViewById(R.id.heart_count);
            mTitle = itemView.findViewById(R.id.title);
            mCategory = itemView.findViewById(R.id.category);
            mPeopleCount = itemView.findViewById(R.id.people_count);
            mImg = itemView.findViewById(R.id.img);
            mhaert = itemView.findViewById(R.id.heart);
            mStar = itemView.findViewById(R.id.star);

        }

        public void bind(@NonNull MyGongguDoc myGongguDoc) {
            setNickname(myGongguDoc.getNickname());
            setHeartCount(Integer.toString(myGongguDoc.getLikeList().size()) + "개");
            setTitle(myGongguDoc.getItemName());
            setmCategory(myGongguDoc.getCategory());
            setPeopleCount(myGongguDoc.getJoinList(), myGongguDoc.getPeopleCount());
            setImgLink(myGongguDoc.getImageLink());
            setHeart(myGongguDoc.getLikeList().contains(uid));
            setStar(myGongguDoc.getScrapList().contains(uid));
        }

        private void setNickname(@Nullable String nickname) {
            mNickname.setText(nickname);
        }

        private void setHeartCount(@Nullable String heartCount) {
            mHeartcount.setText(heartCount);
        }

        private void setTitle(@Nullable String title) {
            mTitle.setText(title);
        }

        private void setmCategory(@Nullable String category) {
            mCategory.setText(category);
        }

        private void setPeopleCount(@Nullable List<String> joinList, String peopleCount) {
            mPeopleCount.setText(joinList.size() + "/" + peopleCount);
        }

        private void setImgLink(@Nullable String imgLink) {
            if (imgLink != null) downloadImageTo(imgLink);
        }

        private void setHeart(@Nullable boolean heart_TF) {
            if (heart_TF) {
                mhaert.setImageResource(R.drawable.heartcount);
            } else mhaert.setImageResource(R.drawable.heart);
        }

        private void setStar(@Nullable boolean star_TF) {
            if (star_TF) {
                mStar.setImageResource(R.drawable.star);
            } else mStar.setImageResource(R.drawable.star_blank);
        }

        private void downloadImageTo(String uri) {
            // Get a default Storage bucket
            FirebaseStorage storage = FirebaseStorage.getInstance();

            // Create a reference to a file from a Google Cloud Storage URI
            Log.d("KYR", "kk" + uri);
            StorageReference gsReference = storage.getReferenceFromUrl(uri); // from gs://~~~
            gsReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(Task<Uri> task) {
                    if (task.isSuccessful()) {
                        // URL 가져오기 성공
                        Uri downloadUri = task.getResult();
                        Log.d("KYR", downloadUri.toString());

                        // Glide를 사용하여 이미지 로드
                        Glide.with(itemView.getContext()).load(downloadUri).into(mImg);

                        // 이미지 로딩이 완료되면 다른 작업을 수행하고자 한다면 여기에 추가
                    } else {
                        // URL 가져오기 실패
                        Exception exception = task.getException();
                        Log.e("KYR", "Download URL retrieval failed: " + exception.getMessage());
                    }

                    // 이 메서드가 완료되면 다른 작업을 수행하고자 한다면 여기에 추가
                    Log.d("KYR", "지나감");
                }
            });
        }

        private boolean hasSignedIn() {
            return FirebaseAuth.getInstance().getCurrentUser() != null ? true : false;
        }

        private String getUidOfCurrentUser() {
            return hasSignedIn() ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        }


        public TextView getmCategory() {
            return mCategory;
        }

        public TextView getmNickname() {
            return mNickname;
        }

        public ImageView getmImg() {
            return mImg;
        }

        public TextView getmHeartcount() {
            return mHeartcount;
        }

        public TextView getmPeopleCount() {
            return mPeopleCount;
        }

        public TextView getmTitle() {
            return mTitle;
        }
    }
}
