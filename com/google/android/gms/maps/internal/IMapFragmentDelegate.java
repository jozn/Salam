package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.maps.GoogleMapOptions;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public interface IMapFragmentDelegate extends IInterface {

    public static abstract class zza extends Binder implements IMapFragmentDelegate {

        private static class zza implements IMapFragmentDelegate {
            private IBinder zzoo;

            zza(IBinder iBinder) {
                this.zzoo = iBinder;
            }

            public final IBinder asBinder() {
                return this.zzoo;
            }

            public final IGoogleMapDelegate getMap() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    this.zzoo.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    IGoogleMapDelegate zzcv = com.google.android.gms.maps.internal.IGoogleMapDelegate.zza.zzcv(obtain2.readStrongBinder());
                    return zzcv;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void getMapAsync(zzl callback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.zzoo.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final boolean isReady() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    this.zzoo.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void onCreate(Bundle savedInstanceState) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    if (savedInstanceState != null) {
                        obtain.writeInt(1);
                        savedInstanceState.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final zzd onCreateView(zzd inflaterWrapper, zzd containerWrapper, Bundle savedInstanceState) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    obtain.writeStrongBinder(inflaterWrapper != null ? inflaterWrapper.asBinder() : null);
                    if (containerWrapper != null) {
                        iBinder = containerWrapper.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    if (savedInstanceState != null) {
                        obtain.writeInt(1);
                        savedInstanceState.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    zzd zzbs = com.google.android.gms.dynamic.zzd.zza.zzbs(obtain2.readStrongBinder());
                    return zzbs;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void onDestroy() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    this.zzoo.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void onDestroyView() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    this.zzoo.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void onEnterAmbient(Bundle ambientDetails) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    if (ambientDetails != null) {
                        obtain.writeInt(1);
                        ambientDetails.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void onExitAmbient() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    this.zzoo.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void onInflate(zzd activityWrapper, GoogleMapOptions options, Bundle savedInstanceState) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    obtain.writeStrongBinder(activityWrapper != null ? activityWrapper.asBinder() : null);
                    if (options != null) {
                        obtain.writeInt(1);
                        options.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (savedInstanceState != null) {
                        obtain.writeInt(1);
                        savedInstanceState.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void onLowMemory() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    this.zzoo.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void onPause() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    this.zzoo.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void onResume() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    this.zzoo.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void onSaveInstanceState(Bundle outState) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    if (outState != null) {
                        obtain.writeInt(1);
                        outState.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        outState.readFromParcel(obtain2);
                    }
                    obtain2.recycle();
                    obtain.recycle();
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static IMapFragmentDelegate zzcy(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IMapFragmentDelegate)) ? new zza(iBinder) : (IMapFragmentDelegate) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IBinder iBinder = null;
            switch (code) {
                case Logger.SEVERE /*1*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    IGoogleMapDelegate map = getMap();
                    reply.writeNoException();
                    reply.writeStrongBinder(map != null ? map.asBinder() : null);
                    return true;
                case Logger.WARNING /*2*/:
                    GoogleMapOptions zzfk;
                    data.enforceInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    zzd zzbs = com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        com.google.android.gms.maps.zza com_google_android_gms_maps_zza = GoogleMapOptions.CREATOR;
                        zzfk = com.google.android.gms.maps.zza.zzfk(data);
                    } else {
                        zzfk = null;
                    }
                    onInflate(zzbs, zzfk, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case Logger.INFO /*3*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    onCreate(data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case Logger.CONFIG /*4*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    zzd onCreateView = onCreateView(com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()), com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    if (onCreateView != null) {
                        iBinder = onCreateView.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Logger.FINE /*5*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    onResume();
                    reply.writeNoException();
                    return true;
                case Logger.FINER /*6*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    onPause();
                    reply.writeNoException();
                    return true;
                case Logger.FINEST /*7*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    onDestroyView();
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    onDestroy();
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    onLowMemory();
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    Bundle bundle = data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null;
                    onSaveInstanceState(bundle);
                    reply.writeNoException();
                    if (bundle != null) {
                        reply.writeInt(1);
                        bundle.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case C0473R.styleable.SwitchButton_offColor /*11*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    boolean isReady = isReady();
                    reply.writeNoException();
                    reply.writeInt(isReady ? 1 : 0);
                    return true;
                case C0473R.styleable.SwitchButton_thumbColor /*12*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    getMapAsync(com.google.android.gms.maps.internal.zzl.zza.zzcH(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_thumbPressedColor /*13*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    onEnterAmbient(data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_animationVelocity /*14*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    onExitAmbient();
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.IMapFragmentDelegate");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    IGoogleMapDelegate getMap() throws RemoteException;

    void getMapAsync(zzl com_google_android_gms_maps_internal_zzl) throws RemoteException;

    boolean isReady() throws RemoteException;

    void onCreate(Bundle bundle) throws RemoteException;

    zzd onCreateView(zzd com_google_android_gms_dynamic_zzd, zzd com_google_android_gms_dynamic_zzd2, Bundle bundle) throws RemoteException;

    void onDestroy() throws RemoteException;

    void onDestroyView() throws RemoteException;

    void onEnterAmbient(Bundle bundle) throws RemoteException;

    void onExitAmbient() throws RemoteException;

    void onInflate(zzd com_google_android_gms_dynamic_zzd, GoogleMapOptions googleMapOptions, Bundle bundle) throws RemoteException;

    void onLowMemory() throws RemoteException;

    void onPause() throws RemoteException;

    void onResume() throws RemoteException;

    void onSaveInstanceState(Bundle bundle) throws RemoteException;
}
