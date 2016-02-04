package org.eclipse.paho.client.mqttv3.util;

import java.util.Enumeration;
import java.util.Properties;
import org.eclipse.paho.client.mqttv3.internal.ClientComms;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

public class Debug {
    private static final String CLASS_NAME;
    static Class class$0 = null;
    private static final String lineSep;
    private static final Logger log;
    private static final String separator = "==============";
    private String clientID;
    private ClientComms comms;

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.internal.ClientComms");
                class$0 = cls;
            } catch (Throwable e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
        log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
        lineSep = System.getProperty("line.separator", "\n");
    }

    public Debug(String clientID, ClientComms comms) {
        this.clientID = clientID;
        this.comms = comms;
        log.setResourceName(clientID);
    }

    public void dumpClientDebug() {
        dumpClientComms();
        dumpConOptions();
        dumpClientState();
        dumpBaseDebug();
    }

    public void dumpBaseDebug() {
        dumpVersion();
        dumpSystemProperties();
        dumpMemoryTrace();
    }

    protected void dumpMemoryTrace() {
        log.dumpTrace();
    }

    protected void dumpVersion() {
        StringBuffer vInfo = new StringBuffer();
        vInfo.append(new StringBuffer(String.valueOf(lineSep)).append("============== Version Info ==============").append(lineSep).toString());
        vInfo.append(new StringBuffer(String.valueOf(left("Version", 20, ' '))).append(":  ").append(ClientComms.VERSION).append(lineSep).toString());
        vInfo.append(new StringBuffer(String.valueOf(left("Build Level", 20, ' '))).append(":  ").append(ClientComms.BUILD_LEVEL).append(lineSep).toString());
        vInfo.append(new StringBuffer("==========================================").append(lineSep).toString());
        log.fine(CLASS_NAME, "dumpVersion", vInfo.toString());
    }

    public void dumpSystemProperties() {
        log.fine(CLASS_NAME, "dumpSystemProperties", dumpProperties(System.getProperties(), "SystemProperties").toString());
    }

    public void dumpClientState() {
        if (this.comms != null && this.comms.getClientState() != null) {
            log.fine(CLASS_NAME, "dumpClientState", dumpProperties(this.comms.getClientState().getDebug(), new StringBuffer(String.valueOf(this.clientID)).append(" : ClientState").toString()).toString());
        }
    }

    public void dumpClientComms() {
        if (this.comms != null) {
            log.fine(CLASS_NAME, "dumpClientComms", dumpProperties(this.comms.getDebug(), new StringBuffer(String.valueOf(this.clientID)).append(" : ClientComms").toString()).toString());
        }
    }

    public void dumpConOptions() {
        if (this.comms != null) {
            log.fine(CLASS_NAME, "dumpConOptions", dumpProperties(this.comms.getConOptions().getDebug(), new StringBuffer(String.valueOf(this.clientID)).append(" : Connect Options").toString()).toString());
        }
    }

    public static String dumpProperties(Properties props, String name) {
        StringBuffer propStr = new StringBuffer();
        Enumeration propsE = props.propertyNames();
        propStr.append(new StringBuffer(String.valueOf(lineSep)).append("============== ").append(name).append(" ==============").append(lineSep).toString());
        while (propsE.hasMoreElements()) {
            String key = (String) propsE.nextElement();
            propStr.append(new StringBuffer(String.valueOf(left(key, 28, ' '))).append(":  ").append(props.get(key)).append(lineSep).toString());
        }
        propStr.append(new StringBuffer("==========================================").append(lineSep).toString());
        return propStr.toString();
    }

    public static String left(String s, int width, char fillChar) {
        if (s.length() >= width) {
            return s;
        }
        StringBuffer sb = new StringBuffer(width);
        sb.append(s);
        int i = width - s.length();
        while (true) {
            i--;
            if (i < 0) {
                return sb.toString();
            }
            sb.append(fillChar);
        }
    }
}
