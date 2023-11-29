package com.example.jachisignal.Doc;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jachisignal.R;

public class GongguDocHolder2 extends RecyclerView.ViewHolder{
    private final TextView mNickname;
    private final TextView mHeartcount;
    private final TextView mTitle;
    private final TextView mCategory;
    private final TextView mPeopleCount;

    public GongguDocHolder2(@NonNull View itemView) {
        super(itemView);
        mNickname = itemView.findViewById(R.id.nickname);
        mHeartcount = itemView.findViewById(R.id.heart_count);
        mTitle = itemView.findViewById(R.id.title);
        mCategory = itemView.findViewById(R.id.category);
        mPeopleCount=itemView.findViewById(R.id.people_count);
    }

    public void bind(@NonNull GongguDoc2 gongguDoc2) {
        setNickname(gongguDoc2.getNickname());
        setHeartCount(Integer.toString(gongguDoc2.getLikeList().size()) + "개");
        setTitle(gongguDoc2.getItemName());
        setmCategory(gongguDoc2.getCategory());
        setPeopleCount(gongguDoc2.getPeopleCount());

    }
    private void setNickname(@Nullable String nickname){ mNickname.setText(nickname);}
    private void setHeartCount(@Nullable String heartCount){ mHeartcount.setText(heartCount);}
    private void setTitle(@Nullable String title){ mTitle.setText(title);}
    private void setmCategory(@Nullable String category){
        mCategory.setText(category);
    }
    private void setPeopleCount(@Nullable String peopleCount){ mPeopleCount.setText(peopleCount);}
}

