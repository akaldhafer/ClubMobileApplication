package com.example.myclub.studentAPI;

import android.app.Activity;

import java.util.ArrayList;

public interface StudentDataPresenter {

    void onSuccessUpdate(Activity activity, String studentID, String studentName, String studentEmail,
                         String studentPassword, String isAdvisor, String token, ArrayList<String> clubList);
}
