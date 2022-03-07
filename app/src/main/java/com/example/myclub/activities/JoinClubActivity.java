package com.example.myclub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclub.R;
import com.example.myclub.adapters.JoinClubAdapter;
import com.example.myclub.clubAPI.ClubModule;
import com.example.myclub.clubAPI.ClubReceiveData;
import com.example.myclub.clubAPI.ClubViewFetchDataMessage;

import java.util.ArrayList;

public class JoinClubActivity extends AppCompatActivity implements ClubViewFetchDataMessage {


    private String studentID, studentEmail,studentPassword,studentName, isAdvisor;
    ArrayList<String> clubList;

    private RecyclerView ListDataView;
    private JoinClubAdapter joinClubAdapter;
    ArrayList<ClubModule> clubModuleArrayList = new ArrayList<>();
    private TextView title;
    private ClubReceiveData clubReceiveData;

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
        title.setText("Lets Join new Club");
        clubReceiveData = new ClubReceiveData(this,this);
        RecyclerViewMethods();
        clubReceiveData.onSuccessUpdate(this);
    }


    public void RecyclerViewMethods() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        ListDataView.setLayoutManager(manager);
        ListDataView.setHasFixedSize(true);
        joinClubAdapter = new JoinClubAdapter(this, clubModuleArrayList,studentID, studentEmail,studentPassword,studentName, isAdvisor, clubList);
        ListDataView.setAdapter(joinClubAdapter);
        ListDataView.invalidate();
    }

    @Override
    public void onUpdateSuccess(ClubModule message) {
        System.out.println("Get Update "+message.getClubAdvisor()+message.getClubName());

        if(clubList == null){
            if(message != null){
                System.out.println("Get Update "+message.getClubPostList()+message.getClubName());
                ClubModule clubModule = new ClubModule();
                clubModule.setClubName(message.getClubName());
                clubModule.setClubDescription(message.getClubDescription());
                clubModule.setClubType(message.getClubType());
                clubModule.setClubAdvisor(message.getClubAdvisor());
                clubModule.setClubMemberList(message.getClubMemberList());
                clubModule.setClubPostList(message.getClubPostList());
                clubModule.setToken(message.getToken());
                clubModuleArrayList.add(clubModule);
            }
        }else{
            if(message != null && !clubList.contains(message.getClubName())){
                System.out.println("Get Update "+message.getClubPostList()+message.getClubName());
                ClubModule clubModule = new ClubModule();
                clubModule.setClubName(message.getClubName());
                clubModule.setClubDescription(message.getClubDescription());
                clubModule.setClubType(message.getClubType());
                clubModule.setClubAdvisor(message.getClubAdvisor());
                clubModule.setClubMemberList(message.getClubMemberList());
                clubModule.setClubPostList(message.getClubPostList());
                clubModule.setToken(message.getToken());
                clubModuleArrayList.add(clubModule);
            }
        }
        joinClubAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdateFailure(String message) {

        Toast.makeText(JoinClubActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(JoinClubActivity.this, StudentHomeActivity.class);
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
