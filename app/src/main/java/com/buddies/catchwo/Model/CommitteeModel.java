package com.buddies.catchwo.Model;

public class CommitteeModel {

    private String id,uid,createdBy,time,email,comm_name,desc_comm,phone,
            comId,type,image,about;

    public CommitteeModel(String id, String uid, String createdBy, String time, String email, String comm_name, String desc_comm, String phone, String comId, String type, String image, String about) {
        this.id = id;
        this.uid = uid;
        this.createdBy = createdBy;
        this.time = time;
        this.email = email;
        this.comm_name = comm_name;
        this.desc_comm = desc_comm;
        this.phone = phone;
        this.comId = comId;
        this.type = type;
        this.image = image;
        this.about = about;
    }

    public CommitteeModel() {
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public String getComm_name() {
        return comm_name;
    }

    public void setComm_name(String comm_name) {
        this.comm_name = comm_name;
    }

    public String getDesc_comm() {
        return desc_comm;
    }

    public void setDesc_comm(String desc_comm) {
        this.desc_comm = desc_comm;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getcomId() {
        return comId;
    }

    public void setcomId(String comId) {
        this.comId = comId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
