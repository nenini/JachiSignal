package com.example.jachisignal.Doc;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.List;

public class MyPageScrapDoc {
    private String contentTitle;
    private String text;
    private List<String> likeList;
    private String writeId;
    private String category;

    public MyPageScrapDoc(String contentTitle, String text, String category,  List<String> likeList) {
        this.likeList = likeList;
        this.text = text;
        this.writeId = writeId;
        this.category = category;
        this.contentTitle = contentTitle;
    }
    public MyPageScrapDoc(){}



    public String getWriteId() {
        return writeId;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getLikeList() {
        return likeList;
    }

    public String getText() {
        return text;
    }

    public void setLikeList(List<String> likeList) {
        this.likeList = likeList;
    }


    public void setWriteId(String writeId) {
        this.writeId = writeId;
    }


    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setText(String text) {
        this.text = text;
    }



}
