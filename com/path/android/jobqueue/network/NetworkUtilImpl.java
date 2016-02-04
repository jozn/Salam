package com.path.android.jobqueue.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.path.android.jobqueue.network.NetworkEventProvider.Listener;

public final class NetworkUtilImpl implements NetworkEventProvider, NetworkUtil {
    Listener listener;

    /* renamed from: com.path.android.jobqueue.network.NetworkUtilImpl.1 */
    class C05011 extends BroadcastReceiver {
        C05011() {
        }

        public final void onReceive(Context context, Intent intent) {
            if (NetworkUtilImpl.this.listener != null) {
                NetworkUtilImpl.this.listener.onNetworkChange(NetworkUtilImpl.this.isConnected(context));
            }
        }
    }

    public NetworkUtilImpl(Context context) {
        context.getApplicationContext().registerReceiver(new C05011(), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public final boolean isConnected(Context context) {
        NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public final void setListener(Listener listener) {
        this.listener = listener;
    }
}
