package com.arellomobile.android.push;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class PushHandlerActivity extends Activity {
    private PushManager mPushManager;

    private void handlePush() {
        this.mPushManager.onHandlePush(this);
        finish();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPushManager = new PushManager(this);
        handlePush();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handlePush();
    }
}
