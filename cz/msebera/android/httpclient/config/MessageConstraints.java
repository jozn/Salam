package cz.msebera.android.httpclient.config;

public final class MessageConstraints implements Cloneable {
    public static final MessageConstraints DEFAULT;
    public final int maxHeaderCount;
    public final int maxLineLength;

    public static class Builder {
        public int maxHeaderCount;
        public int maxLineLength;

        Builder() {
            this.maxLineLength = -1;
            this.maxHeaderCount = -1;
        }

        public final MessageConstraints build() {
            return new MessageConstraints(this.maxLineLength, this.maxHeaderCount);
        }
    }

    protected final /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
        return (MessageConstraints) super.clone();
    }

    static {
        DEFAULT = new Builder().build();
    }

    MessageConstraints(int maxLineLength, int maxHeaderCount) {
        this.maxLineLength = maxLineLength;
        this.maxHeaderCount = maxHeaderCount;
    }

    public final String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[maxLineLength=").append(this.maxLineLength).append(", maxHeaderCount=").append(this.maxHeaderCount).append("]");
        return builder.toString();
    }

    public static Builder custom() {
        return new Builder();
    }
}
