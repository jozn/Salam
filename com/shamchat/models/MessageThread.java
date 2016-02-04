package com.shamchat.models;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.Date;

public final class MessageThread {
    public static final Creator<MessageThread> CREATOR;
    public String friendId;
    public String friendProfileImageUrl;
    public boolean isGroupChat;
    public String lastMessage;
    public int lastMessageDirection;
    public int lastMessageMedium;
    private Date lastUpdateTime;
    public Date lastUpdatedDate;
    public int messageCount;
    public String threadId;
    public String threadOwner;
    private boolean threadStatus;
    public String username;

    /* renamed from: com.shamchat.models.MessageThread.1 */
    static class C11001 implements Creator<MessageThread> {
        C11001() {
        }

        public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
            return new MessageThread(parcel);
        }

        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new MessageThread[i];
        }
    }

    public MessageThread(Parcel in) {
        boolean z = true;
        this.threadId = in.readString();
        this.lastUpdateTime = new Date(in.readLong());
        this.threadStatus = in.readInt() == 1;
        if (in.readInt() != 1) {
            z = false;
        }
        this.isGroupChat = z;
        this.threadOwner = in.readString();
        this.friendId = in.readString();
    }

    public final String getThreadId() {
        return this.threadOwner + "-" + this.friendId;
    }

    static {
        CREATOR = new C11001();
    }
}
