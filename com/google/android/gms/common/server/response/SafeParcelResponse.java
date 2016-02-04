package com.google.android.gms.common.server.response;

import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.server.response.FastJsonResponse.Field;
import com.google.android.gms.internal.zznh;
import com.google.android.gms.internal.zzni;
import com.google.android.gms.internal.zznu;
import com.google.android.gms.internal.zznv;
import com.kyleduo.switchbutton.C0473R;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class SafeParcelResponse extends FastJsonResponse implements SafeParcelable {
    public static final zze CREATOR;
    private final String mClassName;
    final int mVersionCode;
    final FieldMappingDictionary zzalc;
    private final Parcel zzalj;
    final int zzalk;
    private int zzall;
    private int zzalm;

    static {
        CREATOR = new zze();
    }

    SafeParcelResponse(int versionCode, Parcel parcel, FieldMappingDictionary fieldMappingDictionary) {
        this.mVersionCode = versionCode;
        this.zzalj = (Parcel) zzx.zzy(parcel);
        this.zzalk = 2;
        this.zzalc = fieldMappingDictionary;
        if (this.zzalc == null) {
            this.mClassName = null;
        } else {
            this.mClassName = this.zzalc.zzalg;
        }
        this.zzall = 2;
    }

    private static void zza(StringBuilder stringBuilder, int i, Object obj) {
        switch (i) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
            case Logger.SEVERE /*1*/:
            case Logger.WARNING /*2*/:
            case Logger.INFO /*3*/:
            case Logger.CONFIG /*4*/:
            case Logger.FINE /*5*/:
            case Logger.FINER /*6*/:
                stringBuilder.append(obj);
            case Logger.FINEST /*7*/:
                stringBuilder.append("\"").append(zznu.zzcO(obj.toString())).append("\"");
            case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                stringBuilder.append("\"").append(zzni.zzj((byte[]) obj)).append("\"");
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                stringBuilder.append("\"").append(zzni.zzk((byte[]) obj));
                stringBuilder.append("\"");
            case C0473R.styleable.SwitchButton_onColor /*10*/:
                zznv.zza(stringBuilder, (HashMap) obj);
            case C0473R.styleable.SwitchButton_offColor /*11*/:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown type = " + i);
        }
    }

    private void zza(StringBuilder stringBuilder, Map<String, Field<?, ?>> map, Parcel parcel) {
        HashMap hashMap = new HashMap();
        for (Entry entry : map.entrySet()) {
            Entry entry2;
            hashMap.put(Integer.valueOf(((Field) entry2.getValue()).zzrc()), entry2);
        }
        stringBuilder.append('{');
        int zzau = zza.zzau(parcel);
        Object obj = null;
        while (parcel.dataPosition() < zzau) {
            int readInt = parcel.readInt();
            entry2 = (Entry) hashMap.get(Integer.valueOf(SupportMenu.USER_MASK & readInt));
            if (entry2 != null) {
                if (obj != null) {
                    stringBuilder.append(",");
                }
                String str = (String) entry2.getKey();
                Field field = (Field) entry2.getValue();
                stringBuilder.append("\"").append(str).append("\":");
                if ((field.zzald != null ? 1 : null) != null) {
                    switch (field.zzqU()) {
                        case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                            zzb(stringBuilder, field, FastJsonResponse.zza(field, Integer.valueOf(zza.zzg(parcel, readInt))));
                            break;
                        case Logger.SEVERE /*1*/:
                            zzb(stringBuilder, field, FastJsonResponse.zza(field, zza.zzk(parcel, readInt)));
                            break;
                        case Logger.WARNING /*2*/:
                            zzb(stringBuilder, field, FastJsonResponse.zza(field, Long.valueOf(zza.zzi(parcel, readInt))));
                            break;
                        case Logger.INFO /*3*/:
                            zzb(stringBuilder, field, FastJsonResponse.zza(field, Float.valueOf(zza.zzl(parcel, readInt))));
                            break;
                        case Logger.CONFIG /*4*/:
                            zzb(stringBuilder, field, FastJsonResponse.zza(field, Double.valueOf(zza.zzn(parcel, readInt))));
                            break;
                        case Logger.FINE /*5*/:
                            zzb(stringBuilder, field, FastJsonResponse.zza(field, zza.zzo(parcel, readInt)));
                            break;
                        case Logger.FINER /*6*/:
                            zzb(stringBuilder, field, FastJsonResponse.zza(field, Boolean.valueOf(zza.zzc(parcel, readInt))));
                            break;
                        case Logger.FINEST /*7*/:
                            zzb(stringBuilder, field, FastJsonResponse.zza(field, zza.zzp(parcel, readInt)));
                            break;
                        case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                        case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                            zzb(stringBuilder, field, FastJsonResponse.zza(field, zza.zzs(parcel, readInt)));
                            break;
                        case C0473R.styleable.SwitchButton_onColor /*10*/:
                            zzb(stringBuilder, field, FastJsonResponse.zza(field, zzl(zza.zzr(parcel, readInt))));
                            break;
                        case C0473R.styleable.SwitchButton_offColor /*11*/:
                            throw new IllegalArgumentException("Method does not accept concrete type.");
                        default:
                            throw new IllegalArgumentException("Unknown field out type = " + field.zzqU());
                    }
                }
                zzb(stringBuilder, field, parcel, readInt);
                obj = 1;
            }
        }
        if (parcel.dataPosition() != zzau) {
            throw new zza.zza("Overread allowed size end=" + zzau, parcel);
        }
        stringBuilder.append('}');
    }

    private void zzb(StringBuilder stringBuilder, Field<?, ?> field, Parcel parcel, int i) {
        boolean[] zArr = null;
        int i2 = 0;
        int length;
        if (field.zzra()) {
            stringBuilder.append("[");
            int dataPosition;
            switch (field.zzqU()) {
                case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                    int[] zzv = zza.zzv(parcel, i);
                    length = zzv.length;
                    while (i2 < length) {
                        if (i2 != 0) {
                            stringBuilder.append(",");
                        }
                        stringBuilder.append(Integer.toString(zzv[i2]));
                        i2++;
                    }
                    break;
                case Logger.SEVERE /*1*/:
                    Object[] objArr;
                    length = zza.zza(parcel, i);
                    dataPosition = parcel.dataPosition();
                    if (length != 0) {
                        int readInt = parcel.readInt();
                        objArr = new BigInteger[readInt];
                        while (i2 < readInt) {
                            objArr[i2] = new BigInteger(parcel.createByteArray());
                            i2++;
                        }
                        parcel.setDataPosition(length + dataPosition);
                    }
                    zznh.zza(stringBuilder, objArr);
                    break;
                case Logger.WARNING /*2*/:
                    long[] createLongArray;
                    length = zza.zza(parcel, i);
                    i2 = parcel.dataPosition();
                    if (length != 0) {
                        createLongArray = parcel.createLongArray();
                        parcel.setDataPosition(length + i2);
                    }
                    zznh.zza(stringBuilder, createLongArray);
                    break;
                case Logger.INFO /*3*/:
                    float[] createFloatArray;
                    length = zza.zza(parcel, i);
                    i2 = parcel.dataPosition();
                    if (length != 0) {
                        createFloatArray = parcel.createFloatArray();
                        parcel.setDataPosition(length + i2);
                    }
                    zznh.zza(stringBuilder, createFloatArray);
                    break;
                case Logger.CONFIG /*4*/:
                    double[] createDoubleArray;
                    length = zza.zza(parcel, i);
                    i2 = parcel.dataPosition();
                    if (length != 0) {
                        createDoubleArray = parcel.createDoubleArray();
                        parcel.setDataPosition(length + i2);
                    }
                    zznh.zza(stringBuilder, createDoubleArray);
                    break;
                case Logger.FINE /*5*/:
                    zznh.zza(stringBuilder, zza.zzA(parcel, i));
                    break;
                case Logger.FINER /*6*/:
                    length = zza.zza(parcel, i);
                    i2 = parcel.dataPosition();
                    if (length != 0) {
                        zArr = parcel.createBooleanArray();
                        parcel.setDataPosition(length + i2);
                    }
                    zznh.zza(stringBuilder, zArr);
                    break;
                case Logger.FINEST /*7*/:
                    zznh.zza(stringBuilder, zza.zzB(parcel, i));
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    throw new UnsupportedOperationException("List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported");
                case C0473R.styleable.SwitchButton_offColor /*11*/:
                    Parcel[] zzF = zza.zzF(parcel, i);
                    dataPosition = zzF.length;
                    for (int i3 = 0; i3 < dataPosition; i3++) {
                        if (i3 > 0) {
                            stringBuilder.append(",");
                        }
                        zzF[i3].setDataPosition(0);
                        zza(stringBuilder, field.zzrh(), zzF[i3]);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown field type out.");
            }
            stringBuilder.append("]");
            return;
        }
        switch (field.zzqU()) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                stringBuilder.append(zza.zzg(parcel, i));
            case Logger.SEVERE /*1*/:
                stringBuilder.append(zza.zzk(parcel, i));
            case Logger.WARNING /*2*/:
                stringBuilder.append(zza.zzi(parcel, i));
            case Logger.INFO /*3*/:
                stringBuilder.append(zza.zzl(parcel, i));
            case Logger.CONFIG /*4*/:
                stringBuilder.append(zza.zzn(parcel, i));
            case Logger.FINE /*5*/:
                stringBuilder.append(zza.zzo(parcel, i));
            case Logger.FINER /*6*/:
                stringBuilder.append(zza.zzc(parcel, i));
            case Logger.FINEST /*7*/:
                stringBuilder.append("\"").append(zznu.zzcO(zza.zzp(parcel, i))).append("\"");
            case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                stringBuilder.append("\"").append(zzni.zzj(zza.zzs(parcel, i))).append("\"");
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                stringBuilder.append("\"").append(zzni.zzk(zza.zzs(parcel, i)));
                stringBuilder.append("\"");
            case C0473R.styleable.SwitchButton_onColor /*10*/:
                Bundle zzr = zza.zzr(parcel, i);
                Set<String> keySet = zzr.keySet();
                keySet.size();
                stringBuilder.append("{");
                length = 1;
                for (String str : keySet) {
                    if (length == 0) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append("\"").append(str).append("\"");
                    stringBuilder.append(":");
                    stringBuilder.append("\"").append(zznu.zzcO(zzr.getString(str))).append("\"");
                    length = 0;
                }
                stringBuilder.append("}");
            case C0473R.styleable.SwitchButton_offColor /*11*/:
                Parcel zzE = zza.zzE(parcel, i);
                zzE.setDataPosition(0);
                zza(stringBuilder, field.zzrh(), zzE);
            default:
                throw new IllegalStateException("Unknown field type out");
        }
    }

    private static void zzb(StringBuilder stringBuilder, Field<?, ?> field, Object obj) {
        if (field.zzqZ()) {
            ArrayList arrayList = (ArrayList) obj;
            stringBuilder.append("[");
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                if (i != 0) {
                    stringBuilder.append(",");
                }
                zza(stringBuilder, field.zzqT(), arrayList.get(i));
            }
            stringBuilder.append("]");
            return;
        }
        zza(stringBuilder, field.zzqT(), obj);
    }

    private static HashMap<String, String> zzl(Bundle bundle) {
        HashMap<String, String> hashMap = new HashMap();
        for (String str : bundle.keySet()) {
            hashMap.put(str, bundle.getString(str));
        }
        return hashMap;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        zzx.zzb(this.zzalc, (Object) "Cannot convert to JSON on client side.");
        Parcel zzrn = zzrn();
        zzrn.setDataPosition(0);
        StringBuilder stringBuilder = new StringBuilder(100);
        zza(stringBuilder, this.zzalc.zzcL(this.mClassName), zzrn);
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        zze.zza(this, out, flags);
    }

    protected final Object zzcH$9543ced() {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    protected final boolean zzcI$552c4dfd() {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    public final Map<String, Field<?, ?>> zzqV() {
        return this.zzalc == null ? null : this.zzalc.zzcL(this.mClassName);
    }

    public final Parcel zzrn() {
        switch (this.zzall) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                this.zzalm = zzb.zzG(this.zzalj, 20293);
                break;
            case Logger.SEVERE /*1*/:
                break;
        }
        zzb.zzH(this.zzalj, this.zzalm);
        this.zzall = 2;
        return this.zzalj;
    }
}
