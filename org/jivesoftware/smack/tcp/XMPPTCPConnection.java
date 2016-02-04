package org.jivesoftware.smack.tcp;

import android.support.v7.appcompat.BuildConfig;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.PasswordCallback;
import org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.AlreadyConnectedException;
import org.jivesoftware.smack.SmackException.AlreadyLoggedInException;
import org.jivesoftware.smack.SmackException.ConnectionException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.SecurityNotPossibleException;
import org.jivesoftware.smack.SmackException.SecurityRequiredException;
import org.jivesoftware.smack.SynchronizationPoint;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.compress.packet.Compress;
import org.jivesoftware.smack.compress.packet.Compress.Feature;
import org.jivesoftware.smack.compression.XMPPInputOutputStream;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Element;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PlainStreamElement;
import org.jivesoftware.smack.packet.StartTls;
import org.jivesoftware.smack.packet.StreamOpen;
import org.jivesoftware.smack.parsing.ParsingExceptionCallback;
import org.jivesoftware.smack.sasl.SASLAnonymous;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.tcp.sm.SMUtils;
import org.jivesoftware.smack.tcp.sm.packet.StreamManagement.AckRequest;
import org.jivesoftware.smack.tcp.sm.packet.StreamManagement.Enable;
import org.jivesoftware.smack.tcp.sm.packet.StreamManagement.Resume;
import org.jivesoftware.smack.tcp.sm.predicates.ForEveryMessage;
import org.jivesoftware.smack.tcp.sm.predicates.ForMatchingPredicateOrAfterXStanzas;
import org.jivesoftware.smack.util.ArrayBlockingQueueWithShutdown;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.dns.HostAddress;
import org.xmlpull.v1.XmlPullParser;

public class XMPPTCPConnection extends AbstractXMPPConnection {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final Logger LOGGER;
    private static boolean useSmDefault;
    private static boolean useSmResumptionDefault;
    private long clientHandledStanzasCount;
    private final SynchronizationPoint<XMPPException> compressSyncPoint;
    public boolean connected;
    private String connectionID;
    private boolean disconnectedButResumeable;
    private final Map<String, PacketListener> idStanzaAcknowledgedListeners;
    private final SynchronizationPoint<Exception> initalOpenStreamSend;
    private final SynchronizationPoint<XMPPException> maybeCompressFeaturesReceived;
    protected PacketReader packetReader;
    protected PacketWriter packetWriter;
    ParsingExceptionCallback parsingExceptionCallback;
    private final Set<PacketFilter> requestAckPredicates;
    private long serverHandledStanzasCount;
    private int smClientMaxResumptionTime;
    public final SynchronizationPoint<XMPPException> smEnabledSyncPoint;
    private final SynchronizationPoint<XMPPException> smResumedSyncPoint;
    private int smServerMaxResumptimTime;
    private String smSessionId;
    private Socket socket;
    volatile boolean socketClosed;
    private final Collection<PacketListener> stanzaAcknowledgedListeners;
    private BlockingQueue<Packet> unacknowledgedStanzas;
    private boolean useSm;
    private boolean useSmResumption;
    private boolean usingTLS;

    protected class PacketReader {
        volatile boolean done;
        XmlPullParser parser;
        Thread readerThread;

        /* renamed from: org.jivesoftware.smack.tcp.XMPPTCPConnection.PacketReader.1 */
        class C12891 extends Thread {
            C12891() {
            }

            public final void run() {
                PacketReader.access$200(PacketReader.this);
            }
        }

