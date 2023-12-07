package com.example.jachisignal.Doc;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.List;

public class MyGongguDoc {

    private ArrayList<String> contentArray;
    private String category;
    private String contentTitle;
    private String nickname;

    private String createAt;
    private String id;
    private String imageLink;
    private String text;
    private List<String> scrapList;
    private List<String> likeList;
    private List<String> joinList;
    private String writeId;
    private String itemName;
    private String price;
    private String peopleCount;
    private String chatLink;
    private String post;



    @ServerTimestamp
    private Timestamp timestamp; // server timestamp
    public MyGongguDoc(String nickname, String id, String writeId, String contentTitle, String text, String createAt, String category, String imageLink, List<String> likeList, List<String> scrapList,List<String> joinList,String itemName, String price, String peopleCount, String chatLink,ArrayList<String> contentArray,String post) {
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
        this.scrapList=scrapList;
        this.joinList=joinList;
        this.contentArray = contentArray;
        this.post=post;
    }
    public MyGongguDoc(){}

    public void setContentArray(ArrayList<String> contentArray) {
        this.contentArray = contentArray;
    }

    public ArrayList<String> getContentArray() {
        return contentArray;
    }

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
    public String getPost() {
        return post;
    }

    public List<String> getLikeList() {
        return likeList;
    }
    public List<String> getScrapList(){return scrapList; }
    public List<String> getJoinList(){return joinList; }


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
    public void setScrapList(List<String> scrapList) {
        this.scrapList = scrapList;
    }
    public void setJoinList(List<String> joinList) {
        this.joinList = joinList;
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
    public void setPost(String post) {
        this.post = post;
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
