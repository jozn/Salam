package com.rokhgroup.mqtt;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;

public final class NotifySimple {
    private static int MessageID;

    static {
        MessageID = 0;
    }

    public static void notifcation$34410889(Context context, String messageString, Intent intent) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService("notification");
        long when = System.currentTimeMillis();
        CharSequence contentTitle = context.getString(2131493241);
        String ticker = contentTitle + " " + messageString;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 3, intent, 0);
        Builder notificationCompat = new Builder(context);
        notificationCompat.setAutoCancel(true).setContentTitle(contentTitle).setContentIntent(pendingIntent).setContentText(messageString).setTicker(ticker).setWhen(when).setSmallIcon(2130837905);
        mNotificationManager.notify(MessageID, notificationCompat.build());
        MessageID++;
    }
}
