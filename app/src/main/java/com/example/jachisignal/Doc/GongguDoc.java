package com.example.jachisignal.Doc;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.List;

public class GongguDoc {
    private String category;
    private String contentTitle;
    private String nickname;

    private String createAt;
    private String id;
    private String imageLink;
    private String text;
    private List<String> likeList;
    private String writeId;
    private String itemName;
    private String price;
    private String peopleCount;
    private String chatLink;


    @ServerTimestamp
    private Timestamp timestamp; // server timestamp
    public GongguDoc(String nickname, String id, String writeId, String contentTitle, String text, String createAt, String category, String imageLink, List<String> likeList, String itemName, String price, String peopleCount, String chatLink) {
        this.nickname=nickname;
        this.id = id;
        this.likeList = likeList;
        this.text = text;
        this.createAt = createAt;
        this.writeId = writeId;
        this.category = category;
        this.contentTitle = contentTitle;
        this.imageLink = imageLink;
        this.itemName=itemName;
        this.price=price;
        this.peopleCount=peopleCount;
        this.chatLink=chatLink;
    }
    public GongguDoc(){}

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
    public String getItemName() {
        return itemName;
    }

    public String getPrice() {
        return price;
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
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public void setPeopleCount(String peopleCount) {
        this.peopleCount = peopleCount;
    }

    public void setChatLink(String chatLink) {
        this.chatLink = chatLink;
    }
}
