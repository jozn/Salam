package com.shamchat.events;

import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import org.json.JSONException;
import org.json.JSONObject;

public final class NewGroupMessageSentFailedEvent {
    public JSONObject SampleMsg;
    public String from;
    int isGroupChat;
    private String jsonMessageString;
    String latitude;
    String longitude;
    String messageBody;
    int messageType;
    public String messageTypeDesc;
    public String packetId;
    public String threadId;
    public String to;

    public NewGroupMessageSentFailedEvent(String jsonMessage) {
        this.threadId = null;
        this.SampleMsg = null;
        this.packetId = null;
        this.from = null;
        this.to = null;
        this.messageTypeDesc = null;
        this.messageType = 0;
        this.messageBody = null;
        this.latitude = null;
        this.longitude = null;
        this.isGroupChat = 0;
        this.jsonMessageString = jsonMessage;
        try {
            this.SampleMsg = new JSONObject(this.jsonMessageString);
            this.packetId = this.SampleMsg.getString("packetId");
            this.from = this.SampleMsg.getString("from");
            this.to = this.SampleMsg.getString("to");
            this.messageTypeDesc = this.SampleMsg.getString("messageTypeDesc");
            this.messageType = this.SampleMsg.getInt("messageType");
            if (this.messageType == MessageContentType.LOCATION.ordinal()) {
                this.latitude = this.SampleMsg.getString("latitude");
                this.longitude = this.SampleMsg.getString("longitude");
            }
            this.messageBody = this.SampleMsg.getString("messageBody");
            this.isGroupChat = this.SampleMsg.getInt("isGroupChat");
            this.threadId = SHAMChatApplication.getConfig().userId + "-" + this.to;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
