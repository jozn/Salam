package org.jivesoftware.smack.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jxmpp.util.XmppStringUtils;

public final class ProviderManager {
    private static final Map<String, Class<?>> extensionIntrospectionProviders;
    private static final Map<String, PacketExtensionProvider> extensionProviders;
    private static final Map<String, Class<?>> iqIntrospectionProviders;
    private static final Map<String, IQProvider> iqProviders;
    private static final Map<String, StreamFeatureProvider> streamFeatureProviders;

    static {
        extensionProviders = new ConcurrentHashMap();
        iqProviders = new ConcurrentHashMap();
        extensionIntrospectionProviders = new ConcurrentHashMap();
        iqIntrospectionProviders = new ConcurrentHashMap();
        streamFeatureProviders = new ConcurrentHashMap();
        SmackConfiguration.getVersion();
    }

    public static void addLoader(ProviderLoader loader) {
        if (loader.getIQProviderInfo() != null) {
            for (IQProviderInfo info : loader.getIQProviderInfo()) {
                addIQProvider(info.getElementName(), info.getNamespace(), info.provider);
            }
        }
        if (loader.getExtensionProviderInfo() != null) {
            for (ExtensionProviderInfo info2 : loader.getExtensionProviderInfo()) {
                addExtensionProvider(info2.getElementName(), info2.getNamespace(), info2.provider);
            }
        }
    }

    public static IQProvider getIQProvider(String elementName, String namespace) {
        return (IQProvider) iqProviders.get(XmppStringUtils.generateKey(elementName, namespace));
    }

    public static Class<?> getIQIntrospectionProvider(String elementName, String namespace) {
        return (Class) iqIntrospectionProviders.get(XmppStringUtils.generateKey(elementName, namespace));
    }

    public static void addIQProvider(String elementName, String namespace, Object provider) {
        String key = XmppStringUtils.generateKey(elementName, namespace);
        iqProviders.remove(key);
        iqIntrospectionProviders.remove(key);
        if (provider instanceof IQProvider) {
            iqProviders.put(key, (IQProvider) provider);
        } else if ((provider instanceof Class) && IQ.class.isAssignableFrom((Class) provider)) {
            iqIntrospectionProviders.put(key, (Class) provider);
        } else {
            throw new IllegalArgumentException("Provider must be an IQProvider or a Class instance sublcassing IQ.");
        }
    }

    public static PacketExtensionProvider getExtensionProvider(String elementName, String namespace) {
        return (PacketExtensionProvider) extensionProviders.get(XmppStringUtils.generateKey(elementName, namespace));
    }

    public static Class<?> getExtensionIntrospectionProvider(String elementName, String namespace) {
        return (Class) extensionIntrospectionProviders.get(XmppStringUtils.generateKey(elementName, namespace));
    }

    public static void addExtensionProvider(String elementName, String namespace, Object provider) {
        String key = XmppStringUtils.generateKey(elementName, namespace);
        extensionProviders.remove(key);
        extensionIntrospectionProviders.remove(key);
        if (provider instanceof PacketExtensionProvider) {
            extensionProviders.put(key, (PacketExtensionProvider) provider);
        } else if ((provider instanceof Class) && PacketExtension.class.isAssignableFrom((Class) provider)) {
            extensionIntrospectionProviders.put(key, (Class) provider);
        } else {
            throw new IllegalArgumentException("Provider must be a PacketExtensionProvider or a Class instance.");
        }
    }

    public static StreamFeatureProvider getStreamFeatureProvider(String elementName, String namespace) {
        return (StreamFeatureProvider) streamFeatureProviders.get(XmppStringUtils.generateKey(elementName, namespace));
    }

    public static void addStreamFeatureProvider(String elementName, String namespace, StreamFeatureProvider provider) {
        streamFeatureProviders.put(XmppStringUtils.generateKey(elementName, namespace), provider);
    }
}
