package com.example.myclub.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclub.R;
import com.example.myclub.adapters.ApprovePostAdapter;
import com.example.myclub.postAPI.PostModule;
import com.example.myclub.postAPI.PostReceiveData;
import com.example.myclub.postAPI.PostViewFetchDataMessage;

import java.util.ArrayList;

public class ApprovePostActivity extends Activity implements PostViewFetchDataMessage {

    private String studentID, studentEmail,studentPassword,studentName, isAdvisor;
    ArrayList<String> clubList;

    private RecyclerView ListDataView;
    private ApprovePostAdapter approvePostAdapter;
    ArrayList<PostModule> postModuleArrayList = new ArrayList<>();
    private TextView title;
    private PostReceiveData postReceiveData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        studentID = getIntent().getStringExtra("studentID");
        studentEmail = getIntent().getStringExtra("email");
        isAdvisor = getIntent().getStringExtra("role");
        studentPassword = getIntent().getStringExtra("password");
        studentName = getIntent().getStringExtra("name");
        clubList = getIntent().getStringArrayListExtra("clublist");

        System.out.println("on start run "+studentEmail);

        ListDataView = findViewById(R.id.ListView);
        title = findViewById(R.id.settxtTitle);
        title.setText("Approve Club Posts");
        postReceiveData = new PostReceiveData(this,this);
        RecyclerViewMethods();
        postReceiveData.onSuccessUpdate(this);

    }

    public void RecyclerViewMethods() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        ListDataView.setLayoutManager(manager);
        ListDataView.setHasFixedSize(true);
        approvePostAdapter = new ApprovePostAdapter(this, postModuleArrayList);
        ListDataView.setAdapter(approvePostAdapter);
        ListDataView.invalidate();
    }

    @Override
    public void onUpdateSuccess(PostModule message) {
        System.out.println("Get Update "+message.getAdvisorEmail());

        if(message != null && message.getIsApproved().equals("pending")){
            PostModule postModule = new PostModule();
            postModule.setPostID(message.getPostID());
            postModule.setPostTitle(message.getPostTitle());
            postModule.setPostDescription(message.getPostDescription());
            postModule.setAdvisorEmail(message.getAdvisorEmail());
            postModule.setToken(message.getToken());
            postModule.setClubID(message.getClubID());
            postModule.setIsApproved(message.getIsApproved());
            postModule.setPostImgUrl(message.getPostImgUrl());
            postModule.setToken(message.getToken());

            postModuleArrayList.add(postModule);
        }
        approvePostAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(ApprovePostActivity.this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ApprovePostActivity.this, AdminHomeActivity.class);
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
