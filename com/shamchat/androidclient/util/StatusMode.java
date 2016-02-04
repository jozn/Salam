package com.shamchat.androidclient.util;

public enum StatusMode {
    chat(2131493387, 2130837926),
    available(2131493384, 2130838000),
    away(2131493385, 2130837595),
    xa(2131493396, 2130837930),
    dnd(2131493388, 2130837927),
    offline(2131493392, 2130837998),
    subscribe(0, 2130837929),
    unavailable(2131493392, 2130837998);
    
    public final int drawableId;
    public final int textId;

    private StatusMode(int textId, int drawableId) {
        this.textId = textId;
        this.drawableId = drawableId;
    }

    public final String toString() {
        return name();
    }

    public static StatusMode fromString(String status) {
        return valueOf(status);
    }
}
