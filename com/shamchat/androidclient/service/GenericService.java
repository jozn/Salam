package com.shamchat.androidclient.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.Toast;
import com.kyleduo.switchbutton.C0473R;
import com.rokhgroup.mqtt.NotifySimple;
import com.shamchat.activity.BroadcastMessageActivity;
import com.shamchat.activity.ChatActivity;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.activity.MainWindow;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.data.ZaminConfiguration;
import com.shamchat.models.FriendGroup;
import com.shamchat.utils.Utils;
import com.shamchat.utils.Utils.ContactDetails;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public abstract class GenericService extends Service {
    protected static int SERVICE_NOTIFICATION;
    public static String currentOpenedThreadId;
    private static SharedPreferences sp;
    private boolean DEBUG;
    private int lastNotificationId;
    protected ZaminConfiguration mConfig;
    private Notification mNotification;
    private Intent mNotificationGroupIntent;
    private Intent mNotificationIntent;
    NotificationManager mNotificationMGR;
    private Vibrator mVibrator;
    protected WakeLock mWakeLock;
    Map<String, Integer> notificationCount;
    Map<String, Integer> notificationId;

    public GenericService() {
        this.DEBUG = false;
        this.notificationCount = new HashMap(2);
        this.notificationId = new HashMap(2);
        this.lastNotificationId = 2;
    }

    static {
        SERVICE_NOTIFICATION = 1;
        currentOpenedThreadId = null;
    }

    public void notifyUser(String message) {
        NotifySimple.notifcation$34410889(getApplicationContext(), message, new Intent());
    }

    public void onCreate() {
        Log.i("zamin.Service", "called onCreate()");
        if (this.DEBUG) {
            notifyUser("GenericService Started...");
        }
        super.onCreate();
        this.mConfig = SHAMChatApplication.getConfig();
        this.mVibrator = (Vibrator) getSystemService("vibrator");
        this.mWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, "zamin");
        this.mNotificationMGR = (NotificationManager) getSystemService("notification");
        this.mNotificationIntent = new Intent(this, ChatActivity.class);
        this.mNotificationGroupIntent = new Intent(this, ChatInitialForGroupChatActivity.class);
    }

    public void onDestroy() {
        Log.i("zamin.Service", "called onDestroy()");
        if (this.DEBUG) {
            notifyUser("GenericService Destroyed...");
        }
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("zamin.Service", "called onStartCommand()");
        return 1;
    }

    protected final void notifyClient$1c42781f(String fromJid, String fromUserName, String message, boolean showNotification, boolean silent_notification) {
        Object obj;
        UserProvider userProvider;
        String str;
        String threadId = Utils.getUserIdFromXmppUserId(this.mConfig.jabberID) + "-" + Utils.getUserIdFromXmppUserId(fromJid);
        if (currentOpenedThreadId == null || !currentOpenedThreadId.equalsIgnoreCase(threadId)) {
            if (fromJid.equals("rabtcdn.com")) {
                Context context = SHAMChatApplication.getMyApplicationContext();
                Intent intent = new Intent(context, BroadcastMessageActivity.class);
                intent.putExtra("Message", message);
                intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
                context.startActivity(intent);
            } else if (showNotification) {
                int intValue;
                Notification notification;
                int notifyId;
                this.mWakeLock.acquire();
                if (silent_notification && !this.notificationCount.containsKey(fromJid)) {
                    silent_notification = false;
                }
                if (this.notificationCount.containsKey(fromJid)) {
                    intValue = ((Integer) this.notificationCount.get(fromJid)).intValue();
                } else {
                    intValue = 0;
                }
                try {
                    ((NotificationManager) getSystemService("notification")).cancelAll();
                } catch (Exception e) {
                }
                String userIdFromXmppUserId = Utils.getUserIdFromXmppUserId(fromJid);
                String str2 = Utils.getUserIdFromXmppUserId(this.mConfig.jabberID) + "-" + userIdFromXmppUserId;
                if (currentOpenedThreadId == null || !currentOpenedThreadId.equalsIgnoreCase(str2)) {
                    CharSequence charSequence;
                    Object obj2;
                    Cursor query;
                    CharSequence charSequence2;
                    PendingIntent activity;
                    if (message.contains("http://")) {
                        message = "New Media file";
                    }
                    int i = intValue + 1;
                    this.notificationCount.put(fromJid, Integer.valueOf(i));
                    if (fromUserName == null || fromUserName.length() == 0) {
                        charSequence = "New message";
                        obj2 = null;
                    } else {
                        if (fromUserName.contains("_")) {
                            int i2;
                            String str3 = "New message ";
                            query = getContentResolver().query(UserProvider.CONTENT_URI_GROUP, new String[]{FriendGroup.DB_NAME}, FriendGroup.CHAT_ROOM_NAME + "=?", new String[]{fromJid}, null);
                            if (query.getCount() > 0) {
                                query.moveToFirst();
                                String string = query.getString(query.getColumnIndex(FriendGroup.DB_NAME));
                                if (!(string == null || string.isEmpty())) {
                                    charSequence = str3 + "from " + string;
                                    i2 = 1;
                                }
                            }
                            obj = str3;
                            i2 = 1;
                        } else {
                            try {
                                Cursor query2 = getContentResolver().query(Uri.parse(UserProvider.CONTENT_URI_USER.toString() + MqttTopic.TOPIC_LEVEL_SEPARATOR + Utils.getUserIdFromXmppUserId(fromJid)), null, null, null, null);
                                query2.moveToFirst();
                                charSequence = query2.getString(query2.getColumnIndex("mobileNo"));
                                if (charSequence != null) {
                                    ContactDetails conactExists = Utils.getConactExists(getApplicationContext(), charSequence);
                                    if (conactExists.isExist) {
                                        charSequence = conactExists.displayName;
                                        obj2 = null;
                                    } else {
                                        obj2 = null;
                                    }
                                } else {
                                    try {
                                        charSequence = query2.getString(query2.getColumnIndex("name"));
                                        obj2 = null;
                                    } catch (Exception e2) {
                                        userProvider = new UserProvider();
                                        str = UserProvider.getUserDetailsFromServer(Utils.getUserIdFromXmppUserId(fromJid)).username;
                                        e2.printStackTrace();
                                        obj = str;
                                        obj2 = null;
                                    }
                                }
                            } catch (Exception e22) {
                                userProvider = new UserProvider();
                                str = UserProvider.getUserDetailsFromServer(Utils.getUserIdFromXmppUserId(fromJid)).username;
                                e22.printStackTrace();
                                obj = str;
                                obj2 = null;
                            }
                        }
                    }
                    if (obj2 == null) {
                        charSequence = getString(2131493237, new Object[]{charSequence});
                    }
                    if (this.mConfig.ticker) {
                        intValue = message.indexOf(10);
                        if (intValue < 0) {
                            intValue = 0;
                        }
                        if (intValue > 45 || message.length() > 45) {
                            intValue = 45;
                        }
                        if (intValue > 0) {
                            str = message.substring(0, intValue) + " [...]";
                        } else {
                            str = message;
                        }
                        charSequence2 = charSequence + ":\n" + str;
                    } else {
                        charSequence2 = getString(2131493235);
                    }
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("setting", 0);
                    sp = sharedPreferences;
                    switch (sharedPreferences.getInt("notif_icon", 1)) {
                        case Logger.SEVERE /*1*/:
                            this.mNotification = new Notification(2130837905, charSequence2, System.currentTimeMillis());
                            break;
                        case Logger.WARNING /*2*/:
                            this.mNotification = new Notification(2130837906, charSequence2, System.currentTimeMillis());
                            break;
                        case Logger.INFO /*3*/:
                            this.mNotification = new Notification(2130837907, charSequence2, System.currentTimeMillis());
                            break;
                        case Logger.CONFIG /*4*/:
                            this.mNotification = new Notification(2130837908, charSequence2, System.currentTimeMillis());
                            break;
                        case Logger.FINE /*5*/:
                            this.mNotification = new Notification(2130837909, charSequence2, System.currentTimeMillis());
                            break;
                        case Logger.FINER /*6*/:
                            this.mNotification = new Notification(2130837910, charSequence2, System.currentTimeMillis());
                            break;
                        case Logger.FINEST /*7*/:
                            this.mNotification = new Notification(2130837911, charSequence2, System.currentTimeMillis());
                            break;
                        case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                            this.mNotification = new Notification(2130837912, charSequence2, System.currentTimeMillis());
                            break;
                        case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                            this.mNotification = new Notification(2130837913, charSequence2, System.currentTimeMillis());
                            break;
                        default:
                            this.mNotification = new Notification(2130837905, charSequence2, System.currentTimeMillis());
                            break;
                    }
                    this.mNotification.defaults = 0;
                    Uri parse = Uri.parse(fromJid);
                    this.mNotificationIntent.setData(parse);
                    this.mNotificationGroupIntent.setData(parse);
                    if (obj2 == null) {
                        this.mNotificationIntent.putExtra(ChatActivity.INTENT_EXTRA_USER_ID, userIdFromXmppUserId);
                        this.mNotificationIntent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, str2);
                        this.mNotificationIntent.setFlags(536870912);
                        activity = PendingIntent.getActivity(this, 0, this.mNotificationIntent, 134217728);
                    } else {
                        this.mNotificationGroupIntent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, userIdFromXmppUserId);
                        this.mNotificationGroupIntent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, str2);
                        this.mNotificationGroupIntent.setFlags(536870912);
                        activity = PendingIntent.getActivity(this, 0, this.mNotificationGroupIntent, 134217728);
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    Intent intent2 = new Intent(this, MainWindow.class);
                    intent2.setFlags(67108864);
                    PendingIntent.getActivity(this, 0, intent2, 134217728);
                    Builder builder = new Builder(this);
                    builder.setAutoCancel(true).setContentTitle(charSequence).setContentIntent(activity).setContentText(message).setTicker(charSequence2).setWhen(currentTimeMillis).setSmallIcon(2130837905);
                    Notification build = builder.build();
                    build.flags = 8;
                    ((NotificationManager) getSystemService("notification")).notify(1, build);
                    if (i > 1) {
                        this.mNotification.number = i;
                    }
                    this.mNotification.flags = 16;
                    query = getContentResolver().query(UserProvider.CONTENT_URI_NOTIFICATION, null, "user_id=?", new String[]{this.mConfig.userId}, null);
                    query.moveToFirst();
                    UserProvider.userNotificationFromCursor(query);
                    SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    Editor edit = defaultSharedPreferences.edit();
                    try {
                        Uri parse2;
                        int i3 = sp.getInt("sound", 1);
                        Log.i("Notify", "Sound " + i3);
                        switch (i3) {
                            case Logger.SEVERE /*1*/:
                                parse2 = Uri.parse("android.resource://" + SHAMChatApplication.getMyApplicationContext().getPackageName() + "/2131034112");
                                break;
                            case Logger.WARNING /*2*/:
                                parse2 = Uri.parse("android.resource://" + SHAMChatApplication.getMyApplicationContext().getPackageName() + "/2131034113");
                                break;
                            case Logger.INFO /*3*/:
                                parse2 = Uri.parse("android.resource://" + SHAMChatApplication.getMyApplicationContext().getPackageName() + "/2131034114");
                                break;
                            case Logger.CONFIG /*4*/:
                                parse2 = Uri.parse("android.resource://" + SHAMChatApplication.getMyApplicationContext().getPackageName() + "/2131034116");
                                break;
                            case Logger.FINE /*5*/:
                                parse2 = Uri.parse("android.resource://" + SHAMChatApplication.getMyApplicationContext().getPackageName() + "/2131034118");
                                break;
                            default:
                                parse2 = Uri.parse("android.resource://" + SHAMChatApplication.getMyApplicationContext().getPackageName() + "/2131034112");
                                break;
                        }
                        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), parse2);
                        if (System.currentTimeMillis() - Long.valueOf(defaultSharedPreferences.getLong("Notification", 3100)).longValue() > 2000 && !ringtone.isPlaying()) {
                            ringtone.play();
                            edit.putLong("Notification", System.currentTimeMillis());
                            edit.commit();
                        }
                    } catch (Exception e3) {
                    }
                }
                if (this.mConfig.isLEDNotify) {
                    this.mNotification.ledARGB = -65281;
                    this.mNotification.ledOnMS = 300;
                    this.mNotification.ledOffMS = 1000;
                    notification = this.mNotification;
                    notification.flags |= 1;
                }
                if (!silent_notification) {
                    this.mNotification.sound = this.mConfig.notifySound;
                }
                if (this.notificationId.containsKey(fromJid)) {
                    notifyId = ((Integer) this.notificationId.get(fromJid)).intValue();
                } else {
                    this.lastNotificationId++;
                    notifyId = this.lastNotificationId;
                    this.notificationId.put(fromJid, Integer.valueOf(notifyId));
                }
                if (!silent_notification && "SYSTEM".equals(this.mConfig.vibraNotify)) {
                    notification = this.mNotification;
                    notification.defaults |= 2;
                }
                SharedPreferences sharedPreferences2 = SHAMChatApplication.getMyApplicationContext().getSharedPreferences("setting", 0);
                sp = sharedPreferences2;
                int shakes = sharedPreferences2.getInt("shakee", 1);
                Log.i("Notify", "Vibrate " + shakes);
                if (shakes != 0) {
                    this.mNotificationMGR.notify(notifyId, this.mNotification);
                    this.mVibrator.vibrate(400);
                }
                this.mWakeLock.release();
            } else if (!silent_notification) {
                try {
                    if (!Uri.EMPTY.equals(this.mConfig.notifySound)) {
                        RingtoneManager.getRingtone(getApplicationContext(), this.mConfig.notifySound).play();
                    }
                } catch (NullPointerException e4) {
                }
            }
        }
    }

    protected final void shortToastNotify(Throwable e) {
        e.printStackTrace();
        while (e.getCause() != null) {
            e = e.getCause();
        }
        Toast.makeText(this, e.getMessage(), 0).show();
    }

    protected static void logError(String data) {
        Log.e("zamin.Service", data);
    }

    protected static void logInfo(String data) {
        Log.i("zamin.Service", data);
    }
}
