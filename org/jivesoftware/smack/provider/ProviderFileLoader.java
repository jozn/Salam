package org.jivesoftware.smack.provider;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class ProviderFileLoader implements ProviderLoader {
    private static final Logger LOGGER;
    public List<Exception> exceptions;
    private final Collection<ExtensionProviderInfo> extProviders;
    private final Collection<IQProviderInfo> iqProviders;
    private final Collection<StreamFeatureProviderInfo> sfProviders;

    static {
        LOGGER = Logger.getLogger(ProviderFileLoader.class.getName());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ProviderFileLoader(java.io.InputStream r16, java.lang.ClassLoader r17) {
        /*
        r15 = this;
        r15.<init>();
        r11 = new java.util.LinkedList;
        r11.<init>();
        r15.iqProviders = r11;
        r11 = new java.util.LinkedList;
        r11.<init>();
        r15.extProviders = r11;
        r11 = new java.util.LinkedList;
        r11.<init>();
        r15.sfProviders = r11;
        r11 = new java.util.LinkedList;
        r11.<init>();
        r15.exceptions = r11;
        r11 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ Exception -> 0x00ff }
        r8 = r11.newPullParser();	 Catch:{ Exception -> 0x00ff }
        r11 = "http://xmlpull.org/v1/doc/features.html#process-namespaces";
        r12 = 1;
        r8.setFeature(r11, r12);	 Catch:{ Exception -> 0x00ff }
        r11 = "UTF-8";
        r0 = r16;
        r8.setInput(r0, r11);	 Catch:{ Exception -> 0x00ff }
        r5 = r8.getEventType();	 Catch:{ Exception -> 0x00ff }
    L_0x0038:
        r11 = 2;
        if (r5 != r11) goto L_0x008a;
    L_0x003b:
        r10 = r8.getName();	 Catch:{ Exception -> 0x00ff }
        r11 = "smackProviders";
        r11 = r11.equals(r10);	 Catch:{ IllegalArgumentException -> 0x00dc }
        if (r11 != 0) goto L_0x008a;
    L_0x0047:
        r8.next();	 Catch:{ IllegalArgumentException -> 0x00dc }
        r8.next();	 Catch:{ IllegalArgumentException -> 0x00dc }
        r4 = r8.nextText();	 Catch:{ IllegalArgumentException -> 0x00dc }
        r8.next();	 Catch:{ IllegalArgumentException -> 0x00dc }
        r8.next();	 Catch:{ IllegalArgumentException -> 0x00dc }
        r7 = r8.nextText();	 Catch:{ IllegalArgumentException -> 0x00dc }
        r8.next();	 Catch:{ IllegalArgumentException -> 0x00dc }
        r8.next();	 Catch:{ IllegalArgumentException -> 0x00dc }
        r1 = r8.nextText();	 Catch:{ IllegalArgumentException -> 0x00dc }
        r0 = r17;
        r9 = r0.loadClass(r1);	 Catch:{ ClassNotFoundException -> 0x00cc }
        r11 = -1;
        r12 = r10.hashCode();	 Catch:{ ClassNotFoundException -> 0x00cc }
        switch(r12) {
            case -797518000: goto L_0x009f;
            case 80611175: goto L_0x00a9;
            case 1834143545: goto L_0x0095;
            default: goto L_0x0073;
        };	 Catch:{ ClassNotFoundException -> 0x00cc }
    L_0x0073:
        switch(r11) {
            case 0: goto L_0x00b3;
            case 1: goto L_0x012d;
            case 2: goto L_0x015b;
            default: goto L_0x0076;
        };	 Catch:{ ClassNotFoundException -> 0x00cc }
    L_0x0076:
        r11 = LOGGER;	 Catch:{ ClassNotFoundException -> 0x00cc }
        r12 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x00cc }
        r13 = "Unkown provider type: ";
        r12.<init>(r13);	 Catch:{ ClassNotFoundException -> 0x00cc }
        r12 = r12.append(r10);	 Catch:{ ClassNotFoundException -> 0x00cc }
        r12 = r12.toString();	 Catch:{ ClassNotFoundException -> 0x00cc }
        r11.warning(r12);	 Catch:{ ClassNotFoundException -> 0x00cc }
    L_0x008a:
        r5 = r8.next();	 Catch:{ Exception -> 0x00ff }
        r11 = 1;
        if (r5 != r11) goto L_0x0038;
    L_0x0091:
        r16.close();	 Catch:{ Exception -> 0x016d }
    L_0x0094:
        return;
    L_0x0095:
        r12 = "iqProvider";
        r12 = r10.equals(r12);	 Catch:{ ClassNotFoundException -> 0x00cc }
        if (r12 == 0) goto L_0x0073;
    L_0x009d:
        r11 = 0;
        goto L_0x0073;
    L_0x009f:
        r12 = "extensionProvider";
        r12 = r10.equals(r12);	 Catch:{ ClassNotFoundException -> 0x00cc }
        if (r12 == 0) goto L_0x0073;
    L_0x00a7:
        r11 = 1;
        goto L_0x0073;
    L_0x00a9:
        r12 = "streamFeatureProvider";
        r12 = r10.equals(r12);	 Catch:{ ClassNotFoundException -> 0x00cc }
        if (r12 == 0) goto L_0x0073;
    L_0x00b1:
        r11 = 2;
        goto L_0x0073;
    L_0x00b3:
        r11 = org.jivesoftware.smack.provider.IQProvider.class;
        r11 = r11.isAssignableFrom(r9);	 Catch:{ ClassNotFoundException -> 0x00cc }
        if (r11 == 0) goto L_0x0114;
    L_0x00bb:
        r12 = r15.iqProviders;	 Catch:{ ClassNotFoundException -> 0x00cc }
        r13 = new org.jivesoftware.smack.provider.IQProviderInfo;	 Catch:{ ClassNotFoundException -> 0x00cc }
        r11 = r9.newInstance();	 Catch:{ ClassNotFoundException -> 0x00cc }
        r11 = (org.jivesoftware.smack.provider.IQProvider) r11;	 Catch:{ ClassNotFoundException -> 0x00cc }
        r13.<init>(r4, r7, r11);	 Catch:{ ClassNotFoundException -> 0x00cc }
        r12.add(r13);	 Catch:{ ClassNotFoundException -> 0x00cc }
        goto L_0x008a;
    L_0x00cc:
        r2 = move-exception;
        r11 = LOGGER;	 Catch:{ IllegalArgumentException -> 0x00dc }
        r12 = java.util.logging.Level.SEVERE;	 Catch:{ IllegalArgumentException -> 0x00dc }
        r13 = "Could not find provider class";
        r11.log(r12, r13, r2);	 Catch:{ IllegalArgumentException -> 0x00dc }
        r11 = r15.exceptions;	 Catch:{ IllegalArgumentException -> 0x00dc }
        r11.add(r2);	 Catch:{ IllegalArgumentException -> 0x00dc }
        goto L_0x008a;
    L_0x00dc:
        r6 = move-exception;
        r11 = LOGGER;	 Catch:{ Exception -> 0x00ff }
        r12 = java.util.logging.Level.SEVERE;	 Catch:{ Exception -> 0x00ff }
        r13 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00ff }
        r14 = "Invalid provider type found [";
        r13.<init>(r14);	 Catch:{ Exception -> 0x00ff }
        r13 = r13.append(r10);	 Catch:{ Exception -> 0x00ff }
        r14 = "] when expecting iqProvider or extensionProvider";
        r13 = r13.append(r14);	 Catch:{ Exception -> 0x00ff }
        r13 = r13.toString();	 Catch:{ Exception -> 0x00ff }
        r11.log(r12, r13, r6);	 Catch:{ Exception -> 0x00ff }
        r11 = r15.exceptions;	 Catch:{ Exception -> 0x00ff }
        r11.add(r6);	 Catch:{ Exception -> 0x00ff }
        goto L_0x008a;
    L_0x00ff:
        r3 = move-exception;
        r11 = LOGGER;	 Catch:{ all -> 0x0128 }
        r12 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0128 }
        r13 = "Unknown error occurred while parsing provider file";
        r11.log(r12, r13, r3);	 Catch:{ all -> 0x0128 }
        r11 = r15.exceptions;	 Catch:{ all -> 0x0128 }
        r11.add(r3);	 Catch:{ all -> 0x0128 }
        r16.close();	 Catch:{ Exception -> 0x0112 }
        goto L_0x0094;
    L_0x0112:
        r11 = move-exception;
        goto L_0x0094;
    L_0x0114:
        r11 = org.jivesoftware.smack.packet.IQ.class;
        r11 = r11.isAssignableFrom(r9);	 Catch:{ ClassNotFoundException -> 0x00cc }
        if (r11 == 0) goto L_0x008a;
    L_0x011c:
        r11 = r15.iqProviders;	 Catch:{ ClassNotFoundException -> 0x00cc }
        r12 = new org.jivesoftware.smack.provider.IQProviderInfo;	 Catch:{ ClassNotFoundException -> 0x00cc }
        r12.<init>(r4, r7, r9);	 Catch:{ ClassNotFoundException -> 0x00cc }
        r11.add(r12);	 Catch:{ ClassNotFoundException -> 0x00cc }
        goto L_0x008a;
    L_0x0128:
        r11 = move-exception;
        r16.close();	 Catch:{ Exception -> 0x0170 }
    L_0x012c:
        throw r11;
    L_0x012d:
        r11 = org.jivesoftware.smack.provider.PacketExtensionProvider.class;
        r11 = r11.isAssignableFrom(r9);	 Catch:{ ClassNotFoundException -> 0x00cc }
        if (r11 == 0) goto L_0x0147;
    L_0x0135:
        r12 = r15.extProviders;	 Catch:{ ClassNotFoundException -> 0x00cc }
        r13 = new org.jivesoftware.smack.provider.ExtensionProviderInfo;	 Catch:{ ClassNotFoundException -> 0x00cc }
        r11 = r9.newInstance();	 Catch:{ ClassNotFoundException -> 0x00cc }
        r11 = (org.jivesoftware.smack.provider.PacketExtensionProvider) r11;	 Catch:{ ClassNotFoundException -> 0x00cc }
        r13.<init>(r4, r7, r11);	 Catch:{ ClassNotFoundException -> 0x00cc }
        r12.add(r13);	 Catch:{ ClassNotFoundException -> 0x00cc }
        goto L_0x008a;
    L_0x0147:
        r11 = org.jivesoftware.smack.packet.PacketExtension.class;
        r11 = r11.isAssignableFrom(r9);	 Catch:{ ClassNotFoundException -> 0x00cc }
        if (r11 == 0) goto L_0x008a;
    L_0x014f:
        r11 = r15.extProviders;	 Catch:{ ClassNotFoundException -> 0x00cc }
        r12 = new org.jivesoftware.smack.provider.ExtensionProviderInfo;	 Catch:{ ClassNotFoundException -> 0x00cc }
        r12.<init>(r4, r7, r9);	 Catch:{ ClassNotFoundException -> 0x00cc }
        r11.add(r12);	 Catch:{ ClassNotFoundException -> 0x00cc }
        goto L_0x008a;
    L_0x015b:
        r12 = r15.sfProviders;	 Catch:{ ClassNotFoundException -> 0x00cc }
        r13 = new org.jivesoftware.smack.provider.StreamFeatureProviderInfo;	 Catch:{ ClassNotFoundException -> 0x00cc }
        r11 = r9.newInstance();	 Catch:{ ClassNotFoundException -> 0x00cc }
        r11 = (org.jivesoftware.smack.provider.StreamFeatureProvider) r11;	 Catch:{ ClassNotFoundException -> 0x00cc }
        r13.<init>(r4, r7, r11);	 Catch:{ ClassNotFoundException -> 0x00cc }
        r12.add(r13);	 Catch:{ ClassNotFoundException -> 0x00cc }
        goto L_0x008a;
    L_0x016d:
        r11 = move-exception;
        goto L_0x0094;
    L_0x0170:
        r12 = move-exception;
        goto L_0x012c;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.provider.ProviderFileLoader.<init>(java.io.InputStream, java.lang.ClassLoader):void");
    }

    public final Collection<IQProviderInfo> getIQProviderInfo() {
        return this.iqProviders;
    }

    public final Collection<ExtensionProviderInfo> getExtensionProviderInfo() {
        return this.extProviders;
    }
}
