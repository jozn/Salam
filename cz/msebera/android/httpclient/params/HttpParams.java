package cz.msebera.android.httpclient.params;

@Deprecated
public interface HttpParams {
    boolean getBooleanParameter(String str, boolean z);

    int getIntParameter(String str, int i);

    long getLongParameter$505cfb67(String str);

    Object getParameter(String str);

    HttpParams setBooleanParameter$59a1668b(String str);

    HttpParams setIntParameter(String str, int i);

    HttpParams setLongParameter(String str, long j);

    HttpParams setParameter(String str, Object obj);
}
