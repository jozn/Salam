package com.google.android.gms.maps;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.dynamic.zzf;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.IMapFragmentDelegate;
import com.google.android.gms.maps.internal.MapLifecycleDelegate;
import com.google.android.gms.maps.internal.zzy;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import java.util.ArrayList;
import java.util.List;

public class SupportMapFragment extends Fragment {
    private final zzb zzaPR;
    private GoogleMap zzaPf;

    static class zza implements MapLifecycleDelegate {
        final IMapFragmentDelegate zzaPg;
        private final Fragment zzajv;

        /* renamed from: com.google.android.gms.maps.SupportMapFragment.zza.1 */
        class C04691 extends com.google.android.gms.maps.internal.zzl.zza {
            final /* synthetic */ zza zzaPS;
            final /* synthetic */ OnMapReadyCallback zzaPh;

            C04691(zza com_google_android_gms_maps_SupportMapFragment_zza, OnMapReadyCallback onMapReadyCallback) {
                this.zzaPS = com_google_android_gms_maps_SupportMapFragment_zza;
                this.zzaPh = onMapReadyCallback;
            }

            public final void zza(IGoogleMapDelegate iGoogleMapDelegate) throws RemoteException {
                GoogleMap googleMap = new GoogleMap(iGoogleMapDelegate);
            }
        }

        public zza(Fragment fragment, IMapFragmentDelegate iMapFragmentDelegate) {
            this.zzaPg = (IMapFragmentDelegate) zzx.zzy(iMapFragmentDelegate);
            this.zzajv = (Fragment) zzx.zzy(fragment);
        }

