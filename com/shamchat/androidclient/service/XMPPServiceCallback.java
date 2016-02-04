package com.shamchat.androidclient.service;

public interface XMPPServiceCallback {
    void connectionStateChanged();

    void newMessage(String str, String str2, boolean z);
}
