package com.shamchat.events;

import com.shamchat.models.ChatMessage;
import java.util.List;

public final class ChatMessagesPartDBLoadCompletedEvent {
    public int appendDirection;
    public List<ChatMessage> messages;
    public String threadId;

    public ChatMessagesPartDBLoadCompletedEvent(String threadId, List<ChatMessage> messages, int appendDirection) {
        this.appendDirection = 1;
        this.threadId = threadId;
        this.messages = messages;
        this.appendDirection = appendDirection;
    }
}
