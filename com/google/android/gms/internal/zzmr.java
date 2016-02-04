package com.google.android.gms.internal;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzx;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class zzmr extends Fragment implements OnCancelListener {
    private static final GoogleApiAvailability zzagU;
    private boolean mStarted;
    private boolean zzagV;
    private int zzagW;
    private ConnectionResult zzagX;
    private final Handler zzagY;
    private zzmk zzagZ;
    private final SparseArray<zza> zzaha;

    private class zza implements OnConnectionFailedListener {
        public final int zzahb;
        public final GoogleApiClient zzahc;
        public final OnConnectionFailedListener zzahd;
        final /* synthetic */ zzmr zzahe;

        public zza(zzmr com_google_android_gms_internal_zzmr, int i, GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
            this.zzahe = com_google_android_gms_internal_zzmr;
            this.zzahb = i;
            this.zzahc = googleApiClient;
            this.zzahd = onConnectionFailedListener;
            googleApiClient.registerConnectionFailedListener$40dd7b8f();
        }

        public final void onConnectionFailed(ConnectionResult result) {
            this.zzahe.zzagY.post(new zzb(this.zzahe, this.zzahb, result));
        }
    }

    private class zzb implements Runnable {
        final /* synthetic */ zzmr zzahe;
        private final int zzahf;
        private final ConnectionResult zzahg;

        /* renamed from: com.google.android.gms.internal.zzmr.zzb.1 */
        class C04631 extends zzmk {
            final /* synthetic */ Dialog zzahh;
            final /* synthetic */ zzb zzahi;

            C04631(zzb com_google_android_gms_internal_zzmr_zzb, Dialog dialog) {
                this.zzahi = com_google_android_gms_internal_zzmr_zzb;
                this.zzahh = dialog;
            }

            protected final void zzpv() {
                this.zzahi.zzahe.zzpA();
                this.zzahh.dismiss();
            }
        }

        public zzb(zzmr com_google_android_gms_internal_zzmr, int i, ConnectionResult connectionResult) {
            this.zzahe = com_google_android_gms_internal_zzmr;
            this.zzahf = i;
            this.zzahg = connectionResult;
        }

        public final void run() {
            if (this.zzahe.mStarted && !this.zzahe.zzagV) {
                this.zzahe.zzagV = true;
                this.zzahe.zzagW = this.zzahf;
                this.zzahe.zzagX = this.zzahg;
                if (this.zzahg.hasResolution()) {
                    try {
                        int indexOf = ((this.zzahe.getActivity().getSupportFragmentManager().getFragments().indexOf(this.zzahe) + 1) << 16) + 1;
                        ConnectionResult connectionResult = this.zzahg;
                        Activity activity = this.zzahe.getActivity();
                        if (connectionResult.hasResolution()) {
                            activity.startIntentSenderForResult(connectionResult.mPendingIntent.getIntentSender(), indexOf, null, 0, 0, 0);
                            return;
                        }
                        return;
                    } catch (SendIntentException e) {
                        this.zzahe.zzpA();
                        return;
                    }
                }
                zzmr.zzagU;
                if (GoogleApiAvailability.isUserResolvableError(this.zzahg.zzabx)) {
                    GooglePlayServicesUtil.showErrorDialogFragment$7f66c94e(this.zzahg.zzabx, this.zzahe.getActivity(), this.zzahe, this.zzahe);
                } else if (this.zzahg.zzabx == 18) {
                    zzmr.zzagU;
                    this.zzahe.zzagZ = zzmk.zza(this.zzahe.getActivity().getApplicationContext(), new C04631(this, GoogleApiAvailability.zza(this.zzahe.getActivity(), this.zzahe)));
                } else {
                    this.zzahe.zza(this.zzahf, this.zzahg);
                }
            }
        }
    }

    static {
        zzagU = GoogleApiAvailability.getInstance();
    }

    public zzmr() {
        this.zzagW = -1;
        this.zzagY = new Handler(Looper.getMainLooper());
        this.zzaha = new SparseArray();
    }

    public static zzmr zza(FragmentActivity fragmentActivity) {
        zzx.zzcx("Must be called from main thread of process");
        try {
            zzmr com_google_android_gms_internal_zzmr = (zzmr) fragmentActivity.getSupportFragmentManager().findFragmentByTag("GmsSupportLifecycleFrag");
            return (com_google_android_gms_internal_zzmr == null || com_google_android_gms_internal_zzmr.isRemoving()) ? null : com_google_android_gms_internal_zzmr;
        } catch (Throwable e) {
            throw new IllegalStateException("Fragment with tag GmsSupportLifecycleFrag is not a SupportLifecycleFragment", e);
        }
    }

    private void zza(int i, ConnectionResult connectionResult) {
        Log.w("GmsSupportLifecycleFrag", "Unresolved error while connecting client. Stopping auto-manage.");
        zza com_google_android_gms_internal_zzmr_zza = (zza) this.zzaha.get(i);
        if (com_google_android_gms_internal_zzmr_zza != null) {
            zzbE(i);
            OnConnectionFailedListener onConnectionFailedListener = com_google_android_gms_internal_zzmr_zza.zzahd;
            if (onConnectionFailedListener != null) {
                onConnectionFailedListener.onConnectionFailed(connectionResult);
            }
        }
        zzpA();
    }

    public static zzmr zzb(FragmentActivity fragmentActivity) {
        zzmr zza = zza(fragmentActivity);
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        if (zza != null) {
            return zza;
        }
        Fragment com_google_android_gms_internal_zzmr = new zzmr();
        supportFragmentManager.beginTransaction().add(com_google_android_gms_internal_zzmr, "GmsSupportLifecycleFrag").commitAllowingStateLoss();
        supportFragmentManager.executePendingTransactions();
        return com_google_android_gms_internal_zzmr;
    }

    private void zzpA() {
        this.zzagV = false;
        this.zzagW = -1;
        this.zzagX = null;
        if (this.zzagZ != null) {
            this.zzagZ.unregister();
            this.zzagZ = null;
        }
        for (int i = 0; i < this.zzaha.size(); i++) {
            ((zza) this.zzaha.valueAt(i)).zzahc.connect();
        }
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
        for (int i = 0; i < this.zzaha.size(); i++) {
            zza com_google_android_gms_internal_zzmr_zza = (zza) this.zzaha.valueAt(i);
            writer.append(prefix).append("GoogleApiClient #").print(com_google_android_gms_internal_zzmr_zza.zzahb);
            writer.println(":");
            GoogleApiClient googleApiClient = com_google_android_gms_internal_zzmr_zza.zzahc;
            new StringBuilder().append(prefix).append("  ");
            googleApiClient.dump$ec96877();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActivityResult(int r5, int r6, android.content.Intent r7) {
        /*
        r4 = this;
        r0 = 1;
        r1 = 0;
        switch(r5) {
            case 1: goto L_0x0017;
            case 2: goto L_0x000c;
            default: goto L_0x0005;
        };
    L_0x0005:
        r0 = r1;
    L_0x0006:
        if (r0 == 0) goto L_0x0027;
    L_0x0008:
        r4.zzpA();
    L_0x000b:
        return;
    L_0x000c:
        r2 = r4.getActivity();
        r2 = com.google.android.gms.common.GoogleApiAvailability.isGooglePlayServicesAvailable(r2);
        if (r2 != 0) goto L_0x0005;
    L_0x0016:
        goto L_0x0006;
    L_0x0017:
        r2 = -1;
        if (r6 == r2) goto L_0x0006;
    L_0x001a:
        if (r6 != 0) goto L_0x0005;
    L_0x001c:
        r0 = new com.google.android.gms.common.ConnectionResult;
        r2 = 13;
        r3 = 0;
        r0.<init>(r2, r3);
        r4.zzagX = r0;
        goto L_0x0005;
    L_0x0027:
        r0 = r4.zzagW;
        r1 = r4.zzagX;
        r4.zza(r0, r1);
        goto L_0x000b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzmr.onActivityResult(int, int, android.content.Intent):void");
    }

    public void onCancel(DialogInterface dialogInterface) {
        zza(this.zzagW, new ConnectionResult(13, null));
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.zzagV = savedInstanceState.getBoolean("resolving_error", false);
            this.zzagW = savedInstanceState.getInt("failed_client_id", -1);
            if (this.zzagW >= 0) {
                this.zzagX = new ConnectionResult(savedInstanceState.getInt("failed_status"), (PendingIntent) savedInstanceState.getParcelable("failed_resolution"));
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("resolving_error", this.zzagV);
        if (this.zzagW >= 0) {
            outState.putInt("failed_client_id", this.zzagW);
            outState.putInt("failed_status", this.zzagX.zzabx);
            outState.putParcelable("failed_resolution", this.zzagX.mPendingIntent);
        }
    }

    public void onStart() {
        super.onStart();
        this.mStarted = true;
        if (!this.zzagV) {
            for (int i = 0; i < this.zzaha.size(); i++) {
                ((zza) this.zzaha.valueAt(i)).zzahc.connect();
            }
        }
    }

    public void onStop() {
        super.onStop();
        this.mStarted = false;
        for (int i = 0; i < this.zzaha.size(); i++) {
            ((zza) this.zzaha.valueAt(i)).zzahc.disconnect();
        }
    }

    public void zza(int i, GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
        zzx.zzb((Object) googleApiClient, (Object) "GoogleApiClient instance cannot be null");
        Object obj = this.zzaha.indexOfKey(i) < 0 ? 1 : null;
        String str = "Already managing a GoogleApiClient with id " + i;
        if (obj == null) {
            throw new IllegalStateException(String.valueOf(str));
        }
        this.zzaha.put(i, new zza(this, i, googleApiClient, onConnectionFailedListener));
        if (this.mStarted && !this.zzagV) {
            googleApiClient.connect();
        }
    }

    public void zzbE(int i) {
        zza com_google_android_gms_internal_zzmr_zza = (zza) this.zzaha.get(i);
        this.zzaha.remove(i);
        if (com_google_android_gms_internal_zzmr_zza != null) {
            com_google_android_gms_internal_zzmr_zza.zzahc.unregisterConnectionFailedListener$40dd7b8f();
            com_google_android_gms_internal_zzmr_zza.zzahc.disconnect();
        }
    }
}
