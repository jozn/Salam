package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.internal.zze;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GoogleSignInOptions implements SafeParcelable {
    public static final Creator<GoogleSignInOptions> CREATOR;
    public static final GoogleSignInOptions DEFAULT_SIGN_IN;
    public static final Scope zzVA;
    public static final Scope zzVB;
    public static final Scope zzVC;
    final int versionCode;
    Account zzSo;
    boolean zzVD;
    final boolean zzVE;
    final boolean zzVF;
    String zzVG;
    private final ArrayList<Scope> zzVr;

    public static final class Builder {
        Account zzSo;
        boolean zzVD;
        boolean zzVE;
        boolean zzVF;
        String zzVG;
        Set<Scope> zzVH;

        public Builder() {
            this.zzVH = new HashSet();
        }
    }

    static {
        zzVA = new Scope("profile");
        zzVB = new Scope(NotificationCompatApi21.CATEGORY_EMAIL);
        zzVC = new Scope("openid");
        Builder builder = new Builder();
        builder.zzVH.add(zzVC);
        builder.zzVH.add(zzVA);
        DEFAULT_SIGN_IN = new GoogleSignInOptions(builder.zzSo, builder.zzVD, builder.zzVE, builder.zzVF, builder.zzVG, (byte) 0);
        CREATOR = new zzd();
    }

    GoogleSignInOptions(int versionCode, ArrayList<Scope> scopes, Account account, boolean idTokenRequested, boolean serverAuthCodeRequested, boolean forceCodeForRefreshToken, String serverClientId) {
        this.versionCode = versionCode;
        this.zzVr = scopes;
        this.zzSo = account;
        this.zzVD = idTokenRequested;
        this.zzVE = serverAuthCodeRequested;
        this.zzVF = forceCodeForRefreshToken;
        this.zzVG = serverClientId;
    }

    private GoogleSignInOptions(Set<Scope> scopes, Account account, boolean idTokenRequested, boolean serverAuthCodeRequested, boolean forceCodeForRefreshToken, String serverClientId) {
        this(1, new ArrayList(scopes), account, idTokenRequested, serverAuthCodeRequested, forceCodeForRefreshToken, serverClientId);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            GoogleSignInOptions googleSignInOptions = (GoogleSignInOptions) obj;
            if (this.zzVr.size() != googleSignInOptions.zzmu().size() || !this.zzVr.containsAll(googleSignInOptions.zzmu())) {
                return false;
            }
            if (this.zzSo == null) {
                if (googleSignInOptions.zzSo != null) {
                    return false;
                }
            } else if (!this.zzSo.equals(googleSignInOptions.zzSo)) {
                return false;
            }
            if (TextUtils.isEmpty(this.zzVG)) {
                if (!TextUtils.isEmpty(googleSignInOptions.zzVG)) {
                    return false;
                }
            } else if (!this.zzVG.equals(googleSignInOptions.zzVG)) {
                return false;
            }
            return this.zzVF == googleSignInOptions.zzVF && this.zzVD == googleSignInOptions.zzVD && this.zzVE == googleSignInOptions.zzVE;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        List arrayList = new ArrayList();
        Iterator it = this.zzVr.iterator();
        while (it.hasNext()) {
            arrayList.add(((Scope) it.next()).zzaeW);
        }
        Collections.sort(arrayList);
        return new zze().zzo(arrayList).zzo(this.zzSo).zzo(this.zzVG).zzP(this.zzVF).zzP(this.zzVD).zzP(this.zzVE).zzWb;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzd.zza(this, out, flags);
    }

    public final ArrayList<Scope> zzmu() {
        return new ArrayList(this.zzVr);
    }
}
