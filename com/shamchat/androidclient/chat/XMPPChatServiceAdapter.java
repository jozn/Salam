package com.shamchat.androidclient.chat;

import android.os.RemoteException;
import android.util.Log;
import com.shamchat.androidclient.service.IXMPPChatService;
import com.shamchat.androidclient.util.ConnectionState;

public final class XMPPChatServiceAdapter {
    public IXMPPChatService xmppServiceStub;

    public XMPPChatServiceAdapter(IXMPPChatService xmppServiceStub) {
        Log.i("zamin.XMPPCSAdapter", "New XMPPChatServiceAdapter construced");
        this.xmppServiceStub = xmppServiceStub;
    }

    public final void setStatusFromConfig() {
        try {
            this.xmppServiceStub.setStatusFromConfig();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public final void disconnect() {
        try {
            this.xmppServiceStub.disconnect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public final ConnectionState getConnectionState() {
        try {
            return ConnectionState.values()[this.xmppServiceStub.getConnectionState()];
        } catch (RemoteException e) {
            e.printStackTrace();
            return ConnectionState.OFFLINE;
        }
    }

    public final String sendMessage(String toJID, String message, String messageType, boolean isGroupChat, String packetId, String latitude, String longitude, String description) {
        try {
            Log.i("zamin.XMPPCSAdapter", "Called sendMessage(): " + toJID + ": " + message);
            return this.xmppServiceStub.sendMessage(toJID, message, messageType, isGroupChat, packetId, latitude, longitude, description);
        } catch (RemoteException e) {
            Log.e("zamin.XMPPCSAdapter", "caught RemoteException: " + e.getMessage());
            return MqttServiceConstants.TRACE_EXCEPTION;
        }
    }

    public final String sendSeenPacket(String packetId, String to, String threadId) {
        try {
            return this.xmppServiceStub.sendSeenPacket(packetId, to, threadId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return MqttServiceConstants.TRACE_EXCEPTION;
        }
    }

    public final long getLastActivity(String friendJabberId) {
        try {
            return this.xmppServiceStub.getLastActivity(friendJabberId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 1;
        }
    }
}
