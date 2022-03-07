package com.example.myclub.postAPI;

import android.app.Activity;

public interface PostDataPresenter {
    void onSuccessUpdate(Activity activity,String postID, String postTitle, String postDescription, String postImgUrl, String clubID, String advisorEmail,String isApproved,String token);
}
