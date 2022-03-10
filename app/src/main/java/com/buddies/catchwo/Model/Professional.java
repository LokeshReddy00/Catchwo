package com.buddies.catchwo.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Professional implements Parcelable {

    private String name,username, password,image,professionId,address ,phone,uid;
    private Long rating;

    public Professional(String name, String username, String password, String image, String professionId, String address, String phone, String uid, Long rating) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.image = image;
        this.professionId = professionId;
        this.address = address;
        this.phone = phone;
        this.uid = uid;
        this.rating = rating;
    }

    public Professional() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProfessionId() {
        return professionId;
    }

    public void setProfessionId(String professionId) {
        this.professionId = professionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public static Creator<Professional> getCREATOR() {
        return CREATOR;
    }

    protected Professional(Parcel in) {
        name = in.readString();
        username = in.readString();
        password = in.readString();
        image = in.readString();
        professionId = in.readString();
        address = in.readString();
        phone = in.readString();
        uid = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readLong();
        }
    }

    public static final Creator<Professional> CREATOR = new Creator<Professional>() {
        @Override
        public Professional createFromParcel(Parcel in) {
            return new Professional(in);
        }

        @Override
        public Professional[] newArray(int size) {
            return new Professional[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(image);
        dest.writeString(professionId);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(uid);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(rating);
        }
    }
}
