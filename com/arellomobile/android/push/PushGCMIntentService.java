package com.arellomobile.android.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import com.arellomobile.android.push.preference.SoundType;
import com.arellomobile.android.push.preference.VibrateType;
import com.arellomobile.android.push.utils.GeneralUtils;
import com.arellomobile.android.push.utils.NetworkUtils.NetworkResult;
import com.arellomobile.android.push.utils.PreferenceUtils;
import com.arellomobile.android.push.utils.notification.BannerNotificationFactory;
import com.arellomobile.android.push.utils.notification.BaseNotificationFactory;
import com.arellomobile.android.push.utils.notification.SimpleNotificationFactory;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;

public class PushGCMIntentService extends GCMBaseIntentService {
    protected final void onDeletedMessages$1a54e370() {
        Log.i("GCMIntentService", "Received deleted messages notification");
    }

    protected final void onError(Context context, String str) {
        Log.e("GCMIntentService", "Messaging registration error: " + str);
        PushEventsTransmitter.onRegisterError(context, str);
    }

    protected final void onMessage(Context context, Intent intent) {
        Log.i("GCMIntentService", "Received message");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Intent intent2;
            String obj;
            Notification notification;
            extras.putBoolean("foreground", GeneralUtils.isAppOnForeground(context));
            extras.putBoolean("onStart", !GeneralUtils.isAppOnForeground(context));
            String str = (String) extras.get("title");
            String str2 = (String) extras.get("header");
            String str3 = (String) extras.get("l");
            if (str3 != null) {
                Intent intent3 = new Intent("android.intent.action.VIEW", Uri.parse(str3));
                intent3.addFlags(ClientDefaults.MAX_MSG_SIZE);
                intent2 = intent3;
            } else {
                Intent intent4 = new Intent(context, PushHandlerActivity.class);
                intent4.addFlags(603979776);
                intent4.putExtra("pushBundle", extras);
                intent2 = intent4;
            }
            if (str2 == null) {
                Object applicationLabel = context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
                if (applicationLabel == null) {
                    applicationLabel = BuildConfig.VERSION_NAME;
                }
                obj = applicationLabel.toString();
            } else {
                obj = str2;
            }
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            BaseNotificationFactory simpleNotificationFactory = (context.getResources().getIdentifier("notification", "layout", context.getPackageName()) == 0 || ((String) extras.get("b")) == null) ? new SimpleNotificationFactory(context, extras, obj, str, PreferenceUtils.getSoundType(context), PreferenceUtils.getVibrateType(context)) : new BannerNotificationFactory(context, extras, obj, str, PreferenceUtils.getSoundType(context), PreferenceUtils.getVibrateType(context));
            int identifier = simpleNotificationFactory.mContext.getResources().getIdentifier("new_push_message", "string", simpleNotificationFactory.mContext.getPackageName());
            if (identifier != 0) {
                simpleNotificationFactory.mNotification = simpleNotificationFactory.generateNotificationInner(simpleNotificationFactory.mContext, simpleNotificationFactory.mData, simpleNotificationFactory.mHeader, simpleNotificationFactory.mMessage, simpleNotificationFactory.mContext.getString(identifier));
            } else {
                simpleNotificationFactory.mNotification = simpleNotificationFactory.generateNotificationInner(simpleNotificationFactory.mContext, simpleNotificationFactory.mData, simpleNotificationFactory.mHeader, simpleNotificationFactory.mMessage, simpleNotificationFactory.mMessage);
            }
            str2 = (String) simpleNotificationFactory.mData.get("s");
            AudioManager audioManager = (AudioManager) simpleNotificationFactory.mContext.getSystemService("audio");
            if (simpleNotificationFactory.mSoundType == SoundType.ALWAYS || (audioManager.getRingerMode() == 2 && simpleNotificationFactory.mSoundType == SoundType.DEFAULT_MODE)) {
                Context context2 = simpleNotificationFactory.mContext;
                Notification notification2 = simpleNotificationFactory.mNotification;
                if (!(str2 == null || str2.length() == 0)) {
                    identifier = context2.getResources().getIdentifier(str2, "raw", context2.getPackageName());
                    if (identifier != 0) {
                        notification2.sound = Uri.parse("android.resource://" + context2.getPackageName() + MqttTopic.TOPIC_LEVEL_SEPARATOR + identifier);
                    }
                }
                notification2.defaults |= 1;
            }
            if ((simpleNotificationFactory.mVibrateType == VibrateType.ALWAYS || (audioManager.getRingerMode() == 1 && simpleNotificationFactory.mVibrateType == VibrateType.DEFAULT_MODE)) && BaseNotificationFactory.phoneHaveVibratePermission(simpleNotificationFactory.mContext)) {
                notification = simpleNotificationFactory.mNotification;
                notification.defaults |= 2;
            }
            notification = simpleNotificationFactory.mNotification;
            notification.flags |= 16;
            if (context.getSharedPreferences("com.pushwoosh.pushnotifications", 0).getBoolean("dm_ledon", false)) {
                simpleNotificationFactory.mNotification.ledARGB = -1;
                notification = simpleNotificationFactory.mNotification;
                notification.flags |= 1;
                simpleNotificationFactory.mNotification.ledOnMS = 100;
                simpleNotificationFactory.mNotification.ledOffMS = 1000;
            }
            Notification notification3 = simpleNotificationFactory.mNotification;
            identifier = PreferenceUtils.getMessageId(context);
            if (context.getSharedPreferences("com.pushwoosh.pushnotifications", 0).getBoolean("dm_multimode", false)) {
                identifier++;
                Editor edit = context.getSharedPreferences("com.pushwoosh.pushnotifications", 0).edit();
                edit.putInt("dm_messageid", identifier);
                edit.commit();
            }
            notification3.contentIntent = PendingIntent.getActivity(context, identifier, intent2, 134217728);
            notificationManager.notify(identifier, notification3);
            PushServiceHelper.generateBroadcast(context, extras);
            DeviceFeature2_5.sendMessageDeliveryEvent(context, extras.getString("p"));
        }
    }

    protected final boolean onRecoverableError(Context context, String str) {
        Log.i("GCMIntentService", "Received recoverable error: " + str);
        return super.onRecoverableError(context, str);
    }

    protected final void onRegistered(Context context, String str) {
        Log.i("GCMIntentService", "Device registered: regId = " + str);
        DeviceRegistrar.registerWithServer(context, str);
    }

    protected final void onUnregistered(Context context, String str) {
        Log.i("GCMIntentService", "Device unregistered");
        Log.w("DeviceRegistrar", "Try To Unregistered for pushes");
        GCMRegistrar.setRegisteredOnServer(context, false);
        Throwable exception = new Exception();
        NetworkResult networkResult = null;
        int i = false;
        while (i < 5) {
            try {
                networkResult = DeviceRegistrar.makeRequest(context, str, "unregisterDevice");
                if (200 != networkResult.mResultCode) {
                    continue;
                    i++;
                } else if (200 != networkResult.mPushwooshCode) {
                    break;
                } else {
                    PushEventsTransmitter.onUnregistered(context, str);
                    Log.w("DeviceRegistrar", "Unregistered for pushes: " + str);
                    Editor edit = context.getSharedPreferences("com.pushwoosh.pushnotifications", 0).edit();
                    edit.remove("last_registration_change");
                    edit.commit();
                    return;
                }
            } catch (Exception e) {
                exception = e;
            }
        }
        if (exception.getMessage() != null) {
            PushEventsTransmitter.onUnregisteredError(context, exception.getMessage());
            Log.e("DeviceRegistrar", "Unregistration error " + exception.getMessage(), exception);
            return;
        }
        Log.e("DeviceRegistrar", "Unregistration error " + networkResult.mResultData.toString());
        PushEventsTransmitter.onUnregisteredError(context, networkResult.mResultData.toString());
    }
}
