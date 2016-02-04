package cz.msebera.android.httpclient.client;

import cz.msebera.android.httpclient.ProtocolException;
import java.net.URI;

@Deprecated
public interface RedirectHandler {
    URI getLocationURI$3b87f9f6() throws ProtocolException;

    boolean isRedirectRequested$22649b72();
}
