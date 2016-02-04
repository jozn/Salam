package com.shamchat.jobs;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.rokhgroup.mqtt.Connections;
import com.rokhgroup.mqtt.Notify;
import com.rokhgroup.utils.RokhPref;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.adapters.MyMessageType;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.events.NewMessageEvent;
import com.shamchat.models.ChatMessage.MessageStatusType;
import com.shamchat.utils.Utils;
import de.greenrobot.event.EventBus;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public final class PublishToTopicJob extends Job {
    private static final AtomicInteger jobCounter;
    boolean DEBUG;
    public String from;
    private final int id;
    int isGroupChat;
    boolean isRetry;
    private String jsonMessageString;
    String latitude;
    String longitude;
    String messageBody;
    int messageType;
    public String messageTypeDesc;
    public String packetId;
    String packetType;
    public String threadId;
    String timestamp;
    public String to;
    private String topicName;

    /* renamed from: com.shamchat.jobs.PublishToTopicJob.1 */
    class C10871 implements IMqttActionListener {
        C10871() {
        }

        public final void onSuccess(IMqttToken arg0) {
            PublishToTopicJob.this.jsonMessageString;
            if (PublishToTopicJob.this.messageType == MessageContentType.TEXT.ordinal() || PublishToTopicJob.this.messageType == MessageContentType.STICKER.ordinal()) {
                PublishToTopicJob.access$100$5fb95331(PublishToTopicJob.this.packetId, MessageStatusType.DELIVERED);
            } else {
                PublishToTopicJob.access$100$5fb95331(PublishToTopicJob.this.packetId, MessageStatusType.DELIVERED);
            }
        }

        public final void onFailure(IMqttToken arg0, Throwable arg1) {
            if (PublishToTopicJob.this.messageType == MessageContentType.IMAGE.ordinal() || PublishToTopicJob.this.messageType == MessageContentType.VIDEO.ordinal() || PublishToTopicJob.this.messageType == MessageContentType.LOCATION.ordinal() || PublishToTopicJob.this.messageType == MessageContentType.VOICE_RECORD.ordinal()) {
                PublishToTopicJob.access$100$5fb95331(PublishToTopicJob.this.packetId, MessageStatusType.FAILED);
            } else {
                PublishToTopicJob.access$100$5fb95331(PublishToTopicJob.this.packetId, MessageStatusType.FAILED);
            }
            String actionTaken = SHAMChatApplication.getMyApplicationContext().getString(2131493273);
            if (PublishToTopicJob.this.DEBUG) {
                Notify.toast$4475a3b4(SHAMChatApplication.getMyApplicationContext(), actionTaken);
            }
        }
    }

    /* renamed from: com.shamchat.jobs.PublishToTopicJob.2 */
    static /* synthetic */ class C10882 {
        static final /* synthetic */ int[] f28x39e42954;

        static {
            f28x39e42954 = new int[MessageContentType.values().length];
            try {
                f28x39e42954[MessageContentType.TEXT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f28x39e42954[MessageContentType.IMAGE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f28x39e42954[MessageContentType.VIDEO.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f28x39e42954[MessageContentType.STICKER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f28x39e42954[MessageContentType.LOCATION.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f28x39e42954[MessageContentType.VOICE_RECORD.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    static /* synthetic */ void access$100$5fb95331(String x1, MessageStatusType x2) {
        Throwable th;
        ContentResolver contentResolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
        Cursor query;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("message_status", Integer.valueOf(x2.ordinal()));
            contentValues.put("message_last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
            query = contentResolver.query(ChatProviderNew.CONTENT_URI_CHAT, new String[]{"_id", ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID}, "packet_id=?", new String[]{x1}, null);
            try {
                if (query.getCount() > 0) {
                    query.moveToFirst();
                    String num = Integer.toString(query.getInt(query.getColumnIndex("_id")));
                    String string = query.getString(query.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID));
                    contentResolver.update(Uri.parse("content://org.zamin.androidclient.provider.Messages/chat_message/" + num), contentValues, "_id = ? AND message_status != " + MessageStatusType.SEEN.ordinal() + " AND message_type = " + MyMessageType.OUTGOING_MSG.ordinal(), new String[]{num});
                    SHAMChatApplication.getInstance().jobManager.addJobInBackground(new MessageStateChangedJob(string, x1, x2.type));
                }
                query.close();
            } catch (Throwable th2) {
                th = th2;
                query.close();
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            query = null;
            query.close();
            throw th;
        }
    }

    static {
        jobCounter = new AtomicInteger(0);
    }

    public PublishToTopicJob(String jsonMessageString, String topicName) {
        Params params = new Params(1000);
        params.persistent = true;
        params.requiresNetwork = true;
        super(params);
        this.DEBUG = false;
        this.isRetry = false;
        this.threadId = null;
        this.packetId = null;
        this.from = null;
        this.to = null;
        this.messageTypeDesc = null;
        this.messageType = 0;
        this.messageBody = null;
        this.latitude = null;
        this.timestamp = null;
        this.longitude = null;
        this.packetType = null;
        this.isGroupChat = 0;
        this.id = jobCounter.incrementAndGet();
        initClass(jsonMessageString, topicName);
    }

    public PublishToTopicJob(String jsonMessageString, String topicName, byte isRetry) {
        Params params = new Params(1000);
        params.persistent = true;
        params.requiresNetwork = true;
        super(params);
        this.DEBUG = false;
        this.isRetry = false;
        this.threadId = null;
        this.packetId = null;
        this.from = null;
        this.to = null;
        this.messageTypeDesc = null;
        this.messageType = 0;
        this.messageBody = null;
        this.latitude = null;
        this.timestamp = null;
        this.longitude = null;
        this.packetType = null;
        this.isGroupChat = 0;
        this.id = jobCounter.incrementAndGet();
        this.isRetry = true;
        initClass(jsonMessageString, topicName);
    }

    private void initClass(String jsonMessageString, String topicName) {
        this.jsonMessageString = jsonMessageString;
        this.topicName = topicName;
        try {
            JSONObject SampleMsg = new JSONObject(jsonMessageString);
            this.packetId = SampleMsg.getString("packetId");
            this.packetType = SampleMsg.getString("packet_type");
            this.from = SampleMsg.getString("from");
            this.to = SampleMsg.getString("to");
            this.messageTypeDesc = SampleMsg.getString("messageTypeDesc");
            this.timestamp = SampleMsg.getString("timestamp");
            this.messageType = SampleMsg.getInt("messageType");
            if (this.messageType == MessageContentType.LOCATION.ordinal()) {
                this.latitude = SampleMsg.getString("latitude");
                this.longitude = SampleMsg.getString("longitude");
            }
            this.messageBody = SampleMsg.getString("messageBody");
            this.isGroupChat = SampleMsg.getInt("isGroupChat");
            this.threadId = SHAMChatApplication.getConfig().userId + "-" + this.to;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public final void onAdded() {
        if (!this.isRetry) {
            if (this.messageType == MessageContentType.TEXT.ordinal() || this.messageType == MessageContentType.STICKER.ordinal()) {
                int ordinal = MyMessageType.OUTGOING_MSG.ordinal();
                int ordinal2 = MessageStatusType.SENDING.ordinal();
                MessageContentType.TEXT.ordinal();
                addChatMessageToDB$2f10955c(ordinal, ordinal2, this.jsonMessageString);
            }
        }
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        String[] args = new String[]{this.jsonMessageString, this.topicName + ";qos:1;retained:false"};
        MqttAndroidClient client = Connections.getInstance(SHAMChatApplication.getMyApplicationContext()).getConnection(new RokhPref(SHAMChatApplication.getInstance().getApplicationContext()).getClientHandle()).client;
        client.publish(this.topicName, this.jsonMessageString.getBytes(), 1, false, null, new C10871());
        client.close();
    }

    private boolean addChatMessageToDB$2f10955c(int direction, int messageStatus, String jsonMessageString) {
        Cursor c;
        boolean isExistingMessage = false;
        ContentResolver mContentResolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
        String packetId = null;
        String from = null;
        String to = null;
        String messageTypeDesc = null;
        int messageType = 0;
        String messageBody = null;
        int isGroupChat = 0;
        try {
            JSONObject SampleMsg = new JSONObject(jsonMessageString);
            packetId = SampleMsg.getString("packetId");
            from = SampleMsg.getString("from");
            SampleMsg.getInt("from_userid");
            to = SampleMsg.getString("to");
            messageTypeDesc = SampleMsg.getString("messageTypeDesc");
            messageType = SampleMsg.getInt("messageType");
            messageBody = SampleMsg.getString("messageBody");
            isGroupChat = SampleMsg.getInt("isGroupChat");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = SHAMChatApplication.getConfig().userId;
        saveOrUpdateThread(this.threadId, jsonMessageString, messageType, to, direction);
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(ChatProviderNew.CONTENT_URI_CHAT, new String[]{"_id"}, "packet_id=?", new String[]{packetId}, null);
            if (cursor != null && cursor.getCount() > 0) {
                isExistingMessage = true;
            }
            if (!isExistingMessage) {
                ContentValues values = new ContentValues();
                values.put("message_recipient", to);
                values.put("message_type", Integer.valueOf(direction));
                values.put("packet_id", packetId);
                values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, this.threadId);
                values.put("description", messageTypeDesc);
                values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE, Integer.valueOf(messageType));
                values.put("message_status", Integer.valueOf(messageStatus));
                if (isGroupChat == 1) {
                    values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, to);
                }
                System.out.println("processMessage addChatMessageToDB Single chat, both directions, Group chat outgoing");
                values.put("message_sender", from);
                values.put("text_message", messageBody);
                c = null;
                c = mContentResolver.query(mContentResolver.insert(ChatProviderNew.CONTENT_URI_CHAT, values), null, null, null, null);
                c.moveToFirst();
                c.getInt(c.getColumnIndex("_id"));
                if (c != null) {
                }
                NewMessageEvent newMessageEvent = new NewMessageEvent();
                newMessageEvent.threadId = this.threadId;
                newMessageEvent.jsonMessageString = jsonMessageString;
                if (direction == MyMessageType.OUTGOING_MSG.ordinal()) {
                    newMessageEvent.direction = MyMessageType.OUTGOING_MSG.ordinal();
                }
                EventBus.getDefault().postSticky(newMessageEvent);
            }
            cursor.close();
            return isExistingMessage;
        } catch (Throwable th) {
            if (c != null) {
            }
        } finally {
            cursor.close();
        }
    }

    private static boolean saveOrUpdateThread(String threadId, String receivedJsonMessage, int messageContentType, String friendId, int direction) {
        System.out.println("processMessage addChatMessageToDB saveOrUpdateThread");
        ContentResolver mContentResolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
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
                switch (C10882.f28x39e42954[Utils.readMessageContentType(messageType).ordinal()]) {
                    case Logger.SEVERE /*1*/:
                        int limit;
                        if (messageBody.length() > 70) {
                            limit = 70;
                        } else {
                            limit = messageBody.length();
                        }
                        lastMessage = SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493359) + " " + messageBody.substring(0, limit);
                        break;
                    case Logger.WARNING /*2*/:
                        lastMessage = SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493360);
                        break;
                    case Logger.INFO /*3*/:
                        lastMessage = SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493363);
                        break;
                    case Logger.CONFIG /*4*/:
                        lastMessage = SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493362);
                        break;
                    case Logger.FINE /*5*/:
                        lastMessage = SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493361);
                        break;
                    case Logger.FINER /*6*/:
                        lastMessage = SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493364);
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

    protected final int getRetryLimit() {
        return 1;
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("Publish to topic run again");
        return true;
    }
}
