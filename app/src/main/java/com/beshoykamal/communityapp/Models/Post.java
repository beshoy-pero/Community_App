package com.beshoykamal.communityapp.Models;

import com.google.firebase.database.ServerValue;

public class Post {

    private String postkey;
    private String Titele;
    private String Discreption;
    private String UserId;
    private String Picture;
    private String UserPicture;
    private Object TimeStamp;


    public Post(String titele, String discreption, String picture, String userId, String userPicture) {
        Titele = titele;
        Discreption = discreption;
        Picture = picture;
        UserId = userId;
        UserPicture = userPicture;
        TimeStamp = ServerValue.TIMESTAMP;

    }

    public Post() {
    }

    public String getPostkey() {
        return postkey;
    }

    public void setPostkey(String postkey) {
        this.postkey = postkey;
    }

    public String getTitele() {
        return Titele;
    }

    public void setTitele(String titele) {
        Titele = titele;
    }

    public String getDiscreption() {
        return Discreption;
    }

    public void setDiscreption(String discreption) {
        Discreption = discreption;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getUserPicture() {
        return UserPicture;
    }

    public void setUserPicture(String userPicture) {
        UserPicture = userPicture;
    }

    public Object getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        TimeStamp = timeStamp;
    }
}
