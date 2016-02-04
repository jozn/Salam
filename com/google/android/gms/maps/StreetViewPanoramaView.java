package com.google.android.gms.maps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.dynamic.zzf;
import com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate;
import com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate;
import com.google.android.gms.maps.internal.StreetViewLifecycleDelegate;
import com.google.android.gms.maps.internal.zzy;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import java.util.ArrayList;
import java.util.List;

public class StreetViewPanoramaView extends FrameLayout {
    private final zzb zzaPM;

    static class zza implements StreetViewLifecycleDelegate {
        final IStreetViewPanoramaViewDelegate zzaPN;
        private View zzaPO;
        private final ViewGroup zzaPm;

        /* renamed from: com.google.android.gms.maps.StreetViewPanoramaView.zza.1 */
        class C04681 extends com.google.android.gms.maps.internal.zzv.zza {
            final /* synthetic */ OnStreetViewPanoramaReadyCallback zzaPC;
            final /* synthetic */ zza zzaPP;

            C04681(zza com_google_android_gms_maps_StreetViewPanoramaView_zza, OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
                this.zzaPP = com_google_android_gms_maps_StreetViewPanoramaView_zza;
                this.zzaPC = onStreetViewPanoramaReadyCallback;
            }

            public final void zza(IStreetViewPanoramaDelegate iStreetViewPanoramaDelegate) throws RemoteException {
                StreetViewPanorama streetViewPanorama = new StreetViewPanorama(iStreetViewPanoramaDelegate);
            }
        }

        public zza(ViewGroup viewGroup, IStreetViewPanoramaViewDelegate iStreetViewPanoramaViewDelegate) {
            this.zzaPN = (IStreetViewPanoramaViewDelegate) zzx.zzy(iStreetViewPanoramaViewDelegate);
            this.zzaPm = (ViewGroup) zzx.zzy(viewGroup);
        }

        public final void onCreate(Bundle savedInstanceState) {
            try {
                this.zzaPN.onCreate(savedInstanceState);
                this.zzaPO = (View) zze.zzp(this.zzaPN.getView());
                this.zzaPm.removeAllViews();
                this.zzaPm.addView(this.zzaPO);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            throw new UnsupportedOperationException("onCreateView not allowed on StreetViewPanoramaViewDelegate");
        }

        public final void onDestroy() {
            try {
                this.zzaPN.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onDestroyView() {
            throw new UnsupportedOperationException("onDestroyView not allowed on StreetViewPanoramaViewDelegate");
        }

        public final void onInflate(Activity activity, Bundle attrs, Bundle savedInstanceState) {
            throw new UnsupportedOperationException("onInflate not allowed on StreetViewPanoramaViewDelegate");
        }

        public final void onLowMemory() {
            try {
                this.zzaPN.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onPause() {
            try {
                this.zzaPN.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onResume() {
            try {
                this.zzaPN.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onSaveInstanceState(Bundle outState) {
            try {
                this.zzaPN.onSaveInstanceState(outState);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
    }

    static class zzb extends com.google.android.gms.dynamic.zza<zza> {
        private final Context mContext;
        private final List<OnStreetViewPanoramaReadyCallback> zzaPE;
        private final StreetViewPanoramaOptions zzaPQ;
        protected zzf<zza> zzaPj;
        private final ViewGroup zzaPq;

        zzb(ViewGroup viewGroup, Context context) {
            this.zzaPE = new ArrayList();
            this.zzaPq = viewGroup;
            this.mContext = context;
            this.zzaPQ = null;
        }

        protected final void zza(zzf<zza> com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_StreetViewPanoramaView_zza) {
            this.zzaPj = com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_StreetViewPanoramaView_zza;
            if (this.zzaPj != null && this.zzatr == null) {
                try {
                    this.zzaPj.zza(new zza(this.zzaPq, zzy.zzaP(this.mContext).zza(zze.zzB(this.mContext), this.zzaPQ)));
                    for (OnStreetViewPanoramaReadyCallback c04681 : this.zzaPE) {
                        zza com_google_android_gms_maps_StreetViewPanoramaView_zza = (zza) this.zzatr;
                        com_google_android_gms_maps_StreetViewPanoramaView_zza.zzaPN.getStreetViewPanoramaAsync(new C04681(com_google_android_gms_maps_StreetViewPanoramaView_zza, c04681));
                    }
                    this.zzaPE.clear();
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                } catch (GooglePlayServicesNotAvailableException e2) {
                } catch (RemoteException e3) {
                    throw new RuntimeRemoteException(e3);
                }
            }
        }
    }

    public StreetViewPanoramaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.zzaPM = new zzb(this, context);
    }

    public StreetViewPanoramaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.zzaPM = new zzb(this, context);
    }
}
