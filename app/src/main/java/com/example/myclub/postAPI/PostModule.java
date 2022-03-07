package com.example.myclub.postAPI;

public class PostModule {
    private String postID, postTitle, postDescription, postImgUrl, clubID, advisorEmail, isApproved,token;

    public PostModule(String postID, String postTitle, String postDescription, String postImgUrl, String clubID,String advisorEmail,String isApproved, String token) {
        this.postID = postID;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.postImgUrl = postImgUrl;
        this.clubID = clubID;
        this.advisorEmail =advisorEmail;
        this.isApproved =isApproved;
        this.token = token;
    }

    public PostModule() {
    }

    public String getAdvisorEmail() {
        return advisorEmail;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public void setAdvisorEmail(String advisorEmail) {
        this.advisorEmail = advisorEmail;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostImgUrl() {
        return postImgUrl;
    }

    public void setPostImgUrl(String postImgUrl) {
        this.postImgUrl = postImgUrl;
    }

    public String getClubID() {
        return clubID;
    }

    public void setClubID(String clubID) {
        this.clubID = clubID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
