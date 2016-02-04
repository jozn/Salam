package com.shamchat.events;

import com.shamchat.models.ContactFriend;
import java.util.List;
import java.util.Map;

public final class CitContactsDBLoadCompletedEvent {
    public Map<String, List<ContactFriend>> contactFriends;

    public CitContactsDBLoadCompletedEvent(Map<String, List<ContactFriend>> contactFriends) {
        this.contactFriends = contactFriends;
    }
}
