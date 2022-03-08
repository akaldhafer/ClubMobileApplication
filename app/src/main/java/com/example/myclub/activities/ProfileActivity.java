package com.example.myclub.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.myclub.Encryption.SymmtCrypto;
import com.example.myclub.R;
import com.example.myclub.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProfileActivity extends Activity {
    private String studentID, studentEmail,studentPassword,studentName, isAdvisor;
    ArrayList<String> clubList = new ArrayList<>();
    EditText edName;
    TextView vEmail, vclublist;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        edName = findViewById(R.id.profile_Name);
        vEmail = findViewById(R.id.profile_Email);
        vclublist = findViewById(R.id.profile_clubList);

        studentID = getIntent().getStringExtra("studentID");
        studentEmail = getIntent().getStringExtra("email");
        isAdvisor = getIntent().getStringExtra("role");
        studentPassword = getIntent().getStringExtra("password");
        studentName = getIntent().getStringExtra("name");
        clubList = getIntent().getStringArrayListExtra("clublist");
        System.out.println("done getting emails");

        edName.setText(studentName);
        vEmail.setText(studentEmail);
        if(clubList == null && clubList.isEmpty()){
            vclublist.setText("No Club Yet, try to join a club !");
        }else{
            vclublist.setText(clubList.toString());
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickUpdateButton(View view) {
        String name="";
        if(!edName.getText().toString().isEmpty()){
            SymmtCrypto symmtCrypto = new SymmtCrypto();
            try {
                name = symmtCrypto.encrypt(edName.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference record = firebaseFirestore.collection("StudentData").document(studentEmail);
            record.update("studentName",name)
                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            studentName = edName.getText().toString();
                            Toast.makeText(ProfileActivity.this, "Name Update !", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "Could not update user Name",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            edName.setError("Student Name is required");
            return;
        }

    }

    public void onClickLogOutButton(View view) {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isAdvisor.equals("admin")){
            Intent intent = new Intent(ProfileActivity.this, AdminHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("studentID", studentID);
            intent.putExtra("email", studentEmail);
            intent.putExtra("password",studentPassword);
            intent.putExtra("name", studentName);
            intent.putExtra("role", isAdvisor);
            intent.putStringArrayListExtra("clublist", clubList);
            startActivity(intent);
            finish();
        }else if(isAdvisor.equals("advisor")){
            Intent intent = new Intent(ProfileActivity.this, AdvisorHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("studentID", studentID);
            intent.putExtra("email", studentEmail);
            intent.putExtra("password",studentPassword);
            intent.putExtra("name", studentName);
            intent.putExtra("role", isAdvisor);
            intent.putStringArrayListExtra("clublist", clubList);
            startActivity(intent);
            finish();
        }else if(isAdvisor.equals("student")){
            Intent intent = new Intent(ProfileActivity.this, StudentHomeActivity.class);
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
        else{
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

    }
}
