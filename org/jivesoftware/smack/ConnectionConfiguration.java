package org.jivesoftware.smack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.security.auth.callback.CallbackHandler;
import org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory;
import org.jivesoftware.smack.proxy.DirectSocketFactory;
import org.jivesoftware.smack.proxy.HTTPProxySocketFactory;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.proxy.ProxyInfo.ProxyType;
import org.jivesoftware.smack.proxy.Socks4ProxySocketFactory;
import org.jivesoftware.smack.proxy.Socks5ProxySocketFactory;
import org.jivesoftware.smack.rosterstore.RosterStore;
import org.jivesoftware.smack.util.DNSUtil;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.dns.HostAddress;

public final class ConnectionConfiguration implements Cloneable {
    public CallbackHandler callbackHandler;
    public boolean compressionEnabled;
    public SSLContext customSSLContext;
    public boolean debuggerEnabled;
    public String[] enabledSSLCiphers;
    public String[] enabledSSLProtocols;
    protected List<HostAddress> hostAddresses;
    public HostnameVerifier hostnameVerifier;
    public String keystorePath;
    public String keystoreType;
    boolean legacySessionDisabled;
    public String password;
    public String pkcs11Library;
    protected ProxyInfo proxy;
    public boolean reconnectionAllowed;
    public String resource;
    public boolean rosterLoadedAtLogin;
    RosterStore rosterStore;
    public int securityMode$21bc7c29;
    public boolean sendPresence;
    public String serviceName;
    public SocketFactory socketFactory;
    private boolean useDnsSrvRr;
    public String username;

    public enum SecurityMode {
        ;

        static {
            required$21bc7c29 = 1;
            enabled$21bc7c29 = 2;
            disabled$21bc7c29 = 3;
            $VALUES$2c11d652 = new int[]{required$21bc7c29, enabled$21bc7c29, disabled$21bc7c29};
        }
    }

    public ConnectionConfiguration(String serviceName) {
        this.compressionEnabled = false;
        this.debuggerEnabled = SmackConfiguration.DEBUG_ENABLED;
        this.reconnectionAllowed = true;
        this.sendPresence = true;
        this.rosterLoadedAtLogin = true;
        this.legacySessionDisabled = false;
        this.useDnsSrvRr = true;
        this.securityMode$21bc7c29 = SecurityMode.enabled$21bc7c29;
        init(serviceName, ProxyInfo.forDefaultProxy());
    }

    public ConnectionConfiguration(String host, int port, String serviceName) {
        this.compressionEnabled = false;
        this.debuggerEnabled = SmackConfiguration.DEBUG_ENABLED;
        this.reconnectionAllowed = true;
        this.sendPresence = true;
        this.rosterLoadedAtLogin = true;
        this.legacySessionDisabled = false;
        this.useDnsSrvRr = true;
        this.securityMode$21bc7c29 = SecurityMode.enabled$21bc7c29;
        if (StringUtils.isEmpty(host)) {
            throw new IllegalArgumentException("host must not be the empty String");
        }
        this.hostAddresses = new ArrayList(1);
        this.hostAddresses.add(new HostAddress(host, port));
        this.useDnsSrvRr = false;
        init(serviceName, ProxyInfo.forDefaultProxy());
    }

    private void init(String serviceName, ProxyInfo proxy) {
        if (StringUtils.isEmpty(serviceName)) {
            throw new IllegalArgumentException("serviceName must not be the empty String");
        }
        this.serviceName = serviceName;
        this.proxy = proxy;
        this.keystorePath = System.getProperty(SSLSocketFactoryFactory.SYSKEYSTORE);
        this.keystoreType = "jks";
        this.pkcs11Library = "pkcs11.config";
        SocketFactory directSocketFactory = proxy.proxyType == ProxyType.NONE ? new DirectSocketFactory() : proxy.proxyType == ProxyType.HTTP ? new HTTPProxySocketFactory(proxy) : proxy.proxyType == ProxyType.SOCKS4 ? new Socks4ProxySocketFactory(proxy) : proxy.proxyType == ProxyType.SOCKS5 ? new Socks5ProxySocketFactory(proxy) : null;
        this.socketFactory = directSocketFactory;
    }

    public final List<HostAddress> getHostAddresses() {
        return Collections.unmodifiableList(this.hostAddresses);
    }

    public final void maybeResolveDns() throws Exception {
        if (this.useDnsSrvRr) {
            this.hostAddresses = DNSUtil.resolveXMPPDomain(this.serviceName);
        }
    }
}
