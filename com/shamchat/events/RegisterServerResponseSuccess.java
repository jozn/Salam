package com.shamchat.events;

import org.json.JSONObject;

public final class RegisterServerResponseSuccess {
    public String phoneNumber;
    public JSONObject serverResponseJsonObject;
    public String status;

    public RegisterServerResponseSuccess(String phoneNumber, JSONObject serverResponseJsonObject, String status) {
        this.phoneNumber = phoneNumber;
        this.serverResponseJsonObject = serverResponseJsonObject;
        this.status = status;
    }
}
