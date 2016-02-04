package com.shamchat.androidclient.chat.extension;

import android.support.v7.appcompat.BuildConfig;
import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

public abstract class PacketExtension implements Container, org.jivesoftware.smack.packet.PacketExtension {
    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        return SerializerUtils.toXml(this);
    }

    public final void serialize(XmlSerializer serializer) throws IOException {
        serializer.setPrefix(BuildConfig.VERSION_NAME, getNamespace());
        serializer.startTag(getNamespace(), getElementName());
        serializeContent(serializer);
        serializer.endTag(getNamespace(), getElementName());
    }
}
