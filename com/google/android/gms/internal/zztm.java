package com.google.android.gms.internal;

import java.util.Arrays;

final class zztm {
    final int tag;
    final byte[] zzbqc;

    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zztm)) {
            return false;
        }
        zztm com_google_android_gms_internal_zztm = (zztm) o;
        return this.tag == com_google_android_gms_internal_zztm.tag && Arrays.equals(this.zzbqc, com_google_android_gms_internal_zztm.zzbqc);
    }

    public final int hashCode() {
        return ((this.tag + 527) * 31) + Arrays.hashCode(this.zzbqc);
    }
}
