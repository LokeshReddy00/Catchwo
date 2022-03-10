package com.buddies.catchwo.Model;

public class BookChatModel {

    String image,name,token,uid;

    public BookChatModel(String image, String name, String token, String uid) {
        this.image = image;
        this.name = name;
        this.token = token;
        this.uid = uid;
    }

    public BookChatModel() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
