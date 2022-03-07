package com.example.myclub.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myclub.R;

import java.util.ArrayList;

public class AdminHomeActivity extends Activity {
    private String studentID, studentEmail,studentPassword,studentName, isAdvisor;
    ArrayList<String> clubList;
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.admin_home_activity);
        studentID = getIntent().getStringExtra("studentID");
        studentEmail = getIntent().getStringExtra("email");
        isAdvisor = getIntent().getStringExtra("role");

        studentPassword = getIntent().getStringExtra("password");

        studentName = getIntent().getStringExtra("name");
        clubList = getIntent().getStringArrayListExtra("clublist");
        textView = findViewById(R.id.admin_name);
        textView.setText(textView.getText()+studentName);


    }

    public void onClickProfile(View view) {
        Intent intent = new Intent(AdminHomeActivity.this, ProfileActivity.class);
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

    public void onClickBookingListButton(View view) {
        Intent intent = new Intent(AdminHomeActivity.this, BookingList.class);
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

    public void onClickPublishPostButton(View view) {
        Intent intent = new Intent(AdminHomeActivity.this, StudentHomeActivity.class);
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

    public void onClickBookRoomButton(View view) {
        Intent intent = new Intent(AdminHomeActivity.this, ApproveClubBookings.class);
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

    public void onClickCreateClubButton(View view) {
        Intent intent = new Intent(AdminHomeActivity.this, CreateClubActivity.class);
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
        finishAffinity();
        finish();
    }
}
