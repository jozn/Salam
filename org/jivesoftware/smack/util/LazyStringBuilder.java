package org.jivesoftware.smack.util;

import java.util.ArrayList;
import java.util.List;

public class LazyStringBuilder implements Appendable, CharSequence {
    static final /* synthetic */ boolean $assertionsDisabled;
    String cache;
    final List<CharSequence> list;

    static {
        $assertionsDisabled = !LazyStringBuilder.class.desiredAssertionStatus();
    }

    public LazyStringBuilder() {
        this.list = new ArrayList(20);
    }

    public final LazyStringBuilder append(CharSequence csq) {
        if ($assertionsDisabled || csq != null) {
            this.list.add(csq);
            this.cache = null;
            return this;
        }
        throw new AssertionError();
    }

    public final LazyStringBuilder append(CharSequence csq, int start, int end) {
        this.list.add(csq.subSequence(start, end));
        this.cache = null;
        return this;
    }

    public final LazyStringBuilder append(char c) {
        this.list.add(Character.toString(c));
        this.cache = null;
        return this;
    }

    public int length() {
        if (this.cache != null) {
            return this.cache.length();
        }
        int length = 0;
        for (CharSequence csq : this.list) {
            length += csq.length();
        }
        return length;
    }

    public char charAt(int index) {
        if (this.cache != null) {
            return this.cache.charAt(index);
        }
        for (CharSequence csq : this.list) {
            if (index < csq.length()) {
                return csq.charAt(index);
            }
            index -= csq.length();
        }
        throw new IndexOutOfBoundsException();
    }

    public CharSequence subSequence(int start, int end) {
        return toString().subSequence(start, end);
    }

    public String toString() {
        if (this.cache == null) {
            StringBuilder sb = new StringBuilder(length());
            for (CharSequence csq : this.list) {
                sb.append(csq);
            }
            this.cache = sb.toString();
        }
        return this.cache;
    }
}
