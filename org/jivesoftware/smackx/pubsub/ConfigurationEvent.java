package org.jivesoftware.smackx.pubsub;

public final class ConfigurationEvent extends NodeExtension implements EmbeddedPacketExtension {
    private ConfigureForm form;

    public ConfigurationEvent(String nodeId) {
        super(PubSubElementType.CONFIGURATION, nodeId);
    }

    public ConfigurationEvent(String nodeId, ConfigureForm configForm) {
        super(PubSubElementType.CONFIGURATION, nodeId);
        this.form = configForm;
    }
}
