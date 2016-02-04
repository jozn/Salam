package com.shamchat.events;

public final class SendStickerToGroupEvent {
    private String reciepientId;
    public String stikerUrl;

    public SendStickerToGroupEvent(String reciepientId, String stikerUrl) {
        this.reciepientId = reciepientId;
        this.stikerUrl = stikerUrl;
    }
}
