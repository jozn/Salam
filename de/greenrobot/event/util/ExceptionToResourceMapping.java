package de.greenrobot.event.util;

import java.util.Map;

public final class ExceptionToResourceMapping {
    public final Map<Class<? extends Throwable>, Integer> throwableToMsgIdMap;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Integer mapThrowable(java.lang.Throwable r12) {
        /*
        r11 = this;
        r7 = 0;
        r2 = r12;
        r0 = 20;
    L_0x0004:
        r8 = r2.getClass();
        r3 = r11.throwableToMsgIdMap;
        r3 = r3.get(r8);
        r3 = (java.lang.Integer) r3;
        if (r3 != 0) goto L_0x0070;
    L_0x0012:
        r4 = r11.throwableToMsgIdMap;
        r4 = r4.entrySet();
        r9 = r4.iterator();
        r5 = r7;
        r6 = r3;
    L_0x001e:
        r3 = r9.hasNext();
        if (r3 == 0) goto L_0x0047;
    L_0x0024:
        r3 = r9.next();
        r3 = (java.util.Map.Entry) r3;
        r4 = r3.getKey();
        r4 = (java.lang.Class) r4;
        r10 = r4.isAssignableFrom(r8);
        if (r10 == 0) goto L_0x006d;
    L_0x0036:
        if (r5 == 0) goto L_0x003e;
    L_0x0038:
        r10 = r5.isAssignableFrom(r4);
        if (r10 == 0) goto L_0x006d;
    L_0x003e:
        r3 = r3.getValue();
        r3 = (java.lang.Integer) r3;
    L_0x0044:
        r5 = r4;
        r6 = r3;
        goto L_0x001e;
    L_0x0047:
        r1 = r6;
    L_0x0048:
        if (r1 == 0) goto L_0x004b;
    L_0x004a:
        return r1;
    L_0x004b:
        r2 = r2.getCause();
        r0 = r0 + -1;
        if (r0 <= 0) goto L_0x0057;
    L_0x0053:
        if (r2 == r12) goto L_0x0057;
    L_0x0055:
        if (r2 != 0) goto L_0x0004;
    L_0x0057:
        r3 = de.greenrobot.event.EventBus.TAG;
        r4 = new java.lang.StringBuilder;
        r5 = "No specific message ressource ID found for ";
        r4.<init>(r5);
        r4 = r4.append(r12);
        r4 = r4.toString();
        android.util.Log.d(r3, r4);
        r1 = r7;
        goto L_0x004a;
    L_0x006d:
        r4 = r5;
        r3 = r6;
        goto L_0x0044;
    L_0x0070:
        r1 = r3;
        goto L_0x0048;
        */
        throw new UnsupportedOperationException("Method not decompiled: de.greenrobot.event.util.ExceptionToResourceMapping.mapThrowable(java.lang.Throwable):java.lang.Integer");
    }
}
