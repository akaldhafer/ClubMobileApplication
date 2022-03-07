package com.example.myclub.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myclub.R;
import com.example.myclub.classroomAPI.ClassroomModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import fr.ganfra.materialspinner.MaterialSpinner;

public class BookClassroom extends Activity {
    private String studentID, studentEmail,studentPassword,studentName, isAdvisor;
    ArrayList<String> clubList;
    private MaterialSpinner spinnerTime, spinnerLocation;
    TextView edDate;
    private int difference;
    private String date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_classroom_activity);
        //get the current user info
        studentID = getIntent().getStringExtra("studentID");
        studentEmail = getIntent().getStringExtra("email");
        studentPassword = getIntent().getStringExtra("password");
        isAdvisor = getIntent().getStringExtra("role");
        studentName = getIntent().getStringExtra("name");
        clubList = getIntent().getStringArrayListExtra("clublist");

        //link with ui
        edDate = findViewById(R.id.edDate);
        spinnerLocation = findViewById(R.id.spinnerLocations);
        spinnerTime = findViewById(R.id.spinnerTime);

        String[] Times = {"8:00-9:00 am", "9:00-10:00 am", "10:00-11:00 am", "11:00-12:00 pm", "2:00-3:00 pm", "3:00-4:00 pm", "4:00-5:00 pm", "5:00-6:00 pm","6:00-7:00 pm"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapter);

        String[] Locations = {"AD1Level1","AD2Level1", "AD3Level2","AD4Level2", "AD5Level5","AD6Level6"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Locations);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(adapter2);

        //get the date
        Calendar calendar = Calendar.getInstance();
        final int Cyear = calendar.get(Calendar.YEAR);
        final int Cmonth = calendar.get(Calendar.MONTH);
        final int Cday = calendar.get(Calendar.DAY_OF_MONTH);

        edDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(
                        BookClassroom.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        int currentDate = ((Cyear*12*365)+(Cmonth*30)+Cday);
                        int pickDate = ((year*12*365)+(month*30)+day);
                        difference = pickDate - currentDate;
                        month=month+1;
                        date = day+"/"+month+"/"+year;
                        edDate.setText(date);
                    }
                },Cyear,Cmonth,Cday);

                datePickerDialog.show();
            }
        });


    }

    private void UploadData(String classID,String classLocation,String isAvailable,
                            String bookedByStudentEmail,String bookedForClubName,String bookedTime,String bookedDate,String token){

//        classroomSendData.onSuccessUpdate(BookClassroom.this,classID,classLocation,isAvailable,
//                bookedByStudentEmail,bookedForClubName,bookedTime,bookedDate,token);
        ClassroomModule classroomModule = new ClassroomModule( classID,  classLocation,  isAvailable,  bookedByStudentEmail,
                bookedForClubName,  bookedTime,  bookedDate,  token);

        FirebaseFirestore.getInstance().collection("ClassroomData").document(token).set(classroomModule, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(BookClassroom.this, "Booking successes ! ", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(BookClassroom.this, AdvisorHomeActivity.class);
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
                        else {
                            Toast.makeText(BookClassroom.this, "Something went wrong !", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void onBookClick(View view) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String classID = timestamp.toString();
        String isAvailable="Pending";
        String bookedByStudentEmail= studentEmail;
        String bookedForClubName =clubList.get(0).toString();
        String token = timestamp.toString();



        if(!TextUtils.isEmpty(edDate.getText())
                && !classID.isEmpty()
                && spinnerLocation.getSelectedItem() != null
                && spinnerTime.getSelectedItem() != null
                && !isAvailable.isEmpty()
                && !bookedByStudentEmail.isEmpty()
                && !bookedForClubName.isEmpty()
                && difference >=0){

            String bookedDate= date.trim();
            String classLocation = spinnerLocation.getSelectedItem().toString().trim();
            String bookedTime= spinnerTime.getSelectedItem().toString().trim();


            UploadData(classID,classLocation,isAvailable,
                    bookedByStudentEmail,bookedForClubName,bookedTime,bookedDate,token);
        }
        else{
            if (difference <0){
                edDate.setError("The date cannot be older than the current date!");
                Toast.makeText(BookClassroom.this, "Date cannot be older than the current date!", Toast.LENGTH_SHORT).show();
                return;
            }if(TextUtils.isEmpty(edDate.getText())){
                edDate.setError("Date is required!");
                return;}
            if(spinnerLocation.getSelectedItem() == null){
                spinnerLocation.setError("Location is required!");
                return;
            }
            if (spinnerTime.getSelectedItem() == null){
                spinnerTime.setError("Time is required!");
                return;
            }

        }



    }

    public void onClickViewBookingButton(View view) {
        Intent intent = new Intent(BookClassroom.this, ClubbookingList.class);
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
        Intent intent = new Intent(BookClassroom.this, AdvisorHomeActivity.class);
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
