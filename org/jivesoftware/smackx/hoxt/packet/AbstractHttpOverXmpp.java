package org.jivesoftware.smackx.hoxt.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.shim.packet.HeadersExtension;

public abstract class AbstractHttpOverXmpp extends IQ {

    public static abstract class AbstractBody {
        public Data data;
        public HeadersExtension headers;
        protected String version;

        protected abstract String getEndTag();

        protected abstract String getStartTag();

        public final String toXML() {
            StringBuilder builder = new StringBuilder();
            builder.append(getStartTag());
            builder.append(this.headers.toXML());
            Data data = this.data;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<data>");
            stringBuilder.append(data.child.toXML());
            stringBuilder.append("</data>");
            builder.append(stringBuilder.toString());
            builder.append(getEndTag());
            return builder.toString();
        }

        public final void setVersion(String version) {
            this.version = version;
        }
    }

    public interface DataChild {
        String toXML();
    }

    public static class Base64 implements DataChild {
        private final String text;

        public Base64(String text) {
            this.text = text;
        }

        public final String toXML() {
            StringBuilder builder = new StringBuilder();
            builder.append("<base64>");
            if (this.text != null) {
                builder.append(this.text);
            }
            builder.append("</base64>");
            return builder.toString();
        }
    }

    public static class ChunkedBase64 implements DataChild {
        private final String streamId;

        public ChunkedBase64(String streamId) {
            this.streamId = streamId;
        }

        public final String toXML() {
            StringBuilder builder = new StringBuilder();
            builder.append("<chunkedBase64 streamId='");
            builder.append(this.streamId);
            builder.append("'/>");
            return builder.toString();
        }
    }

    public static class Data {
        final DataChild child;

        public Data(DataChild child) {
            this.child = child;
        }
    }

    public static class Ibb implements DataChild {
        private final String sid;

        public Ibb(String sid) {
            this.sid = sid;
        }

        public final String toXML() {
            StringBuilder builder = new StringBuilder();
            builder.append("<ibb sid='");
            builder.append(this.sid);
            builder.append("'/>");
            return builder.toString();
        }
    }

    public static class Text implements DataChild {
        private final String text;

        public Text(String text) {
            this.text = text;
        }

        public final String toXML() {
            StringBuilder builder = new StringBuilder();
            builder.append("<text>");
            if (this.text != null) {
                builder.append(this.text);
            }
            builder.append("</text>");
            return builder.toString();
        }
    }

    public static class Xml implements DataChild {
        private final String text;

        public Xml(String text) {
            this.text = text;
        }

        public final String toXML() {
            StringBuilder builder = new StringBuilder();
            builder.append("<xml>");
            if (this.text != null) {
                builder.append(this.text);
            }
            builder.append("</xml>");
            return builder.toString();
        }
    }
}
