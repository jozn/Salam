package com.shamchat.androidclient.chat.extension;

import android.support.v7.appcompat.BuildConfig;
import android.util.Xml;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.xmlpull.v1.XmlSerializer;

public final class SerializerUtils {
    public static String toXml(Instance instance) {
        Writer writer = new StringWriter();
        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(writer);
            instance.serialize(serializer);
            serializer.flush();
            return writer.toString();
        } catch (IOException e) {
            return BuildConfig.VERSION_NAME;
        }
    }

    public static void setTextAttribute(XmlSerializer serializer, String attributeName, String value) throws IOException {
        serializer.attribute(null, attributeName, value);
    }
}
