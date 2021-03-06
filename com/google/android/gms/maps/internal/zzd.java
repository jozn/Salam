package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.maps.model.internal.zzf;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public interface zzd extends IInterface {

    public static abstract class zza extends Binder implements zzd {

        private static class zza implements zzd {
            private IBinder zzoo;

            zza(IBinder iBinder) {
                this.zzoo = iBinder;
            }

            public final IBinder asBinder() {
                return this.zzoo;
            }

            public final com.google.android.gms.dynamic.zzd zzf(zzf com_google_android_gms_maps_model_internal_zzf) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    obtain.writeStrongBinder(com_google_android_gms_maps_model_internal_zzf != null ? com_google_android_gms_maps_model_internal_zzf.asBinder() : null);
                    this.zzoo.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    com.google.android.gms.dynamic.zzd zzbs = com.google.android.gms.dynamic.zzd.zza.zzbs(obtain2.readStrongBinder());
                    return zzbs;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final com.google.android.gms.dynamic.zzd zzg(zzf com_google_android_gms_maps_model_internal_zzf) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    obtain.writeStrongBinder(com_google_android_gms_maps_model_internal_zzf != null ? com_google_android_gms_maps_model_internal_zzf.asBinder() : null);
                    this.zzoo.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    com.google.android.gms.dynamic.zzd zzbs = com.google.android.gms.dynamic.zzd.zza.zzbs(obtain2.readStrongBinder());
                    return zzbs;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IBinder iBinder = null;
            com.google.android.gms.dynamic.zzd zzf;
            switch (code) {
                case Logger.SEVERE /*1*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    zzf = zzf(com.google.android.gms.maps.model.internal.zzf.zza.zzdd(data.readStrongBinder()));
                    reply.writeNoException();
                    if (zzf != null) {
                        iBinder = zzf.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case Logger.WARNING /*2*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    zzf = zzg(com.google.android.gms.maps.model.internal.zzf.zza.zzdd(data.readStrongBinder()));
                    reply.writeNoException();
                    if (zzf != null) {
                        iBinder = zzf.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    com.google.android.gms.dynamic.zzd zzf(zzf com_google_android_gms_maps_model_internal_zzf) throws RemoteException;

    com.google.android.gms.dynamic.zzd zzg(zzf com_google_android_gms_maps_model_internal_zzf) throws RemoteException;
}
