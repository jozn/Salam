package com.arellomobile.android.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.arellomobile.android.push.utils.GeneralUtils;

public class AlarmReceiver extends BroadcastReceiver {
    private static int counter;

    static {
        counter = 0;
    }

    public void onReceive(Context context, Intent intent) {
        Intent intent2 = new Intent(context, PushGCMIntentService.class);
        if (GeneralUtils.isAmazonDevice()) {
            intent2.setAction("com.amazon.device.messaging.intent.RECEIVE");
        } else {
            intent2.setAction("com.google.android.c2dm.intent.RECEIVE");
        }
        intent2.putExtras(intent.getExtras());
        context.startService(intent2);
    }
}
