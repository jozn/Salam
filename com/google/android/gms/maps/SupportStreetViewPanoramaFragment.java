package com.google.android.gms.maps;

import android.app.Activity;
import android.os.Bundle;
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
import com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate;
import com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate;
import com.google.android.gms.maps.internal.StreetViewLifecycleDelegate;
import com.google.android.gms.maps.internal.zzy;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import java.util.ArrayList;
import java.util.List;

public class SupportStreetViewPanoramaFragment extends Fragment {
    private StreetViewPanorama zzaPA;
    private final zzb zzaPT;

    static class zza implements StreetViewLifecycleDelegate {
        final IStreetViewPanoramaFragmentDelegate zzaPB;
        private final Fragment zzajv;

        /* renamed from: com.google.android.gms.maps.SupportStreetViewPanoramaFragment.zza.1 */
        class C04701 extends com.google.android.gms.maps.internal.zzv.zza {
            final /* synthetic */ OnStreetViewPanoramaReadyCallback zzaPC;
            final /* synthetic */ zza zzaPU;

            C04701(zza com_google_android_gms_maps_SupportStreetViewPanoramaFragment_zza, OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
                this.zzaPU = com_google_android_gms_maps_SupportStreetViewPanoramaFragment_zza;
                this.zzaPC = onStreetViewPanoramaReadyCallback;
            }

            public final void zza(IStreetViewPanoramaDelegate iStreetViewPanoramaDelegate) throws RemoteException {
                StreetViewPanorama streetViewPanorama = new StreetViewPanorama(iStreetViewPanoramaDelegate);
            }
        }

        public zza(Fragment fragment, IStreetViewPanoramaFragmentDelegate iStreetViewPanoramaFragmentDelegate) {
            this.zzaPB = (IStreetViewPanoramaFragmentDelegate) zzx.zzy(iStreetViewPanoramaFragmentDelegate);
            this.zzajv = (Fragment) zzx.zzy(fragment);
        }

