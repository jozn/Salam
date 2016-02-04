package com.arellomobile.android.push.registrar;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import com.arellomobile.android.push.utils.GeneralUtils;
import com.arellomobile.android.push.utils.PreferenceUtils;
import com.google.android.gcm.GCMRegistrar;

public final class PushRegistrarGCM implements PushRegistrar {
    public final void checkDevice(Context context) {
        String applicationId = PreferenceUtils.getApplicationId(context);
        String senderId = PreferenceUtils.getSenderId(context);
        GeneralUtils.checkNotNullOrEmpty(applicationId, "mAppId");
        GeneralUtils.checkNotNullOrEmpty(senderId, "mSenderId");
        int i = VERSION.SDK_INT;
        if (i < 8) {
            throw new UnsupportedOperationException("Device must be at least API Level 8 (instead of " + i + ")");
        }
        try {
            context.getPackageManager().getPackageInfo("com.google.android.gsf", 0);
            GCMRegistrar.checkManifest(context);
        } catch (NameNotFoundException e) {
            throw new UnsupportedOperationException("Device does not have package com.google.android.gsf");
        }
    }

    public final void registerPW(Context context) {
        String[] strArr = new String[]{PreferenceUtils.getSenderId(context)};
        GCMRegistrar.resetBackoff(context);
        GCMRegistrar.internalRegister(context, strArr);
    }
}
