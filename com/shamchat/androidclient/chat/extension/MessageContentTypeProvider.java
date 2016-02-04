package com.shamchat.androidclient.chat.extension;

import org.xmlpull.v1.XmlPullParser;

public class MessageContentTypeProvider extends AbstractExtensionProvider<MessageContentTypeExtention> {

    public enum MessageContentType {
        TEXT(0),
        IMAGE(1),
        STICKER(2),
        VOICE_RECORD(3),
        FAVORITE(4),
        MESSAGE_WITH_IMOTICONS(5),
        LOCATION(6),
        INCOMING_CALL(7),
        OUTGOING_CALL(8),
        VIDEO(9),
        MISSED_CALL(10),
        GROUP_INFO(11);
        
        public int type;

        private MessageContentType(int type) {
            this.type = type;
        }
    }

    protected final /* bridge */ /* synthetic */ Instance createInstance(XmlPullParser xmlPullParser) {
        return new MessageContentTypeExtention(xmlPullParser.getAttributeValue(null, "type"), xmlPullParser.getAttributeValue(null, "desc"));
    }
}