        public final void getMapAsync(OnMapReadyCallback callback) {
            try {
                this.zzaPg.getMapAsync(new C04691(this, callback));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onCreate(Bundle savedInstanceState) {
            if (savedInstanceState == null) {
                try {
                    savedInstanceState = new Bundle();
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                }
            }
            Bundle arguments = this.zzajv.getArguments();
            if (arguments != null && arguments.containsKey("MapOptions")) {
                com.google.android.gms.maps.internal.zzx.zza(savedInstanceState, "MapOptions", arguments.getParcelable("MapOptions"));
            }
            this.zzaPg.onCreate(savedInstanceState);
        }

        public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            try {
                return (View) zze.zzp(this.zzaPg.onCreateView(zze.zzB(inflater), zze.zzB(container), savedInstanceState));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onDestroy() {
            try {
                this.zzaPg.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onDestroyView() {
            try {
                this.zzaPg.onDestroyView();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onInflate(Activity activity, Bundle attrs, Bundle savedInstanceState) {
            try {
                this.zzaPg.onInflate(zze.zzB(activity), (GoogleMapOptions) attrs.getParcelable("MapOptions"), savedInstanceState);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onLowMemory() {
            try {
                this.zzaPg.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onPause() {
            try {
                this.zzaPg.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onResume() {
            try {
                this.zzaPg.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onSaveInstanceState(Bundle outState) {
            try {
                this.zzaPg.onSaveInstanceState(outState);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
    }

    static class zzb extends com.google.android.gms.dynamic.zza<zza> {
        private Activity mActivity;
        protected zzf<zza> zzaPj;
        final List<OnMapReadyCallback> zzaPk;
        private final Fragment zzajv;

        zzb(Fragment fragment) {
            this.zzaPk = new ArrayList();
            this.zzajv = fragment;
        }

        static /* synthetic */ void zza(zzb com_google_android_gms_maps_SupportMapFragment_zzb, Activity activity) {
            com_google_android_gms_maps_SupportMapFragment_zzb.mActivity = activity;
            com_google_android_gms_maps_SupportMapFragment_zzb.zzzh();
        }

        protected final void zza(zzf<zza> com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_SupportMapFragment_zza) {
            this.zzaPj = com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_SupportMapFragment_zza;
            zzzh();
        }

        public final void zzzh() {
            if (this.mActivity != null && this.zzaPj != null && this.zzatr == null) {
                try {
                    MapsInitializer.initialize(this.mActivity);
                    IMapFragmentDelegate zzt = zzy.zzaP(this.mActivity).zzt(zze.zzB(this.mActivity));
                    if (zzt != null) {
                        this.zzaPj.zza(new zza(this.zzajv, zzt));
                        for (OnMapReadyCallback mapAsync : this.zzaPk) {
                            ((zza) this.zzatr).getMapAsync(mapAsync);
                        }
                        this.zzaPk.clear();
                    }
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                } catch (GooglePlayServicesNotAvailableException e2) {
                }
            }
        }
    }

    public SupportMapFragment() {
        this.zzaPR = new zzb(this);
    }

    public static SupportMapFragment newInstance() {
        return new SupportMapFragment();
    }

    public static SupportMapFragment newInstance(GoogleMapOptions options) {
        SupportMapFragment supportMapFragment = new SupportMapFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("MapOptions", options);
        supportMapFragment.setArguments(bundle);
        return supportMapFragment;
    }

    @Deprecated
    public final GoogleMap getMap() {
        IMapFragmentDelegate zzzg = zzzg();
        if (zzzg == null) {
            return null;
        }
        try {
            IGoogleMapDelegate map = zzzg.getMap();
            if (map == null) {
                return null;
            }
            if (this.zzaPf == null || this.zzaPf.zzaOy.asBinder() != map.asBinder()) {
                this.zzaPf = new GoogleMap(map);
            }
            return this.zzaPf;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void getMapAsync(OnMapReadyCallback callback) {
        zzx.zzcx("getMapAsync must be called on the main thread.");
        com.google.android.gms.dynamic.zza com_google_android_gms_dynamic_zza = this.zzaPR;
        if (com_google_android_gms_dynamic_zza.zzatr != null) {
            ((zza) com_google_android_gms_dynamic_zza.zzatr).getMapAsync(callback);
        } else {
            com_google_android_gms_dynamic_zza.zzaPk.add(callback);
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.setClassLoader(SupportMapFragment.class.getClassLoader());
        }
        super.onActivityCreated(savedInstanceState);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        zzb.zza(this.zzaPR, activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.zzaPR.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View onCreateView = this.zzaPR.onCreateView(inflater, container, savedInstanceState);
        onCreateView.setClickable(true);
        return onCreateView;
    }

    public void onDestroy() {
        this.zzaPR.onDestroy();
        super.onDestroy();
    }

    public void onDestroyView() {
        this.zzaPR.onDestroyView();
        super.onDestroyView();
    }

    public final void onEnterAmbient(Bundle ambientDetails) {
        zzx.zzcx("onEnterAmbient must be called on the main thread.");
        com.google.android.gms.dynamic.zza com_google_android_gms_dynamic_zza = this.zzaPR;
        if (com_google_android_gms_dynamic_zza.zzatr != null) {
            try {
                ((zza) com_google_android_gms_dynamic_zza.zzatr).zzaPg.onEnterAmbient(ambientDetails);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
    }

    public final void onExitAmbient() {
        zzx.zzcx("onExitAmbient must be called on the main thread.");
        com.google.android.gms.dynamic.zza com_google_android_gms_dynamic_zza = this.zzaPR;
        if (com_google_android_gms_dynamic_zza.zzatr != null) {
            try {
                ((zza) com_google_android_gms_dynamic_zza.zzatr).zzaPg.onExitAmbient();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
    }

    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        zzb.zza(this.zzaPR, activity);
        Parcelable createFromAttributes = GoogleMapOptions.createFromAttributes(activity, attrs);
        Bundle bundle = new Bundle();
        bundle.putParcelable("MapOptions", createFromAttributes);
        this.zzaPR.onInflate(activity, bundle, savedInstanceState);
    }

    public void onLowMemory() {
        this.zzaPR.onLowMemory();
        super.onLowMemory();
    }

    public void onPause() {
        this.zzaPR.onPause();
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        this.zzaPR.onResume();
    }

    public void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.setClassLoader(SupportMapFragment.class.getClassLoader());
        }
        super.onSaveInstanceState(outState);
        this.zzaPR.onSaveInstanceState(outState);
    }

    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    protected IMapFragmentDelegate zzzg() {
        this.zzaPR.zzzh();
        return this.zzaPR.zzatr == null ? null : ((zza) this.zzaPR.zzatr).zzaPg;
    }
}
