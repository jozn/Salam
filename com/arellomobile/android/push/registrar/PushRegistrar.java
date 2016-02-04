package com.arellomobile.android.push.registrar;

import android.content.Context;

public interface PushRegistrar {
    void checkDevice(Context context);

    void registerPW(Context context);
}
