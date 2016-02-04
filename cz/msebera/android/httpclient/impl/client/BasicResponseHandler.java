package cz.msebera.android.httpclient.impl.client;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpResponseException;
import cz.msebera.android.httpclient.util.EntityUtils;
import java.io.IOException;

public final class BasicResponseHandler extends AbstractResponseHandler<String> {
    public final /* bridge */ /* synthetic */ Object handleEntity(HttpEntity httpEntity) throws IOException {
        return EntityUtils.toString$5ae72a81(httpEntity);
    }

    public final /* bridge */ /* synthetic */ Object handleResponse(HttpResponse httpResponse) throws HttpResponseException, IOException {
        return (String) super.handleResponse(httpResponse);
    }
}
