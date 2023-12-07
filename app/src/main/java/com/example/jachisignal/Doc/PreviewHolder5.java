package com.example.jachisignal.Doc;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jachisignal.R;

public class PreviewHolder5 extends RecyclerView.ViewHolder{
    private final TextView mTitle;

    public PreviewHolder5(@NonNull View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.title_community2);
    }

    public void bind(@NonNull JachiDoc jachiDoc) {
        setTitle(jachiDoc.getContentTitle());
    }
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public TextView getmTitle() {
        return mTitle;
    }
}