        protected PacketReader() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static /* synthetic */ void access$200(org.jivesoftware.smack.tcp.XMPPTCPConnection.PacketReader r9) {
            /*
            r3 = -1;
            r5 = 2;
            r2 = 0;
            r4 = 1;
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.initalOpenStreamSend;	 Catch:{ Exception -> 0x0121 }
            r0.checkIfSuccessOrWait();	 Catch:{ Exception -> 0x0121 }
            r0 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r1 = r0.getEventType();	 Catch:{ Exception -> 0x0121 }
        L_0x0013:
            if (r1 != r5) goto L_0x0491;
        L_0x0015:
            r0 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r6 = r0.getName();	 Catch:{ Exception -> 0x0121 }
            r0 = r6.hashCode();	 Catch:{ Exception -> 0x0121 }
            switch(r0) {
                case -1867169789: goto L_0x00a4;
                case -1609594047: goto L_0x00bc;
                case -1281977283: goto L_0x00c8;
                case -1276666629: goto L_0x005d;
                case -1086574198: goto L_0x008f;
                case -891990144: goto L_0x0067;
                case -369449087: goto L_0x00b0;
                case -309519186: goto L_0x0085;
                case -290659267: goto L_0x007b;
                case 97: goto L_0x00e0;
                case 114: goto L_0x00ec;
                case 3368: goto L_0x0053;
                case 96784904: goto L_0x0071;
                case 954925063: goto L_0x0049;
                case 1097547223: goto L_0x00d4;
                case 1402633315: goto L_0x0099;
                default: goto L_0x0022;
            };	 Catch:{ Exception -> 0x0121 }
        L_0x0022:
            r0 = r3;
        L_0x0023:
            switch(r0) {
                case 0: goto L_0x00f8;
                case 1: goto L_0x00f8;
                case 2: goto L_0x00f8;
                case 3: goto L_0x0175;
                case 4: goto L_0x01c3;
                case 5: goto L_0x01cf;
                case 6: goto L_0x01d8;
                case 7: goto L_0x01f4;
                case 8: goto L_0x025e;
                case 9: goto L_0x0270;
                case 10: goto L_0x029d;
                case 11: goto L_0x02b2;
                case 12: goto L_0x0333;
                case 13: goto L_0x03b5;
                case 14: goto L_0x0443;
                case 15: goto L_0x0466;
                default: goto L_0x0026;
            };	 Catch:{ Exception -> 0x0121 }
        L_0x0026:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.LOGGER;	 Catch:{ Exception -> 0x0121 }
            r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0121 }
            r7 = "Unkown top level stream element: ";
            r1.<init>(r7);	 Catch:{ Exception -> 0x0121 }
            r1 = r1.append(r6);	 Catch:{ Exception -> 0x0121 }
            r1 = r1.toString();	 Catch:{ Exception -> 0x0121 }
            r0.warning(r1);	 Catch:{ Exception -> 0x0121 }
        L_0x003c:
            r0 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.next();	 Catch:{ Exception -> 0x0121 }
        L_0x0042:
            r1 = r9.done;	 Catch:{ Exception -> 0x0121 }
            if (r1 != 0) goto L_0x0048;
        L_0x0046:
            if (r0 != r4) goto L_0x04a9;
        L_0x0048:
            return;
        L_0x0049:
            r0 = "message";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x0051:
            r0 = r2;
            goto L_0x0023;
        L_0x0053:
            r0 = "iq";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x005b:
            r0 = r4;
            goto L_0x0023;
        L_0x005d:
            r0 = "presence";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x0065:
            r0 = r5;
            goto L_0x0023;
        L_0x0067:
            r0 = "stream";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x006f:
            r0 = 3;
            goto L_0x0023;
        L_0x0071:
            r0 = "error";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x0079:
            r0 = 4;
            goto L_0x0023;
        L_0x007b:
            r0 = "features";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x0083:
            r0 = 5;
            goto L_0x0023;
        L_0x0085:
            r0 = "proceed";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x008d:
            r0 = 6;
            goto L_0x0023;
        L_0x008f:
            r0 = "failure";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x0097:
            r0 = 7;
            goto L_0x0023;
        L_0x0099:
            r0 = "challenge";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x00a1:
            r0 = 8;
            goto L_0x0023;
        L_0x00a4:
            r0 = "success";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x00ac:
            r0 = 9;
            goto L_0x0023;
        L_0x00b0:
            r0 = "compressed";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x00b8:
            r0 = 10;
            goto L_0x0023;
        L_0x00bc:
            r0 = "enabled";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x00c4:
            r0 = 11;
            goto L_0x0023;
        L_0x00c8:
            r0 = "failed";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x00d0:
            r0 = 12;
            goto L_0x0023;
        L_0x00d4:
            r0 = "resumed";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x00dc:
            r0 = 13;
            goto L_0x0023;
        L_0x00e0:
            r0 = "a";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x00e8:
            r0 = 14;
            goto L_0x0023;
        L_0x00ec:
            r0 = "r";
            r0 = r6.equals(r0);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0022;
        L_0x00f4:
            r0 = 15;
            goto L_0x0023;
        L_0x00f8:
            r0 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r6 = r0.getDepth();	 Catch:{ Exception -> 0x0121 }
            r0 = r9.parser;	 Catch:{ Exception -> 0x0133 }
            r7 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0133 }
            r0 = org.jivesoftware.smack.util.PacketParserUtils.parseStanza(r0, r7);	 Catch:{ Exception -> 0x0133 }
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r6 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r6 = r6.clientHandledStanzasCount;	 Catch:{ Exception -> 0x0121 }
            r6 = org.jivesoftware.smack.tcp.sm.SMUtils.incrementHeight(r6);	 Catch:{ Exception -> 0x0121 }
            r1.clientHandledStanzasCount = r6;	 Catch:{ Exception -> 0x0121 }
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r1.lastStanzaReceived = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0121 }
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r1.processPacket(r0);	 Catch:{ Exception -> 0x0121 }
            goto L_0x003c;
        L_0x0121:
            r0 = move-exception;
            r1 = r9.done;
            if (r1 != 0) goto L_0x0048;
        L_0x0126:
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;
            r1 = r1.socketClosed;
            if (r1 != 0) goto L_0x0048;
        L_0x012c:
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;
            r1.notifyConnectionError(r0);
            goto L_0x0048;
        L_0x0133:
            r0 = move-exception;
            r7 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ all -> 0x015f }
            r7 = r7.parsingExceptionCallback;	 Catch:{ all -> 0x015f }
            r8 = r9.parser;	 Catch:{ all -> 0x015f }
            r6 = org.jivesoftware.smack.util.PacketParserUtils.parseContentDepth(r8, r6);	 Catch:{ all -> 0x015f }
            r8 = new org.jivesoftware.smack.parsing.UnparsablePacket;	 Catch:{ all -> 0x015f }
            r8.<init>(r6, r0);	 Catch:{ all -> 0x015f }
            if (r7 == 0) goto L_0x0148;
        L_0x0145:
            r7.handleUnparsablePacket(r8);	 Catch:{ all -> 0x015f }
        L_0x0148:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r6 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r6 = r6.clientHandledStanzasCount;	 Catch:{ Exception -> 0x0121 }
            r6 = org.jivesoftware.smack.tcp.sm.SMUtils.incrementHeight(r6);	 Catch:{ Exception -> 0x0121 }
            r0.clientHandledStanzasCount = r6;	 Catch:{ Exception -> 0x0121 }
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0.lastStanzaReceived = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0121 }
            r0 = r1;
            goto L_0x0042;
        L_0x015f:
            r0 = move-exception;
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r2 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r2 = r2.clientHandledStanzasCount;	 Catch:{ Exception -> 0x0121 }
            r2 = org.jivesoftware.smack.tcp.sm.SMUtils.incrementHeight(r2);	 Catch:{ Exception -> 0x0121 }
            r1.clientHandledStanzasCount = r2;	 Catch:{ Exception -> 0x0121 }
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r1.lastStanzaReceived = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0121 }
            throw r0;	 Catch:{ Exception -> 0x0121 }
        L_0x0175:
            r0 = "jabber:client";
            r1 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r6 = 0;
            r1 = r1.getNamespace(r6);	 Catch:{ Exception -> 0x0121 }
            r0 = r0.equals(r1);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x003c;
        L_0x0184:
            r0 = r2;
        L_0x0185:
            r1 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r1 = r1.getAttributeCount();	 Catch:{ Exception -> 0x0121 }
            if (r0 >= r1) goto L_0x003c;
        L_0x018d:
            r1 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r1 = r1.getAttributeName(r0);	 Catch:{ Exception -> 0x0121 }
            r6 = "id";
            r1 = r1.equals(r6);	 Catch:{ Exception -> 0x0121 }
            if (r1 == 0) goto L_0x01a9;
        L_0x019b:
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r6 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r6 = r6.getAttributeValue(r0);	 Catch:{ Exception -> 0x0121 }
            r1.connectionID = r6;	 Catch:{ Exception -> 0x0121 }
        L_0x01a6:
            r0 = r0 + 1;
            goto L_0x0185;
        L_0x01a9:
            r1 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r1 = r1.getAttributeName(r0);	 Catch:{ Exception -> 0x0121 }
            r6 = "from";
            r1 = r1.equals(r6);	 Catch:{ Exception -> 0x0121 }
            if (r1 == 0) goto L_0x01a6;
        L_0x01b7:
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r6 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r6 = r6.getAttributeValue(r0);	 Catch:{ Exception -> 0x0121 }
            r1.setServiceName(r6);	 Catch:{ Exception -> 0x0121 }
            goto L_0x01a6;
        L_0x01c3:
            r0 = new org.jivesoftware.smack.XMPPException$StreamErrorException;	 Catch:{ Exception -> 0x0121 }
            r1 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r1 = org.jivesoftware.smack.util.PacketParserUtils.parseStreamError(r1);	 Catch:{ Exception -> 0x0121 }
            r0.<init>(r1);	 Catch:{ Exception -> 0x0121 }
            throw r0;	 Catch:{ Exception -> 0x0121 }
        L_0x01cf:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r1 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r0.parseFeatures(r1);	 Catch:{ Exception -> 0x0121 }
            goto L_0x003c;
        L_0x01d8:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x01e4 }
            org.jivesoftware.smack.tcp.XMPPTCPConnection.access$1000(r0);	 Catch:{ Exception -> 0x01e4 }
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x01e4 }
            r0.openStream();	 Catch:{ Exception -> 0x01e4 }
            goto L_0x003c;
        L_0x01e4:
            r0 = move-exception;
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r1 = r1.saslFeatureReceived;	 Catch:{ Exception -> 0x0121 }
            r2 = new org.jivesoftware.smack.SmackException;	 Catch:{ Exception -> 0x0121 }
            r2.<init>(r0);	 Catch:{ Exception -> 0x0121 }
            r1.reportFailure(r2);	 Catch:{ Exception -> 0x0121 }
            throw r0;	 Catch:{ Exception -> 0x0121 }
        L_0x01f4:
            r0 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r1 = 0;
            r0 = r0.getNamespace(r1);	 Catch:{ Exception -> 0x0121 }
            r1 = r0.hashCode();	 Catch:{ Exception -> 0x0121 }
            switch(r1) {
                case -1570142914: goto L_0x0225;
                case 919182852: goto L_0x0211;
                case 2117926358: goto L_0x021b;
                default: goto L_0x0202;
            };	 Catch:{ Exception -> 0x0121 }
        L_0x0202:
            r0 = r3;
        L_0x0203:
            switch(r0) {
                case 0: goto L_0x0208;
                case 1: goto L_0x022f;
                case 2: goto L_0x0242;
                default: goto L_0x0206;
            };	 Catch:{ Exception -> 0x0121 }
        L_0x0206:
            goto L_0x003c;
        L_0x0208:
            r0 = new org.jivesoftware.smack.XMPPException$XMPPErrorException;	 Catch:{ Exception -> 0x0121 }
            r1 = "TLS negotiation has failed";
            r2 = 0;
            r0.<init>(r1, r2);	 Catch:{ Exception -> 0x0121 }
            throw r0;	 Catch:{ Exception -> 0x0121 }
        L_0x0211:
            r1 = "urn:ietf:params:xml:ns:xmpp-tls";
            r0 = r0.equals(r1);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0202;
        L_0x0219:
            r0 = r2;
            goto L_0x0203;
        L_0x021b:
            r1 = "http://jabber.org/protocol/compress";
            r0 = r0.equals(r1);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0202;
        L_0x0223:
            r0 = r4;
            goto L_0x0203;
        L_0x0225:
            r1 = "urn:ietf:params:xml:ns:xmpp-sasl";
            r0 = r0.equals(r1);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0202;
        L_0x022d:
            r0 = r5;
            goto L_0x0203;
        L_0x022f:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.compressSyncPoint;	 Catch:{ Exception -> 0x0121 }
            r1 = new org.jivesoftware.smack.XMPPException$XMPPErrorException;	 Catch:{ Exception -> 0x0121 }
            r6 = "Could not establish compression";
            r7 = 0;
            r1.<init>(r6, r7);	 Catch:{ Exception -> 0x0121 }
            r0.reportFailure(r1);	 Catch:{ Exception -> 0x0121 }
            goto L_0x003c;
        L_0x0242:
            r0 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r0 = org.jivesoftware.smack.util.PacketParserUtils.parseSASLFailure(r0);	 Catch:{ Exception -> 0x0121 }
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r1 = r1.saslAuthentication;	 Catch:{ Exception -> 0x0121 }
            r6 = new org.jivesoftware.smack.sasl.SASLErrorException;	 Catch:{ Exception -> 0x0121 }
            r7 = r1.currentMechanism;	 Catch:{ Exception -> 0x0121 }
            r7 = r7.getName();	 Catch:{ Exception -> 0x0121 }
            r6.<init>(r7, r0);	 Catch:{ Exception -> 0x0121 }
            r1.authenticationFailed(r6);	 Catch:{ Exception -> 0x0121 }
            goto L_0x003c;
        L_0x025e:
            r0 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.nextText();	 Catch:{ Exception -> 0x0121 }
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r1 = r1.saslAuthentication;	 Catch:{ Exception -> 0x0121 }
            r6 = 0;
            r1.challengeReceived(r0, r6);	 Catch:{ Exception -> 0x0121 }
            goto L_0x003c;
        L_0x0270:
            r0 = new org.jivesoftware.smack.sasl.packet.SaslStreamElements$Success;	 Catch:{ Exception -> 0x0121 }
            r1 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r1 = r1.nextText();	 Catch:{ Exception -> 0x0121 }
            r0.<init>(r1);	 Catch:{ Exception -> 0x0121 }
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r1.openStream();	 Catch:{ Exception -> 0x0121 }
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r1 = r1.saslAuthentication;	 Catch:{ Exception -> 0x0121 }
            r6 = r0.data;	 Catch:{ Exception -> 0x0121 }
            if (r6 == 0) goto L_0x0290;
        L_0x028a:
            r0 = r0.data;	 Catch:{ Exception -> 0x0121 }
            r6 = 1;
            r1.challengeReceived(r0, r6);	 Catch:{ Exception -> 0x0121 }
        L_0x0290:
            r0 = 1;
            r1.authenticationSuccessful = r0;	 Catch:{ Exception -> 0x0121 }
            monitor-enter(r1);	 Catch:{ Exception -> 0x0121 }
            r1.notify();	 Catch:{ all -> 0x029a }
            monitor-exit(r1);	 Catch:{ all -> 0x029a }
            goto L_0x003c;
        L_0x029a:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x029a }
            throw r0;	 Catch:{ Exception -> 0x0121 }
        L_0x029d:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0.initReaderAndWriter();	 Catch:{ Exception -> 0x0121 }
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0.openStream();	 Catch:{ Exception -> 0x0121 }
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.compressSyncPoint;	 Catch:{ Exception -> 0x0121 }
            r0.reportSuccess();	 Catch:{ Exception -> 0x0121 }
            goto L_0x003c;
        L_0x02b2:
            r0 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            org.jivesoftware.smack.util.ParserUtils.assertAtStartTag(r0);	 Catch:{ Exception -> 0x0121 }
            r1 = "resume";
            r1 = org.jivesoftware.smack.util.ParserUtils.getBooleanAttribute$24eeb9f1(r0, r1);	 Catch:{ Exception -> 0x0121 }
            r6 = "";
            r7 = "id";
            r6 = r0.getAttributeValue(r6, r7);	 Catch:{ Exception -> 0x0121 }
            r7 = "";
            r8 = "location";
            r7 = r0.getAttributeValue(r7, r8);	 Catch:{ Exception -> 0x0121 }
            r8 = "max";
            r8 = org.jivesoftware.smack.util.ParserUtils.getIntegerAttribute$24eef9d3(r0, r8);	 Catch:{ Exception -> 0x0121 }
            r0.next();	 Catch:{ Exception -> 0x0121 }
            org.jivesoftware.smack.util.ParserUtils.assertAtEndTag(r0);	 Catch:{ Exception -> 0x0121 }
            r0 = new org.jivesoftware.smack.tcp.sm.packet.StreamManagement$Enabled;	 Catch:{ Exception -> 0x0121 }
            r0.<init>(r6, r1, r7, r8);	 Catch:{ Exception -> 0x0121 }
            r1 = r0.isResumeSet();	 Catch:{ Exception -> 0x0121 }
            if (r1 == 0) goto L_0x032c;
        L_0x02e4:
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r6 = r0.id;	 Catch:{ Exception -> 0x0121 }
            r1.smSessionId = r6;	 Catch:{ Exception -> 0x0121 }
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r1 = r1.smSessionId;	 Catch:{ Exception -> 0x0121 }
            r1 = org.jivesoftware.smack.util.StringUtils.isNullOrEmpty(r1);	 Catch:{ Exception -> 0x0121 }
            if (r1 == 0) goto L_0x030f;
        L_0x02f7:
            r0 = new org.jivesoftware.smack.XMPPException$XMPPErrorException;	 Catch:{ Exception -> 0x0121 }
            r1 = "Stream Management 'enabled' element with resume attribute but without session id received";
            r2 = new org.jivesoftware.smack.packet.XMPPError;	 Catch:{ Exception -> 0x0121 }
            r3 = org.jivesoftware.smack.packet.XMPPError.Condition.bad_request;	 Catch:{ Exception -> 0x0121 }
            r2.<init>(r3);	 Catch:{ Exception -> 0x0121 }
            r0.<init>(r1, r2);	 Catch:{ Exception -> 0x0121 }
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r1 = r1.smEnabledSyncPoint;	 Catch:{ Exception -> 0x0121 }
            r1.reportFailure(r0);	 Catch:{ Exception -> 0x0121 }
            throw r0;	 Catch:{ Exception -> 0x0121 }
        L_0x030f:
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.getMaxResumptionTime();	 Catch:{ Exception -> 0x0121 }
            r1.smServerMaxResumptimTime = r0;	 Catch:{ Exception -> 0x0121 }
        L_0x0318:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.smEnabledSyncPoint;	 Catch:{ Exception -> 0x0121 }
            r0.reportSuccess();	 Catch:{ Exception -> 0x0121 }
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.LOGGER;	 Catch:{ Exception -> 0x0121 }
            r1 = "Stream Management (XEP-198): succesfully enabled";
            r0.fine(r1);	 Catch:{ Exception -> 0x0121 }
            goto L_0x003c;
        L_0x032c:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r1 = 0;
            r0.smSessionId = r1;	 Catch:{ Exception -> 0x0121 }
            goto L_0x0318;
        L_0x0333:
            r6 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            org.jivesoftware.smack.util.ParserUtils.assertAtStartTag(r6);	 Catch:{ Exception -> 0x0121 }
            r0 = "unknown";
        L_0x033a:
            r1 = r6.next();	 Catch:{ Exception -> 0x0121 }
            switch(r1) {
                case 2: goto L_0x0342;
                case 3: goto L_0x0354;
                default: goto L_0x0341;
            };	 Catch:{ Exception -> 0x0121 }
        L_0x0341:
            goto L_0x033a;
        L_0x0342:
            r1 = r6.getName();	 Catch:{ Exception -> 0x0121 }
            r7 = r6.getNamespace();	 Catch:{ Exception -> 0x0121 }
            r8 = "urn:ietf:params:xml:ns:xmpp-stanzas";
            r7 = r8.equals(r7);	 Catch:{ Exception -> 0x0121 }
            if (r7 == 0) goto L_0x033a;
        L_0x0352:
            r0 = r1;
            goto L_0x033a;
        L_0x0354:
            r1 = r6.getName();	 Catch:{ Exception -> 0x0121 }
            r7 = "failed";
            r1 = r7.equals(r1);	 Catch:{ Exception -> 0x0121 }
            if (r1 == 0) goto L_0x033a;
        L_0x0360:
            org.jivesoftware.smack.util.ParserUtils.assertAtEndTag(r6);	 Catch:{ Exception -> 0x0121 }
            r1 = new org.jivesoftware.smack.packet.XMPPError;	 Catch:{ Exception -> 0x0121 }
            r1.<init>(r0);	 Catch:{ Exception -> 0x0121 }
            r0 = new org.jivesoftware.smack.tcp.sm.packet.StreamManagement$Failed;	 Catch:{ Exception -> 0x0121 }
            r0.<init>(r1);	 Catch:{ Exception -> 0x0121 }
            r0 = r0.error;	 Catch:{ Exception -> 0x0121 }
            r1 = new org.jivesoftware.smack.XMPPException$XMPPErrorException;	 Catch:{ Exception -> 0x0121 }
            r6 = "Stream Management failed";
            r1.<init>(r6, r0);	 Catch:{ Exception -> 0x0121 }
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.smResumedSyncPoint;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.requestSent();	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x038d;
        L_0x0382:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.smResumedSyncPoint;	 Catch:{ Exception -> 0x0121 }
            r0.reportFailure(r1);	 Catch:{ Exception -> 0x0121 }
            goto L_0x003c;
        L_0x038d:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.smEnabledSyncPoint;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.requestSent();	 Catch:{ Exception -> 0x0121 }
            if (r0 != 0) goto L_0x03a1;
        L_0x0399:
            r0 = new java.lang.IllegalStateException;	 Catch:{ Exception -> 0x0121 }
            r1 = "Failed element received but SM was not previously enabled";
            r0.<init>(r1);	 Catch:{ Exception -> 0x0121 }
            throw r0;	 Catch:{ Exception -> 0x0121 }
        L_0x03a1:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.smEnabledSyncPoint;	 Catch:{ Exception -> 0x0121 }
            r0.reportFailure(r1);	 Catch:{ Exception -> 0x0121 }
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.lastFeaturesReceived;	 Catch:{ Exception -> 0x0121 }
            r0.reportSuccess();	 Catch:{ Exception -> 0x0121 }
            goto L_0x003c;
        L_0x03b5:
            r0 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            org.jivesoftware.smack.util.ParserUtils.assertAtStartTag(r0);	 Catch:{ Exception -> 0x0121 }
            r1 = "h";
            r1 = org.jivesoftware.smack.util.ParserUtils.getLongAttribute(r0, r1);	 Catch:{ Exception -> 0x0121 }
            r6 = r1.longValue();	 Catch:{ Exception -> 0x0121 }
            r1 = "";
            r8 = "previd";
            r1 = r0.getAttributeValue(r1, r8);	 Catch:{ Exception -> 0x0121 }
            r0.next();	 Catch:{ Exception -> 0x0121 }
            org.jivesoftware.smack.util.ParserUtils.assertAtEndTag(r0);	 Catch:{ Exception -> 0x0121 }
            r0 = new org.jivesoftware.smack.tcp.sm.packet.StreamManagement$Resumed;	 Catch:{ Exception -> 0x0121 }
            r0.<init>(r6, r1);	 Catch:{ Exception -> 0x0121 }
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r1 = r1.smSessionId;	 Catch:{ Exception -> 0x0121 }
            r6 = r0.getPrevId();	 Catch:{ Exception -> 0x0121 }
            r1 = r1.equals(r6);	 Catch:{ Exception -> 0x0121 }
            if (r1 != 0) goto L_0x03f7;
        L_0x03e7:
            r1 = new org.jivesoftware.smack.tcp.sm.StreamManagementException$StreamIdDoesNotMatchException;	 Catch:{ Exception -> 0x0121 }
            r2 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r2 = r2.smSessionId;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.getPrevId();	 Catch:{ Exception -> 0x0121 }
            r1.<init>(r2, r0);	 Catch:{ Exception -> 0x0121 }
            throw r1;	 Catch:{ Exception -> 0x0121 }
        L_0x03f7:
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r6 = r0.getHandledCount();	 Catch:{ Exception -> 0x0121 }
            org.jivesoftware.smack.tcp.XMPPTCPConnection.access$2300(r1, r6);	 Catch:{ Exception -> 0x0121 }
            r0 = new java.util.LinkedList;	 Catch:{ Exception -> 0x0121 }
            r0.<init>();	 Catch:{ Exception -> 0x0121 }
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r1 = r1.unacknowledgedStanzas;	 Catch:{ Exception -> 0x0121 }
            r0.addAll(r1);	 Catch:{ Exception -> 0x0121 }
            r1 = r0.iterator();	 Catch:{ Exception -> 0x0121 }
        L_0x0412:
            r0 = r1.hasNext();	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0426;
        L_0x0418:
            r0 = r1.next();	 Catch:{ Exception -> 0x0121 }
            r0 = (org.jivesoftware.smack.packet.Packet) r0;	 Catch:{ Exception -> 0x0121 }
            r6 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r6 = r6.packetWriter;	 Catch:{ Exception -> 0x0121 }
            r6.sendStreamElement(r0);	 Catch:{ Exception -> 0x0121 }
            goto L_0x0412;
        L_0x0426:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.smResumedSyncPoint;	 Catch:{ Exception -> 0x0121 }
            r0.reportSuccess();	 Catch:{ Exception -> 0x0121 }
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.smEnabledSyncPoint;	 Catch:{ Exception -> 0x0121 }
            r0.reportSuccess();	 Catch:{ Exception -> 0x0121 }
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.LOGGER;	 Catch:{ Exception -> 0x0121 }
            r1 = "Stream Management (XEP-198): Stream resumed";
            r0.fine(r1);	 Catch:{ Exception -> 0x0121 }
            goto L_0x003c;
        L_0x0443:
            r0 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            org.jivesoftware.smack.util.ParserUtils.assertAtStartTag(r0);	 Catch:{ Exception -> 0x0121 }
            r1 = "h";
            r1 = org.jivesoftware.smack.util.ParserUtils.getLongAttribute(r0, r1);	 Catch:{ Exception -> 0x0121 }
            r6 = r1.longValue();	 Catch:{ Exception -> 0x0121 }
            r0.next();	 Catch:{ Exception -> 0x0121 }
            org.jivesoftware.smack.util.ParserUtils.assertAtEndTag(r0);	 Catch:{ Exception -> 0x0121 }
            r0 = new org.jivesoftware.smack.tcp.sm.packet.StreamManagement$AckAnswer;	 Catch:{ Exception -> 0x0121 }
            r0.<init>(r6);	 Catch:{ Exception -> 0x0121 }
            r1 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r6 = r0.handledCount;	 Catch:{ Exception -> 0x0121 }
            org.jivesoftware.smack.tcp.XMPPTCPConnection.access$2300(r1, r6);	 Catch:{ Exception -> 0x0121 }
            goto L_0x003c;
        L_0x0466:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.smEnabledSyncPoint;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.wasSuccessful();	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x0486;
        L_0x0472:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.packetWriter;	 Catch:{ Exception -> 0x0121 }
            r1 = new org.jivesoftware.smack.tcp.sm.packet.StreamManagement$AckAnswer;	 Catch:{ Exception -> 0x0121 }
            r6 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r6 = r6.clientHandledStanzasCount;	 Catch:{ Exception -> 0x0121 }
            r1.<init>(r6);	 Catch:{ Exception -> 0x0121 }
            r0.sendStreamElement(r1);	 Catch:{ Exception -> 0x0121 }
            goto L_0x003c;
        L_0x0486:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.LOGGER;	 Catch:{ Exception -> 0x0121 }
            r1 = "SM Ack Request received while SM is not enabled";
            r0.warning(r1);	 Catch:{ Exception -> 0x0121 }
            goto L_0x003c;
        L_0x0491:
            r0 = 3;
            if (r1 != r0) goto L_0x003c;
        L_0x0494:
            r0 = r9.parser;	 Catch:{ Exception -> 0x0121 }
            r0 = r0.getName();	 Catch:{ Exception -> 0x0121 }
            r1 = "stream";
            r0 = r0.equals(r1);	 Catch:{ Exception -> 0x0121 }
            if (r0 == 0) goto L_0x003c;
        L_0x04a2:
            r0 = org.jivesoftware.smack.tcp.XMPPTCPConnection.this;	 Catch:{ Exception -> 0x0121 }
            r0.disconnect();	 Catch:{ Exception -> 0x0121 }
            goto L_0x003c;
        L_0x04a9:
            r1 = r0;
            goto L_0x0013;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.tcp.XMPPTCPConnection.PacketReader.access$200(org.jivesoftware.smack.tcp.XMPPTCPConnection$PacketReader):void");
        }
    }

    protected class PacketWriter {
        private volatile boolean instantShutdown;
        private final ArrayBlockingQueueWithShutdown<Element> queue;
        protected SynchronizationPoint<NoResponseException> shutdownDone;
        protected volatile Long shutdownTimestamp;
        private Thread writerThread;

        /* renamed from: org.jivesoftware.smack.tcp.XMPPTCPConnection.PacketWriter.1 */
        class C12901 extends Thread {
            C12901() {
            }

            public final void run() {
                PacketWriter.access$2600(PacketWriter.this);
            }
        }

        protected PacketWriter() {
            this.queue = new ArrayBlockingQueueWithShutdown();
            this.shutdownDone = new SynchronizationPoint(XMPPTCPConnection.this);
            this.shutdownTimestamp = null;
        }

        static /* synthetic */ void access$2600(PacketWriter x0) {
            try {
                XMPPTCPConnection.this.openStream();
                XMPPTCPConnection.this.initalOpenStreamSend.reportSuccess();
                while (!x0.done()) {
                    Element nextStreamElement = x0.nextStreamElement();
                    if (nextStreamElement != null) {
                        if (XMPPTCPConnection.this.smEnabledSyncPoint.wasSuccessful() && (nextStreamElement instanceof Packet)) {
                            if (((double) XMPPTCPConnection.this.unacknowledgedStanzas.size()) == 400.0d) {
                                XMPPTCPConnection.this.writer.write(AckRequest.INSTANCE.toXML().toString());
                                XMPPTCPConnection.this.writer.flush();
                            }
                            XMPPTCPConnection.this.unacknowledgedStanzas.put((Packet) nextStreamElement);
                        }
                        XMPPTCPConnection.this.writer.write(nextStreamElement.toXML().toString());
                        if (x0.queue.isEmpty()) {
                            XMPPTCPConnection.this.writer.flush();
                        }
                    }
                }
                if (!x0.instantShutdown) {
                    while (!x0.queue.isEmpty()) {
                        try {
                            XMPPTCPConnection.this.writer.write(((Element) x0.queue.remove()).toXML().toString());
                        } catch (Throwable e) {
                            XMPPTCPConnection.LOGGER.log(Level.WARNING, "Exception flushing queue during shutdown, ignore and continue", e);
                        }
                    }
                    XMPPTCPConnection.this.writer.flush();
                    try {
                        XMPPTCPConnection.this.writer.write("</stream:stream>");
                        XMPPTCPConnection.this.writer.flush();
                    } catch (Throwable e2) {
                        XMPPTCPConnection.LOGGER.log(Level.WARNING, "Exception writing closing stream element", e2);
                    }
                    x0.queue.clear();
                } else if (x0.instantShutdown && XMPPTCPConnection.this.smEnabledSyncPoint.wasSuccessful()) {
                    x0.drainWriterQueueToUnacknowledgedStanzas();
                }
                try {
                    XMPPTCPConnection.this.writer.close();
                } catch (Exception e3) {
                }
                x0.shutdownDone.reportSuccess();
            } catch (Throwable e22) {
                throw new IllegalStateException(e22);
            } catch (Throwable e222) {
                if (x0.done() || XMPPTCPConnection.this.socketClosed) {
                    XMPPTCPConnection.LOGGER.log(Level.FINE, "Ignoring Exception in writePackets()", e222);
                } else {
                    XMPPTCPConnection.this.notifyConnectionError(e222);
                }
                x0.shutdownDone.reportSuccess();
            } catch (Throwable th) {
                x0.shutdownDone.reportSuccess();
            }
        }

        final void init() {
            this.shutdownDone.init();
            this.shutdownTimestamp = null;
            if (XMPPTCPConnection.this.unacknowledgedStanzas != null) {
                drainWriterQueueToUnacknowledgedStanzas();
            }
            ArrayBlockingQueueWithShutdown arrayBlockingQueueWithShutdown = this.queue;
            arrayBlockingQueueWithShutdown.lock.lock();
            try {
                arrayBlockingQueueWithShutdown.isShutdown = false;
                this.writerThread = new C12901();
                this.writerThread.setName("Smack Packet Writer (" + XMPPTCPConnection.this.getConnectionCounter() + ")");
                this.writerThread.setDaemon(true);
                this.writerThread.start();
            } finally {
                arrayBlockingQueueWithShutdown.lock.unlock();
            }
        }

        final boolean done() {
            return this.shutdownTimestamp != null;
        }

        protected final void sendStreamElement(Element element) throws NotConnectedException {
            if (!done() || XMPPTCPConnection.this.isSmResumptionPossible()) {
                try {
                    this.queue.put(element);
                    return;
                } catch (InterruptedException e) {
                    throw new NotConnectedException();
                }
            }
            throw new NotConnectedException();
        }

        final void shutdown(boolean instant) {
            this.instantShutdown = instant;
            this.shutdownTimestamp = Long.valueOf(System.currentTimeMillis());
            ArrayBlockingQueueWithShutdown arrayBlockingQueueWithShutdown = this.queue;
            arrayBlockingQueueWithShutdown.lock.lock();
            try {
                arrayBlockingQueueWithShutdown.isShutdown = true;
                arrayBlockingQueueWithShutdown.notEmpty.signalAll();
                arrayBlockingQueueWithShutdown.notFull.signalAll();
                try {
                    this.shutdownDone.checkIfSuccessOrWait();
                } catch (NoResponseException e) {
                    XMPPTCPConnection.LOGGER.log(Level.WARNING, "NoResponseException", e);
                }
            } finally {
                arrayBlockingQueueWithShutdown.lock.unlock();
            }
        }

        private Element nextStreamElement() {
            if (done()) {
                return null;
            }
            Element packet = null;
            try {
                return (Element) this.queue.take();
            } catch (InterruptedException e) {
                return packet;
            }
        }

        private void drainWriterQueueToUnacknowledgedStanzas() {
            List<Element> elements = new ArrayList(this.queue.size());
            this.queue.drainTo(elements);
            for (Element element : elements) {
                if (element instanceof Packet) {
                    XMPPTCPConnection.this.unacknowledgedStanzas.add((Packet) element);
                }
            }
        }
    }

