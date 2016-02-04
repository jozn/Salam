package org.jivesoftware.smack.packet;

import com.shamchat.activity.AddFavoriteTextActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.jivesoftware.smack.util.XmlStringBuilder;

public final class XMPPError {
    private List<PacketExtension> applicationExtensions;
    private final String condition;
    private String message;
    public final Type type;

    public static class Condition implements CharSequence {
        public static final Condition bad_request;
        public static final Condition conflict;
        public static final Condition feature_not_implemented;
        public static final Condition forbidden;
        public static final Condition gone;
        public static final Condition internal_server_error;
        public static final Condition item_not_found;
        public static final Condition jid_malformed;
        public static final Condition not_acceptable;
        public static final Condition not_allowed;
        public static final Condition not_authorized;
        public static final Condition payment_required;
        public static final Condition recipient_unavailable;
        public static final Condition redirect;
        public static final Condition registration_required;
        public static final Condition remote_server_error;
        public static final Condition remote_server_not_found;
        public static final Condition remote_server_timeout;
        public static final Condition request_timeout;
        public static final Condition resource_constraint;
        public static final Condition service_unavailable;
        public static final Condition subscription_required;
        public static final Condition undefined_condition;
        public static final Condition unexpected_request;
        private final String value;

        static {
            internal_server_error = new Condition("internal-server-error");
            forbidden = new Condition("forbidden");
            bad_request = new Condition("bad-request");
            conflict = new Condition("conflict");
            feature_not_implemented = new Condition("feature-not-implemented");
            gone = new Condition("gone");
            item_not_found = new Condition("item-not-found");
            jid_malformed = new Condition("jid-malformed");
            not_acceptable = new Condition("not-acceptable");
            not_allowed = new Condition("not-allowed");
            not_authorized = new Condition("not-authorized");
            payment_required = new Condition("payment-required");
            recipient_unavailable = new Condition("recipient-unavailable");
            redirect = new Condition("redirect");
            registration_required = new Condition("registration-required");
            remote_server_error = new Condition("remote-server-error");
            remote_server_not_found = new Condition("remote-server-not-found");
            remote_server_timeout = new Condition("remote-server-timeout");
            resource_constraint = new Condition("resource-constraint");
            service_unavailable = new Condition("service-unavailable");
            subscription_required = new Condition("subscription-required");
            undefined_condition = new Condition("undefined-condition");
            unexpected_request = new Condition("unexpected-request");
            request_timeout = new Condition("request-timeout");
        }

        public Condition(String value) {
            this.value = value;
        }

        public final String toString() {
            return this.value;
        }

        public final boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            return toString().equals(other.toString());
        }

        public final int hashCode() {
            return this.value.hashCode();
        }

        public final int length() {
            return this.value.length();
        }

        public final char charAt(int index) {
            return this.value.charAt(index);
        }

