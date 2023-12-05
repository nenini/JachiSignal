package com.example.jachisignal.Doc;

import java.util.List;

public class MyPageWriteDoc {
    private String contentTitle;
    private String text;
    private List<String> likeList;
    private List<String> scrapList;
    private String writeId;
    private String category;

    public MyPageWriteDoc(String contentTitle, String text, String category,  List<String> likeList,List<String> scrapList) {
        this.likeList = likeList;
        this.text = text;
        this.writeId = writeId;
        this.category = category;
        this.contentTitle = contentTitle;
        this.scrapList=scrapList;
    }
    public MyPageWriteDoc(){}



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
    public List<String> getScrapList() {
        return scrapList;
    }

    public String getText() {
        return text;
    }

    public void setLikeList(List<String> likeList) {
        this.likeList = likeList;
    }
    public void setScrapList(List<String> scrapList) {
        this.scrapList = scrapList;
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
