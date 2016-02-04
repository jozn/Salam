package com.shamchat.androidclient.chat.extension;

import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

public interface Container extends Instance {
    String getElementName();

    String getNamespace();

    void serializeContent(XmlSerializer xmlSerializer) throws IOException;
}
