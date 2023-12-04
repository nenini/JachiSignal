package com.example.jachisignal.Doc;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Chat {
    private String id;
    private String nickname;
    private String text;
    @ServerTimestamp
    private Timestamp timestamp; // server timestamp
    public Chat(String id, String nickname, String text){
        this.id = id;
        this.nickname = nickname;
        this.text = text;
    }
    public Chat(){}

    public String getNickname() {
        return nickname;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
