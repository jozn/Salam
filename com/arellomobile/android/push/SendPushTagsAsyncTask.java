package com.arellomobile.android.push;

import android.content.Context;
import com.arellomobile.android.push.exception.PushWooshException;
import java.util.Map;

final class SendPushTagsAsyncTask extends SendPushTagsAbstractAsyncTask {
    private SendPushTagsCallBack mCallBack;

    public SendPushTagsAsyncTask(Context context, SendPushTagsCallBack sendPushTagsCallBack) {
        super(context);
        this.mCallBack = sendPushTagsCallBack;
    }

    public final void onSentTagsError(PushWooshException pushWooshException) {
        if (this.mCallBack != null) {
            this.mCallBack.onSentTagsError(pushWooshException);
        }
    }

    public final void onSentTagsSuccess(Map<String, String> map) {
        if (this.mCallBack != null) {
            this.mCallBack.onSentTagsSuccess(map);
        }
    }

    public final void taskStarted() {
        if (this.mCallBack != null) {
            this.mCallBack.taskStarted();
        }
    }
}
