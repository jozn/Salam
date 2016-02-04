package org.jivesoftware.smack.packet;

import java.util.Locale;
import org.jivesoftware.smack.util.XmlStringBuilder;

public abstract class IQ extends Packet {
    public Type type;

    /* renamed from: org.jivesoftware.smack.packet.IQ.1 */
    static class C12861 extends IQ {
        C12861() {
        }

        public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
            return null;
        }
    }

    /* renamed from: org.jivesoftware.smack.packet.IQ.2 */
    static class C12872 extends IQ {
        final /* synthetic */ IQ val$request;

        C12872(IQ iq) {
            this.val$request = iq;
        }

        public final CharSequence getChildElementXML() {
            return this.val$request.getChildElementXML();
        }
    }

    public enum Type {
        get,
        set,
        result,
        error;

        public static Type fromString(String string) {
            return valueOf(string.toLowerCase(Locale.US));
        }
    }

    public abstract CharSequence getChildElementXML();

    public IQ() {
        this.type = Type.get;
    }

    public IQ(IQ iq) {
        super((Packet) iq);
        this.type = Type.get;
        this.type = iq.type;
    }

    public final void setType(Type type) {
        if (type == null) {
            this.type = Type.get;
        } else {
            this.type = type;
        }
    }

    public final CharSequence toXML() {
        XmlStringBuilder buf = new XmlStringBuilder();
        buf.halfOpenElement("iq");
        addCommonAttributes(buf);
        if (this.type == null) {
            buf.attribute("type", "get");
        } else {
            buf.attribute("type", this.type.toString());
        }
        buf.rightAngleBracket();
        buf.optAppend(getChildElementXML());
        XMPPError error = this.error;
        if (error != null) {
            buf.append(error.toXML());
        }
        buf.closeElement("iq");
        return buf;
    }

    public static IQ createResultIQ(IQ request) {
        if (request.type == Type.get || request.type == Type.set) {
            IQ result = new C12861();
            result.setType(Type.result);
            result.packetID = request.packetID;
            result.from = request.to;
            result.to = request.from;
            return result;
        }
        throw new IllegalArgumentException("IQ must be of type 'set' or 'get'. Original IQ: " + request.toXML());
    }

    public static IQ createErrorResponse(IQ request, XMPPError error) {
        if (request.type == Type.get || request.type == Type.set) {
            IQ result = new C12872(request);
            result.setType(Type.error);
            result.packetID = request.packetID;
            result.from = request.to;
            result.to = request.from;
            result.error = error;
            return result;
        }
        throw new IllegalArgumentException("IQ must be of type 'set' or 'get'. Original IQ: " + request.toXML());
    }
}
