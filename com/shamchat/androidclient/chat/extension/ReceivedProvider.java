package com.shamchat.androidclient.chat.extension;

import org.xmlpull.v1.XmlPullParser;

public class ReceivedProvider extends AbstractExtensionProvider<Received> {
    protected final /* bridge */ /* synthetic */ Instance createInstance(XmlPullParser xmlPullParser) {
        return new Received(xmlPullParser.getAttributeValue(null, "id"));
    }
}
