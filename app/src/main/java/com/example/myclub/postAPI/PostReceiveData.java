package com.example.myclub.postAPI;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.myclub.Encryption.SymmtCrypto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class PostReceiveData implements PostFetchDataPresenter {
    private final Context context;
    private final PostViewFetchDataMessage postViewFetchDataMessage;

    public PostReceiveData(Context context, PostViewFetchDataMessage postViewFetchDataMessage){
        this.context =context;
        this.postViewFetchDataMessage =postViewFetchDataMessage;
    }

    @Override
    public void onSuccessUpdate(Activity activity) {
        FirebaseFirestore.getInstance().collection("PostData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(int i=0; i<task.getResult().size();i++){
                        //fetch
                        String postID = task.getResult().getDocuments().get(i).getString("postID");
                        String postTitle = task.getResult().getDocuments().get(i).getString("postTitle");
                        String postDescription = task.getResult().getDocuments().get(i).getString("postDescription");
                        String postImgUrl = task.getResult().getDocuments().get(i).getString("postImgUrl");
                        String clubID = task.getResult().getDocuments().get(i).getString("clubID");
                        String advisorEmail= task.getResult().getDocuments().get(i).getString("advisorEmail");
                        String isApproved = task.getResult().getDocuments().get(i).getString("isApproved");
                        String token = task.getResult().getDocuments().get(i).getString("token");
                        //assign
                        PostModule postModule = new PostModule(postID,postTitle,postDescription,postImgUrl,clubID,advisorEmail,isApproved,token);

                        postViewFetchDataMessage.onUpdateSuccess(postModule);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                postViewFetchDataMessage.onUpdateFailure(e.getMessage());
            }
        });
    }
}
