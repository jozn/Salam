package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v7.appcompat.BuildConfig;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class ConnectionEvent extends zzf implements SafeParcelable {
    public static final Creator<ConnectionEvent> CREATOR;
    final int mVersionCode;
    final long zzaln;
    int zzalo;
    final String zzalp;
    final String zzalq;
    final String zzalr;
    final String zzals;
    final String zzalt;
    final String zzalu;
    final long zzalv;
    final long zzalw;
    private long zzalx;

    static {
        CREATOR = new zza();
    }

    ConnectionEvent(int versionCode, long timeMillis, int eventType, String callingProcess, String callingService, String targetProcess, String targetService, String stackTrace, String connKey, long elapsedRealtime, long heapAlloc) {
        this.mVersionCode = versionCode;
        this.zzaln = timeMillis;
        this.zzalo = eventType;
        this.zzalp = callingProcess;
        this.zzalq = callingService;
        this.zzalr = targetProcess;
        this.zzals = targetService;
        this.zzalx = -1;
        this.zzalt = stackTrace;
        this.zzalu = connKey;
        this.zzalv = elapsedRealtime;
        this.zzalw = heapAlloc;
    }

    public final int describeContents() {
        return 0;
    }

    public final int getEventType() {
        return this.zzalo;
    }

    public final long getTimeMillis() {
        return this.zzaln;
    }

    public final void writeToParcel(Parcel out, int flags) {
        zza.zza$151b04f0(this, out);
    }

    public final long zzrv() {
        return this.zzalx;
    }

    public final String zzry() {
        return "\t" + this.zzalp + MqttTopic.TOPIC_LEVEL_SEPARATOR + this.zzalq + "\t" + this.zzalr + MqttTopic.TOPIC_LEVEL_SEPARATOR + this.zzals + "\t" + (this.zzalt == null ? BuildConfig.VERSION_NAME : this.zzalt) + "\t" + this.zzalw;
    }
}
