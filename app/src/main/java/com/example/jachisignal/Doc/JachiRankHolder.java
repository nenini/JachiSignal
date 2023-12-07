package com.example.jachisignal.Doc;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class JachiRankHolder extends RecyclerView.ViewHolder {
    private final TextView mTitle;
    private final ImageView mImg;

    public JachiRankHolder(@NonNull View itemView) {
        super(itemView);
        mTitle=itemView.findViewById(R.id.title);
        mImg=itemView.findViewById(R.id.img);
    }

    public void bind(@NonNull JachiDoc jachiDoc){
        setTitle(jachiDoc.getContentTitle());
        setImgLink(jachiDoc.getImageLink());
    }


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
    private void setTitle(@Nullable String title){ mTitle.setText(title);}
    public TextView getmTitle() {
        return mTitle;
    }

    private boolean hasSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null ? true : false;
    }

    private String getUidOfCurrentUser() {
        return hasSignedIn() ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
    }
}
