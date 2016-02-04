package org.jivesoftware.smack.proxy;

public final class ProxyInfo {
    String proxyAddress;
    String proxyPassword;
    int proxyPort;
    public ProxyType proxyType;
    String proxyUsername;

    public enum ProxyType {
        NONE,
        HTTP,
        SOCKS4,
        SOCKS5
    }

    private ProxyInfo(ProxyType pType) {
        this.proxyType = pType;
        this.proxyAddress = null;
        this.proxyPort = 0;
        this.proxyUsername = null;
        this.proxyPassword = null;
    }

    public static ProxyInfo forDefaultProxy() {
        return new ProxyInfo(ProxyType.NONE);
    }
}
