package org.jivesoftware.smackx.hoxt.packet;

import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.hoxt.packet.AbstractHttpOverXmpp.AbstractBody;

public class HttpOverXmppResp extends AbstractHttpOverXmpp {
    public Resp resp;

    public static class Resp extends AbstractBody {
        public int statusCode;
        public String statusMessage;

        public Resp() {
            this.statusMessage = null;
        }

        protected final String getStartTag() {
            StringBuilder builder = new StringBuilder();
            builder.append("<resp");
            builder.append(" ");
            builder.append("xmlns='urn:xmpp:http'");
            builder.append(" ");
            builder.append("version='").append(StringUtils.escapeForXML(this.version)).append("'");
            builder.append(" ");
            builder.append("statusCode='").append(Integer.toString(this.statusCode)).append("'");
            if (this.statusMessage != null) {
                builder.append(" ");
                builder.append("statusMessage='").append(StringUtils.escapeForXML(this.statusMessage)).append("'");
            }
            builder.append(">");
            return builder.toString();
        }

        protected final String getEndTag() {
            return "</resp>";
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        return this.resp.toXML();
    }
}
