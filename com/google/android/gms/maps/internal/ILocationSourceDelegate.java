package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public interface ILocationSourceDelegate extends IInterface {

    public static abstract class zza extends Binder implements ILocationSourceDelegate {

        private static class zza implements ILocationSourceDelegate {
            private IBinder zzoo;

            zza(IBinder iBinder) {
                this.zzoo = iBinder;
            }

            public final void activate(zzh listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ILocationSourceDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoo.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final IBinder asBinder() {
                return this.zzoo;
            }

            public final void deactivate() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ILocationSourceDelegate");
                    this.zzoo.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case Logger.SEVERE /*1*/:
                    zzh com_google_android_gms_maps_internal_zzh;
                    data.enforceInterface("com.google.android.gms.maps.internal.ILocationSourceDelegate");
                    IBinder readStrongBinder = data.readStrongBinder();
                    if (readStrongBinder == null) {
                        com_google_android_gms_maps_internal_zzh = null;
                    } else {
                        IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IOnLocationChangeListener");
                        com_google_android_gms_maps_internal_zzh = (queryLocalInterface == null || !(queryLocalInterface instanceof zzh)) ? new zza(readStrongBinder) : (zzh) queryLocalInterface;
                    }
                    activate(com_google_android_gms_maps_internal_zzh);
                    reply.writeNoException();
                    return true;
                case Logger.WARNING /*2*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.ILocationSourceDelegate");
                    deactivate();
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.ILocationSourceDelegate");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void activate(zzh com_google_android_gms_maps_internal_zzh) throws RemoteException;

    void deactivate() throws RemoteException;
}
