package com.google.android.gms.common.data;

import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import java.util.ArrayList;
import java.util.HashMap;

public final class DataHolder implements SafeParcelable {
    public static final zze CREATOR;
    private static final zza zzahI;
    boolean mClosed;
    final int mVersionCode;
    final int zzabx;
    final String[] zzahA;
    Bundle zzahB;
    final CursorWindow[] zzahC;
    final Bundle zzahD;
    int[] zzahE;
    int zzahF;
    private Object zzahG;
    private boolean zzahH;

    public static class zza {
        private final String[] zzahA;
        private final ArrayList<HashMap<String, Object>> zzahJ;
        private final String zzahK;
        private final HashMap<Object, Integer> zzahL;
        private boolean zzahM;
        private String zzahN;

        private zza(String[] strArr) {
            this.zzahA = (String[]) zzx.zzy(strArr);
            this.zzahJ = new ArrayList();
            this.zzahK = null;
            this.zzahL = new HashMap();
            this.zzahM = false;
            this.zzahN = null;
        }
    }

    /* renamed from: com.google.android.gms.common.data.DataHolder.1 */
    static class C02401 extends zza {
        C02401(String[] strArr) {
            super((byte) 0);
        }
    }

    static {
        CREATOR = new zze();
        zzahI = new C02401(new String[0]);
    }

    DataHolder(int versionCode, String[] columns, CursorWindow[] windows, int statusCode, Bundle metadata) {
        this.mClosed = false;
        this.zzahH = true;
        this.mVersionCode = versionCode;
        this.zzahA = columns;
        this.zzahC = windows;
        this.zzabx = statusCode;
        this.zzahD = metadata;
    }

    private void close() {
        synchronized (this) {
            if (!this.mClosed) {
                this.mClosed = true;
                for (CursorWindow close : this.zzahC) {
                    close.close();
                }
            }
        }
    }

    private boolean isClosed() {
        boolean z;
        synchronized (this) {
            z = this.mClosed;
        }
        return z;
    }

    public final int describeContents() {
        return 0;
    }

    protected final void finalize() throws Throwable {
        try {
            if (this.zzahH && this.zzahC.length > 0 && !isClosed()) {
                Log.e("DataBuffer", "Internal data leak within a DataBuffer object detected!  Be sure to explicitly call release() on all DataBuffer extending objects when you are done with them. (" + (this.zzahG == null ? "internal object: " + toString() : this.zzahG.toString()) + ")");
                close();
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    public final void writeToParcel(Parcel dest, int flags) {
        zze.zza(this, dest, flags);
    }

    public final void zzpL() {
        int i;
        int i2 = 0;
        this.zzahB = new Bundle();
        for (i = 0; i < this.zzahA.length; i++) {
            this.zzahB.putInt(this.zzahA[i], i);
        }
        this.zzahE = new int[this.zzahC.length];
        i = 0;
        while (i2 < this.zzahC.length) {
            this.zzahE[i2] = i;
            i += this.zzahC[i2].getNumRows() - (i - this.zzahC[i2].getStartPosition());
            i2++;
        }
        this.zzahF = i;
    }
}
