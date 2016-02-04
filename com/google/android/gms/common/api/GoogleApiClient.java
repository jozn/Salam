package com.google.android.gms.common.api;

import com.google.android.gms.common.ConnectionResult;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public abstract class GoogleApiClient {
    private static final Set<GoogleApiClient> zzaez;

    public interface OnConnectionFailedListener {
        void onConnectionFailed(ConnectionResult connectionResult);
    }

    static {
        zzaez = Collections.newSetFromMap(new WeakHashMap());
    }

    public abstract void connect();

    public abstract void disconnect();

    public abstract void dump$ec96877();

    public abstract void registerConnectionFailedListener$40dd7b8f();

    public abstract void unregisterConnectionFailedListener$40dd7b8f();
}
