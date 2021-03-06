package com.google.android.gms.internal;

import android.support.v4.media.TransportMediator;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;

public final class zztd {
    private final ByteBuffer zzbpP;

    public static class zza extends IOException {
        zza(int i, int i2) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space (pos " + i + " limit " + i2 + ").");
        }
    }

    private zztd(ByteBuffer byteBuffer) {
        this.zzbpP = byteBuffer;
        this.zzbpP.order(ByteOrder.LITTLE_ENDIAN);
    }

    zztd(byte[] bArr, int i) {
        this(ByteBuffer.wrap(bArr, 0, i));
    }

    public static int zzF(byte[] bArr) {
        return zzmz(bArr.length) + bArr.length;
    }

    public static int zzI(int i, int i2) {
        return zzmx(i) + zzmu(i2);
    }

    private static int zza(CharSequence charSequence, byte[] bArr, int i, int i2) {
        int length = charSequence.length();
        int i3 = 0;
        int i4 = i + i2;
        while (i3 < length && i3 + i < i4) {
            char charAt = charSequence.charAt(i3);
            if (charAt >= '\u0080') {
                break;
            }
            bArr[i + i3] = (byte) charAt;
            i3++;
        }
        if (i3 == length) {
            return i + length;
        }
        int i5 = i + i3;
        while (i3 < length) {
            int i6;
            char charAt2 = charSequence.charAt(i3);
            if (charAt2 < '\u0080' && i5 < i4) {
                i6 = i5 + 1;
                bArr[i5] = (byte) charAt2;
            } else if (charAt2 < '\u0800' && i5 <= i4 - 2) {
                r6 = i5 + 1;
                bArr[i5] = (byte) ((charAt2 >>> 6) | 960);
                i6 = r6 + 1;
                bArr[r6] = (byte) ((charAt2 & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            } else if ((charAt2 < '\ud800' || '\udfff' < charAt2) && i5 <= i4 - 3) {
                i6 = i5 + 1;
                bArr[i5] = (byte) ((charAt2 >>> 12) | 480);
                i5 = i6 + 1;
                bArr[i6] = (byte) (((charAt2 >>> 6) & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                i6 = i5 + 1;
                bArr[i5] = (byte) ((charAt2 & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            } else if (i5 <= i4 - 4) {
                if (i3 + 1 != charSequence.length()) {
                    i3++;
                    charAt = charSequence.charAt(i3);
                    if (Character.isSurrogatePair(charAt2, charAt)) {
                        int toCodePoint = Character.toCodePoint(charAt2, charAt);
                        i6 = i5 + 1;
                        bArr[i5] = (byte) ((toCodePoint >>> 18) | 240);
                        i5 = i6 + 1;
                        bArr[i6] = (byte) (((toCodePoint >>> 12) & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                        r6 = i5 + 1;
                        bArr[i5] = (byte) (((toCodePoint >>> 6) & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                        i6 = r6 + 1;
                        bArr[r6] = (byte) ((toCodePoint & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                    }
                }
                throw new IllegalArgumentException("Unpaired surrogate at index " + (i3 - 1));
            } else if ('\ud800' > charAt2 || charAt2 > '\udfff' || (i3 + 1 != charSequence.length() && Character.isSurrogatePair(charAt2, charSequence.charAt(i3 + 1)))) {
                throw new ArrayIndexOutOfBoundsException("Failed writing " + charAt2 + " at index " + i5);
            } else {
                throw new IllegalArgumentException("Unpaired surrogate at index " + i3);
            }
            i3++;
            i5 = i6;
        }
        return i5;
    }

    private static void zza(CharSequence charSequence, ByteBuffer byteBuffer) {
        if (byteBuffer.isReadOnly()) {
            throw new ReadOnlyBufferException();
        } else if (byteBuffer.hasArray()) {
            try {
                byteBuffer.position(zza(charSequence, byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining()) - byteBuffer.arrayOffset());
            } catch (Throwable e) {
                BufferOverflowException bufferOverflowException = new BufferOverflowException();
                bufferOverflowException.initCause(e);
                throw bufferOverflowException;
            }
        } else {
            zzb(charSequence, byteBuffer);
        }
    }

    public static int zzag(long j) {
        return (-128 & j) == 0 ? 1 : (-16384 & j) == 0 ? 2 : (-2097152 & j) == 0 ? 3 : (-268435456 & j) == 0 ? 4 : (-34359738368L & j) == 0 ? 5 : (-4398046511104L & j) == 0 ? 6 : (-562949953421312L & j) == 0 ? 7 : (-72057594037927936L & j) == 0 ? 8 : (Long.MIN_VALUE & j) == 0 ? 9 : 10;
    }

    public static long zzai(long j) {
        return (j << 1) ^ (j >> 63);
    }

    public static int zzb(int i, byte[] bArr) {
        return zzmx(i) + zzF(bArr);
    }

    private static void zzb(CharSequence charSequence, ByteBuffer byteBuffer) {
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            char charAt = charSequence.charAt(i);
            if (charAt < '\u0080') {
                byteBuffer.put((byte) charAt);
            } else if (charAt < '\u0800') {
                byteBuffer.put((byte) ((charAt >>> 6) | 960));
                byteBuffer.put((byte) ((charAt & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS));
            } else if (charAt < '\ud800' || '\udfff' < charAt) {
                byteBuffer.put((byte) ((charAt >>> 12) | 480));
                byteBuffer.put((byte) (((charAt >>> 6) & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS));
                byteBuffer.put((byte) ((charAt & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS));
            } else {
                if (i + 1 != charSequence.length()) {
                    i++;
                    char charAt2 = charSequence.charAt(i);
                    if (Character.isSurrogatePair(charAt, charAt2)) {
                        int toCodePoint = Character.toCodePoint(charAt, charAt2);
                        byteBuffer.put((byte) ((toCodePoint >>> 18) | 240));
                        byteBuffer.put((byte) (((toCodePoint >>> 12) & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS));
                        byteBuffer.put((byte) (((toCodePoint >>> 6) & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS));
                        byteBuffer.put((byte) ((toCodePoint & 63) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS));
                    }
                }
                throw new IllegalArgumentException("Unpaired surrogate at index " + (i - 1));
            }
            i++;
        }
    }

    public static int zzc(int i, zztk com_google_android_gms_internal_zztk) {
        int zzmx = zzmx(i);
        int serializedSize = com_google_android_gms_internal_zztk.getSerializedSize();
        return zzmx + (serializedSize + zzmz(serializedSize));
    }

    private static int zzc(CharSequence charSequence) {
        int i = 0;
        int length = charSequence.length();
        int i2 = 0;
        while (i2 < length && charSequence.charAt(i2) < '\u0080') {
            i2++;
        }
        int i3 = length;
        while (i2 < length) {
            char charAt = charSequence.charAt(i2);
            if (charAt < '\u0800') {
                i3 += (127 - charAt) >>> 31;
                i2++;
            } else {
                int length2 = charSequence.length();
                while (i2 < length2) {
                    char charAt2 = charSequence.charAt(i2);
                    if (charAt2 < '\u0800') {
                        i += (127 - charAt2) >>> 31;
                    } else {
                        i += 2;
                        if ('\ud800' <= charAt2 && charAt2 <= '\udfff') {
                            if (Character.codePointAt(charSequence, i2) < AccessibilityNodeInfoCompat.ACTION_CUT) {
                                throw new IllegalArgumentException("Unpaired surrogate at index " + i2);
                            }
                            i2++;
                        }
                    }
                    i2++;
                }
                i2 = i3 + i;
                if (i2 < length) {
                    return i2;
                }
                throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (((long) i2) + 4294967296L));
            }
        }
        i2 = i3;
        if (i2 < length) {
            return i2;
        }
        throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (((long) i2) + 4294967296L));
    }

    public static int zzd(int i, long j) {
        return zzmx(i) + zzag(j);
    }

    public static int zzga(String str) {
        int zzc = zzc((CharSequence) str);
        return zzc + zzmz(zzc);
    }

    public static int zzmu(int i) {
        return i >= 0 ? zzmz(i) : 10;
    }

    private void zzmw(int i) throws IOException {
        byte b = (byte) i;
        if (this.zzbpP.hasRemaining()) {
            this.zzbpP.put(b);
            return;
        }
        throw new zza(this.zzbpP.position(), this.zzbpP.limit());
    }

    public static int zzmx(int i) {
        return zzmz(zztn.zzL(i, 0));
    }

    public static int zzmz(int i) {
        return (i & -128) == 0 ? 1 : (i & -16384) == 0 ? 2 : (-2097152 & i) == 0 ? 3 : (-268435456 & i) == 0 ? 4 : 5;
    }

    public static int zzp(int i, String str) {
        return zzmx(i) + zzga(str);
    }

    public final void zzG(int i, int i2) throws IOException {
        zzK(i, 0);
        if (i2 >= 0) {
            zzmy(i2);
        } else {
            zzaf((long) i2);
        }
    }

    public final void zzG(byte[] bArr) throws IOException {
        int length = bArr.length;
        if (this.zzbpP.remaining() >= length) {
            this.zzbpP.put(bArr, 0, length);
            return;
        }
        throw new zza(this.zzbpP.position(), this.zzbpP.limit());
    }

    public final void zzK(int i, int i2) throws IOException {
        zzmy(zztn.zzL(i, i2));
    }

    public final void zza(int i, zztk com_google_android_gms_internal_zztk) throws IOException {
        zzK(i, 2);
        zzc(com_google_android_gms_internal_zztk);
    }

    public final void zza(int i, byte[] bArr) throws IOException {
        zzK(i, 2);
        zzmy(bArr.length);
        zzG(bArr);
    }

    public final void zzaf(long j) throws IOException {
        while ((-128 & j) != 0) {
            zzmw((((int) j) & TransportMediator.KEYCODE_MEDIA_PAUSE) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            j >>>= 7;
        }
        zzmw((int) j);
    }

    public final void zzb(int i, long j) throws IOException {
        zzK(i, 0);
        zzaf(j);
    }

    public final void zzb(int i, String str) throws IOException {
        zzK(i, 2);
        try {
            int zzmz = zzmz(str.length());
            if (zzmz == zzmz(str.length() * 3)) {
                int position = this.zzbpP.position();
                if (this.zzbpP.remaining() < zzmz) {
                    throw new zza(zzmz + position, this.zzbpP.limit());
                }
                this.zzbpP.position(position + zzmz);
                zza((CharSequence) str, this.zzbpP);
                int position2 = this.zzbpP.position();
                this.zzbpP.position(position);
                zzmy((position2 - position) - zzmz);
                this.zzbpP.position(position2);
                return;
            }
            zzmy(zzc((CharSequence) str));
            zza((CharSequence) str, this.zzbpP);
        } catch (Throwable e) {
            zza com_google_android_gms_internal_zztd_zza = new zza(this.zzbpP.position(), this.zzbpP.limit());
            com_google_android_gms_internal_zztd_zza.initCause(e);
            throw com_google_android_gms_internal_zztd_zza;
        }
    }

    public final void zzb(int i, boolean z) throws IOException {
        int i2 = 0;
        zzK(i, 0);
        if (z) {
            i2 = 1;
        }
        zzmw(i2);
    }

    public final void zzc(zztk com_google_android_gms_internal_zztk) throws IOException {
        zzmy(com_google_android_gms_internal_zztk.getCachedSize());
        com_google_android_gms_internal_zztk.writeTo(this);
    }

    public final void zzmy(int i) throws IOException {
        while ((i & -128) != 0) {
            zzmw((i & TransportMediator.KEYCODE_MEDIA_PAUSE) | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            i >>>= 7;
        }
        zzmw(i);
    }
}
