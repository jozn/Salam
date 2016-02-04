package org.eclipse.paho.client.mqttv3.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import javax.net.SocketFactory;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

public class TCPNetworkModule implements NetworkModule {
    private static final String CLASS_NAME;
    static Class class$0;
    private static final Logger log;
    private int conTimeout;
    private SocketFactory factory;
    private String host;
    private int port;
    protected Socket socket;

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.internal.TCPNetworkModule");
                class$0 = cls;
            } catch (Throwable e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
        log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    }

    public TCPNetworkModule(SocketFactory factory, String host, int port, String resourceContext) {
        log.setResourceName(resourceContext);
        this.factory = factory;
        this.host = host;
        this.port = port;
    }

    public void start() throws IOException, MqttException {
        try {
            log.fine(CLASS_NAME, "start", "252", new Object[]{this.host, new Integer(this.port), new Long((long) (this.conTimeout * 1000))});
            SocketAddress sockaddr = new InetSocketAddress(this.host, this.port);
            this.socket = this.factory.createSocket();
            this.socket.connect(sockaddr, this.conTimeout * 1000);
        } catch (ConnectException ex) {
            log.fine(CLASS_NAME, "start", "250", null, ex);
            throw new MqttException(32103, ex);
        }
    }

    public InputStream getInputStream() throws IOException {
        return this.socket.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return this.socket.getOutputStream();
    }

    public void stop() throws IOException {
        if (this.socket != null) {
            this.socket.close();
        }
    }

    public void setConnectTimeout(int timeout) {
        this.conTimeout = timeout;
    }
}
