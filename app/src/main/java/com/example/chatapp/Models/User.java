package com.example.chatapp.Models;

public class User {
    private String email;
    private String id;
    private String imageurl;
    private String name;

    public User(String email, String id, String imageurl, String name) {
        this.email = email;
        this.id = id;
        this.imageurl = imageurl;
        this.name = name;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
