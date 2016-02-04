package com.rokhgroup.mqtt;

import android.util.Log;
import org.eclipse.paho.android.service.MqttTraceHandler;

public final class MqttTraceCallback implements MqttTraceHandler {
    public final void traceDebug(String arg0, String arg1) {
        Log.i(arg0, arg1);
    }

    public final void traceError(String arg0, String arg1) {
        Log.e(arg0, arg1);
    }

    public final void traceException(String arg0, String arg1, Exception arg2) {
        Log.e(arg0, arg1, arg2);
    }
}
