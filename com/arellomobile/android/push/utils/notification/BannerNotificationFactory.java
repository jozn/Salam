package com.arellomobile.android.push.utils.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.widget.RemoteViews;
import com.arellomobile.android.push.preference.SoundType;
import com.arellomobile.android.push.preference.VibrateType;

public final class BannerNotificationFactory extends BaseNotificationFactory {
    public BannerNotificationFactory(Context context, Bundle bundle, String str, String str2, SoundType soundType, VibrateType vibrateType) {
        super(context, bundle, str, str2, soundType, vibrateType);
    }

    @SuppressLint({"NewApi"})
    final Notification generateNotificationInner(Context context, Bundle bundle, String str, String str2, String str3) {
        Notification notification = new Notification();
        int identifier = this.mContext.getResources().getIdentifier("notification", "layout", this.mContext.getPackageName());
        if (identifier == 0) {
            throw new IllegalArgumentException();
        }
        RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), identifier);
        Bitmap tryToGetBitmapFromInternet = Helper.tryToGetBitmapFromInternet(this.mData.getString("b"), this.mContext, -1);
        if (tryToGetBitmapFromInternet != null) {
            remoteViews.setBitmap(this.mContext.getResources().getIdentifier("image", "id", this.mContext.getPackageName()), "setImageBitmap", tryToGetBitmapFromInternet);
        } else {
            remoteViews.setBitmap(this.mContext.getResources().getIdentifier("image", "id", this.mContext.getPackageName()), "setImageBitmap", ((BitmapDrawable) this.mContext.getResources().getDrawable(this.mContext.getApplicationInfo().icon)).getBitmap());
        }
        notification.contentView = remoteViews;
        if (VERSION.SDK_INT >= 16) {
            notification.bigContentView = remoteViews;
        }
        if (VERSION.SDK_INT >= 11) {
            notification.tickerView = remoteViews;
        } else {
            notification.tickerText = str3;
        }
        notification.icon = Helper.tryToGetIconFormStringOrGetFromApplication(this.mData.getString("i"), this.mContext);
        return notification;
    }
}
