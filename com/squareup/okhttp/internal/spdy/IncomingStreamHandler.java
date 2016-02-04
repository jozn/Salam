package com.squareup.okhttp.internal.spdy;

import java.io.IOException;

public interface IncomingStreamHandler {
    public static final IncomingStreamHandler REFUSE_INCOMING_STREAMS;

    /* renamed from: com.squareup.okhttp.internal.spdy.IncomingStreamHandler.1 */
    static class C12111 implements IncomingStreamHandler {
        C12111() {
        }

        public final void receive(SpdyStream stream) throws IOException {
            stream.close(ErrorCode.REFUSED_STREAM);
        }
    }

    void receive(SpdyStream spdyStream) throws IOException;

    static {
        REFUSE_INCOMING_STREAMS = new C12111();
    }
}
