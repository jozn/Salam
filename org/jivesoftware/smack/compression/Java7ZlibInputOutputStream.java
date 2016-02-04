package org.jivesoftware.smack.compression;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.jivesoftware.smack.compression.XMPPInputOutputStream.FlushMethod;

public final class Java7ZlibInputOutputStream extends XMPPInputOutputStream {
    private static final Method method;
    private static final boolean supported;

    /* renamed from: org.jivesoftware.smack.compression.Java7ZlibInputOutputStream.1 */
    class C12841 extends InflaterInputStream {
        C12841(InputStream x0, Inflater x1) {
            super(x0, x1, AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY);
        }

        public final int available() throws IOException {
            if (this.inf.needsInput()) {
                return 0;
            }
            return super.available();
        }
    }

    /* renamed from: org.jivesoftware.smack.compression.Java7ZlibInputOutputStream.2 */
    class C12852 extends DeflaterOutputStream {
        final /* synthetic */ int val$flushMethodInt;

        C12852(OutputStream x0, Deflater x1, int i) {
            this.val$flushMethodInt = i;
            super(x0, x1);
        }

        public final void flush() throws IOException {
            if (Java7ZlibInputOutputStream.supported) {
                while (true) {
                    try {
                        int count = ((Integer) Java7ZlibInputOutputStream.method.invoke(this.def, new Object[]{this.buf, Integer.valueOf(0), Integer.valueOf(this.buf.length), Integer.valueOf(this.val$flushMethodInt)})).intValue();
                        if (count != 0) {
                            this.out.write(this.buf, 0, count);
                        } else {
                            super.flush();
                            return;
                        }
                    } catch (IllegalArgumentException e) {
                        throw new IOException("Can't flush");
                    } catch (IllegalAccessException e2) {
                        throw new IOException("Can't flush");
                    } catch (InvocationTargetException e3) {
                        throw new IOException("Can't flush");
                    }
                }
            }
            super.flush();
        }
    }

    static {
        boolean z = true;
        Method m = null;
        try {
            m = Deflater.class.getMethod("deflate", new Class[]{byte[].class, Integer.TYPE, Integer.TYPE, Integer.TYPE});
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e2) {
        }
        method = m;
        if (m == null) {
            z = false;
        }
        supported = z;
    }

    public Java7ZlibInputOutputStream() {
        this.compressionMethod = "zlib";
    }

    public final boolean isSupported() {
        return supported;
    }

    public final InputStream getInputStream(InputStream inputStream) {
        return new C12841(inputStream, new Inflater());
    }

    public final OutputStream getOutputStream(OutputStream outputStream) {
        int flushMethodInt;
        if (flushMethod$2c003870 == FlushMethod.SYNC_FLUSH$2c003870) {
            flushMethodInt = 2;
        } else {
            flushMethodInt = 3;
        }
        return new C12852(outputStream, new Deflater(-1), flushMethodInt);
    }
}
