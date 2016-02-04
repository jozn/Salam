package org.jivesoftware.smack.provider;

import org.jivesoftware.smack.packet.IQ;

public final class IQProviderInfo extends AbstractProviderInfo {
    public final /* bridge */ /* synthetic */ String getElementName() {
        return super.getElementName();
    }

    public final /* bridge */ /* synthetic */ String getNamespace() {
        return super.getNamespace();
    }

    public IQProviderInfo(String elementName, String namespace, IQProvider iqProvider) {
        super(elementName, namespace, iqProvider);
    }

    public IQProviderInfo(String elementName, String namespace, Class<? extends IQ> iqProviderClass) {
        super(elementName, namespace, iqProviderClass);
    }
}
