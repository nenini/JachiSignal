package com.example.jachisignal.Doc;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class ReportDoc {
    private String category;
    private String writeId;
    private String contentTitle;
    private String text;
    private String imageLink;
    @ServerTimestamp
    private Timestamp timestamp; // server timestamp

    public ReportDoc(String category,String writeId, String contentTitle,String text, String imageLink){
        this.category=category;
        this.contentTitle=contentTitle;
        this.text=text;
        this.writeId=writeId;
        this.imageLink=imageLink;
    }
    public ReportDoc(){}
    public String getCategory() {
        return category;
    }
    public String getImageLink() {
        return imageLink;
    }
    public String getWriteId() {
        return writeId;
    }
    public Timestamp getTimestamp() { return timestamp; }
    public String getContentTitle() {
        return contentTitle;
    }

    public String getText() {
        return text;
    }
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
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
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public void setWriteId(String writeId) {
        this.writeId = writeId;
    }

}
