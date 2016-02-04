package com.shamchat.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;

public class Message implements Parcelable {
    public static final Creator<Message> CREATOR;
    public String messageContent;
    public String messageId;
    public long time;
    public MessageType type;
    public String userId;

    /* renamed from: com.shamchat.models.Message.1 */
    static class C10991 implements Creator<Message> {
        C10991() {
        }

        public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
            return new Message(parcel);
        }

        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new Message[i];
        }
    }

    public enum MessageType {
        IMAGE(0),
        TEXT(1);
        
        public int type;

        private MessageType(int type) {
            this.type = type;
        }
    }

    public Message(Parcel in) {
        this.type = (MessageType) in.readSerializable();
        this.messageContent = in.readString();
        this.messageId = in.readString();
        this.userId = in.readString();
        this.time = in.readLong();
    }

    static {
        CREATOR = new C10991();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.type);
        dest.writeString(this.messageContent);
        dest.writeString(this.messageId);
        dest.writeString(this.userId);
        dest.writeLong(this.time);
    }

    public final Message assignUniqueId(String lastMessageId) {
        int lastId = 0;
        if (lastMessageId != null && lastMessageId.length() > 1) {
            lastId = Integer.parseInt(lastMessageId.substring(1));
        }
        this.messageId = "M" + (lastId + 1);
        return this;
    }

    public String toString() {
        StringBuilder append = new StringBuilder("Id:").append(this.messageId).append(" UserId:").append(this.userId).append(" Type:").append(this.type).append(" ContentSize: ");
        int length = this.messageContent.length();
        return append.append(length < AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT ? length + "B" : (length / AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT) + "KB").toString();
    }

    public static void loadAsyncImageContent$78bd0eef() {
    }
}
