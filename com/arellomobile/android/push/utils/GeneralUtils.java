package com.arellomobile.android.push.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class GeneralUtils {
    private static List<String> sWrongAndroidDevices;

    static {
        List arrayList = new ArrayList();
        sWrongAndroidDevices = arrayList;
        arrayList.add("9774d56d682e549c");
    }

    public static void checkNotNull(Object obj, String str) {
        if (obj == null) {
            throw new IllegalArgumentException(String.format("Please set the %1$s constant and recompile the app.", new Object[]{str}));
        }
    }

    public static void checkNotNullOrEmpty(String str, String str2) {
        checkNotNull(str, str2);
        if (str.length() == 0) {
            throw new IllegalArgumentException(String.format("Please set the %1$s constant and recompile the app.", new Object[]{str2}));
        }
    }

    public static boolean checkStickyBroadcastPermissions(Context context) {
        return context.getPackageManager().checkPermission("android.permission.BROADCAST_STICKY", context.getPackageName()) == 0;
    }

    public static String getDeviceUUID(Context context) {
        String string = Secure.getString(context.getContentResolver(), "android_id");
        if (string != null && !sWrongAndroidDevices.contains(string)) {
            return string;
        }
        try {
            string = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
            if (string != null) {
                return string;
            }
        } catch (RuntimeException e) {
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.arellomobile.android.push.deviceid", 2);
        string = sharedPreferences.getString("deviceid", null);
        if (string != null) {
            return string;
        }
        string = UUID.randomUUID().toString();
        Editor edit = sharedPreferences.edit();
        edit.putString("deviceid", string);
        edit.commit();
        return string;
    }

    public static boolean isAmazonDevice() {
        return Build.MANUFACTURER.equals("Amazon");
    }

    public static boolean isAppOnForeground(Context context) {
        List<RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return false;
        }
        String packageName = context.getPackageName();
        for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.importance == 100 && runningAppProcessInfo.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 4) == 4;
    }
}
