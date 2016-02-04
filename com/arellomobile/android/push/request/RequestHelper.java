package com.arellomobile.android.push.request;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import com.arellomobile.android.push.utils.GeneralUtils;
import com.arellomobile.android.push.utils.PreferenceUtils;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class RequestHelper {
    public static Map<String, Object> getRegistrationUnregistrationData(Context context, String str) {
        Object hashMap = new HashMap();
        hashMap.put("application", PreferenceUtils.getApplicationId(context));
        hashMap.put("hwid", GeneralUtils.getDeviceUUID(context));
        hashMap.put("device_name", GeneralUtils.isTablet(context) ? "Tablet" : "Phone");
        if (GeneralUtils.isAmazonDevice()) {
            hashMap.put("device_type", "9");
        } else {
            hashMap.put("device_type", "3");
        }
        hashMap.put("v", "2.2");
        hashMap.put("language", Locale.getDefault().getLanguage());
        hashMap.put("timezone", Integer.valueOf(Calendar.getInstance().getTimeZone().getRawOffset() / 1000));
        hashMap.put("android_package", context.getApplicationContext().getPackageName());
        hashMap.put("push_token", str);
        try {
            hashMap.put("app_version", context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
        } catch (NameNotFoundException e) {
        }
        return hashMap;
    }

    public static Map<String, Object> getSendPushStatData(Context context, String str) {
        Map<String, Object> hashMap = new HashMap();
        hashMap.put("application", PreferenceUtils.getApplicationId(context));
        hashMap.put("hwid", GeneralUtils.getDeviceUUID(context));
        hashMap.put("hash", str);
        return hashMap;
    }
}
