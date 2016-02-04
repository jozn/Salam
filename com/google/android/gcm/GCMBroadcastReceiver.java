package com.google.android.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GCMBroadcastReceiver extends BroadcastReceiver {
    public final void onReceive(Context context, Intent intent) {
        Log.v("GCMBroadcastReceiver", "onReceive: " + intent.getAction());
        String str = "com.arellomobile.android.push.PushGCMIntentService";
        Log.v("GCMBroadcastReceiver", "GCM IntentService class: " + str);
        GCMBaseIntentService.runIntentInService(context, intent, str);
        setResult(-1, null, null);
    }
}