    static {
        $assertionsDisabled = !XMPPTCPConnection.class.desiredAssertionStatus();
        LOGGER = Logger.getLogger(XMPPTCPConnection.class.getName());
        useSmDefault = false;
        useSmResumptionDefault = true;
    }

    static /* synthetic */ void access$1000(XMPPTCPConnection x0) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, NoSuchProviderException, UnrecoverableKeyException, KeyManagementException, SmackException {
        KeyManager[] keyManagerArr;
        SSLContext instance;
        Collection hashSet;
        SSLContext sSLContext = x0.config.customSSLContext;
        if (x0.config.callbackHandler == null || sSLContext != null) {
            keyManagerArr = null;
        } else {
            PasswordCallback passwordCallback;
            KeyStore keyStore;
            if (x0.config.keystoreType.equals("NONE")) {
                passwordCallback = null;
                keyStore = null;
            } else if (x0.config.keystoreType.equals("PKCS11")) {
                try {
                    Constructor constructor = Class.forName("sun.security.pkcs11.SunPKCS11").getConstructor(new Class[]{InputStream.class});
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(("name = SmartCard\nlibrary = " + x0.config.pkcs11Library).getBytes());
                    Provider provider = (Provider) constructor.newInstance(new Object[]{byteArrayInputStream});
                    Security.addProvider(provider);
                    keyStore = KeyStore.getInstance("PKCS11", provider);
                    passwordCallback = new PasswordCallback("PKCS11 Password: ", false);
                    x0.config.callbackHandler.handle(new Callback[]{passwordCallback});
                    keyStore.load(null, passwordCallback.getPassword());
                } catch (Exception e) {
                    passwordCallback = null;
                    keyStore = null;
                }
            } else if (x0.config.keystoreType.equals("Apple")) {
                KeyStore instance2 = KeyStore.getInstance("KeychainStore", "Apple");
                instance2.load(null, null);
                keyStore = instance2;
                passwordCallback = null;
            } else {
                keyStore = KeyStore.getInstance(x0.config.keystoreType);
                try {
                    passwordCallback = new PasswordCallback("Keystore Password: ", false);
                    x0.config.callbackHandler.handle(new Callback[]{passwordCallback});
                    keyStore.load(new FileInputStream(x0.config.keystorePath), passwordCallback.getPassword());
                } catch (Exception e2) {
                    passwordCallback = null;
                    keyStore = null;
                }
            }
            KeyManagerFactory instance3 = KeyManagerFactory.getInstance("SunX509");
            if (passwordCallback == null) {
                try {
                    instance3.init(keyStore, null);
                } catch (NullPointerException e3) {
                    keyManagerArr = null;
                }
            } else {
                instance3.init(keyStore, passwordCallback.getPassword());
                passwordCallback.clearPassword();
            }
            keyManagerArr = instance3.getKeyManagers();
        }
        if (sSLContext == null) {
            instance = SSLContext.getInstance(SSLSocketFactoryFactory.DEFAULT_PROTOCOL);
            instance.init(keyManagerArr, null, new SecureRandom());
        } else {
            instance = sSLContext;
        }
        Socket socket = x0.socket;
        x0.socket = instance.getSocketFactory().createSocket(socket, socket.getInetAddress().getHostAddress(), socket.getPort(), true);
        x0.initReaderAndWriter();
        SSLSocket sSLSocket = (SSLSocket) x0.socket;
        String[] strArr = x0.config.enabledSSLProtocols;
        String[] strArr2 = x0.config.enabledSSLCiphers;
        if (strArr != null) {
            Collection hashSet2 = new HashSet(Arrays.asList(strArr));
            hashSet = new HashSet(Arrays.asList(sSLSocket.getSupportedProtocols()));
            Set hashSet3 = new HashSet(hashSet);
            hashSet3.retainAll(hashSet2);
            if (hashSet3.isEmpty()) {
                throw new SecurityNotPossibleException("Request to enable SSL/TLS protocols '" + StringUtils.collectionToString(hashSet2) + "', but only '" + StringUtils.collectionToString(hashSet) + "' are supported.");
            }
            sSLSocket.setEnabledProtocols((String[]) hashSet3.toArray(new String[hashSet3.size()]));
        }
        if (strArr2 != null) {
            hashSet = new HashSet(Arrays.asList(strArr2));
            Collection hashSet4 = new HashSet(Arrays.asList(sSLSocket.getEnabledCipherSuites()));
            Set hashSet5 = new HashSet(hashSet4);
            hashSet5.retainAll(hashSet);
            if (hashSet5.isEmpty()) {
                throw new SecurityNotPossibleException("Request to enable SSL/TLS ciphers '" + StringUtils.collectionToString(hashSet) + "', but only '" + StringUtils.collectionToString(hashSet4) + "' are supported.");
            }
            sSLSocket.setEnabledCipherSuites((String[]) hashSet5.toArray(new String[hashSet5.size()]));
        }
        sSLSocket.startHandshake();
        ConnectionConfiguration connectionConfiguration = x0.config;
        HostnameVerifier defaultHostnameVerifier = connectionConfiguration.hostnameVerifier != null ? connectionConfiguration.hostnameVerifier : SmackConfiguration.getDefaultHostnameVerifier();
        if (defaultHostnameVerifier == null) {
            throw new IllegalStateException("No HostnameVerifier set. Use connectionConfiguration.setHostnameVerifier() to configure.");
        } else if (defaultHostnameVerifier.verify(x0.config.serviceName, sSLSocket.getSession())) {
            x0.usingTLS = true;
        } else {
            throw new CertificateException("Hostname verification of certificate failed. Certificate does not authenticate " + x0.config.serviceName);
        }
    }

