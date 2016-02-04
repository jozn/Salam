package com.shamchat.pushnotification;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import com.arellomobile.android.push.PushManager;
import com.arellomobile.android.push.SendPushTagsCallBack;
import com.arellomobile.android.push.exception.PushWooshException;
import java.util.HashMap;
import java.util.Map;

public class SendTagsFragment extends Fragment implements SendPushTagsCallBack {
    private int mSendTagsStatus;
    private final Object mSyncObject;
    private AsyncTask<Void, Void, Void> mTask;

    /* renamed from: com.shamchat.pushnotification.SendTagsFragment.1 */
    class C11661 extends AsyncTask<Void, Void, Void> {
        final /* synthetic */ Context val$context;
        final /* synthetic */ Map val$tags;

        C11661(Context context, Map map) {
            this.val$context = context;
            this.val$tags = map;
        }

        protected final /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
            PushManager.sendTags(this.val$context, this.val$tags, SendTagsFragment.this);
            return null;
        }
    }

    public SendTagsFragment() {
        this.mSyncObject = new Object();
        this.mSendTagsStatus = 2131493393;
    }

    public boolean canSendTags() {
        boolean z;
        synchronized (this.mSyncObject) {
            z = this.mTask == null;
        }
        return z;
    }

    public void submitTags(Context context, String citUserId) {
        System.out.println("PUSH submitTags citUserId " + citUserId);
        synchronized (this.mSyncObject) {
            if (!canSendTags()) {
            } else if (goodAllInputData(citUserId)) {
                this.mSendTagsStatus = 2131493394;
                Map<String, Object> tags = generateTags(citUserId);
                System.out.println("PUSH submitTags tags with citUserId " + tags);
                this.mTask = new C11661(context, tags);
                this.mTask.execute(new Void[]{null});
            }
        }
    }

    public int getSendTagsStatus() {
        int i;
        synchronized (this.mSyncObject) {
            i = this.mSendTagsStatus;
        }
        return i;
    }

    public void taskStarted() {
        synchronized (this.mSyncObject) {
            this.mSendTagsStatus = 2131493394;
        }
    }

    public void onSentTagsSuccess(Map<String, String> map) {
        synchronized (this.mSyncObject) {
            this.mSendTagsStatus = 2131493395;
            this.mTask = null;
        }
    }

    public void onSentTagsError(PushWooshException e) {
        synchronized (this.mSyncObject) {
            this.mSendTagsStatus = 2131493389;
            if (e != null) {
                e.printStackTrace();
            }
            this.mTask = null;
        }
    }

    private boolean goodAllInputData(String tagString) {
        if (tagString.length() != 0) {
            return true;
        }
        this.mSendTagsStatus = 2131493390;
        return false;
    }

    private void transfareTaskStartsToActivity() {
        getActivity();
    }

    private void transfareTaskEndsToActivity() {
        getActivity();
    }

    private void transfareStatusToActivity() {
        getActivity();
    }

    private Map<String, Object> generateTags(String tagString) {
        Map<String, Object> tags = new HashMap();
        if (tagString.length() != 0) {
            tags.put("userId", tagString);
            System.out.println("PUSH TAG PUT" + tagString);
        }
        return tags;
    }
}
