package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zze implements Creator<SafeParcelResponse> {
    static void zza(SafeParcelResponse safeParcelResponse, Parcel parcel, int i) {
        Parcelable parcelable;
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, safeParcelResponse.mVersionCode);
        Parcel zzrn = safeParcelResponse.zzrn();
        if (zzrn != null) {
            int zzG2 = zzb.zzG(parcel, 2);
            parcel.appendFrom(zzrn, 0, zzrn.dataSize());
            zzb.zzH(parcel, zzG2);
        }
        switch (safeParcelResponse.zzalk) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                parcelable = null;
                break;
            case Logger.SEVERE /*1*/:
                parcelable = safeParcelResponse.zzalc;
                break;
            case Logger.WARNING /*2*/:
                parcelable = safeParcelResponse.zzalc;
                break;
            default:
                throw new IllegalStateException("Invalid creation type: " + safeParcelResponse.zzalk);
        }
        zzb.zza$377a007(parcel, 3, parcelable, i);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        FieldMappingDictionary fieldMappingDictionary = null;
        int zzau = zza.zzau(x0);
        int i = 0;
        Parcel parcel = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    parcel = zza.zzE(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    fieldMappingDictionary = (FieldMappingDictionary) zza.zza(x0, readInt, FieldMappingDictionary.CREATOR);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new SafeParcelResponse(i, parcel, fieldMappingDictionary);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new SafeParcelResponse[x0];
    }
}
