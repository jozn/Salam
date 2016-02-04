package com.google.android.gcm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class GCMBaseIntentService extends IntentService {
    private static final Object LOCK;
    private static final int MAX_BACKOFF_MS;
    private static final String TOKEN;
    private static int sCounter;
    private static final Random sRandom;
    private static WakeLock sWakeLock;
    protected String mSenderId;

    static {
        LOCK = GCMBaseIntentService.class;
        sCounter = 0;
        sRandom = new Random();
        MAX_BACKOFF_MS = (int) TimeUnit.SECONDS.toMillis(3600);
        TOKEN = Long.toBinaryString(sRandom.nextLong());
    }

    public GCMBaseIntentService() {
        StringBuilder stringBuilder = new StringBuilder("GCMIntentService--");
        int i = sCounter + 1;
        sCounter = i;
        super(stringBuilder.append(i).toString());
    }

    static void runIntentInService(Context context, Intent intent, String str) {
        synchronized (LOCK) {
            if (sWakeLock == null) {
                sWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "GCM_LIB");
            }
        }
        Log.v("GCMBaseIntentService", "Acquiring wakelock");
        sWakeLock.acquire();
        intent.setClassName(context, str);
        context.startService(intent);
    }

    public void onDeletedMessages$1a54e370() {
    }

    public abstract void onError(Context context, String str);

    public final void onHandleIntent(Intent intent) {
        Object obj = 1;
        try {
            Context applicationContext = getApplicationContext();
            String action = intent.getAction();
            String stringExtra;
            Intent intent2;
            if (action.equals("com.google.android.c2dm.intent.REGISTRATION")) {
                stringExtra = intent.getStringExtra("registration_id");
                String stringExtra2 = intent.getStringExtra(MqttServiceConstants.TRACE_ERROR);
                action = intent.getStringExtra("unregistered");
                Log.d("GCMBaseIntentService", "handleRegistration: registrationId = " + stringExtra + ", error = " + stringExtra2 + ", unregistered = " + action);
                if (stringExtra != null) {
                    GCMRegistrar.resetBackoff(applicationContext);
                    GCMRegistrar.setRegistrationId(applicationContext, stringExtra);
                    onRegistered(applicationContext, stringExtra);
                } else if (action != null) {
                    GCMRegistrar.resetBackoff(applicationContext);
                    onUnregistered(applicationContext, GCMRegistrar.setRegistrationId(applicationContext, BuildConfig.VERSION_NAME));
                } else {
                    Log.d("GCMBaseIntentService", "Registration error: " + stringExtra2);
                    if (!"SERVICE_NOT_AVAILABLE".equals(stringExtra2)) {
                        onError(applicationContext, stringExtra2);
                    } else if (onRecoverableError(applicationContext, stringExtra2)) {
                        int i = applicationContext.getSharedPreferences("com.google.android.gcm", 0).getInt("backoff_ms", 3000);
                        int nextInt = sRandom.nextInt(i) + (i / 2);
                        Log.d("GCMBaseIntentService", "Scheduling registration retry, backoff = " + nextInt + " (" + i + ")");
                        intent2 = new Intent("com.google.android.gcm.intent.RETRY");
                        intent2.putExtra("token", TOKEN);
                        ((AlarmManager) applicationContext.getSystemService(NotificationCompatApi21.CATEGORY_ALARM)).set(3, SystemClock.elapsedRealtime() + ((long) nextInt), PendingIntent.getBroadcast(applicationContext, 0, intent2, 0));
                        if (i < MAX_BACKOFF_MS) {
                            GCMRegistrar.setBackoff(applicationContext, i * 2);
                        }
                    } else {
                        Log.d("GCMBaseIntentService", "Not retrying failed operation");
                    }
                }
            } else if (action.equals("com.google.android.c2dm.intent.RECEIVE")) {
                stringExtra = intent.getStringExtra("message_type");
                if (stringExtra == null) {
                    WakeLock wakeLock = null;
                    if (applicationContext.getSharedPreferences("com.pushwoosh.pushnotifications", 0).getBoolean("dm_lightson", false)) {
                        wakeLock = ((PowerManager) getSystemService("power")).newWakeLock(268435462, "GCM_MESSAGE_ALERT_LOCK");
                        wakeLock.acquire();
                    }
                    onMessage(applicationContext, intent);
                    if (wakeLock != null) {
                        wakeLock.release();
                    }
                } else if (stringExtra.equals("deleted_messages")) {
                    stringExtra = intent.getStringExtra("total_deleted");
                    if (stringExtra != null) {
                        try {
                            Log.v("GCMBaseIntentService", "Received deleted messages notification: " + Integer.parseInt(stringExtra));
                            onDeletedMessages$1a54e370();
                        } catch (NumberFormatException e) {
                            Log.e("GCMBaseIntentService", "GCM returned invalid number of deleted messages: " + stringExtra);
                        }
                    }
                } else {
                    Log.e("GCMBaseIntentService", "Received unknown special message: " + stringExtra);
                }
            } else if (action.equals("com.google.android.gcm.intent.RETRY")) {
                action = intent.getStringExtra("token");
                if (TOKEN.equals(action)) {
                    if (GCMRegistrar.getRegistrationId(applicationContext).length() <= 0) {
                        obj = null;
                    }
                    if (obj != null) {
                        Log.v("GCMRegistrar", "Unregistering app " + applicationContext.getPackageName());
                        intent2 = new Intent("com.google.android.c2dm.intent.UNREGISTER");
                        intent2.setPackage("com.google.android.gsf");
                        intent2.putExtra("app", PendingIntent.getBroadcast(applicationContext, 0, new Intent(), 0));
                        applicationContext.startService(intent2);
                    } else {
                        GCMRegistrar.internalRegister(applicationContext, this.mSenderId);
                    }
                } else {
                    Log.e("GCMBaseIntentService", "Received invalid token: " + action);
                    try {
                        synchronized (LOCK) {
                            if (sWakeLock != null) {
                                Log.v("GCMBaseIntentService", "Releasing wakelock");
                                sWakeLock.release();
                            } else {
                                Log.e("GCMBaseIntentService", "Wakelock reference is null");
                            }
                        }
                        return;
                    } catch (Exception e2) {
                        return;
                    }
                }
            }
            try {
                synchronized (LOCK) {
                    if (sWakeLock != null) {
                        Log.v("GCMBaseIntentService", "Releasing wakelock");
                        sWakeLock.release();
                    } else {
                        Log.e("GCMBaseIntentService", "Wakelock reference is null");
                    }
                }
            } catch (Exception e3) {
            }
        } catch (Throwable th) {
            try {
                synchronized (LOCK) {
                }
                if (sWakeLock != null) {
                    Log.v("GCMBaseIntentService", "Releasing wakelock");
                    sWakeLock.release();
                } else {
                    Log.e("GCMBaseIntentService", "Wakelock reference is null");
                }
            } catch (Exception e4) {
            }
        }
    }

    public abstract void onMessage(Context context, Intent intent);

    public boolean onRecoverableError(Context context, String str) {
        return true;
    }

    public abstract void onRegistered(Context context, String str);

    public abstract void onUnregistered(Context context, String str);
}
