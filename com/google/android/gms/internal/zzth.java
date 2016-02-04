package com.google.android.gms.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class zzth implements Cloneable {
    private zztf<?, ?> zzbpX;
    private Object zzbpY;
    private List<zztm> zzbpZ;

    zzth() {
        this.zzbpZ = new ArrayList();
    }

    private byte[] toByteArray() throws IOException {
        byte[] bArr = new byte[zzz()];
        writeTo(new zztd(bArr, bArr.length));
        return bArr;
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        return zzHB();
    }

    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzth)) {
            return false;
        }
        zzth com_google_android_gms_internal_zzth = (zzth) o;
        if (this.zzbpY == null || com_google_android_gms_internal_zzth.zzbpY == null) {
            if (this.zzbpZ != null && com_google_android_gms_internal_zzth.zzbpZ != null) {
                return this.zzbpZ.equals(com_google_android_gms_internal_zzth.zzbpZ);
            }
            try {
                return Arrays.equals(toByteArray(), com_google_android_gms_internal_zzth.toByteArray());
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        } else if (this.zzbpX != com_google_android_gms_internal_zzth.zzbpX) {
            return false;
        } else {
            if (!this.zzbpX.zzbpR.isArray()) {
                return this.zzbpY.equals(com_google_android_gms_internal_zzth.zzbpY);
            }
            if (this.zzbpY instanceof byte[]) {
                return Arrays.equals((byte[]) this.zzbpY, (byte[]) com_google_android_gms_internal_zzth.zzbpY);
            }
            if (this.zzbpY instanceof int[]) {
                return Arrays.equals((int[]) this.zzbpY, (int[]) com_google_android_gms_internal_zzth.zzbpY);
            }
            if (this.zzbpY instanceof long[]) {
                return Arrays.equals((long[]) this.zzbpY, (long[]) com_google_android_gms_internal_zzth.zzbpY);
            }
            if (this.zzbpY instanceof float[]) {
                return Arrays.equals((float[]) this.zzbpY, (float[]) com_google_android_gms_internal_zzth.zzbpY);
            }
            if (this.zzbpY instanceof double[]) {
                return Arrays.equals((double[]) this.zzbpY, (double[]) com_google_android_gms_internal_zzth.zzbpY);
            }
            return this.zzbpY instanceof boolean[] ? Arrays.equals((boolean[]) this.zzbpY, (boolean[]) com_google_android_gms_internal_zzth.zzbpY) : Arrays.deepEquals((Object[]) this.zzbpY, (Object[]) com_google_android_gms_internal_zzth.zzbpY);
        }
    }

    public final int hashCode() {
        try {
            return Arrays.hashCode(toByteArray()) + 527;
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    final void writeTo(zztd output) throws IOException {
        if (this.zzbpY != null) {
            this.zzbpX.zza(this.zzbpY, output);
            return;
        }
        for (zztm com_google_android_gms_internal_zztm : this.zzbpZ) {
            output.zzmy(com_google_android_gms_internal_zztm.tag);
            output.zzG(com_google_android_gms_internal_zztm.zzbqc);
        }
    }

    public final zzth zzHB() {
        int i = 0;
        zzth com_google_android_gms_internal_zzth = new zzth();
        try {
            com_google_android_gms_internal_zzth.zzbpX = this.zzbpX;
            if (this.zzbpZ == null) {
                com_google_android_gms_internal_zzth.zzbpZ = null;
            } else {
                com_google_android_gms_internal_zzth.zzbpZ.addAll(this.zzbpZ);
            }
            if (this.zzbpY != null) {
                if (this.zzbpY instanceof zztk) {
                    com_google_android_gms_internal_zzth.zzbpY = ((zztk) this.zzbpY).clone();
                } else if (this.zzbpY instanceof byte[]) {
                    com_google_android_gms_internal_zzth.zzbpY = ((byte[]) this.zzbpY).clone();
                } else if (this.zzbpY instanceof byte[][]) {
                    byte[][] bArr = (byte[][]) this.zzbpY;
                    Object obj = new byte[bArr.length][];
                    com_google_android_gms_internal_zzth.zzbpY = obj;
                    for (int i2 = 0; i2 < bArr.length; i2++) {
                        obj[i2] = (byte[]) bArr[i2].clone();
                    }
                } else if (this.zzbpY instanceof boolean[]) {
                    com_google_android_gms_internal_zzth.zzbpY = ((boolean[]) this.zzbpY).clone();
                } else if (this.zzbpY instanceof int[]) {
                    com_google_android_gms_internal_zzth.zzbpY = ((int[]) this.zzbpY).clone();
                } else if (this.zzbpY instanceof long[]) {
                    com_google_android_gms_internal_zzth.zzbpY = ((long[]) this.zzbpY).clone();
                } else if (this.zzbpY instanceof float[]) {
                    com_google_android_gms_internal_zzth.zzbpY = ((float[]) this.zzbpY).clone();
                } else if (this.zzbpY instanceof double[]) {
                    com_google_android_gms_internal_zzth.zzbpY = ((double[]) this.zzbpY).clone();
                } else if (this.zzbpY instanceof zztk[]) {
                    zztk[] com_google_android_gms_internal_zztkArr = (zztk[]) this.zzbpY;
                    Object obj2 = new zztk[com_google_android_gms_internal_zztkArr.length];
                    com_google_android_gms_internal_zzth.zzbpY = obj2;
                    while (i < com_google_android_gms_internal_zztkArr.length) {
                        obj2[i] = com_google_android_gms_internal_zztkArr[i].clone();
                        i++;
                    }
                }
            }
            return com_google_android_gms_internal_zzth;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    final int zzz() {
        if (this.zzbpY != null) {
            return this.zzbpX.zzY(this.zzbpY);
        }
        int i = 0;
        for (zztm com_google_android_gms_internal_zztm : this.zzbpZ) {
            i = (com_google_android_gms_internal_zztm.zzbqc.length + (zztd.zzmz(com_google_android_gms_internal_zztm.tag) + 0)) + i;
        }
        return i;
    }
}
