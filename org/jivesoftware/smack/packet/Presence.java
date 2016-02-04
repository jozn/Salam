package org.jivesoftware.smack.packet;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.Locale;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class Presence extends Packet {
    public Mode mode;
    public int priority;
    public String status;
    public Type type;

    public enum Mode {
        chat,
        available,
        away,
        xa,
        dnd;

        public static Mode fromString(String string) {
            return valueOf(string.toLowerCase(Locale.US));
        }
    }

    public enum Type {
        available,
        unavailable,
        subscribe,
        subscribed,
        unsubscribe,
        unsubscribed,
        error,
        probe;

        public static Type fromString(String string) {
            return valueOf(string.toLowerCase(Locale.US));
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder();
        xmlStringBuilder.halfOpenElement("presence");
        addCommonAttributes(xmlStringBuilder);
        if (this.type != Type.available) {
            xmlStringBuilder.attribute("type", this.type);
        }
        xmlStringBuilder.rightAngleBracket();
        xmlStringBuilder.optElement(NotificationCompatApi21.CATEGORY_STATUS, this.status);
        if (this.priority != RtlSpacingHelper.UNDEFINED) {
            xmlStringBuilder.element("priority", Integer.toString(this.priority));
        }
        if (!(this.mode == null || this.mode == Mode.available)) {
            String str = "show";
            Enum enumR = this.mode;
            if (XmlStringBuilder.$assertionsDisabled || enumR != null) {
                xmlStringBuilder.element(str, enumR.name());
            } else {
                throw new AssertionError();
            }
        }
        xmlStringBuilder.append(getExtensionsXML());
        XMPPError xMPPError = this.error;
        if (xMPPError != null) {
            xmlStringBuilder.append(xMPPError.toXML());
        }
        xmlStringBuilder.closeElement("presence");
        return xmlStringBuilder;
    }

    public Presence(Type type) {
        this.type = Type.available;
        this.status = null;
        this.priority = RtlSpacingHelper.UNDEFINED;
        this.mode = null;
        if (type == null) {
            throw new NullPointerException("Type cannot be null");
        }
        this.type = type;
    }

    public final void setPriority(int priority) {
        if (priority < -128 || priority > AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) {
            throw new IllegalArgumentException("Priority value " + priority + " is not valid. Valid range is -128 through 128.");
        }
        this.priority = priority;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(this.type);
        if (this.mode != null) {
            buf.append(": ").append(this.mode);
        }
        if (this.status != null) {
            buf.append(" (").append(this.status).append(")");
        }
        return buf.toString();
    }
}
