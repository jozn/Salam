package com.google.android.gms.common;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzw.zza;
import com.kyleduo.switchbutton.C0473R;
import com.shamchat.activity.AddFavoriteTextActivity;
import java.util.Arrays;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class ConnectionResult implements SafeParcelable {
    public static final Creator<ConnectionResult> CREATOR;
    public static final ConnectionResult zzadR;
    public final PendingIntent mPendingIntent;
    final int mVersionCode;
    public final int zzabx;
    final String zzadS;

    static {
        zzadR = new ConnectionResult();
        CREATOR = new zzb();
    }

    private ConnectionResult() {
        this(0, null, (byte) 0);
    }

    ConnectionResult(int versionCode, int statusCode, PendingIntent pendingIntent, String statusMessage) {
        this.mVersionCode = versionCode;
        this.zzabx = statusCode;
        this.mPendingIntent = pendingIntent;
        this.zzadS = statusMessage;
    }

    public ConnectionResult(int statusCode, PendingIntent pendingIntent) {
        this(statusCode, pendingIntent, (byte) 0);
    }

    private ConnectionResult(int statusCode, PendingIntent pendingIntent, byte b) {
        this(1, statusCode, pendingIntent, null);
    }

    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ConnectionResult)) {
            return false;
        }
        ConnectionResult connectionResult = (ConnectionResult) o;
        return this.zzabx == connectionResult.zzabx && zzw.equal(this.mPendingIntent, connectionResult.mPendingIntent) && zzw.equal(this.zzadS, connectionResult.zzadS);
    }

    public final boolean hasResolution() {
        return (this.zzabx == 0 || this.mPendingIntent == null) ? false : true;
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.zzabx), this.mPendingIntent, this.zzadS});
    }

    public final String toString() {
        Object obj;
        zza zzx = zzw.zzx(this);
        String str = "statusCode";
        int i = this.zzabx;
        switch (i) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                obj = "SUCCESS";
                break;
            case Logger.SEVERE /*1*/:
                obj = "SERVICE_MISSING";
                break;
            case Logger.WARNING /*2*/:
                obj = "SERVICE_VERSION_UPDATE_REQUIRED";
                break;
            case Logger.INFO /*3*/:
                obj = "SERVICE_DISABLED";
                break;
            case Logger.CONFIG /*4*/:
                obj = "SIGN_IN_REQUIRED";
                break;
            case Logger.FINE /*5*/:
                obj = "INVALID_ACCOUNT";
                break;
            case Logger.FINER /*6*/:
                obj = "RESOLUTION_REQUIRED";
                break;
            case Logger.FINEST /*7*/:
                obj = "NETWORK_ERROR";
                break;
            case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                obj = "INTERNAL_ERROR";
                break;
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                obj = "SERVICE_INVALID";
                break;
            case C0473R.styleable.SwitchButton_onColor /*10*/:
                obj = "DEVELOPER_ERROR";
                break;
            case C0473R.styleable.SwitchButton_offColor /*11*/:
                obj = "LICENSE_CHECK_FAILED";
                break;
            case C0473R.styleable.SwitchButton_thumbPressedColor /*13*/:
                obj = "CANCELED";
                break;
            case C0473R.styleable.SwitchButton_animationVelocity /*14*/:
                obj = "TIMEOUT";
                break;
            case C0473R.styleable.SwitchButton_radius /*15*/:
                obj = "INTERRUPTED";
                break;
            case C0473R.styleable.SwitchButton_measureFactor /*16*/:
                obj = "API_UNAVAILABLE";
                break;
            case C0473R.styleable.SwitchButton_insetLeft /*17*/:
                obj = "SIGN_IN_FAILED";
                break;
            case C0473R.styleable.SwitchButton_insetRight /*18*/:
                obj = "SERVICE_UPDATING";
                break;
            case C0473R.styleable.SwitchButton_insetTop /*19*/:
                obj = "SERVICE_MISSING_PERMISSION";
                break;
            default:
                obj = "UNKNOWN_ERROR_CODE(" + i + ")";
                break;
        }
        return zzx.zzg(str, obj).zzg("resolution", this.mPendingIntent).zzg(AddFavoriteTextActivity.EXTRA_MESSAGE, this.zzadS).toString();
    }

    public final void writeToParcel(Parcel out, int flags) {
        zzb.zza(this, out, flags);
    }
}
