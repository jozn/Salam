package com.shamchat.androidclient.chat.extension;

import org.xmlpull.v1.XmlPullParser;

public class SeenRequestProvider extends AbstractExtensionProvider<SeenRequest> {
    protected final /* bridge */ /* synthetic */ Instance createInstance(XmlPullParser xmlPullParser) {
        return new SeenRequest();
    }
}
