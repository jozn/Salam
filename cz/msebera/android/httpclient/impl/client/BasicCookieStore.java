package cz.msebera.android.httpclient.impl.client;

import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.cookie.CookieIdentityComparator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public final class BasicCookieStore implements CookieStore, Serializable {
    private final TreeSet<Cookie> cookies;

    public BasicCookieStore() {
        this.cookies = new TreeSet(new CookieIdentityComparator());
    }

    public final synchronized void addCookie(Cookie cookie) {
        if (cookie != null) {
            this.cookies.remove(cookie);
            if (!cookie.isExpired(new Date())) {
                this.cookies.add(cookie);
            }
        }
    }

    public final synchronized List<Cookie> getCookies() {
        return new ArrayList(this.cookies);
    }

    public final synchronized boolean clearExpired(Date date) {
        boolean removed;
        removed = false;
        Iterator<Cookie> it = this.cookies.iterator();
        while (it.hasNext()) {
            if (((Cookie) it.next()).isExpired(date)) {
                it.remove();
                removed = true;
            }
        }
        return removed;
    }

    public final synchronized String toString() {
        return this.cookies.toString();
    }
}
