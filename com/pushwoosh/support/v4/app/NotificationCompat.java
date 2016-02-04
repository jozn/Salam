package com.pushwoosh.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;

public final class NotificationCompat {
    private static final NotificationCompatImpl IMPL;

    public static class Action {
        public PendingIntent actionIntent;
        public int icon;
        public CharSequence title;
    }

    public static abstract class Style {
        CharSequence mBigContentTitle;
        Builder mBuilder;
        CharSequence mSummaryText;
        boolean mSummaryTextSet;

        public Style() {
            this.mSummaryTextSet = false;
        }
    }

    public static class BigPictureStyle extends Style {
        Bitmap mPicture;
    }

    public static class BigTextStyle extends Style {
        public CharSequence mBigText;
    }

    public static class Builder {
        ArrayList<Action> mActions;
        CharSequence mContentInfo;
        PendingIntent mContentIntent;
        public CharSequence mContentText;
        public CharSequence mContentTitle;
        Context mContext;
        PendingIntent mFullScreenIntent;
        public Bitmap mLargeIcon;
        public Notification mNotification;
        int mNumber;
        int mPriority;
        int mProgress;
        boolean mProgressIndeterminate;
        int mProgressMax;
        Style mStyle;
        CharSequence mSubText;
        RemoteViews mTickerView;
        boolean mUseChronometer;

        public Builder(Context context) {
            this.mActions = new ArrayList();
            this.mNotification = new Notification();
            this.mContext = context;
            this.mNotification.when = System.currentTimeMillis();
            this.mNotification.audioStreamType = -1;
            this.mPriority = 0;
        }

        public final Builder setStyle(Style style) {
            if (this.mStyle != style) {
                this.mStyle = style;
                if (this.mStyle != null) {
                    Style style2 = this.mStyle;
                    if (style2.mBuilder != this) {
                        style2.mBuilder = this;
                        if (style2.mBuilder != null) {
                            style2.mBuilder.setStyle(style2);
                        }
                    }
                }
            }
            return this;
        }
    }

    public static class InboxStyle extends Style {
        ArrayList<CharSequence> mTexts;

        public InboxStyle() {
            this.mTexts = new ArrayList();
        }
    }

    interface NotificationCompatImpl {
        Notification build(Builder builder);
    }

    static class NotificationCompatImplBase implements NotificationCompatImpl {
        NotificationCompatImplBase() {
        }

        public final Notification build(Builder builder) {
            Notification notification = builder.mNotification;
            notification.setLatestEventInfo(builder.mContext, builder.mContentTitle, builder.mContentText, builder.mContentIntent);
            if (builder.mPriority > 0) {
                notification.flags |= AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS;
            }
            return notification;
        }
    }

    static class NotificationCompatImplHoneycomb implements NotificationCompatImpl {
        NotificationCompatImplHoneycomb() {
        }

        public final Notification build(Builder builder) {
            Context context = builder.mContext;
            Notification notification = builder.mNotification;
            CharSequence charSequence = builder.mContentTitle;
            CharSequence charSequence2 = builder.mContentText;
            CharSequence charSequence3 = builder.mContentInfo;
            RemoteViews remoteViews = builder.mTickerView;
            int i = builder.mNumber;
            PendingIntent pendingIntent = builder.mContentIntent;
            return new android.app.Notification.Builder(context).setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setOngoing((notification.flags & 2) != 0).setOnlyAlertOnce((notification.flags & 8) != 0).setAutoCancel((notification.flags & 16) != 0).setDefaults(notification.defaults).setContentTitle(charSequence).setContentText(charSequence2).setContentInfo(charSequence3).setContentIntent(pendingIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(builder.mFullScreenIntent, (notification.flags & AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) != 0).setLargeIcon(builder.mLargeIcon).setNumber(i).getNotification();
        }
    }

    static class NotificationCompatImplIceCreamSandwich implements NotificationCompatImpl {
        NotificationCompatImplIceCreamSandwich() {
        }

