package org.jivesoftware.smack;

import java.util.List;
import org.jivesoftware.smack.util.dns.HostAddress;

public class SmackException extends Exception {

    public static class AlreadyConnectedException extends SmackException {
    }

    public static class AlreadyLoggedInException extends SmackException {
    }

    public static class ConnectionException extends SmackException {
        private final List<HostAddress> failedAddresses;

        private ConnectionException(String message, List<HostAddress> failedAddresses) {
            super(message);
            this.failedAddresses = failedAddresses;
        }

        public static ConnectionException from(List<HostAddress> failedAddresses) {
            StringBuilder sb = new StringBuilder("The following addresses failed: ");
            for (HostAddress hostAddress : failedAddresses) {
                sb.append(hostAddress.toString() + " Exception: " + (hostAddress.exception == null ? "No error logged" : hostAddress.exception.getMessage()));
                sb.append(", ");
            }
            sb.deleteCharAt(sb.length() - 1);
            return new ConnectionException(sb.toString(), failedAddresses);
        }
    }

    public static class NoResponseException extends SmackException {
    }

    public static class NotConnectedException extends SmackException {
    }

    public static class NotLoggedInException extends SmackException {
    }

    public static class ResourceBindingNotOfferedException extends SmackException {
    }

    public static class SecurityNotPossibleException extends SmackException {
        public SecurityNotPossibleException(String message) {
            super(message);
        }
    }

    public static class SecurityRequiredException extends SmackException {
        public SecurityRequiredException(String message) {
            super(message);
        }
    }

    public SmackException(Throwable wrappedThrowable) {
        super(wrappedThrowable);
    }

    public SmackException(String message) {
        super(message);
    }

    public SmackException(String message, Throwable wrappedThrowable) {
        super(message, wrappedThrowable);
    }
}
