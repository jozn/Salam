package org.jivesoftware.smack.packet;

public final class StreamError {
    public String code;
    private String text;

    private StreamError(String code) {
        this.code = code;
    }

    public StreamError(String code, String text) {
        this(code);
        this.text = text;
    }

    public final String toString() {
        StringBuilder txt = new StringBuilder();
        txt.append("stream:error (").append(this.code).append(")");
        if (this.text != null) {
            txt.append(" text: ").append(this.text);
        }
        return txt.toString();
    }
}
