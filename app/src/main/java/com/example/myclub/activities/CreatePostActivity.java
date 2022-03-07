package com.example.myclub.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myclub.R;
import com.example.myclub.postAPI.PostSendData;
import com.example.myclub.postAPI.PostViewMessage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;

public class CreatePostActivity extends Activity implements PostViewMessage {
    private String studentID, studentEmail,studentPassword,studentName, isAdvisor;
    ArrayList<String> clubList= new ArrayList<>();
    EditText edTitle, edDesc;
    private String date;
    private ImageView imageView;
    public Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storageReference;
    private StorageTask uploadtask;
    private static final String TAG = "createPost";

    ProgressDialog mProgressDialog;
    PostSendData postSendData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_activity);

        //get the current user info
        studentID = getIntent().getStringExtra("studentID");
        studentEmail = getIntent().getStringExtra("email");
        studentPassword = getIntent().getStringExtra("password");
        isAdvisor = getIntent().getStringExtra("role");
        studentName = getIntent().getStringExtra("name");
        clubList = getIntent().getStringArrayListExtra("clublist");

    }

    public void onCreatePostClick(View view) {
    }

    public void onClickViewPostButton(View view) {

        Intent intent = new Intent(CreatePostActivity.this, ClubbookingList.class);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CreatePostActivity.this, AdvisorHomeActivity.class);
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

    @Override
    public void onUpdateFailure(String message) {

    }

    @Override
    public void onUpdateSuccess(String message) {

    }
}
