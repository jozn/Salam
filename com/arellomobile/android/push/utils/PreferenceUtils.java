package com.arellomobile.android.push.utils;

import android.content.Context;
import android.support.v7.appcompat.BuildConfig;
import com.arellomobile.android.push.preference.SoundType;
import com.arellomobile.android.push.preference.VibrateType;

public final class PreferenceUtils {
    public static String getApplicationId(Context context) {
        return context.getSharedPreferences("com.pushwoosh.pushnotifications", 0).getString("dm_pwapp", BuildConfig.VERSION_NAME);
    }

    public static int getMessageId(Context context) {
        return context.getSharedPreferences("com.pushwoosh.pushnotifications", 0).getInt("dm_messageid", 1001);
    }

    public static String getSenderId(Context context) {
        return context.getSharedPreferences("com.pushwoosh.pushnotifications", 0).getString("dm_sender_id", BuildConfig.VERSION_NAME);
    }

    public static SoundType getSoundType(Context context) {
        return SoundType.fromInt(Integer.valueOf(context.getSharedPreferences("com.pushwoosh.pushnotifications", 0).getInt("dm_soundtype", 0)).intValue());
    }

    public static VibrateType getVibrateType(Context context) {
        return VibrateType.fromInt(Integer.valueOf(context.getSharedPreferences("com.pushwoosh.pushnotifications", 0).getInt("dm_vibratetype", 0)).intValue());
    }
}