    static /* synthetic */ void access$2300(XMPPTCPConnection x0, long x1) throws NotConnectedException {
        long calculateDelta = SMUtils.calculateDelta(x1, x0.serverHandledStanzasCount);
        List<Packet> arrayList = new ArrayList(x1 <= 2147483647L ? (int) x1 : ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        long j = 0;
        while (j < calculateDelta) {
            Packet packet = (Packet) x0.unacknowledgedStanzas.poll();
            if ($assertionsDisabled || packet != null) {
                arrayList.add(packet);
                j = 1 + j;
            } else {
                throw new AssertionError();
            }
        }
        for (Packet packet2 : arrayList) {
            for (PacketListener processPacket : x0.stanzaAcknowledgedListeners) {
                PacketListener processPacket2;
                processPacket2.processPacket(packet2);
            }
            String str = packet2.packetID;
            if (str != null) {
                processPacket2 = (PacketListener) x0.idStanzaAcknowledgedListeners.remove(str);
                if (processPacket2 != null) {
                    processPacket2.processPacket(packet2);
                }
            }
        }
        x0.serverHandledStanzasCount = x1;
    }

    public XMPPTCPConnection(ConnectionConfiguration config) {
        super(config);
        this.connectionID = null;
        this.connected = false;
        this.disconnectedButResumeable = false;
        this.socketClosed = false;
        this.usingTLS = false;
        this.parsingExceptionCallback = SmackConfiguration.getDefaultParsingExceptionCallback();
        this.initalOpenStreamSend = new SynchronizationPoint(this);
        this.maybeCompressFeaturesReceived = new SynchronizationPoint(this);
        this.compressSyncPoint = new SynchronizationPoint(this);
        this.smResumedSyncPoint = new SynchronizationPoint(this);
        this.smEnabledSyncPoint = new SynchronizationPoint(this);
        this.smClientMaxResumptionTime = -1;
        this.smServerMaxResumptimTime = -1;
        this.useSm = useSmDefault;
        this.useSmResumption = useSmResumptionDefault;
        this.serverHandledStanzasCount = 0;
        this.clientHandledStanzasCount = 0;
        this.stanzaAcknowledgedListeners = new ConcurrentLinkedQueue();
        this.idStanzaAcknowledgedListeners = new ConcurrentHashMap();
        this.requestAckPredicates = new LinkedHashSet();
    }

    public final String getUser() {
        if (this.authenticated) {
            return this.user;
        }
        return null;
    }

    public final synchronized void login(String username, String password, String resource) throws XMPPException, SmackException, IOException {
        Object obj = 1;
        synchronized (this) {
            if (!this.connected) {
                throw new NotConnectedException();
            } else if (!this.authenticated || this.disconnectedButResumeable) {
                if (username != null) {
                    username = username.toLowerCase(Locale.US).trim();
                }
                SASLAuthentication sASLAuthentication = this.saslAuthentication;
                if (sASLAuthentication.serverMechanisms().isEmpty() || (sASLAuthentication.serverMechanisms().size() == 1 && sASLAuthentication.hasAnonymousAuthentication())) {
                    obj = null;
                }
                if (obj != null) {
                    SASLAuthentication sASLAuthentication2;
                    SASLMechanism selectMechanism;
                    if (password != null) {
                        sASLAuthentication2 = this.saslAuthentication;
                        selectMechanism = sASLAuthentication2.selectMechanism();
                        if (selectMechanism != null) {
                            sASLAuthentication2.currentMechanism = selectMechanism;
                            synchronized (sASLAuthentication2) {
                                sASLAuthentication2.currentMechanism.authenticate(username, sASLAuthentication2.connection.host, sASLAuthentication2.connection.config.serviceName, password);
                                try {
                                    sASLAuthentication2.wait(sASLAuthentication2.connection.packetReplyTimeout);
                                } catch (InterruptedException e) {
                                }
                            }
                            sASLAuthentication2.maybeThrowException();
                            if (!sASLAuthentication2.authenticationSuccessful) {
                                throw new NoResponseException();
                            }
                        }
                        throw new SmackException("SASL Authentication failed. No known authentication mechanisims.");
                    }
                    sASLAuthentication2 = this.saslAuthentication;
                    selectMechanism = sASLAuthentication2.selectMechanism();
                    if (selectMechanism != null) {
                        sASLAuthentication2.currentMechanism = selectMechanism;
                        synchronized (sASLAuthentication2) {
                            sASLAuthentication2.currentMechanism.authenticate$79f1ca4c(sASLAuthentication2.connection.host, sASLAuthentication2.connection.config.serviceName);
                            try {
                                sASLAuthentication2.wait(sASLAuthentication2.connection.packetReplyTimeout);
                            } catch (InterruptedException e2) {
                            }
                        }
                        sASLAuthentication2.maybeThrowException();
                        if (!sASLAuthentication2.authenticationSuccessful) {
                            throw new NoResponseException();
                        }
                    }
                    throw new SmackException("SASL Authentication failed. No known authentication mechanisims.");
                    if (this.config.compressionEnabled) {
                        useCompression();
                    }
                    if (isSmResumptionPossible()) {
                        this.smResumedSyncPoint.sendAndWaitForResponse(new Resume(this.clientHandledStanzasCount, this.smSessionId));
                        if (this.smResumedSyncPoint.wasSuccessful()) {
                            afterSuccessfulLogin(false, true);
                        } else {
                            LOGGER.fine("Stream resumption failed, continuing with normal stream establishment process");
                        }
                    }
                    bindResourceAndEstablishSession(resource);
                    List<Packet> previouslyUnackedStanzas = new LinkedList();
                    if (this.unacknowledgedStanzas != null) {
                        this.unacknowledgedStanzas.drainTo(previouslyUnackedStanzas);
                    }
                    if (hasFeature("sm", "urn:xmpp:sm:3") && this.useSm) {
                        this.unacknowledgedStanzas = new ArrayBlockingQueue(500);
                        this.clientHandledStanzasCount = 0;
                        this.serverHandledStanzasCount = 0;
                        this.smEnabledSyncPoint.sendAndWaitForResponseOrThrow(new Enable(this.useSmResumption, this.smClientMaxResumptionTime));
                        synchronized (this.requestAckPredicates) {
                            if (this.requestAckPredicates.isEmpty()) {
                                this.requestAckPredicates.add(new ForMatchingPredicateOrAfterXStanzas(ForEveryMessage.INSTANCE));
                            }
                        }
                    }
                    for (Packet stanza : previouslyUnackedStanzas) {
                        sendPacketInternal(stanza);
                    }
                    ConnectionConfiguration connectionConfiguration = this.config;
                    connectionConfiguration.username = username;
                    connectionConfiguration.password = password;
                    connectionConfiguration.resource = resource;
                    afterSuccessfulLogin(false, false);
                } else {
                    throw new SmackException("No non-anonymous SASL authentication mechanism available");
                }
            } else {
                throw new AlreadyLoggedInException();
            }
        }
    }

    private synchronized void loginAnonymously() throws XMPPException, SmackException, IOException {
        if (!this.connected) {
            throw new NotConnectedException();
        } else if (this.authenticated) {
            throw new AlreadyLoggedInException();
        } else {
            this.saslFeatureReceived.checkIfSuccessOrWaitOrThrow();
            if (this.saslAuthentication.hasAnonymousAuthentication()) {
                SASLAuthentication sASLAuthentication = this.saslAuthentication;
                sASLAuthentication.currentMechanism = new SASLAnonymous().instanceForAuthentication(sASLAuthentication.connection);
                synchronized (sASLAuthentication) {
                    sASLAuthentication.currentMechanism.authenticate(null, null, null, BuildConfig.VERSION_NAME);
                    try {
                        sASLAuthentication.wait(sASLAuthentication.connection.packetReplyTimeout);
                    } catch (InterruptedException e) {
                    }
                }
                sASLAuthentication.maybeThrowException();
                if (sASLAuthentication.authenticationSuccessful) {
                    if (this.config.compressionEnabled) {
                        useCompression();
                    }
                    bindResourceAndEstablishSession(null);
                    afterSuccessfulLogin(true, false);
                } else {
                    throw new NoResponseException();
                }
            }
            throw new SmackException("No anonymous SASL authentication mechanism available");
        }
    }

    public final boolean isConnected() {
        return this.connected;
    }

    public final boolean isAuthenticated() {
        return this.authenticated;
    }

    protected final void shutdown() {
        shutdown(false);
    }

    public final void instantShutdown() {
        shutdown(true);
    }

    private void shutdown(boolean instant) {
        if (this.packetReader != null) {
            this.packetReader.done = true;
        }
        if (this.packetWriter != null) {
            this.packetWriter.shutdown(instant);
        }
        this.socketClosed = true;
        try {
            this.socket.close();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "shutdown", e);
        }
        boolean z = this.authenticated;
        if (!this.wasAuthenticated) {
            this.wasAuthenticated = z;
        }
        if (isSmResumptionPossible() && instant) {
            this.disconnectedButResumeable = true;
        } else {
            this.authenticated = false;
            this.connected = false;
            this.usingTLS = false;
            this.disconnectedButResumeable = false;
        }
        this.reader = null;
        this.writer = null;
        this.maybeCompressFeaturesReceived.init();
        this.compressSyncPoint.init();
        this.smResumedSyncPoint.init();
        this.smEnabledSyncPoint.init();
        this.initalOpenStreamSend.init();
    }

