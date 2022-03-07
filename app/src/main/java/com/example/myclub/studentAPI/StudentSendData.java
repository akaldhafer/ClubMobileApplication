package com.example.myclub.studentAPI;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

public class StudentSendData implements StudentDataPresenter, StudentViewMessage {
    private final StudentViewMessage studentViewMessage;
    public StudentSendData(StudentViewMessage studentViewMessage){
        this.studentViewMessage = studentViewMessage;
    }
    @Override
    public void onSuccessUpdate(Activity activity, String studentID, String studentName,
                                String studentEmail, String studentPassword, String isAdvisor, String token, ArrayList<String> clubList) {
        StudentModule studentModule = new StudentModule(studentID,  studentName,  studentEmail,  studentPassword,  isAdvisor,  token, clubList);

        FirebaseFirestore.getInstance().collection("StudentData").document(token).set(studentModule, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            studentViewMessage.onUpdateSuccess("Registered successfully");
                        }
                        else{
                            studentViewMessage.onUpdateFailure("Check internet connection !");
                        }
                    }
                });
    }

    @Override
    public void onUpdateFailure(String message) {

        studentViewMessage.onUpdateFailure(message);
    }

    @Override
    public void onUpdateSuccess(String message) {
        studentViewMessage.onUpdateSuccess(message);

    }
}
