package com.shamchat.androidclient.chat.extension;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public abstract class AbstractExtensionProvider<Extension extends PacketExtension> extends AbstractProvider<Extension> implements PacketExtensionProvider {
    public final /* bridge */ /* synthetic */ PacketExtension parseExtension(XmlPullParser xmlPullParser) throws Exception {
        return (PacketExtension) provideInstance(xmlPullParser);
    }
}
