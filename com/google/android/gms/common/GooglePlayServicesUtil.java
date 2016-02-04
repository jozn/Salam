package com.google.android.gms.common;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.C0170R;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import com.google.android.gms.C0237R;
import com.google.android.gms.common.internal.zzd;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.common.internal.zzh;
import com.google.android.gms.internal.zznj;
import com.google.android.gms.internal.zznx;
import com.kyleduo.switchbutton.C0473R;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class GooglePlayServicesUtil {
    @Deprecated
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE;
    public static boolean zzaee;
    public static boolean zzaef;
    private static int zzaeg;
    private static String zzaeh;
    private static Integer zzaei;
    static final AtomicBoolean zzaej;
    private static final AtomicBoolean zzaek;
    private static final Object zzqf;

    static {
        GOOGLE_PLAY_SERVICES_VERSION_CODE = 8298000;
        zzaee = false;
        zzaef = false;
        zzaeg = -1;
        zzqf = new Object();
        zzaeh = null;
        zzaei = null;
        zzaej = new AtomicBoolean();
        zzaek = new AtomicBoolean();
    }

    public static Context getRemoteContext(Context context) {
        try {
            return context.createPackageContext("com.google.android.gms", 3);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    @Deprecated
    public static int isGooglePlayServicesAvailable(Context context) {
        if (zzd.zzaiU) {
            return 0;
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            context.getResources().getString(C0237R.string.common_google_play_services_unknown_issue);
        } catch (Throwable th) {
            Log.e("GooglePlayServicesUtil", "The Google Play services resources were not found. Check your project configuration to ensure that the resources are included.");
        }
        if (!("com.google.android.gms".equals(context.getPackageName()) || zzaek.get())) {
            Integer num;
            synchronized (zzqf) {
                if (zzaeh == null) {
                    zzaeh = context.getPackageName();
                    try {
                        Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS).metaData;
                        if (bundle != null) {
                            zzaei = Integer.valueOf(bundle.getInt("com.google.android.gms.version"));
                        } else {
                            zzaei = null;
                        }
                    } catch (Throwable e) {
                        Log.wtf("GooglePlayServicesUtil", "This should never happen.", e);
                    }
                } else if (!zzaeh.equals(context.getPackageName())) {
                    throw new IllegalArgumentException("isGooglePlayServicesAvailable should only be called with Context from your application's package. A previous call used package '" + zzaeh + "' and this call used package '" + context.getPackageName() + "'.");
                }
                num = zzaei;
            }
            if (num == null) {
                throw new IllegalStateException("A required meta-data tag in your app's AndroidManifest.xml does not exist.  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
            } else if (num.intValue() != GOOGLE_PLAY_SERVICES_VERSION_CODE) {
                throw new IllegalStateException("The meta-data tag in your app's AndroidManifest.xml does not have the right value.  Expected " + GOOGLE_PLAY_SERVICES_VERSION_CODE + " but found " + num + ".  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
            }
        }
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.google.android.gms", 64);
            zzd.zzox();
            if (!zznj.zzav(context)) {
                try {
                    if (zzd.zza(packageManager.getPackageInfo("com.android.vending", 8256), zzcg.zzaed) == null) {
                        Log.w("GooglePlayServicesUtil", "Google Play Store signature invalid.");
                        return 9;
                    }
                    if (zzd.zza(packageInfo, zzd.zza(packageManager.getPackageInfo("com.android.vending", 8256), zzcg.zzaed)) == null) {
                        Log.w("GooglePlayServicesUtil", "Google Play services signature invalid.");
                        return 9;
                    }
                } catch (NameNotFoundException e2) {
                    Log.w("GooglePlayServicesUtil", "Google Play Store is neither installed nor updating.");
                    return 9;
                }
            } else if (zzd.zza(packageInfo, zzcg.zzaed) == null) {
                Log.w("GooglePlayServicesUtil", "Google Play services signature invalid.");
                return 9;
            }
            if (zznj.zzcp(packageInfo.versionCode) < zznj.zzcp(GOOGLE_PLAY_SERVICES_VERSION_CODE)) {
                Log.w("GooglePlayServicesUtil", "Google Play services out of date.  Requires " + GOOGLE_PLAY_SERVICES_VERSION_CODE + " but found " + packageInfo.versionCode);
                return 2;
            }
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            if (applicationInfo == null) {
                try {
                    applicationInfo = packageManager.getApplicationInfo("com.google.android.gms", 0);
                } catch (Throwable e3) {
                    Log.wtf("GooglePlayServicesUtil", "Google Play services missing when getting application info.", e3);
                    return 1;
                }
            }
            return !applicationInfo.enabled ? 3 : 0;
        } catch (NameNotFoundException e4) {
            Log.w("GooglePlayServicesUtil", "Google Play services is missing.");
            return 1;
        }
    }

    @Deprecated
    public static boolean isUserRecoverableError(int errorCode) {
        switch (errorCode) {
            case Logger.SEVERE /*1*/:
            case Logger.WARNING /*2*/:
            case Logger.INFO /*3*/:
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                return true;
            default:
                return false;
        }
    }

    public static boolean showErrorDialogFragment$7f66c94e(int errorCode, Activity activity, Fragment fragment, OnCancelListener cancelListener) {
        Dialog dialog;
        CharSequence charSequence = null;
        if (errorCode == 0) {
            dialog = null;
        } else {
            Builder builder;
            Intent zza;
            OnClickListener com_google_android_gms_common_internal_zzh;
            CharSequence zzh;
            Resources resources;
            if (zznj.zzav(activity) && errorCode == 2) {
                errorCode = 42;
            }
            if (zznx.zzcr(14)) {
                TypedValue typedValue = new TypedValue();
                activity.getTheme().resolveAttribute(16843529, typedValue, true);
                if ("Theme.Dialog.Alert".equals(activity.getResources().getResourceEntryName(typedValue.resourceId))) {
                    builder = new Builder(activity, 5);
                    if (builder == null) {
                        builder = new Builder(activity);
                    }
                    builder.setMessage(zzg.zzc(activity, errorCode, zzam(activity)));
                    if (cancelListener != null) {
                        builder.setOnCancelListener(cancelListener);
                    }
                    GoogleApiAvailability.getInstance();
                    zza = GoogleApiAvailability.zza(activity, errorCode, "d");
                    com_google_android_gms_common_internal_zzh = fragment != null ? new zzh(activity, zza) : new zzh(fragment, zza);
                    zzh = zzg.zzh(activity, errorCode);
                    if (zzh != null) {
                        builder.setPositiveButton(zzh, com_google_android_gms_common_internal_zzh);
                    }
                    resources = activity.getResources();
                    switch (errorCode) {
                        case Logger.SEVERE /*1*/:
                            charSequence = resources.getString(C0237R.string.common_google_play_services_install_title);
                            break;
                        case Logger.WARNING /*2*/:
                            charSequence = resources.getString(C0237R.string.common_google_play_services_update_title);
                            break;
                        case Logger.INFO /*3*/:
                            charSequence = resources.getString(C0237R.string.common_google_play_services_enable_title);
                            break;
                        case Logger.CONFIG /*4*/:
                        case Logger.FINER /*6*/:
                            break;
                        case Logger.FINE /*5*/:
                            Log.e("GoogleApiAvailability", "An invalid account was specified when connecting. Please provide a valid account.");
                            charSequence = resources.getString(C0237R.string.common_google_play_services_invalid_account_title);
                            break;
                        case Logger.FINEST /*7*/:
                            Log.e("GoogleApiAvailability", "Network error occurred. Please retry request later.");
                            charSequence = resources.getString(C0237R.string.common_google_play_services_network_error_title);
                            break;
                        case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                            Log.e("GoogleApiAvailability", "Internal error occurred. Please see logs for detailed information");
                            break;
                        case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                            Log.e("GoogleApiAvailability", "Google Play services is invalid. Cannot recover.");
                            charSequence = resources.getString(C0237R.string.common_google_play_services_unsupported_title);
                            break;
                        case C0473R.styleable.SwitchButton_onColor /*10*/:
                            Log.e("GoogleApiAvailability", "Developer error occurred. Please see logs for detailed information");
                            break;
                        case C0473R.styleable.SwitchButton_offColor /*11*/:
                            Log.e("GoogleApiAvailability", "The application is not licensed to the user.");
                            break;
                        case C0473R.styleable.SwitchButton_measureFactor /*16*/:
                            Log.e("GoogleApiAvailability", "One of the API components you attempted to connect to is not available.");
                            break;
                        case C0473R.styleable.SwitchButton_insetLeft /*17*/:
                            Log.e("GoogleApiAvailability", "The specified account could not be signed in.");
                            charSequence = resources.getString(C0237R.string.common_google_play_services_sign_in_failed_title);
                            break;
                        case C0473R.styleable.SwitchButton_insetRight /*18*/:
                            charSequence = resources.getString(C0237R.string.common_google_play_services_updating_title);
                            break;
                        case C0170R.styleable.Theme_dialogTheme /*42*/:
                            charSequence = resources.getString(C0237R.string.common_android_wear_update_title);
                            break;
                        default:
                            Log.e("GoogleApiAvailability", "Unexpected error code " + errorCode);
                            break;
                    }
                    if (charSequence != null) {
                        builder.setTitle(charSequence);
                    }
                    dialog = builder.create();
                }
            }
            builder = null;
            if (builder == null) {
                builder = new Builder(activity);
            }
            builder.setMessage(zzg.zzc(activity, errorCode, zzam(activity)));
            if (cancelListener != null) {
                builder.setOnCancelListener(cancelListener);
            }
            GoogleApiAvailability.getInstance();
            zza = GoogleApiAvailability.zza(activity, errorCode, "d");
            if (fragment != null) {
            }
            zzh = zzg.zzh(activity, errorCode);
            if (zzh != null) {
                builder.setPositiveButton(zzh, com_google_android_gms_common_internal_zzh);
            }
            resources = activity.getResources();
            switch (errorCode) {
                case Logger.SEVERE /*1*/:
                    charSequence = resources.getString(C0237R.string.common_google_play_services_install_title);
                    break;
                case Logger.WARNING /*2*/:
                    charSequence = resources.getString(C0237R.string.common_google_play_services_update_title);
                    break;
                case Logger.INFO /*3*/:
                    charSequence = resources.getString(C0237R.string.common_google_play_services_enable_title);
                    break;
                case Logger.CONFIG /*4*/:
                case Logger.FINER /*6*/:
                    break;
                case Logger.FINE /*5*/:
                    Log.e("GoogleApiAvailability", "An invalid account was specified when connecting. Please provide a valid account.");
                    charSequence = resources.getString(C0237R.string.common_google_play_services_invalid_account_title);
                    break;
                case Logger.FINEST /*7*/:
                    Log.e("GoogleApiAvailability", "Network error occurred. Please retry request later.");
                    charSequence = resources.getString(C0237R.string.common_google_play_services_network_error_title);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    Log.e("GoogleApiAvailability", "Internal error occurred. Please see logs for detailed information");
                    break;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    Log.e("GoogleApiAvailability", "Google Play services is invalid. Cannot recover.");
                    charSequence = resources.getString(C0237R.string.common_google_play_services_unsupported_title);
                    break;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    Log.e("GoogleApiAvailability", "Developer error occurred. Please see logs for detailed information");
                    break;
                case C0473R.styleable.SwitchButton_offColor /*11*/:
                    Log.e("GoogleApiAvailability", "The application is not licensed to the user.");
                    break;
                case C0473R.styleable.SwitchButton_measureFactor /*16*/:
                    Log.e("GoogleApiAvailability", "One of the API components you attempted to connect to is not available.");
                    break;
                case C0473R.styleable.SwitchButton_insetLeft /*17*/:
                    Log.e("GoogleApiAvailability", "The specified account could not be signed in.");
                    charSequence = resources.getString(C0237R.string.common_google_play_services_sign_in_failed_title);
                    break;
                case C0473R.styleable.SwitchButton_insetRight /*18*/:
                    charSequence = resources.getString(C0237R.string.common_google_play_services_updating_title);
                    break;
                case C0170R.styleable.Theme_dialogTheme /*42*/:
                    charSequence = resources.getString(C0237R.string.common_android_wear_update_title);
                    break;
                default:
                    Log.e("GoogleApiAvailability", "Unexpected error code " + errorCode);
                    break;
            }
            if (charSequence != null) {
                builder.setTitle(charSequence);
            }
            dialog = builder.create();
        }
        if (dialog == null) {
            return false;
        }
        zza(activity, cancelListener, "GooglePlayServicesErrorDialog", dialog);
        return true;
    }

    public static void zza(Activity activity, OnCancelListener onCancelListener, String str, Dialog dialog) {
        if (activity instanceof FragmentActivity) {
            SupportErrorDialogFragment.newInstance(dialog, onCancelListener).show(((FragmentActivity) activity).getSupportFragmentManager(), str);
        } else if (zznx.zzcr(11)) {
            ErrorDialogFragment.newInstance(dialog, onCancelListener).show(activity.getFragmentManager(), str);
        } else {
            throw new RuntimeException("This Activity does not support Fragments.");
        }
    }

    public static String zzam(Context context) {
        Object obj = context.getApplicationInfo().name;
        if (!TextUtils.isEmpty(obj)) {
            return obj;
        }
        ApplicationInfo applicationInfo;
        String packageName = context.getPackageName();
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            applicationInfo = null;
        }
        return applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo).toString() : packageName;
    }

    private static boolean zzb(Context context, int i, String str) {
        if (zznx.zzcr(19)) {
            try {
                ((AppOpsManager) context.getSystemService("appops")).checkPackage(i, str);
                return true;
            } catch (SecurityException e) {
                return false;
            }
        }
        String[] packagesForUid = context.getPackageManager().getPackagesForUid(i);
        if (packagesForUid == null) {
            return false;
        }
        for (Object equals : packagesForUid) {
            if (str.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    private static boolean zzb(PackageManager packageManager) {
        boolean z = true;
        synchronized (zzqf) {
            if (zzaeg == -1) {
                try {
                    PackageInfo packageInfo = packageManager.getPackageInfo("com.google.android.gms", 64);
                    zzd.zzox();
                    if (zzd.zza(packageInfo, zzc.zzadW[1]) != null) {
                        zzaeg = 1;
                    } else {
                        zzaeg = 0;
                    }
                } catch (NameNotFoundException e) {
                    zzaeg = 0;
                }
            }
            if (zzaeg == 0) {
                z = false;
            }
        }
        return z;
    }

    @Deprecated
    public static Intent zzbv(int i) {
        GoogleApiAvailability.getInstance();
        return GoogleApiAvailability.zza(null, i, null);
    }

    public static boolean zzc(PackageManager packageManager) {
        if (!zzb(packageManager)) {
            if (zzaee ? zzaef : "user".equals(Build.TYPE)) {
                return false;
            }
        }
        return true;
    }

    @Deprecated
    public static boolean zzd(Context context, int i) {
        return i == 18 ? true : i == 1 ? zzh(context, "com.google.android.gms") : false;
    }

    public static boolean zze(Context context, int i) {
        if (zzb(context, i, "com.google.android.gms")) {
            zzd.zzox();
            if (zzd.zzb(context.getPackageManager(), "com.google.android.gms")) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean zzh(android.content.Context r5, java.lang.String r6) {
        /*
        r1 = 1;
        r2 = 0;
        r0 = 21;
        r0 = com.google.android.gms.internal.zznx.zzcr(r0);
        if (r0 == 0) goto L_0x0032;
    L_0x000a:
        r0 = r5.getPackageManager();
        r0 = r0.getPackageInstaller();
        r0 = r0.getAllSessions();
        r3 = r0.iterator();
    L_0x001a:
        r0 = r3.hasNext();
        if (r0 == 0) goto L_0x0032;
    L_0x0020:
        r0 = r3.next();
        r0 = (android.content.pm.PackageInstaller.SessionInfo) r0;
        r0 = r0.getAppPackageName();
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x001a;
    L_0x0030:
        r0 = r1;
    L_0x0031:
        return r0;
    L_0x0032:
        r0 = 18;
        r0 = com.google.android.gms.internal.zznx.zzcr(r0);
        if (r0 == 0) goto L_0x005e;
    L_0x003a:
        r0 = "user";
        r0 = r5.getSystemService(r0);
        r0 = (android.os.UserManager) r0;
        r3 = r5.getPackageName();
        r0 = r0.getApplicationRestrictions(r3);
        if (r0 == 0) goto L_0x005e;
    L_0x004c:
        r3 = "true";
        r4 = "restricted_profile";
        r0 = r0.getString(r4);
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x005e;
    L_0x005a:
        if (r1 == 0) goto L_0x0060;
    L_0x005c:
        r0 = r2;
        goto L_0x0031;
    L_0x005e:
        r1 = r2;
        goto L_0x005a;
    L_0x0060:
        r0 = r5.getPackageManager();
        r1 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r0 = r0.getApplicationInfo(r6, r1);	 Catch:{ NameNotFoundException -> 0x006d }
        r0 = r0.enabled;	 Catch:{ NameNotFoundException -> 0x006d }
        goto L_0x0031;
    L_0x006d:
        r0 = move-exception;
        r0 = r2;
        goto L_0x0031;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GooglePlayServicesUtil.zzh(android.content.Context, java.lang.String):boolean");
    }
}
