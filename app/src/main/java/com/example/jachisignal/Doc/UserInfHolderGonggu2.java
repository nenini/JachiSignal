package com.example.jachisignal.Doc;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jachisignal.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserInfHolderGonggu2 extends RecyclerView.ViewHolder {
    private final TextView mTitle;

    public UserInfHolderGonggu2(@NonNull View itemView) {
        super(itemView);
        mTitle=itemView.findViewById(R.id.title);
    }

    public void bind(@NonNull GongguDoc2 gongguDoc2){
        setTitle(gongguDoc2.getItemName());
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
