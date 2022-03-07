package com.example.myclub.models;

public class UserEmailModel
{

    private String userRandomKey;
    private String userID;
    private String userImage;
    private String userEmail;
    private String userName;

    public UserEmailModel()
    {
    }

    public UserEmailModel(String userRandomKey, String userID, String userImage, String userEmail, String userName)
    {
        this.userRandomKey = userRandomKey;
        this.userID = userID;
        this.userImage = userImage;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public String getUserRandomKey() {
        return userRandomKey;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

}
