package com.squareup.okhttp.internal;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import okio.Base64;
import okio.ByteString;
import okio.Source;

public final class Util {
    public static final byte[] EMPTY_BYTE_ARRAY;
    public static final String[] EMPTY_STRING_ARRAY;
    public static final Charset UTF_8;

    /* renamed from: com.squareup.okhttp.internal.Util.1 */
    static class C12031 implements ThreadFactory {
        final /* synthetic */ boolean val$daemon;
        final /* synthetic */ String val$name;

        C12031(String str, boolean z) {
            this.val$name = str;
            this.val$daemon = z;
        }

        public final Thread newThread(Runnable runnable) {
            Thread result = new Thread(runnable, this.val$name);
            result.setDaemon(this.val$daemon);
            return result;
        }
    }

    public static boolean skipAll(okio.Source r12, int r13, java.util.concurrent.TimeUnit r14) throws java.io.IOException {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x006a in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:58)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
        /*
        r6 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r0 = java.lang.System.nanoTime();
        r5 = r12.timeout();
        r5 = r5.hasDeadline();
        if (r5 == 0) goto L_0x0051;
    L_0x0013:
        r5 = r12.timeout();
        r8 = r5.deadlineNanoTime();
        r2 = r8 - r0;
    L_0x001d:
        r5 = r12.timeout();
        r8 = (long) r13;
        r8 = r14.toNanos(r8);
        r8 = java.lang.Math.min(r2, r8);
        r8 = r8 + r0;
        r5.deadlineNanoTime(r8);
        r4 = new okio.Buffer;	 Catch:{ InterruptedIOException -> 0x0043, all -> 0x0074 }
        r4.<init>();	 Catch:{ InterruptedIOException -> 0x0043, all -> 0x0074 }
    L_0x0033:
        r8 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;	 Catch:{ InterruptedIOException -> 0x0043, all -> 0x0074 }
        r8 = r12.read(r4, r8);	 Catch:{ InterruptedIOException -> 0x0043, all -> 0x0074 }
        r10 = -1;	 Catch:{ InterruptedIOException -> 0x0043, all -> 0x0074 }
        r5 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));	 Catch:{ InterruptedIOException -> 0x0043, all -> 0x0074 }
        if (r5 == 0) goto L_0x0053;	 Catch:{ InterruptedIOException -> 0x0043, all -> 0x0074 }
    L_0x003f:
        r4.clear();	 Catch:{ InterruptedIOException -> 0x0043, all -> 0x0074 }
        goto L_0x0033;
    L_0x0043:
        r5 = move-exception;
        r5 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
        if (r5 != 0) goto L_0x006a;
    L_0x0048:
        r5 = r12.timeout();
        r5.clearDeadline();
    L_0x004f:
        r5 = 0;
    L_0x0050:
        return r5;
    L_0x0051:
        r2 = r6;
        goto L_0x001d;
    L_0x0053:
        r5 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
        if (r5 != 0) goto L_0x0060;
    L_0x0057:
        r5 = r12.timeout();
        r5.clearDeadline();
    L_0x005e:
        r5 = 1;
        goto L_0x0050;
    L_0x0060:
        r5 = r12.timeout();
        r6 = r0 + r2;
        r5.deadlineNanoTime(r6);
        goto L_0x005e;
    L_0x006a:
        r5 = r12.timeout();
        r6 = r0 + r2;
        r5.deadlineNanoTime(r6);
        goto L_0x004f;
    L_0x0074:
        r5 = move-exception;
        r6 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
        if (r6 != 0) goto L_0x0081;
    L_0x0079:
        r6 = r12.timeout();
        r6.clearDeadline();
    L_0x0080:
        throw r5;
    L_0x0081:
        r6 = r12.timeout();
        r8 = r0 + r2;
        r6.deadlineNanoTime(r8);
        goto L_0x0080;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.internal.Util.skipAll(okio.Source, int, java.util.concurrent.TimeUnit):boolean");
    }

    static {
        EMPTY_BYTE_ARRAY = new byte[0];
        EMPTY_STRING_ARRAY = new String[0];
        UTF_8 = Charset.forName("UTF-8");
    }

    private Util() {
    }

    public static int getEffectivePort(URI uri) {
        return getEffectivePort(uri.getScheme(), uri.getPort());
    }

    public static int getEffectivePort(URL url) {
        return getEffectivePort(url.getProtocol(), url.getPort());
    }

    private static int getEffectivePort(String scheme, int specifiedPort) {
        return specifiedPort != -1 ? specifiedPort : getDefaultPort(scheme);
    }

    public static int getDefaultPort(String protocol) {
        if ("http".equals(protocol)) {
            return 80;
        }
        if ("https".equals(protocol)) {
            return 443;
        }
        return -1;
    }

    public static void checkOffsetAndCount(long arrayLength, long offset, long count) {
        if ((offset | count) < 0 || offset > arrayLength || arrayLength - offset < count) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
            }
        }
    }

    public static void closeQuietly(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
            }
        }
    }

    public static void closeQuietly(ServerSocket serverSocket) {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
            }
        }
    }

    public static void closeAll(Closeable a, Closeable b) throws IOException {
        Throwable thrown = null;
        try {
            a.close();
        } catch (Throwable th) {
            thrown = th;
        }
        try {
            b.close();
        } catch (Throwable e) {
            if (thrown == null) {
                thrown = e;
            }
        }
        if (thrown != null) {
            if (thrown instanceof IOException) {
                throw ((IOException) thrown);
            } else if (thrown instanceof RuntimeException) {
                throw ((RuntimeException) thrown);
            } else if (thrown instanceof Error) {
                throw ((Error) thrown);
            } else {
                throw new AssertionError(thrown);
            }
        }
    }

    public static boolean discard(Source source, int timeout, TimeUnit timeUnit) {
        try {
            return skipAll(source, timeout, timeUnit);
        } catch (IOException e) {
            return false;
        }
    }

    public static String md5Hex(String s) {
        Exception e;
        try {
            return ByteString.of(MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8"))).hex();
        } catch (NoSuchAlgorithmException e2) {
            e = e2;
            throw new AssertionError(e);
        } catch (UnsupportedEncodingException e3) {
            e = e3;
            throw new AssertionError(e);
        }
    }

    public static String shaBase64(String s) {
        Exception e;
        try {
            return Base64.encode(ByteString.of(MessageDigest.getInstance("SHA-1").digest(s.getBytes("UTF-8"))).data);
        } catch (NoSuchAlgorithmException e2) {
            e = e2;
            throw new AssertionError(e);
        } catch (UnsupportedEncodingException e3) {
            e = e3;
            throw new AssertionError(e);
        }
    }

    public static ByteString sha1(ByteString s) {
        try {
            return ByteString.of(MessageDigest.getInstance("SHA-1").digest(s.toByteArray()));
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    public static <T> List<T> immutableList(List<T> list) {
        return Collections.unmodifiableList(new ArrayList(list));
    }

    public static <T> List<T> immutableList(T... elements) {
        return Collections.unmodifiableList(Arrays.asList((Object[]) elements.clone()));
    }

    public static <K, V> Map<K, V> immutableMap(Map<K, V> map) {
        return Collections.unmodifiableMap(new LinkedHashMap(map));
    }

    public static ThreadFactory threadFactory(String name, boolean daemon) {
        return new C12031(name, daemon);
    }

    public static <T> T[] intersect(Class<T> arrayType, T[] first, T[] second) {
        List<T> result = intersect(first, second);
        return result.toArray((Object[]) Array.newInstance(arrayType, result.size()));
    }

    private static <T> List<T> intersect(T[] first, T[] second) {
        List<T> result = new ArrayList();
        for (T a : first) {
            for (T b : second) {
                if (a.equals(b)) {
                    result.add(b);
                    break;
                }
            }
        }
        return result;
    }
}
