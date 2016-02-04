package org.jivesoftware.smack.filter;

import android.support.v7.appcompat.BuildConfig;
import java.util.Locale;
import org.jivesoftware.smack.packet.Packet;
import org.jxmpp.util.XmppStringUtils;

public final class FromMatchesFilter implements PacketFilter {
    private String address;
    private boolean matchBareJID;

    public FromMatchesFilter(String address, boolean matchBare) {
        this.matchBareJID = false;
        this.address = address == null ? null : address.toLowerCase(Locale.US);
        this.matchBareJID = matchBare;
    }

    public static FromMatchesFilter create(String address) {
        return new FromMatchesFilter(address, BuildConfig.VERSION_NAME.equals(XmppStringUtils.parseResource(address)));
    }

    public static FromMatchesFilter createFull(String address) {
        return new FromMatchesFilter(address, false);
    }

    public final boolean accept(Packet packet) {
        String from = packet.from;
        if (from == null) {
            return this.address == null;
        } else {
            from = from.toLowerCase(Locale.US);
            if (this.matchBareJID) {
                from = XmppStringUtils.parseBareAddress(from);
            }
            return from.equals(this.address);
        }
    }

    public final String toString() {
        return "FromMatchesFilter (" + (this.matchBareJID ? "bare" : "full") + "): " + this.address;
    }
}
