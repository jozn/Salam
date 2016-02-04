package org.jivesoftware.smackx.offline.packet;

import android.support.v7.appcompat.BuildConfig;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class OfflineMessageRequest extends IQ {
    boolean fetch;
    List<Item> items;
    boolean purge;

    public static class Item {
        String action;
        String jid;
        String node;

        public Item(String node) {
            this.node = node;
        }
    }

    public static class Provider implements IQProvider {
        public final IQ parseIQ(XmlPullParser parser) throws Exception {
            OfflineMessageRequest request = new OfflineMessageRequest();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2) {
                    if (parser.getName().equals("item")) {
                        boolean z = false;
                        Item item = new Item(parser.getAttributeValue(BuildConfig.VERSION_NAME, "node"));
                        item.action = parser.getAttributeValue(BuildConfig.VERSION_NAME, "action");
                        item.jid = parser.getAttributeValue(BuildConfig.VERSION_NAME, "jid");
                        while (!z) {
                            if (parser.next() == 3 && parser.getName().equals("item")) {
                                z = true;
                            }
                        }
                        synchronized (request.items) {
                            request.items.add(item);
                        }
                    } else if (parser.getName().equals("purge")) {
                        request.purge = true;
                    } else if (parser.getName().equals("fetch")) {
                        request.fetch = true;
                    }
                } else if (eventType == 3 && parser.getName().equals("offline")) {
                    done = true;
                }
            }
            return request;
        }
    }

    public OfflineMessageRequest() {
        this.items = new ArrayList();
        this.purge = false;
        this.fetch = false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getChildElementXML() {
        /*
        r7 = this;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r3 = "<offline xmlns=\"http://jabber.org/protocol/offline\">";
        r0.append(r3);
        r4 = r7.items;
        monitor-enter(r4);
        r1 = 0;
    L_0x000e:
        r3 = r7.items;	 Catch:{ all -> 0x009a }
        r3 = r3.size();	 Catch:{ all -> 0x009a }
        if (r1 >= r3) goto L_0x0076;
    L_0x0016:
        r3 = r7.items;	 Catch:{ all -> 0x009a }
        r2 = r3.get(r1);	 Catch:{ all -> 0x009a }
        r2 = (org.jivesoftware.smackx.offline.packet.OfflineMessageRequest.Item) r2;	 Catch:{ all -> 0x009a }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x009a }
        r3.<init>();	 Catch:{ all -> 0x009a }
        r5 = "<item";
        r3.append(r5);	 Catch:{ all -> 0x009a }
        r5 = r2.action;	 Catch:{ all -> 0x009a }
        if (r5 == 0) goto L_0x003d;
    L_0x002c:
        r5 = " action=\"";
        r5 = r3.append(r5);	 Catch:{ all -> 0x009a }
        r6 = r2.action;	 Catch:{ all -> 0x009a }
        r5 = r5.append(r6);	 Catch:{ all -> 0x009a }
        r6 = "\"";
        r5.append(r6);	 Catch:{ all -> 0x009a }
    L_0x003d:
        r5 = r2.jid;	 Catch:{ all -> 0x009a }
        if (r5 == 0) goto L_0x0052;
    L_0x0041:
        r5 = " jid=\"";
        r5 = r3.append(r5);	 Catch:{ all -> 0x009a }
        r6 = r2.jid;	 Catch:{ all -> 0x009a }
        r5 = r5.append(r6);	 Catch:{ all -> 0x009a }
        r6 = "\"";
        r5.append(r6);	 Catch:{ all -> 0x009a }
    L_0x0052:
        r5 = r2.node;	 Catch:{ all -> 0x009a }
        if (r5 == 0) goto L_0x0067;
    L_0x0056:
        r5 = " node=\"";
        r5 = r3.append(r5);	 Catch:{ all -> 0x009a }
        r6 = r2.node;	 Catch:{ all -> 0x009a }
        r5 = r5.append(r6);	 Catch:{ all -> 0x009a }
        r6 = "\"";
        r5.append(r6);	 Catch:{ all -> 0x009a }
    L_0x0067:
        r5 = "/>";
        r3.append(r5);	 Catch:{ all -> 0x009a }
        r3 = r3.toString();	 Catch:{ all -> 0x009a }
        r0.append(r3);	 Catch:{ all -> 0x009a }
        r1 = r1 + 1;
        goto L_0x000e;
    L_0x0076:
        monitor-exit(r4);	 Catch:{ all -> 0x009a }
        r3 = r7.purge;
        if (r3 == 0) goto L_0x0080;
    L_0x007b:
        r3 = "<purge/>";
        r0.append(r3);
    L_0x0080:
        r3 = r7.fetch;
        if (r3 == 0) goto L_0x0089;
    L_0x0084:
        r3 = "<fetch/>";
        r0.append(r3);
    L_0x0089:
        r3 = r7.getExtensionsXML();
        r0.append(r3);
        r3 = "</offline>";
        r0.append(r3);
        r3 = r0.toString();
        return r3;
    L_0x009a:
        r3 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x009a }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smackx.offline.packet.OfflineMessageRequest.getChildElementXML():java.lang.String");
    }
}
