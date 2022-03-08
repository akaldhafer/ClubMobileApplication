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
import com.example.myclub.adapters.BookingListAdapter;
import com.example.myclub.adapters.MemberAdpater;
import com.example.myclub.classroomAPI.ClassroomReceiveData;
import com.example.myclub.studentAPI.StudentModule;
import com.example.myclub.studentAPI.StudentReceiveData;
import com.example.myclub.studentAPI.StudentViewFetchDataMessage;

import java.util.ArrayList;

public class ViewClubMember extends Activity implements StudentViewFetchDataMessage {
    private String studentID, studentEmail,studentPassword,studentName, isAdvisor;
    ArrayList<String> clubList;

    private RecyclerView ListDataView;
    private MemberAdpater memberAdpater;
    ArrayList<StudentModule> studentModuleArrayList = new ArrayList<>();
    private TextView title;
    private StudentReceiveData studentReceiveData;

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
        title.setText("Club Members");
        studentReceiveData = new StudentReceiveData(this,this);
        RecyclerViewMethods();
        studentReceiveData.onSuccessUpdate(this);

    }

    public void RecyclerViewMethods() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        ListDataView.setLayoutManager(manager);
        ListDataView.setHasFixedSize(true);
        memberAdpater = new MemberAdpater(this, studentModuleArrayList,studentID,studentEmail,studentPassword,studentName,isAdvisor,clubList);
        ListDataView.setAdapter(memberAdpater);
        ListDataView.invalidate();
    }

    @Override
    public void onUpdateSuccess(StudentModule message) {
        System.out.println("Get Update "+message.getStudentEmail());

        if(message != null && message.getClubList().contains(clubList.get(0).toString())){
            StudentModule studentModule = new StudentModule();
            studentModule.setStudentID(message.getStudentID());
            studentModule.setStudentEmail(message.getStudentEmail());
            studentModule.setStudentName(message.getStudentName());
            studentModule.setClubList(message.getClubList());
            studentModule.setToken(message.getToken());
            studentModule.setIsAdvisor(message.getIsAdvisor());
            studentModule.setStudentPassword(message.getStudentPassword());
            studentModule.setToken(message.getToken());
            studentModuleArrayList.add(studentModule);
        }
        memberAdpater.notifyDataSetChanged();
    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(ViewClubMember.this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ViewClubMember.this, AdvisorHomeActivity.class);
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