    public final void send(PlainStreamElement element) throws NotConnectedException {
        this.packetWriter.sendStreamElement(element);
    }

    protected final void sendPacketInternal(Packet packet) throws NotConnectedException {
        this.packetWriter.sendStreamElement(packet);
        if (this.smEnabledSyncPoint.wasSuccessful()) {
            for (PacketFilter accept : this.requestAckPredicates) {
                if (accept.accept(packet)) {
                    requestSmAcknowledgementInternal();
                    return;
                }
            }
        }
    }

    private void initConnection() throws SmackException, IOException {
        boolean isFirstInitialization = false;
        if (this.packetReader == null || this.packetWriter == null) {
            isFirstInitialization = true;
        }
        this.compressionHandler = null;
        initReaderAndWriter();
        if (isFirstInitialization) {
            try {
                this.packetWriter = new PacketWriter();
                this.packetReader = new PacketReader();
                if (this.config.debuggerEnabled) {
                    addPacketListener(this.debugger.getReaderListener(), null);
                    if (this.debugger.getWriterListener() != null) {
                        addPacketSendingListener(this.debugger.getWriterListener(), null);
                    }
                }
            } catch (SmackException ex) {
                shutdown(true);
                throw ex;
            }
        }
        this.packetWriter.init();
        PacketReader packetReader = this.packetReader;
        packetReader.done = false;
        packetReader.readerThread = new C12891();
        packetReader.readerThread.setName("Smack Packet Reader (" + packetReader.this$0.getConnectionCounter() + ")");
        packetReader.readerThread.setDaemon(true);
        packetReader.readerThread.start();
        if (isFirstInitialization) {
            for (ConnectionCreationListener connectionCreated : AbstractXMPPConnection.getConnectionCreationListeners()) {
                connectionCreated.connectionCreated(this);
            }
        }
    }

