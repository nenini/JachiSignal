package com.example.jachisignal.Doc;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jachisignal.R;

public class PreviewHolder3 extends RecyclerView.ViewHolder{
    private final TextView mTitle;

    public PreviewHolder3(@NonNull View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.title_home3);
    }

    public void bind(@NonNull LeisureDoc leisureDoc) {
        setTitle(leisureDoc.getContentTitle());
    }
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public TextView getmTitle() {
        return mTitle;
    }
}