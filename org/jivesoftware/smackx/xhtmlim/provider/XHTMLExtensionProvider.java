package org.jivesoftware.smackx.xhtmlim.provider;

import org.jivesoftware.smack.provider.PacketExtensionProvider;

public class XHTMLExtensionProvider implements PacketExtensionProvider {
    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final org.jivesoftware.smack.packet.PacketExtension parseExtension(org.xmlpull.v1.XmlPullParser r8) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        /*
        r7 = this;
        r3 = new org.jivesoftware.smackx.xhtmlim.packet.XHTMLExtension;
        r3.<init>();
        r2 = r8.getDepth();
    L_0x0009:
        r0 = r8.getEventType();
        r1 = r8.getName();
        r4 = 2;
        if (r0 != r4) goto L_0x0030;
    L_0x0014:
        r4 = "body";
        r4 = r1.equals(r4);
        if (r4 == 0) goto L_0x0029;
    L_0x001c:
        r4 = org.jivesoftware.smack.util.PacketParserUtils.parseElement(r8);
        r5 = r3.bodies;
        monitor-enter(r5);
        r6 = r3.bodies;	 Catch:{ all -> 0x002d }
        r6.add(r4);	 Catch:{ all -> 0x002d }
        monitor-exit(r5);	 Catch:{ all -> 0x002d }
    L_0x0029:
        r8.next();
        goto L_0x0009;
    L_0x002d:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x002d }
        throw r4;
    L_0x0030:
        r4 = 3;
        if (r0 != r4) goto L_0x0029;
    L_0x0033:
        r4 = "html";
        r4 = r1.equals(r4);
        if (r4 == 0) goto L_0x0029;
    L_0x003b:
        r4 = r8.getDepth();
        if (r4 > r2) goto L_0x0029;
    L_0x0041:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smackx.xhtmlim.provider.XHTMLExtensionProvider.parseExtension(org.xmlpull.v1.XmlPullParser):org.jivesoftware.smack.packet.PacketExtension");
    }
}
