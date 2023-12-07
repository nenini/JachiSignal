package com.example.jachisignal.Doc;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jachisignal.R;

public class PreviewHolder4 extends RecyclerView.ViewHolder{
    private final TextView mTitle;

    public PreviewHolder4(@NonNull View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.title_community1);
    }

    public void bind(@NonNull CommunityDoc communityDoc) {
        setTitle(communityDoc.getContentTitle());
    }
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public TextView getmTitle() {
        return mTitle;
    }
}