package com.google.android.gms.common.server.response;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.server.response.FastJsonResponse.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FieldMappingDictionary implements SafeParcelable {
    public static final zzc CREATOR;
    final int mVersionCode;
    final HashMap<String, Map<String, Field<?, ?>>> zzale;
    private final ArrayList<Entry> zzalf;
    final String zzalg;

    public static class Entry implements SafeParcelable {
        public static final zzd CREATOR;
        final String className;
        final int versionCode;
        final ArrayList<FieldMapPair> zzalh;

        static {
            CREATOR = new zzd();
        }

        Entry(int versionCode, String className, ArrayList<FieldMapPair> fieldMapping) {
            this.versionCode = versionCode;
            this.className = className;
            this.zzalh = fieldMapping;
        }

        Entry(String className, Map<String, Field<?, ?>> fieldMap) {
            this.versionCode = 1;
            this.className = className;
            this.zzalh = zzF(fieldMap);
        }

        private static ArrayList<FieldMapPair> zzF(Map<String, Field<?, ?>> map) {
            if (map == null) {
                return null;
            }
            ArrayList<FieldMapPair> arrayList = new ArrayList();
            for (String str : map.keySet()) {
                arrayList.add(new FieldMapPair(str, (Field) map.get(str)));
            }
            return arrayList;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            zzd.zza$3d42fe6(this, out);
        }

        final HashMap<String, Field<?, ?>> zzrm() {
            HashMap<String, Field<?, ?>> hashMap = new HashMap();
            int size = this.zzalh.size();
            for (int i = 0; i < size; i++) {
                FieldMapPair fieldMapPair = (FieldMapPair) this.zzalh.get(i);
                hashMap.put(fieldMapPair.key, fieldMapPair.zzali);
            }
            return hashMap;
        }
    }

    public static class FieldMapPair implements SafeParcelable {
        public static final zzb CREATOR;
        final String key;
        final int versionCode;
        final Field<?, ?> zzali;

        static {
            CREATOR = new zzb();
        }

        FieldMapPair(int versionCode, String key, Field<?, ?> value) {
            this.versionCode = versionCode;
            this.key = key;
            this.zzali = value;
        }

        FieldMapPair(String key, Field<?, ?> value) {
            this.versionCode = 1;
            this.key = key;
            this.zzali = value;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            zzb.zza(this, out, flags);
        }
    }

    static {
        CREATOR = new zzc();
    }

    FieldMappingDictionary(int versionCode, ArrayList<Entry> serializedDictionary, String rootClassName) {
        this.mVersionCode = versionCode;
        this.zzalf = null;
        this.zzale = zze(serializedDictionary);
        this.zzalg = (String) zzx.zzy(rootClassName);
        zzri();
    }

    private static HashMap<String, Map<String, Field<?, ?>>> zze(ArrayList<Entry> arrayList) {
        HashMap<String, Map<String, Field<?, ?>>> hashMap = new HashMap();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            Entry entry = (Entry) arrayList.get(i);
            hashMap.put(entry.className, entry.zzrm());
        }
        return hashMap;
    }

    private void zzri() {
        for (String str : this.zzale.keySet()) {
            Map map = (Map) this.zzale.get(str);
            for (String str2 : map.keySet()) {
                ((Field) map.get(str2)).zzalc = this;
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : this.zzale.keySet()) {
            stringBuilder.append(str).append(":\n");
            Map map = (Map) this.zzale.get(str);
            for (String str2 : map.keySet()) {
                stringBuilder.append("  ").append(str2).append(": ");
                stringBuilder.append(map.get(str2));
            }
        }
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        zzc.zza$51a5452c(this, out);
    }

    public final Map<String, Field<?, ?>> zzcL(String str) {
        return (Map) this.zzale.get(str);
    }
}
