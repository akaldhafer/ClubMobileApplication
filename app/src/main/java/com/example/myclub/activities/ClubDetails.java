package com.example.myclub.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myclub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ClubDetails extends Activity {
    private String studentID, studentEmail,studentPassword,studentName, isAdvisor;
    ArrayList<String> clubList;
    EditText edDesc, edCategory;
    TextView vName, vEmail, vClubMember;
    String currentToken;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_details);

        studentID = getIntent().getStringExtra("studentID");
        studentEmail = getIntent().getStringExtra("email");
        studentPassword = getIntent().getStringExtra("password");
        isAdvisor = getIntent().getStringExtra("role");
        studentName = getIntent().getStringExtra("name");
        clubList = getIntent().getStringArrayListExtra("clublist");

        edCategory = findViewById(R.id.viewClubCategory);
        edDesc = findViewById(R.id.viewClubDescription);
        vName = findViewById(R.id.viewClubName);
        vEmail = findViewById(R.id.viewClubAdvisorEmail);
        vClubMember= findViewById(R.id.viewClubMember);
        GetClubDetails();

    }

    void GetClubDetails(){
        FirebaseFirestore.getInstance().collection("ClubData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){


                    for (int i=0; i<task.getResult().getDocuments().size(); i++){
                        String clubID = task.getResult().getDocuments().get(i).getString("clubID");
                        String clubName = task.getResult().getDocuments().get(i).getString("clubName");
                        String clubDescription = task.getResult().getDocuments().get(i).getString("clubDescription");
                        String clubAdvisor = task.getResult().getDocuments().get(i).getString("clubAdvisor");
                        String clubType = task.getResult().getDocuments().get(i).getString("clubType");
                        String token = task.getResult().getDocuments().get(i).getString("token");
                        ArrayList<String> clubMemberList =(ArrayList<String>) task.getResult().getDocuments().get(i).get("clubMemberList");

                        if(clubAdvisor.equals(studentEmail)){
                            edCategory.setText(clubType);
                            edDesc.setText(clubDescription);
                            vName.setText(clubName);
                            vEmail.setText(clubAdvisor);
                            vClubMember.setText(clubMemberList.toString());
                            currentToken = token;
                        }
                    }

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ClubDetails.this, AdvisorHomeActivity.class);
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

    public void onClickUpdateButton(View view) {

        String desc = edDesc.getText().toString().trim();
        String category = edCategory.getText().toString().trim();

        if(!TextUtils.isEmpty(desc) && !TextUtils.isEmpty(category) ){
            //update record
            DocumentReference record = FirebaseFirestore.getInstance().collection("ClubData").document(currentToken);
            record.update("clubType",category, "clubDescription", desc)
                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ClubDetails.this, "Club updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ClubDetails.this, "Something went wrong, check the internet connection",Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            if(TextUtils.isEmpty(desc)){
                edDesc.setError("Description is required!");
                return;
            }
            if(TextUtils.isEmpty(category)){
                edCategory.setError("Category is required!");
                return;
            }
        }
    }

    public void onClickViewActivityButton(View view) {
        Intent intent = new Intent(ClubDetails.this, ClubPostList.class);
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
