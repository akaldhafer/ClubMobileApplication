package com.example.myclub.clubAPI;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.myclub.Encryption.SymmtCrypto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ClubReceiveData implements ClubFetchDataPresenter{
    private final ClubViewFetchDataMessage clubViewFetchDataMessage;
    private final Context context;

    public ClubReceiveData(Context context, ClubViewFetchDataMessage clubViewFetchDataMessage){
        this.clubViewFetchDataMessage=clubViewFetchDataMessage;
        this.context = context;
    }
    @Override
    public void onSuccessUpdate(Activity activity) {
        FirebaseFirestore.getInstance().collection("ClubData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(int i = 0; i<task.getResult().size(); i++){
                        //fetch data
                        String clubID = task.getResult().getDocuments().get(i).getString("clubID");
                        String clubName = task.getResult().getDocuments().get(i).getString("clubName");
                        String clubDescription = task.getResult().getDocuments().get(i).getString("clubDescription");
                        String clubAdvisor = task.getResult().getDocuments().get(i).getString("clubAdvisor");
                        String clubType = task.getResult().getDocuments().get(i).getString("clubType");
                        String token = task.getResult().getDocuments().get(i).getString("token");
                        ArrayList<String> clubMemberList =(ArrayList<String>) task.getResult().getDocuments().get(i).get("clubMemberList");
                        ArrayList<String> clubPostList =(ArrayList<String>) task.getResult().getDocuments().get(i).get("clubPostList");
                        System.out.println("fetch: "+clubDescription+clubAdvisor+clubName);
                        //assign data and decrypt them
                        ClubModule clubModule = new ClubModule();

                        clubModule.setClubID(clubID);
                        clubModule.setClubName(clubName);
                        clubModule.setClubDescription(clubDescription);
                        clubModule.setClubAdvisor(clubAdvisor);
                        clubModule.setClubType(clubType);
                        clubModule.setToken(token);
                        clubModule.setClubMemberList(clubMemberList);
                        clubModule.setClubPostList(clubPostList);
                        clubViewFetchDataMessage.onUpdateSuccess(clubModule);

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                clubViewFetchDataMessage.onUpdateFailure(e.getMessage());
            }
        });
    }
}
