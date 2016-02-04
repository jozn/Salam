package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.server.response.FastJsonResponse.zza;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public final class StringToIntConverter implements SafeParcelable, zza<String, Integer> {
    public static final zzb CREATOR;
    final int mVersionCode;
    final HashMap<String, Integer> zzakP;
    private final HashMap<Integer, String> zzakQ;
    private final ArrayList<Entry> zzakR;

    public static final class Entry implements SafeParcelable {
        public static final zzc CREATOR;
        final int versionCode;
        final String zzakS;
        final int zzakT;

        static {
            CREATOR = new zzc();
        }

        Entry(int versionCode, String stringValue, int intValue) {
            this.versionCode = versionCode;
            this.zzakS = stringValue;
            this.zzakT = intValue;
        }

        Entry(String stringValue, int intValue) {
            this.versionCode = 1;
            this.zzakS = stringValue;
            this.zzakT = intValue;
        }

        public final int describeContents() {
            return 0;
        }

        public final void writeToParcel(Parcel out, int flags) {
            zzc.zza$5bed86f6(this, out);
        }
    }

    static {
        CREATOR = new zzb();
    }

    public StringToIntConverter() {
        this.mVersionCode = 1;
        this.zzakP = new HashMap();
        this.zzakQ = new HashMap();
        this.zzakR = null;
    }

    StringToIntConverter(int versionCode, ArrayList<Entry> serializedMap) {
        this.mVersionCode = versionCode;
        this.zzakP = new HashMap();
        this.zzakQ = new HashMap();
        this.zzakR = null;
        zzd(serializedMap);
    }

    private void zzd(ArrayList<Entry> arrayList) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            String str = entry.zzakS;
            int i = entry.zzakT;
            this.zzakP.put(str, Integer.valueOf(i));
            this.zzakQ.put(Integer.valueOf(i), str);
        }
    }

    public final /* synthetic */ Object convertBack(Object x0) {
        String str = (String) this.zzakQ.get((Integer) x0);
        return (str == null && this.zzakP.containsKey("gms_unknown")) ? "gms_unknown" : str;
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel out, int flags) {
        zzb.zza$dc69de4(this, out);
    }
}
