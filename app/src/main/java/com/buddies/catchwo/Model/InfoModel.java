package com.buddies.catchwo.Model;

public class InfoModel {

    private String id,image,title,description,link;

    public InfoModel(String id, String image, String title, String desc, String link) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.description = desc;
        this.link = link;
    }

    public InfoModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
