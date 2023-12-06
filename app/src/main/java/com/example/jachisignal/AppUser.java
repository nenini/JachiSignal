package com.example.jachisignal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppUser {
    private String img;
    private String name;
    private String nickname;
    private String email;
    private String pw;
    private String address;
    private String phone;
    private List<String> scrap;
    private List<String> myWrite;
    private List<String> myGonggu;


    public AppUser(String email, String pw, String name, String phone, String address, String nickname,String img,List<String> scrap,List<String> myWrite,List<String> myGonggu) {
        this.email=email;
        this.name=name;
        this.nickname = nickname;
        this.pw = pw;
        this.phone = phone;
        this.address=address;
        this.img=img;
        this.scrap=scrap;
        this.myWrite=myWrite;
        this.myGonggu=myGonggu;
    }

    public AppUser(){}

    public String getImg() {
        return img;
    }

    public String getPw() {
        return pw;
    }

    public String getPhone() {
        return phone;
    }

    public String getNickname() {
        return nickname;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }
    public List<String> getScrap(){return scrap;}
    public List<String> getMyWrite(){return myWrite;}
    public List<String> getMyGonggu(){return myGonggu;}

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public void setImg(String img) { this.img = img; }
    public void setScrap(List<String> scrap){this.scrap=scrap;}
    public void setMyWrite(List<String> myWrite){this.myWrite=myWrite;}
    public void setMyGonggu(List<String> myGonggu){this.myGonggu=myGonggu;}


}
