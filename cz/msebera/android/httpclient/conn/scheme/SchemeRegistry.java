package cz.msebera.android.httpclient.conn.scheme;

import cz.msebera.android.httpclient.util.Args;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public final class SchemeRegistry {
    private final ConcurrentHashMap<String, Scheme> registeredSchemes;

    public SchemeRegistry() {
        this.registeredSchemes = new ConcurrentHashMap();
    }

    public final Scheme getScheme(String name) {
        Args.notNull(name, "Scheme name");
        Scheme found = (Scheme) this.registeredSchemes.get(name);
        if (found != null) {
            return found;
        }
        throw new IllegalStateException("Scheme '" + name + "' not registered.");
    }

    public final Scheme register(Scheme sch) {
        Args.notNull(sch, "Scheme");
        return (Scheme) this.registeredSchemes.put(sch.name, sch);
    }
}