        public final CharSequence subSequence(int start, int end) {
            return this.value.subSequence(start, end);
        }
    }

    private static class ErrorSpecification {
        private static Map<Condition, ErrorSpecification> instances;
        private final Condition condition;
        final Type type;

        static {
            Map hashMap = new HashMap();
            instances = hashMap;
            hashMap.put(Condition.internal_server_error, new ErrorSpecification(Condition.internal_server_error, Type.WAIT));
            instances.put(Condition.forbidden, new ErrorSpecification(Condition.forbidden, Type.AUTH));
            instances.put(Condition.bad_request, new ErrorSpecification(Condition.bad_request, Type.MODIFY));
            instances.put(Condition.item_not_found, new ErrorSpecification(Condition.item_not_found, Type.CANCEL));
            instances.put(Condition.conflict, new ErrorSpecification(Condition.conflict, Type.CANCEL));
            instances.put(Condition.feature_not_implemented, new ErrorSpecification(Condition.feature_not_implemented, Type.CANCEL));
            instances.put(Condition.gone, new ErrorSpecification(Condition.gone, Type.MODIFY));
            instances.put(Condition.jid_malformed, new ErrorSpecification(Condition.jid_malformed, Type.MODIFY));
            instances.put(Condition.not_acceptable, new ErrorSpecification(Condition.not_acceptable, Type.MODIFY));
            instances.put(Condition.not_allowed, new ErrorSpecification(Condition.not_allowed, Type.CANCEL));
            instances.put(Condition.not_authorized, new ErrorSpecification(Condition.not_authorized, Type.AUTH));
            instances.put(Condition.payment_required, new ErrorSpecification(Condition.payment_required, Type.AUTH));
            instances.put(Condition.recipient_unavailable, new ErrorSpecification(Condition.recipient_unavailable, Type.WAIT));
            instances.put(Condition.redirect, new ErrorSpecification(Condition.redirect, Type.MODIFY));
            instances.put(Condition.registration_required, new ErrorSpecification(Condition.registration_required, Type.AUTH));
            instances.put(Condition.remote_server_not_found, new ErrorSpecification(Condition.remote_server_not_found, Type.CANCEL));
            instances.put(Condition.remote_server_timeout, new ErrorSpecification(Condition.remote_server_timeout, Type.WAIT));
            instances.put(Condition.remote_server_error, new ErrorSpecification(Condition.remote_server_error, Type.CANCEL));
            instances.put(Condition.resource_constraint, new ErrorSpecification(Condition.resource_constraint, Type.WAIT));
            instances.put(Condition.service_unavailable, new ErrorSpecification(Condition.service_unavailable, Type.CANCEL));
            instances.put(Condition.subscription_required, new ErrorSpecification(Condition.subscription_required, Type.AUTH));
            instances.put(Condition.undefined_condition, new ErrorSpecification(Condition.undefined_condition, Type.WAIT));
            instances.put(Condition.unexpected_request, new ErrorSpecification(Condition.unexpected_request, Type.WAIT));
            instances.put(Condition.request_timeout, new ErrorSpecification(Condition.request_timeout, Type.CANCEL));
        }

        private ErrorSpecification(Condition condition, Type type) {
            this.type = type;
            this.condition = condition;
        }

        protected static ErrorSpecification specFor(Condition condition) {
            return (ErrorSpecification) instances.get(condition);
        }
    }

    public enum Type {
        WAIT,
        CANCEL,
        MODIFY,
        AUTH,
        CONTINUE
    }

    public XMPPError(String condition) {
        this(new Condition(condition));
    }

    public XMPPError(Condition condition) {
        this.applicationExtensions = null;
        ErrorSpecification defaultErrorSpecification = ErrorSpecification.specFor(condition);
        this.condition = condition.value;
        if (defaultErrorSpecification != null) {
            this.type = defaultErrorSpecification.type;
        } else {
            this.type = null;
        }
    }

    public XMPPError(Type type, String condition, String message, List<PacketExtension> extension) {
        this.applicationExtensions = null;
        this.type = type;
        this.condition = condition;
        this.message = message;
        this.applicationExtensions = extension;
    }

    public final XmlStringBuilder toXML() {
        XmlStringBuilder xml = new XmlStringBuilder();
        xml.halfOpenElement(MqttServiceConstants.TRACE_ERROR);
        xml.optAttribute("type", this.type.name().toLowerCase(Locale.US));
        xml.rightAngleBracket();
        if (this.condition != null) {
            xml.halfOpenElement(this.condition);
            xml.xmlnsAttribute("urn:ietf:params:xml:ns:xmpp-stanzas");
            xml.closeEmptyElement();
        }
        if (this.message != null) {
            xml.halfOpenElement(AddFavoriteTextActivity.EXTRA_RESULT_TEXT);
            xml.xmllangAttribute("en");
            xml.xmlnsAttribute("urn:ietf:params:xml:ns:xmpp-stanzas");
            xml.rightAngleBracket();
            xml.escape(this.message);
            xml.closeElement(AddFavoriteTextActivity.EXTRA_RESULT_TEXT);
        }
        for (PacketExtension element : getExtensions()) {
            xml.append(element.toXML());
        }
        xml.closeElement(MqttServiceConstants.TRACE_ERROR);
        return xml;
    }

    public final String toString() {
        StringBuilder txt = new StringBuilder();
        if (this.condition != null) {
            txt.append(this.condition);
        }
        if (this.message != null) {
            txt.append(" ").append(this.message);
        }
        return txt.toString();
    }

    private synchronized List<PacketExtension> getExtensions() {
        List<PacketExtension> emptyList;
        if (this.applicationExtensions == null) {
            emptyList = Collections.emptyList();
        } else {
            emptyList = Collections.unmodifiableList(this.applicationExtensions);
        }
        return emptyList;
    }

    public final synchronized void addExtension(PacketExtension extension) {
        if (this.applicationExtensions == null) {
            this.applicationExtensions = new ArrayList();
        }
        this.applicationExtensions.add(extension);
    }
}
