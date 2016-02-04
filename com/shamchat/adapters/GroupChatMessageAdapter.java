package com.shamchat.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.adapters.OutgoingGroupMsg.ViewHolder;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.androidclient.data.ChatProviderNew.ChatDatabaseHelper;
import com.shamchat.models.ChatMessage;
import com.shamchat.models.ChatMessage.MessageStatusType;
import com.shamchat.models.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class GroupChatMessageAdapter extends BaseAdapter {
    public static int addToBottom;
    public static int addToTop;
    public ArrayList<String> arrkey;
    public ChatInitialForGroupChatActivity chatInitialForGroupChatActivity;
    public Map<String, Row> chatMap;
    private Context context;
    final LayoutInflater inflater;
    private String myUserId;
    public int position;

    /* renamed from: com.shamchat.adapters.GroupChatMessageAdapter.1 */
    class C09521 implements Runnable {
        final /* synthetic */ ViewHolder val$viewHolder;

        public C09521(ViewHolder viewHolder) {
            this.val$viewHolder = viewHolder;
        }

        public final void run() {
            this.val$viewHolder.uploadingProgLayout.setVisibility(8);
            this.val$viewHolder.progressBarVoice.setVisibility(8);
        }
    }

    /* renamed from: com.shamchat.adapters.GroupChatMessageAdapter.2 */
    class C09532 implements Runnable {
        final /* synthetic */ ChatMessage val$chatMessage;
        final /* synthetic */ int val$progress;
        final /* synthetic */ ViewHolder val$viewHolder;

        C09532(ViewHolder viewHolder, int i, ChatMessage chatMessage) {
            this.val$viewHolder = viewHolder;
            this.val$progress = i;
            this.val$chatMessage = chatMessage;
        }

        public final void run() {
            if (this.val$viewHolder != null) {
                System.out.println("View holder NOT null " + this.val$progress);
                this.val$viewHolder.txtProgress.setText(this.val$progress + "%");
                this.val$viewHolder.txtProgress.setVisibility(0);
                this.val$viewHolder.uploadingProgress.setVisibility(0);
                this.val$viewHolder.imgRetry.setVisibility(8);
                if (this.val$progress == 100) {
                    this.val$viewHolder.uploadingProgLayout.setVisibility(8);
                    this.val$viewHolder.progressBarVoice.setVisibility(8);
                    this.val$chatMessage.messageStatus = MessageStatusType.SENDING;
                    return;
                }
                this.val$viewHolder.txtSeen.setText(2131493433);
                this.val$viewHolder.txtSeen.setVisibility(0);
                return;
            }
            System.out.println("View holder null " + this.val$progress);
        }
    }

    /* renamed from: com.shamchat.adapters.GroupChatMessageAdapter.3 */
    class C09543 implements Runnable {
        final /* synthetic */ MessageStatusType val$messageStatusType;
        final /* synthetic */ ViewHolder val$viewHolder;

        C09543(MessageStatusType messageStatusType, ViewHolder viewHolder) {
            this.val$messageStatusType = messageStatusType;
            this.val$viewHolder = viewHolder;
        }

        public final void run() {
            switch (C09554.$SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[this.val$messageStatusType.ordinal()]) {
                case Logger.SEVERE /*1*/:
                    this.val$viewHolder.txtSeen.setVisibility(8);
                case Logger.WARNING /*2*/:
                    this.val$viewHolder.txtSeen.setText(2131493358);
                    this.val$viewHolder.txtSeen.setVisibility(0);
                case Logger.INFO /*3*/:
                    this.val$viewHolder.txtSeen.setText(2131493359);
                    this.val$viewHolder.txtSeen.setVisibility(0);
                    this.val$viewHolder.uploadingProgLayout.setVisibility(8);
                    this.val$viewHolder.progressBarVoice.setVisibility(8);
                case Logger.CONFIG /*4*/:
                    this.val$viewHolder.txtSeen.setVisibility(8);
                    this.val$viewHolder.isDelivered.setVisibility(0);
                    this.val$viewHolder.uploadingProgLayout.setVisibility(8);
                    this.val$viewHolder.progressBarVoice.setVisibility(8);
                case Logger.FINE /*5*/:
                    this.val$viewHolder.txtSeen.setVisibility(0);
                    this.val$viewHolder.txtSeen.setText(2131493354);
                    this.val$viewHolder.isDelivered.setVisibility(0);
                case Logger.FINER /*6*/:
                    this.val$viewHolder.txtSeen.setVisibility(0);
                    this.val$viewHolder.txtSeen.setText(2131493123);
                    this.val$viewHolder.txtRetry.setVisibility(0);
                    this.val$viewHolder.isDelivered.setVisibility(8);
                default:
            }
        }
    }

    /* renamed from: com.shamchat.adapters.GroupChatMessageAdapter.4 */
    static /* synthetic */ class C09554 {
        static final /* synthetic */ int[] $SwitchMap$com$shamchat$adapters$MyMessageType;
        static final /* synthetic */ int[] $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType;

        static {
            $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType = new int[MessageStatusType.values().length];
            try {
                $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[MessageStatusType.QUEUED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[MessageStatusType.SENDING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[MessageStatusType.SENT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[MessageStatusType.DELIVERED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[MessageStatusType.SEEN.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[MessageStatusType.FAILED.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            $SwitchMap$com$shamchat$adapters$MyMessageType = new int[MyMessageType.values().length];
            try {
                $SwitchMap$com$shamchat$adapters$MyMessageType[MyMessageType.INCOMING_MSG.ordinal()] = 1;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$shamchat$adapters$MyMessageType[MyMessageType.OUTGOING_MSG.ordinal()] = 2;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$shamchat$adapters$MyMessageType[MyMessageType.HEADER_MSG.ordinal()] = 3;
            } catch (NoSuchFieldError e9) {
            }
        }
    }

    static {
        addToTop = 0;
        addToBottom = 1;
    }

    public GroupChatMessageAdapter(Context context, String myUserId, ChatInitialForGroupChatActivity chatInitialForGroupChatActivity) {
        this.chatMap = new HashMap();
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.arrkey = new ArrayList();
        this.myUserId = myUserId;
        this.context = context;
        this.chatInitialForGroupChatActivity = chatInitialForGroupChatActivity;
    }

    public final void addList(List<ChatMessage> chatMessages, int appendDirection) {
        for (ChatMessage message : chatMessages) {
            add(message, MyMessageType.HEADER_MSG, appendDirection);
            add(message, message.incomingMessage, appendDirection);
        }
    }

    private static Map<String, Row> reverseChatMap(Map<String, Row> chatMap) {
        Map<String, Row> myNewChatMap = new HashMap();
        for (Entry<String, Row> entry : chatMap.entrySet()) {
            myNewChatMap.put(entry.getKey(), entry.getValue());
        }
        return myNewChatMap;
    }

    public final void add(ChatMessage message, MyMessageType messageType) {
        add(message, messageType, addToBottom);
    }

    public final void add(ChatMessage message, MyMessageType messageType, int appendDirection) {
        String sDate;
        if (appendDirection == addToTop) {
            reverseChatMap(this.chatMap);
            Collections.reverse(this.arrkey);
        }
        switch (C09554.$SwitchMap$com$shamchat$adapters$MyMessageType[messageType.ordinal()]) {
            case Logger.SEVERE /*1*/:
                this.chatMap.put(message.packetId, new IncomingGroupMsg(this.inflater, message, this.context, this.chatInitialForGroupChatActivity, this.position));
                this.arrkey.add(message.packetId);
                Log.i("EndlessScroll", "adding to chatMap & addkey: " + message.textMessage + "  messageType: " + messageType);
                break;
            case Logger.WARNING /*2*/:
                this.chatMap.put(message.packetId, new OutgoingGroupMsg(this.inflater, message, this.context, this.myUserId, this.chatInitialForGroupChatActivity));
                this.arrkey.add(message.packetId);
                Log.i("EndlessScroll", "adding to chatMap & addkey: " + message.textMessage + "  messageType: " + messageType);
                break;
            case Logger.INFO /*3*/:
                ChatMessage cm;
                try {
                    sDate = new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(message.messageDateTime.toString()));
                    if (!this.chatMap.containsKey(sDate.toString())) {
                        cm = new ChatMessage();
                        cm.packetId = sDate.toString();
                        cm.messageDateTime = message.messageDateTime;
                        this.chatMap.put(sDate.toString(), new ChatDateHeader(this.inflater, cm));
                        this.arrkey.add(sDate.toString());
                        break;
                    }
                } catch (Exception e) {
                    try {
                        sDate = new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(message.messageDateTime.toString()));
                        if (!this.chatMap.containsKey(sDate.toString())) {
                            cm = new ChatMessage();
                            cm.packetId = sDate.toString();
                            cm.messageDateTime = message.messageDateTime;
                            this.chatMap.put(sDate.toString(), new ChatDateHeader(this.inflater, cm));
                            this.arrkey.add(sDate.toString());
                            break;
                        }
                    } catch (Exception e2) {
                        break;
                    }
                }
                break;
        }
        if (appendDirection == addToTop) {
            reverseChatMap(this.chatMap);
            Collections.reverse(this.arrkey);
        }
    }

    public final int getViewTypeCount() {
        return MyMessageType.values().length;
    }

    public final int getItemViewType(int position) {
        int x = 0;
        try {
            x = ((Row) this.chatMap.get(getItem(position).getChatMessageObject().packetId)).getViewType();
        } catch (Exception e) {
        }
        return x;
    }

    public final int getCount() {
        return this.chatMap.size();
    }

    public final Row getItem(int position) {
        return (Row) this.chatMap.get(this.arrkey.get(position));
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        this.position = position;
        Row rr = getItem(position);
        String Value = rr.getChatMessageObject().packetId;
        String tt = rr.getChatMessageObject().textMessage;
        if (tt == null) {
            tt = "null";
        }
        Log.v("GroupChatMessageAdapter messages:", tt);
        return ((Row) this.chatMap.get(Value)).getView(convertView);
    }

    public final void setUploadedPercentage(String messageId, int progress) {
        System.out.println("setUploadedPercentage");
        if (this.chatMap.containsKey(messageId)) {
            System.out.println("chatMap.containsKey(messageId)");
            Row row = (Row) this.chatMap.get(messageId);
            if (row instanceof OutgoingGroupMsg) {
                System.out.println("row instanceof OutgoingMsg");
                OutgoingGroupMsg outgoingMsg = (OutgoingGroupMsg) row;
                ChatMessage chatMessage = outgoingMsg.chatMessage;
                chatMessage.uploadedPercentage = progress;
                ViewHolder viewHolder = outgoingMsg.viewHolder;
                if (progress != 9999) {
                    System.out.println("progress != 9999");
                    this.chatInitialForGroupChatActivity.runOnUiThread(new C09532(viewHolder, progress, chatMessage));
                }
            }
        }
    }

    public final void clear() {
        this.chatMap = new HashMap();
        this.arrkey = new ArrayList();
    }

    public final ArrayList<ChatMessage> loadDataSet$6ba208b4(int index, String threadId, int appendDirection, int limit, String ascDesc) {
        String packetId;
        if (appendDirection == 0) {
            packetId = (String) this.arrkey.get(index);
            if (packetId.contains(MqttTopic.TOPIC_LEVEL_SEPARATOR)) {
                packetId = (String) this.arrkey.get(index + 1);
            }
        } else {
            packetId = BuildConfig.VERSION_NAME;
            int i = 1;
            while (!packetId.contains("packet")) {
                packetId = (String) this.arrkey.get(this.arrkey.size() - i);
                i++;
            }
        }
        ChatProviderNew chatProviderNew = new ChatProviderNew();
        return ChatProviderNew.loadDataSet$4f484b25(packetId, threadId, appendDirection, limit, ascDesc);
    }

    public static ChatMessage getLastMessageFromDB(String threadId) {
        ChatProviderNew chatProviderNew = new ChatProviderNew();
        SQLiteDatabase readableDatabase = new ChatDatabaseHelper(SHAMChatApplication.getMyApplicationContext()).getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM chat_message WHERE thread_id=? ORDER BY _id DESC LIMIT 1", new String[]{threadId});
        ChatMessage chatMessage = null;
        if (rawQuery.moveToFirst()) {
            chatMessage = ChatProviderNew.getChatMessageByCursor(rawQuery);
        }
        rawQuery.close();
        readableDatabase.close();
        return chatMessage;
    }

    public static ChatMessage getMessageFromDB(String packetId) {
        ChatMessage chatMessage = null;
        ChatProviderNew chatProviderNew = new ChatProviderNew();
        User user = ChatProviderNew.getUser(SHAMChatApplication.getConfig().userId);
        Cursor query = SHAMChatApplication.getMyApplicationContext().getContentResolver().query(ChatProviderNew.CONTENT_URI_CHAT, null, "packet_id=? OR _id=?", new String[]{packetId, packetId}, null);
        if (query != null && query.getCount() > 0) {
            query.moveToFirst();
            chatMessage = ChatProviderNew.getChatMessageByCursor(query);
            String str = chatMessage.sender;
            System.out.println("chat message sender " + chatMessage.sender);
            if (user.userId.equals(str)) {
                chatMessage.user = user;
            } else if (!str.startsWith("g")) {
                chatMessage.user = ChatProviderNew.getUser(chatMessage.sender);
            }
        }
        query.close();
        return chatMessage;
    }

    public final boolean reachedLastRecord(String threadId, int directionToCheck) {
        String packetId;
        if (directionToCheck == 0) {
            packetId = (String) this.arrkey.get(0);
            if (packetId.contains(MqttTopic.TOPIC_LEVEL_SEPARATOR)) {
                packetId = (String) this.arrkey.get(1);
            }
        } else {
            packetId = BuildConfig.VERSION_NAME;
            int i = 1;
            while (!packetId.contains("packet")) {
                packetId = (String) this.arrkey.get(this.arrkey.size() - i);
                i++;
            }
            Log.i("scrolldown", "reachedLast record check packet id:" + packetId);
        }
        ChatProviderNew chatProviderNew = new ChatProviderNew();
        return ChatProviderNew.isLastRecord(packetId, threadId, directionToCheck);
    }

    public final boolean previousToLastRecord$505cff18(String threadId) {
        String packetId = BuildConfig.VERSION_NAME;
        int i = 1;
        while (!packetId.contains("packet")) {
            packetId = (String) this.arrkey.get(this.arrkey.size() - i);
            i++;
        }
        Log.i("scrolldown", "reachedLast record check packet id:" + packetId);
        ChatProviderNew chatProviderNew = new ChatProviderNew();
        return ChatProviderNew.previousToLastRecord$3b99ba1e(packetId, threadId);
    }

    public final boolean updateMessageStatus(String messageId, MessageStatusType messageStatusType) {
        if (this.chatMap.containsKey(messageId)) {
            Row row = (Row) this.chatMap.get(messageId);
            if (row instanceof OutgoingGroupMsg) {
                OutgoingGroupMsg outgoingMsg = (OutgoingGroupMsg) row;
                ChatMessage chatMessage = outgoingMsg.chatMessage;
                chatMessage.messageStatus = messageStatusType;
                ViewHolder viewHolder = outgoingMsg.viewHolder;
                if (viewHolder != null) {
                    System.out.println("View holder status NOT null");
                    if (this.chatInitialForGroupChatActivity != null) {
                        this.chatInitialForGroupChatActivity.runOnUiThread(new C09543(messageStatusType, viewHolder));
                    }
                    return true;
                }
                System.out.println("GroupChatMessageAdapter failed to find viewHolder to change UI:" + chatMessage.textMessage + "Message Status: " + messageStatusType);
                return false;
            }
            System.out.println("View holder status null");
            return false;
        }
        System.out.println("FAILED: Tried to update message status, but it was not in ChatMap");
        return false;
    }
}