        public final Notification build(Builder builder) {
            Context context = builder.mContext;
            Notification notification = builder.mNotification;
            CharSequence charSequence = builder.mContentTitle;
            CharSequence charSequence2 = builder.mContentText;
            CharSequence charSequence3 = builder.mContentInfo;
            RemoteViews remoteViews = builder.mTickerView;
            int i = builder.mNumber;
            PendingIntent pendingIntent = builder.mContentIntent;
            PendingIntent pendingIntent2 = builder.mFullScreenIntent;
            return new android.app.Notification.Builder(context).setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setOngoing((notification.flags & 2) != 0).setOnlyAlertOnce((notification.flags & 8) != 0).setAutoCancel((notification.flags & 16) != 0).setDefaults(notification.defaults).setContentTitle(charSequence).setContentText(charSequence2).setContentInfo(charSequence3).setContentIntent(pendingIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(pendingIntent2, (notification.flags & AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) != 0).setLargeIcon(builder.mLargeIcon).setNumber(i).setProgress(builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate).getNotification();
        }
    }

    static class NotificationCompatImplJellybean implements NotificationCompatImpl {
        NotificationCompatImplJellybean() {
        }

        public final Notification build(Builder builder) {
            NotificationCompatJellybean notificationCompatJellybean = new NotificationCompatJellybean(builder.mContext, builder.mNotification, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon, builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate, builder.mUseChronometer, builder.mPriority, builder.mSubText);
            Iterator it = builder.mActions.iterator();
            while (it.hasNext()) {
                Action action = (Action) it.next();
                notificationCompatJellybean.f8b.addAction(action.icon, action.title, action.actionIntent);
            }
            if (builder.mStyle != null) {
                CharSequence charSequence;
                boolean z;
                CharSequence charSequence2;
                if (builder.mStyle instanceof BigTextStyle) {
                    BigTextStyle bigTextStyle = (BigTextStyle) builder.mStyle;
                    charSequence = bigTextStyle.mBigContentTitle;
                    z = bigTextStyle.mSummaryTextSet;
                    charSequence2 = bigTextStyle.mSummaryText;
                    android.app.Notification.BigTextStyle bigText = new android.app.Notification.BigTextStyle(notificationCompatJellybean.f8b).setBigContentTitle(charSequence).bigText(bigTextStyle.mBigText);
                    if (z) {
                        bigText.setSummaryText(charSequence2);
                    }
                } else if (builder.mStyle instanceof InboxStyle) {
                    InboxStyle inboxStyle = (InboxStyle) builder.mStyle;
                    notificationCompatJellybean.addInboxStyle(inboxStyle.mBigContentTitle, inboxStyle.mSummaryTextSet, inboxStyle.mSummaryText, inboxStyle.mTexts);
                } else if (builder.mStyle instanceof BigPictureStyle) {
                    BigPictureStyle bigPictureStyle = (BigPictureStyle) builder.mStyle;
                    charSequence = bigPictureStyle.mBigContentTitle;
                    z = bigPictureStyle.mSummaryTextSet;
                    charSequence2 = bigPictureStyle.mSummaryText;
                    android.app.Notification.BigPictureStyle bigPicture = new android.app.Notification.BigPictureStyle(notificationCompatJellybean.f8b).setBigContentTitle(charSequence).bigPicture(bigPictureStyle.mPicture);
                    if (z) {
                        bigPicture.setSummaryText(charSequence2);
                    }
                }
            }
            return notificationCompatJellybean.f8b.build();
        }
    }

    static {
        if (VERSION.SDK_INT >= 16) {
            IMPL = new NotificationCompatImplJellybean();
        } else if (VERSION.SDK_INT >= 14) {
            IMPL = new NotificationCompatImplIceCreamSandwich();
        } else if (VERSION.SDK_INT >= 11) {
            IMPL = new NotificationCompatImplHoneycomb();
        } else {
            IMPL = new NotificationCompatImplBase();
        }
    }
}
