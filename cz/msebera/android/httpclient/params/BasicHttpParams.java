package cz.msebera.android.httpclient.params;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public class BasicHttpParams extends AbstractHttpParams implements Serializable, Cloneable {
    private final Map<String, Object> parameters;

    public BasicHttpParams() {
        this.parameters = new ConcurrentHashMap();
    }

    public Object getParameter(String name) {
        return this.parameters.get(name);
    }

    public HttpParams setParameter(String name, Object value) {
        if (name != null) {
            if (value != null) {
                this.parameters.put(name, value);
            } else {
                this.parameters.remove(name);
            }
        }
        return this;
    }

    public Object clone() throws CloneNotSupportedException {
        BasicHttpParams clone = (BasicHttpParams) super.clone();
        for (Entry entry : this.parameters.entrySet()) {
            clone.setParameter((String) entry.getKey(), entry.getValue());
        }
        return clone;
    }
}
