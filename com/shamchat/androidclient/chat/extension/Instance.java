package com.shamchat.androidclient.chat.extension;

import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

public interface Instance {
    void serialize(XmlSerializer xmlSerializer) throws IOException;
}
