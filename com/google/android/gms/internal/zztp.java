package com.google.android.gms.internal;

import android.support.v7.appcompat.BuildConfig;
import java.io.IOException;
import java.util.Arrays;

public interface zztp {

    public static final class zza extends zzte<zza> {
        public String[] zzbqn;
        public String[] zzbqo;
        public int[] zzbqp;
        public long[] zzbqq;

        public zza() {
            this.zzbqn = zztn.zzbqg;
            this.zzbqo = zztn.zzbqg;
            this.zzbqp = zztn.zzboD;
            this.zzbqq = zztn.zzboC;
            this.zzbpQ = null;
            this.zzbqb = -1;
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zza)) {
                return false;
            }
            zza com_google_android_gms_internal_zztp_zza = (zza) o;
            if (!zzti.equals(this.zzbqn, com_google_android_gms_internal_zztp_zza.zzbqn)) {
                return false;
            }
            if (!zzti.equals(this.zzbqo, com_google_android_gms_internal_zztp_zza.zzbqo)) {
                return false;
            }
            if (!zzti.equals(this.zzbqp, com_google_android_gms_internal_zztp_zza.zzbqp)) {
                return false;
            }
            if (!zzti.equals(this.zzbqq, com_google_android_gms_internal_zztp_zza.zzbqq)) {
                return false;
            }
            if (this.zzbpQ == null || this.zzbpQ.isEmpty()) {
                return com_google_android_gms_internal_zztp_zza.zzbpQ == null || com_google_android_gms_internal_zztp_zza.zzbpQ.isEmpty();
            } else {
                return this.zzbpQ.equals(com_google_android_gms_internal_zztp_zza.zzbpQ);
            }
        }

        public final int hashCode() {
            int hashCode = (((((((((getClass().getName().hashCode() + 527) * 31) + zzti.hashCode(this.zzbqn)) * 31) + zzti.hashCode(this.zzbqo)) * 31) + zzti.hashCode(this.zzbqp)) * 31) + zzti.hashCode(this.zzbqq)) * 31;
            int hashCode2 = (this.zzbpQ == null || this.zzbpQ.isEmpty()) ? 0 : this.zzbpQ.hashCode();
            return hashCode2 + hashCode;
        }

        public final void writeTo(zztd output) throws IOException {
            int i = 0;
            if (this.zzbqn != null && this.zzbqn.length > 0) {
                for (String str : this.zzbqn) {
                    if (str != null) {
                        output.zzb(1, str);
                    }
                }
            }
            if (this.zzbqo != null && this.zzbqo.length > 0) {
                for (String str2 : this.zzbqo) {
                    if (str2 != null) {
                        output.zzb(2, str2);
                    }
                }
            }
            if (this.zzbqp != null && this.zzbqp.length > 0) {
                for (int zzG : this.zzbqp) {
                    output.zzG(3, zzG);
                }
            }
            if (this.zzbqq != null && this.zzbqq.length > 0) {
                while (i < this.zzbqq.length) {
                    output.zzb(4, this.zzbqq[i]);
                    i++;
                }
            }
            super.writeTo(output);
        }

        protected final int zzz() {
            int i;
            int i2;
            int i3;
            int i4 = 0;
            int zzz = super.zzz();
            if (this.zzbqn == null || this.zzbqn.length <= 0) {
                i = zzz;
            } else {
                i2 = 0;
                i3 = 0;
                for (String str : this.zzbqn) {
                    if (str != null) {
                        i3++;
                        i2 += zztd.zzga(str);
                    }
                }
                i = (zzz + i2) + (i3 * 1);
            }
            if (this.zzbqo != null && this.zzbqo.length > 0) {
                i3 = 0;
                zzz = 0;
                for (String str2 : this.zzbqo) {
                    if (str2 != null) {
                        zzz++;
                        i3 += zztd.zzga(str2);
                    }
                }
                i = (i + i3) + (zzz * 1);
            }
            if (this.zzbqp != null && this.zzbqp.length > 0) {
                i3 = 0;
                for (int zzz2 : this.zzbqp) {
                    i3 += zztd.zzmu(zzz2);
                }
                i = (i + i3) + (this.zzbqp.length * 1);
            }
            if (this.zzbqq == null || this.zzbqq.length <= 0) {
                return i;
            }
            i2 = 0;
            while (i4 < this.zzbqq.length) {
                i2 += zztd.zzag(this.zzbqq[i4]);
                i4++;
            }
            return (i + i2) + (this.zzbqq.length * 1);
        }
    }

    public static final class zzb extends zzte<zzb> {
        public String version;
        public int zzbqr;
        public String zzbqs;

        public zzb() {
            this.zzbqr = 0;
            this.zzbqs = BuildConfig.VERSION_NAME;
            this.version = BuildConfig.VERSION_NAME;
            this.zzbpQ = null;
            this.zzbqb = -1;
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzb)) {
                return false;
            }
            zzb com_google_android_gms_internal_zztp_zzb = (zzb) o;
            if (this.zzbqr != com_google_android_gms_internal_zztp_zzb.zzbqr) {
                return false;
            }
            if (this.zzbqs == null) {
                if (com_google_android_gms_internal_zztp_zzb.zzbqs != null) {
                    return false;
                }
            } else if (!this.zzbqs.equals(com_google_android_gms_internal_zztp_zzb.zzbqs)) {
                return false;
            }
            if (this.version == null) {
                if (com_google_android_gms_internal_zztp_zzb.version != null) {
                    return false;
                }
            } else if (!this.version.equals(com_google_android_gms_internal_zztp_zzb.version)) {
                return false;
            }
            if (this.zzbpQ == null || this.zzbpQ.isEmpty()) {
                return com_google_android_gms_internal_zztp_zzb.zzbpQ == null || com_google_android_gms_internal_zztp_zzb.zzbpQ.isEmpty();
            } else {
                return this.zzbpQ.equals(com_google_android_gms_internal_zztp_zzb.zzbpQ);
            }
        }

        public final int hashCode() {
            int i = 0;
            int hashCode = ((this.version == null ? 0 : this.version.hashCode()) + (((this.zzbqs == null ? 0 : this.zzbqs.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + this.zzbqr) * 31)) * 31)) * 31;
            if (!(this.zzbpQ == null || this.zzbpQ.isEmpty())) {
                i = this.zzbpQ.hashCode();
            }
            return hashCode + i;
        }

        public final void writeTo(zztd output) throws IOException {
            if (this.zzbqr != 0) {
                output.zzG(1, this.zzbqr);
            }
            if (!this.zzbqs.equals(BuildConfig.VERSION_NAME)) {
                output.zzb(2, this.zzbqs);
            }
            if (!this.version.equals(BuildConfig.VERSION_NAME)) {
                output.zzb(3, this.version);
            }
            super.writeTo(output);
        }

        protected final int zzz() {
            int zzz = super.zzz();
            if (this.zzbqr != 0) {
                zzz += zztd.zzI(1, this.zzbqr);
            }
            if (!this.zzbqs.equals(BuildConfig.VERSION_NAME)) {
                zzz += zztd.zzp(2, this.zzbqs);
            }
            return !this.version.equals(BuildConfig.VERSION_NAME) ? zzz + zztd.zzp(3, this.version) : zzz;
        }
    }

    public static final class zzc extends zzte<zzc> {
        public byte[] zzbqt;
        public byte[][] zzbqu;
        public boolean zzbqv;

        public zzc() {
            this.zzbqt = zztn.zzbqi;
            this.zzbqu = zztn.zzbqh;
            this.zzbqv = false;
            this.zzbpQ = null;
            this.zzbqb = -1;
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzc)) {
                return false;
            }
            zzc com_google_android_gms_internal_zztp_zzc = (zzc) o;
            if (!Arrays.equals(this.zzbqt, com_google_android_gms_internal_zztp_zzc.zzbqt)) {
                return false;
            }
            if (!zzti.zza(this.zzbqu, com_google_android_gms_internal_zztp_zzc.zzbqu)) {
                return false;
            }
            if (this.zzbqv != com_google_android_gms_internal_zztp_zzc.zzbqv) {
                return false;
            }
            if (this.zzbpQ == null || this.zzbpQ.isEmpty()) {
                return com_google_android_gms_internal_zztp_zzc.zzbpQ == null || com_google_android_gms_internal_zztp_zzc.zzbpQ.isEmpty();
            } else {
                return this.zzbpQ.equals(com_google_android_gms_internal_zztp_zzc.zzbpQ);
            }
        }

        public final int hashCode() {
            int hashCode = ((this.zzbqv ? 1231 : 1237) + ((((((getClass().getName().hashCode() + 527) * 31) + Arrays.hashCode(this.zzbqt)) * 31) + zzti.zza(this.zzbqu)) * 31)) * 31;
            int hashCode2 = (this.zzbpQ == null || this.zzbpQ.isEmpty()) ? 0 : this.zzbpQ.hashCode();
            return hashCode2 + hashCode;
        }

        public final void writeTo(zztd output) throws IOException {
            if (!Arrays.equals(this.zzbqt, zztn.zzbqi)) {
                output.zza(1, this.zzbqt);
            }
            if (this.zzbqu != null && this.zzbqu.length > 0) {
                for (byte[] bArr : this.zzbqu) {
                    if (bArr != null) {
                        output.zza(2, bArr);
                    }
                }
            }
            if (this.zzbqv) {
                output.zzb(3, this.zzbqv);
            }
            super.writeTo(output);
        }

        protected final int zzz() {
            int i = 0;
            int zzz = super.zzz();
            if (!Arrays.equals(this.zzbqt, zztn.zzbqi)) {
                zzz += zztd.zzb(1, this.zzbqt);
            }
            if (this.zzbqu != null && this.zzbqu.length > 0) {
                int i2 = 0;
                int i3 = 0;
                while (i < this.zzbqu.length) {
                    byte[] bArr = this.zzbqu[i];
                    if (bArr != null) {
                        i3++;
                        i2 += zztd.zzF(bArr);
                    }
                    i++;
                }
                zzz = (zzz + i2) + (i3 * 1);
            }
            return this.zzbqv ? zzz + (zztd.zzmx(3) + 1) : zzz;
        }
    }

    public static final class zzd extends zzte<zzd> {
        public String tag;
        public boolean zzbqA;
        public zze[] zzbqB;
        public zzb zzbqC;
        public byte[] zzbqD;
        public byte[] zzbqE;
        public byte[] zzbqF;
        public zza zzbqG;
        public String zzbqH;
        public long zzbqI;
        public zzc zzbqJ;
        public byte[] zzbqK;
        public int zzbqL;
        public int[] zzbqM;
        public long zzbqw;
        public long zzbqx;
        public long zzbqy;
        public int zzbqz;
        public int zznN;

        public zzd() {
            this.zzbqw = 0;
            this.zzbqx = 0;
            this.zzbqy = 0;
            this.tag = BuildConfig.VERSION_NAME;
            this.zzbqz = 0;
            this.zznN = 0;
            this.zzbqA = false;
            this.zzbqB = zze.zzHQ();
            this.zzbqC = null;
            this.zzbqD = zztn.zzbqi;
            this.zzbqE = zztn.zzbqi;
            this.zzbqF = zztn.zzbqi;
            this.zzbqG = null;
            this.zzbqH = BuildConfig.VERSION_NAME;
            this.zzbqI = 180000;
            this.zzbqJ = null;
            this.zzbqK = zztn.zzbqi;
            this.zzbqL = 0;
            this.zzbqM = zztn.zzboD;
            this.zzbpQ = null;
            this.zzbqb = -1;
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzd)) {
                return false;
            }
            zzd com_google_android_gms_internal_zztp_zzd = (zzd) o;
            if (this.zzbqw != com_google_android_gms_internal_zztp_zzd.zzbqw) {
                return false;
            }
            if (this.zzbqx != com_google_android_gms_internal_zztp_zzd.zzbqx) {
                return false;
            }
            if (this.zzbqy != com_google_android_gms_internal_zztp_zzd.zzbqy) {
                return false;
            }
            if (this.tag == null) {
                if (com_google_android_gms_internal_zztp_zzd.tag != null) {
                    return false;
                }
            } else if (!this.tag.equals(com_google_android_gms_internal_zztp_zzd.tag)) {
                return false;
            }
            if (this.zzbqz != com_google_android_gms_internal_zztp_zzd.zzbqz) {
                return false;
            }
            if (this.zznN != com_google_android_gms_internal_zztp_zzd.zznN) {
                return false;
            }
            if (this.zzbqA != com_google_android_gms_internal_zztp_zzd.zzbqA) {
                return false;
            }
            if (!zzti.equals(this.zzbqB, com_google_android_gms_internal_zztp_zzd.zzbqB)) {
                return false;
            }
            if (this.zzbqC == null) {
                if (com_google_android_gms_internal_zztp_zzd.zzbqC != null) {
                    return false;
                }
            } else if (!this.zzbqC.equals(com_google_android_gms_internal_zztp_zzd.zzbqC)) {
                return false;
            }
            if (!Arrays.equals(this.zzbqD, com_google_android_gms_internal_zztp_zzd.zzbqD)) {
                return false;
            }
            if (!Arrays.equals(this.zzbqE, com_google_android_gms_internal_zztp_zzd.zzbqE)) {
                return false;
            }
            if (!Arrays.equals(this.zzbqF, com_google_android_gms_internal_zztp_zzd.zzbqF)) {
                return false;
            }
            if (this.zzbqG == null) {
                if (com_google_android_gms_internal_zztp_zzd.zzbqG != null) {
                    return false;
                }
            } else if (!this.zzbqG.equals(com_google_android_gms_internal_zztp_zzd.zzbqG)) {
                return false;
            }
            if (this.zzbqH == null) {
                if (com_google_android_gms_internal_zztp_zzd.zzbqH != null) {
                    return false;
                }
            } else if (!this.zzbqH.equals(com_google_android_gms_internal_zztp_zzd.zzbqH)) {
                return false;
            }
            if (this.zzbqI != com_google_android_gms_internal_zztp_zzd.zzbqI) {
                return false;
            }
            if (this.zzbqJ == null) {
                if (com_google_android_gms_internal_zztp_zzd.zzbqJ != null) {
                    return false;
                }
            } else if (!this.zzbqJ.equals(com_google_android_gms_internal_zztp_zzd.zzbqJ)) {
                return false;
            }
            if (!Arrays.equals(this.zzbqK, com_google_android_gms_internal_zztp_zzd.zzbqK)) {
                return false;
            }
            if (this.zzbqL != com_google_android_gms_internal_zztp_zzd.zzbqL) {
                return false;
            }
            if (!zzti.equals(this.zzbqM, com_google_android_gms_internal_zztp_zzd.zzbqM)) {
                return false;
            }
            if (this.zzbpQ == null || this.zzbpQ.isEmpty()) {
                return com_google_android_gms_internal_zztp_zzd.zzbpQ == null || com_google_android_gms_internal_zztp_zzd.zzbpQ.isEmpty();
            } else {
                return this.zzbpQ.equals(com_google_android_gms_internal_zztp_zzd.zzbpQ);
            }
        }

        public final int hashCode() {
            int i = 0;
            int hashCode = ((((((((this.zzbqJ == null ? 0 : this.zzbqJ.hashCode()) + (((((this.zzbqH == null ? 0 : this.zzbqH.hashCode()) + (((this.zzbqG == null ? 0 : this.zzbqG.hashCode()) + (((((((((this.zzbqC == null ? 0 : this.zzbqC.hashCode()) + (((((this.zzbqA ? 1231 : 1237) + (((((((this.tag == null ? 0 : this.tag.hashCode()) + ((((((((getClass().getName().hashCode() + 527) * 31) + ((int) (this.zzbqw ^ (this.zzbqw >>> 32)))) * 31) + ((int) (this.zzbqx ^ (this.zzbqx >>> 32)))) * 31) + ((int) (this.zzbqy ^ (this.zzbqy >>> 32)))) * 31)) * 31) + this.zzbqz) * 31) + this.zznN) * 31)) * 31) + zzti.hashCode(this.zzbqB)) * 31)) * 31) + Arrays.hashCode(this.zzbqD)) * 31) + Arrays.hashCode(this.zzbqE)) * 31) + Arrays.hashCode(this.zzbqF)) * 31)) * 31)) * 31) + ((int) (this.zzbqI ^ (this.zzbqI >>> 32)))) * 31)) * 31) + Arrays.hashCode(this.zzbqK)) * 31) + this.zzbqL) * 31) + zzti.hashCode(this.zzbqM)) * 31;
            if (!(this.zzbpQ == null || this.zzbpQ.isEmpty())) {
                i = this.zzbpQ.hashCode();
            }
            return hashCode + i;
        }

        public final void writeTo(zztd output) throws IOException {
            int i = 0;
            if (this.zzbqw != 0) {
                output.zzb(1, this.zzbqw);
            }
            if (!this.tag.equals(BuildConfig.VERSION_NAME)) {
                output.zzb(2, this.tag);
            }
            if (this.zzbqB != null && this.zzbqB.length > 0) {
                for (zztk com_google_android_gms_internal_zztk : this.zzbqB) {
                    if (com_google_android_gms_internal_zztk != null) {
                        output.zza(3, com_google_android_gms_internal_zztk);
                    }
                }
            }
            if (!Arrays.equals(this.zzbqD, zztn.zzbqi)) {
                output.zza(6, this.zzbqD);
            }
            if (this.zzbqG != null) {
                output.zza(7, this.zzbqG);
            }
            if (!Arrays.equals(this.zzbqE, zztn.zzbqi)) {
                output.zza(8, this.zzbqE);
            }
            if (this.zzbqC != null) {
                output.zza(9, this.zzbqC);
            }
            if (this.zzbqA) {
                output.zzb(10, this.zzbqA);
            }
            if (this.zzbqz != 0) {
                output.zzG(11, this.zzbqz);
            }
            if (this.zznN != 0) {
                output.zzG(12, this.zznN);
            }
            if (!Arrays.equals(this.zzbqF, zztn.zzbqi)) {
                output.zza(13, this.zzbqF);
            }
            if (!this.zzbqH.equals(BuildConfig.VERSION_NAME)) {
                output.zzb(14, this.zzbqH);
            }
            if (this.zzbqI != 180000) {
                long j = this.zzbqI;
                output.zzK(15, 0);
                output.zzaf(zztd.zzai(j));
            }
            if (this.zzbqJ != null) {
                output.zza(16, this.zzbqJ);
            }
            if (this.zzbqx != 0) {
                output.zzb(17, this.zzbqx);
            }
            if (!Arrays.equals(this.zzbqK, zztn.zzbqi)) {
                output.zza(18, this.zzbqK);
            }
            if (this.zzbqL != 0) {
                output.zzG(19, this.zzbqL);
            }
            if (this.zzbqM != null && this.zzbqM.length > 0) {
                while (i < this.zzbqM.length) {
                    output.zzG(20, this.zzbqM[i]);
                    i++;
                }
            }
            if (this.zzbqy != 0) {
                output.zzb(21, this.zzbqy);
            }
            super.writeTo(output);
        }

        protected final int zzz() {
            int i;
            int i2 = 0;
            int zzz = super.zzz();
            if (this.zzbqw != 0) {
                zzz += zztd.zzd(1, this.zzbqw);
            }
            if (!this.tag.equals(BuildConfig.VERSION_NAME)) {
                zzz += zztd.zzp(2, this.tag);
            }
            if (this.zzbqB != null && this.zzbqB.length > 0) {
                i = zzz;
                for (zztk com_google_android_gms_internal_zztk : this.zzbqB) {
                    if (com_google_android_gms_internal_zztk != null) {
                        i += zztd.zzc(3, com_google_android_gms_internal_zztk);
                    }
                }
                zzz = i;
            }
            if (!Arrays.equals(this.zzbqD, zztn.zzbqi)) {
                zzz += zztd.zzb(6, this.zzbqD);
            }
            if (this.zzbqG != null) {
                zzz += zztd.zzc(7, this.zzbqG);
            }
            if (!Arrays.equals(this.zzbqE, zztn.zzbqi)) {
                zzz += zztd.zzb(8, this.zzbqE);
            }
            if (this.zzbqC != null) {
                zzz += zztd.zzc(9, this.zzbqC);
            }
            if (this.zzbqA) {
                zzz += zztd.zzmx(10) + 1;
            }
            if (this.zzbqz != 0) {
                zzz += zztd.zzI(11, this.zzbqz);
            }
            if (this.zznN != 0) {
                zzz += zztd.zzI(12, this.zznN);
            }
            if (!Arrays.equals(this.zzbqF, zztn.zzbqi)) {
                zzz += zztd.zzb(13, this.zzbqF);
            }
            if (!this.zzbqH.equals(BuildConfig.VERSION_NAME)) {
                zzz += zztd.zzp(14, this.zzbqH);
            }
            if (this.zzbqI != 180000) {
                zzz += zztd.zzag(zztd.zzai(this.zzbqI)) + zztd.zzmx(15);
            }
            if (this.zzbqJ != null) {
                zzz += zztd.zzc(16, this.zzbqJ);
            }
            if (this.zzbqx != 0) {
                zzz += zztd.zzd(17, this.zzbqx);
            }
            if (!Arrays.equals(this.zzbqK, zztn.zzbqi)) {
                zzz += zztd.zzb(18, this.zzbqK);
            }
            if (this.zzbqL != 0) {
                zzz += zztd.zzI(19, this.zzbqL);
            }
            if (this.zzbqM != null && this.zzbqM.length > 0) {
                i = 0;
                while (i2 < this.zzbqM.length) {
                    i += zztd.zzmu(this.zzbqM[i2]);
                    i2++;
                }
                zzz = (zzz + i) + (this.zzbqM.length * 2);
            }
            return this.zzbqy != 0 ? zzz + zztd.zzd(21, this.zzbqy) : zzz;
        }
    }

    public static final class zze extends zzte<zze> {
        private static volatile zze[] zzbqN;
        public String key;
        public String value;

        public zze() {
            this.key = BuildConfig.VERSION_NAME;
            this.value = BuildConfig.VERSION_NAME;
            this.zzbpQ = null;
            this.zzbqb = -1;
        }

        public static zze[] zzHQ() {
            if (zzbqN == null) {
                synchronized (zzti.zzbqa) {
                    if (zzbqN == null) {
                        zzbqN = new zze[0];
                    }
                }
            }
            return zzbqN;
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zze)) {
                return false;
            }
            zze com_google_android_gms_internal_zztp_zze = (zze) o;
            if (this.key == null) {
                if (com_google_android_gms_internal_zztp_zze.key != null) {
                    return false;
                }
            } else if (!this.key.equals(com_google_android_gms_internal_zztp_zze.key)) {
                return false;
            }
            if (this.value == null) {
                if (com_google_android_gms_internal_zztp_zze.value != null) {
                    return false;
                }
            } else if (!this.value.equals(com_google_android_gms_internal_zztp_zze.value)) {
                return false;
            }
            if (this.zzbpQ == null || this.zzbpQ.isEmpty()) {
                return com_google_android_gms_internal_zztp_zze.zzbpQ == null || com_google_android_gms_internal_zztp_zze.zzbpQ.isEmpty();
            } else {
                return this.zzbpQ.equals(com_google_android_gms_internal_zztp_zze.zzbpQ);
            }
        }

        public final int hashCode() {
            int i = 0;
            int hashCode = ((this.value == null ? 0 : this.value.hashCode()) + (((this.key == null ? 0 : this.key.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31;
            if (!(this.zzbpQ == null || this.zzbpQ.isEmpty())) {
                i = this.zzbpQ.hashCode();
            }
            return hashCode + i;
        }

        public final void writeTo(zztd output) throws IOException {
            if (!this.key.equals(BuildConfig.VERSION_NAME)) {
                output.zzb(1, this.key);
            }
            if (!this.value.equals(BuildConfig.VERSION_NAME)) {
                output.zzb(2, this.value);
            }
            super.writeTo(output);
        }

        protected final int zzz() {
            int zzz = super.zzz();
            if (!this.key.equals(BuildConfig.VERSION_NAME)) {
                zzz += zztd.zzp(1, this.key);
            }
            return !this.value.equals(BuildConfig.VERSION_NAME) ? zzz + zztd.zzp(2, this.value) : zzz;
        }
    }
}
