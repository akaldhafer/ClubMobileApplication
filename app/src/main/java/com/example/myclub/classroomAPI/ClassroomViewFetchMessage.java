package com.example.myclub.classroomAPI;


import com.example.myclub.studentAPI.StudentModule;

public interface ClassroomViewFetchMessage {
    void onUpdateSuccess(ClassroomModule message);
    void onUpdateFailure(String message);
}
