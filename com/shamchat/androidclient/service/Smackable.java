package com.shamchat.androidclient.service;

import com.shamchat.androidclient.exceptions.ZaminXMPPException;
import com.shamchat.androidclient.util.ConnectionState;
import java.util.List;

public interface Smackable {
    String addFriendsToChatRoom(String str, List<String> list);

    void addRosterGroup(String str);

    void addRosterItem$14e1ec6d(String str, String str2) throws ZaminXMPPException;

    String changePassword(String str);

    String createRoom$7157d249(String str);

    ConnectionState getConnectionState();

    long getLastActivity(String str);

    String getLastError();

    String getNameForJID(String str);

    boolean isAuthenticated();

    void moveRosterItemToGroup(String str, String str2) throws ZaminXMPPException;

    String onComposing(String str, String str2, boolean z);

    void registerCallback(XMPPServiceCallback xMPPServiceCallback);

    void removeRosterItem(String str) throws ZaminXMPPException;

    void renameRosterGroup(String str, String str2);

    void renameRosterItem(String str, String str2) throws ZaminXMPPException;

    void requestConnectionState(ConnectionState connectionState);

    void requestConnectionState(ConnectionState connectionState, boolean z);

    String sendMessage(String str, String str2, String str3, boolean z, String str4, String str5, String str6, String str7);

    void sendPresenceRequest(String str, String str2);

    String sendSeenPacket(String str, String str2, String str3);

    void sendServerPing();

    void setStatusFromConfig();

    void setUserWatching(boolean z);

    void unRegisterCallback();
}
