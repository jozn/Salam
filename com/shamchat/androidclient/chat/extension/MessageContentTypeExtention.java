package com.shamchat.androidclient.chat.extension;

import com.kyleduo.switchbutton.C0473R;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.xmlpull.v1.XmlSerializer;

public final class MessageContentTypeExtention extends PacketExtension {
    public String description;
    private String messageContentType;

    public MessageContentTypeExtention(String messageContentType, String description) {
        this.messageContentType = messageContentType;
        this.description = description;
    }

    public final MessageContentType getMessageContentType() {
        int parseInt = Integer.parseInt(this.messageContentType);
        MessageContentType messageContentType = MessageContentType.TEXT;
        switch (parseInt) {
            case Logger.SEVERE /*1*/:
                return MessageContentType.IMAGE;
            case Logger.WARNING /*2*/:
                return MessageContentType.STICKER;
            case Logger.INFO /*3*/:
                return MessageContentType.VOICE_RECORD;
            case Logger.CONFIG /*4*/:
                return MessageContentType.FAVORITE;
            case Logger.FINE /*5*/:
                return MessageContentType.MESSAGE_WITH_IMOTICONS;
            case Logger.FINER /*6*/:
                return MessageContentType.LOCATION;
            case Logger.FINEST /*7*/:
                return MessageContentType.INCOMING_CALL;
            case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                return MessageContentType.OUTGOING_CALL;
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                return MessageContentType.VIDEO;
            case C0473R.styleable.SwitchButton_offColor /*11*/:
                return MessageContentType.GROUP_INFO;
            default:
                return messageContentType;
        }
    }

    public final String getElementName() {
        return "messagecontenttype";
    }

    public final String getNamespace() {
        return "urn:xmpp:messagetype";
    }

    public final void serializeContent(XmlSerializer serializer) throws IOException {
        SerializerUtils.setTextAttribute(serializer, "type", this.messageContentType);
        SerializerUtils.setTextAttribute(serializer, "desc", this.description);
    }
}
