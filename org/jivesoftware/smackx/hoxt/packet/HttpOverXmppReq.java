package org.jivesoftware.smackx.hoxt.packet;

import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.hoxt.packet.AbstractHttpOverXmpp.AbstractBody;

public class HttpOverXmppReq extends AbstractHttpOverXmpp {
    public Req req;

    public static class Req extends AbstractBody {
        public boolean ibb;
        public boolean jingle;
        public int maxChunkSize;
        private HttpMethod method;
        private String resource;
        public boolean sipub;

        public Req(HttpMethod method, String resource) {
            this.maxChunkSize = 0;
            this.sipub = true;
            this.ibb = true;
            this.jingle = true;
            this.method = method;
            this.resource = resource;
        }

        protected final String getStartTag() {
            StringBuilder builder = new StringBuilder();
            builder.append("<req");
            builder.append(" ");
            builder.append("xmlns='urn:xmpp:http'");
            builder.append(" ");
            builder.append("method='").append(this.method.toString()).append("'");
            builder.append(" ");
            builder.append("resource='").append(StringUtils.escapeForXML(this.resource)).append("'");
            builder.append(" ");
            builder.append("version='").append(StringUtils.escapeForXML(this.version)).append("'");
            if (this.maxChunkSize != 0) {
                builder.append(" ");
                builder.append("maxChunkSize='").append(Integer.toString(this.maxChunkSize)).append("'");
            }
            builder.append(" ");
            builder.append("sipub='").append(Boolean.toString(this.sipub)).append("'");
            builder.append(" ");
            builder.append("ibb='").append(Boolean.toString(this.ibb)).append("'");
            builder.append(" ");
            builder.append("jingle='").append(Boolean.toString(this.jingle)).append("'");
            builder.append(">");
            return builder.toString();
        }

        protected final String getEndTag() {
            return "</req>";
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        return this.req.toXML();
    }
}
