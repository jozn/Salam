package cz.msebera.android.httpclient.impl.client;

import cz.msebera.android.httpclient.params.AbstractHttpParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;

@Deprecated
public final class ClientParamsStack extends AbstractHttpParams {
    protected final HttpParams applicationParams;
    protected final HttpParams clientParams;
    protected final HttpParams overrideParams;
    protected final HttpParams requestParams;

    public ClientParamsStack(HttpParams cparams, HttpParams rparams) {
        this.applicationParams = null;
        this.clientParams = cparams;
        this.requestParams = rparams;
        this.overrideParams = null;
    }

    public final Object getParameter(String name) {
        Args.notNull(name, "Parameter name");
        Object result = null;
        if (this.overrideParams != null) {
            result = this.overrideParams.getParameter(name);
        }
        if (result == null && this.requestParams != null) {
            result = this.requestParams.getParameter(name);
        }
        if (result == null && this.clientParams != null) {
            result = this.clientParams.getParameter(name);
        }
        if (result != null || this.applicationParams == null) {
            return result;
        }
        return this.applicationParams.getParameter(name);
    }

    public final HttpParams setParameter(String name, Object value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Setting parameters in a stack is not supported.");
    }
}
