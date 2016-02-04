package org.jivesoftware.smackx.hoxt.provider;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.hoxt.packet.HttpOverXmppResp;
import org.jivesoftware.smackx.hoxt.packet.HttpOverXmppResp.Resp;
import org.xmlpull.v1.XmlPullParser;

public class HttpOverXmppRespProvider extends AbstractHttpOverXmppProvider {
    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        String version = parser.getAttributeValue(BuildConfig.VERSION_NAME, "version");
        String statusMessage = parser.getAttributeValue(BuildConfig.VERSION_NAME, "statusMessage");
        int statusCode = Integer.parseInt(parser.getAttributeValue(BuildConfig.VERSION_NAME, "statusCode"));
        Resp resp = new Resp();
        resp.setVersion(version);
        resp.statusMessage = statusMessage;
        resp.statusCode = statusCode;
        AbstractHttpOverXmppProvider.parseHeadersAndData(parser, "resp", resp);
        HttpOverXmppResp packet = new HttpOverXmppResp();
        packet.resp = resp;
        return packet;
    }
}
