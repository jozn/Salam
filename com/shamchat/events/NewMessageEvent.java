package com.shamchat.events;

public final class NewMessageEvent {
    public boolean consumed;
    public int direction;
    public String jsonMessageString;
    public String packetId;
    public String threadId;

    public NewMessageEvent() {
        this.packetId = null;
        this.consumed = false;
    }
}
