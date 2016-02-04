package org.jivesoftware.smackx.hoxt.provider;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.hoxt.packet.HttpMethod;
import org.jivesoftware.smackx.hoxt.packet.HttpOverXmppReq;
import org.jivesoftware.smackx.hoxt.packet.HttpOverXmppReq.Req;
import org.xmlpull.v1.XmlPullParser;

public class HttpOverXmppReqProvider extends AbstractHttpOverXmppProvider {
    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        String method = parser.getAttributeValue(BuildConfig.VERSION_NAME, "method");
        String resource = parser.getAttributeValue(BuildConfig.VERSION_NAME, "resource");
        String version = parser.getAttributeValue(BuildConfig.VERSION_NAME, "version");
        String maxChunkSize = parser.getAttributeValue(BuildConfig.VERSION_NAME, "maxChunkSize");
        Req req = new Req(HttpMethod.valueOf(method), resource);
        req.setVersion(version);
        Boolean sipub = Boolean.valueOf(true);
        Boolean jingle = Boolean.valueOf(true);
        Boolean ibb = Boolean.valueOf(true);
        String sipubStr = parser.getAttributeValue(BuildConfig.VERSION_NAME, "sipub");
        String ibbStr = parser.getAttributeValue(BuildConfig.VERSION_NAME, "ibb");
        String jingleStr = parser.getAttributeValue(BuildConfig.VERSION_NAME, "jingle");
        if (sipubStr != null) {
            sipub = Boolean.valueOf(sipubStr);
        }
        if (ibbStr != null) {
            ibb = Boolean.valueOf(ibbStr);
        }
        if (jingleStr != null) {
            jingle = Boolean.valueOf(jingleStr);
        }
        req.ibb = ibb.booleanValue();
        req.sipub = sipub.booleanValue();
        req.jingle = jingle.booleanValue();
        if (maxChunkSize != null) {
            req.maxChunkSize = Integer.parseInt(maxChunkSize);
        }
        AbstractHttpOverXmppProvider.parseHeadersAndData(parser, "req", req);
        HttpOverXmppReq packet = new HttpOverXmppReq();
        packet.req = req;
        return packet;
    }
}
