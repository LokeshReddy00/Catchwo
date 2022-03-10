package com.buddies.catchwo.Model;

public class ReelsModel {

    String time,desc,uid,post,id;

    public ReelsModel(String time, String desc, String uid, String post, String id) {
        this.time = time;
        this.desc = desc;
        this.uid = uid;
        this.post = post;
        this.id = id;
    }

    public ReelsModel() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
