package com.example.myclub.classroomAPI;

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

public class ClassroomReceiveData implements  ClassroomFetchDataPresenter{
    private final Context context;
    private final ClassroomViewFetchMessage classroomViewFetchMessage;

    public ClassroomReceiveData(Context context, ClassroomViewFetchMessage classroomViewFetchMessage){
        this.classroomViewFetchMessage = classroomViewFetchMessage;
        this.context = context;

    }

    @Override
    public void onSuccessUpdate(Activity activity) {
        FirebaseFirestore.getInstance().collection("ClassroomData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(int i =0; i<task.getResult().size();i++){
                        //fetch data
                        String classID = task.getResult().getDocuments().get(i).getString("classID");
                        String classLocation = task.getResult().getDocuments().get(i).getString("classLocation");
                        String isAvailable = task.getResult().getDocuments().get(i).getString("isAvailable");
                        String bookedByStudentEmail = task.getResult().getDocuments().get(i).getString("bookedByStudentEmail");
                        String bookedForClubName = task.getResult().getDocuments().get(i).getString("bookedForClubName");
                        String bookedTime = task.getResult().getDocuments().get(i).getString("bookedTime");
                        String bookedDate = task.getResult().getDocuments().get(i).getString("bookedDate");
                        String token = task.getResult().getDocuments().get(i).getString("token");

                        ClassroomModule classroomModule = new ClassroomModule(classID, classLocation,isAvailable,bookedByStudentEmail,bookedForClubName,bookedTime,bookedDate,token);

                        classroomViewFetchMessage.onUpdateSuccess(classroomModule);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                classroomViewFetchMessage.onUpdateFailure(e.getMessage());
            }
        });

    }
}
