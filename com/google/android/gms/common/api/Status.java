package com.google.android.gms.common.api;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzw.zza;
import com.kyleduo.switchbutton.C0473R;
import java.util.Arrays;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class Status implements SafeParcelable {
    public static final Creator<Status> CREATOR;
    public static final Status zzaeX;
    public static final Status zzaeY;
    public static final Status zzaeZ;
    public static final Status zzafa;
    public static final Status zzafb;
    final PendingIntent mPendingIntent;
    final int mVersionCode;
    final int zzabx;
    final String zzadS;

    static {
        zzaeX = new Status(0);
        zzaeY = new Status(14);
        zzaeZ = new Status(8);
        zzafa = new Status(15);
        zzafb = new Status(16);
        CREATOR = new zzd();
    }

    private Status(int statusCode) {
        this(statusCode, (byte) 0);
    }

    private Status(int statusCode, byte b) {
        this(1, statusCode, null, null);
    }

    Status(int versionCode, int statusCode, String statusMessage, PendingIntent pendingIntent) {
        this.mVersionCode = versionCode;
        this.zzabx = statusCode;
        this.zzadS = statusMessage;
        this.mPendingIntent = pendingIntent;
    }

    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof Status)) {
            return false;
        }
        Status status = (Status) obj;
        return this.mVersionCode == status.mVersionCode && this.zzabx == status.zzabx && zzw.equal(this.zzadS, status.zzadS) && zzw.equal(this.mPendingIntent, status.mPendingIntent);
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.mVersionCode), Integer.valueOf(this.zzabx), this.zzadS, this.mPendingIntent});
    }

    public final String toString() {
        Object obj;
        zza zzx = zzw.zzx(this);
        String str = "statusCode";
        if (this.zzadS == null) {
            int i = this.zzabx;
            switch (i) {
                case MqttServiceConstants.NON_MQTT_EXCEPTION /*-1*/:
                    obj = "SUCCESS_CACHE";
                    break;
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
                    obj = "ERROR";
                    break;
                case C0473R.styleable.SwitchButton_animationVelocity /*14*/:
                    obj = "INTERRUPTED";
                    break;
                case C0473R.styleable.SwitchButton_radius /*15*/:
                    obj = "TIMEOUT";
                    break;
                case C0473R.styleable.SwitchButton_measureFactor /*16*/:
                    obj = "CANCELED";
                    break;
                case C0473R.styleable.SwitchButton_insetLeft /*17*/:
                    obj = "API_NOT_CONNECTED";
                    break;
                case 3000:
                    obj = "AUTH_API_INVALID_CREDENTIALS";
                    break;
                case 3001:
                    obj = "AUTH_API_ACCESS_FORBIDDEN";
                    break;
                case 3002:
                    obj = "AUTH_API_CLIENT_ERROR";
                    break;
                case 3003:
                    obj = "AUTH_API_SERVER_ERROR";
                    break;
                case 3004:
                    obj = "AUTH_TOKEN_ERROR";
                    break;
                case 3005:
                    obj = "AUTH_URL_RESOLUTION";
                    break;
                default:
                    obj = "unknown status code: " + i;
                    break;
            }
        }
        obj = this.zzadS;
        return zzx.zzg(str, obj).zzg("resolution", this.mPendingIntent).toString();
    }

    public final void writeToParcel(Parcel out, int flags) {
        zzd.zza(this, out, flags);
    }
}
