package com.example.myclub.studentAPI;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.myclub.Encryption.SymmtCrypto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StudentReceiveData implements StudentFetchDataPresenter{
    private final Context context;
    private final StudentViewFetchDataMessage studentViewFetchDataMessage;

    public StudentReceiveData(Context context, StudentViewFetchDataMessage studentViewFetchDataMessage){
        this.context = context;
        this.studentViewFetchDataMessage =studentViewFetchDataMessage;
    }
    @Override
    public void onSuccessUpdate(Activity activity) {
        FirebaseFirestore.getInstance().collection("StudentData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(int i = 0; i< task.getResult().size();i++){
                        // fetch the data
                        String studentID = task.getResult().getDocuments().get(i).getString("studentID");
                        String studentName = task.getResult().getDocuments().get(i).getString("studentName");
                        String studentEmail = task.getResult().getDocuments().get(i).getString("studentEmail");
                        String studentPassword = task.getResult().getDocuments().get(i).getString("studentPassword");
                        String isAdvisor = task.getResult().getDocuments().get(i).getString("isAdvisor");
                        String token = task.getResult().getDocuments().get(i).getString("token");
                        ArrayList<String> clubList =(ArrayList<String>) task.getResult().getDocuments().get(i).get("clubList");

                        //assign the data
                        StudentModule studentModule = new StudentModule();
                        //decrypt the data
                        SymmtCrypto symmtCrypto = new SymmtCrypto();

                        try {
                            studentModule.setStudentID(studentID);
                            studentModule.setStudentName(symmtCrypto.decrypt(studentName));
                            studentModule.setStudentEmail(symmtCrypto.decrypt(studentEmail));
                            studentModule.setStudentPassword(symmtCrypto.decrypt(studentPassword));
                            studentModule.setIsAdvisor(isAdvisor);
                            studentModule.setToken(token);
                            studentModule.setClubList(clubList);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                studentViewFetchDataMessage.onUpdateFailure(e.getMessage());
            }
        });
    }
}
