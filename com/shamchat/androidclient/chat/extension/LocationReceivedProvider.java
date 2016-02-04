package com.shamchat.androidclient.chat.extension;

import org.xmlpull.v1.XmlPullParser;

public class LocationReceivedProvider extends AbstractExtensionProvider<LocationDetails> {
    protected final /* bridge */ /* synthetic */ Instance createInstance(XmlPullParser xmlPullParser) {
        return new LocationDetails(xmlPullParser.getAttributeValue(null, "latitude"), xmlPullParser.getAttributeValue(null, "longitude"));
    }
}
