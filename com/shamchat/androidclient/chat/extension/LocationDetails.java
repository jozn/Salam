package com.shamchat.androidclient.chat.extension;

import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

public final class LocationDetails extends PacketExtension {
    public final String latitude;
    public final String longitude;

    public LocationDetails(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public final String getElementName() {
        return "locationdetails";
    }

    public final String getNamespace() {
        return "urn:xmpp:location";
    }

    public final void serializeContent(XmlSerializer serializer) throws IOException {
        SerializerUtils.setTextAttribute(serializer, "latitude", this.latitude);
        SerializerUtils.setTextAttribute(serializer, "longitude", this.longitude);
    }
}
