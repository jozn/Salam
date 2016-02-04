package com.shamchat.androidclient.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.RosterProvider;

public class ZaminBroadcastReceiver extends BroadcastReceiver {
    private static int networkType;

    static {
        networkType = -1;
    }

    public static void initNetworkStatus(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        networkType = -1;
        if (networkInfo != null) {
            Log.d("com.miiny.BroadcastReceiver", "Init: ACTIVE NetworkInfo: " + networkInfo.toString());
            if (networkInfo.isConnected()) {
                networkType = networkInfo.getType();
            }
        }
        Log.d("com.miiny.BroadcastReceiver", "initNetworkStatus -> " + networkType);
    }

    public void onReceive(Context context, Intent intent) {
        boolean z = true;
        Log.d("com.miiny.BroadcastReceiver", "onReceive " + intent);
        if (intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")) {
            Log.d("com.miiny.BroadcastReceiver", "System shutdown, stopping minny.");
            context.stopService(new Intent(context, XMPPService.class));
        } else if ((intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE") || intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) && PreferenceManager.getDefaultSharedPreferences(context).getBoolean("connstartup", false)) {
            boolean isConnected;
            Intent xmppServiceIntent = new Intent(context, XMPPService.class);
            NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()) {
                isConnected = false;
            } else {
                isConnected = true;
            }
            if (networkType == -1) {
                z = false;
            }
            if (z && !isConnected) {
                Log.d("com.miiny.BroadcastReceiver", "we got disconnected");
                networkType = -1;
                xmppServiceIntent.setAction(MqttServiceConstants.DISCONNECT_ACTION);
                SHAMChatApplication.getMyApplicationContext().getContentResolver().notifyChange(RosterProvider.CONTENT_URI, null);
            } else if (isConnected && networkInfo.getType() != networkType) {
                Log.d("com.miiny.BroadcastReceiver", "we got (re)connected: " + networkInfo.toString());
                networkType = networkInfo.getType();
                xmppServiceIntent.setAction("reconnect");
            } else if (isConnected && networkInfo.getType() == networkType) {
                Log.d("com.miiny.BroadcastReceiver", "we stay connected, sending a ping");
                xmppServiceIntent.setAction("ping");
            } else {
                return;
            }
            context.startService(xmppServiceIntent);
        }
    }
}
