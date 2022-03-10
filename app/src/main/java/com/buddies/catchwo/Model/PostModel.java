package com.buddies.catchwo.Model;

public class PostModel {

    String type,desc,uid,post,id,time,cat,name,comt;

    public PostModel(String type, String desc, String uid, String post, String id, String time, String cat, String name, String comt) {
        this.type = type;
        this.desc = desc;
        this.uid = uid;
        this.post = post;
        this.id = id;
        this.time = time;
        this.cat = cat;
        this.name = name;
        this.comt = comt;
    }

    public PostModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComt() {
        return comt;
    }

    public void setComt(String comt) {
        this.comt = comt;
    }
}
