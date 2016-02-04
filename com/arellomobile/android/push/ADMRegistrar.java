package com.arellomobile.android.push;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ADMRegistrar {
    public static void checkManifest(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        String str = packageName + ".permission.RECEIVE_ADM_MESSAGE";
        try {
            packageManager.getPermissionInfo(str, AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
            try {
                ActivityInfo[] activityInfoArr = packageManager.getPackageInfo(packageName, 2).receivers;
                if (activityInfoArr == null || activityInfoArr.length == 0) {
                    throw new IllegalStateException("No receiver for package " + packageName);
                }
                if (Log.isLoggable("ADMRegistrar", 2)) {
                    Log.v("ADMRegistrar", "number of receivers for " + packageName + ": " + activityInfoArr.length);
                }
                Set hashSet = new HashSet();
                for (ActivityInfo activityInfo : activityInfoArr) {
                    if ("com.amazon.device.messaging.permission.SEND".equals(activityInfo.permission)) {
                        hashSet.add(activityInfo.name);
                    }
                }
                if (hashSet.isEmpty()) {
                    throw new IllegalStateException("No receiver allowed to receive com.amazon.device.messaging.permission.SEND");
                }
                checkReceiver(context, hashSet, "com.amazon.device.messaging.intent.REGISTRATION");
                checkReceiver(context, hashSet, "com.amazon.device.messaging.intent.RECEIVE");
            } catch (NameNotFoundException e) {
                throw new IllegalStateException("Could not get receivers for package " + packageName);
            }
        } catch (NameNotFoundException e2) {
            throw new IllegalStateException("Application does not define permission " + str);
        }
    }

    private static void checkReceiver(Context context, Set<String> set, String str) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        Intent intent = new Intent(str);
        intent.setPackage(packageName);
        List<ResolveInfo> queryBroadcastReceivers = packageManager.queryBroadcastReceivers(intent, 32);
        if (queryBroadcastReceivers.isEmpty()) {
            throw new IllegalStateException("No receivers for action " + str);
        }
        if (Log.isLoggable("ADMRegistrar", 2)) {
            Log.v("ADMRegistrar", "Found " + queryBroadcastReceivers.size() + " receivers for action " + str);
        }
        for (ResolveInfo resolveInfo : queryBroadcastReceivers) {
            String str2 = resolveInfo.activityInfo.name;
            if (!set.contains(str2)) {
                throw new IllegalStateException("Receiver " + str2 + " is not set with permission com.amazon.device.messaging.permission.SEND");
            }
        }
    }
}
