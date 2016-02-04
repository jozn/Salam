package cz.msebera.android.httpclient.impl.cookie;

import cz.msebera.android.httpclient.cookie.ClientCookie;
import cz.msebera.android.httpclient.cookie.SetCookie;
import cz.msebera.android.httpclient.util.Args;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BasicClientCookie implements ClientCookie, SetCookie, Serializable, Cloneable {
    private Map<String, String> attribs;
    private String cookieComment;
    private String cookieDomain;
    private Date cookieExpiryDate;
    String cookiePath;
    int cookieVersion;
    private boolean isSecure;
    private final String name;
    private String value;

    public BasicClientCookie(String name, String value) {
        Args.notNull(name, "Name");
        this.name = name;
        this.attribs = new HashMap();
        this.value = value;
    }

    public final String getName() {
        return this.name;
    }

    public final String getValue() {
        return this.value;
    }

    public final void setComment(String comment) {
        this.cookieComment = comment;
    }

    public final Date getExpiryDate() {
        return this.cookieExpiryDate;
    }

    public final void setExpiryDate(Date expiryDate) {
        this.cookieExpiryDate = expiryDate;
    }

    public final String getDomain() {
        return this.cookieDomain;
    }

    public final void setDomain(String domain) {
        if (domain != null) {
            this.cookieDomain = domain.toLowerCase(Locale.ROOT);
        } else {
            this.cookieDomain = null;
        }
    }

    public final String getPath() {
        return this.cookiePath;
    }

    public final void setPath(String path) {
        this.cookiePath = path;
    }

    public final boolean isSecure() {
        return this.isSecure;
    }

    public final void setSecure$1385ff() {
        this.isSecure = true;
    }

    public int[] getPorts() {
        return null;
    }

    public final int getVersion() {
        return this.cookieVersion;
    }

    public final void setVersion(int version) {
        this.cookieVersion = version;
    }

    public boolean isExpired(Date date) {
        Args.notNull(date, "Date");
        return this.cookieExpiryDate != null && this.cookieExpiryDate.getTime() <= date.getTime();
    }

    public final void setAttribute(String name, String value) {
        this.attribs.put(name, value);
    }

    public final String getAttribute(String name) {
        return (String) this.attribs.get(name);
    }

    public final boolean containsAttribute(String name) {
        return this.attribs.containsKey(name);
    }

    public Object clone() throws CloneNotSupportedException {
        BasicClientCookie clone = (BasicClientCookie) super.clone();
        clone.attribs = new HashMap(this.attribs);
        return clone;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[version: ");
        buffer.append(Integer.toString(this.cookieVersion));
        buffer.append("]");
        buffer.append("[name: ");
        buffer.append(this.name);
        buffer.append("]");
        buffer.append("[value: ");
        buffer.append(this.value);
        buffer.append("]");
        buffer.append("[domain: ");
        buffer.append(this.cookieDomain);
        buffer.append("]");
        buffer.append("[path: ");
        buffer.append(this.cookiePath);
        buffer.append("]");
        buffer.append("[expiry: ");
        buffer.append(this.cookieExpiryDate);
        buffer.append("]");
        return buffer.toString();
    }
}