    private void initReaderAndWriter() throws IOException, SmackException {
        try {
            InputStream is = this.socket.getInputStream();
            OutputStream os = this.socket.getOutputStream();
            if (this.compressionHandler != null) {
                is = this.compressionHandler.getInputStream(is);
                os = this.compressionHandler.getOutputStream(os);
            }
            this.writer = new OutputStreamWriter(os, "UTF-8");
            this.reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            if (this.reader == null || this.writer == null) {
                throw new NullPointerException("Reader or writer isn't initialized.");
            } else if (this.config.debuggerEnabled) {
                if (this.debugger == null) {
                    this.debugger = SmackConfiguration.createDebugger(this, this.writer, this.reader);
                }
                if (this.debugger == null) {
                    AbstractXMPPConnection.LOGGER.severe("Debugging enabled but could not find debugger class");
                    return;
                }
                this.reader = this.debugger.newConnectionReader$32745501();
                this.writer = this.debugger.newConnectionWriter$1bc0ff();
            }
        } catch (IOException e) {
            throw e;
        } catch (Throwable e2) {
            throw new SmackException(e2);
        }
    }

    private void useCompression() throws NotConnectedException, NoResponseException, XMPPException {
        this.maybeCompressFeaturesReceived.checkIfSuccessOrWait();
        Feature feature = (Feature) getFeature("compression", "http://jabber.org/protocol/compress");
        if (feature != null) {
            for (XMPPInputOutputStream xMPPInputOutputStream : SmackConfiguration.getCompresionHandlers()) {
                if (Collections.unmodifiableList(feature.methods).contains(xMPPInputOutputStream.getCompressionMethod())) {
                    break;
                }
            }
        }
        XMPPInputOutputStream xMPPInputOutputStream2 = null;
        this.compressionHandler = xMPPInputOutputStream2;
        if (xMPPInputOutputStream2 != null) {
            this.compressSyncPoint.sendAndWaitForResponseOrThrow(new Compress(this.compressionHandler.getCompressionMethod()));
        } else {
            LOGGER.warning("Could not enable compression because no matching handler/method pair was found");
        }
    }

