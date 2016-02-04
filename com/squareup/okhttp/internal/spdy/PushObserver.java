package com.squareup.okhttp.internal.spdy;

import java.io.IOException;
import java.util.List;
import okio.BufferedSource;

public interface PushObserver {
    public static final PushObserver CANCEL;

    /* renamed from: com.squareup.okhttp.internal.spdy.PushObserver.1 */
    static class C12141 implements PushObserver {
        C12141() {
        }

        public final boolean onRequest(int streamId, List<Header> list) {
            return true;
        }

        public final boolean onHeaders(int streamId, List<Header> list, boolean last) {
            return true;
        }

        public final boolean onData(int streamId, BufferedSource source, int byteCount, boolean last) throws IOException {
            source.skip((long) byteCount);
            return true;
        }

        public final void onReset(int streamId, ErrorCode errorCode) {
        }
    }

    boolean onData(int i, BufferedSource bufferedSource, int i2, boolean z) throws IOException;

    boolean onHeaders(int i, List<Header> list, boolean z);

    boolean onRequest(int i, List<Header> list);

    void onReset(int i, ErrorCode errorCode);

    static {
        CANCEL = new C12141();
    }
}
