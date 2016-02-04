package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.zzb;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public interface zzc extends IInterface {

    public static abstract class zza extends Binder implements zzc {

        private static class zza implements zzc {
            private IBinder zzoo;

            zza(IBinder iBinder) {
                this.zzoo = iBinder;
            }

            public final IBinder asBinder() {
                return this.zzoo;
            }

            public final IMapViewDelegate zza(zzd com_google_android_gms_dynamic_zzd, GoogleMapOptions googleMapOptions) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    if (googleMapOptions != null) {
                        obtain.writeInt(1);
                        googleMapOptions.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    IMapViewDelegate zzcz = com.google.android.gms.maps.internal.IMapViewDelegate.zza.zzcz(obtain2.readStrongBinder());
                    return zzcz;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final IStreetViewPanoramaViewDelegate zza(zzd com_google_android_gms_dynamic_zzd, StreetViewPanoramaOptions streetViewPanoramaOptions) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    if (streetViewPanoramaOptions != null) {
                        obtain.writeInt(1);
                        streetViewPanoramaOptions.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    IStreetViewPanoramaViewDelegate zzcW = com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate.zza.zzcW(obtain2.readStrongBinder());
                    return zzcW;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void zzd(zzd com_google_android_gms_dynamic_zzd, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    obtain.writeInt(i);
                    this.zzoo.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void zzs(zzd com_google_android_gms_dynamic_zzd) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    this.zzoo.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final IMapFragmentDelegate zzt(zzd com_google_android_gms_dynamic_zzd) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    this.zzoo.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    IMapFragmentDelegate zzcy = com.google.android.gms.maps.internal.IMapFragmentDelegate.zza.zzcy(obtain2.readStrongBinder());
                    return zzcy;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final IStreetViewPanoramaFragmentDelegate zzu(zzd com_google_android_gms_dynamic_zzd) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    this.zzoo.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    IStreetViewPanoramaFragmentDelegate zzcV = com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate.zza.zzcV(obtain2.readStrongBinder());
                    return zzcV;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final ICameraUpdateFactoryDelegate zzzp() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    this.zzoo.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    ICameraUpdateFactoryDelegate zzcs = com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate.zza.zzcs(obtain2.readStrongBinder());
                    return zzcs;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final com.google.android.gms.maps.model.internal.zza zzzq() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    this.zzoo.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    com.google.android.gms.maps.model.internal.zza zzcY = com.google.android.gms.maps.model.internal.zza.zza.zzcY(obtain2.readStrongBinder());
                    return zzcY;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzc zzcu(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.ICreator");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzc)) ? new zza(iBinder) : (zzc) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IBinder iBinder = null;
            zzd zzbs;
            switch (code) {
                case Logger.SEVERE /*1*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    zzs(com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Logger.WARNING /*2*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IMapFragmentDelegate zzt = zzt(com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()));
                    reply.writeNoException();
                    if (zzt != null) {
                        iBinder = zzt.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Logger.INFO /*3*/:
                    GoogleMapOptions zzfk;
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    zzbs = com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        com.google.android.gms.maps.zza com_google_android_gms_maps_zza = GoogleMapOptions.CREATOR;
                        zzfk = com.google.android.gms.maps.zza.zzfk(data);
                    } else {
                        zzfk = null;
                    }
                    IMapViewDelegate zza = zza(zzbs, zzfk);
                    reply.writeNoException();
                    if (zza != null) {
                        iBinder = zza.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Logger.CONFIG /*4*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    ICameraUpdateFactoryDelegate zzzp = zzzp();
                    reply.writeNoException();
                    if (zzzp != null) {
                        iBinder = zzzp.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Logger.FINE /*5*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    com.google.android.gms.maps.model.internal.zza zzzq = zzzq();
                    reply.writeNoException();
                    if (zzzq != null) {
                        iBinder = zzzq.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Logger.FINER /*6*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    zzd(com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case Logger.FINEST /*7*/:
                    StreetViewPanoramaOptions zzfl;
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    zzbs = com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        zzb com_google_android_gms_maps_zzb = StreetViewPanoramaOptions.CREATOR;
                        zzfl = zzb.zzfl(data);
                    } else {
                        zzfl = null;
                    }
                    IStreetViewPanoramaViewDelegate zza2 = zza(zzbs, zzfl);
                    reply.writeNoException();
                    if (zza2 != null) {
                        iBinder = zza2.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IStreetViewPanoramaFragmentDelegate zzu = zzu(com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()));
                    reply.writeNoException();
                    if (zzu != null) {
                        iBinder = zzu.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.ICreator");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    IMapViewDelegate zza(zzd com_google_android_gms_dynamic_zzd, GoogleMapOptions googleMapOptions) throws RemoteException;

    IStreetViewPanoramaViewDelegate zza(zzd com_google_android_gms_dynamic_zzd, StreetViewPanoramaOptions streetViewPanoramaOptions) throws RemoteException;

    void zzd(zzd com_google_android_gms_dynamic_zzd, int i) throws RemoteException;

    void zzs(zzd com_google_android_gms_dynamic_zzd) throws RemoteException;

    IMapFragmentDelegate zzt(zzd com_google_android_gms_dynamic_zzd) throws RemoteException;

    IStreetViewPanoramaFragmentDelegate zzu(zzd com_google_android_gms_dynamic_zzd) throws RemoteException;

    ICameraUpdateFactoryDelegate zzzp() throws RemoteException;

    com.google.android.gms.maps.model.internal.zza zzzq() throws RemoteException;
}
