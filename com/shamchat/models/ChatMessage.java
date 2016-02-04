package com.shamchat.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.kyleduo.switchbutton.C0473R;
import com.shamchat.adapters.MyMessageType;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.utils.Utils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class ChatMessage implements Parcelable {
    public static final Creator<ChatMessage> CREATOR;
    public byte[] blobMessage;
    private String deliveredDateTime;
    public String description;
    public long fileSize;
    public String fileUrl;
    public MyMessageType incomingMessage;
    private String lastUpdatedDateTime;
    public double latitude;
    public double longitude;
    public MessageContentType messageContentType;
    public String messageDateTime;
    public int messageId;
    public MessageStatusType messageStatus;
    private boolean offlineSent;
    public String packetId;
    public String recipient;
    public String sender;
    private String sendersMobileNumber;
    private boolean sentSeen;
    public String textMessage;
    public String threadId;
    public String uploadedFileUrl;
    public int uploadedPercentage;
    public User user;

    /* renamed from: com.shamchat.models.ChatMessage.1 */
    static class C10981 implements Creator<ChatMessage> {
        C10981() {
        }

        public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
            return new ChatMessage(parcel);
        }

        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new ChatMessage[i];
        }
    }

    public enum MessageStatusType {
        QUEUED(0),
        SENDING(1),
        SENT(2),
        DELIVERED(3),
        SEEN(4),
        FAILED(5);
        
        public int type;

        private MessageStatusType(int type) {
            this.type = type;
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i = 1;
        dest.writeInt(this.messageId);
        dest.writeString(this.threadId);
        dest.writeString(this.sender);
        dest.writeString(this.recipient);
        dest.writeInt(this.messageContentType.type);
        dest.writeString(this.textMessage);
        dest.writeInt(this.blobMessage.length);
        dest.writeByteArray(this.blobMessage);
        dest.writeLong(Utils.getDateFromStringDate(this.messageDateTime).getTime());
        dest.writeLong(Utils.getDateFromStringDate(this.deliveredDateTime).getTime());
        dest.writeInt(this.offlineSent ? 1 : 0);
        dest.writeInt(this.incomingMessage.ordinal());
        if (!this.sentSeen) {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeString(this.packetId);
        dest.writeString(this.description);
        dest.writeString(this.sendersMobileNumber);
        dest.writeLong(this.fileSize);
        dest.writeInt(this.uploadedPercentage);
    }

    public ChatMessage(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.messageId = in.readInt();
        this.threadId = in.readString();
        this.sender = in.readString();
        this.recipient = in.readString();
        int readInt = in.readInt();
        MessageContentType messageContentType = MessageContentType.TEXT;
        switch (readInt) {
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
            case C0473R.styleable.SwitchButton_offColor /*11*/:
                messageContentType = MessageContentType.GROUP_INFO;
                break;
        }
        this.messageContentType = messageContentType;
        this.textMessage = in.readString();
        byte[] temp = new byte[in.readInt()];
        in.readByteArray(temp);
        this.blobMessage = temp;
        this.messageDateTime = Utils.formatDate(in.readLong(), "yyyy/MM/dd HH:mm:ss");
        this.deliveredDateTime = Utils.formatDate(in.readLong(), "yyyy/MM/dd HH:mm:ss");
        if (in.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.offlineSent = z;
        readInt = in.readInt();
        MyMessageType myMessageType = MyMessageType.HEADER_MSG;
        switch (readInt) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                myMessageType = MyMessageType.OUTGOING_MSG;
                break;
            case Logger.SEVERE /*1*/:
                myMessageType = MyMessageType.INCOMING_MSG;
                break;
        }
        this.incomingMessage = myMessageType;
        if (in.readInt() != 1) {
            z2 = false;
        }
        this.sentSeen = z2;
        this.packetId = in.readString();
        this.description = in.readString();
        this.sendersMobileNumber = in.readString();
        this.fileSize = in.readLong();
        this.uploadedPercentage = in.readInt();
    }

    static {
        CREATOR = new C10981();
    }

    public ChatMessage(int messageId, String threadId, MessageContentType messageContentType, String textMessage, byte[] blobMessage, String messageDateTime, String deliverdDateTime, MyMessageType messageType, String packetId, String sender, String recipient, String description, double longitude, double latitude, String sendersMobileNumber, long fileSize, int uploadedPercentage, String fileUrl, MessageStatusType messageStatus, String lastUpdatedDateTime, String uploadedFileUrl) {
        this.messageId = messageId;
        this.threadId = threadId;
        this.messageContentType = messageContentType;
        this.textMessage = textMessage;
        this.blobMessage = blobMessage;
        this.messageDateTime = messageDateTime;
        this.deliveredDateTime = deliverdDateTime;
        this.incomingMessage = messageType;
        this.packetId = packetId;
        this.sender = sender;
        this.recipient = recipient;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.sendersMobileNumber = sendersMobileNumber;
        this.fileSize = fileSize;
        this.uploadedPercentage = uploadedPercentage;
        this.fileUrl = fileUrl;
        this.messageStatus = messageStatus;
        this.lastUpdatedDateTime = lastUpdatedDateTime;
        this.uploadedFileUrl = uploadedFileUrl;
    }

    public int describeContents() {
        return 0;
    }
}
