package com.arellomobile.android.push;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.arellomobile.android.push.utils.PreferenceUtils;

public abstract class BasePushMessageReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        ((NotificationManager) context.getSystemService("notification")).cancel(PreferenceUtils.getMessageId(context));
        new PushManager(context).sendPushStat(context, intent.getExtras().getString("p"));
    }
}
