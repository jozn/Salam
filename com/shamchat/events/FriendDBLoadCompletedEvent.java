package com.shamchat.events;

import com.shamchat.models.User;

public final class FriendDBLoadCompletedEvent {
    public User user;

    public FriendDBLoadCompletedEvent(User user) {
        this.user = user;
    }
}
