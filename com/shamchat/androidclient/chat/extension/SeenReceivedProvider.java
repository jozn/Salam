package com.shamchat.androidclient.chat.extension;

import org.xmlpull.v1.XmlPullParser;

public class SeenReceivedProvider extends AbstractExtensionProvider<SeenReceived> {
    protected final /* bridge */ /* synthetic */ Instance createInstance(XmlPullParser xmlPullParser) {
        return new SeenReceived(xmlPullParser.getAttributeValue(null, "seen"));
    }
}
