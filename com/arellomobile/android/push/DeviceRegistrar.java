package com.arellomobile.android.push;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import com.arellomobile.android.push.request.RequestHelper;
import com.arellomobile.android.push.utils.NetworkUtils;
import com.arellomobile.android.push.utils.NetworkUtils.NetworkResult;
import com.google.android.gcm.GCMRegistrar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class DeviceRegistrar {
    static NetworkResult makeRequest(Context context, String str, String str2) throws Exception {
        Map hashMap = new HashMap();
        hashMap.putAll(RequestHelper.getRegistrationUnregistrationData(context, str));
        return NetworkUtils.makeRequest(hashMap, str2);
    }

    static void registerWithServer(Context context, String str) {
        Log.w("DeviceRegistrar", "Try To Registered for pushes");
        Throwable exception = new Exception();
        NetworkResult networkResult = null;
        int i = 0;
        while (i < 5) {
            try {
                networkResult = makeRequest(context, str, "registerDevice");
                if (200 != networkResult.mResultCode) {
                    continue;
                    i++;
                } else if (200 != networkResult.mPushwooshCode) {
                    break;
                } else {
                    GCMRegistrar.setRegisteredOnServer(context, true);
                    PushEventsTransmitter.onRegistered(context, str);
                    long time = new Date().getTime();
                    Editor edit = context.getSharedPreferences("com.pushwoosh.pushnotifications", 0).edit();
                    edit.putLong("last_registration_change", time);
                    edit.commit();
                    Log.w("DeviceRegistrar", "Registered for pushes: " + str);
                    return;
                }
            } catch (Exception e) {
                exception = e;
            }
        }
        if (exception.getMessage() != null) {
            PushEventsTransmitter.onRegisterError(context, exception.getMessage());
            Log.e("DeviceRegistrar", "Registration error " + exception.getMessage(), exception);
            return;
        }
        Log.e("DeviceRegistrar", "Registration error " + networkResult.mResultData.toString());
        PushEventsTransmitter.onRegisterError(context, networkResult.mResultData.toString());
    }
}
