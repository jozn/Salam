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
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.IMapViewDelegate;
import com.google.android.gms.maps.internal.MapLifecycleDelegate;
import com.google.android.gms.maps.internal.zzy;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import java.util.ArrayList;
import java.util.List;

public class MapView extends FrameLayout {
    private final zzb zzaPl;

    static class zza implements MapLifecycleDelegate {
        private final ViewGroup zzaPm;
        final IMapViewDelegate zzaPn;
        private View zzaPo;

        /* renamed from: com.google.android.gms.maps.MapView.zza.1 */
        class C04661 extends com.google.android.gms.maps.internal.zzl.zza {
            final /* synthetic */ OnMapReadyCallback zzaPh;
            final /* synthetic */ zza zzaPp;

            C04661(zza com_google_android_gms_maps_MapView_zza, OnMapReadyCallback onMapReadyCallback) {
                this.zzaPp = com_google_android_gms_maps_MapView_zza;
                this.zzaPh = onMapReadyCallback;
            }

            public final void zza(IGoogleMapDelegate iGoogleMapDelegate) throws RemoteException {
                GoogleMap googleMap = new GoogleMap(iGoogleMapDelegate);
            }
        }

        public zza(ViewGroup viewGroup, IMapViewDelegate iMapViewDelegate) {
            this.zzaPn = (IMapViewDelegate) zzx.zzy(iMapViewDelegate);
            this.zzaPm = (ViewGroup) zzx.zzy(viewGroup);
        }

        public final void onCreate(Bundle savedInstanceState) {
            try {
                this.zzaPn.onCreate(savedInstanceState);
                this.zzaPo = (View) zze.zzp(this.zzaPn.getView());
                this.zzaPm.removeAllViews();
                this.zzaPm.addView(this.zzaPo);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            throw new UnsupportedOperationException("onCreateView not allowed on MapViewDelegate");
        }

        public final void onDestroy() {
            try {
                this.zzaPn.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onDestroyView() {
            throw new UnsupportedOperationException("onDestroyView not allowed on MapViewDelegate");
        }

        public final void onInflate(Activity activity, Bundle attrs, Bundle savedInstanceState) {
            throw new UnsupportedOperationException("onInflate not allowed on MapViewDelegate");
        }

        public final void onLowMemory() {
            try {
                this.zzaPn.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onPause() {
            try {
                this.zzaPn.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onResume() {
            try {
                this.zzaPn.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onSaveInstanceState(Bundle outState) {
            try {
                this.zzaPn.onSaveInstanceState(outState);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
    }

    static class zzb extends com.google.android.gms.dynamic.zza<zza> {
        private final Context mContext;
        protected zzf<zza> zzaPj;
        private final List<OnMapReadyCallback> zzaPk;
        private final ViewGroup zzaPq;
        private final GoogleMapOptions zzaPr;

        zzb(ViewGroup viewGroup, Context context, GoogleMapOptions googleMapOptions) {
            this.zzaPk = new ArrayList();
            this.zzaPq = viewGroup;
            this.mContext = context;
            this.zzaPr = googleMapOptions;
        }

        protected final void zza(zzf<zza> com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_MapView_zza) {
            this.zzaPj = com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_MapView_zza;
            if (this.zzaPj != null && this.zzatr == null) {
                try {
                    MapsInitializer.initialize(this.mContext);
                    IMapViewDelegate zza = zzy.zzaP(this.mContext).zza(zze.zzB(this.mContext), this.zzaPr);
                    if (zza != null) {
                        this.zzaPj.zza(new zza(this.zzaPq, zza));
                        for (OnMapReadyCallback c04661 : this.zzaPk) {
                            zza com_google_android_gms_maps_MapView_zza = (zza) this.zzatr;
                            com_google_android_gms_maps_MapView_zza.zzaPn.getMapAsync(new C04661(com_google_android_gms_maps_MapView_zza, c04661));
                        }
                        this.zzaPk.clear();
                    }
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                } catch (GooglePlayServicesNotAvailableException e2) {
                } catch (RemoteException e3) {
                    throw new RuntimeRemoteException(e3);
                }
            }
        }
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.zzaPl = new zzb(this, context, GoogleMapOptions.createFromAttributes(context, attrs));
        setClickable(true);
    }

    public MapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.zzaPl = new zzb(this, context, GoogleMapOptions.createFromAttributes(context, attrs));
        setClickable(true);
    }
}
