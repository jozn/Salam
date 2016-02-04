package org.jivesoftware.smack.sasl.packet;

import org.jivesoftware.smack.packet.PlainStreamElement;
import org.jivesoftware.smack.sasl.SASLError;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.XmlStringBuilder;

public final class SaslStreamElements {

    public static class AuthMechanism extends PlainStreamElement {
        private final String authenticationText;
        private final String mechanism;

        public final /* bridge */ /* synthetic */ CharSequence toXML() {
            CharSequence xmlStringBuilder = new XmlStringBuilder();
            xmlStringBuilder.halfOpenElement("auth").xmlnsAttribute("urn:ietf:params:xml:ns:xmpp-sasl").attribute("mechanism", this.mechanism).rightAngleBracket();
            xmlStringBuilder.optAppend(this.authenticationText);
            xmlStringBuilder.closeElement("auth");
            return xmlStringBuilder;
        }

        public AuthMechanism(String mechanism, String authenticationText) {
            if (mechanism == null) {
                throw new NullPointerException("SASL mechanism shouldn't be null.");
            } else if (StringUtils.isNullOrEmpty(authenticationText)) {
                throw new IllegalArgumentException("SASL authenticationText must not be null or empty (RFC6120 6.4.2)");
            } else {
                this.mechanism = mechanism;
                this.authenticationText = authenticationText;
            }
        }
    }

    public static class Response extends PlainStreamElement {
        private final String authenticationText;

        public final /* bridge */ /* synthetic */ CharSequence toXML() {
            CharSequence xmlStringBuilder = new XmlStringBuilder();
            xmlStringBuilder.halfOpenElement("response").xmlnsAttribute("urn:ietf:params:xml:ns:xmpp-sasl").rightAngleBracket();
            xmlStringBuilder.optAppend(this.authenticationText);
            xmlStringBuilder.closeElement("response");
            return xmlStringBuilder;
        }

        public Response() {
            this.authenticationText = null;
        }

        public Response(String authenticationText) {
            this.authenticationText = StringUtils.returnIfNotEmptyTrimmed(authenticationText);
        }
    }

    public static class SASLFailure extends PlainStreamElement {
        private final SASLError saslError;
        public final String saslErrorString;

        public final /* bridge */ /* synthetic */ CharSequence toXML() {
            CharSequence xmlStringBuilder = new XmlStringBuilder();
            xmlStringBuilder.halfOpenElement("failure").xmlnsAttribute("failure").rightAngleBracket();
            xmlStringBuilder.emptyElement(this.saslErrorString);
            xmlStringBuilder.closeElement("failure");
            return xmlStringBuilder;
        }

        public SASLFailure(String saslError) {
            SASLError error = SASLError.fromString(saslError);
            if (error == null) {
                this.saslError = SASLError.not_authorized;
            } else {
                this.saslError = error;
            }
            this.saslErrorString = saslError;
        }
    }

    public static class Success extends PlainStreamElement {
        public final String data;

        public final /* bridge */ /* synthetic */ CharSequence toXML() {
            CharSequence xmlStringBuilder = new XmlStringBuilder();
            xmlStringBuilder.halfOpenElement("success").xmlnsAttribute("urn:ietf:params:xml:ns:xmpp-sasl").rightAngleBracket();
            xmlStringBuilder.optAppend(this.data);
            xmlStringBuilder.closeElement("success");
            return xmlStringBuilder;
        }

        public Success(String data) {
            this.data = StringUtils.returnIfNotEmptyTrimmed(data);
        }
    }
}
