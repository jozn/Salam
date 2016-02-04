package com.shamchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.shamchat.activity.ChatActivity;
import com.shamchat.models.ChatMessage;
import com.shamchat.models.ChatMessage.MessageStatusType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class ChatMessageAdapter extends BaseAdapter {
    public ArrayList<String> arrkey;
    private ChatActivity chatActivity;
    public Map<String, Row> chatMap;
    private Context context;
    private String friendId;
    private String friendProfileImageUrl;
    final LayoutInflater inflater;
    private String myProfileImageUrl;
    private String myUserId;

    /* renamed from: com.shamchat.adapters.ChatMessageAdapter.1 */
    class C09381 implements Runnable {
        final /* synthetic */ ViewHolder val$viewHolder;

        C09381(ViewHolder viewHolder) {
            this.val$viewHolder = viewHolder;
        }

        public final void run() {
            this.val$viewHolder.uploadingProgLayout.setVisibility(8);
            this.val$viewHolder.progressBarVoice.setVisibility(8);
        }
    }

    /* renamed from: com.shamchat.adapters.ChatMessageAdapter.2 */
    class C09392 implements Runnable {
        final /* synthetic */ ChatMessage val$chatMessage;
        final /* synthetic */ int val$progress;
        final /* synthetic */ ViewHolder val$viewHolder;

        C09392(ViewHolder viewHolder, int i, ChatMessage chatMessage) {
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

    /* renamed from: com.shamchat.adapters.ChatMessageAdapter.3 */
    class C09403 implements Runnable {
        final /* synthetic */ MessageStatusType val$messageStatusType;
        final /* synthetic */ ViewHolder val$viewHolder;

        C09403(MessageStatusType messageStatusType, ViewHolder viewHolder) {
            this.val$messageStatusType = messageStatusType;
            this.val$viewHolder = viewHolder;
        }

        public final void run() {
            switch (C09414.$SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[this.val$messageStatusType.ordinal()]) {
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
                    this.val$viewHolder.isDelivered.setVisibility(8);
                default:
            }
        }
    }

    /* renamed from: com.shamchat.adapters.ChatMessageAdapter.4 */
    static /* synthetic */ class C09414 {
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

    public ChatMessageAdapter(Context context, String myUserId, String myProfileImageUrl, String friendId, String friendProfileImageUrl, ChatActivity chatActivity) {
        this.chatMap = new HashMap();
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.arrkey = new ArrayList();
        this.myUserId = myUserId;
        this.friendId = friendId;
        this.myProfileImageUrl = myProfileImageUrl;
        this.friendProfileImageUrl = friendProfileImageUrl;
        this.context = context;
        this.chatActivity = chatActivity;
    }

    public final void add(ChatMessage message, MyMessageType messageType) {
        ChatMessage cm;
        switch (C09414.$SwitchMap$com$shamchat$adapters$MyMessageType[messageType.ordinal()]) {
            case Logger.SEVERE /*1*/:
                this.chatMap.put(message.packetId, new IncomingMsg(this.inflater, message, this.context, this.chatActivity, this.friendId, this.friendProfileImageUrl));
                this.arrkey.add(message.packetId);
            case Logger.WARNING /*2*/:
                this.chatMap.put(message.packetId, new OutgoingMsg(this.inflater, message, this.context, this.myUserId, this.chatActivity, this.myProfileImageUrl));
                this.arrkey.add(message.packetId);
            case Logger.INFO /*3*/:
                String sDate;
                try {
                    sDate = new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(message.messageDateTime.toString()));
                    if (!this.chatMap.containsKey(sDate.toString())) {
                        cm = new ChatMessage();
                        cm.packetId = sDate.toString();
                        cm.messageDateTime = message.messageDateTime;
                        this.chatMap.put(sDate.toString(), new ChatDateHeader(this.inflater, cm));
                        this.arrkey.add(sDate.toString());
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
                        }
                    } catch (Exception e2) {
                    }
                }
            default:
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

    private Row getItem(int position) {
        return (Row) this.chatMap.get(this.arrkey.get(position));
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        return ((Row) this.chatMap.get(getItem(position).getChatMessageObject().packetId)).getView(convertView);
    }

    public final void hideProgresssBarAndSetUrl(String messageId, String url) {
        if (this.chatMap.containsKey(messageId)) {
            System.out.println("TEST UPLOADING URL " + url);
            Row row = (Row) this.chatMap.get(messageId);
            if (row instanceof OutgoingMsg) {
                System.out.println("row instanceof OutgoingMsg");
                OutgoingMsg outgoingMsg = (OutgoingMsg) row;
                ChatMessage chatMessage = outgoingMsg.chatMessage;
                chatMessage.uploadedPercentage = 100;
                chatMessage.uploadedFileUrl = url;
                ViewHolder viewHolder = outgoingMsg.viewHolder;
                if (viewHolder != null && this.chatActivity != null) {
                    this.chatActivity.runOnUiThread(new C09381(viewHolder));
                }
            }
        }
    }

    public final void setUploadedPercentage(String messageId, int progress) {
        System.out.println("setUploadedPercentage");
        if (this.chatMap.containsKey(messageId)) {
            System.out.println("chatMap.containsKey(messageId)");
            Row row = (Row) this.chatMap.get(messageId);
            if (row instanceof OutgoingMsg) {
                System.out.println("row instanceof OutgoingMsg");
                OutgoingMsg outgoingMsg = (OutgoingMsg) row;
                ChatMessage chatMessage = outgoingMsg.chatMessage;
                chatMessage.uploadedPercentage = progress;
                ViewHolder viewHolder = outgoingMsg.viewHolder;
                if (progress != 9999) {
                    System.out.println("progress != 9999");
                    if (this.chatActivity != null) {
                        this.chatActivity.runOnUiThread(new C09392(viewHolder, progress, chatMessage));
                    }
                }
            }
        }
    }

    public final boolean updateMessageStatus(String messageId, MessageStatusType messageStatusType) {
        if (this.chatMap.containsKey(messageId)) {
            Row row = (Row) this.chatMap.get(messageId);
            if (row instanceof OutgoingMsg) {
                OutgoingMsg outgoingMsg = (OutgoingMsg) row;
                ChatMessage chatMessage = outgoingMsg.chatMessage;
                chatMessage.messageStatus = messageStatusType;
                ViewHolder viewHolder = outgoingMsg.viewHolder;
                if (viewHolder != null) {
                    System.out.println("Trying to update the status of the message" + chatMessage.textMessage);
                    if (this.chatActivity != null) {
                        this.chatActivity.runOnUiThread(new C09403(messageStatusType, viewHolder));
                    }
                    return true;
                }
                System.out.println("ChatMessageAdapter failed to find viewHolder to change UI:" + chatMessage.textMessage + "Message Status: " + messageStatusType);
                return false;
            }
            System.out.println("View holder status null");
            return false;
        }
        System.out.println("FAILED: Tried to update message status, but it was not in ChatMap");
        return false;
    }
}
