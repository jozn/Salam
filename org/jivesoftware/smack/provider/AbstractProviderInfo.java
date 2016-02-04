package org.jivesoftware.smack.provider;

abstract class AbstractProviderInfo {
    private String element;
    private String ns;
    Object provider;

    AbstractProviderInfo(String elementName, String namespace, Object iqOrExtProvider) {
        this.element = elementName;
        this.ns = namespace;
        this.provider = iqOrExtProvider;
    }

    public String getElementName() {
        return this.element;
    }

    public String getNamespace() {
        return this.ns;
    }
}
