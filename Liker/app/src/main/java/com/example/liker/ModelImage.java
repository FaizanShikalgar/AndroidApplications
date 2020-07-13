package com.example.liker;

public class ModelImage {
    private String id;
    private String imageurl;

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    private String likes = "0";

    public ModelImage() {
    }

    public ModelImage(String id, String imageurl,String likes) {
        this.id = id;
        this.imageurl = imageurl;
        this.likes = likes;
    }

    public ModelImage(String id, String imageurl) {
        this.id = id;
        this.imageurl = imageurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}