package com.example.myclub.classroomAPI;

import android.app.Activity;

public interface ClassroomDataPresenter {
    void onSuccessUpdate(Activity activity,String classID, String classLocation, String isAvailable, String bookedByStudentEmail,
                         String bookedForClubName, String bookedTime, String bookedDate, String token);
}