        public final void getStreetViewPanoramaAsync(OnStreetViewPanoramaReadyCallback callback) {
            try {
                this.zzaPB.getStreetViewPanoramaAsync(new C04701(this, callback));
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
            if (arguments != null && arguments.containsKey("StreetViewPanoramaOptions")) {
                com.google.android.gms.maps.internal.zzx.zza(savedInstanceState, "StreetViewPanoramaOptions", arguments.getParcelable("StreetViewPanoramaOptions"));
            }
            this.zzaPB.onCreate(savedInstanceState);
        }

        public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            try {
                return (View) zze.zzp(this.zzaPB.onCreateView(zze.zzB(inflater), zze.zzB(container), savedInstanceState));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onDestroy() {
            try {
                this.zzaPB.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onDestroyView() {
            try {
                this.zzaPB.onDestroyView();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onInflate(Activity activity, Bundle attrs, Bundle savedInstanceState) {
            try {
                this.zzaPB.onInflate(zze.zzB(activity), null, savedInstanceState);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onLowMemory() {
            try {
                this.zzaPB.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onPause() {
            try {
                this.zzaPB.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onResume() {
            try {
                this.zzaPB.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onSaveInstanceState(Bundle outState) {
            try {
                this.zzaPB.onSaveInstanceState(outState);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
    }

    static class zzb extends com.google.android.gms.dynamic.zza<zza> {
        private Activity mActivity;
        final List<OnStreetViewPanoramaReadyCallback> zzaPE;
        protected zzf<zza> zzaPj;
        private final Fragment zzajv;

        zzb(Fragment fragment) {
            this.zzaPE = new ArrayList();
            this.zzajv = fragment;
        }

        static /* synthetic */ void zza(zzb com_google_android_gms_maps_SupportStreetViewPanoramaFragment_zzb, Activity activity) {
            com_google_android_gms_maps_SupportStreetViewPanoramaFragment_zzb.mActivity = activity;
            com_google_android_gms_maps_SupportStreetViewPanoramaFragment_zzb.zzzh();
        }

        protected final void zza(zzf<zza> com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_SupportStreetViewPanoramaFragment_zza) {
            this.zzaPj = com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_SupportStreetViewPanoramaFragment_zza;
            zzzh();
        }

        public final void zzzh() {
            if (this.mActivity != null && this.zzaPj != null && this.zzatr == null) {
                try {
                    MapsInitializer.initialize(this.mActivity);
                    this.zzaPj.zza(new zza(this.zzajv, zzy.zzaP(this.mActivity).zzu(zze.zzB(this.mActivity))));
                    for (OnStreetViewPanoramaReadyCallback streetViewPanoramaAsync : this.zzaPE) {
                        ((zza) this.zzatr).getStreetViewPanoramaAsync(streetViewPanoramaAsync);
                    }
                    this.zzaPE.clear();
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                } catch (GooglePlayServicesNotAvailableException e2) {
                }
            }
        }
    }

    public SupportStreetViewPanoramaFragment() {
        this.zzaPT = new zzb(this);
    }

    public static SupportStreetViewPanoramaFragment newInstance() {
        return new SupportStreetViewPanoramaFragment();
    }

    public static SupportStreetViewPanoramaFragment newInstance(StreetViewPanoramaOptions options) {
        SupportStreetViewPanoramaFragment supportStreetViewPanoramaFragment = new SupportStreetViewPanoramaFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("StreetViewPanoramaOptions", options);
        supportStreetViewPanoramaFragment.setArguments(bundle);
        return supportStreetViewPanoramaFragment;
    }

    @Deprecated
    public final StreetViewPanorama getStreetViewPanorama() {
        IStreetViewPanoramaFragmentDelegate zzzk = zzzk();
        if (zzzk == null) {
            return null;
        }
        try {
            IStreetViewPanoramaDelegate streetViewPanorama = zzzk.getStreetViewPanorama();
            if (streetViewPanorama == null) {
                return null;
            }
            if (this.zzaPA == null || this.zzaPA.zzaPt.asBinder() != streetViewPanorama.asBinder()) {
                this.zzaPA = new StreetViewPanorama(streetViewPanorama);
            }
            return this.zzaPA;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void getStreetViewPanoramaAsync(OnStreetViewPanoramaReadyCallback callback) {
        zzx.zzcx("getStreetViewPanoramaAsync() must be called on the main thread");
        com.google.android.gms.dynamic.zza com_google_android_gms_dynamic_zza = this.zzaPT;
        if (com_google_android_gms_dynamic_zza.zzatr != null) {
            ((zza) com_google_android_gms_dynamic_zza.zzatr).getStreetViewPanoramaAsync(callback);
        } else {
            com_google_android_gms_dynamic_zza.zzaPE.add(callback);
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.setClassLoader(SupportStreetViewPanoramaFragment.class.getClassLoader());
        }
        super.onActivityCreated(savedInstanceState);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        zzb.zza(this.zzaPT, activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.zzaPT.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.zzaPT.onCreateView(inflater, container, savedInstanceState);
    }

    public void onDestroy() {
        this.zzaPT.onDestroy();
        super.onDestroy();
    }

    public void onDestroyView() {
        this.zzaPT.onDestroyView();
        super.onDestroyView();
    }

    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        zzb.zza(this.zzaPT, activity);
        this.zzaPT.onInflate(activity, new Bundle(), savedInstanceState);
    }

    public void onLowMemory() {
        this.zzaPT.onLowMemory();
        super.onLowMemory();
    }

    public void onPause() {
        this.zzaPT.onPause();
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        this.zzaPT.onResume();
    }

    public void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.setClassLoader(SupportStreetViewPanoramaFragment.class.getClassLoader());
        }
        super.onSaveInstanceState(outState);
        this.zzaPT.onSaveInstanceState(outState);
    }

    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    protected IStreetViewPanoramaFragmentDelegate zzzk() {
        this.zzaPT.zzzh();
        return this.zzaPT.zzatr == null ? null : ((zza) this.zzaPT.zzatr).zzaPB;
    }
}
