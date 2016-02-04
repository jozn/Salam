package com.arellomobile.android.push.utils.notification;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.text.Html;
import android.text.TextUtils;
import com.arellomobile.android.push.preference.SoundType;
import com.arellomobile.android.push.preference.VibrateType;
import com.pushwoosh.support.v4.app.NotificationCompat;
import com.pushwoosh.support.v4.app.NotificationCompat.BigTextStyle;
import com.pushwoosh.support.v4.app.NotificationCompat.Builder;
import com.pushwoosh.support.v4.app.NotificationCompat.Style;

public final class SimpleNotificationFactory extends BaseNotificationFactory {
    public SimpleNotificationFactory(Context context, Bundle bundle, String str, String str2, SoundType soundType, VibrateType vibrateType) {
        super(context, bundle, str, str2, soundType, vibrateType);
    }

    private static CharSequence getContent(String str) {
        return TextUtils.isEmpty(str) ? str : Html.fromHtml(str);
    }

    final Notification generateNotificationInner(Context context, Bundle bundle, String str, String str2, String str3) {
        int tryToGetIconFormStringOrGetFromApplication = Helper.tryToGetIconFormStringOrGetFromApplication(bundle.getString("i"), context);
        context.getResources();
        Bitmap bitmap = null;
        String string = bundle.getString("ci");
        if (string != null) {
            bitmap = Helper.tryToGetBitmapFromInternet(string, context, AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        }
        Builder builder = new Builder(context);
        builder.mContentTitle = getContent(str);
        builder.mContentText = getContent(str2);
        builder.mNotification.tickerText = getContent(str3);
        builder.mNotification.when = System.currentTimeMillis();
        Style bigTextStyle = new BigTextStyle();
        bigTextStyle.mBigText = getContent(str2);
        builder.setStyle(bigTextStyle);
        builder.mNotification.icon = tryToGetIconFormStringOrGetFromApplication;
        if (bitmap != null) {
            builder.mLargeIcon = bitmap;
        }
        return NotificationCompat.IMPL.build(builder);
    }
}