    protected final void connectInternal() throws SmackException, IOException, XMPPException {
        if (!this.connected || this.disconnectedButResumeable) {
            ConnectionConfiguration connectionConfiguration = this.config;
            try {
                this.config.maybeResolveDns();
                Iterator it = connectionConfiguration.getHostAddresses().iterator();
                List linkedList = new LinkedList();
                while (it.hasNext()) {
                    Exception exception = null;
                    HostAddress hostAddress = (HostAddress) it.next();
                    String str = hostAddress.fqdn;
                    int i = hostAddress.port;
                    try {
                        if (connectionConfiguration.socketFactory == null) {
                            this.socket = new Socket(str, i);
                        } else {
                            this.socket = connectionConfiguration.socketFactory.createSocket(str, i);
                        }
                    } catch (Exception e) {
                        exception = e;
                    }
                    if (exception == null) {
                        this.host = str;
                        this.port = i;
                        break;
                    }
                    hostAddress.exception = exception;
                    linkedList.add(hostAddress);
                    if (!it.hasNext()) {
                        throw ConnectionException.from(linkedList);
                    }
                }
                this.socketClosed = false;
                initConnection();
                this.saslFeatureReceived.checkIfSuccessOrWaitOrThrow();
                this.connected = true;
                callConnectionConnectedListener();
                if (this.wasAuthenticated) {
                    if (this.anonymous) {
                        loginAnonymously();
                    } else {
                        login(this.config.username, this.config.password, this.config.resource);
                    }
                    notifyReconnection();
                    return;
                }
                return;
            } catch (Throwable e2) {
                throw new SmackException(e2);
            }
        }
        throw new AlreadyConnectedException();
    }

