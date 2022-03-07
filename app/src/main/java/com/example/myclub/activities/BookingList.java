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
import com.example.myclub.classroomAPI.ClassroomModule;
import com.example.myclub.classroomAPI.ClassroomReceiveData;
import com.example.myclub.classroomAPI.ClassroomViewFetchMessage;

import java.util.ArrayList;

public class BookingList extends Activity implements ClassroomViewFetchMessage {

    private String studentID, studentEmail,studentPassword,studentName, isAdvisor;
    ArrayList<String> clubList;

    private RecyclerView ListDataView;
    private BookingListAdapter bookingListAdapter;
    ArrayList<ClassroomModule> classroomModuleArrayList = new ArrayList<>();
    private TextView title;
    private ClassroomReceiveData classroomReceiveData;

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
        title.setText("Approved Classroom Booking");
        classroomReceiveData = new ClassroomReceiveData(this,this);
        RecyclerViewMethods();
        classroomReceiveData.onSuccessUpdate(this);

    }

    public void RecyclerViewMethods() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        ListDataView.setLayoutManager(manager);
        ListDataView.setHasFixedSize(true);
        bookingListAdapter = new BookingListAdapter(this, classroomModuleArrayList);
        ListDataView.setAdapter(bookingListAdapter);
        ListDataView.invalidate();
    }

    @Override
    public void onUpdateSuccess(ClassroomModule message) {
        System.out.println("Get Update "+message.getBookedByStudentEmail());

        if(message != null && message.getIsAvailable().equals("Accepted")){
            ClassroomModule classroomModule = new ClassroomModule();
            classroomModule.setBookedDate(message.getBookedDate());
            classroomModule.setClassID(message.getClassID());
            classroomModule.setBookedForClubName(message.getBookedForClubName());
            classroomModule.setClassLocation(message.getClassLocation());
            classroomModule.setBookedTime(message.getBookedTime());
            classroomModule.setIsAvailable(message.getIsAvailable());
            classroomModule.setBookedByStudentEmail(message.getBookedByStudentEmail());
            classroomModule.setToken(message.getToken());
            classroomModuleArrayList.add(classroomModule);
        }
        bookingListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(BookingList.this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BookingList.this, AdminHomeActivity.class);
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
