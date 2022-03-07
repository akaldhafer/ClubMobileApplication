package com.example.myclub.classroomAPI;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class ClassroomSendData implements ClassroomViewMessage, ClassroomDataPresenter{
     ClassroomViewMessage classroomViewMessage;

    public ClassroomSendData(ClassroomViewMessage classroomViewMessage){
        this.classroomViewMessage =classroomViewMessage;
    }
    @Override
    public void onSuccessUpdate(Activity activity, String classID, String classLocation, String isAvailable, String bookedByStudentEmail,
                                String bookedForClubName, String bookedTime, String bookedDate, String token) {
        ClassroomModule classroomModule = new ClassroomModule( classID,  classLocation,  isAvailable,  bookedByStudentEmail,
                 bookedForClubName,  bookedTime,  bookedDate,  token);

        FirebaseFirestore.getInstance().collection("ClassroomData").document(token).set(classroomModule, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            classroomViewMessage.onUpdateSuccess("Classroom Booked successfully!");
                        }
                        else {
                            classroomViewMessage.onUpdateFailure("Something went wrong !");
                        }
                    }
                });
    }

    @Override
    public void onUpdateFailure(String message) {

        classroomViewMessage.onUpdateFailure(message);
    }

    @Override
    public void onUpdateSuccess(String message) {

        classroomViewMessage.onUpdateSuccess(message);
    }
}
