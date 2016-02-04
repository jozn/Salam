package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zznl;
import com.google.android.gms.internal.zzno;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleSignInAccount implements SafeParcelable {
    public static final Creator<GoogleSignInAccount> CREATOR;
    public static zznl zzVs;
    private static Comparator<Scope> zzVz;
    final int versionCode;
    List<Scope> zzTV;
    String zzUN;
    String zzVt;
    String zzVu;
    Uri zzVv;
    String zzVw;
    long zzVx;
    String zzVy;
    String zzxX;

    /* renamed from: com.google.android.gms.auth.api.signin.GoogleSignInAccount.1 */
    static class C02381 implements Comparator<Scope> {
        C02381() {
        }

        public final /* synthetic */ int compare(Object x0, Object x1) {
            return ((Scope) x0).zzaeW.compareTo(((Scope) x1).zzaeW);
        }
    }

    static {
        CREATOR = new zzc();
        zzVs = zzno.zzrM();
        zzVz = new C02381();
    }

    GoogleSignInAccount(int versionCode, String id, String idToken, String email, String displayName, Uri photoUrl, String serverAuthCode, long expirationTimeSecs, String obfuscatedIdentifier, List<Scope> grantedScopes) {
        this.versionCode = versionCode;
        this.zzxX = id;
        this.zzUN = idToken;
        this.zzVt = email;
        this.zzVu = displayName;
        this.zzVv = photoUrl;
        this.zzVw = serverAuthCode;
        this.zzVx = expirationTimeSecs;
        this.zzVy = obfuscatedIdentifier;
        this.zzTV = grantedScopes;
    }

    private JSONObject zzms() {
        JSONObject jSONObject = new JSONObject();
        try {
            if (this.zzxX != null) {
                jSONObject.put("id", this.zzxX);
            }
            if (this.zzUN != null) {
                jSONObject.put("tokenId", this.zzUN);
            }
            if (this.zzVt != null) {
                jSONObject.put(NotificationCompatApi21.CATEGORY_EMAIL, this.zzVt);
            }
            if (this.zzVu != null) {
                jSONObject.put("displayName", this.zzVu);
            }
            if (this.zzVv != null) {
                jSONObject.put("photoUrl", this.zzVv.toString());
            }
            if (this.zzVw != null) {
                jSONObject.put("serverAuthCode", this.zzVw);
            }
            jSONObject.put("expirationTime", this.zzVx);
            jSONObject.put("obfuscatedIdentifier", this.zzVy);
            JSONArray jSONArray = new JSONArray();
            Collections.sort(this.zzTV, zzVz);
            for (Scope scope : this.zzTV) {
                jSONArray.put(scope.zzaeW);
            }
            jSONObject.put("grantedScopes", jSONArray);
            return jSONObject;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return !(obj instanceof GoogleSignInAccount) ? false : ((GoogleSignInAccount) obj).zzms().toString().equals(zzms().toString());
    }

    public void writeToParcel(Parcel out, int flags) {
        zzc.zza(this, out, flags);
    }
}
