package com.buddies.catchwo.Model;

public class BookModel {


    String image,uid,bookID,name,written,category,description,
            time,book,type;

    public BookModel(String image, String uid, String bookID, String name, String written, String category, String description, String time, String book, String type) {
        this.image = image;
        this.uid = uid;
        this.bookID = bookID;
        this.name = name;
        this.written = written;
        this.category = category;
        this.description = description;
        this.time = time;
        this.book = book;
        this.type = type;
    }

    public BookModel() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWritten() {
        return written;
    }

    public void setWritten(String written) {
        this.written = written;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
