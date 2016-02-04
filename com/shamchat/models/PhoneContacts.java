package com.shamchat.models;

import java.util.List;
import org.json.JSONArray;

public final class PhoneContacts {
    private List<String> contactListOnyNumbers;
    public JSONArray contactsJson;
    private long lastId;
    public List<User> newlyAddedContacts;

    public PhoneContacts(List<User> newlyAddedContacts, JSONArray contactsJson, List<String> contactListOnyNumbers, long lastId) {
        this.newlyAddedContacts = newlyAddedContacts;
        this.contactsJson = contactsJson;
        this.contactListOnyNumbers = contactListOnyNumbers;
        this.lastId = lastId;
    }
}
