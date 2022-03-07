package com.example.myclub.clubAPI;

import android.app.Activity;

import java.util.ArrayList;

public interface ClubDataPresenter {
    void onSuccessUpdate(Activity activity, String clubID, String clubName, String clubDescription, String clubAdvisor, String clubType, String token,ArrayList<String> clubMemberList, ArrayList<String> clubPostList);
}
