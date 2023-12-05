package com.example.jachisignal.Doc;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.List;

public class CommunityDoc {

    private String siName;
    private String guName;
    private String category;
    private String contentTitle;
    private String nickname;
    private String createAt;
    private String id;


    private String imageLink;
    private String text;
    private List<String> likeList;
    private String writeId;
    private boolean isQuestion;
    @ServerTimestamp
    private Timestamp timestamp; // server timestamp

    public CommunityDoc(String nickname, String id,String writeId, String contentTitle, String text, String createAt, String category, String imageLink,  List<String> likeList, boolean isQuestion,String siName,String guName) {
        this.nickname=nickname;
        this.id=id;
        this.likeList = likeList;
        this.text = text;
        this.createAt = createAt;
        this.writeId = writeId;
        this.category = category;
        this.contentTitle = contentTitle;
        this.imageLink = imageLink;
        this.isQuestion = isQuestion;
        this.siName = siName;
        this.guName = guName;

    }
    public CommunityDoc(){}

    public String getSiName() {
        return siName;
    }

    public void setSiName(String siName) {
        this.siName = siName;
    }
    public String getGuName() {
        return guName;
    }

    public void setGuName(String guName) {
        this.guName = guName;
    }

    public boolean getIsQuestion(){
        return isQuestion;
    }
    public void setIsQuestion(boolean isQuestion){
        this.isQuestion = isQuestion;
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

    public List<String> getLikeList() {
        return likeList;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setId(String id) {
        this.id = id;
    }


}
