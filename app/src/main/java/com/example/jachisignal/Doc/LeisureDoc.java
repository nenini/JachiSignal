package com.example.jachisignal.Doc;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.List;

import kotlinx.coroutines.stream.StreamKt;

public class LeisureDoc {
    private String category;
    private String contentTitle;
    private String nickname;

    private String createAt;
    private String id;
    private String imageLink;
    private String text;
    private List<String> likeList;
    private String writeId;
    private String place;
    private String date;
    private String peopleCount;
    private String chatLink;


    @ServerTimestamp
    private Timestamp timestamp; // server timestamp
    public LeisureDoc(String nickname, String id, String writeId, String contentTitle, String text, String createAt, String category, String imageLink, List<String> likeList, String place, String date, String peopleCount, String chatLink) {
        this.nickname=nickname;
        this.id = id;
        this.likeList = likeList;
        this.text = text;
        this.createAt = createAt;
        this.writeId = writeId;
        this.category = category;
        this.contentTitle = contentTitle;
        this.imageLink = imageLink;
        this.place=place;
        this.date=date;
        this.peopleCount=peopleCount;
        this.chatLink=chatLink;
    }
    public LeisureDoc(){}

    public String getNickname() {
        return nickname;
    }

    public String getWriteId() {
        return writeId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getCreateAt() {
        return createAt;
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

    public String getId() {
        return id;
    }
    public String getPlace() {
        return place;
    }

    public String getDate() {
        return date;
    }
    public String getPeopleCount() {
        return peopleCount;
    }

    public String getChatLink() {
        return chatLink;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setLikeList(List<String> likeList) {
        this.likeList = likeList;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setWriteId(String writeId) {
        this.writeId = writeId;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setPlace(String space) {
        this.place = place;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setPeopleCount(String peopleCount) {
        this.peopleCount = peopleCount;
    }

    public void setChatLink(String chatLink) {
        this.chatLink = chatLink;
    }
}
