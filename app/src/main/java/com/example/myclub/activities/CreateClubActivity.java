package com.example.myclub.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myclub.Encryption.SymmtCrypto;
import com.example.myclub.R;
import com.example.myclub.clubAPI.ClubDataPresenter;
import com.example.myclub.clubAPI.ClubModule;
import com.example.myclub.clubAPI.ClubSendData;
import com.example.myclub.clubAPI.ClubViewMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.sql.Timestamp;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;

public class CreateClubActivity extends Activity {
    private String studentID, studentEmail,studentPassword,studentName, isAdvisor,  advisorEmail;
    ArrayList<String> clubList;
    ArrayList<String> AdvisorClubList = new ArrayList<>();

    String[] advisorlists;
    private FirebaseFirestore firebaseFirestore;

    TextView textClubName, textClubCategory, textClubDesc;
    private MaterialSpinner spinnerAdvisor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.create_club_activity);
        studentID = getIntent().getStringExtra("studentID");
        studentEmail = getIntent().getStringExtra("email");
        isAdvisor = getIntent().getStringExtra("role");
        studentPassword = getIntent().getStringExtra("password");
        studentName = getIntent().getStringExtra("name");
        clubList = getIntent().getStringArrayListExtra("clublist");
        getStudentList();
        System.out.println("done getting emails");

        spinnerAdvisor = findViewById(R.id.spinnerAdvisor);
        textClubName = findViewById(R.id.editTxtClubName);
        textClubCategory = findViewById(R.id.editTxtCategory);
        textClubDesc = findViewById(R.id.editTxtClubDescription);
    }


    private void ValidateRecord() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String id = studentEmail;
        String name = textClubName.getText().toString();
        String category = textClubCategory.getText().toString();
        String desc = textClubDesc.getText().toString();
        String token = timestamp.toString();


        if(!TextUtils.isEmpty(textClubDesc.getText().toString())
                && !TextUtils.isEmpty(textClubName.getText().toString())
                && !TextUtils.isEmpty(textClubCategory.getText().toString())
                && spinnerAdvisor.getSelectedItem() != null){

            advisorEmail= spinnerAdvisor.getSelectedItem().toString();
            //clubMemberList.add(advisorEmail);
            System.out.println(advisorEmail);
           // clubSendData.onSuccessUpdate(CreateClubActivity.this, id, name, desc, advisorEmail, category, token);
            uploadData( id, name, desc, advisorEmail, category, token);
        }else{
            if(TextUtils.isEmpty(textClubName.getText().toString())){
                textClubName.setError("Club Name is required");
                return;
            }if(TextUtils.isEmpty(textClubDesc.getText().toString())){
                textClubDesc.setError("Club Description is required ");
                return;
            }if(TextUtils.isEmpty(textClubCategory.getText().toString())){
                textClubCategory.setError("Club Category is required ");
                return;
            }if (spinnerAdvisor.getSelectedItem() == null){
                spinnerAdvisor.setError("Please select advisor");
                return;
            }
        }
    }
    void uploadData(String clubID, String clubName, String clubDescription, String clubAdvisor, String clubType, String token){
        ArrayList<String> clubMemberList = new ArrayList<>();
        ArrayList<String> clubPostList = new ArrayList<>();
        clubList.add(clubName);
        clubMemberList.add(0,advisorEmail);
        clubPostList.add(0,"Welcome Post");
        //clubMemberList.add(clubAdvisor);
        AdvisorClubList.add(clubName);
        ClubModule clubModule = new ClubModule(clubID,clubName, clubDescription,clubAdvisor, clubType, token, clubMemberList,clubPostList);
        FirebaseFirestore.getInstance().collection("ClubData").document(token).set(clubModule, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            firebaseFirestore = FirebaseFirestore.getInstance();
                            DocumentReference record = firebaseFirestore.collection("StudentData").document(clubAdvisor);
                            record.update("isAdvisor", "advisor","clubList",AdvisorClubList)
                                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(CreateClubActivity.this, "Club Created !", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(CreateClubActivity.this, AdminHomeActivity.class);
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
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CreateClubActivity.this, "Could not update Student role",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(CreateClubActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    void getStudentList(){
        FirebaseFirestore.getInstance().collection("StudentData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            ArrayList<String> listOfStudent = new ArrayList<>();

                            for (int i=0; i<task.getResult().getDocuments().size(); i++){
                                String email =task.getResult().getDocuments().get(i).get("studentID").toString();
                                String role =task.getResult().getDocuments().get(i).get("isAdvisor").toString();
                                if(role.equals("student")){
                                    listOfStudent.add(email);
                                }
                                advisorlists = listOfStudent.toArray(new String[listOfStudent.size()]);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateClubActivity.this, android.R.layout.simple_spinner_item, advisorlists);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerAdvisor.setAdapter(adapter);

                        }
                    }
                });
    }
    public void onCreateClubClick(View view) {
        ValidateRecord();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CreateClubActivity.this, AdminHomeActivity.class);
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


    public void onClickViewClubButton(View view) {
        Intent intent = new Intent(CreateClubActivity.this, ClubListActivity.class);
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
}
