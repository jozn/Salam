package org.jivesoftware.smackx.bytestreams.ibb;

import org.jivesoftware.smackx.bytestreams.ibb.packet.Open;

public final class InBandBytestreamRequest {
    private final Open byteStreamRequest;
    private final InBandBytestreamManager manager;

    protected InBandBytestreamRequest(InBandBytestreamManager manager, Open byteStreamRequest) {
        this.manager = manager;
        this.byteStreamRequest = byteStreamRequest;
    }
}
