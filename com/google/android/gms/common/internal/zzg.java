package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.appcompat.C0170R;
import com.google.android.gms.C0237R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzg {
    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String zzc(android.content.Context r6, int r7, java.lang.String r8) {
        /*
        r5 = 3;
        r1 = 1;
        r2 = 0;
        r3 = r6.getResources();
        switch(r7) {
            case 1: goto L_0x0011;
            case 2: goto L_0x007a;
            case 3: goto L_0x0064;
            case 5: goto L_0x00a4;
            case 7: goto L_0x009c;
            case 9: goto L_0x0090;
            case 16: goto L_0x00ac;
            case 17: goto L_0x00b8;
            case 18: goto L_0x006f;
            case 42: goto L_0x0085;
            default: goto L_0x000a;
        };
    L_0x000a:
        r0 = com.google.android.gms.C0237R.string.common_google_play_services_unknown_issue;
        r0 = r3.getString(r0);
    L_0x0010:
        return r0;
    L_0x0011:
        if (r3 == 0) goto L_0x0057;
    L_0x0013:
        r0 = r3.getConfiguration();
        r0 = r0.screenLayout;
        r0 = r0 & 15;
        if (r0 <= r5) goto L_0x0051;
    L_0x001d:
        r0 = r1;
    L_0x001e:
        r4 = 11;
        r4 = com.google.android.gms.internal.zznx.zzcr(r4);
        if (r4 == 0) goto L_0x0028;
    L_0x0026:
        if (r0 != 0) goto L_0x0043;
    L_0x0028:
        r0 = r3.getConfiguration();
        r4 = 13;
        r4 = com.google.android.gms.internal.zznx.zzcr(r4);
        if (r4 == 0) goto L_0x0055;
    L_0x0034:
        r4 = r0.screenLayout;
        r4 = r4 & 15;
        if (r4 > r5) goto L_0x0053;
    L_0x003a:
        r0 = r0.smallestScreenWidthDp;
        r4 = 600; // 0x258 float:8.41E-43 double:2.964E-321;
        if (r0 < r4) goto L_0x0053;
    L_0x0040:
        r0 = r1;
    L_0x0041:
        if (r0 == 0) goto L_0x0057;
    L_0x0043:
        r0 = r1;
    L_0x0044:
        if (r0 == 0) goto L_0x0059;
    L_0x0046:
        r0 = com.google.android.gms.C0237R.string.common_google_play_services_install_text_tablet;
        r1 = new java.lang.Object[r1];
        r1[r2] = r8;
        r0 = r3.getString(r0, r1);
        goto L_0x0010;
    L_0x0051:
        r0 = r2;
        goto L_0x001e;
    L_0x0053:
        r0 = r2;
        goto L_0x0041;
    L_0x0055:
        r0 = r2;
        goto L_0x0041;
    L_0x0057:
        r0 = r2;
        goto L_0x0044;
    L_0x0059:
        r0 = com.google.android.gms.C0237R.string.common_google_play_services_install_text_phone;
        r1 = new java.lang.Object[r1];
        r1[r2] = r8;
        r0 = r3.getString(r0, r1);
        goto L_0x0010;
    L_0x0064:
        r0 = com.google.android.gms.C0237R.string.common_google_play_services_enable_text;
        r1 = new java.lang.Object[r1];
        r1[r2] = r8;
        r0 = r3.getString(r0, r1);
        goto L_0x0010;
    L_0x006f:
        r0 = com.google.android.gms.C0237R.string.common_google_play_services_updating_text;
        r1 = new java.lang.Object[r1];
        r1[r2] = r8;
        r0 = r3.getString(r0, r1);
        goto L_0x0010;
    L_0x007a:
        r0 = com.google.android.gms.C0237R.string.common_google_play_services_update_text;
        r1 = new java.lang.Object[r1];
        r1[r2] = r8;
        r0 = r3.getString(r0, r1);
        goto L_0x0010;
    L_0x0085:
        r0 = com.google.android.gms.C0237R.string.common_android_wear_update_text;
        r1 = new java.lang.Object[r1];
        r1[r2] = r8;
        r0 = r3.getString(r0, r1);
        goto L_0x0010;
    L_0x0090:
        r0 = com.google.android.gms.C0237R.string.common_google_play_services_unsupported_text;
        r1 = new java.lang.Object[r1];
        r1[r2] = r8;
        r0 = r3.getString(r0, r1);
        goto L_0x0010;
    L_0x009c:
        r0 = com.google.android.gms.C0237R.string.common_google_play_services_network_error_text;
        r0 = r3.getString(r0);
        goto L_0x0010;
    L_0x00a4:
        r0 = com.google.android.gms.C0237R.string.common_google_play_services_invalid_account_text;
        r0 = r3.getString(r0);
        goto L_0x0010;
    L_0x00ac:
        r0 = com.google.android.gms.C0237R.string.common_google_play_services_api_unavailable_text;
        r1 = new java.lang.Object[r1];
        r1[r2] = r8;
        r0 = r3.getString(r0, r1);
        goto L_0x0010;
    L_0x00b8:
        r0 = com.google.android.gms.C0237R.string.common_google_play_services_sign_in_failed_text;
        r0 = r3.getString(r0);
        goto L_0x0010;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zzg.zzc(android.content.Context, int, java.lang.String):java.lang.String");
    }

    public static String zzh(Context context, int i) {
        Resources resources = context.getResources();
        switch (i) {
            case Logger.SEVERE /*1*/:
                return resources.getString(C0237R.string.common_google_play_services_install_button);
            case Logger.WARNING /*2*/:
            case C0170R.styleable.Theme_dialogTheme /*42*/:
                return resources.getString(C0237R.string.common_google_play_services_update_button);
            case Logger.INFO /*3*/:
                return resources.getString(C0237R.string.common_google_play_services_enable_button);
            default:
                return resources.getString(17039370);
        }
    }
}
