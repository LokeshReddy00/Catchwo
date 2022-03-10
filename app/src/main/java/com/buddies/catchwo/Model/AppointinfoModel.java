package com.buddies.catchwo.Model;

public class AppointinfoModel {

    private String id,uid,customername,customerphone,done,personname,peronaddress,profid,
            profname,query,slot,time,timestamp,place,Cuid,Date;

    public AppointinfoModel(String id, String uid, String customername, String customerphone, String done, String personname, String peronaddress, String profid, String profname, String query, String slot, String time, String timestamp, String place, String cuid, String date) {
        this.id = id;
        this.uid = uid;
        this.customername = customername;
        this.customerphone = customerphone;
        this.done = done;
        this.personname = personname;
        this.peronaddress = peronaddress;
        this.profid = profid;
        this.profname = profname;
        this.query = query;
        this.slot = slot;
        this.time = time;
        this.timestamp = timestamp;
        this.place = place;
        Cuid = cuid;
        Date = date;
    }

    public AppointinfoModel() {
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

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getCustomerphone() {
        return customerphone;
    }

    public void setCustomerphone(String customerphone) {
        this.customerphone = customerphone;
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public String getPeronaddress() {
        return peronaddress;
    }

    public void setPeronaddress(String peronaddress) {
        this.peronaddress = peronaddress;
    }

    public String getProfid() {
        return profid;
    }

    public void setProfid(String profid) {
        this.profid = profid;
    }

    public String getProfname() {
        return profname;
    }

    public void setProfname(String profname) {
        this.profname = profname;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCuid() {
        return Cuid;
    }

    public void setCuid(String cuid) {
        Cuid = cuid;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
