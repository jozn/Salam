package com.arellomobile.android.push.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class RegisterBroadcastReceiver extends BroadcastReceiver {
    public final void onReceive(Context context, Intent intent) {
        onRegisterActionReceive$3b2d1350(intent);
        if (GeneralUtils.checkStickyBroadcastPermissions(context)) {
            context.removeStickyBroadcast(new Intent("com.arellomobile.android.push.REGISTER_BROAD_CAST_ACTION"));
        }
    }

    public abstract void onRegisterActionReceive$3b2d1350(Intent intent);
}
