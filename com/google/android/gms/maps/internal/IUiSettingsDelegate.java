package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public interface IUiSettingsDelegate extends IInterface {

    public static abstract class zza extends Binder implements IUiSettingsDelegate {

        private static class zza implements IUiSettingsDelegate {
            private IBinder zzoo;

            zza(IBinder iBinder) {
                this.zzoo = iBinder;
            }

            public final IBinder asBinder() {
                return this.zzoo;
            }

            public final boolean isCompassEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zzoo.transact(10, obtain, obtain2, 0);
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

            public final boolean isIndoorLevelPickerEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
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

            public final boolean isMapToolbarEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zzoo.transact(19, obtain, obtain2, 0);
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

            public final boolean isMyLocationButtonEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
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

            public final boolean isRotateGesturesEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zzoo.transact(15, obtain, obtain2, 0);
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

            public final boolean isScrollGesturesEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zzoo.transact(12, obtain, obtain2, 0);
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

            public final boolean isTiltGesturesEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zzoo.transact(14, obtain, obtain2, 0);
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

            public final boolean isZoomControlsEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zzoo.transact(9, obtain, obtain2, 0);
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

            public final boolean isZoomGesturesEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zzoo.transact(13, obtain, obtain2, 0);
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

            public final void setAllGesturesEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (enabled) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoo.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setCompassEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (enabled) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoo.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setIndoorLevelPickerEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (enabled) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoo.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setMapToolbarEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (enabled) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoo.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setMyLocationButtonEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (enabled) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoo.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setRotateGesturesEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (enabled) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoo.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setScrollGesturesEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (enabled) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoo.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setTiltGesturesEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (enabled) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoo.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setZoomControlsEnabled(boolean enabled) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (!enabled) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    this.zzoo.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setZoomGesturesEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (enabled) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoo.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static IUiSettingsDelegate zzcX(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IUiSettingsDelegate)) ? new zza(iBinder) : (IUiSettingsDelegate) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            boolean z;
            boolean isZoomControlsEnabled;
            switch (code) {
                case Logger.SEVERE /*1*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setZoomControlsEnabled(z);
                    reply.writeNoException();
                    return true;
                case Logger.WARNING /*2*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setCompassEnabled(z);
                    reply.writeNoException();
                    return true;
                case Logger.INFO /*3*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setMyLocationButtonEnabled(z);
                    reply.writeNoException();
                    return true;
                case Logger.CONFIG /*4*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setScrollGesturesEnabled(z);
                    reply.writeNoException();
                    return true;
                case Logger.FINE /*5*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setZoomGesturesEnabled(z);
                    reply.writeNoException();
                    return true;
                case Logger.FINER /*6*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setTiltGesturesEnabled(z);
                    reply.writeNoException();
                    return true;
                case Logger.FINEST /*7*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setRotateGesturesEnabled(z);
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setAllGesturesEnabled(z);
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    isZoomControlsEnabled = isZoomControlsEnabled();
                    reply.writeNoException();
                    if (isZoomControlsEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    isZoomControlsEnabled = isCompassEnabled();
                    reply.writeNoException();
                    if (isZoomControlsEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case C0473R.styleable.SwitchButton_offColor /*11*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    isZoomControlsEnabled = isMyLocationButtonEnabled();
                    reply.writeNoException();
                    if (isZoomControlsEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case C0473R.styleable.SwitchButton_thumbColor /*12*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    isZoomControlsEnabled = isScrollGesturesEnabled();
                    reply.writeNoException();
                    if (isZoomControlsEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case C0473R.styleable.SwitchButton_thumbPressedColor /*13*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    isZoomControlsEnabled = isZoomGesturesEnabled();
                    reply.writeNoException();
                    if (isZoomControlsEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case C0473R.styleable.SwitchButton_animationVelocity /*14*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    isZoomControlsEnabled = isTiltGesturesEnabled();
                    reply.writeNoException();
                    if (isZoomControlsEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case C0473R.styleable.SwitchButton_radius /*15*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    isZoomControlsEnabled = isRotateGesturesEnabled();
                    reply.writeNoException();
                    if (isZoomControlsEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case C0473R.styleable.SwitchButton_measureFactor /*16*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setIndoorLevelPickerEnabled(z);
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_insetLeft /*17*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    isZoomControlsEnabled = isIndoorLevelPickerEnabled();
                    reply.writeNoException();
                    if (isZoomControlsEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case C0473R.styleable.SwitchButton_insetRight /*18*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setMapToolbarEnabled(z);
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_insetTop /*19*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    isZoomControlsEnabled = isMapToolbarEnabled();
                    reply.writeNoException();
                    if (isZoomControlsEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    boolean isCompassEnabled() throws RemoteException;

    boolean isIndoorLevelPickerEnabled() throws RemoteException;

    boolean isMapToolbarEnabled() throws RemoteException;

    boolean isMyLocationButtonEnabled() throws RemoteException;

    boolean isRotateGesturesEnabled() throws RemoteException;

    boolean isScrollGesturesEnabled() throws RemoteException;

    boolean isTiltGesturesEnabled() throws RemoteException;

    boolean isZoomControlsEnabled() throws RemoteException;

    boolean isZoomGesturesEnabled() throws RemoteException;

    void setAllGesturesEnabled(boolean z) throws RemoteException;

    void setCompassEnabled(boolean z) throws RemoteException;

    void setIndoorLevelPickerEnabled(boolean z) throws RemoteException;

    void setMapToolbarEnabled(boolean z) throws RemoteException;

    void setMyLocationButtonEnabled(boolean z) throws RemoteException;

    void setRotateGesturesEnabled(boolean z) throws RemoteException;

    void setScrollGesturesEnabled(boolean z) throws RemoteException;

    void setTiltGesturesEnabled(boolean z) throws RemoteException;

    void setZoomControlsEnabled(boolean z) throws RemoteException;

    void setZoomGesturesEnabled(boolean z) throws RemoteException;
}
