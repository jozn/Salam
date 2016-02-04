package com.arellomobile.android.push;

import android.content.Context;
import android.os.AsyncTask;
import com.arellomobile.android.push.exception.PushWooshException;
import java.util.Map;

abstract class SendPushTagsAbstractAsyncTask extends AsyncTask<Map<String, Object>, Void, Map<String, String>> implements SendPushTagsCallBack {
    private Context mContext;
    private PushWooshException mError;

    public SendPushTagsAbstractAsyncTask(Context context) {
        this.mContext = context;
    }

    private Map<String, String> doInBackground(Map<String, Object>... mapArr) {
        try {
            if (mapArr.length != 1) {
                throw new PushWooshException("Wrong parameters");
            }
            Map<String, String> sendTagsFromBG = PushManager.sendTagsFromBG(this.mContext, mapArr[0]);
            this.mContext = null;
            return sendTagsFromBG;
        } catch (PushWooshException e) {
            this.mError = e;
            this.mContext = null;
            return null;
        }
    }

    protected /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
        Map map = (Map) obj;
        super.onPostExecute(map);
        if (this.mError != null) {
            onSentTagsError(this.mError);
        } else {
            onSentTagsSuccess(map);
        }
    }

    protected void onPreExecute() {
        super.onPreExecute();
        taskStarted();
    }
}
