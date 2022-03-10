package com.buddies.catchwo.Model;

public class CommentModel {


    String id,uid,comment,Cid,time;

    public CommentModel(String id, String uid, String comment, String cid, String time) {
        this.id = id;
        this.uid = uid;
        this.comment = comment;
        Cid = cid;
        this.time = time;
    }

    public CommentModel() {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
