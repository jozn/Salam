package com.google.android.gms.common;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v7.appcompat.BuildConfig;
import android.support.v7.appcompat.C0170R;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import com.google.android.gms.C0237R;
import com.google.android.gms.common.internal.zzn;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class GoogleApiAvailability {
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE;
    private static final GoogleApiAvailability zzadU;

    static {
        GOOGLE_PLAY_SERVICES_VERSION_CODE = GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        zzadU = new GoogleApiAvailability();
    }

    GoogleApiAvailability() {
    }

    public static GoogleApiAvailability getInstance() {
        return zzadU;
    }

    public static int isGooglePlayServicesAvailable(Context context) {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        return GooglePlayServicesUtil.zzd(context, isGooglePlayServicesAvailable) ? 18 : isGooglePlayServicesAvailable;
    }

    public static boolean isUserResolvableError(int errorCode) {
        return GooglePlayServicesUtil.isUserRecoverableError(errorCode);
    }

    public static Dialog zza(Activity activity, OnCancelListener onCancelListener) {
        View progressBar = new ProgressBar(activity, null, 16842874);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(0);
        Builder builder = new Builder(activity);
        builder.setView(progressBar);
        String zzam = GooglePlayServicesUtil.zzam(activity);
        builder.setMessage(activity.getResources().getString(C0237R.string.common_google_play_services_updating_text, new Object[]{zzam}));
        builder.setTitle(C0237R.string.common_google_play_services_updating_title);
        builder.setPositiveButton(BuildConfig.VERSION_NAME, null);
        Dialog create = builder.create();
        GooglePlayServicesUtil.zza(activity, onCancelListener, "GooglePlayServicesUpdatingDialog", create);
        return create;
    }

    public static Intent zza(Context context, int i, String str) {
        switch (i) {
            case Logger.SEVERE /*1*/:
            case Logger.WARNING /*2*/:
                return zzn.zzy("com.google.android.gms", zzi(context, str));
            case Logger.INFO /*3*/:
                return zzn.zzcD("com.google.android.gms");
            case C0170R.styleable.Theme_dialogTheme /*42*/:
                return zzn.zzqE();
            default:
                return null;
        }
    }

    public static boolean zzh(Context context, String str) {
        return GooglePlayServicesUtil.zzh(context, str);
    }

    private static String zzi(Context context, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("gcore_");
        stringBuilder.append(GOOGLE_PLAY_SERVICES_VERSION_CODE);
        stringBuilder.append("-");
        if (!TextUtils.isEmpty(str)) {
            stringBuilder.append(str);
        }
        stringBuilder.append("-");
        if (context != null) {
            stringBuilder.append(context.getPackageName());
        }
        stringBuilder.append("-");
        if (context != null) {
            try {
                stringBuilder.append(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
            } catch (NameNotFoundException e) {
            }
        }
        return stringBuilder.toString();
    }
}
