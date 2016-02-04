package com.commonsware.cwac.updater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.commonsware.cwac.wakeful.WakefulIntentService;

public class WakefulReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        WakefulIntentService.sendWakefulWork(context, (Intent) intent.getParcelableExtra("com.commonsware.cwac.updater.EXTRA_COMMAND"));
    }
}
