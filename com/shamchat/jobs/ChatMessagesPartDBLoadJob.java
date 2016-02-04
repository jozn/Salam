package com.shamchat.jobs;

import android.content.ContentResolver;
import android.database.Cursor;
import com.kyleduo.switchbutton.C0473R;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.adapters.MyMessageType;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.androidclient.data.RosterProvider;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.events.ChatMessagesPartDBLoadCompletedEvent;
import com.shamchat.models.ChatMessage;
import com.shamchat.models.ChatMessage.MessageStatusType;
import com.shamchat.models.User;
import com.shamchat.utils.Utils;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class ChatMessagesPartDBLoadJob extends Job {
    private static final AtomicInteger jobCounter;
    private int appendDirection;
    private int fromOffset;
    private final int id;
    private String threadId;
    private int toOffset;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public ChatMessagesPartDBLoadJob(String threadId, int fromOffset, int toOffset, int appendDirection) {
        Params params = new Params(1000);
        params.persistent = true;
        params.groupId = "LoadDB_then_MessageStatusChange";
        super(params);
        this.id = jobCounter.incrementAndGet();
        this.threadId = threadId;
        this.fromOffset = fromOffset;
        this.toOffset = toOffset;
        this.appendDirection = appendDirection;
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        if (this.id == jobCounter.get()) {
            User me = getUser(SHAMChatApplication.getConfig().userId);
            if (me != null) {
                List<ChatMessage> listMsg = new ArrayList();
                Cursor cursor = null;
                try {
                    cursor = SHAMChatApplication.getMyApplicationContext().getContentResolver().query(ChatProviderNew.CONTENT_URI_CHAT, null, "thread_id=?", new String[]{this.threadId}, "_id DESC LIMIT " + this.fromOffset + "," + this.toOffset);
                    if (cursor != null && cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            int i = cursor.getInt(cursor.getColumnIndex("_id"));
                            int i2 = cursor.getInt(cursor.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE));
                            MessageContentType messageContentType = MessageContentType.TEXT;
                            switch (i2) {
                                case Logger.SEVERE /*1*/:
                                    messageContentType = MessageContentType.IMAGE;
                                    break;
                                case Logger.WARNING /*2*/:
                                    messageContentType = MessageContentType.STICKER;
                                    break;
                                case Logger.INFO /*3*/:
                                    messageContentType = MessageContentType.VOICE_RECORD;
                                    break;
                                case Logger.CONFIG /*4*/:
                                    messageContentType = MessageContentType.FAVORITE;
                                    break;
                                case Logger.FINE /*5*/:
                                    messageContentType = MessageContentType.MESSAGE_WITH_IMOTICONS;
                                    break;
                                case Logger.FINER /*6*/:
                                    messageContentType = MessageContentType.LOCATION;
                                    break;
                                case Logger.FINEST /*7*/:
                                    messageContentType = MessageContentType.INCOMING_CALL;
                                    break;
                                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                                    messageContentType = MessageContentType.OUTGOING_CALL;
                                    break;
                                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                                    messageContentType = MessageContentType.VIDEO;
                                    break;
                                case C0473R.styleable.SwitchButton_onColor /*10*/:
                                    messageContentType = MessageContentType.MISSED_CALL;
                                    break;
                                case C0473R.styleable.SwitchButton_offColor /*11*/:
                                    messageContentType = MessageContentType.GROUP_INFO;
                                    break;
                            }
                            String string = cursor.getString(cursor.getColumnIndex("text_message"));
                            byte[] blob = cursor.getBlob(cursor.getColumnIndex("blob_message"));
                            String string2 = cursor.getString(cursor.getColumnIndex("message_datetime"));
                            i2 = cursor.getInt(cursor.getColumnIndex("message_status"));
                            String string3 = cursor.getString(cursor.getColumnIndex("delivered_datetime"));
                            int i3 = cursor.getInt(cursor.getColumnIndex("message_type"));
                            System.out.println("message status " + i2 + " messageType " + i3);
                            String string4 = cursor.getString(cursor.getColumnIndex("packet_id"));
                            String string5 = cursor.getString(cursor.getColumnIndex("message_sender"));
                            String string6 = cursor.getString(cursor.getColumnIndex("message_recipient"));
                            String string7 = cursor.getString(cursor.getColumnIndex("description"));
                            double d = cursor.getDouble(cursor.getColumnIndex("longitude"));
                            double d2 = cursor.getDouble(cursor.getColumnIndex("latitude"));
                            String string8 = cursor.getString(cursor.getColumnIndex("senders_mobile_number"));
                            long j = cursor.getLong(cursor.getColumnIndex("file_size"));
                            int i4 = cursor.getInt(cursor.getColumnIndex("uploaded_percentage"));
                            String string9 = cursor.getString(cursor.getColumnIndex("file_url"));
                            String string10 = cursor.getString(cursor.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID));
                            String string11 = cursor.getString(cursor.getColumnIndex("message_last_updated_datetime"));
                            String string12 = cursor.getString(cursor.getColumnIndex("uploaded_file_url"));
                            MyMessageType myMessageType = MyMessageType.HEADER_MSG;
                            switch (i3) {
                                case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                                    myMessageType = MyMessageType.OUTGOING_MSG;
                                    break;
                                case Logger.SEVERE /*1*/:
                                    myMessageType = MyMessageType.INCOMING_MSG;
                                    break;
                            }
                            MessageStatusType messageStatusType = MessageStatusType.QUEUED;
                            switch (i2) {
                                case Logger.SEVERE /*1*/:
                                    messageStatusType = MessageStatusType.SENDING;
                                    break;
                                case Logger.WARNING /*2*/:
                                    messageStatusType = MessageStatusType.SENT;
                                    break;
                                case Logger.INFO /*3*/:
                                    messageStatusType = MessageStatusType.DELIVERED;
                                    break;
                                case Logger.CONFIG /*4*/:
                                    messageStatusType = MessageStatusType.SEEN;
                                    break;
                                case Logger.FINE /*5*/:
                                    messageStatusType = MessageStatusType.FAILED;
                                    break;
                            }
                            ChatMessage chatMessage = new ChatMessage(i, string10, messageContentType, string, blob, string2, string3, myMessageType, string4, string5, string6, string7, d, d2, string8, j, i4, string9, messageStatusType, string11, string12);
                            String sender = chatMessage.sender;
                            System.out.println("chat message sender " + chatMessage.sender);
                            if (me.userId.equals(sender)) {
                                chatMessage.user = me;
                            } else {
                                if (!sender.startsWith("g")) {
                                    chatMessage.user = getUser(chatMessage.sender);
                                }
                            }
                            listMsg.add(chatMessage);
                        }
                    }
                    cursor.close();
                    Collections.reverse(listMsg);
                    EventBus.getDefault().postSticky(new ChatMessagesPartDBLoadCompletedEvent(this.threadId, listMsg, this.appendDirection));
                } catch (Throwable th) {
                    cursor.close();
                }
            }
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("AAA download should re run on throwable");
        return false;
    }

    private static User getUser(String userId) {
        Throwable th;
        ContentResolver contentResolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
        Cursor friendCursor = null;
        Cursor rosterCursor = null;
        User user = null;
        try {
            friendCursor = contentResolver.query(UserProvider.CONTENT_URI_USER, null, "userId=?", new String[]{userId}, null);
            friendCursor.moveToFirst();
            String friendJID = Utils.createXmppUserIdByUserId(userId);
            rosterCursor = contentResolver.query(RosterProvider.CONTENT_URI, null, "jid=?", new String[]{friendJID}, null);
            if (friendCursor != null && friendCursor.getCount() == 1) {
                User user2 = new User();
                try {
                    user2.userId = userId;
                    user2.username = friendCursor.getString(friendCursor.getColumnIndex("name"));
                    user2.mobileNo = friendCursor.getString(friendCursor.getColumnIndex("mobileNo"));
                    user = user2;
                } catch (Throwable th2) {
                    th = th2;
                    user = user2;
                    friendCursor.close();
                    rosterCursor.close();
                    throw th;
                }
            }
            friendCursor.close();
            rosterCursor.close();
            return user;
        } catch (Throwable th3) {
            th = th3;
            friendCursor.close();
            rosterCursor.close();
            throw th;
        }
    }
}
