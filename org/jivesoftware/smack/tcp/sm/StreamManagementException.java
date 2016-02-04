package org.jivesoftware.smack.tcp.sm;

import org.jivesoftware.smack.SmackException;

public abstract class StreamManagementException extends SmackException {

    public static class StreamIdDoesNotMatchException extends StreamManagementException {
        public StreamIdDoesNotMatchException(String expected, String got) {
            super("Stream IDs do not match. Expected '" + expected + "', but got '" + got + "'");
        }
    }

    public static class StreamManagementNotEnabledException extends StreamManagementException {
    }

    public StreamManagementException(String message) {
        super(message);
    }
}
