package com.example.jachisignal.Doc;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jachisignal.R;

public class PreviewHolder2 extends RecyclerView.ViewHolder{
    private final TextView mTitle;

    public PreviewHolder2(@NonNull View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.title_home2);
    }

    public void bind(@NonNull GongguDoc2 gongguDoc2) {
        setTitle(gongguDoc2.getContentTitle());
    }
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public TextView getmTitle() {
        return mTitle;
    }
}