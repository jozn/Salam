package com.google.android.gms.common.server.response;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.server.converter.ConverterWrapper;
import com.google.android.gms.internal.zzni;
import com.google.android.gms.internal.zznu;
import com.google.android.gms.internal.zznv;
import com.kyleduo.switchbutton.C0473R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class FastJsonResponse {

    public interface zza<I, O> {
        I convertBack(O o);
    }

    public static class Field<I, O> implements SafeParcelable {
        public static final zza CREATOR;
        final int mVersionCode;
        protected final int zzakU;
        protected final boolean zzakV;
        protected final int zzakW;
        protected final boolean zzakX;
        protected final String zzakY;
        protected final int zzakZ;
        protected final Class<? extends FastJsonResponse> zzala;
        protected final String zzalb;
        FieldMappingDictionary zzalc;
        zza<I, O> zzald;

        static {
            CREATOR = new zza();
        }

        Field(int versionCode, int typeIn, boolean typeInArray, int typeOut, boolean typeOutArray, String outputFieldName, int safeParcelableFieldId, String concreteTypeName, ConverterWrapper wrappedConverter) {
            zza com_google_android_gms_common_server_response_FastJsonResponse_zza = null;
            this.mVersionCode = versionCode;
            this.zzakU = typeIn;
            this.zzakV = typeInArray;
            this.zzakW = typeOut;
            this.zzakX = typeOutArray;
            this.zzakY = outputFieldName;
            this.zzakZ = safeParcelableFieldId;
            if (concreteTypeName == null) {
                this.zzala = null;
                this.zzalb = null;
            } else {
                this.zzala = SafeParcelResponse.class;
                this.zzalb = concreteTypeName;
            }
            if (wrappedConverter != null) {
                if (wrappedConverter.zzakO != null) {
                    com_google_android_gms_common_server_response_FastJsonResponse_zza = wrappedConverter.zzakO;
                } else {
                    throw new IllegalStateException("There was no converter wrapped in this ConverterWrapper.");
                }
            }
            this.zzald = com_google_android_gms_common_server_response_FastJsonResponse_zza;
        }

        public int describeContents() {
            return 0;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Field\n");
            stringBuilder.append("            versionCode=").append(this.mVersionCode).append('\n');
            stringBuilder.append("                 typeIn=").append(this.zzakU).append('\n');
            stringBuilder.append("            typeInArray=").append(this.zzakV).append('\n');
            stringBuilder.append("                typeOut=").append(this.zzakW).append('\n');
            stringBuilder.append("           typeOutArray=").append(this.zzakX).append('\n');
            stringBuilder.append("        outputFieldName=").append(this.zzakY).append('\n');
            stringBuilder.append("      safeParcelFieldId=").append(this.zzakZ).append('\n');
            stringBuilder.append("       concreteTypeName=").append(zzre()).append('\n');
            if (this.zzala != null) {
                stringBuilder.append("     concreteType.class=").append(this.zzala.getCanonicalName()).append('\n');
            }
            stringBuilder.append("          converterName=").append(this.zzald == null ? "null" : this.zzald.getClass().getCanonicalName()).append('\n');
            return stringBuilder.toString();
        }

        public void writeToParcel(Parcel out, int flags) {
            zza.zza(this, out, flags);
        }

        public final int zzqT() {
            return this.zzakU;
        }

        public final int zzqU() {
            return this.zzakW;
        }

        public final boolean zzqZ() {
            return this.zzakV;
        }

        public final boolean zzra() {
            return this.zzakX;
        }

        public final String zzrb() {
            return this.zzakY;
        }

        public final int zzrc() {
            return this.zzakZ;
        }

        public final Class<? extends FastJsonResponse> zzrd() {
            return this.zzala;
        }

        final String zzre() {
            return this.zzalb == null ? null : this.zzalb;
        }

        public final Map<String, Field<?, ?>> zzrh() {
            zzx.zzy(this.zzalb);
            zzx.zzy(this.zzalc);
            return this.zzalc.zzcL(this.zzalb);
        }
    }

    protected static <O, I> I zza(Field<I, O> field, Object obj) {
        return field.zzald != null ? field.zzald.convertBack(obj) : obj;
    }

    private static void zza(StringBuilder stringBuilder, Field field, Object obj) {
        if (field.zzqT() == 11) {
            stringBuilder.append(((FastJsonResponse) field.zzrd().cast(obj)).toString());
        } else if (field.zzqT() == 7) {
            stringBuilder.append("\"");
            stringBuilder.append(zznu.zzcO((String) obj));
            stringBuilder.append("\"");
        } else {
            stringBuilder.append(obj);
        }
    }

    private static void zza(StringBuilder stringBuilder, Field field, ArrayList<Object> arrayList) {
        stringBuilder.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                stringBuilder.append(",");
            }
            Object obj = arrayList.get(i);
            if (obj != null) {
                zza(stringBuilder, field, obj);
            }
        }
        stringBuilder.append("]");
    }

    private Object zzb(Field field) {
        String zzrb = field.zzrb();
        if (field.zzrd() != null) {
            field.zzrb();
            String str = "Concrete field shouldn't be value object: %s";
            Object[] objArr = new Object[]{field.zzrb()};
            if ((zzcH$9543ced() == null ? 1 : 0) == 0) {
                throw new IllegalStateException(String.format(str, objArr));
            }
            field.zzra();
            try {
                return getClass().getMethod("get" + Character.toUpperCase(zzrb.charAt(0)) + zzrb.substring(1), new Class[0]).invoke(this, new Object[0]);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        field.zzrb();
        return zzcH$9543ced();
    }

    public String toString() {
        Map zzqV = zzqV();
        StringBuilder stringBuilder = new StringBuilder(100);
        for (String str : zzqV.keySet()) {
            Field field = (Field) zzqV.get(str);
            if (field.zzqU() != 11) {
                field.zzrb();
                if (zzcI$552c4dfd()) {
                    Object zza = zza(field, zzb(field));
                    if (stringBuilder.length() == 0) {
                        stringBuilder.append("{");
                    } else {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append("\"").append(str).append("\":");
                    if (zza != null) {
                        switch (field.zzqU()) {
                            case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                                stringBuilder.append("\"").append(zzni.zzj((byte[]) zza)).append("\"");
                                break;
                            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                                stringBuilder.append("\"").append(zzni.zzk((byte[]) zza)).append("\"");
                                break;
                            case C0473R.styleable.SwitchButton_onColor /*10*/:
                                zznv.zza(stringBuilder, (HashMap) zza);
                                break;
                            default:
                                if (!field.zzqZ()) {
                                    zza(stringBuilder, field, zza);
                                    break;
                                }
                                zza(stringBuilder, field, (ArrayList) zza);
                                break;
                        }
                    }
                    stringBuilder.append("null");
                }
            } else if (field.zzra()) {
                field.zzrb();
                throw new UnsupportedOperationException("Concrete type arrays not supported");
            } else {
                field.zzrb();
                throw new UnsupportedOperationException("Concrete types not supported");
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.append("}");
        } else {
            stringBuilder.append("{}");
        }
        return stringBuilder.toString();
    }

    protected abstract Object zzcH$9543ced();

    protected abstract boolean zzcI$552c4dfd();

    public abstract Map<String, Field<?, ?>> zzqV();
}
