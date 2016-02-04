package cz.msebera.android.httpclient;

public interface HttpResponse extends HttpMessage {
    HttpEntity getEntity();

    StatusLine getStatusLine();

    void setEntity(HttpEntity httpEntity);
}
