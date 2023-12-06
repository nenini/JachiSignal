package com.example.jachisignal.Doc;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jachisignal.R;
import com.google.firebase.Timestamp;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;

public class NestedChatHolder extends RecyclerView.ViewHolder{
    private final TextView mText;
    private final TextView mNickname;
    private final TextView mTimestamp;

    public NestedChatHolder(@NonNull View itemView){
        super(itemView);
        mNickname = itemView.findViewById(R.id.nickname_nested_chat);
        mText = itemView.findViewById(R.id.text_nested_chat);
        mTimestamp = itemView.findViewById(R.id.timestamp_nested_chat);
    }
    public void bind(@NonNull NestedChat chat) {
        setNickname(chat.getNickname());
        setText(chat.getText());
        setTimeStamp(chat.getTimestamp());
    }

    public void setText(@Nullable String text) {
        mText.setText(text);
    }
    public void setNickname(@Nullable String nickname) {
        mNickname.setText(nickname);
    }
    private void setTimeStamp(@Nullable Timestamp timeStamp) {
        if (timeStamp != null) {
            Date date = timeStamp.toDate();
            Log.d("ksh", "setTimeStamp: "+date);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String dateString = sdf.format(date);
            mTimestamp.setText(dateString);
        } else {
            mTimestamp.setText("No timestamp available"); // 예외 처리: timestamp가 null인 경우 대체 값 설정
        }
    }

    public TextView getmNickname() {
        return mNickname;
    }

    public TextView getmText() {
        return mText;
    }
}
