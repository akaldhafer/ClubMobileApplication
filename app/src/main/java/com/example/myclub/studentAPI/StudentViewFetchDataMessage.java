package com.example.myclub.studentAPI;

public interface StudentViewFetchDataMessage {
    void onUpdateSuccess(StudentModule message);
    void onUpdateFailure(String message);
}
