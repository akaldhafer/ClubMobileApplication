package com.example.myclub.clubAPI;

import java.util.ArrayList;

public class ClubModule {
    private String clubID, clubName, clubDescription, clubAdvisor,clubType, token;
    private ArrayList<String> clubMemberList, clubPostList;

    public ClubModule(String clubID, String clubName, String clubDescription, String clubAdvisor, String clubType, String token,
                      ArrayList<String> clubMemberList, ArrayList<String> clubPostList) {
        this.clubID = clubID;
        this.clubName = clubName;
        this.clubDescription = clubDescription;
        this.clubAdvisor = clubAdvisor;
        this.clubType = clubType;
        this.token = token;
        this.clubMemberList = clubMemberList;
        this.clubPostList = clubPostList;
    }

    public ClubModule() {
    }

    public String getClubID() {
        return clubID;
    }

    public void setClubID(String clubID) {
        this.clubID = clubID;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public void setClubDescription(String clubDescription) {
        this.clubDescription = clubDescription;
    }

    public String getClubAdvisor() {
        return clubAdvisor;
    }

    public void setClubAdvisor(String clubAdvisor) {
        this.clubAdvisor = clubAdvisor;
    }

    public String getClubType() {
        return clubType;
    }

    public void setClubType(String clubType) {
        this.clubType = clubType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<String> getClubMemberList() {
        return clubMemberList;
    }

    public void setClubMemberList(ArrayList<String> clubMemberList) {
        this.clubMemberList = clubMemberList;
    }

    public ArrayList<String> getClubPostList() {
        return clubPostList;
    }

    public void setClubPostList(ArrayList<String> clubPostList) {
        this.clubPostList = clubPostList;
    }
}
