package com.google.android.gms.common.internal;

import java.util.Iterator;

public final class zzv {
    private final String separator;

    public zzv(String str) {
        this.separator = str;
    }

    private static CharSequence zzw(Object obj) {
        return obj instanceof CharSequence ? (CharSequence) obj : obj.toString();
    }

    public final StringBuilder zza(StringBuilder stringBuilder, Iterable<?> iterable) {
        Iterator it = iterable.iterator();
        if (it.hasNext()) {
            stringBuilder.append(zzw(it.next()));
            while (it.hasNext()) {
                stringBuilder.append(this.separator);
                stringBuilder.append(zzw(it.next()));
            }
        }
        return stringBuilder;
    }
}
