package com.example.hw9;

public interface IFragmentCallback {
    void sendMsgToActivity(String msg);

    String getMsgFromActivity(String msg);
}
