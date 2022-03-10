package com.buddies.catchwo;

public class NotificationModel {

    String title,uid,id,type,time;

    public NotificationModel(String title, String uid, String id, String type, String time) {
        this.title = title;
        this.uid = uid;
        this.id = id;
        this.type = type;
        this.time = time;
    }

    public NotificationModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
