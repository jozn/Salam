package com.shamchat.androidclient.chat.extension;

import org.xmlpull.v1.XmlPullParser;

public class RequestProvider extends AbstractExtensionProvider<Request> {
    protected final /* bridge */ /* synthetic */ Instance createInstance(XmlPullParser xmlPullParser) {
        return new Request();
    }
}
