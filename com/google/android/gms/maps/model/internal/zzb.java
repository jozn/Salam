package com.google.android.gms.maps.model.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.zze;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public interface zzb extends IInterface {

    public static abstract class zza extends Binder implements zzb {

        private static class zza implements zzb {
            private IBinder zzoo;

            zza(IBinder iBinder) {
                this.zzoo = iBinder;
            }

            public final IBinder asBinder() {
                return this.zzoo;
            }

            public final LatLng getCenter() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    LatLng zzfq;
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzoo.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        zze com_google_android_gms_maps_model_zze = LatLng.CREATOR;
                        zzfq = zze.zzfq(obtain2);
                    } else {
                        zzfq = null;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return zzfq;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final int getFillColor() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzoo.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final String getId() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzoo.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final double getRadius() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzoo.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    double readDouble = obtain2.readDouble();
                    return readDouble;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final int getStrokeColor() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzoo.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final float getStrokeWidth() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzoo.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final float getZIndex() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzoo.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final int hashCodeRemote() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzoo.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final boolean isVisible() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzoo.transact(16, obtain, obtain2, 0);
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

            public final void remove() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzoo.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setCenter(LatLng center) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    if (center != null) {
                        obtain.writeInt(1);
                        center.writeToParcel(obtain, 0);
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

            public final void setFillColor(int color) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    obtain.writeInt(color);
                    this.zzoo.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setRadius(double radius) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    obtain.writeDouble(radius);
                    this.zzoo.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setStrokeColor(int color) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    obtain.writeInt(color);
                    this.zzoo.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setStrokeWidth(float width) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    obtain.writeFloat(width);
                    this.zzoo.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setVisible(boolean visible) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    if (visible) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoo.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setZIndex(float zIndex) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    obtain.writeFloat(zIndex);
                    this.zzoo.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final boolean zza(zzb com_google_android_gms_maps_model_internal_zzb) throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    obtain.writeStrongBinder(com_google_android_gms_maps_model_internal_zzb != null ? com_google_android_gms_maps_model_internal_zzb.asBinder() : null);
                    this.zzoo.transact(17, obtain, obtain2, 0);
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
        }

        public static zzb zzcZ(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzb)) ? new zza(iBinder) : (zzb) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            float strokeWidth;
            boolean isVisible;
            switch (code) {
                case Logger.SEVERE /*1*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    remove();
                    reply.writeNoException();
                    return true;
                case Logger.WARNING /*2*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    String id = getId();
                    reply.writeNoException();
                    reply.writeString(id);
                    return true;
                case Logger.INFO /*3*/:
                    LatLng zzfq;
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    if (data.readInt() != 0) {
                        zze com_google_android_gms_maps_model_zze = LatLng.CREATOR;
                        zzfq = zze.zzfq(data);
                    } else {
                        zzfq = null;
                    }
                    setCenter(zzfq);
                    reply.writeNoException();
                    return true;
                case Logger.CONFIG /*4*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    LatLng center = getCenter();
                    reply.writeNoException();
                    if (center != null) {
                        reply.writeInt(1);
                        center.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case Logger.FINE /*5*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    setRadius(data.readDouble());
                    reply.writeNoException();
                    return true;
                case Logger.FINER /*6*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    double radius = getRadius();
                    reply.writeNoException();
                    reply.writeDouble(radius);
                    return true;
                case Logger.FINEST /*7*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    setStrokeWidth(data.readFloat());
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    strokeWidth = getStrokeWidth();
                    reply.writeNoException();
                    reply.writeFloat(strokeWidth);
                    return true;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    setStrokeColor(data.readInt());
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    i = getStrokeColor();
                    reply.writeNoException();
                    reply.writeInt(i);
                    return true;
                case C0473R.styleable.SwitchButton_offColor /*11*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    setFillColor(data.readInt());
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_thumbColor /*12*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    i = getFillColor();
                    reply.writeNoException();
                    reply.writeInt(i);
                    return true;
                case C0473R.styleable.SwitchButton_thumbPressedColor /*13*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    setZIndex(data.readFloat());
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_animationVelocity /*14*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    strokeWidth = getZIndex();
                    reply.writeNoException();
                    reply.writeFloat(strokeWidth);
                    return true;
                case C0473R.styleable.SwitchButton_radius /*15*/:
                    boolean z;
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setVisible(z);
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_measureFactor /*16*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    isVisible = isVisible();
                    reply.writeNoException();
                    if (isVisible) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case C0473R.styleable.SwitchButton_insetLeft /*17*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    isVisible = zza(zzcZ(data.readStrongBinder()));
                    reply.writeNoException();
                    if (isVisible) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case C0473R.styleable.SwitchButton_insetRight /*18*/:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    i = hashCodeRemote();
                    reply.writeNoException();
                    reply.writeInt(i);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    LatLng getCenter() throws RemoteException;

    int getFillColor() throws RemoteException;

    String getId() throws RemoteException;

    double getRadius() throws RemoteException;

    int getStrokeColor() throws RemoteException;

    float getStrokeWidth() throws RemoteException;

    float getZIndex() throws RemoteException;

    int hashCodeRemote() throws RemoteException;

    boolean isVisible() throws RemoteException;

    void remove() throws RemoteException;

    void setCenter(LatLng latLng) throws RemoteException;

    void setFillColor(int i) throws RemoteException;

    void setRadius(double d) throws RemoteException;

    void setStrokeColor(int i) throws RemoteException;

    void setStrokeWidth(float f) throws RemoteException;

    void setVisible(boolean z) throws RemoteException;

    void setZIndex(float f) throws RemoteException;

    boolean zza(zzb com_google_android_gms_maps_model_internal_zzb) throws RemoteException;
}
