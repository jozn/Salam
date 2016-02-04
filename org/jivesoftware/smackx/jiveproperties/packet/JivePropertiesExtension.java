package org.jivesoftware.smackx.jiveproperties.packet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;
import org.jivesoftware.smack.packet.PacketExtension;

public class JivePropertiesExtension implements PacketExtension {
    private static final Logger LOGGER;
    private final Map<String, Object> properties;

    static {
        LOGGER = Logger.getLogger(JivePropertiesExtension.class.getName());
    }

    public JivePropertiesExtension() {
        this.properties = new HashMap();
    }

    public JivePropertiesExtension(Map<String, Object> properties) {
        this.properties = properties;
    }

    private synchronized Object getProperty(String name) {
        Object obj;
        if (this.properties == null) {
            obj = null;
        } else {
            obj = this.properties.get(name);
        }
        return obj;
    }

    private synchronized Collection<String> getPropertyNames() {
        Collection<String> emptySet;
        if (this.properties == null) {
            emptySet = Collections.emptySet();
        } else {
            emptySet = Collections.unmodifiableSet(new HashSet(this.properties.keySet()));
        }
        return emptySet;
    }

    public final String getElementName() {
        return "properties";
    }

    public final String getNamespace() {
        return "http://www.jivesoftware.com/xmlns/xmpp/properties";
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.CharSequence toXML() {
        /*
        r14 = this;
        r10 = new org.jivesoftware.smack.util.XmlStringBuilder;
        r10.<init>(r14);
        r10.rightAngleBracket();
        r11 = r14.getPropertyNames();
        r3 = r11.iterator();
    L_0x0010:
        r11 = r3.hasNext();
        if (r11 == 0) goto L_0x0101;
    L_0x0016:
        r4 = r3.next();
        r4 = (java.lang.String) r4;
        r8 = r14.getProperty(r4);
        r11 = "property";
        r10.openElement(r11);
        r11 = "name";
        r10.element(r11, r4);
        r11 = "value";
        r10.halfOpenElement(r11);
        r11 = r8 instanceof java.lang.Integer;
        if (r11 == 0) goto L_0x0055;
    L_0x0033:
        r7 = "integer";
        r8 = (java.lang.Integer) r8;
        r11 = r8.intValue();
        r9 = java.lang.Integer.toString(r11);
    L_0x003f:
        r11 = "type";
        r10.attribute(r11, r7);
        r10.rightAngleBracket();
        r10.escape(r9);
        r11 = "value";
        r10.closeElement(r11);
        r11 = "property";
        r10.closeElement(r11);
        goto L_0x0010;
    L_0x0055:
        r11 = r8 instanceof java.lang.Long;
        if (r11 == 0) goto L_0x0066;
    L_0x0059:
        r7 = "long";
        r8 = (java.lang.Long) r8;
        r12 = r8.longValue();
        r9 = java.lang.Long.toString(r12);
        goto L_0x003f;
    L_0x0066:
        r11 = r8 instanceof java.lang.Float;
        if (r11 == 0) goto L_0x0077;
    L_0x006a:
        r7 = "float";
        r8 = (java.lang.Float) r8;
        r11 = r8.floatValue();
        r9 = java.lang.Float.toString(r11);
        goto L_0x003f;
    L_0x0077:
        r11 = r8 instanceof java.lang.Double;
        if (r11 == 0) goto L_0x0088;
    L_0x007b:
        r7 = "double";
        r8 = (java.lang.Double) r8;
        r12 = r8.doubleValue();
        r9 = java.lang.Double.toString(r12);
        goto L_0x003f;
    L_0x0088:
        r11 = r8 instanceof java.lang.Boolean;
        if (r11 == 0) goto L_0x0099;
    L_0x008c:
        r7 = "boolean";
        r8 = (java.lang.Boolean) r8;
        r11 = r8.booleanValue();
        r9 = java.lang.Boolean.toString(r11);
        goto L_0x003f;
    L_0x0099:
        r11 = r8 instanceof java.lang.String;
        if (r11 == 0) goto L_0x00a3;
    L_0x009d:
        r7 = "string";
        r9 = r8;
        r9 = (java.lang.String) r9;
        goto L_0x003f;
    L_0x00a3:
        r0 = 0;
        r5 = 0;
        r1 = new java.io.ByteArrayOutputStream;	 Catch:{ Exception -> 0x00c7 }
        r1.<init>();	 Catch:{ Exception -> 0x00c7 }
        r6 = new java.io.ObjectOutputStream;	 Catch:{ Exception -> 0x0114, all -> 0x010d }
        r6.<init>(r1);	 Catch:{ Exception -> 0x0114, all -> 0x010d }
        r6.writeObject(r8);	 Catch:{ Exception -> 0x0117, all -> 0x0110 }
        r7 = "java-object";
        r11 = r1.toByteArray();	 Catch:{ Exception -> 0x0117, all -> 0x0110 }
        r9 = org.jivesoftware.smack.util.stringencoder.Base64.encodeToString(r11);	 Catch:{ Exception -> 0x0117, all -> 0x0110 }
        r6.close();	 Catch:{ Exception -> 0x0105 }
    L_0x00bf:
        r1.close();	 Catch:{ Exception -> 0x00c4 }
        goto L_0x003f;
    L_0x00c4:
        r11 = move-exception;
        goto L_0x003f;
    L_0x00c7:
        r2 = move-exception;
    L_0x00c8:
        r11 = LOGGER;	 Catch:{ all -> 0x00f5 }
        r12 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x00f5 }
        r13 = "Error encoding java object";
        r11.log(r12, r13, r2);	 Catch:{ all -> 0x00f5 }
        r7 = "java-object";
        r11 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f5 }
        r12 = "Serializing error: ";
        r11.<init>(r12);	 Catch:{ all -> 0x00f5 }
        r12 = r2.getMessage();	 Catch:{ all -> 0x00f5 }
        r11 = r11.append(r12);	 Catch:{ all -> 0x00f5 }
        r9 = r11.toString();	 Catch:{ all -> 0x00f5 }
        if (r5 == 0) goto L_0x00eb;
    L_0x00e8:
        r5.close();	 Catch:{ Exception -> 0x0107 }
    L_0x00eb:
        if (r0 == 0) goto L_0x003f;
    L_0x00ed:
        r0.close();	 Catch:{ Exception -> 0x00f2 }
        goto L_0x003f;
    L_0x00f2:
        r11 = move-exception;
        goto L_0x003f;
    L_0x00f5:
        r11 = move-exception;
    L_0x00f6:
        if (r5 == 0) goto L_0x00fb;
    L_0x00f8:
        r5.close();	 Catch:{ Exception -> 0x0109 }
    L_0x00fb:
        if (r0 == 0) goto L_0x0100;
    L_0x00fd:
        r0.close();	 Catch:{ Exception -> 0x010b }
    L_0x0100:
        throw r11;
    L_0x0101:
        r10.closeElement(r14);
        return r10;
    L_0x0105:
        r11 = move-exception;
        goto L_0x00bf;
    L_0x0107:
        r11 = move-exception;
        goto L_0x00eb;
    L_0x0109:
        r12 = move-exception;
        goto L_0x00fb;
    L_0x010b:
        r12 = move-exception;
        goto L_0x0100;
    L_0x010d:
        r11 = move-exception;
        r0 = r1;
        goto L_0x00f6;
    L_0x0110:
        r11 = move-exception;
        r5 = r6;
        r0 = r1;
        goto L_0x00f6;
    L_0x0114:
        r2 = move-exception;
        r0 = r1;
        goto L_0x00c8;
    L_0x0117:
        r2 = move-exception;
        r5 = r6;
        r0 = r1;
        goto L_0x00c8;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smackx.jiveproperties.packet.JivePropertiesExtension.toXML():java.lang.CharSequence");
    }
}
