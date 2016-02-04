package org.jivesoftware.smackx.bytestreams.ibb;

import java.io.InputStream;
import java.io.OutputStream;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.bytestreams.ibb.packet.Close;

public final class InBandBytestreamSession {
    final XMPPConnection connection;
    private IBBInputStream inputStream;
    private IBBOutputStream outputStream;

    private abstract class IBBInputStream extends InputStream {
        private final PacketListener dataPacketListener;
        private boolean isClosed;
        final /* synthetic */ InBandBytestreamSession this$0;

        static /* synthetic */ void access$500(IBBInputStream x0) {
            if (!x0.isClosed) {
                x0.isClosed = true;
            }
        }
    }

    private abstract class IBBOutputStream extends OutputStream {
        protected boolean isClosed;

        protected final void closeInternal$1385ff() {
            if (!this.isClosed) {
                this.isClosed = true;
            }
        }
    }

    protected final void closeByPeer(Close closeRequest) throws NotConnectedException {
        IBBInputStream.access$500(this.inputStream);
        this.inputStream.this$0.connection.removePacketListener(this.inputStream.dataPacketListener);
        this.outputStream.closeInternal$1385ff();
        this.connection.sendPacket(IQ.createResultIQ(closeRequest));
    }
}
