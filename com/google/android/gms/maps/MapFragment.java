package com.google.android.gms.maps;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
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

public class MapFragment extends Fragment {
    private final zzb zzaPe;

    static class zza implements MapLifecycleDelegate {
        final IMapFragmentDelegate zzaPg;
        private final Fragment zzatC;

        /* renamed from: com.google.android.gms.maps.MapFragment.zza.1 */
        class C04651 extends com.google.android.gms.maps.internal.zzl.zza {
            final /* synthetic */ OnMapReadyCallback zzaPh;
            final /* synthetic */ zza zzaPi;

            C04651(zza com_google_android_gms_maps_MapFragment_zza, OnMapReadyCallback onMapReadyCallback) {
                this.zzaPi = com_google_android_gms_maps_MapFragment_zza;
                this.zzaPh = onMapReadyCallback;
            }

            public final void zza(IGoogleMapDelegate iGoogleMapDelegate) throws RemoteException {
                GoogleMap googleMap = new GoogleMap(iGoogleMapDelegate);
            }
        }

        public zza(Fragment fragment, IMapFragmentDelegate iMapFragmentDelegate) {
            this.zzaPg = (IMapFragmentDelegate) zzx.zzy(iMapFragmentDelegate);
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
        private final List<OnMapReadyCallback> zzaPk;
        private final Fragment zzatC;

        zzb(Fragment fragment) {
            this.zzaPk = new ArrayList();
            this.zzatC = fragment;
        }

        static /* synthetic */ void zza(zzb com_google_android_gms_maps_MapFragment_zzb, Activity activity) {
            com_google_android_gms_maps_MapFragment_zzb.mActivity = activity;
            com_google_android_gms_maps_MapFragment_zzb.zzzh();
        }

        private void zzzh() {
            if (this.mActivity != null && this.zzaPj != null && this.zzatr == null) {
                try {
                    MapsInitializer.initialize(this.mActivity);
                    IMapFragmentDelegate zzt = zzy.zzaP(this.mActivity).zzt(zze.zzB(this.mActivity));
                    if (zzt != null) {
                        this.zzaPj.zza(new zza(this.zzatC, zzt));
                        for (OnMapReadyCallback c04651 : this.zzaPk) {
                            zza com_google_android_gms_maps_MapFragment_zza = (zza) this.zzatr;
                            com_google_android_gms_maps_MapFragment_zza.zzaPg.getMapAsync(new C04651(com_google_android_gms_maps_MapFragment_zza, c04651));
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

        protected final void zza(zzf<zza> com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_MapFragment_zza) {
            this.zzaPj = com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_MapFragment_zza;
            zzzh();
        }
    }

    public MapFragment() {
        this.zzaPe = new zzb(this);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.setClassLoader(MapFragment.class.getClassLoader());
        }
        super.onActivityCreated(savedInstanceState);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        zzb.zza(this.zzaPe, activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.zzaPe.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View onCreateView = this.zzaPe.onCreateView(inflater, container, savedInstanceState);
        onCreateView.setClickable(true);
        return onCreateView;
    }

    public void onDestroy() {
        this.zzaPe.onDestroy();
        super.onDestroy();
    }

    public void onDestroyView() {
        this.zzaPe.onDestroyView();
        super.onDestroyView();
    }

    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        zzb.zza(this.zzaPe, activity);
        Parcelable createFromAttributes = GoogleMapOptions.createFromAttributes(activity, attrs);
        Bundle bundle = new Bundle();
        bundle.putParcelable("MapOptions", createFromAttributes);
        this.zzaPe.onInflate(activity, bundle, savedInstanceState);
    }

    public void onLowMemory() {
        this.zzaPe.onLowMemory();
        super.onLowMemory();
    }

    public void onPause() {
        this.zzaPe.onPause();
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        this.zzaPe.onResume();
    }

    public void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.setClassLoader(MapFragment.class.getClassLoader());
        }
        super.onSaveInstanceState(outState);
        this.zzaPe.onSaveInstanceState(outState);
    }

    public void setArguments(Bundle args) {
        super.setArguments(args);
    }
}
