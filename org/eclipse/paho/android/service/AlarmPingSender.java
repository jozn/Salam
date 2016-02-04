package org.eclipse.paho.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.internal.ClientComms;

class AlarmPingSender implements MqttPingSender {
    static final String TAG = "AlarmPingSender";
    private BroadcastReceiver alarmReceiver;
    private ClientComms comms;
    private volatile boolean hasStarted;
    private PendingIntent pendingIntent;
    private MqttService service;
    private AlarmPingSender that;

    class AlarmReceiver extends BroadcastReceiver {
        private String wakeLockTag;
        private WakeLock wakelock;

        /* renamed from: org.eclipse.paho.android.service.AlarmPingSender.AlarmReceiver.1 */
        class C12651 implements IMqttActionListener {
            C12651() {
            }

            public void onSuccess(IMqttToken asyncActionToken) {
                Log.d(AlarmPingSender.TAG, "Success. Release lock(" + AlarmReceiver.this.wakeLockTag + "):" + System.currentTimeMillis());
                if (AlarmReceiver.this.wakelock != null && AlarmReceiver.this.wakelock.isHeld()) {
                    AlarmReceiver.this.wakelock.release();
                }
            }

            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.d(AlarmPingSender.TAG, "Failure. Release lock(" + AlarmReceiver.this.wakeLockTag + "):" + System.currentTimeMillis());
                if (AlarmReceiver.this.wakelock != null && AlarmReceiver.this.wakelock.isHeld()) {
                    AlarmReceiver.this.wakelock.release();
                }
            }
        }

        AlarmReceiver() {
            this.wakeLockTag = new StringBuilder(MqttServiceConstants.PING_WAKELOCK).append(AlarmPingSender.this.that.comms.getClient().getClientId()).toString();
        }

        public void onReceive(Context context, Intent intent) {
            Log.d(AlarmPingSender.TAG, "Ping " + intent.getIntExtra("android.intent.extra.ALARM_COUNT", -1) + " times.");
            Log.d(AlarmPingSender.TAG, "Check time :" + System.currentTimeMillis());
            IMqttToken token = AlarmPingSender.this.comms.checkForActivity();
            if (token != null) {
                if (this.wakelock == null) {
                    this.wakelock = ((PowerManager) AlarmPingSender.this.service.getSystemService("power")).newWakeLock(1, this.wakeLockTag);
                }
                this.wakelock.acquire();
                token.setActionCallback(new C12651());
            }
        }
    }

    public AlarmPingSender(MqttService service) {
        this.hasStarted = false;
        if (service == null) {
            throw new IllegalArgumentException("Neither service nor client can be null.");
        }
        this.service = service;
        this.that = this;
    }

    public void init(ClientComms comms) {
        this.comms = comms;
        this.alarmReceiver = new AlarmReceiver();
    }

    public void start() {
        String action = new StringBuilder(MqttServiceConstants.PING_SENDER).append(this.comms.getClient().getClientId()).toString();
        Log.d(TAG, "Register alarmreceiver to MqttService" + action);
        this.service.registerReceiver(this.alarmReceiver, new IntentFilter(action));
        this.pendingIntent = PendingIntent.getBroadcast(this.service, 0, new Intent(action), 134217728);
        schedule(this.comms.getKeepAlive());
        this.hasStarted = true;
    }

    public void stop() {
        ((AlarmManager) this.service.getSystemService(NotificationCompatApi21.CATEGORY_ALARM)).cancel(this.pendingIntent);
        Log.d(TAG, "Unregister alarmreceiver to MqttService" + this.comms.getClient().getClientId());
        if (this.hasStarted) {
            this.hasStarted = false;
            try {
                this.service.unregisterReceiver(this.alarmReceiver);
            } catch (IllegalArgumentException e) {
            }
        }
    }

    public void schedule(long delayInMilliseconds) {
        long nextAlarmInMilliseconds = System.currentTimeMillis() + delayInMilliseconds;
        Log.d(TAG, "Schedule next alarm at " + nextAlarmInMilliseconds);
        ((AlarmManager) this.service.getSystemService(NotificationCompatApi21.CATEGORY_ALARM)).set(0, nextAlarmInMilliseconds, this.pendingIntent);
    }
}
