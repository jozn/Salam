package org.jivesoftware.smackx.pubsub.packet;

import java.util.Locale;

public enum PubSubNamespace {
    BASIC(null),
    ERROR("errors"),
    EVENT(NotificationCompatApi21.CATEGORY_EVENT),
    OWNER("owner");
    
    private String fragment;

    private PubSubNamespace(String fragment) {
        this.fragment = fragment;
    }

    public final String getXmlns() {
        String ns = "http://jabber.org/protocol/pubsub";
        if (this.fragment != null) {
            return ns + '#' + this.fragment;
        }
        return ns;
    }

    public static PubSubNamespace valueOfFromXmlns(String ns) {
        if (ns.lastIndexOf(35) != -1) {
            return valueOf(ns.substring(ns.lastIndexOf(35) + 1).toUpperCase(Locale.US));
        }
        return BASIC;
    }
}
