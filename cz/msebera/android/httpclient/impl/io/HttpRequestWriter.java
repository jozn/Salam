package cz.msebera.android.httpclient.impl.io;

import cz.msebera.android.httpclient.HttpMessage;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.io.SessionOutputBuffer;
import java.io.IOException;

@Deprecated
public final class HttpRequestWriter extends AbstractMessageWriter<HttpRequest> {
    protected final /* bridge */ /* synthetic */ void writeHeadLine(HttpMessage httpMessage) throws IOException {
        this.lineFormatter.formatRequestLine(this.lineBuf, ((HttpRequest) httpMessage).getRequestLine());
        this.sessionBuffer.writeLine(this.lineBuf);
    }

    public HttpRequestWriter(SessionOutputBuffer buffer) {
        super(buffer);
    }
}
