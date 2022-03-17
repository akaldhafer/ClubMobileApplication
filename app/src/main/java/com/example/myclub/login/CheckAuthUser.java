package com.example.myclub.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.myclub.Encryption.SymmtCrypto;
import com.example.myclub.activities.AdminHomeActivity;
import com.example.myclub.activities.AdvisorHomeActivity;
import com.example.myclub.activities.MainActivity;
import com.example.myclub.activities.OnBoardingPage1;
import com.example.myclub.activities.StudentHomeActivity;
import com.example.myclub.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CheckAuthUser extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String email = user.getEmail();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            FirebaseFirestore.getInstance().collection("StudentData")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(int i = 0; i< task.getResult().size();i++){
                            try {
                                //decrypt the data
                                SymmtCrypto symmtCrypto = new SymmtCrypto();
                                // fetch the data
                                String studentID = task.getResult().getDocuments().get(i).getString("studentID");
                                String studentName = symmtCrypto.decrypt(task.getResult().getDocuments().get(i).getString("studentName"));
                                String studentEmail = symmtCrypto.decrypt(task.getResult().getDocuments().get(i).getString("studentEmail"));
                                String studentPassword = symmtCrypto.decrypt(task.getResult().getDocuments().get(i).getString("studentPassword"));
                                String isAdvisor = task.getResult().getDocuments().get(i).getString("isAdvisor");
                                String token = task.getResult().getDocuments().get(i).getString("token");
                                ArrayList<String> clubList =(ArrayList<String>) task.getResult().getDocuments().get(i).get("clubList");

                                //check user type
                                if(studentEmail.equals(uid) && isAdvisor.equals("admin")){
                                    Toast.makeText(CheckAuthUser.this, "Welcome Back !",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(CheckAuthUser.this, AdminHomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("studentID", studentID);
                                    intent.putExtra("email", studentEmail);
                                    intent.putExtra("password",studentPassword);
                                    intent.putExtra("name", studentName);
                                    intent.putExtra("role", isAdvisor);
                                    intent.putStringArrayListExtra("clublist", clubList);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(studentEmail.equals(uid) && isAdvisor.equals("advisor")){
                                    Toast.makeText(CheckAuthUser.this, "Welcome Back !",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(CheckAuthUser.this, AdvisorHomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("studentID", studentID);
                                    intent.putExtra("email", studentEmail);
                                    intent.putExtra("password",studentPassword);
                                    intent.putExtra("name", studentName);
                                    intent.putExtra("role", isAdvisor);
                                    intent.putStringArrayListExtra("clublist", clubList);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(studentEmail.equals(uid) && isAdvisor.equals("student")){
                                    Toast.makeText(CheckAuthUser.this, "Welcome Back !",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(CheckAuthUser.this, StudentHomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("studentID", studentID);
                                    intent.putExtra("email", studentEmail);
                                    intent.putExtra("password",studentPassword);
                                    intent.putExtra("name", studentName);
                                    intent.putExtra("role", isAdvisor);
                                    intent.putStringArrayListExtra("clublist", clubList);
                                    startActivity(intent);
                                    finish();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(CheckAuthUser.this, e.getMessage(),Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(CheckAuthUser.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CheckAuthUser.this, "Please login",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CheckAuthUser.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });

        }
        else{
            Intent intent = new Intent(CheckAuthUser.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }


}
