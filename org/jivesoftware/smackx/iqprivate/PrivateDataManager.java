package org.jivesoftware.smackx.iqprivate;

import android.support.v7.appcompat.BuildConfig;
import java.util.Hashtable;
import java.util.Map;
import java.util.WeakHashMap;
import org.jivesoftware.smack.Manager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.iqprivate.packet.DefaultPrivateData;
import org.jivesoftware.smackx.iqprivate.packet.PrivateData;
import org.jivesoftware.smackx.iqprivate.provider.PrivateDataProvider;
import org.xmlpull.v1.XmlPullParser;

public final class PrivateDataManager extends Manager {
    private static final Map<XMPPConnection, PrivateDataManager> instances;
    private static Map<String, PrivateDataProvider> privateDataProviders;

    /* renamed from: org.jivesoftware.smackx.iqprivate.PrivateDataManager.1 */
    class C13261 extends IQ {
        final /* synthetic */ String val$elementName;
        final /* synthetic */ String val$namespace;

        public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<query xmlns=\"jabber:iq:private\">");
            stringBuilder.append("<").append(this.val$elementName).append(" xmlns=\"").append(this.val$namespace).append("\"/>");
            stringBuilder.append("</query>");
            return stringBuilder.toString();
        }
    }

    /* renamed from: org.jivesoftware.smackx.iqprivate.PrivateDataManager.2 */
    class C13272 extends IQ {
        final /* synthetic */ PrivateData val$privateData;

        public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<query xmlns=\"jabber:iq:private\">");
            stringBuilder.append(this.val$privateData.toXML());
            stringBuilder.append("</query>");
            return stringBuilder.toString();
        }
    }

    public static class PrivateDataIQProvider implements IQProvider {
        public final IQ parseIQ(XmlPullParser parser) throws Exception {
            PrivateData privateData = null;
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2) {
                    String elementName = parser.getName();
                    String namespace = parser.getNamespace();
                    PrivateDataProvider provider = PrivateDataManager.getPrivateDataProvider(elementName, namespace);
                    if (provider != null) {
                        privateData = provider.parsePrivateData$4e70e355();
                    } else {
                        DefaultPrivateData data = new DefaultPrivateData(elementName, namespace);
                        boolean finished = false;
                        while (!finished) {
                            int event = parser.next();
                            if (event == 2) {
                                String name = parser.getName();
                                if (parser.isEmptyElementTag()) {
                                    data.setValue(name, BuildConfig.VERSION_NAME);
                                } else if (parser.next() == 4) {
                                    data.setValue(name, parser.getText());
                                }
                            } else if (event == 3 && parser.getName().equals(elementName)) {
                                finished = true;
                            }
                        }
                        privateData = data;
                    }
                } else if (eventType == 3 && parser.getName().equals("query")) {
                    done = true;
                }
            }
            return new PrivateDataResult(privateData);
        }
    }

    private static class PrivateDataResult extends IQ {
        private PrivateData privateData;

        public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<query xmlns=\"jabber:iq:private\">");
            if (this.privateData != null) {
                stringBuilder.append(this.privateData.toXML());
            }
            stringBuilder.append("</query>");
            return stringBuilder.toString();
        }

        PrivateDataResult(PrivateData privateData) {
            this.privateData = privateData;
        }
    }

    static {
        instances = new WeakHashMap();
        privateDataProviders = new Hashtable();
    }

    public static PrivateDataProvider getPrivateDataProvider(String elementName, String namespace) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(elementName).append("/><").append(namespace).append("/>");
        return (PrivateDataProvider) privateDataProviders.get(stringBuilder.toString());
    }
}
