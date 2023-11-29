package com.example.jachisignal.Doc;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jachisignal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class GongguDocHolder extends RecyclerView.ViewHolder{
    private final TextView mNickname;
    private final TextView mHeartcount;
    private final TextView mTitle;
    private final TextView mCategory;
    private final TextView mPeopleCount;
    private final ImageView mImg;

    public GongguDocHolder(@NonNull View itemView) {
        super(itemView);
        mNickname = itemView.findViewById(R.id.nickname);
        mHeartcount = itemView.findViewById(R.id.heart_count);
        mTitle = itemView.findViewById(R.id.title);
        mCategory = itemView.findViewById(R.id.category);
        mPeopleCount=itemView.findViewById(R.id.people_count);
        mImg=itemView.findViewById(R.id.img);
    }

    public void bind(@NonNull GongguDoc gongguDoc) {
        setNickname(gongguDoc.getNickname());
        setHeartCount(Integer.toString(gongguDoc.getLikeList().size()) + "개");
        setTitle(gongguDoc.getItemName());
        setmCategory(gongguDoc.getCategory());
        setPeopleCount(gongguDoc.getPeopleCount());
        setImgLink(gongguDoc.getImageLink());

    }
    private void setNickname(@Nullable String nickname){ mNickname.setText(nickname);}
    private void setHeartCount(@Nullable String heartCount){ mHeartcount.setText(heartCount);}
    private void setTitle(@Nullable String title){ mTitle.setText(title);}
    private void setmCategory(@Nullable String category){
        mCategory.setText(category);
    }
    private void setPeopleCount(@Nullable String peopleCount){ mPeopleCount.setText(peopleCount);}
    private void setImgLink(@Nullable String imgLink){if (imgLink!=null)downloadImageTo(imgLink);}
    private void downloadImageTo(String uri) {
        // Get a default Storage bucket
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a reference to a file from a Google Cloud Storage URI
        Log.d("KYR", "kk"+uri);
        StorageReference gsReference = storage.getReferenceFromUrl(uri); // from gs://~~~
        gsReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete( Task<Uri> task) {
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
}
