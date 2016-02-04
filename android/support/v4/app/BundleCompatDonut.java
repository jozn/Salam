package android.support.v4.app;

import java.lang.reflect.Method;

class BundleCompatDonut {
    private static final String TAG = "BundleCompatDonut";
    private static Method sGetIBinderMethod;
    private static boolean sGetIBinderMethodFetched;
    private static Method sPutIBinderMethod;
    private static boolean sPutIBinderMethodFetched;

    BundleCompatDonut() {
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.os.IBinder getBinder(android.os.Bundle r8, java.lang.String r9) {
        /*
        r2 = 0;
        r7 = 1;
        r1 = sGetIBinderMethodFetched;
        if (r1 != 0) goto L_0x001e;
    L_0x0006:
        r1 = android.os.Bundle.class;
        r3 = "getIBinder";
        r4 = 1;
        r4 = new java.lang.Class[r4];	 Catch:{ NoSuchMethodException -> 0x0031 }
        r5 = 0;
        r6 = java.lang.String.class;
        r4[r5] = r6;	 Catch:{ NoSuchMethodException -> 0x0031 }
        r1 = r1.getMethod(r3, r4);	 Catch:{ NoSuchMethodException -> 0x0031 }
        sGetIBinderMethod = r1;	 Catch:{ NoSuchMethodException -> 0x0031 }
        r3 = 1;
        r1.setAccessible(r3);	 Catch:{ NoSuchMethodException -> 0x0031 }
    L_0x001c:
        sGetIBinderMethodFetched = r7;
    L_0x001e:
        r1 = sGetIBinderMethod;
        if (r1 == 0) goto L_0x0044;
    L_0x0022:
        r1 = sGetIBinderMethod;	 Catch:{ InvocationTargetException -> 0x0048, IllegalAccessException -> 0x003a, IllegalArgumentException -> 0x0046 }
        r3 = 1;
        r3 = new java.lang.Object[r3];	 Catch:{ InvocationTargetException -> 0x0048, IllegalAccessException -> 0x003a, IllegalArgumentException -> 0x0046 }
        r4 = 0;
        r3[r4] = r9;	 Catch:{ InvocationTargetException -> 0x0048, IllegalAccessException -> 0x003a, IllegalArgumentException -> 0x0046 }
        r1 = r1.invoke(r8, r3);	 Catch:{ InvocationTargetException -> 0x0048, IllegalAccessException -> 0x003a, IllegalArgumentException -> 0x0046 }
        r1 = (android.os.IBinder) r1;	 Catch:{ InvocationTargetException -> 0x0048, IllegalAccessException -> 0x003a, IllegalArgumentException -> 0x0046 }
    L_0x0030:
        return r1;
    L_0x0031:
        r0 = move-exception;
        r1 = "BundleCompatDonut";
        r3 = "Failed to retrieve getIBinder method";
        android.util.Log.i(r1, r3, r0);
        goto L_0x001c;
    L_0x003a:
        r0 = move-exception;
    L_0x003b:
        r1 = "BundleCompatDonut";
        r3 = "Failed to invoke getIBinder via reflection";
        android.util.Log.i(r1, r3, r0);
        sGetIBinderMethod = r2;
    L_0x0044:
        r1 = r2;
        goto L_0x0030;
    L_0x0046:
        r0 = move-exception;
        goto L_0x003b;
    L_0x0048:
        r0 = move-exception;
        goto L_0x003b;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.BundleCompatDonut.getBinder(android.os.Bundle, java.lang.String):android.os.IBinder");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void putBinder(android.os.Bundle r7, java.lang.String r8, android.os.IBinder r9) {
        /*
        r6 = 1;
        r1 = sPutIBinderMethodFetched;
        if (r1 != 0) goto L_0x0022;
    L_0x0005:
        r1 = android.os.Bundle.class;
        r2 = "putIBinder";
        r3 = 2;
        r3 = new java.lang.Class[r3];	 Catch:{ NoSuchMethodException -> 0x0035 }
        r4 = 0;
        r5 = java.lang.String.class;
        r3[r4] = r5;	 Catch:{ NoSuchMethodException -> 0x0035 }
        r4 = 1;
        r5 = android.os.IBinder.class;
        r3[r4] = r5;	 Catch:{ NoSuchMethodException -> 0x0035 }
        r1 = r1.getMethod(r2, r3);	 Catch:{ NoSuchMethodException -> 0x0035 }
        sPutIBinderMethod = r1;	 Catch:{ NoSuchMethodException -> 0x0035 }
        r2 = 1;
        r1.setAccessible(r2);	 Catch:{ NoSuchMethodException -> 0x0035 }
    L_0x0020:
        sPutIBinderMethodFetched = r6;
    L_0x0022:
        r1 = sPutIBinderMethod;
        if (r1 == 0) goto L_0x0034;
    L_0x0026:
        r1 = sPutIBinderMethod;	 Catch:{ InvocationTargetException -> 0x004c, IllegalAccessException -> 0x003e, IllegalArgumentException -> 0x004a }
        r2 = 2;
        r2 = new java.lang.Object[r2];	 Catch:{ InvocationTargetException -> 0x004c, IllegalAccessException -> 0x003e, IllegalArgumentException -> 0x004a }
        r3 = 0;
        r2[r3] = r8;	 Catch:{ InvocationTargetException -> 0x004c, IllegalAccessException -> 0x003e, IllegalArgumentException -> 0x004a }
        r3 = 1;
        r2[r3] = r9;	 Catch:{ InvocationTargetException -> 0x004c, IllegalAccessException -> 0x003e, IllegalArgumentException -> 0x004a }
        r1.invoke(r7, r2);	 Catch:{ InvocationTargetException -> 0x004c, IllegalAccessException -> 0x003e, IllegalArgumentException -> 0x004a }
    L_0x0034:
        return;
    L_0x0035:
        r0 = move-exception;
        r1 = "BundleCompatDonut";
        r2 = "Failed to retrieve putIBinder method";
        android.util.Log.i(r1, r2, r0);
        goto L_0x0020;
    L_0x003e:
        r0 = move-exception;
    L_0x003f:
        r1 = "BundleCompatDonut";
        r2 = "Failed to invoke putIBinder via reflection";
        android.util.Log.i(r1, r2, r0);
        r1 = 0;
        sPutIBinderMethod = r1;
        goto L_0x0034;
    L_0x004a:
        r0 = move-exception;
        goto L_0x003f;
    L_0x004c:
        r0 = move-exception;
        goto L_0x003f;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.BundleCompatDonut.putBinder(android.os.Bundle, java.lang.String, android.os.IBinder):void");
    }
}
