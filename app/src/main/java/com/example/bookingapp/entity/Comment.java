package com.example.bookingapp.entity;

import java.io.Serializable;
import java.sql.Date;

public class Comment implements Serializable {
    private int id;
    private String content;
    private String userName;
    private String userAvatar;
    private String userNationality;

    public Comment(String content, String userName, String userAvatar, String userNationality) {
        this.content = content;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.userNationality = userNationality;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNationality() {
        return userNationality;
    }

    public void setUserNationality(String userNationality) {
        this.userNationality = userNationality;
    }
}
