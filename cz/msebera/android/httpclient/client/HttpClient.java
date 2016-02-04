package cz.msebera.android.httpclient.client;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import java.io.IOException;

public interface HttpClient {
    HttpResponse execute(HttpUriRequest httpUriRequest) throws IOException, ClientProtocolException;

    <T> T execute(HttpUriRequest httpUriRequest, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException;
}
