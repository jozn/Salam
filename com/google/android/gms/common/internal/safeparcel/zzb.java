package com.google.android.gms.common.internal.safeparcel;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.internal.view.SupportMenu;
import java.util.List;

public final class zzb {
    public static int zzG(Parcel parcel, int i) {
        parcel.writeInt(SupportMenu.CATEGORY_MASK | i);
        parcel.writeInt(0);
        return parcel.dataPosition();
    }

    public static void zzH(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        int i2 = dataPosition - i;
        parcel.setDataPosition(i - 4);
        parcel.writeInt(i2);
        parcel.setDataPosition(dataPosition);
    }

    public static void zza(Parcel parcel, int i, byte b) {
        zzb(parcel, i, 4);
        parcel.writeInt(b);
    }

    public static void zza(Parcel parcel, int i, double d) {
        zzb(parcel, i, 8);
        parcel.writeDouble(d);
    }

    public static void zza(Parcel parcel, int i, float f) {
        zzb(parcel, i, 4);
        parcel.writeFloat(f);
    }

    public static void zza(Parcel parcel, int i, long j) {
        zzb(parcel, i, 8);
        parcel.writeLong(j);
    }

    public static void zza(Parcel parcel, int i, boolean z) {
        zzb(parcel, i, 4);
        parcel.writeInt(z ? 1 : 0);
    }

    private static <T extends Parcelable> void zza(Parcel parcel, T t, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(1);
        int dataPosition2 = parcel.dataPosition();
        t.writeToParcel(parcel, i);
        int dataPosition3 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition3 - dataPosition2);
        parcel.setDataPosition(dataPosition3);
    }

    public static void zza$2cfb68bf(Parcel parcel, int i, String str) {
        if (str != null) {
            int zzG = zzG(parcel, i);
            parcel.writeString(str);
            zzH(parcel, zzG);
        }
    }

    public static <T extends Parcelable> void zza$2d7953c6(Parcel parcel, int i, T[] tArr, int i2) {
        if (tArr != null) {
            int zzG = zzG(parcel, i);
            parcel.writeInt(r3);
            for (Parcelable parcelable : tArr) {
                if (parcelable == null) {
                    parcel.writeInt(0);
                } else {
                    zza(parcel, parcelable, i2);
                }
            }
            zzH(parcel, zzG);
        }
    }

    public static void zza$377a007(Parcel parcel, int i, Parcelable parcelable, int i2) {
        if (parcelable != null) {
            int zzG = zzG(parcel, i);
            parcelable.writeToParcel(parcel, i2);
            zzH(parcel, zzG);
        }
    }

    public static void zza$52910762(Parcel parcel, int i, byte[] bArr) {
        if (bArr != null) {
            int zzG = zzG(parcel, i);
            parcel.writeByteArray(bArr);
            zzH(parcel, zzG);
        }
    }

    public static void zza$cdac282(Parcel parcel, int i, IBinder iBinder) {
        if (iBinder != null) {
            int zzG = zzG(parcel, i);
            parcel.writeStrongBinder(iBinder);
            zzH(parcel, zzG);
        }
    }

    public static void zza$f7bef55(Parcel parcel, int i, Bundle bundle) {
        if (bundle != null) {
            int zzG = zzG(parcel, i);
            parcel.writeBundle(bundle);
            zzH(parcel, zzG);
        }
    }

    public static void zzb(Parcel parcel, int i, int i2) {
        if (i2 >= SupportMenu.USER_MASK) {
            parcel.writeInt(SupportMenu.CATEGORY_MASK | i);
            parcel.writeInt(i2);
            return;
        }
        parcel.writeInt((i2 << 16) | i);
    }

    public static void zzb$62107c48(Parcel parcel, int i, List<String> list) {
        if (list != null) {
            int zzG = zzG(parcel, i);
            parcel.writeStringList(list);
            zzH(parcel, zzG);
        }
    }

    public static void zzc(Parcel parcel, int i, int i2) {
        zzb(parcel, i, 4);
        parcel.writeInt(i2);
    }

    public static <T extends Parcelable> void zzc$62107c48(Parcel parcel, int i, List<T> list) {
        if (list != null) {
            int zzG = zzG(parcel, i);
            int size = list.size();
            parcel.writeInt(size);
            for (int i2 = 0; i2 < size; i2++) {
                Parcelable parcelable = (Parcelable) list.get(i2);
                if (parcelable == null) {
                    parcel.writeInt(0);
                } else {
                    zza(parcel, parcelable, 0);
                }
            }
            zzH(parcel, zzG);
        }
    }
}
