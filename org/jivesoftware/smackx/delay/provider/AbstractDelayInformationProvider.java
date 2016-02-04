package org.jivesoftware.smackx.delay.provider;

import android.support.v7.appcompat.BuildConfig;
import java.util.Date;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.xmlpull.v1.XmlPullParser;

public abstract class AbstractDelayInformationProvider implements PacketExtensionProvider {
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract Date parseDate(String str) throws Exception;

    static {
        $assertionsDisabled = !AbstractDelayInformationProvider.class.desiredAssertionStatus();
    }

    public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        String stampString = parser.getAttributeValue(BuildConfig.VERSION_NAME, "stamp");
        String from = parser.getAttributeValue(BuildConfig.VERSION_NAME, "from");
        String reason = null;
        if (parser.isEmptyElementTag()) {
            parser.next();
        } else {
            int event = parser.next();
            switch (event) {
                case Logger.INFO /*3*/:
                    reason = BuildConfig.VERSION_NAME;
                    break;
                case Logger.CONFIG /*4*/:
                    reason = parser.getText();
                    parser.next();
                    break;
                default:
                    throw new IllegalStateException("Unexpected event: " + event);
            }
        }
        if ($assertionsDisabled || parser.getEventType() == 3) {
            return new DelayInformation(parseDate(stampString), from, reason);
        }
        throw new AssertionError();
    }
}
