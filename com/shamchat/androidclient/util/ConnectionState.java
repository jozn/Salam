package com.shamchat.androidclient.util;

public enum ConnectionState {
    OFFLINE,
    CONNECTING,
    ONLINE,
    DISCONNECTING,
    DISCONNECTED,
    RECONNECT_NETWORK,
    RECONNECT_DELAYED
}
