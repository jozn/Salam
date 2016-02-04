package com.rokhgroup.mqtt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import com.path.android.jobqueue.JobManager;
import com.rokhgroup.mqtt.ActionListener.Action;
import com.rokhgroup.mqtt.Connection.ConnectionStatus;
import com.rokhgroup.utils.RokhPref;
import com.shamchat.activity.AddFavoriteTextActivity;
import com.shamchat.activity.ChatActivity;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.adapters.MyMessageType;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.events.CloseGroupActivityEvent;
import com.shamchat.events.FileUploadingProgressEvent;
import com.shamchat.events.NewMessageEvent;
import com.shamchat.events.UpdateGroupMembersList;
import com.shamchat.jobs.PublishToTopicJob;
import com.shamchat.jobs.RoomRestoreJob;
import com.shamchat.jobs.SubscribeToAllTopicsJob;
import com.shamchat.jobs.SubscribeToEventsJob;
import com.shamchat.models.ChatMessage;
import com.shamchat.models.ChatMessage.MessageStatusType;
import com.shamchat.models.FriendGroup;
import com.shamchat.models.FriendGroupMember;
import com.shamchat.models.MessageThread;
import com.shamchat.models.User;
import com.shamchat.utils.Utils;
import de.greenrobot.event.EventBus;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MQTTService extends Service {
    private static boolean serviceRunning;
    RokhPref Session;
    private UncaughtExceptionHandler androidDefaultUEH;
    private String brokerHostName;
    private int brokerPortNumber;
    private ChangeListener changeListener;
    ChatProviderNew chatProvider;
    private boolean cleanStart;
    private String clientHandle;
    MqttConnectOptions conOpt;
    int connectRequestCount;
    Connection connection;
    private int connectionStatus$4da8916d;
    private Hashtable<String, String> dataCache;
    private BackgroundDataChangeIntentReceiver dataEnabledReceiver;
    private UncaughtExceptionHandler handler;
    boolean isConnectedtoMqtt;
    private JobManager jobManager;
    private short keepAliveSeconds;
    private LocalBinder<MQTTService> mBinder;
    private MqttAndroidClient mqttClient;
    private String mqttClientId;
    private NetworkConnectionIntentReceiver netConnReceiver;
    private PingSender pingSender;
    private int[] qualitiesOfService;

    /* renamed from: com.rokhgroup.mqtt.MQTTService.1 */
    class C06741 implements UncaughtExceptionHandler {
        C06741() {
        }

        public final void uncaughtException(Thread thread, Throwable ex) {
            Log.e("TestApplication", "Uncaught exception is: ", ex);
            MQTTService.this.androidDefaultUEH.uncaughtException(thread, ex);
        }
    }

    /* renamed from: com.rokhgroup.mqtt.MQTTService.2 */
    class C06752 implements Runnable {
        final /* synthetic */ Intent val$intent;
        final /* synthetic */ int val$startId;

        C06752(Intent intent, int i) {
            this.val$intent = intent;
            this.val$startId = i;
        }

        public final void run() {
            MQTTService.this.handleStart$7a9ca511();
        }
    }

    /* renamed from: com.rokhgroup.mqtt.MQTTService.3 */
    class C06763 implements Runnable {
        final /* synthetic */ Intent val$intent;
        final /* synthetic */ int val$startId;

        C06763(Intent intent, int i) {
            this.val$intent = intent;
            this.val$startId = i;
        }

        public final void run() {
            MQTTService.this.handleStart$7a9ca511();
        }
    }

    /* renamed from: com.rokhgroup.mqtt.MQTTService.4 */
    class C06774 implements IMqttActionListener {
        C06774() {
        }

        public final void onSuccess(IMqttToken arg0) {
            if (MQTTService.this.Session.pref.getBoolean("isFirstRun", false)) {
                MQTTService.this.jobManager.addJobInBackground(new SubscribeToAllTopicsJob());
            } else {
                MQTTService.this.Session.setFirstRun(true);
                MQTTService.this.jobManager.addJobInBackground(new RoomRestoreJob());
            }
            MQTTService.this.jobManager.addJobInBackground(new SubscribeToEventsJob());
            MQTTService.this.broadcastServiceStatus("Connected");
            MQTTService.this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.CONNECTED$6abac5f4);
            MQTTService.this.connectionStatus$4da8916d = MQTTConnectionStatus.CONNECTED$4da8916d;
            MQTTService.this.scheduleNextPing();
        }

        public final void onFailure(IMqttToken arg0, Throwable arg1) {
            if (MQTTService.this.connectRequestCount > 0) {
                MQTTService mQTTService = MQTTService.this;
                mQTTService.connectRequestCount--;
            }
            MQTTService.this.isConnectedtoMqtt = false;
            MQTTService.this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.ERROR$6abac5f4);
            MQTTService.this.connectionStatus$4da8916d = MQTTConnectionStatus.NOTCONNECTED_UNKNOWNREASON$4da8916d;
            if ((arg1 instanceof MqttPersistenceException) && ((MqttPersistenceException) arg1).getReasonCode() == 32200) {
                Notify.toast$4475a3b4(SHAMChatApplication.getMyApplicationContext(), "P fail");
            }
            MQTTService.this.scheduleNextPing();
        }
    }

    /* renamed from: com.rokhgroup.mqtt.MQTTService.5 */
    static /* synthetic */ class C06785 {
        static final /* synthetic */ int[] $SwitchMap$com$rokhgroup$mqtt$MQTTService$MQTTConnectionStatus;
        static final /* synthetic */ int[] f12x39e42954;

        static {
            f12x39e42954 = new int[MessageContentType.values().length];
            try {
                f12x39e42954[MessageContentType.TEXT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f12x39e42954[MessageContentType.IMAGE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f12x39e42954[MessageContentType.VIDEO.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f12x39e42954[MessageContentType.STICKER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f12x39e42954[MessageContentType.LOCATION.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f12x39e42954[MessageContentType.VOICE_RECORD.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            $SwitchMap$com$rokhgroup$mqtt$MQTTService$MQTTConnectionStatus = new int[MQTTConnectionStatus.values$646d2e33().length];
            try {
                $SwitchMap$com$rokhgroup$mqtt$MQTTService$MQTTConnectionStatus[MQTTConnectionStatus.INITIAL$4da8916d - 1] = 1;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$rokhgroup$mqtt$MQTTService$MQTTConnectionStatus[MQTTConnectionStatus.CONNECTING$4da8916d - 1] = 2;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$rokhgroup$mqtt$MQTTService$MQTTConnectionStatus[MQTTConnectionStatus.CONNECTED$4da8916d - 1] = 3;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$rokhgroup$mqtt$MQTTService$MQTTConnectionStatus[MQTTConnectionStatus.NOTCONNECTED_UNKNOWNREASON$4da8916d - 1] = 4;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$rokhgroup$mqtt$MQTTService$MQTTConnectionStatus[MQTTConnectionStatus.NOTCONNECTED_USERDISCONNECT$4da8916d - 1] = 5;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$rokhgroup$mqtt$MQTTService$MQTTConnectionStatus[MQTTConnectionStatus.NOTCONNECTED_DATADISABLED$4da8916d - 1] = 6;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$rokhgroup$mqtt$MQTTService$MQTTConnectionStatus[MQTTConnectionStatus.NOTCONNECTED_WAITINGFORINTERNET$4da8916d - 1] = 7;
            } catch (NoSuchFieldError e13) {
            }
        }
    }

    private class BackgroundDataChangeIntentReceiver extends BroadcastReceiver {
        private BackgroundDataChangeIntentReceiver() {
        }

        public final void onReceive(Context ctx, Intent intent) {
            WakeLock wl = ((PowerManager) MQTTService.this.getSystemService("power")).newWakeLock(1, "MQTT");
            wl.acquire();
            try {
                if (!((ConnectivityManager) MQTTService.this.getSystemService("connectivity")).getBackgroundDataSetting()) {
                    MQTTService.this.connectionStatus$4da8916d = MQTTConnectionStatus.NOTCONNECTED_WAITINGFORINTERNET$4da8916d;
                    MQTTService.this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.DISCONNECTED$6abac5f4);
                    MQTTService.this.broadcastServiceStatus("Not connected - background data disabled");
                    MQTTService.this.disconnectFromBroker();
                } else if (MQTTService.this.connectionStatus$4da8916d != MQTTConnectionStatus.CONNECTING$4da8916d) {
                    MQTTService.this.connectionStatus$4da8916d = MQTTConnectionStatus.CONNECTING$4da8916d;
                    MQTTService.this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.CONNECTING$6abac5f4);
                    MQTTService.this.defineConnectionToBroker(MQTTService.this.brokerHostName);
                    MQTTService.this.handleStart$7a9ca511();
                }
                wl.release();
            } catch (Throwable th) {
                wl.release();
            }
        }
    }

    private class ChangeListener implements PropertyChangeListener {
        private ChangeListener() {
        }

        public final void propertyChange(PropertyChangeEvent event) {
        }
    }

    public class LocalBinder<S> extends Binder {
        WeakReference<S> mService;

        public LocalBinder(S service) {
            this.mService = new WeakReference(service);
        }
    }

    public enum MQTTConnectionStatus {
        ;

        public static int[] values$646d2e33() {
            return (int[]) $VALUES$6a916c0e.clone();
        }

        static {
            INITIAL$4da8916d = 1;
            CONNECTING$4da8916d = 2;
            CONNECTED$4da8916d = 3;
            NOTCONNECTED_WAITINGFORINTERNET$4da8916d = 4;
            NOTCONNECTED_USERDISCONNECT$4da8916d = 5;
            NOTCONNECTED_DATADISABLED$4da8916d = 6;
            NOTCONNECTED_UNKNOWNREASON$4da8916d = 7;
            $VALUES$6a916c0e = new int[]{INITIAL$4da8916d, CONNECTING$4da8916d, CONNECTED$4da8916d, NOTCONNECTED_WAITINGFORINTERNET$4da8916d, NOTCONNECTED_USERDISCONNECT$4da8916d, NOTCONNECTED_DATADISABLED$4da8916d, NOTCONNECTED_UNKNOWNREASON$4da8916d};
        }
    }

    public class MqttCallbackHandler implements MqttCallback {
        private String CURRENT_USER_ID;
        private String clientHandle;
        private Context context;

        public MqttCallbackHandler(Context context, String clientHandle) {
            this.CURRENT_USER_ID = null;
            this.context = context;
            this.clientHandle = clientHandle;
            this.CURRENT_USER_ID = SHAMChatApplication.getConfig().userId;
        }

        public final void connectionLost(Throwable cause) {
            WakeLock wl = ((PowerManager) MQTTService.this.getSystemService("power")).newWakeLock(1, "MQTT");
            wl.acquire();
            try {
                if (MQTTService.this.isOnline()) {
                    MQTTService.this.connection.addAction("Connection Lost");
                    MQTTService.this.connectionStatus$4da8916d = MQTTConnectionStatus.NOTCONNECTED_UNKNOWNREASON$4da8916d;
                    MQTTService.this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.DISCONNECTED$6abac5f4);
                    MQTTService.this.broadcastServiceStatus("Connection lost - reconnecting...");
                    Intent intent = new Intent();
                    boolean doConnect = true;
                    try {
                        if (MQTTService.this.connectRequestCount > 0) {
                            doConnect = false;
                        }
                    } catch (Exception e) {
                    }
                    if (doConnect) {
                        MQTTService.this.connectionStatus$4da8916d = MQTTConnectionStatus.CONNECTING$4da8916d;
                        MQTTService.this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.CONNECTING$6abac5f4);
                    }
                } else {
                    MQTTService.this.connectionStatus$4da8916d = MQTTConnectionStatus.NOTCONNECTED_WAITINGFORINTERNET$4da8916d;
                    MQTTService.this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.DISCONNECTED$6abac5f4);
                    MQTTService.this.broadcastServiceStatus("Connection lost - no network connection");
                }
                wl.release();
            } catch (Throwable th) {
                wl.release();
            }
        }

        public final void messageArrived(String topic, MqttMessage message) throws Exception {
            WakeLock wl = ((PowerManager) this.context.getSystemService("power")).newWakeLock(1, "MQTT");
            wl.acquire();
            try {
                String packetType;
                String[] args = new String[]{new String(message.getPayload(), "UTF-8"), topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained()};
                Log.e("ARRIVED MSG", new String(message.getPayload(), "UTF-8"));
                String messageString = this.context.getString(2131493191, (Object[]) args);
                Intent intent = new Intent();
                intent.setClassName(this.context, "org.eclipse.paho.android.service.sample.ConnectionDetails");
                intent.putExtra("handle", this.clientHandle);
                Object[] notifyArgs = new String[]{MQTTService.this.connection.clientId, new String(message.getPayload(), "UTF-8"), topic};
                String jsonMessageString = new String(message.getPayload(), "UTF-8");
                if (jsonMessageString.equalsIgnoreCase("ping")) {
                    packetType = "ping";
                } else {
                    packetType = Utils.detectPacketType(jsonMessageString);
                }
                if (topic.contains("events/")) {
                    notifyArgs[1] = "received an event -  group?";
                    if (packetType.equals("invite")) {
                        processInvitation(jsonMessageString);
                        Log.e("CALL BACK RECEIVED", "PROCCESS CALLED");
                    } else if (packetType.equals("ping")) {
                        Log.e("Ping received", "Ping received");
                    } else if (packetType.equals(EnvironmentCompat.MEDIA_UNKNOWN)) {
                        Log.e("unknown received", "unknown packet type received");
                    }
                } else if (packetType.equals(AddFavoriteTextActivity.EXTRA_MESSAGE)) {
                    if (Utils.isMyOwnPacket(jsonMessageString)) {
                        MQTTService.this.scheduleNextPing();
                        wl.release();
                        return;
                    }
                    int messageContentType = Utils.detectMessageContentType(jsonMessageString);
                    addChatMessageToDB(MyMessageType.INCOMING_MSG.ordinal(), MessageStatusType.QUEUED.ordinal(), messageContentType, jsonMessageString);
                    if (!(messageContentType == MessageContentType.TEXT.ordinal() || messageContentType == MessageContentType.IMAGE.ordinal() || messageContentType == MessageContentType.VIDEO.ordinal() || messageContentType == MessageContentType.STICKER.ordinal() || messageContentType == MessageContentType.LOCATION.ordinal() || messageContentType == MessageContentType.VOICE_RECORD.ordinal())) {
                        MessageContentType.MESSAGE_WITH_IMOTICONS.ordinal();
                    }
                } else if (packetType.equals("invite")) {
                    addInviteMessageToGroup(jsonMessageString);
                } else if (!packetType.equals("join")) {
                    if (packetType.equals("left")) {
                        addLeftRoomToGroup(jsonMessageString);
                    } else if (packetType.equals("kick")) {
                        addKickedFromRoomToGroup(jsonMessageString);
                    }
                }
                MQTTService.this.connection.addAction(messageString);
            } finally {
                MQTTService.this.scheduleNextPing();
                wl.release();
            }
        }

        public final void deliveryComplete(IMqttDeliveryToken token) {
        }

        private boolean addChatMessageToDB(int direction, int messageStatus, int messageContentType, String jsonMessageString) {
            JSONException e;
            String groupId;
            String friendId;
            String threadId;
            Cursor chatCursor;
            ContentValues values;
            Cursor c;
            int dbId;
            NewMessageEvent newMessageEvent;
            boolean isExistingMessage = false;
            ContentResolver mContentResolver = SHAMChatApplication.getInstance().getContentResolver();
            JSONObject SampleMsg = null;
            String packetId = null;
            String from = null;
            String to = null;
            String messageTypeDesc = null;
            int messageType = 0;
            String messageBody = null;
            String timestamp = null;
            int isGroupChat = 0;
            String latitude = null;
            try {
                JSONObject SampleMsg2 = new JSONObject(jsonMessageString);
                try {
                    packetId = SampleMsg2.getString("packetId");
                    from = SampleMsg2.getString("from");
                    SampleMsg2.getInt("from_userid");
                    to = SampleMsg2.getString("to");
                    messageTypeDesc = SampleMsg2.getString("messageTypeDesc");
                    timestamp = SampleMsg2.getString("timestamp");
                    messageType = SampleMsg2.getInt("messageType");
                    messageBody = SampleMsg2.getString("messageBody");
                    isGroupChat = SampleMsg2.getInt("isGroupChat");
                    if (SampleMsg2.has("latitude")) {
                        latitude = SampleMsg2.getString("latitude");
                    }
                    if (SampleMsg2.has("longitude")) {
                        latitude = SampleMsg2.getString("longitude");
                    }
                    SampleMsg = SampleMsg2;
                } catch (JSONException e2) {
                    e = e2;
                    SampleMsg = SampleMsg2;
                    e.printStackTrace();
                    groupId = to;
                    friendId = to;
                    threadId = SHAMChatApplication.getConfig().userId + "-" + friendId;
                    saveOrUpdateThread(threadId, jsonMessageString, messageContentType, friendId, direction);
                    chatCursor = null;
                    chatCursor = mContentResolver.query(ChatProviderNew.CONTENT_URI_CHAT, new String[]{"_id"}, "packet_id=?", new String[]{packetId}, null);
                    isExistingMessage = true;
                    if (!isExistingMessage) {
                        values = new ContentValues();
                        values.put("message_recipient", to);
                        values.put("message_type", Integer.valueOf(direction));
                        values.put("packet_id", packetId);
                        values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, threadId);
                        values.put("description", messageTypeDesc);
                        values.put("message_datetime", timestamp);
                        values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE, Integer.valueOf(messageType));
                        values.put("message_status", Integer.valueOf(messageStatus));
                        if (isGroupChat == 1) {
                            values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, groupId);
                        }
                        if (SampleMsg.has("latitude")) {
                            values.put("latitude", latitude);
                        }
                        if (SampleMsg.has("longitude")) {
                            values.put("longitude", null);
                        }
                        if (isGroupChat == 1) {
                        }
                        System.out.println("processMessage addChatMessageToDB Single chat, both directions, Group chat outgoing");
                        values.put("message_sender", from);
                        values.put("text_message", messageBody);
                        c = null;
                        c = mContentResolver.query(mContentResolver.insert(ChatProviderNew.CONTENT_URI_CHAT, values), null, null, null, null);
                        c.moveToFirst();
                        dbId = c.getInt(c.getColumnIndex("_id"));
                        if (c != null) {
                            c.close();
                        }
                        newMessageEvent = new NewMessageEvent();
                        newMessageEvent.threadId = threadId;
                        newMessageEvent.jsonMessageString = jsonMessageString;
                        if (direction == MyMessageType.OUTGOING_MSG.ordinal()) {
                            newMessageEvent.direction = MyMessageType.INCOMING_MSG.ordinal();
                        } else {
                            newMessageEvent.direction = MyMessageType.OUTGOING_MSG.ordinal();
                        }
                        EventBus.getDefault().postSticky(newMessageEvent);
                        System.out.println("Chat test chat message id " + dbId + " thread id " + threadId);
                    }
                    chatCursor.close();
                    return isExistingMessage;
                }
            } catch (JSONException e3) {
                e = e3;
                e.printStackTrace();
                groupId = to;
                friendId = to;
                threadId = SHAMChatApplication.getConfig().userId + "-" + friendId;
                saveOrUpdateThread(threadId, jsonMessageString, messageContentType, friendId, direction);
                chatCursor = null;
                chatCursor = mContentResolver.query(ChatProviderNew.CONTENT_URI_CHAT, new String[]{"_id"}, "packet_id=?", new String[]{packetId}, null);
                isExistingMessage = true;
                if (isExistingMessage) {
                    values = new ContentValues();
                    values.put("message_recipient", to);
                    values.put("message_type", Integer.valueOf(direction));
                    values.put("packet_id", packetId);
                    values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, threadId);
                    values.put("description", messageTypeDesc);
                    values.put("message_datetime", timestamp);
                    values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE, Integer.valueOf(messageType));
                    values.put("message_status", Integer.valueOf(messageStatus));
                    if (isGroupChat == 1) {
                        values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, groupId);
                    }
                    if (SampleMsg.has("latitude")) {
                        values.put("latitude", latitude);
                    }
                    if (SampleMsg.has("longitude")) {
                        values.put("longitude", null);
                    }
                    if (isGroupChat == 1) {
                    }
                    System.out.println("processMessage addChatMessageToDB Single chat, both directions, Group chat outgoing");
                    values.put("message_sender", from);
                    values.put("text_message", messageBody);
                    c = null;
                    c = mContentResolver.query(mContentResolver.insert(ChatProviderNew.CONTENT_URI_CHAT, values), null, null, null, null);
                    c.moveToFirst();
                    dbId = c.getInt(c.getColumnIndex("_id"));
                    if (c != null) {
                        c.close();
                    }
                    newMessageEvent = new NewMessageEvent();
                    newMessageEvent.threadId = threadId;
                    newMessageEvent.jsonMessageString = jsonMessageString;
                    if (direction == MyMessageType.OUTGOING_MSG.ordinal()) {
                        newMessageEvent.direction = MyMessageType.OUTGOING_MSG.ordinal();
                    } else {
                        newMessageEvent.direction = MyMessageType.INCOMING_MSG.ordinal();
                    }
                    EventBus.getDefault().postSticky(newMessageEvent);
                    System.out.println("Chat test chat message id " + dbId + " thread id " + threadId);
                }
                chatCursor.close();
                return isExistingMessage;
            }
            groupId = to;
            friendId = to;
            threadId = SHAMChatApplication.getConfig().userId + "-" + friendId;
            saveOrUpdateThread(threadId, jsonMessageString, messageContentType, friendId, direction);
            chatCursor = null;
            try {
                chatCursor = mContentResolver.query(ChatProviderNew.CONTENT_URI_CHAT, new String[]{"_id"}, "packet_id=?", new String[]{packetId}, null);
                if (chatCursor != null && chatCursor.getCount() > 0) {
                    isExistingMessage = true;
                }
                if (isExistingMessage) {
                    values = new ContentValues();
                    values.put("message_recipient", to);
                    values.put("message_type", Integer.valueOf(direction));
                    values.put("packet_id", packetId);
                    values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, threadId);
                    values.put("description", messageTypeDesc);
                    values.put("message_datetime", timestamp);
                    values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE, Integer.valueOf(messageType));
                    values.put("message_status", Integer.valueOf(messageStatus));
                    if (isGroupChat == 1) {
                        values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, groupId);
                    }
                    if (SampleMsg.has("latitude")) {
                        values.put("latitude", latitude);
                    }
                    if (SampleMsg.has("longitude")) {
                        values.put("longitude", null);
                    }
                    if (isGroupChat == 1 || direction != MyMessageType.INCOMING_MSG.ordinal()) {
                        System.out.println("processMessage addChatMessageToDB Single chat, both directions, Group chat outgoing");
                        values.put("message_sender", from);
                        values.put("text_message", messageBody);
                    } else {
                        String userId = from;
                        String username = getContactNameFromPhone(SHAMChatApplication.getMyApplicationContext(), from);
                        values.put("message_sender", userId);
                        if (messageContentType != MessageContentType.GROUP_INFO.ordinal()) {
                            String formattedMessage;
                            if (messageContentType == MessageContentType.TEXT.ordinal()) {
                                formattedMessage = username + " : \n" + messageBody;
                            } else {
                                System.out.println("processMessage addChatMessageToDB messageContentType.getMessageContentType() != MessageContentType.TEXT");
                                formattedMessage = username + " " + this.context.getString(2131493359);
                                String body = messageBody;
                                values.put("uploaded_file_url", body);
                                if (body != null && body.contains("http://")) {
                                    ContentValues contentValues = values;
                                    contentValues.put("file_size", Integer.valueOf(Utils.getFileSize(new URL(body))));
                                }
                            }
                            values.put("text_message", formattedMessage);
                        }
                    }
                    c = null;
                    c = mContentResolver.query(mContentResolver.insert(ChatProviderNew.CONTENT_URI_CHAT, values), null, null, null, null);
                    c.moveToFirst();
                    dbId = c.getInt(c.getColumnIndex("_id"));
                    if (c != null) {
                        c.close();
                    }
                    newMessageEvent = new NewMessageEvent();
                    newMessageEvent.threadId = threadId;
                    newMessageEvent.jsonMessageString = jsonMessageString;
                    if (direction == MyMessageType.OUTGOING_MSG.ordinal()) {
                        newMessageEvent.direction = MyMessageType.OUTGOING_MSG.ordinal();
                    } else {
                        newMessageEvent.direction = MyMessageType.INCOMING_MSG.ordinal();
                    }
                    EventBus.getDefault().postSticky(newMessageEvent);
                    System.out.println("Chat test chat message id " + dbId + " thread id " + threadId);
                }
            } catch (Exception e4) {
                e4.printStackTrace();
            } catch (Throwable th) {
                chatCursor.close();
            }
            chatCursor.close();
            return isExistingMessage;
        }

        private void processInvitation(String jsonString) {
            Log.e("PROCCESS STRATED", "Done");
            Log.e("JSON", jsonString);
            JSONObject topic = new JSONObject(jsonString).getJSONObject("topic");
            JSONArray users = null;
            if (topic.has("users")) {
                users = topic.getJSONArray("users");
            }
            String hashcode = topic.getString("hashcode");
            String title = topic.getString("title");
            String ownerId = topic.getJSONObject("admin").getString(ChatActivity.INTENT_EXTRA_USER_ID);
            new RokhPref(SHAMChatApplication.getInstance().getApplicationContext()).getClientHandle();
            ContentResolver mContentResolver = SHAMChatApplication.getInstance().getApplicationContext().getContentResolver();
            String userId = SHAMChatApplication.getConfig().userId;
            Cursor groupCursor = mContentResolver.query(UserProvider.CONTENT_URI_GROUP, new String[]{FriendGroup.DB_ID}, FriendGroup.CHAT_ROOM_NAME + "=?", new String[]{hashcode}, null);
            boolean isUpdate = false;
            if (groupCursor.getCount() > 0) {
                isUpdate = true;
            }
            String groupName = title;
            String ownerID = ownerId;
            groupCursor.close();
            System.out.println(" invitation group name " + groupName);
            FriendGroup group = new FriendGroup();
            group.id = hashcode;
            group.name = groupName;
            group.recordOwnerId = ownerID;
            group.chatRoomName = hashcode;
            ContentValues values = new ContentValues();
            values.put(FriendGroup.DB_ID, hashcode);
            values.put(FriendGroup.DB_NAME, groupName);
            values.put(FriendGroup.DB_RECORD_OWNER, ownerID);
            values.put(FriendGroup.CHAT_ROOM_NAME, hashcode);
            MessageThread thread = new MessageThread();
            thread.friendId = hashcode;
            thread.isGroupChat = true;
            thread.threadOwner = userId;
            ContentValues vals = new ContentValues();
            vals.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, thread.getThreadId());
            ContentValues contentValues = vals;
            contentValues.put("friend_id", thread.friendId);
            vals.put("read_status", Integer.valueOf(0));
            vals.put("last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
            vals.put("is_group_chat", Integer.valueOf(1));
            contentValues = vals;
            contentValues.put("thread_owner", thread.threadOwner);
            if (isUpdate) {
                mContentResolver.update(UserProvider.CONTENT_URI_GROUP, values, FriendGroup.DB_ID + "=?", new String[]{group.id});
                contentValues = vals;
                mContentResolver.update(ChatProviderNew.CONTENT_URI_THREAD, contentValues, "thread_id=?", new String[]{thread.getThreadId()});
            } else {
                mContentResolver.insert(UserProvider.CONTENT_URI_GROUP, values);
                vals.put("last_message", SHAMChatApplication.getInstance().getString(2131493220));
                vals.put("last_message_direction", Integer.valueOf(MyMessageType.INCOMING_MSG.ordinal()));
                vals.put("last_message_content_type", Integer.valueOf(0));
                mContentResolver.insert(ChatProviderNew.CONTENT_URI_THREAD, vals);
            }
            FriendGroupMember admin = new FriendGroupMember(group.id, ownerID);
            admin.assignUniqueId(userId);
            ContentValues adminCv = new ContentValues();
            adminCv.put(FriendGroupMember.DB_ID, admin.id);
            adminCv.put(FriendGroupMember.DB_FRIEND, admin.friendId);
            adminCv.put(FriendGroupMember.DB_GROUP, admin.groupID);
            adminCv.put(FriendGroupMember.DB_FRIEND_IS_ADMIN, Integer.valueOf(1));
            adminCv.put(FriendGroupMember.PHONE_NUMBER, admin.phoneNumber);
            if (mContentResolver.update(UserProvider.CONTENT_URI_GROUP_MEMBER, adminCv, FriendGroupMember.DB_GROUP + "=? AND " + FriendGroupMember.DB_FRIEND + "=?", new String[]{admin.groupID, ownerID}) == 0) {
                mContentResolver.insert(UserProvider.CONTENT_URI_GROUP_MEMBER, adminCv);
            }
            if (users.length() > 0) {
                for (int i = 0; i < users.length(); i++) {
                    JSONObject Item = users.getJSONObject(i);
                    String memberId = Item.getString(ChatActivity.INTENT_EXTRA_USER_ID);
                    String memberPhone = Item.getString("phone");
                    boolean isAdmin = Item.getBoolean("is_admin");
                    FriendGroupMember friendGroupMember = new FriendGroupMember(group.id, memberId);
                    friendGroupMember.assignUniqueId(userId);
                    ContentValues groupMember = new ContentValues();
                    groupMember.put(FriendGroupMember.DB_ID, friendGroupMember.id);
                    groupMember.put(FriendGroupMember.DB_FRIEND, friendGroupMember.friendId);
                    groupMember.put(FriendGroupMember.DB_GROUP, friendGroupMember.groupID);
                    groupMember.put(FriendGroupMember.PHONE_NUMBER, memberPhone);
                    if (isAdmin) {
                        groupMember.put(FriendGroupMember.DB_FRIEND_IS_ADMIN, Integer.valueOf(1));
                    }
                    groupMember.put(FriendGroupMember.DB_FRIEND_DID_JOIN, Integer.valueOf(1));
                    if (mContentResolver.update(UserProvider.CONTENT_URI_GROUP_MEMBER, groupMember, FriendGroupMember.DB_GROUP + "=? AND " + FriendGroupMember.DB_FRIEND + "=?", new String[]{friendGroupMember.groupID, memberId}) == 0) {
                        mContentResolver.insert(UserProvider.CONTENT_URI_GROUP_MEMBER, groupMember);
                    }
                }
            }
            try {
                Log.e("Subscribing to topic: ", hashcode);
                String[] strArr = new String[1];
                strArr[0] = SHAMChatApplication.getConfig().userId;
                Cursor query = SHAMChatApplication.getMyApplicationContext().getContentResolver().query(UserProvider.CONTENT_URI_USER, null, "userId=?", strArr, null);
                query.moveToFirst();
                User userFromCursor = UserProvider.userFromCursor(query);
                query.close();
                String str = userFromCursor.userId;
                IMqttActionListener actionListener = new ActionListener(SHAMChatApplication.getMyApplicationContext(), Action.SUBSCRIBE$621bd8f2, this.clientHandle, "groups/" + hashcode);
                try {
                    MQTTService.this.mqttClient.subscribe("groups/" + hashcode, 1, null, actionListener);
                } catch (Throwable e) {
                    Log.e(getClass().getCanonicalName(), "Failed to subscribe to" + hashcode + " the client with the handle " + this.clientHandle, e);
                    actionListener.onFailure(null, e);
                } catch (Throwable e2) {
                    Log.e(getClass().getCanonicalName(), "Failed to subscribe to" + hashcode + " the client with the handle " + this.clientHandle, e2);
                    actionListener.onFailure(null, e2);
                }
                NewMessageEvent newMessageEvent = new NewMessageEvent();
                newMessageEvent.threadId = thread.getThreadId();
                newMessageEvent.direction = MyMessageType.INCOMING_MSG.ordinal();
                EventBus.getDefault().postSticky(newMessageEvent);
            } catch (JSONException e3) {
                e3.printStackTrace();
            }
        }

        private static boolean saveOrUpdateThread(String threadId, String receivedJsonMessage, int messageContentType, String friendId, int direction) {
            ContentResolver mContentResolver = SHAMChatApplication.getInstance().getContentResolver();
            int messageType = 0;
            String messageBody = null;
            int isGroupChat = 0;
            try {
                JSONObject SampleMsg = new JSONObject(receivedJsonMessage);
                SampleMsg.getString("packetId");
                SampleMsg.getString("from");
                SampleMsg.getInt("from_userid");
                SampleMsg.getString("to");
                SampleMsg.getString("messageTypeDesc");
                messageType = SampleMsg.getInt("messageType");
                messageBody = SampleMsg.getString("messageBody");
                isGroupChat = SampleMsg.getInt("isGroupChat");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Cursor threadCursor = null;
            try {
                threadCursor = mContentResolver.query(ChatProviderNew.CONTENT_URI_THREAD, new String[]{ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID}, "thread_id=?", new String[]{threadId}, null);
                boolean isExistingThread = false;
                if (threadCursor != null && threadCursor.getCount() > 0) {
                    System.out.println("processMessage addChatMessageToDB saveOrUpdateThread isExisting thread");
                    isExistingThread = true;
                }
                String lastMessage = messageBody;
                if (messageContentType != MessageContentType.GROUP_INFO.ordinal()) {
                    System.out.println("processMessage addChatMessageToDB saveOrUpdateThread NOT group info");
                    switch (C06785.f12x39e42954[Utils.readMessageContentType(messageType).ordinal()]) {
                        case Logger.SEVERE /*1*/:
                            int limit;
                            if (messageBody.length() > 70) {
                                limit = 70;
                            } else {
                                limit = messageBody.length();
                            }
                            lastMessage = messageBody.substring(0, limit);
                            break;
                        case Logger.WARNING /*2*/:
                            lastMessage = SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493279);
                            break;
                        case Logger.INFO /*3*/:
                            lastMessage = SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493282);
                            break;
                        case Logger.CONFIG /*4*/:
                            lastMessage = SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493281);
                            break;
                        case Logger.FINE /*5*/:
                            lastMessage = SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493280);
                            break;
                        case Logger.FINER /*6*/:
                            lastMessage = SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493283);
                            break;
                        default:
                            System.out.println("processMessage addChatMessageToDB saveOrUpdateThread DEFAULT" + lastMessage);
                            break;
                    }
                }
                ContentValues values;
                if (isExistingThread) {
                    values = new ContentValues();
                    values.put("last_message", lastMessage.trim());
                    values.put("last_message_content_type", Integer.valueOf(messageContentType));
                    values.put("read_status", Integer.valueOf(0));
                    values.put("last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
                    values.put("last_message_direction", Integer.valueOf(direction));
                    values.put("thread_owner", SHAMChatApplication.getConfig().userId);
                    values.put("is_group_chat", Integer.valueOf(isGroupChat));
                    ContentValues contentValues = values;
                    mContentResolver.update(ChatProviderNew.CONTENT_URI_THREAD, contentValues, "thread_id=?", new String[]{threadId});
                } else {
                    System.out.println("processMessage addChatMessageToDB saveOrUpdateThread new thread " + lastMessage);
                    values = new ContentValues();
                    values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, threadId);
                    values.put("friend_id", friendId);
                    values.put("read_status", Integer.valueOf(0));
                    values.put("last_message", lastMessage.trim());
                    values.put("last_message_content_type", Integer.valueOf(messageContentType));
                    values.put("last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
                    values.put("is_group_chat", Integer.valueOf(isGroupChat));
                    values.put("thread_owner", SHAMChatApplication.getConfig().userId);
                    values.put("last_message_direction", Integer.valueOf(direction));
                    mContentResolver.insert(ChatProviderNew.CONTENT_URI_THREAD, values);
                }
                threadCursor.close();
                return true;
            } catch (Throwable th) {
                threadCursor.close();
            }
        }

        private static boolean addInviteMessageToGroup(String jsonMessageString) {
            ContentResolver mContentResolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
            JSONArray invitedUsers = null;
            String hashcode = null;
            String newMemberPhone = null;
            int newMemberUserId = 0;
            String packetId = null;
            try {
                JSONObject MessageObject = new JSONObject(jsonMessageString);
                JSONObject topicObject = MessageObject.getJSONObject("topic");
                invitedUsers = MessageObject.getJSONArray("target");
                JSONObject actorObject = MessageObject.getJSONObject("actor");
                hashcode = topicObject.getString("hashcode");
                actorObject.getString("phone");
                actorObject.getInt(ChatActivity.INTENT_EXTRA_USER_ID);
                packetId = MessageObject.getString("packet_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String roomname = hashcode;
            String threadId = SHAMChatApplication.getConfig().userId + "-" + roomname;
            JSONObject item = null;
            for (int i = 0; i < invitedUsers.length(); i++) {
                try {
                    item = invitedUsers.getJSONObject(i);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                try {
                    newMemberPhone = item.getString("phone");
                    newMemberUserId = item.getInt(ChatActivity.INTENT_EXTRA_USER_ID);
                } catch (JSONException e22) {
                    e22.printStackTrace();
                }
                String userId = String.valueOf(newMemberUserId);
                Cursor cursor = mContentResolver.query(UserProvider.CONTENT_URI_GROUP_MEMBER, new String[]{FriendGroupMember.DB_ID}, FriendGroupMember.DB_FRIEND + "=? AND " + FriendGroupMember.DB_GROUP + "=?", new String[]{userId, roomname}, null);
                if (cursor.getCount() == 0) {
                    FriendGroupMember friendGroupMember = new FriendGroupMember(roomname, userId);
                    friendGroupMember.assignUniqueId(SHAMChatApplication.getConfig().userId);
                    ContentValues vals = new ContentValues();
                    vals.put(FriendGroupMember.DB_ID, friendGroupMember.id);
                    vals.put(FriendGroupMember.DB_FRIEND, friendGroupMember.friendId);
                    vals.put(FriendGroupMember.DB_GROUP, friendGroupMember.groupID);
                    vals.put(FriendGroupMember.PHONE_NUMBER, newMemberPhone);
                    vals.put(FriendGroupMember.DB_FRIEND_DID_JOIN, "1");
                    if (mContentResolver.update(UserProvider.CONTENT_URI_GROUP_MEMBER, vals, FriendGroupMember.DB_GROUP + "=? AND " + FriendGroupMember.DB_FRIEND + "=?", new String[]{roomname, userId}) == 0) {
                        mContentResolver.insert(UserProvider.CONTENT_URI_GROUP_MEMBER, vals);
                    }
                    String message = getContactNameFromPhone(SHAMChatApplication.getMyApplicationContext(), newMemberPhone) + " " + SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493161);
                    ContentValues values = new ContentValues();
                    values.put("last_message", message);
                    values.put("last_message_content_type", Integer.valueOf(MessageContentType.GROUP_INFO.ordinal()));
                    values.put("read_status", Integer.valueOf(0));
                    values.put("last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
                    values.put("last_message_direction", Integer.valueOf(MyMessageType.INCOMING_MSG.ordinal()));
                    ContentValues contentValues = values;
                    mContentResolver.update(ChatProviderNew.CONTENT_URI_THREAD, contentValues, "thread_id=?", new String[]{threadId});
                    ContentValues chatmessageVals = new ContentValues();
                    chatmessageVals.put("message_recipient", SHAMChatApplication.getConfig().userId);
                    chatmessageVals.put("message_type", Integer.valueOf(MyMessageType.INCOMING_MSG.ordinal()));
                    chatmessageVals.put("packet_id", packetId);
                    chatmessageVals.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, threadId);
                    chatmessageVals.put("description", BuildConfig.VERSION_NAME);
                    chatmessageVals.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE, Integer.valueOf(MessageContentType.GROUP_INFO.ordinal()));
                    chatmessageVals.put("message_status", Integer.valueOf(MessageStatusType.SEEN.ordinal()));
                    chatmessageVals.put("file_size", Integer.valueOf(0));
                    chatmessageVals.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, roomname);
                    chatmessageVals.put("message_sender", roomname);
                    chatmessageVals.put("text_message", message);
                    if (mContentResolver.update(ChatProviderNew.CONTENT_URI_CHAT, chatmessageVals, "packet_id=?", new String[]{packetId}) == 0) {
                        mContentResolver.insert(ChatProviderNew.CONTENT_URI_CHAT, chatmessageVals);
                    }
                }
                cursor.close();
            }
            NewMessageEvent newMessageEvent = new NewMessageEvent();
            newMessageEvent.threadId = threadId;
            newMessageEvent.jsonMessageString = jsonMessageString;
            newMessageEvent.direction = MyMessageType.INCOMING_MSG.ordinal();
            EventBus.getDefault().postSticky(newMessageEvent);
            EventBus.getDefault().postSticky(new UpdateGroupMembersList(threadId, roomname));
            return true;
        }

        private boolean addLeftRoomToGroup(String jsonMessageString) {
            String canonicalName;
            StringBuilder stringBuilder;
            ContentResolver mContentResolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
            String hashcode = null;
            String leftMemberPhone = null;
            int leftMemberUserId = 0;
            String packetId = null;
            try {
                JSONObject MessageObject = new JSONObject(jsonMessageString);
                JSONObject topicObject = MessageObject.getJSONObject("topic");
                JSONObject actorObject = MessageObject.getJSONObject("actor");
                hashcode = topicObject.getString("hashcode");
                leftMemberPhone = actorObject.getString("phone");
                leftMemberUserId = actorObject.getInt(ChatActivity.INTENT_EXTRA_USER_ID);
                packetId = Utils.makePacketId(String.valueOf(leftMemberUserId));
                MessageObject.put("packet_id", packetId);
                jsonMessageString = MessageObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String groupId = hashcode;
            String userId = String.valueOf(leftMemberUserId);
            String threadId = SHAMChatApplication.getConfig().userId + "-" + groupId;
            mContentResolver.delete(UserProvider.CONTENT_URI_GROUP_MEMBER, FriendGroupMember.DB_GROUP + "=? AND " + FriendGroupMember.DB_FRIEND + "=?", new String[]{groupId, userId});
            String displayMessage = getContactNameFromPhone(SHAMChatApplication.getMyApplicationContext(), leftMemberPhone) + " " + SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493182);
            ContentValues cvs = new ContentValues();
            cvs.put("last_message", displayMessage);
            String str = "thread_id=?";
            mContentResolver.update(ChatProviderNew.CONTENT_URI_THREAD, cvs, r23, new String[]{threadId});
            Log.w("=======================leftuserid", String.valueOf(leftMemberUserId));
            Log.w("=======================currentuserid", this.CURRENT_USER_ID);
            if (String.valueOf(leftMemberUserId).equals(this.CURRENT_USER_ID)) {
                this.clientHandle = new RokhPref(SHAMChatApplication.getInstance().getApplicationContext()).getClientHandle();
                String topic = "groups/" + groupId;
                try {
                    MQTTService.this.mqttClient.unsubscribe(topic, SHAMChatApplication.getInstance().getApplicationContext(), null);
                } catch (MqttSecurityException e2) {
                    canonicalName = getClass().getCanonicalName();
                    stringBuilder = new StringBuilder("Failed to unsubscribe to");
                    Log.e(canonicalName, r23.append(topic).append(" the client with the handle ").append(this.clientHandle).toString(), e2);
                } catch (MqttException e3) {
                    canonicalName = getClass().getCanonicalName();
                    stringBuilder = new StringBuilder("Failed to unsubscribe to");
                    Log.e(canonicalName, r23.append(topic).append(" the client with the handle ").append(this.clientHandle).toString(), e3);
                }
                str = "thread_id=?";
                mContentResolver.delete(ChatProviderNew.CONTENT_URI_THREAD, r23, new String[]{threadId});
            }
            ContentValues chatmessageVals = new ContentValues();
            String str2 = "message_recipient";
            chatmessageVals.put(canonicalName, SHAMChatApplication.getConfig().userId);
            chatmessageVals.put("message_type", Integer.valueOf(MyMessageType.INCOMING_MSG.ordinal()));
            chatmessageVals.put("packet_id", packetId);
            chatmessageVals.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, threadId);
            chatmessageVals.put("description", BuildConfig.VERSION_NAME);
            chatmessageVals.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE, Integer.valueOf(MessageContentType.GROUP_INFO.ordinal()));
            chatmessageVals.put("message_status", Integer.valueOf(MessageStatusType.SEEN.ordinal()));
            chatmessageVals.put("file_size", Integer.valueOf(0));
            chatmessageVals.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, groupId);
            chatmessageVals.put("message_sender", groupId);
            chatmessageVals.put("text_message", displayMessage);
            if (mContentResolver.update(ChatProviderNew.CONTENT_URI_CHAT, chatmessageVals, "packet_id=?", new String[]{packetId}) == 0) {
                mContentResolver.insert(ChatProviderNew.CONTENT_URI_CHAT, chatmessageVals);
            }
            NewMessageEvent newMessageEvent = new NewMessageEvent();
            newMessageEvent.threadId = threadId;
            newMessageEvent.jsonMessageString = jsonMessageString;
            newMessageEvent.direction = MyMessageType.INCOMING_MSG.ordinal();
            newMessageEvent.jsonMessageString = jsonMessageString;
            EventBus.getDefault().postSticky(newMessageEvent);
            EventBus.getDefault().postSticky(new UpdateGroupMembersList(threadId, groupId));
            if (String.valueOf(leftMemberUserId).equals(this.CURRENT_USER_ID)) {
                EventBus.getDefault().postSticky(new CloseGroupActivityEvent(threadId, groupId));
            }
            return true;
        }

        private boolean addKickedFromRoomToGroup(String jsonMessageString) {
            String canonicalName;
            StringBuilder stringBuilder;
            ContentResolver mContentResolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
            String hashcode = null;
            String kickedMemberPhone = null;
            int kickedMemberUserId = 0;
            String packetId = null;
            try {
                JSONObject MessageObject = new JSONObject(jsonMessageString);
                JSONObject topicObject = MessageObject.getJSONObject("topic");
                MessageObject.getJSONObject("actor");
                JSONObject targetObject = MessageObject.getJSONObject("target");
                hashcode = topicObject.getString("hashcode");
                kickedMemberPhone = targetObject.getString("phone");
                kickedMemberUserId = targetObject.getInt(ChatActivity.INTENT_EXTRA_USER_ID);
                packetId = Utils.makePacketId(String.valueOf(kickedMemberUserId));
                MessageObject.put("packet_id", packetId);
                jsonMessageString = MessageObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String groupId = hashcode;
            String userId = String.valueOf(kickedMemberUserId);
            String threadId = SHAMChatApplication.getConfig().userId + "-" + groupId;
            mContentResolver.delete(UserProvider.CONTENT_URI_GROUP_MEMBER, FriendGroupMember.DB_GROUP + "=? AND " + FriendGroupMember.DB_FRIEND + "=?", new String[]{groupId, userId});
            String displayMessage = getContactNameFromPhone(SHAMChatApplication.getMyApplicationContext(), kickedMemberPhone) + " " + SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493178);
            ContentValues cvs = new ContentValues();
            cvs.put("last_message", displayMessage);
            String str = "thread_id=?";
            mContentResolver.update(ChatProviderNew.CONTENT_URI_THREAD, cvs, r23, new String[]{threadId});
            if (String.valueOf(kickedMemberUserId).equals(this.CURRENT_USER_ID)) {
                displayMessage = this.context.getString(2131493449);
                this.clientHandle = new RokhPref(SHAMChatApplication.getInstance().getApplicationContext()).getClientHandle();
                String topic = "groups/" + groupId;
                try {
                    MQTTService.this.mqttClient.unsubscribe(topic, SHAMChatApplication.getInstance().getApplicationContext(), null);
                } catch (MqttSecurityException e2) {
                    canonicalName = getClass().getCanonicalName();
                    stringBuilder = new StringBuilder("Failed to unsubscribe to");
                    Log.e(canonicalName, r23.append(topic).append(" the client with the handle ").append(this.clientHandle).toString(), e2);
                } catch (MqttException e3) {
                    canonicalName = getClass().getCanonicalName();
                    stringBuilder = new StringBuilder("Failed to unsubscribe to");
                    Log.e(canonicalName, r23.append(topic).append(" the client with the handle ").append(this.clientHandle).toString(), e3);
                }
                str = "thread_id=?";
                mContentResolver.delete(ChatProviderNew.CONTENT_URI_THREAD, r23, new String[]{threadId});
            }
            ContentValues chatmessageVals = new ContentValues();
            String str2 = "message_recipient";
            chatmessageVals.put(canonicalName, SHAMChatApplication.getConfig().userId);
            chatmessageVals.put("message_type", Integer.valueOf(MyMessageType.INCOMING_MSG.ordinal()));
            chatmessageVals.put("packet_id", packetId);
            chatmessageVals.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, threadId);
            chatmessageVals.put("description", BuildConfig.VERSION_NAME);
            chatmessageVals.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE, Integer.valueOf(MessageContentType.GROUP_INFO.ordinal()));
            chatmessageVals.put("message_status", Integer.valueOf(MessageStatusType.SEEN.ordinal()));
            chatmessageVals.put("file_size", Integer.valueOf(0));
            chatmessageVals.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, groupId);
            chatmessageVals.put("message_sender", groupId);
            chatmessageVals.put("text_message", displayMessage);
            if (mContentResolver.update(ChatProviderNew.CONTENT_URI_CHAT, chatmessageVals, "packet_id=?", new String[]{packetId}) == 0) {
                mContentResolver.insert(ChatProviderNew.CONTENT_URI_CHAT, chatmessageVals);
            }
            NewMessageEvent newMessageEvent = new NewMessageEvent();
            newMessageEvent.threadId = threadId;
            newMessageEvent.jsonMessageString = jsonMessageString;
            newMessageEvent.direction = MyMessageType.INCOMING_MSG.ordinal();
            EventBus.getDefault().postSticky(newMessageEvent);
            EventBus.getDefault().postSticky(new UpdateGroupMembersList(threadId, groupId));
            if (String.valueOf(kickedMemberUserId).equals(this.CURRENT_USER_ID)) {
                EventBus.getDefault().postSticky(new CloseGroupActivityEvent(threadId, groupId));
            }
            return true;
        }

        private static String getContactNameFromPhone(Context context, String number) {
            String[] projection = new String[]{"display_name", "_id"};
            Cursor cursor = context.getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number)), projection, null, null, null);
            if (cursor.moveToFirst()) {
                String contactId = cursor.getString(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("display_name"));
                Contacts.openContactPhotoInputStream(context.getContentResolver(), ContentUris.withAppendedId(Contacts.CONTENT_URI, Long.parseLong(contactId)));
                Log.v("ffnet", "Started : Contact Found @ " + number);
                Log.v("ffnet", "Started : Contact name  = " + name);
                Log.v("ffnet", "Started : Contact id    = " + contactId);
                cursor.close();
                return name;
            }
            Log.v("ffnet", "Started : Contact Not Found @ " + number);
            cursor.close();
            return number;
        }
    }

    private class NetworkConnectionIntentReceiver extends BroadcastReceiver {
        private NetworkConnectionIntentReceiver() {
        }

        public final void onReceive(Context ctx, Intent intent) {
            WakeLock wl = ((PowerManager) MQTTService.this.getSystemService("power")).newWakeLock(1, "MQTT");
            wl.acquire();
            try {
                if (MQTTService.this.isOnline()) {
                    boolean doConnect = true;
                    try {
                        if (MQTTService.this.mqttClient != null && MQTTService.this.connectRequestCount > 0) {
                            doConnect = false;
                        }
                    } catch (Exception e) {
                    }
                    if (doConnect) {
                        MQTTService.this.mqttClient.disconnect();
                        MQTTService.this.connectionStatus$4da8916d = MQTTConnectionStatus.NOTCONNECTED_USERDISCONNECT$4da8916d;
                        MQTTService.this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.DISCONNECTED$6abac5f4);
                        Intent intent2 = new Intent();
                        MQTTService.this.connectionStatus$4da8916d = MQTTConnectionStatus.CONNECTING$4da8916d;
                        MQTTService.this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.CONNECTING$6abac5f4);
                        MQTTService.this.defineConnectionToBroker(MQTTService.this.brokerHostName);
                        MQTTService.this.handleStart$7a9ca511();
                    }
                } else {
                    MQTTService.this.connectionStatus$4da8916d = MQTTConnectionStatus.NOTCONNECTED_WAITINGFORINTERNET$4da8916d;
                    MQTTService.this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.DISCONNECTED$6abac5f4);
                    MQTTService.this.isConnectedtoMqtt = false;
                    MQTTService.this.scheduleNextPing();
                }
            } catch (Exception e1) {
                Log.e("mqtt", "disconnect failed - exception", e1);
            } catch (Throwable th) {
                wl.release();
            }
            wl.release();
        }
    }

    public class PingSender extends BroadcastReceiver {

        /* renamed from: com.rokhgroup.mqtt.MQTTService.PingSender.1 */
        class C06791 implements IMqttActionListener {
            C06791() {
            }

            public final void onSuccess(IMqttToken arg0) {
                Log.e("mqtt", "ping publish success");
            }

            public final void onFailure(IMqttToken arg0, Throwable arg1) {
                Log.e("mqtt", "ping publish failed");
                if (MQTTService.this.connectRequestCount != 0) {
                    MQTTService.this.connectRequestCount = 0;
                }
                try {
                    MQTTService.this.mqttClient.disconnect();
                    MQTTService.this.connectionStatus$4da8916d = MQTTConnectionStatus.NOTCONNECTED_WAITINGFORINTERNET$4da8916d;
                    MQTTService.this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.DISCONNECTED$6abac5f4);
                } catch (Exception e) {
                }
                MQTTService.this.isConnectedtoMqtt = false;
                Intent intent = new Intent();
                boolean doConnect = true;
                try {
                    if (MQTTService.this.connectRequestCount > 0) {
                        doConnect = false;
                    }
                } catch (Exception e2) {
                }
                if (doConnect) {
                    MQTTService.this.connectionStatus$4da8916d = MQTTConnectionStatus.CONNECTING$4da8916d;
                    MQTTService.this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.CONNECTING$6abac5f4);
                    MQTTService.this.defineConnectionToBroker(MQTTService.this.brokerHostName);
                    MQTTService.this.handleStart$7a9ca511();
                }
            }
        }

        public void onReceive(Context context, Intent intent) {
            try {
                String pingMessage = "ping";
                String[] args = new String[]{pingMessage, ("events/" + SHAMChatApplication.getConfig().userId) + ";qos:1;retained:false"};
                if (MQTTService.this.mqttClient != null) {
                    MQTTService.this.mqttClient.publish(topic, pingMessage.getBytes(), 1, false, null, new C06791());
                    MQTTService.this.scheduleNextPing();
                    return;
                }
                Log.e("mqtt", "ping mqttclient null");
                throw new IOException("ping mqttclient null");
            } catch (Exception e) {
                if (MQTTService.this.connectRequestCount != 0) {
                    MQTTService.this.connectRequestCount = 0;
                }
                try {
                    MQTTService.this.mqttClient.disconnect();
                    MQTTService.this.connectionStatus$4da8916d = MQTTConnectionStatus.NOTCONNECTED_WAITINGFORINTERNET$4da8916d;
                    MQTTService.this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.DISCONNECTED$6abac5f4);
                } catch (Exception e2) {
                }
                MQTTService.this.isConnectedtoMqtt = false;
                Intent intent2 = new Intent();
                boolean doConnect = true;
                try {
                    if (MQTTService.this.connectRequestCount > 0) {
                        doConnect = false;
                    }
                } catch (Exception e3) {
                }
                if (doConnect) {
                    MQTTService.this.connectionStatus$4da8916d = MQTTConnectionStatus.CONNECTING$4da8916d;
                    MQTTService.this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.CONNECTING$6abac5f4);
                    MQTTService.this.defineConnectionToBroker(MQTTService.this.brokerHostName);
                    MQTTService.this.handleStart$7a9ca511();
                }
            }
        }
    }

    public MQTTService() {
        this.handler = new C06741();
        this.connectionStatus$4da8916d = MQTTConnectionStatus.INITIAL$4da8916d;
        this.brokerHostName = BuildConfig.VERSION_NAME;
        this.brokerPortNumber = 1883;
        this.cleanStart = false;
        this.qualitiesOfService = new int[]{1};
        this.keepAliveSeconds = (short) 60;
        this.mqttClientId = null;
        this.clientHandle = null;
        this.mqttClient = null;
        this.changeListener = new ChangeListener();
        this.connectRequestCount = 0;
        this.isConnectedtoMqtt = false;
        this.dataCache = new Hashtable();
    }

    static {
        serviceRunning = false;
    }

    private static synchronized boolean isRunning() {
        boolean z = true;
        synchronized (MQTTService.class) {
            if (!serviceRunning) {
                serviceRunning = true;
                z = false;
            }
        }
        return z;
    }

    public void onCreate() {
        super.onCreate();
        this.androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this.handler);
        this.connectionStatus$4da8916d = MQTTConnectionStatus.INITIAL$4da8916d;
        this.mBinder = new LocalBinder(this);
        this.brokerHostName = "sp.rabtcdn.com";
        this.mqttClientId = SHAMChatApplication.getConfig().userId;
        this.brokerPortNumber = Integer.valueOf("1883").intValue();
        this.Session = new RokhPref(SHAMChatApplication.getInstance().getApplicationContext());
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        this.dataEnabledReceiver = new BackgroundDataChangeIntentReceiver();
        registerReceiver(this.dataEnabledReceiver, new IntentFilter("android.net.conn.BACKGROUND_DATA_SETTING_CHANGED"));
        try {
            EventBus.getDefault().register(this, true, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        defineConnectionToBroker(this.brokerHostName);
    }

    public void onStart(Intent intent, int startId) {
        new Thread(new C06752(intent, startId), "BackgroundService").start();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning()) {
            new Thread(new C06763(intent, startId), "BackgroundService").start();
        }
        return 1;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    final synchronized void handleStart$7a9ca511() {
        /*
        r5 = this;
        monitor-enter(r5);
        r2 = r5.mqttClient;	 Catch:{ all -> 0x0029 }
        if (r2 != 0) goto L_0x000a;
    L_0x0005:
        r5.stopSelf();	 Catch:{ all -> 0x0029 }
    L_0x0008:
        monitor-exit(r5);
        return;
    L_0x000a:
        r2 = "connectivity";
        r2 = r5.getSystemService(r2);	 Catch:{ all -> 0x0029 }
        r2 = (android.net.ConnectivityManager) r2;	 Catch:{ all -> 0x0029 }
        r2 = r2.getBackgroundDataSetting();	 Catch:{ all -> 0x0029 }
        if (r2 != 0) goto L_0x002c;
    L_0x0018:
        r2 = com.rokhgroup.mqtt.MQTTService.MQTTConnectionStatus.NOTCONNECTED_DATADISABLED$4da8916d;	 Catch:{ all -> 0x0029 }
        r5.connectionStatus$4da8916d = r2;	 Catch:{ all -> 0x0029 }
        r2 = r5.connection;	 Catch:{ all -> 0x0029 }
        r3 = com.rokhgroup.mqtt.Connection.ConnectionStatus.DISCONNECTED$6abac5f4;	 Catch:{ all -> 0x0029 }
        r2.changeConnectionStatus$19646d7f(r3);	 Catch:{ all -> 0x0029 }
        r2 = "Not connected - background data disabled";
        r5.broadcastServiceStatus(r2);	 Catch:{ all -> 0x0029 }
        goto L_0x0008;
    L_0x0029:
        r2 = move-exception;
        monitor-exit(r5);
        throw r2;
    L_0x002c:
        r0 = 1;
        r2 = r5.connectRequestCount;	 Catch:{ Exception -> 0x00a6 }
        r3 = 1;
        if (r2 <= r3) goto L_0x0033;
    L_0x0032:
        r0 = 0;
    L_0x0033:
        if (r0 == 0) goto L_0x0057;
    L_0x0035:
        r2 = com.rokhgroup.mqtt.MQTTService.MQTTConnectionStatus.CONNECTING$4da8916d;	 Catch:{ all -> 0x0029 }
        r5.connectionStatus$4da8916d = r2;	 Catch:{ all -> 0x0029 }
        r2 = r5.connection;	 Catch:{ all -> 0x0029 }
        r3 = com.rokhgroup.mqtt.Connection.ConnectionStatus.CONNECTING$6abac5f4;	 Catch:{ all -> 0x0029 }
        r2.changeConnectionStatus$19646d7f(r3);	 Catch:{ all -> 0x0029 }
        r2 = r5.isOnline();	 Catch:{ all -> 0x0029 }
        if (r2 == 0) goto L_0x0095;
    L_0x0046:
        r2 = com.rokhgroup.mqtt.MQTTService.MQTTConnectionStatus.CONNECTING$4da8916d;	 Catch:{ all -> 0x0029 }
        r5.connectionStatus$4da8916d = r2;	 Catch:{ all -> 0x0029 }
        r2 = r5.connection;	 Catch:{ all -> 0x0029 }
        r3 = com.rokhgroup.mqtt.Connection.ConnectionStatus.CONNECTING$6abac5f4;	 Catch:{ all -> 0x0029 }
        r2.changeConnectionStatus$19646d7f(r3);	 Catch:{ all -> 0x0029 }
        r2 = r5.connectToBroker();	 Catch:{ all -> 0x0029 }
        if (r2 == 0) goto L_0x0057;
    L_0x0057:
        r2 = r5.netConnReceiver;	 Catch:{ all -> 0x0029 }
        if (r2 != 0) goto L_0x007c;
    L_0x005b:
        r2 = new com.rokhgroup.mqtt.MQTTService$NetworkConnectionIntentReceiver;	 Catch:{ all -> 0x0029 }
        r3 = 0;
        r2.<init>(r3);	 Catch:{ all -> 0x0029 }
        r5.netConnReceiver = r2;	 Catch:{ all -> 0x0029 }
        r1 = new android.content.IntentFilter;	 Catch:{ all -> 0x0029 }
        r1.<init>();	 Catch:{ all -> 0x0029 }
        r2 = "android.net.wifi.supplicant.CONNECTION_CHANGE";
        r1.addAction(r2);	 Catch:{ all -> 0x0029 }
        r2 = "android.net.wifi.supplicant.STATE_CHANGE";
        r1.addAction(r2);	 Catch:{ all -> 0x0029 }
        r2 = "android.net.conn.CONNECTIVITY_CHANGE";
        r1.addAction(r2);	 Catch:{ all -> 0x0029 }
        r2 = r5.netConnReceiver;	 Catch:{ all -> 0x0029 }
        r5.registerReceiver(r2, r1);	 Catch:{ all -> 0x0029 }
    L_0x007c:
        r2 = r5.pingSender;	 Catch:{ all -> 0x0029 }
        if (r2 != 0) goto L_0x0008;
    L_0x0080:
        r2 = new com.rokhgroup.mqtt.MQTTService$PingSender;	 Catch:{ all -> 0x0029 }
        r2.<init>();	 Catch:{ all -> 0x0029 }
        r5.pingSender = r2;	 Catch:{ all -> 0x0029 }
        r2 = r5.pingSender;	 Catch:{ all -> 0x0029 }
        r3 = new android.content.IntentFilter;	 Catch:{ all -> 0x0029 }
        r4 = "com.rokhgroup.mqtt.PING";
        r3.<init>(r4);	 Catch:{ all -> 0x0029 }
        r5.registerReceiver(r2, r3);	 Catch:{ all -> 0x0029 }
        goto L_0x0008;
    L_0x0095:
        r2 = com.rokhgroup.mqtt.MQTTService.MQTTConnectionStatus.NOTCONNECTED_WAITINGFORINTERNET$4da8916d;	 Catch:{ all -> 0x0029 }
        r5.connectionStatus$4da8916d = r2;	 Catch:{ all -> 0x0029 }
        r2 = r5.connection;	 Catch:{ all -> 0x0029 }
        r3 = com.rokhgroup.mqtt.Connection.ConnectionStatus.DISCONNECTED$6abac5f4;	 Catch:{ all -> 0x0029 }
        r2.changeConnectionStatus$19646d7f(r3);	 Catch:{ all -> 0x0029 }
        r2 = "Waiting for network connection";
        r5.broadcastServiceStatus(r2);	 Catch:{ all -> 0x0029 }
        goto L_0x0057;
    L_0x00a6:
        r2 = move-exception;
        goto L_0x0033;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.rokhgroup.mqtt.MQTTService.handleStart$7a9ca511():void");
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        disconnectFromBroker();
        broadcastServiceStatus("Disconnected");
        for (Connection connection : Connections.getInstance(this).connections.values()) {
            connection.registerChangeListener(this.changeListener);
            connection.client.unregisterResources();
        }
        if (this.dataEnabledReceiver != null) {
            unregisterReceiver(this.dataEnabledReceiver);
            this.dataEnabledReceiver = null;
        }
        if (this.mBinder != null) {
            this.mBinder.mService = null;
            this.mBinder = null;
        }
    }

    private void broadcastServiceStatus(String statusDescription) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.rokhgroup.mqtt.STATUS");
        broadcastIntent.putExtra("com.rokhgroup.mqtt.STATUS_MSG", statusDescription);
        sendBroadcast(broadcastIntent);
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    private void defineConnectionToBroker(String brokerHostName) {
        Persistence databasePersistence = new Persistence(this);
        SQLiteDatabase readableDatabase = databasePersistence.getReadableDatabase();
        int simpleQueryForLong = (int) readableDatabase.compileStatement("SELECT COUNT(*) FROM connections").simpleQueryForLong();
        readableDatabase.close();
        if (simpleQueryForLong > 3) {
            readableDatabase = databasePersistence.getWritableDatabase();
            readableDatabase.delete("connections", null, null);
            readableDatabase.close();
        }
        this.conOpt = new MqttConnectOptions();
        this.connectRequestCount++;
        String uri = "tcp://" + brokerHostName + ":" + this.brokerPortNumber;
        try {
            this.clientHandle = "user" + this.mqttClientId;
            RokhPref rokhPref = this.Session;
            rokhPref.editor.putString(MqttServiceConstants.CLIENT_HANDLE, this.clientHandle);
            rokhPref.editor.commit();
            this.mqttClient = new MqttAndroidClient((Context) this, uri, this.clientHandle, new MemoryPersistence());
            this.connection = new Connection(this.clientHandle, this.mqttClientId, brokerHostName, this.brokerPortNumber, this, this.mqttClient, false);
            this.connection.registerChangeListener(this.changeListener);
            new String[1][0] = this.mqttClientId;
            this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.CONNECTING$6abac5f4);
            this.connectionStatus$4da8916d = MQTTConnectionStatus.CONNECTING$4da8916d;
            this.conOpt.setCleanSession(this.cleanStart);
            this.mqttClient.setCallback(new MqttCallbackHandler(this, this.clientHandle));
            this.mqttClient.setTraceCallback(new MqttTraceCallback());
            this.connection.conOpt = this.conOpt;
            Connections instance = Connections.getInstance(this);
            Connection connection = this.connection;
            instance.connections.put(connection.clientHandle, connection);
            try {
                int i;
                String str;
                ContentValues contentValues;
                String str2;
                ContentValues contentValues2;
                long insert;
                Persistence persistence = instance.persistence;
                MqttConnectOptions mqttConnectOptions = connection.conOpt;
                MqttMessage willMessage = mqttConnectOptions.getWillMessage();
                SQLiteDatabase writableDatabase = persistence.getWritableDatabase();
                ContentValues contentValues3 = new ContentValues();
                contentValues3.put("host", connection.host);
                contentValues3.put("port", Integer.valueOf(connection.port));
                contentValues3.put("clientID", connection.clientId);
                String str3 = "ssl";
                if (connection.sslConnection) {
                    i = 1;
                } else {
                    i = 0;
                }
                contentValues3.put(str3, Integer.valueOf(i));
                contentValues3.put("keepalive", Integer.valueOf(mqttConnectOptions.getKeepAliveInterval()));
                contentValues3.put("timeout", Integer.valueOf(mqttConnectOptions.getConnectionTimeout()));
                contentValues3.put("username", mqttConnectOptions.getUserName());
                contentValues3.put("topic", mqttConnectOptions.getWillDestination());
                char[] password = mqttConnectOptions.getPassword();
                contentValues3.put("cleanSession", Integer.valueOf(mqttConnectOptions.isCleanSession() ? 1 : 0));
                contentValues3.put("password", password != null ? String.valueOf(password) : null);
                contentValues3.put(AddFavoriteTextActivity.EXTRA_MESSAGE, willMessage != null ? new String(willMessage.getPayload()) : null);
                contentValues3.put(MqttServiceConstants.QOS, Integer.valueOf(willMessage != null ? willMessage.getQos() : 0));
                if (willMessage == null) {
                    str = MqttServiceConstants.RETAINED;
                    contentValues = contentValues3;
                } else {
                    str = MqttServiceConstants.RETAINED;
                    if (willMessage.isRetained()) {
                        str2 = str;
                        contentValues2 = contentValues3;
                        i = 1;
                        contentValues2.put(str2, Integer.valueOf(i));
                        insert = writableDatabase.insert("connections", null, contentValues3);
                        writableDatabase.close();
                        if (insert != -1) {
                            throw new PersistenceException("Failed to persist connection: " + connection.clientHandle);
                        }
                        connection.persistenceId = insert;
                        return;
                    }
                    contentValues = contentValues3;
                }
                contentValues2 = contentValues;
                str2 = str;
                i = 0;
                contentValues2.put(str2, Integer.valueOf(i));
                insert = writableDatabase.insert("connections", null, contentValues3);
                writableDatabase.close();
                if (insert != -1) {
                    connection.persistenceId = insert;
                    return;
                }
                throw new PersistenceException("Failed to persist connection: " + connection.clientHandle);
            } catch (PersistenceException e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            this.mqttClient = null;
            this.connectionStatus$4da8916d = MQTTConnectionStatus.NOTCONNECTED_UNKNOWNREASON$4da8916d;
            this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.DISCONNECTED$6abac5f4);
        }
    }

    private boolean connectToBroker() {
        boolean z = true;
        this.isConnectedtoMqtt = true;
        try {
            if (this.mqttClient != null) {
                this.mqttClient.connect(this.conOpt, null, new C06774());
                if (this.connection.status$6abac5f4 != ConnectionStatus.CONNECTED$6abac5f4) {
                    z = false;
                }
                if (z) {
                    return this.mqttClient.isConnected();
                }
                return false;
            }
            Log.e("mqtt", "connectToBroker mqttclient = null");
            throw new IOException("connectToBroker mqttclient = null");
        } catch (IllegalArgumentException e) {
            Log.e("mqtt", "invalid handle exception");
        } catch (Exception e2) {
            if (this.connectRequestCount > 0) {
                this.connectRequestCount--;
            }
            this.isConnectedtoMqtt = false;
            this.connection.changeConnectionStatus$19646d7f(ConnectionStatus.ERROR$6abac5f4);
            this.connectionStatus$4da8916d = MQTTConnectionStatus.NOTCONNECTED_UNKNOWNREASON$4da8916d;
            broadcastServiceStatus("Unable to connect");
            Intent intent = new Intent();
            intent.setClassName(getApplicationContext(), "org.eclipse.paho.android.service.sample.ConnectionDetails");
            intent.putExtra("handle", this.clientHandle);
            NotifySimple.notifcation$34410889(getApplicationContext(), "Unable to connect - will retry later", intent);
            scheduleNextPing();
            Log.e(getClass().getCanonicalName(), "MqttException Occured", e2);
            return false;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void disconnectFromBroker() {
        /*
        r6 = this;
        r5 = 0;
        r4 = 0;
        r2 = r6.netConnReceiver;	 Catch:{ Exception -> 0x0028 }
        if (r2 == 0) goto L_0x000e;
    L_0x0006:
        r2 = r6.netConnReceiver;	 Catch:{ Exception -> 0x0028 }
        r6.unregisterReceiver(r2);	 Catch:{ Exception -> 0x0028 }
        r2 = 0;
        r6.netConnReceiver = r2;	 Catch:{ Exception -> 0x0028 }
    L_0x000e:
        r2 = r6.pingSender;	 Catch:{ Exception -> 0x0028 }
        if (r2 == 0) goto L_0x001a;
    L_0x0012:
        r2 = r6.pingSender;	 Catch:{ Exception -> 0x0028 }
        r6.unregisterReceiver(r2);	 Catch:{ Exception -> 0x0028 }
        r2 = 0;
        r6.pingSender = r2;	 Catch:{ Exception -> 0x0028 }
    L_0x001a:
        r2 = r6.mqttClient;	 Catch:{ MqttPersistenceException -> 0x0031, MqttException -> 0x003e }
        if (r2 == 0) goto L_0x0023;
    L_0x001e:
        r2 = r6.mqttClient;	 Catch:{ MqttPersistenceException -> 0x0031, MqttException -> 0x003e }
        r2.disconnect();	 Catch:{ MqttPersistenceException -> 0x0031, MqttException -> 0x003e }
    L_0x0023:
        r6.mqttClient = r4;
        r6.isConnectedtoMqtt = r5;
    L_0x0027:
        return;
    L_0x0028:
        r1 = move-exception;
        r2 = "mqtt";
        r3 = "unregister failed";
        android.util.Log.e(r2, r3, r1);
        goto L_0x001a;
    L_0x0031:
        r0 = move-exception;
        r2 = "mqtt";
        r3 = "disconnect failed - persistence exception";
        android.util.Log.e(r2, r3, r0);	 Catch:{ all -> 0x004b }
        r6.mqttClient = r4;
        r6.isConnectedtoMqtt = r5;
        goto L_0x0027;
    L_0x003e:
        r0 = move-exception;
        r2 = "mqtt";
        r3 = "disconnect failed - persistence exception";
        android.util.Log.e(r2, r3, r0);	 Catch:{ all -> 0x004b }
        r6.mqttClient = r4;
        r6.isConnectedtoMqtt = r5;
        goto L_0x0027;
    L_0x004b:
        r2 = move-exception;
        r6.mqttClient = r4;
        r6.isConnectedtoMqtt = r5;
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.rokhgroup.mqtt.MQTTService.disconnectFromBroker():void");
    }

    private void scheduleNextPing() {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.rokhgroup.mqtt.PING"), 134217728);
        Calendar wakeUpTime = Calendar.getInstance();
        wakeUpTime.add(13, this.keepAliveSeconds);
        ((AlarmManager) getSystemService(NotificationCompatApi21.CATEGORY_ALARM)).set(0, wakeUpTime.getTimeInMillis(), pendingIntent);
    }

    public void onEventBackgroundThread(NewMessageEvent event) {
        String jsonMessageString = event.jsonMessageString;
        if (!event.consumed && jsonMessageString != null) {
            Notify.notifcation(getApplicationContext(), jsonMessageString);
        }
    }

    public void onEventBackgroundThread(FileUploadingProgressEvent event) {
        Boolean isGroup = Boolean.valueOf(false);
        if (event.threadId.contains("g")) {
            isGroup = Boolean.valueOf(true);
        }
        if (event.uploadedPercentage == 100 && isGroup.booleanValue()) {
            Cursor cursor = getContentResolver().query(ChatProviderNew.CONTENT_URI_CHAT, null, "thread_id=? AND packet_id=?", new String[]{event.threadId, event.packetId}, null);
            this.chatProvider = new ChatProviderNew();
            if (this.chatProvider != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ChatMessage message = ChatProviderNew.getChatMessageByCursor(cursor);
                    ContentResolver mContentResolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
                    if (message.threadId.equals(event.threadId)) {
                        String uploadedUrl = message.uploadedFileUrl;
                        User user = null;
                        FriendGroup group = null;
                        try {
                            Cursor cursorMe = mContentResolver.query(UserProvider.CONTENT_URI_USER, null, "userId=?", new String[]{SHAMChatApplication.getConfig().userId}, null);
                            cursorMe.moveToFirst();
                            user = UserProvider.userFromCursor(cursorMe);
                            cursorMe.close();
                            ContentResolver contentResolver = getContentResolver();
                            Uri uri = UserProvider.CONTENT_URI_GROUP;
                            String str = FriendGroup.DB_ID + "=?";
                            String[] strArr = new String[1];
                            strArr[0] = message.recipient;
                            group = UserProvider.groupFromCursor(contentResolver.query(uri, null, str, strArr, null));
                        } catch (Exception e) {
                        }
                        JSONObject jsonMessageObject = new JSONObject();
                        try {
                            jsonMessageObject.put("packet_type", AddFavoriteTextActivity.EXTRA_MESSAGE);
                            jsonMessageObject.put("to", message.recipient);
                            jsonMessageObject.put("from", user.mobileNo);
                            jsonMessageObject.put("from_userid", user.userId);
                            jsonMessageObject.put("messageBody", uploadedUrl);
                            jsonMessageObject.put("messageType", message.messageContentType.ordinal());
                            jsonMessageObject.put("messageTypeDesc", message.description);
                            jsonMessageObject.put("timestamp", Utils.getTimeStamp());
                            jsonMessageObject.put("groupAlias", group.name);
                            jsonMessageObject.put("latitude", message.latitude);
                            jsonMessageObject.put("longitude", message.longitude);
                            jsonMessageObject.put("isGroupChat", 1);
                            jsonMessageObject.put("packetId", message.packetId);
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                        this.jobManager.addJobInBackground(new PublishToTopicJob(jsonMessageObject.toString(), "groups/" + message.recipient));
                    }
                }
            }
            cursor.close();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService("connectivity");
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }
}
