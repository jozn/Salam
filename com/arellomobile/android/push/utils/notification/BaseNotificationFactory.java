package com.arellomobile.android.push.utils.notification;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.arellomobile.android.push.preference.SoundType;
import com.arellomobile.android.push.preference.VibrateType;

public abstract class BaseNotificationFactory {
    public Context mContext;
    public Bundle mData;
    public String mHeader;
    public String mMessage;
    public Notification mNotification;
    public SoundType mSoundType;
    public VibrateType mVibrateType;

    public BaseNotificationFactory(Context context, Bundle bundle, String str, String str2, SoundType soundType, VibrateType vibrateType) {
        this.mContext = context;
        this.mData = bundle;
        this.mHeader = str;
        this.mMessage = str2;
        this.mSoundType = soundType;
        this.mVibrateType = vibrateType;
    }

    public static boolean phoneHaveVibratePermission(Context context) {
        try {
            if (context.getPackageManager().checkPermission("android.permission.VIBRATE", context.getPackageName()) == 0) {
                return true;
            }
        } catch (Throwable e) {
            Log.e("PushWoosh", "error in checking vibrate permission", e);
        }
        return false;
    }

    public abstract Notification generateNotificationInner(Context context, Bundle bundle, String str, String str2, String str3);
}
