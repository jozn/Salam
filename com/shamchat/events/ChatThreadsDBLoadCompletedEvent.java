package com.shamchat.events;

import com.shamchat.models.MessageThread;
import java.util.List;

public final class ChatThreadsDBLoadCompletedEvent {
    public List<MessageThread> messageThreads;

    public ChatThreadsDBLoadCompletedEvent(List<MessageThread> messageThreads) {
        this.messageThreads = messageThreads;
    }
}
