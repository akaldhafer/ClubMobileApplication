package com.example.myclub.clubAPI;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

public class ClubSendData implements ClubDataPresenter, ClubViewMessage
{
    private final ClubViewMessage clubViewMessage;
    public ClubSendData(ClubViewMessage clubViewMessage){
        this.clubViewMessage=clubViewMessage;
    }


    @Override
    public void onSuccessUpdate(Activity activity, String clubID, String clubName, String clubDescription, String clubAdvisor, String clubType, String token,ArrayList<String> clubMemberList, ArrayList<String> clubPostList) {
        ClubModule clubModule = new ClubModule(clubID,clubName, clubDescription,clubAdvisor, clubType, token, clubMemberList,clubPostList);
        FirebaseFirestore.getInstance().collection("ClubData").document(token).set(clubModule, SetOptions.merge())
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    clubViewMessage.onUpdateSuccess("Club has been created !");
                }else{
                    clubViewMessage.onUpdateFailure("Something went wrong!");
                }
            }
        });

    }


    @Override
    public void onUpdateFailure(String message) {
        clubViewMessage.onUpdateFailure(message);
    }

    @Override
    public void onUpdateSuccess(String message) {
        clubViewMessage.onUpdateSuccess(message);
    }
}
