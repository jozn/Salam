package com.google.android.gms.dynamic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzg;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class zza<T extends LifecycleDelegate> {
    public T zzatr;
    Bundle zzats;
    LinkedList<zza> zzatt;
    private final zzf<T> zzatu;

    /* renamed from: com.google.android.gms.dynamic.zza.1 */
    class C04561 implements zzf<T> {
        final /* synthetic */ zza zzatv;

        C04561(zza com_google_android_gms_dynamic_zza) {
            this.zzatv = com_google_android_gms_dynamic_zza;
        }

        public final void zza(T t) {
            this.zzatv.zzatr = t;
            Iterator it = this.zzatv.zzatt.iterator();
            while (it.hasNext()) {
                ((zza) it.next()).zzb$6728a24f();
            }
            this.zzatv.zzatt.clear();
            this.zzatv.zzats = null;
        }
    }

    private interface zza {
        int getState();

        void zzb$6728a24f();
    }

    /* renamed from: com.google.android.gms.dynamic.zza.2 */
    class C04572 implements zza {
        final /* synthetic */ zza zzatv;
        final /* synthetic */ Activity zzatw;
        final /* synthetic */ Bundle zzatx;
        final /* synthetic */ Bundle zzaty;

        C04572(zza com_google_android_gms_dynamic_zza, Activity activity, Bundle bundle, Bundle bundle2) {
            this.zzatv = com_google_android_gms_dynamic_zza;
            this.zzatw = activity;
            this.zzatx = bundle;
            this.zzaty = bundle2;
        }

        public final int getState() {
            return 0;
        }

        public final void zzb$6728a24f() {
            this.zzatv.zzatr.onInflate(this.zzatw, this.zzatx, this.zzaty);
        }
    }

    /* renamed from: com.google.android.gms.dynamic.zza.3 */
    class C04583 implements zza {
        final /* synthetic */ zza zzatv;
        final /* synthetic */ Bundle zzaty;

        C04583(zza com_google_android_gms_dynamic_zza, Bundle bundle) {
            this.zzatv = com_google_android_gms_dynamic_zza;
            this.zzaty = bundle;
        }

        public final int getState() {
            return 1;
        }

        public final void zzb$6728a24f() {
            this.zzatv.zzatr.onCreate(this.zzaty);
        }
    }

    /* renamed from: com.google.android.gms.dynamic.zza.4 */
    class C04594 implements zza {
        final /* synthetic */ LayoutInflater zzatA;
        final /* synthetic */ ViewGroup zzatB;
        final /* synthetic */ zza zzatv;
        final /* synthetic */ Bundle zzaty;
        final /* synthetic */ FrameLayout zzatz;

        C04594(zza com_google_android_gms_dynamic_zza, FrameLayout frameLayout, LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            this.zzatv = com_google_android_gms_dynamic_zza;
            this.zzatz = frameLayout;
            this.zzatA = layoutInflater;
            this.zzatB = viewGroup;
            this.zzaty = bundle;
        }

        public final int getState() {
            return 2;
        }

        public final void zzb$6728a24f() {
            this.zzatz.removeAllViews();
            this.zzatz.addView(this.zzatv.zzatr.onCreateView(this.zzatA, this.zzatB, this.zzaty));
        }
    }

    /* renamed from: com.google.android.gms.dynamic.zza.5 */
    static class C04605 implements OnClickListener {
        final /* synthetic */ Context zzsm;
        final /* synthetic */ int zzzC;

        C04605(Context context, int i) {
            this.zzsm = context;
            this.zzzC = i;
        }

        public final void onClick(View v) {
            this.zzsm.startActivity(GooglePlayServicesUtil.zzbv(this.zzzC));
        }
    }

    /* renamed from: com.google.android.gms.dynamic.zza.7 */
    class C04617 implements zza {
        final /* synthetic */ zza zzatv;

        C04617(zza com_google_android_gms_dynamic_zza) {
            this.zzatv = com_google_android_gms_dynamic_zza;
        }

        public final int getState() {
            return 5;
        }

        public final void zzb$6728a24f() {
            this.zzatv.zzatr.onResume();
        }
    }

    public zza() {
        this.zzatu = new C04561(this);
    }

    private void zza(Bundle bundle, zza com_google_android_gms_dynamic_zza_zza) {
        if (this.zzatr != null) {
            com_google_android_gms_dynamic_zza_zza.zzb$6728a24f();
            return;
        }
        if (this.zzatt == null) {
            this.zzatt = new LinkedList();
        }
        this.zzatt.add(com_google_android_gms_dynamic_zza_zza);
        if (bundle != null) {
            if (this.zzats == null) {
                this.zzats = (Bundle) bundle.clone();
            } else {
                this.zzats.putAll(bundle);
            }
        }
        zza(this.zzatu);
    }

    private void zzeF(int i) {
        while (!this.zzatt.isEmpty() && ((zza) this.zzatt.getLast()).getState() >= i) {
            this.zzatt.removeLast();
        }
    }

    public final void onCreate(Bundle savedInstanceState) {
        zza(savedInstanceState, new C04583(this, savedInstanceState));
    }

    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frameLayout = new FrameLayout(inflater.getContext());
        zza(savedInstanceState, new C04594(this, frameLayout, inflater, container, savedInstanceState));
        if (this.zzatr == null) {
            Context context = frameLayout.getContext();
            int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
            CharSequence zzc = zzg.zzc(context, isGooglePlayServicesAvailable, GooglePlayServicesUtil.zzam(context));
            CharSequence zzh = zzg.zzh(context, isGooglePlayServicesAvailable);
            View linearLayout = new LinearLayout(frameLayout.getContext());
            linearLayout.setOrientation(1);
            linearLayout.setLayoutParams(new LayoutParams(-2, -2));
            frameLayout.addView(linearLayout);
            View textView = new TextView(frameLayout.getContext());
            textView.setLayoutParams(new LayoutParams(-2, -2));
            textView.setText(zzc);
            linearLayout.addView(textView);
            if (zzh != null) {
                View button = new Button(context);
                button.setLayoutParams(new LayoutParams(-2, -2));
                button.setText(zzh);
                linearLayout.addView(button);
                button.setOnClickListener(new C04605(context, isGooglePlayServicesAvailable));
            }
        }
        return frameLayout;
    }

    public final void onDestroy() {
        if (this.zzatr != null) {
            this.zzatr.onDestroy();
        } else {
            zzeF(1);
        }
    }

    public final void onDestroyView() {
        if (this.zzatr != null) {
            this.zzatr.onDestroyView();
        } else {
            zzeF(2);
        }
    }

    public final void onInflate(Activity activity, Bundle attrs, Bundle savedInstanceState) {
        zza(savedInstanceState, new C04572(this, activity, attrs, savedInstanceState));
    }

    public final void onLowMemory() {
        if (this.zzatr != null) {
            this.zzatr.onLowMemory();
        }
    }

    public final void onPause() {
        if (this.zzatr != null) {
            this.zzatr.onPause();
        } else {
            zzeF(5);
        }
    }

    public final void onResume() {
        zza(null, new C04617(this));
    }

    public final void onSaveInstanceState(Bundle outState) {
        if (this.zzatr != null) {
            this.zzatr.onSaveInstanceState(outState);
        } else if (this.zzats != null) {
            outState.putAll(this.zzats);
        }
    }

    public abstract void zza(zzf<T> com_google_android_gms_dynamic_zzf_T);
}
