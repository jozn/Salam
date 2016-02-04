package org.jivesoftware.smackx.pubsub;

import java.util.Locale;
import org.jivesoftware.smackx.pubsub.packet.PubSubNamespace;

public enum PubSubElementType {
    CREATE("create", PubSubNamespace.BASIC),
    DELETE("delete", PubSubNamespace.OWNER),
    DELETE_EVENT("delete", PubSubNamespace.EVENT),
    CONFIGURE("configure", PubSubNamespace.BASIC),
    CONFIGURE_OWNER("configure", PubSubNamespace.OWNER),
    CONFIGURATION("configuration", PubSubNamespace.EVENT),
    OPTIONS("options", PubSubNamespace.BASIC),
    DEFAULT("default", PubSubNamespace.OWNER),
    ITEMS("items", PubSubNamespace.BASIC),
    ITEMS_EVENT("items", PubSubNamespace.EVENT),
    ITEM("item", PubSubNamespace.BASIC),
    ITEM_EVENT("item", PubSubNamespace.EVENT),
    PUBLISH("publish", PubSubNamespace.BASIC),
    PUBLISH_OPTIONS("publish-options", PubSubNamespace.BASIC),
    PURGE_OWNER("purge", PubSubNamespace.OWNER),
    PURGE_EVENT("purge", PubSubNamespace.EVENT),
    RETRACT("retract", PubSubNamespace.BASIC),
    AFFILIATIONS("affiliations", PubSubNamespace.BASIC),
    SUBSCRIBE(MqttServiceConstants.SUBSCRIBE_ACTION, PubSubNamespace.BASIC),
    SUBSCRIPTION("subscription", PubSubNamespace.BASIC),
    SUBSCRIPTIONS("subscriptions", PubSubNamespace.BASIC),
    UNSUBSCRIBE(MqttServiceConstants.UNSUBSCRIBE_ACTION, PubSubNamespace.BASIC);
    
    String eName;
    PubSubNamespace nSpace;

    private PubSubElementType(String elemName, PubSubNamespace ns) {
        this.eName = elemName;
        this.nSpace = ns;
    }

    public static PubSubElementType valueOfFromElemName(String elemName, String namespace) {
        int index = namespace.lastIndexOf(35);
        String fragment = index == -1 ? null : namespace.substring(index + 1);
        if (fragment != null) {
            return valueOf((elemName + '_' + fragment).toUpperCase(Locale.US));
        }
        return valueOf(elemName.toUpperCase(Locale.US).replace('-', '_'));
    }
}
