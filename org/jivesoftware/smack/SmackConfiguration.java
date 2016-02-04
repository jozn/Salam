package org.jivesoftware.smack;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import org.jivesoftware.smack.compression.XMPPInputOutputStream;
import org.jivesoftware.smack.debugger.ReflectionDebuggerFactory;
import org.jivesoftware.smack.debugger.SmackDebugger;
import org.jivesoftware.smack.debugger.SmackDebuggerFactory;
import org.jivesoftware.smack.parsing.ExceptionThrowingCallback;
import org.jivesoftware.smack.parsing.ParsingExceptionCallback;

public final class SmackConfiguration {
    public static boolean DEBUG_ENABLED;
    static final List<XMPPInputOutputStream> compressionHandlers;
    private static SmackDebuggerFactory debuggerFactory;
    private static ParsingExceptionCallback defaultCallback;
    private static HostnameVerifier defaultHostnameVerififer;
    private static List<String> defaultMechs;
    private static int defaultPacketReplyTimeout;
    static Set<String> disabledSmackClasses;
    private static int packetCollectorSize;
    static boolean smackInitialized;

    static {
        defaultPacketReplyTimeout = 5000;
        packetCollectorSize = 5000;
        defaultMechs = new ArrayList();
        disabledSmackClasses = new HashSet();
        compressionHandlers = new ArrayList(2);
        smackInitialized = false;
        debuggerFactory = new ReflectionDebuggerFactory();
        DEBUG_ENABLED = false;
        defaultCallback = new ExceptionThrowingCallback();
    }

    public static String getVersion() {
        return SmackInitialization.SMACK_VERSION;
    }

    public static int getDefaultPacketReplyTimeout() {
        if (defaultPacketReplyTimeout <= 0) {
            defaultPacketReplyTimeout = 5000;
        }
        return defaultPacketReplyTimeout;
    }

    public static void setDefaultPacketReplyTimeout$13462e() {
        defaultPacketReplyTimeout = 30000;
    }

    public static int getPacketCollectorSize() {
        return packetCollectorSize;
    }

    public static SmackDebugger createDebugger(XMPPConnection connection, Writer writer, Reader reader) {
        SmackDebuggerFactory factory = debuggerFactory;
        if (factory == null) {
            return null;
        }
        return factory.create(connection, writer, reader);
    }

    public static ParsingExceptionCallback getDefaultParsingExceptionCallback() {
        return defaultCallback;
    }

    public static List<XMPPInputOutputStream> getCompresionHandlers() {
        List<XMPPInputOutputStream> res = new ArrayList(compressionHandlers.size());
        for (XMPPInputOutputStream ios : compressionHandlers) {
            if (ios.isSupported()) {
                res.add(ios);
            }
        }
        return res;
    }

    public static void setDefaultHostnameVerifier(HostnameVerifier verifier) {
        defaultHostnameVerififer = verifier;
    }

    public static HostnameVerifier getDefaultHostnameVerifier() {
        return defaultHostnameVerififer;
    }
}
