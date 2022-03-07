package com.example.myclub.postAPI;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class PostSendData implements PostDataPresenter ,PostViewMessage {
    private final PostViewMessage postViewMessage;
    public PostSendData(PostViewMessage postViewMessage){this.postViewMessage = postViewMessage;}
    @Override
    public void onSuccessUpdate(Activity activity, String postID, String postTitle, String postDescription, String postImgUrl, String clubID,String advisorEmail,String isApproved, String token) {

        PostModule postModule = new PostModule(postID, postTitle, postDescription, postImgUrl, clubID, advisorEmail,isApproved, token);

        FirebaseFirestore.getInstance().collection("PostData").document(token).set(postModule, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            postViewMessage.onUpdateSuccess("Post sent successfully!");
                        }else{
                            postViewMessage.onUpdateFailure("Something went wrong!");
                        }
                    }
                });
    }

    @Override
    public void onUpdateFailure(String message) {
        postViewMessage.onUpdateFailure(message);
    }

    @Override
    public void onUpdateSuccess(String message) {
           postViewMessage.onUpdateSuccess(message);
    }
}