    private synchronized void notifyConnectionError(Exception e) {
        if (!((this.packetReader == null || this.packetReader.done) && (this.packetWriter == null || this.packetWriter.done()))) {
            shutdown(true);
            callConnectionClosedOnErrorListener(e);
        }
    }

    private void notifyReconnection() {
        for (ConnectionListener listener : getConnectionListeners()) {
            try {
                listener.reconnectionSuccessful();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "notifyReconnection()", e);
            }
        }
    }

    protected final void afterFeaturesReceived() throws SecurityRequiredException, NotConnectedException {
        StartTls startTlsFeature = (StartTls) getFeature("starttls", "urn:ietf:params:xml:ns:xmpp-tls");
        if (startTlsFeature != null) {
            if (startTlsFeature.required && this.config.securityMode$21bc7c29 == SecurityMode.disabled$21bc7c29) {
                notifyConnectionError(new SecurityRequiredException("TLS required by server but not allowed by connection configuration"));
                return;
            } else if (this.config.securityMode$21bc7c29 != SecurityMode.disabled$21bc7c29) {
                send(new StartTls());
            } else {
                return;
            }
        }
        if (!this.usingTLS && startTlsFeature == null && this.config.securityMode$21bc7c29 == SecurityMode.required$21bc7c29) {
            throw new SecurityRequiredException();
        } else if (this.saslAuthentication.authenticationSuccessful) {
            this.maybeCompressFeaturesReceived.reportSuccess();
        }
    }

    final void openStream() throws SmackException {
        send(new StreamOpen(this.config.serviceName));
        try {
            this.packetReader.parser = PacketParserUtils.newXmppParser(this.reader);
        } catch (Throwable e) {
            throw new SmackException(e);
        }
    }

    public static void setUseStreamManagementDefault$1385ff() {
        useSmDefault = true;
    }

    public final void requestSmAcknowledgementInternal() throws NotConnectedException {
        this.packetWriter.sendStreamElement(AckRequest.INSTANCE);
    }

    public final boolean isSmResumptionPossible() {
        if (this.smSessionId == null) {
            return false;
        }
        Long shutdownTimestamp = this.packetWriter.shutdownTimestamp;
        if (shutdownTimestamp == null) {
            return true;
        }
        int clientResumptionTime;
        int serverResumptionTime;
        long current = System.currentTimeMillis();
        if (this.smClientMaxResumptionTime > 0) {
            clientResumptionTime = this.smClientMaxResumptionTime;
        } else {
            clientResumptionTime = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        }
        if (this.smServerMaxResumptimTime > 0) {
            serverResumptionTime = this.smServerMaxResumptimTime;
        } else {
            serverResumptionTime = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        }
        if (shutdownTimestamp.longValue() + ((long) (Math.max(clientResumptionTime, serverResumptionTime) * 1000)) > current) {
            return false;
        }
        return true;
    }
}
