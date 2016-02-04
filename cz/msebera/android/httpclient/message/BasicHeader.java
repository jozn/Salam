package cz.msebera.android.httpclient.message;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.util.Args;
import java.io.Serializable;

public final class BasicHeader implements Header, Serializable, Cloneable {
    private final String name;
    private final String value;

    public BasicHeader(String name, String value) {
        this.name = (String) Args.notNull(name, "Name");
        this.value = value;
    }

    public final String getName() {
        return this.name;
    }

    public final String getValue() {
        return this.value;
    }

    public final String toString() {
        return BasicLineFormatter.INSTANCE.formatHeader(null, this).toString();
    }

    public final HeaderElement[] getElements() throws ParseException {
        if (this.value != null) {
            return BasicHeaderValueParser.parseElements$2d622134(this.value);
        }
        return new HeaderElement[0];
    }

    public final Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
