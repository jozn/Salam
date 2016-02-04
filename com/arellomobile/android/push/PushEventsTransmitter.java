package com.arellomobile.android.push;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import com.arellomobile.android.push.utils.GeneralUtils;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;

public class PushEventsTransmitter {
    private static boolean getUseBroadcast(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getApplicationContext().getPackageName(), AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            if (applicationInfo.metaData == null) {
                return true;
            }
            boolean z = applicationInfo.metaData.getBoolean("PW_BROADCAST_REGISTRATION", true);
            System.out.println("Using broadcast registration: " + z);
            return z;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    static void onMessageReceive(Context context, String str, Bundle bundle) {
        transmit(context, str, "PUSH_RECEIVE_EVENT", bundle);
    }

    static void onRegisterError(Context context, String str) {
        if (getUseBroadcast(context)) {
            transmitBroadcast(context, str, "REGISTER_ERROR_EVENT");
        } else {
            transmit(context, str, "REGISTER_ERROR_EVENT", null);
        }
    }

    public static void onRegistered(Context context, String str) {
        if (getUseBroadcast(context)) {
            transmitBroadcast(context, str, "REGISTER_EVENT");
        } else {
            transmit(context, str, "REGISTER_EVENT", null);
        }
    }

    static void onUnregistered(Context context, String str) {
        if (getUseBroadcast(context)) {
            transmitBroadcast(context, str, "UNREGISTER_EVENT");
        } else {
            transmit(context, str, "UNREGISTER_EVENT", null);
        }
    }

    static void onUnregisteredError(Context context, String str) {
        if (getUseBroadcast(context)) {
            transmitBroadcast(context, str, "UNREGISTER_ERROR_EVENT");
        } else {
            transmit(context, str, "UNREGISTER_ERROR_EVENT", null);
        }
    }

    private static void transmit(Context context, String str, String str2, Bundle bundle) {
        Intent intent = new Intent(context, MessageActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.putExtra(str2, str);
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        context.startActivity(intent);
    }

    private static void transmitBroadcast(Context context, String str, String str2) {
        String packageName = context.getPackageName();
        Intent intent = new Intent(packageName + ".com.arellomobile.android.push.REGISTER_BROAD_CAST_ACTION");
        intent.putExtra(str2, str);
        intent.setPackage(packageName);
        if (GeneralUtils.checkStickyBroadcastPermissions(context)) {
            context.sendStickyBroadcast(intent);
            return;
        }
        Log.w(PushEventsTransmitter.class.getSimpleName(), "No android.permission.BROADCAST_STICKY. Reverting to simple broadcast");
        context.sendBroadcast(intent);
    }
}
