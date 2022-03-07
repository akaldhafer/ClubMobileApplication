package com.example.myclub.classroomAPI;

public class ClassroomModule {
    private String classID, classLocation, isAvailable, bookedByStudentEmail, bookedForClubName,bookedTime, bookedDate, token;

    public ClassroomModule(String classID, String classLocation, String isAvailable, String bookedByStudentEmail,
                           String bookedForClubName, String bookedTime, String bookedDate, String token) {
        this.classID = classID;
        this.classLocation = classLocation;
        this.isAvailable = isAvailable;
        this.bookedByStudentEmail = bookedByStudentEmail;
        this.bookedForClubName = bookedForClubName;
        this.bookedTime = bookedTime;
        this.bookedDate = bookedDate;
        this.token = token;
    }

    public ClassroomModule() {

    }


    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getClassLocation() {
        return classLocation;
    }

    public void setClassLocation(String classLocation) {
        this.classLocation = classLocation;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getBookedByStudentEmail() {
        return bookedByStudentEmail;
    }

    public void setBookedByStudentEmail(String bookedByStudentEmail) {
        this.bookedByStudentEmail = bookedByStudentEmail;
    }

    public String getBookedForClubName() {
        return bookedForClubName;
    }

    public void setBookedForClubName(String bookedForClubName) {
        this.bookedForClubName = bookedForClubName;
    }

    public String getBookedTime() {
        return bookedTime;
    }

    public void setBookedTime(String bookedTime) {
        this.bookedTime = bookedTime;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(String bookedDate) {
        this.bookedDate = bookedDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
