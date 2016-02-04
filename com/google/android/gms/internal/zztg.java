package com.google.android.gms.internal;

public final class zztg implements Cloneable {
    private static final zzth zzbpT;
    private int mSize;
    private boolean zzbpU;
    private int[] zzbpV;
    private zzth[] zzbpW;

    static {
        zzbpT = new zzth();
    }

    zztg() {
        this(10);
    }

    private zztg(int i) {
        this.zzbpU = false;
        int idealByteArraySize = idealByteArraySize(i * 4) / 4;
        this.zzbpV = new int[idealByteArraySize];
        this.zzbpW = new zzth[idealByteArraySize];
        this.mSize = 0;
    }

    private void gc() {
        int i = this.mSize;
        int[] iArr = this.zzbpV;
        zzth[] com_google_android_gms_internal_zzthArr = this.zzbpW;
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            zzth com_google_android_gms_internal_zzth = com_google_android_gms_internal_zzthArr[i3];
            if (com_google_android_gms_internal_zzth != zzbpT) {
                if (i3 != i2) {
                    iArr[i2] = iArr[i3];
                    com_google_android_gms_internal_zzthArr[i2] = com_google_android_gms_internal_zzth;
                    com_google_android_gms_internal_zzthArr[i3] = null;
                }
                i2++;
            }
        }
        this.zzbpU = false;
        this.mSize = i2;
    }

    private static int idealByteArraySize(int need) {
        for (int i = 4; i < 32; i++) {
            if (need <= (1 << i) - 12) {
                return (1 << i) - 12;
            }
        }
        return need;
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        return zzHA();
    }

    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zztg)) {
            return false;
        }
        zztg com_google_android_gms_internal_zztg = (zztg) o;
        if (size() != com_google_android_gms_internal_zztg.size()) {
            return false;
        }
        int i;
        boolean z;
        int[] iArr = this.zzbpV;
        int[] iArr2 = com_google_android_gms_internal_zztg.zzbpV;
        int i2 = this.mSize;
        for (i = 0; i < i2; i++) {
            if (iArr[i] != iArr2[i]) {
                z = false;
                break;
            }
        }
        z = true;
        if (z) {
            zzth[] com_google_android_gms_internal_zzthArr = this.zzbpW;
            zzth[] com_google_android_gms_internal_zzthArr2 = com_google_android_gms_internal_zztg.zzbpW;
            i2 = this.mSize;
            for (i = 0; i < i2; i++) {
                if (!com_google_android_gms_internal_zzthArr[i].equals(com_google_android_gms_internal_zzthArr2[i])) {
                    z = false;
                    break;
                }
            }
            z = true;
            if (z) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        if (this.zzbpU) {
            gc();
        }
        int i = 17;
        for (int i2 = 0; i2 < this.mSize; i2++) {
            i = (((i * 31) + this.zzbpV[i2]) * 31) + this.zzbpW[i2].hashCode();
        }
        return i;
    }

    public final boolean isEmpty() {
        return size() == 0;
    }

    final int size() {
        if (this.zzbpU) {
            gc();
        }
        return this.mSize;
    }

    public final zztg zzHA() {
        int i = 0;
        int size = size();
        zztg com_google_android_gms_internal_zztg = new zztg(size);
        System.arraycopy(this.zzbpV, 0, com_google_android_gms_internal_zztg.zzbpV, 0, size);
        while (i < size) {
            if (this.zzbpW[i] != null) {
                com_google_android_gms_internal_zztg.zzbpW[i] = this.zzbpW[i].zzHB();
            }
            i++;
        }
        com_google_android_gms_internal_zztg.mSize = size;
        return com_google_android_gms_internal_zztg;
    }

    final zzth zzmD(int i) {
        if (this.zzbpU) {
            gc();
        }
        return this.zzbpW[i];
    }
}
