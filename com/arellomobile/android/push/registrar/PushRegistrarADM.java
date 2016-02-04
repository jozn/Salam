package com.arellomobile.android.push.registrar;

import android.content.Context;
import com.amazon.device.messaging.ADM;
import com.arellomobile.android.push.ADMRegistrar;
import com.arellomobile.android.push.utils.GeneralUtils;
import com.arellomobile.android.push.utils.PreferenceUtils;

public final class PushRegistrarADM implements PushRegistrar {
    private final ADM mAdm;

    public PushRegistrarADM(Context context) {
        this.mAdm = new ADM(context);
    }

    public final void checkDevice(Context context) {
        GeneralUtils.checkNotNullOrEmpty(PreferenceUtils.getApplicationId(context), "mAppId");
        if (this.mAdm.isSupported()) {
            ADMRegistrar.checkManifest(context);
            return;
        }
        throw new UnsupportedOperationException("ADM is not supported on the current device");
    }

    public final void registerPW(Context context) {
        this.mAdm.startRegister();
    }
}
