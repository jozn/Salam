package org.eclipse.paho.client.mqttv3.internal;

import android.support.v7.appcompat.BuildConfig;
import java.io.IOException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

public class SSLNetworkModule extends TCPNetworkModule {
    private static final String CLASS_NAME;
    static Class class$0;
    private static final Logger log;
    private String[] enabledCiphers;
    private int handshakeTimeoutSecs;

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.internal.SSLNetworkModule");
                class$0 = cls;
            } catch (Throwable e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
        log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    }

    public SSLNetworkModule(SSLSocketFactory factory, String host, int port, String resourceContext) {
        super(factory, host, port, resourceContext);
        log.setResourceName(resourceContext);
    }

    public String[] getEnabledCiphers() {
        return this.enabledCiphers;
    }

    public void setEnabledCiphers(String[] enabledCiphers) {
        this.enabledCiphers = enabledCiphers;
        if (this.socket != null && enabledCiphers != null) {
            if (log.isLoggable(5)) {
                String ciphers = BuildConfig.VERSION_NAME;
                for (int i = 0; i < enabledCiphers.length; i++) {
                    if (i > 0) {
                        ciphers = new StringBuffer(String.valueOf(ciphers)).append(",").toString();
                    }
                    ciphers = new StringBuffer(String.valueOf(ciphers)).append(enabledCiphers[i]).toString();
                }
                log.fine(CLASS_NAME, "setEnabledCiphers", "260", new Object[]{ciphers});
            }
            ((SSLSocket) this.socket).setEnabledCipherSuites(enabledCiphers);
        }
    }

    public void setSSLhandshakeTimeout(int timeout) {
        super.setConnectTimeout(timeout);
        this.handshakeTimeoutSecs = timeout;
    }

    public void start() throws IOException, MqttException {
        super.start();
        setEnabledCiphers(this.enabledCiphers);
        int soTimeout = this.socket.getSoTimeout();
        if (soTimeout == 0) {
            this.socket.setSoTimeout(this.handshakeTimeoutSecs * 1000);
        }
        ((SSLSocket) this.socket).startHandshake();
        this.socket.setSoTimeout(soTimeout);
    }
}
