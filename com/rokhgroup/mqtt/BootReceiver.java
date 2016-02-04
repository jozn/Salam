package com.rokhgroup.mqtt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.shamchat.androidclient.service.XMPPService;

public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.d(getClass().getCanonicalName(), "onReceive");
        context.startService(new Intent(context, MQTTService.class));
        context.startService(new Intent(context, XMPPService.class));
    }
}
