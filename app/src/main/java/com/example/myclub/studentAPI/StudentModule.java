package com.example.myclub.studentAPI;

import java.util.ArrayList;

public class StudentModule {
    //create the student data
    private String studentID, studentName, studentEmail, studentPassword ,isAdvisor, token;
    private ArrayList<String> clubList;

    public ArrayList<String> getClubList() {
        return clubList;
    }

    public void setClubList(ArrayList<String> clubList) {
        this.clubList = clubList;
    }

    public StudentModule(String studentID, String studentName, String studentEmail, String studentPassword, String isAdvisor, String token, ArrayList<String> clubList) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentPassword = studentPassword;
        this.isAdvisor = isAdvisor;
        this.token = token;
        this.clubList = clubList;
    }

    public StudentModule() {
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    public void setStudentPassword(String studentPassword) {
        this.studentPassword = studentPassword;
    }



    public String getIsAdvisor() {
        return isAdvisor;
    }

    public void setIsAdvisor(String isAdvisor) {
        this.isAdvisor = isAdvisor;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
