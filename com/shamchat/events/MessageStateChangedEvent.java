package com.shamchat.events;

import com.shamchat.models.ChatMessage.MessageStatusType;

public final class MessageStateChangedEvent {
    public MessageStatusType messageStatusType;
    public String packetId;
    public String threadId;
}
