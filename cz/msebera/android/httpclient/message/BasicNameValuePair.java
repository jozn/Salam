package cz.msebera.android.httpclient.message;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.LangUtils;
import java.io.Serializable;

public final class BasicNameValuePair implements NameValuePair, Serializable, Cloneable {
    public final String name;
    public final String value;

    public BasicNameValuePair(String name, String value) {
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
        if (this.value == null) {
            return this.name;
        }
        StringBuilder buffer = new StringBuilder((this.name.length() + 1) + this.value.length());
        buffer.append(this.name);
        buffer.append("=");
        buffer.append(this.value);
        return buffer.toString();
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof NameValuePair)) {
            return false;
        }
        BasicNameValuePair that = (BasicNameValuePair) object;
        if (this.name.equals(that.name) && LangUtils.equals(this.value, that.value)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return LangUtils.hashCode(LangUtils.hashCode(17, this.name), this.value);
    }

    public final Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
