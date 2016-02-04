package com.google.android.gcm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class GCMRegistrar {
    public static void checkManifest(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        String str = packageName + ".permission.C2D_MESSAGE";
        try {
            packageManager.getPermissionInfo(str, AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
            try {
                ActivityInfo[] activityInfoArr = packageManager.getPackageInfo(packageName, 2).receivers;
                if (activityInfoArr == null || activityInfoArr.length == 0) {
                    throw new IllegalStateException("No receiver for package " + packageName);
                }
                if (Log.isLoggable("GCMRegistrar", 2)) {
                    Log.v("GCMRegistrar", "number of receivers for " + packageName + ": " + activityInfoArr.length);
                }
                Set hashSet = new HashSet();
                for (ActivityInfo activityInfo : activityInfoArr) {
                    if ("com.google.android.c2dm.permission.SEND".equals(activityInfo.permission)) {
                        hashSet.add(activityInfo.name);
                    }
                }
                if (hashSet.isEmpty()) {
                    throw new IllegalStateException("No receiver allowed to receive com.google.android.c2dm.permission.SEND");
                }
                checkReceiver(context, hashSet, "com.google.android.c2dm.intent.REGISTRATION");
                checkReceiver(context, hashSet, "com.google.android.c2dm.intent.RECEIVE");
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
        if (Log.isLoggable("GCMRegistrar", 2)) {
            Log.v("GCMRegistrar", "Found " + queryBroadcastReceivers.size() + " receivers for action " + str);
        }
        for (ResolveInfo resolveInfo : queryBroadcastReceivers) {
            String str2 = resolveInfo.activityInfo.name;
            if (!set.contains(str2)) {
                throw new IllegalStateException("Receiver " + str2 + " is not set with permission com.google.android.c2dm.permission.SEND");
            }
        }
    }

    private static int getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Coult not get package name: " + e);
        }
    }

    public static String getRegistrationId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.google.android.gcm", 0);
        String string = sharedPreferences.getString("regId", BuildConfig.VERSION_NAME);
        int i = sharedPreferences.getInt("appVersion", RtlSpacingHelper.UNDEFINED);
        int appVersion = getAppVersion(context);
        if (i == RtlSpacingHelper.UNDEFINED || i == appVersion) {
            return string;
        }
        Log.v("GCMRegistrar", "App version changed from " + i + " to " + appVersion + "; resetting registration id");
        setRegistrationId(context, BuildConfig.VERSION_NAME);
        return BuildConfig.VERSION_NAME;
    }

    public static void internalRegister(Context context, String... strArr) {
        String stringBuilder = new StringBuilder(strArr[0]).toString();
        Log.v("GCMRegistrar", "Registering app " + context.getPackageName() + " of senders " + stringBuilder);
        Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
        intent.setPackage("com.google.android.gsf");
        intent.putExtra("app", PendingIntent.getBroadcast(context, 0, new Intent(), 0));
        intent.putExtra("sender", stringBuilder);
        context.startService(intent);
    }

    public static void resetBackoff(Context context) {
        Log.d("GCMRegistrar", "resetting backoff for " + context.getPackageName());
        setBackoff(context, 3000);
    }

    static void setBackoff(Context context, int i) {
        Editor edit = context.getSharedPreferences("com.google.android.gcm", 0).edit();
        edit.putInt("backoff_ms", i);
        edit.commit();
    }

    public static void setRegisteredOnServer(Context context, boolean z) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.google.android.gcm", 0);
        Log.v("GCMRegistrar", "Setting registered on server status as: " + z);
        Editor edit = sharedPreferences.edit();
        edit.putBoolean("onServer", z);
        edit.commit();
    }

    static String setRegistrationId(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.google.android.gcm", 0);
        String string = sharedPreferences.getString("regId", BuildConfig.VERSION_NAME);
        int appVersion = getAppVersion(context);
        Log.v("GCMRegistrar", "Saving regId on app version " + appVersion);
        Editor edit = sharedPreferences.edit();
        edit.putString("regId", str);
        edit.putInt("appVersion", appVersion);
        edit.commit();
        return string;
    }
}
