package com.google.android.gms.internal;

import com.kyleduo.switchbutton.C0473R;
import java.io.IOException;
import java.lang.reflect.Array;

public final class zztf<M extends zzte<M>, T> {
    public final int tag;
    protected final int type;
    protected final Class<T> zzbpR;
    protected final boolean zzbpS;

    private int zzaa(Object obj) {
        int zzmG = zztn.zzmG(this.tag);
        switch (this.type) {
            case C0473R.styleable.SwitchButton_onColor /*10*/:
                return (zztd.zzmx(zzmG) * 2) + ((zztk) obj).getSerializedSize();
            case C0473R.styleable.SwitchButton_offColor /*11*/:
                return zztd.zzc(zzmG, (zztk) obj);
            default:
                throw new IllegalArgumentException("Unknown type " + this.type);
        }
    }

    private void zzb(Object obj, zztd com_google_android_gms_internal_zztd) {
        try {
            com_google_android_gms_internal_zztd.zzmy(this.tag);
            switch (this.type) {
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    zztk com_google_android_gms_internal_zztk = (zztk) obj;
                    int zzmG = zztn.zzmG(this.tag);
                    com_google_android_gms_internal_zztk.writeTo(com_google_android_gms_internal_zztd);
                    com_google_android_gms_internal_zztd.zzK(zzmG, 4);
                    return;
                case C0473R.styleable.SwitchButton_offColor /*11*/:
                    com_google_android_gms_internal_zztd.zzc((zztk) obj);
                    return;
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalStateException(e);
    }

    final int zzY(Object obj) {
        int i = 0;
        if (!this.zzbpS) {
            return zzaa(obj);
        }
        int length = Array.getLength(obj);
        for (int i2 = 0; i2 < length; i2++) {
            if (Array.get(obj, i2) != null) {
                i += zzaa(Array.get(obj, i2));
            }
        }
        return i;
    }

    final void zza(Object obj, zztd com_google_android_gms_internal_zztd) throws IOException {
        if (this.zzbpS) {
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                Object obj2 = Array.get(obj, i);
                if (obj2 != null) {
                    zzb(obj2, com_google_android_gms_internal_zztd);
                }
            }
            return;
        }
        zzb(obj, com_google_android_gms_internal_zztd);
    }
}
