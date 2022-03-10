package com.buddies.catchwo.Model;

import com.google.gson.annotations.SerializedName;

public class Users {

    @SerializedName("id") private String id;
    @SerializedName("uid") private String uid;
    @SerializedName("name") private String name;
    @SerializedName("time") private String time;
    @SerializedName("email") private String email;
    @SerializedName("dob") private String dob;
    @SerializedName("gender") private String gender;
    @SerializedName("phone") private String phone;
    @SerializedName("image") private String image;
    @SerializedName("cover") private String cover;

    public Users(String id, String uid, String name, String time, String email, String dob, String gender, String phone, String image, String cover) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.time = time;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.image = image;
        this.cover = cover;
    }

    public Users() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
