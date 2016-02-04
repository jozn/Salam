package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.kyleduo.switchbutton.C0473R;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzh implements Creator<PolygonOptions> {
    static void zza$4c96f4fe(PolygonOptions polygonOptions, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, polygonOptions.mVersionCode);
        zzb.zzc$62107c48(parcel, 2, polygonOptions.zzaQL);
        List list = polygonOptions.zzaQM;
        if (list != null) {
            int zzG2 = zzb.zzG(parcel, 3);
            parcel.writeList(list);
            zzb.zzH(parcel, zzG2);
        }
        zzb.zza(parcel, 4, polygonOptions.zzaQg);
        zzb.zzc(parcel, 5, polygonOptions.zzaQh);
        zzb.zzc(parcel, 6, polygonOptions.zzaQi);
        zzb.zza(parcel, 7, polygonOptions.zzaQj);
        zzb.zza(parcel, 8, polygonOptions.zzaQk);
        zzb.zza(parcel, 9, polygonOptions.zzaQN);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzft(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new PolygonOptions[x0];
    }

    public final PolygonOptions zzft(Parcel parcel) {
        float f = 0.0f;
        boolean z = false;
        int zzau = zza.zzau(parcel);
        List list = null;
        List arrayList = new ArrayList();
        boolean z2 = false;
        int i = 0;
        int i2 = 0;
        float f2 = 0.0f;
        int i3 = 0;
        while (parcel.dataPosition() < zzau) {
            int readInt = parcel.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i3 = zza.zzg(parcel, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    list = zza.zzc(parcel, readInt, LatLng.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    ClassLoader classLoader = getClass().getClassLoader();
                    readInt = zza.zza(parcel, readInt);
                    int dataPosition = parcel.dataPosition();
                    if (readInt == 0) {
                        break;
                    }
                    parcel.readList(arrayList, classLoader);
                    parcel.setDataPosition(readInt + dataPosition);
                    break;
                case Logger.CONFIG /*4*/:
                    f2 = zza.zzl(parcel, readInt);
                    break;
                case Logger.FINE /*5*/:
                    i2 = zza.zzg(parcel, readInt);
                    break;
                case Logger.FINER /*6*/:
                    i = zza.zzg(parcel, readInt);
                    break;
                case Logger.FINEST /*7*/:
                    f = zza.zzl(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    z2 = zza.zzc(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    z = zza.zzc(parcel, readInt);
                    break;
                default:
                    zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new PolygonOptions(i3, list, arrayList, f2, i2, i, f, z2, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }
}
