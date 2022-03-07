package com.example.myclub.clubAPI;


public interface ClubViewFetchDataMessage {
    void onUpdateSuccess(ClubModule message);
    void onUpdateFailure(String message);
}
