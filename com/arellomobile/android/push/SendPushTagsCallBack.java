package com.arellomobile.android.push;

import com.arellomobile.android.push.exception.PushWooshException;
import java.util.Map;

public interface SendPushTagsCallBack {
    void onSentTagsError(PushWooshException pushWooshException);

    void onSentTagsSuccess(Map<String, String> map);

    void taskStarted();
}
