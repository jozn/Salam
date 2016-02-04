package com.shamchat.androidclient.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import com.rokhgroup.mqtt.NotifySimple;
import com.shamchat.activity.MainWindow;
import com.shamchat.androidclient.IXMPPChatCallback;
import com.shamchat.androidclient.data.RosterProvider;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.exceptions.ZaminXMPPException;
import com.shamchat.androidclient.service.IXMPPChatService.Stub;
import com.shamchat.androidclient.util.ConnectionState;
import com.shamchat.androidclient.util.StatusMode;
import com.shamchat.utils.Utils;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class XMPPService extends GenericService {
    private boolean DEBUG;
    private RemoteCallbackList<IXMPPChatCallback> callbacks;
    private boolean create_account;
    private long lastScreenOn;
    private Intent mAlarmIntent;
    private BroadcastReceiver mAlarmReceiver;
    private AtomicBoolean mConnectionDemanded;
    private HashSet<String> mIsBoundTo;
    private Handler mMainHandler;
    private PendingIntent mPAlarmIntent;
    private String mReconnectInfo;
    private int mReconnectTimeout;
    private Stub mServiceChatConnection;
    private ServiceNotification mServiceNotification;
    private Smackable mSmackable;
    private NetworkConnectionIntentReceiver netConnReceiver;
    private int number_of_eyes;
    private WakeLock wakeLock;

    /* renamed from: com.shamchat.androidclient.service.XMPPService.1 */
    class C10801 extends Stub {
        C10801() {
        }

        public final boolean isAuthenticated() throws RemoteException {
            if (XMPPService.this.mSmackable != null) {
                return XMPPService.this.mSmackable.isAuthenticated();
            }
            return false;
        }

        public final void clearNotifications(String jid) throws RemoteException {
            GenericService genericService = XMPPService.this;
            if (genericService.notificationId.containsKey(jid)) {
                genericService.mNotificationMGR.cancel(((Integer) genericService.notificationId.get(jid)).intValue());
            }
        }

        public final String createRoom(String roomName, String nickName) throws RemoteException {
            String returnValue = XMPPService.this.mSmackable.createRoom$7157d249(roomName);
            int broadCastItems = XMPPService.this.callbacks.beginBroadcast();
            for (int i = 0; i < broadCastItems; i++) {
                try {
                    if (returnValue.equals("success")) {
                        ((IXMPPChatCallback) XMPPService.this.callbacks.getBroadcastItem(i)).roomCreated(true);
                    } else {
                        ((IXMPPChatCallback) XMPPService.this.callbacks.getBroadcastItem(i)).roomCreated(false);
                    }
                } catch (RemoteException e) {
                    GenericService.logError("caught RemoteException: " + e.getMessage());
                }
            }
            XMPPService.this.callbacks.finishBroadcast();
            return returnValue;
        }

        public final String sendMessage(String toJID, String message, String messageType, boolean isGroupChat, String packetId, String latitude, String longitude, String description) throws RemoteException {
            if (XMPPService.this.mSmackable != null) {
                XMPPService.this.mSmackable.sendMessage(toJID, message, messageType, isGroupChat, packetId, latitude, longitude, description);
            }
            return "success";
        }

        public final int getConnectionState() throws RemoteException {
            if (XMPPService.this.mSmackable != null) {
                return XMPPService.this.mSmackable.getConnectionState().ordinal();
            }
            return ConnectionState.OFFLINE.ordinal();
        }

        public final String getConnectionStateString() throws RemoteException {
            return XMPPService.access$400(XMPPService.this);
        }

        public final void setStatusFromConfig() throws RemoteException {
            if (XMPPService.this.mSmackable != null) {
                XMPPService.this.mSmackable.setStatusFromConfig();
                XMPPService.this.updateServiceNotification();
            }
        }

        public final void addRosterItem(String user, String alias, String group) throws RemoteException {
            try {
                XMPPService.this.mSmackable.addRosterItem$14e1ec6d(user, alias);
            } catch (ZaminXMPPException e) {
                XMPPService.this.shortToastNotify(e);
            }
        }

        public final void addRosterGroup(String group) throws RemoteException {
            XMPPService.this.mSmackable.addRosterGroup(group);
        }

        public final void removeRosterItem(String user) throws RemoteException {
            try {
                XMPPService.this.mSmackable.removeRosterItem(user);
            } catch (ZaminXMPPException e) {
                XMPPService.this.shortToastNotify(e);
            }
        }

        public final void moveRosterItemToGroup(String user, String group) throws RemoteException {
            try {
                XMPPService.this.mSmackable.moveRosterItemToGroup(user, group);
            } catch (ZaminXMPPException e) {
                XMPPService.this.shortToastNotify(e);
            }
        }

        public final void renameRosterItem(String user, String newName) throws RemoteException {
            try {
                XMPPService.this.mSmackable.renameRosterItem(user, newName);
            } catch (ZaminXMPPException e) {
                XMPPService.this.shortToastNotify(e);
            }
        }

        public final void renameRosterGroup(String group, String newGroup) throws RemoteException {
            XMPPService.this.mSmackable.renameRosterGroup(group, newGroup);
        }

        public final String changePassword(String newPassword) throws RemoteException {
            return XMPPService.this.mSmackable.changePassword(newPassword);
        }

        public final void disconnect() throws RemoteException {
            XMPPService.this.manualDisconnect();
        }

        public final void connect() throws RemoteException {
            XMPPService.this.mConnectionDemanded.set(true);
            XMPPService.this.mReconnectTimeout = 5;
            XMPPService.this.doConnect();
        }

        public final void sendPresenceRequest(String jid, String type) throws RemoteException {
            XMPPService.this.mSmackable.sendPresenceRequest(jid, type);
        }

        public final String addFriendsToChatRoom(String chatRoom, List<String> friendsJIDs) throws RemoteException {
            XMPPService.this.mSmackable.addFriendsToChatRoom(chatRoom, friendsJIDs);
            return null;
        }

        public final String sendSeenPacket(String packetId, String to, String threadId) throws RemoteException {
            return XMPPService.this.mSmackable.sendSeenPacket(packetId, to, threadId);
        }

        public final long getLastActivity(String friendJabberId) throws RemoteException {
            return XMPPService.this.mSmackable.getLastActivity(friendJabberId);
        }

        public final String onComposing(String toJabberId, String composingText, boolean isGroupChat) throws RemoteException {
            return XMPPService.this.mSmackable.onComposing(toJabberId, composingText, isGroupChat);
        }

        public final void registerChatCallback(IXMPPChatCallback callback) throws RemoteException {
            if (callback != null) {
                XMPPService.this.callbacks.register(callback);
            }
        }

        public final void unregisterChatCallback(IXMPPChatCallback callback) throws RemoteException {
            if (callback != null) {
                XMPPService.this.callbacks.unregister(callback);
            }
        }
    }

    /* renamed from: com.shamchat.androidclient.service.XMPPService.2 */
    class C10812 implements XMPPServiceCallback {
        C10812() {
        }

        public final void newMessage(String from, String message, boolean silent_notification) {
            GenericService.logInfo("notification: " + from);
            XMPPService.this.notifyClient$1c42781f(from, XMPPService.this.mSmackable.getNameForJID(from), message, !XMPPService.this.mIsBoundTo.contains(from), silent_notification);
        }

        public final void connectionStateChanged() {
            switch (C10834.$SwitchMap$com$shamchat$androidclient$util$ConnectionState[XMPPService.this.mSmackable.getConnectionState().ordinal()]) {
                case Logger.SEVERE /*1*/:
                    XMPPService.access$1100(XMPPService.this, XMPPService.this.getString(2131493061));
                    return;
                case Logger.WARNING /*2*/:
                    XMPPService.this.mReconnectTimeout = 5;
                    break;
            }
            XMPPService.this.updateServiceNotification();
        }
    }

    /* renamed from: com.shamchat.androidclient.service.XMPPService.3 */
    class C10823 implements Runnable {
        C10823() {
        }

        public final void run() {
            if (XMPPService.this.mSmackable != null && XMPPService.this.number_of_eyes == 0) {
                XMPPService.this.mSmackable.setUserWatching(false);
            }
        }
    }

    /* renamed from: com.shamchat.androidclient.service.XMPPService.4 */
    static /* synthetic */ class C10834 {
        static final /* synthetic */ int[] $SwitchMap$com$shamchat$androidclient$util$ConnectionState;

        static {
            $SwitchMap$com$shamchat$androidclient$util$ConnectionState = new int[ConnectionState.values().length];
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.DISCONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.ONLINE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private class NetworkConnectionIntentReceiver extends BroadcastReceiver {
        private NetworkConnectionIntentReceiver() {
        }

        public final void onReceive(Context ctx, Intent intent) {
            WakeLock wl = ((PowerManager) XMPPService.this.getSystemService("power")).newWakeLock(1, "XMPPService");
            wl.acquire();
            try {
                if (XMPPService.this.DEBUG) {
                    XMPPService.this.notifyUser("XMPPService - net change");
                }
                boolean doConnect = true;
                if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                    if (XMPPService.this.lastScreenOn - System.currentTimeMillis() <= 300000) {
                        doConnect = false;
                    }
                    XMPPService.this.lastScreenOn = System.currentTimeMillis();
                    if (!(XMPPService.this.mSmackable == null || XMPPService.this.mSmackable.isAuthenticated())) {
                        doConnect = true;
                    }
                }
                if (!XMPPService.this.networkConnected()) {
                    if (XMPPService.this.DEBUG) {
                        XMPPService.this.notifyUser("XMPPService -no internet");
                    }
                    ((AlarmManager) XMPPService.this.getSystemService(NotificationCompatApi21.CATEGORY_ALARM)).set(0, System.currentTimeMillis() + ((long) (XMPPService.this.mReconnectTimeout * 1000)), XMPPService.this.mPAlarmIntent);
                } else if (doConnect) {
                    if (XMPPService.this.DEBUG) {
                        XMPPService.this.notifyUser("doConnect = true");
                    }
                    XMPPService.this.failConnection("after mobile sleep - trying to reconnect");
                    XMPPService.this.mReconnectTimeout = 5;
                    XMPPService.this.doConnect();
                }
                wl.release();
            } catch (Throwable th) {
                wl.release();
            }
        }
    }

    private class ReconnectAlarmReceiver extends BroadcastReceiver {
        private ReconnectAlarmReceiver() {
        }

        public final void onReceive(Context ctx, Intent i) {
            WakeLock wl = ((PowerManager) XMPPService.this.getSystemService("power")).newWakeLock(1, "XMPPService");
            wl.acquire();
            try {
                GenericService.logInfo("Alarm received.");
                if (XMPPService.this.DEBUG) {
                    XMPPService.this.notifyUser("ReconnectAlarm received");
                }
                if (!XMPPService.this.mConnectionDemanded.get()) {
                    return;
                }
                if (XMPPService.this.mSmackable == null || XMPPService.this.mSmackable.getConnectionState() != ConnectionState.ONLINE) {
                    XMPPService.this.doConnect();
                    wl.release();
                    return;
                }
                GenericService.logError("Reconnect attempt aborted: we are connected again!");
                wl.release();
            } finally {
                wl.release();
            }
        }
    }

    public XMPPService() {
        this.mConnectionDemanded = new AtomicBoolean(false);
        this.DEBUG = false;
        this.mReconnectTimeout = 5;
        this.mReconnectInfo = BuildConfig.VERSION_NAME;
        this.mAlarmIntent = new Intent("com.shamchat.androidclient.RECONNECT_ALARM");
        this.mAlarmReceiver = new ReconnectAlarmReceiver();
        this.mServiceNotification = null;
        this.create_account = false;
        this.callbacks = new RemoteCallbackList();
        this.mIsBoundTo = new HashSet();
        this.mMainHandler = new Handler();
        this.wakeLock = null;
        this.lastScreenOn = 0;
        this.number_of_eyes = 0;
    }

    static /* synthetic */ void access$1100(XMPPService x0, String x1) {
        Log.i("zamin.Service", "connectionFailed: " + x1);
        if (x0.DEBUG) {
            x0.notifyUser("connectionFailed " + x1);
        }
        if (!x0.networkConnected()) {
            x0.mReconnectInfo = x0.getString(2131493064);
            x0.mSmackable.requestConnectionState(ConnectionState.RECONNECT_NETWORK);
        } else if (x0.mConnectionDemanded.get()) {
            x0.mReconnectInfo = x0.getString(2131493067, new Object[]{Integer.valueOf(x0.mReconnectTimeout)});
            x0.mSmackable.requestConnectionState(ConnectionState.RECONNECT_DELAYED);
            Log.i("zamin.Service", "connectionFailed(): registering reconnect in " + x0.mReconnectTimeout + "s");
            ((AlarmManager) x0.getSystemService(NotificationCompatApi21.CATEGORY_ALARM)).set(0, System.currentTimeMillis() + ((long) (x0.mReconnectTimeout * 1000)), x0.mPAlarmIntent);
        } else {
            Log.i("zamin.Service", "connectionClosed.");
            if (x0.DEBUG) {
                x0.notifyUser("connectionClosed");
            }
            x0.mReconnectInfo = BuildConfig.VERSION_NAME;
            x0.mServiceNotification.hideNotification$4289b6e2(x0);
        }
    }

    static /* synthetic */ String access$400(XMPPService x0) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(x0.mReconnectInfo);
        if (!(x0.mSmackable == null || x0.mSmackable.getLastError() == null)) {
            stringBuilder.append("\n");
            stringBuilder.append(x0.mSmackable.getLastError());
        }
        return stringBuilder.toString();
    }

    public final void notifyUser(String message) {
        NotifySimple.notifcation$34410889(getApplicationContext(), message, new Intent());
    }

    public IBinder onBind(Intent intent) {
        userStartedWatching();
        if (this.DEBUG) {
            notifyUser("XMPPService onBind");
        }
        return this.mServiceChatConnection;
    }

    public void onRebind(Intent intent) {
        if (this.DEBUG) {
            notifyUser("XMPPService rebind");
        }
        userStartedWatching();
        String chatPartner = intent.getDataString();
        if (chatPartner != null) {
            this.mIsBoundTo.add(chatPartner);
            this.notificationCount.remove(chatPartner);
        }
    }

    public boolean onUnbind(Intent intent) {
        if (this.DEBUG) {
            notifyUser("XMPPService unbinded");
        }
        String chatPartner = intent.getDataString();
        if (chatPartner != null) {
            this.mIsBoundTo.remove(chatPartner);
        }
        this.number_of_eyes--;
        Log.i("zamin.Service", "userStoppedWatching: " + this.number_of_eyes);
        this.mMainHandler.postDelayed(new C10823(), 3000);
        return true;
    }

    public void onCreate() {
        super.onCreate();
        if (this.DEBUG) {
            notifyUser("XMPPService onCreate");
        }
        this.mServiceChatConnection = new C10801();
        if (this.mPAlarmIntent == null) {
            this.mPAlarmIntent = PendingIntent.getBroadcast(this, 0, this.mAlarmIntent, 134217728);
            registerReceiver(this.mAlarmReceiver, new IntentFilter("com.shamchat.androidclient.RECONNECT_ALARM"));
        }
        if (this.netConnReceiver == null) {
            this.netConnReceiver = new NetworkConnectionIntentReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
            intentFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
            intentFilter.addAction("android.net.wifi.STATE_CHANGE");
            intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(this.netConnReceiver, intentFilter);
        }
        ZaminBroadcastReceiver.initNetworkStatus(getApplicationContext());
        if (this.mConfig.autoConnect && this.mConfig.jid_configured) {
            startService(new Intent(this, XMPPService.class));
        }
        this.mServiceNotification = Holder.sInstance;
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.DEBUG) {
            notifyUser("XMPPService Destroyed");
        }
        if (this.mPAlarmIntent != null) {
            ((AlarmManager) getSystemService(NotificationCompatApi21.CATEGORY_ALARM)).cancel(this.mPAlarmIntent);
        }
        this.callbacks.kill();
        if (this.mSmackable != null) {
            manualDisconnect();
            this.mSmackable.unRegisterCallback();
        }
        unregisterReceiver(this.mAlarmReceiver);
        unregisterReceiver(this.netConnReceiver);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("zamin.Service", "onStartCommand(), mConnectionDemanded=" + this.mConnectionDemanded.get());
        if (intent != null) {
            this.create_account = intent.getBooleanExtra("create_account", false);
            if (MqttServiceConstants.DISCONNECT_ACTION.equals(intent.getAction())) {
                failConnection(getString(2131493064));
            } else if ("reconnect".equals(intent.getAction())) {
                failConnection(getString(2131493064));
                this.mReconnectTimeout = 5;
                doConnect();
            } else if ("ping".equals(intent.getAction()) && this.mSmackable != null) {
                this.mSmackable.sendServerPing();
            }
            return 1;
        }
        this.mConnectionDemanded.set(this.mConfig.autoConnect);
        doConnect();
        return 1;
    }

    private void updateServiceNotification() {
        ConnectionState cs = ConnectionState.OFFLINE;
        if (this.mSmackable != null) {
            cs = this.mSmackable.getConnectionState();
        }
        if (this.DEBUG) {
            notifyUser("ServiceNotification " + cs.toString());
        }
        getContentResolver().notifyChange(RosterProvider.CONTENT_URI, null);
        getContentResolver().notifyChange(RosterProvider.GROUPS_URI, null);
        try {
            broadcastConnectionState(cs);
        } catch (Exception e) {
        }
        if (!this.mConfig.foregroundService) {
            return;
        }
        if (cs == ConnectionState.OFFLINE) {
            this.mServiceNotification.hideNotification$4289b6e2(this);
            return;
        }
        String message;
        Notification n = new Notification(2130837998, null, System.currentTimeMillis());
        n.flags = 42;
        Intent notificationIntent = new Intent(this, MainWindow.class);
        notificationIntent.setFlags(67108864);
        n.contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 134217728);
        if (cs == ConnectionState.ONLINE) {
            n.icon = 2130838000;
        }
        String userId = Utils.getUserIdFromXmppUserId(this.mConfig.jabberID);
        try {
            Cursor cursor = getContentResolver().query(UserProvider.CONTENT_URI_USER, new String[]{"name"}, "userId=?", new String[]{userId}, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                userId = cursor.getString(cursor.getColumnIndex("name"));
            }
            cursor.close();
        } catch (Exception e2) {
            userId = "User";
        }
        String title = getString(2131493068, new Object[]{userId});
        if (cs != ConnectionState.ONLINE) {
            message = this.mReconnectInfo;
        } else {
            String string = getString(StatusMode.fromString(this.mConfig.statusMode).textId);
            if (this.mConfig.statusMessage.length() > 0) {
                string = string + " (" + this.mConfig.statusMessage + ")";
            }
            message = string;
        }
        long when = System.currentTimeMillis();
        String ticker = title + " " + message;
        Builder notificationCompat = new Builder(this);
        notificationCompat.setAutoCancel(true).setContentTitle(title).setContentIntent(n.contentIntent).setContentText(message).setTicker(ticker).setWhen(when).setSmallIcon(2130837905);
        Notification notification = notificationCompat.build();
        notification.flags = 42;
        ((NotificationManager) getSystemService("notification")).notify(0, notification);
        this.mServiceNotification.showNotification(this, SERVICE_NOTIFICATION, notification);
    }

    private void doConnect() {
        this.mReconnectInfo = getString(2131493060);
        if (this.DEBUG) {
            notifyUser("doConnect()...");
        }
        updateServiceNotification();
        if (this.mSmackable == null) {
            System.setProperty("smack.debugEnabled", this.mConfig.smackdebug);
            try {
                this.mSmackable = new SmackableImp(this.mConfig, getContentResolver(), this);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            this.mSmackable.registerCallback(new C10812());
        }
        this.mSmackable.requestConnectionState(ConnectionState.ONLINE, this.create_account);
    }

    private void broadcastConnectionState(ConnectionState cs) {
        int broadCastItems = this.callbacks.beginBroadcast();
        for (int i = 0; i < broadCastItems; i++) {
            try {
                ((IXMPPChatCallback) this.callbacks.getBroadcastItem(i)).connectionStateChanged(cs.ordinal());
            } catch (RemoteException e) {
                Log.e("zamin.Service", "caught RemoteException: " + e.getMessage());
            }
        }
        this.callbacks.finishBroadcast();
    }

    private boolean networkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService("connectivity");
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }

    private void failConnection(String reason) {
        Log.i("zamin.Service", "failConnection: " + reason);
        if (this.DEBUG) {
            notifyUser("failConnection " + reason);
        }
        this.mReconnectInfo = reason;
        updateServiceNotification();
        if (this.mSmackable != null) {
            this.mSmackable.requestConnectionState(ConnectionState.DISCONNECTED);
        }
    }

    public final void manualDisconnect() {
        this.mConnectionDemanded.set(false);
        this.mReconnectInfo = getString(2131493062);
        if (this.mSmackable != null) {
            this.mSmackable.requestConnectionState(ConnectionState.OFFLINE);
        }
    }

    private void userStartedWatching() {
        this.number_of_eyes++;
        Log.i("zamin.Service", "userStartedWatching: " + this.number_of_eyes);
        if (this.mSmackable != null) {
            this.mSmackable.setUserWatching(true);
        }
    }
}
