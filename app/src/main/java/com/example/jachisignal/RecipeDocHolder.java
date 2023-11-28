package com.example.jachisignal;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

public class RecipeDocHolder extends RecyclerView.ViewHolder {
    private final TextView mNickname;
    private final TextView mHeartcount;
    private final TextView mTitle;
    public RecipeDocHolder(@NonNull View itemView) {
        super(itemView);
        mNickname=itemView.findViewById(R.id.nickname);
        mHeartcount=itemView.findViewById(R.id.heart_count);
        mTitle=itemView.findViewById(R.id.title);

    }

    public  void bind(@NonNull RecipeDoc recipeDoc){
        setNickname(recipeDoc.getNickname());
        setHeartCount(Integer.toString(recipeDoc.getLikeList().size())+"개");
        setTitle(recipeDoc.getContentTitle());
    }
    private void setNickname(@Nullable String nickname){ mNickname.setText(nickname);}
    private void setHeartCount(@Nullable String heartCount){ mHeartcount.setText(heartCount);}
    private void setTitle(@Nullable String title){ mTitle.setText(title);}


}
