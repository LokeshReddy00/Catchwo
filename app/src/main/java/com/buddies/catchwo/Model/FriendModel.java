package com.buddies.catchwo.Model;

public class FriendModel {

    private String id,uidto,image,name,uidfrom,senttime,acctime,task;

    public FriendModel(String id, String uidto, String image, String name, String uidfrom, String senttime, String acctime, String task) {
        this.id = id;
        this.uidto = uidto;
        this.image = image;
        this.name = name;
        this.uidfrom = uidfrom;
        this.senttime = senttime;
        this.acctime = acctime;
        this.task = task;
    }

    public FriendModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUidto() {
        return uidto;
    }

    public void setUidto(String uidto) {
        this.uidto = uidto;
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

    public String getUidfrom() {
        return uidfrom;
    }

    public void setUidfrom(String uidfrom) {
        this.uidfrom = uidfrom;
    }

    public String getSenttime() {
        return senttime;
    }

    public void setSenttime(String senttime) {
        this.senttime = senttime;
    }

    public String getAcctime() {
        return acctime;
    }

    public void setAcctime(String acctime) {
        this.acctime = acctime;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
