package com.example.jachisignal.Doc;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jachisignal.R;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommunityDocHolder extends RecyclerView.ViewHolder {

    private final TextView mText;
    private final TextView mHeartcount;
    private final TextView mTitle;
    private final TextView mTimeStamp;


    public CommunityDocHolder(@NonNull View itemView) {
        super(itemView);
        mText = itemView.findViewById(R.id.community_text);
        mHeartcount = itemView.findViewById(R.id.heart_count);
        mTitle = itemView.findViewById(R.id.title);
        mTimeStamp = itemView.findViewById(R.id.community_time);
    }

    public void bind(@NonNull CommunityDoc communityDoc) {
        setText(communityDoc.getText());
        setHeartCount(Integer.toString(communityDoc.getLikeList().size()) + "개");
        setTitle(communityDoc.getContentTitle());
        setTimeStamp(communityDoc.getTimestamp());
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

    private void setTimeStamp(@Nullable Timestamp timeStamp) {
        if (timeStamp != null) {
            Date date = timeStamp.toDate();
            Log.d("ksh", "setTimeStamp: "+date);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String dateString = sdf.format(date);
            mTimeStamp.setText(dateString);
        } else {
            mTimeStamp.setText("No timestamp available"); // 예외 처리: timestamp가 null인 경우 대체 값 설정
        }
    }
}
