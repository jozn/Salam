package com.shamchat.androidclient.chat.extension;

import org.xmlpull.v1.XmlPullParser;

public abstract class AbstractProvider<T extends Instance> extends AbstractInflater<T> {
    protected abstract T createInstance(XmlPullParser xmlPullParser);

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final T provideInstance(org.xmlpull.v1.XmlPullParser r5) throws java.lang.Exception {
        /*
        r4 = this;
        r0 = r4.createInstance(r5);
        r1 = r5.getName();
    L_0x0008:
        r2 = r5.next();
        r3 = 2;
        if (r2 != r3) goto L_0x0013;
    L_0x000f:
        com.shamchat.androidclient.chat.extension.ProviderUtils.skipTag(r5);
        goto L_0x0008;
    L_0x0013:
        r3 = 3;
        if (r2 != r3) goto L_0x0026;
    L_0x0016:
        r2 = r5.getName();
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x0029;
    L_0x0020:
        r1 = new java.lang.IllegalStateException;
        r1.<init>();
        throw r1;
    L_0x0026:
        r3 = 1;
        if (r2 != r3) goto L_0x0008;
    L_0x0029:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shamchat.androidclient.chat.extension.AbstractProvider.provideInstance(org.xmlpull.v1.XmlPullParser):T");
    }
}
