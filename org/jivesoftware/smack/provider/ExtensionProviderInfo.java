package org.jivesoftware.smack.provider;

public final class ExtensionProviderInfo extends AbstractProviderInfo {
    public final /* bridge */ /* synthetic */ String getElementName() {
        return super.getElementName();
    }

    public final /* bridge */ /* synthetic */ String getNamespace() {
        return super.getNamespace();
    }

    public ExtensionProviderInfo(String elementName, String namespace, PacketExtensionProvider extProvider) {
        super(elementName, namespace, extProvider);
    }

    public ExtensionProviderInfo(String elementName, String namespace, Class<?> beanClass) {
        super(elementName, namespace, beanClass);
    }
}
