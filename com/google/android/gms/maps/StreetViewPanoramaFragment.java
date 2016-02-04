package com.google.android.gms.maps;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.RemoteException;
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

public class StreetViewPanoramaFragment extends Fragment {
    private final zzb zzaPz;

    static class zza implements StreetViewLifecycleDelegate {
        final IStreetViewPanoramaFragmentDelegate zzaPB;
        private final Fragment zzatC;

        /* renamed from: com.google.android.gms.maps.StreetViewPanoramaFragment.zza.1 */
        class C04671 extends com.google.android.gms.maps.internal.zzv.zza {
            final /* synthetic */ OnStreetViewPanoramaReadyCallback zzaPC;
            final /* synthetic */ zza zzaPD;

            C04671(zza com_google_android_gms_maps_StreetViewPanoramaFragment_zza, OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
                this.zzaPD = com_google_android_gms_maps_StreetViewPanoramaFragment_zza;
                this.zzaPC = onStreetViewPanoramaReadyCallback;
            }

            public final void zza(IStreetViewPanoramaDelegate iStreetViewPanoramaDelegate) throws RemoteException {
                StreetViewPanorama streetViewPanorama = new StreetViewPanorama(iStreetViewPanoramaDelegate);
            }
        }

        public zza(Fragment fragment, IStreetViewPanoramaFragmentDelegate iStreetViewPanoramaFragmentDelegate) {
            this.zzaPB = (IStreetViewPanoramaFragmentDelegate) zzx.zzy(iStreetViewPanoramaFragmentDelegate);
            this.zzatC = (Fragment) zzx.zzy(fragment);
        }

        public final void onCreate(Bundle savedInstanceState) {
            if (savedInstanceState == null) {
                try {
                    savedInstanceState = new Bundle();
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                }
            }
            Bundle arguments = this.zzatC.getArguments();
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
        private final List<OnStreetViewPanoramaReadyCallback> zzaPE;
        protected zzf<zza> zzaPj;
        private final Fragment zzatC;

        zzb(Fragment fragment) {
            this.zzaPE = new ArrayList();
            this.zzatC = fragment;
        }

        static /* synthetic */ void zza(zzb com_google_android_gms_maps_StreetViewPanoramaFragment_zzb, Activity activity) {
            com_google_android_gms_maps_StreetViewPanoramaFragment_zzb.mActivity = activity;
            com_google_android_gms_maps_StreetViewPanoramaFragment_zzb.zzzh();
        }

        private void zzzh() {
            if (this.mActivity != null && this.zzaPj != null && this.zzatr == null) {
                try {
                    MapsInitializer.initialize(this.mActivity);
                    this.zzaPj.zza(new zza(this.zzatC, zzy.zzaP(this.mActivity).zzu(zze.zzB(this.mActivity))));
                    for (OnStreetViewPanoramaReadyCallback c04671 : this.zzaPE) {
                        zza com_google_android_gms_maps_StreetViewPanoramaFragment_zza = (zza) this.zzatr;
                        com_google_android_gms_maps_StreetViewPanoramaFragment_zza.zzaPB.getStreetViewPanoramaAsync(new C04671(com_google_android_gms_maps_StreetViewPanoramaFragment_zza, c04671));
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

        protected final void zza(zzf<zza> com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_StreetViewPanoramaFragment_zza) {
            this.zzaPj = com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_StreetViewPanoramaFragment_zza;
            zzzh();
        }
    }

    public StreetViewPanoramaFragment() {
        this.zzaPz = new zzb(this);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.setClassLoader(StreetViewPanoramaFragment.class.getClassLoader());
        }
        super.onActivityCreated(savedInstanceState);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        zzb.zza(this.zzaPz, activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.zzaPz.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.zzaPz.onCreateView(inflater, container, savedInstanceState);
    }

    public void onDestroy() {
        this.zzaPz.onDestroy();
        super.onDestroy();
    }

    public void onDestroyView() {
        this.zzaPz.onDestroyView();
        super.onDestroyView();
    }

    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        zzb.zza(this.zzaPz, activity);
        this.zzaPz.onInflate(activity, new Bundle(), savedInstanceState);
    }

    public void onLowMemory() {
        this.zzaPz.onLowMemory();
        super.onLowMemory();
    }

    public void onPause() {
        this.zzaPz.onPause();
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        this.zzaPz.onResume();
    }

    public void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.setClassLoader(StreetViewPanoramaFragment.class.getClassLoader());
        }
        super.onSaveInstanceState(outState);
        this.zzaPz.onSaveInstanceState(outState);
    }

    public void setArguments(Bundle args) {
        super.setArguments(args);
    }
}
