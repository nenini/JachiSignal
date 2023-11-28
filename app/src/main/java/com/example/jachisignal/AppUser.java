package com.example.jachisignal;

public class AppUser {
    private String img;
    private String name;
    private String nickname;
    private String email;
    private String pw;
    private String address;
    private String phone;

    public AppUser(String email, String pw, String name, String phone, String address, String nickname) {
        this.email=email;
        this.name=name;
        this.nickname = nickname;
        this.pw = pw;
        this.phone = phone;
        this.address=address;
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

    public void setImg(String img) {
        this.img = img;
    }
}
