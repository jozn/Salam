package cz.msebera.android.httpclient.conn;

import java.io.IOException;
import java.io.InputStream;

public interface EofSensorWatcher {
    boolean eofDetected(InputStream inputStream) throws IOException;

    boolean streamAbort$71225a42() throws IOException;

    boolean streamClosed(InputStream inputStream) throws IOException;
}
