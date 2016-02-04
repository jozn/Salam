package com.shamchat.events;

import com.shamchat.models.ContactFriend;
import java.util.ArrayList;
import java.util.List;

public final class AllContactsDBLoadCompletedEvent {
    public List<ArrayList<ContactFriend>> contactFriends;

    public AllContactsDBLoadCompletedEvent(List<ArrayList<ContactFriend>> contactFriends) {
        this.contactFriends = contactFriends;
    }
}
