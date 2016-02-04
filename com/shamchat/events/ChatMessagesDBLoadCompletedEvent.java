package com.shamchat.events;

import com.shamchat.models.ChatMessage;
import java.util.List;

public final class ChatMessagesDBLoadCompletedEvent {
    private int appendDirection;
    public List<ChatMessage> messages;
    public String threadId;

    public ChatMessagesDBLoadCompletedEvent(String threadId, List<ChatMessage> messages) {
        this.appendDirection = 1;
        this.threadId = threadId;
        this.messages = messages;
    }
}
