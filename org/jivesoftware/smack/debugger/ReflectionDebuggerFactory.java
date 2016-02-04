package org.jivesoftware.smack.debugger;

import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.XMPPConnection;

public class ReflectionDebuggerFactory implements SmackDebuggerFactory {
    private static final String[] DEFAULT_DEBUGGERS;
    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(ReflectionDebuggerFactory.class.getName());
        DEFAULT_DEBUGGERS = new String[]{"org.jivesoftware.smackx.debugger.EnhancedDebugger", "org.jivesoftware.smackx.debugger.android.AndroidDebugger", "org.jivesoftware.smack.debugger.LiteDebugger", "org.jivesoftware.smack.debugger.ConsoleDebugger"};
    }

    private static Class<SmackDebugger> getDebuggerClass() {
        String customDebuggerClassName = getCustomDebuggerClassName();
        if (customDebuggerClassName == null) {
            return getOneOfDefaultDebuggerClasses();
        }
        try {
            return Class.forName(customDebuggerClassName);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unable to instantiate debugger class " + customDebuggerClassName, e);
            return null;
        }
    }

    public final SmackDebugger create(XMPPConnection connection, Writer writer, Reader reader) throws IllegalArgumentException {
        Class<SmackDebugger> debuggerClass = getDebuggerClass();
        if (debuggerClass == null) {
            return null;
        }
        try {
            return (SmackDebugger) debuggerClass.getConstructor(new Class[]{XMPPConnection.class, Writer.class, Reader.class}).newInstance(new Object[]{connection, writer, reader});
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't initialize the configured debugger!", e);
        }
    }

    private static String getCustomDebuggerClassName() {
        try {
            return System.getProperty("smack.debuggerClass");
        } catch (Throwable th) {
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Class<org.jivesoftware.smack.debugger.SmackDebugger> getOneOfDefaultDebuggerClasses() {
        /*
        r0 = DEFAULT_DEBUGGERS;
        r3 = r0.length;
        r2 = 0;
    L_0x0004:
        if (r2 >= r3) goto L_0x003d;
    L_0x0006:
        r1 = r0[r2];
        r4 = java.lang.Class.forName(r1);	 Catch:{ ClassNotFoundException -> 0x000d, ClassCastException -> 0x002b, Exception -> 0x0034 }
    L_0x000c:
        return r4;
    L_0x000d:
        r4 = move-exception;
        r4 = LOGGER;
        r5 = new java.lang.StringBuilder;
        r6 = "Did not find debugger class '";
        r5.<init>(r6);
        r5 = r5.append(r1);
        r6 = "'";
        r5 = r5.append(r6);
        r5 = r5.toString();
        r4.fine(r5);
    L_0x0028:
        r2 = r2 + 1;
        goto L_0x0004;
    L_0x002b:
        r4 = move-exception;
        r4 = LOGGER;
        r5 = "Found debugger class that does not appears to implement SmackDebugger interface";
        r4.warning(r5);
        goto L_0x0028;
    L_0x0034:
        r4 = move-exception;
        r4 = LOGGER;
        r5 = "Unable to instantiate either Smack debugger class";
        r4.warning(r5);
        goto L_0x0028;
    L_0x003d:
        r4 = 0;
        goto L_0x000c;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.debugger.ReflectionDebuggerFactory.getOneOfDefaultDebuggerClasses():java.lang.Class<org.jivesoftware.smack.debugger.SmackDebugger>");
    }
}
