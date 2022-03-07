package com.example.myclub.postAPI;


public interface PostViewFetchDataMessage {
    void onUpdateSuccess(PostModule message);
    void onUpdateFailure(String message);
}
