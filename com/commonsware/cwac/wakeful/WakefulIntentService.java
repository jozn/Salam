package com.commonsware.cwac.wakeful;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public abstract class WakefulIntentService extends IntentService {
    private static volatile WakeLock lockStatic;

    public interface AlarmListener {
    }

    public abstract void doWakefulWork(Intent intent);

    static {
        lockStatic = null;
    }

    private static synchronized WakeLock getLock(Context context) {
        WakeLock newWakeLock;
        synchronized (WakefulIntentService.class) {
            if (lockStatic == null) {
                newWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "com.commonsware.cwac.wakeful.WakefulIntentService");
                lockStatic = newWakeLock;
                newWakeLock.setReferenceCounted(true);
            }
            newWakeLock = lockStatic;
        }
        return newWakeLock;
    }

    public static void sendWakefulWork(Context ctxt, Intent i) {
        getLock(ctxt.getApplicationContext()).acquire();
        ctxt.startService(i);
    }

    public static void scheduleAlarms$779ace80(Context ctxt) {
        ctxt.getSharedPreferences("com.commonsware.cwac.wakeful.WakefulIntentService", 0).getLong("lastAlarm", 0);
        ctxt.getSystemService(NotificationCompatApi21.CATEGORY_ALARM);
        PendingIntent.getBroadcast(ctxt, 0, new Intent(ctxt, AlarmReceiver.class), 0);
    }

    public WakefulIntentService(String name) {
        super(name);
        setIntentRedelivery(true);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if ((flags & 1) != 0) {
            getLock(getApplicationContext()).acquire();
        }
        super.onStartCommand(intent, flags, startId);
        return 3;
    }

    protected final void onHandleIntent(Intent intent) {
        try {
            doWakefulWork(intent);
        } finally {
            getLock(getApplicationContext()).release();
        }
    }
}
