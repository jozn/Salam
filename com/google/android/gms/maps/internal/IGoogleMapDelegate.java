package com.google.android.gms.maps.internal;

import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.appcompat.C0170R;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.internal.IPolylineDelegate;
import com.google.android.gms.maps.model.internal.zzb;
import com.google.android.gms.maps.model.internal.zzc;
import com.google.android.gms.maps.model.internal.zzf;
import com.google.android.gms.maps.model.internal.zzg;
import com.google.android.gms.maps.model.internal.zzh;
import com.google.android.gms.maps.model.zzi;
import com.google.android.gms.maps.model.zzo;
import com.kyleduo.switchbutton.C0473R;
import com.shamchat.activity.FavoriteMessagesActivity;
import com.shamchat.activity.MainChatItemsFragment;
import com.shamchat.activity.MessageDetailsActivity;
import com.squareup.okhttp.internal.http.HttpEngine;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public interface IGoogleMapDelegate extends IInterface {

    public static abstract class zza extends Binder implements IGoogleMapDelegate {

        private static class zza implements IGoogleMapDelegate {
            private IBinder zzoo;

            zza(IBinder iBinder) {
                this.zzoo = iBinder;
            }

            public final zzb addCircle(CircleOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (options != null) {
                        obtain.writeInt(1);
                        options.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(35, obtain, obtain2, 0);
                    obtain2.readException();
                    zzb zzcZ = com.google.android.gms.maps.model.internal.zzb.zza.zzcZ(obtain2.readStrongBinder());
                    return zzcZ;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final zzc addGroundOverlay(GroundOverlayOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (options != null) {
                        obtain.writeInt(1);
                        options.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    zzc zzda = com.google.android.gms.maps.model.internal.zzc.zza.zzda(obtain2.readStrongBinder());
                    return zzda;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final zzf addMarker(MarkerOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (options != null) {
                        obtain.writeInt(1);
                        options.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    zzf zzdd = com.google.android.gms.maps.model.internal.zzf.zza.zzdd(obtain2.readStrongBinder());
                    return zzdd;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final zzg addPolygon(PolygonOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (options != null) {
                        obtain.writeInt(1);
                        options.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    zzg zzde = com.google.android.gms.maps.model.internal.zzg.zza.zzde(obtain2.readStrongBinder());
                    return zzde;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final IPolylineDelegate addPolyline(PolylineOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (options != null) {
                        obtain.writeInt(1);
                        options.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    IPolylineDelegate zzdf = com.google.android.gms.maps.model.internal.IPolylineDelegate.zza.zzdf(obtain2.readStrongBinder());
                    return zzdf;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final zzh addTileOverlay(TileOverlayOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (options != null) {
                        obtain.writeInt(1);
                        options.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    zzh zzdg = com.google.android.gms.maps.model.internal.zzh.zza.zzdg(obtain2.readStrongBinder());
                    return zzdg;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void animateCamera(zzd update) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(update != null ? update.asBinder() : null);
                    this.zzoo.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void animateCameraWithCallback(zzd update, zzb callback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(update != null ? update.asBinder() : null);
                    if (callback != null) {
                        iBinder = callback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzoo.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void animateCameraWithDurationAndCallback(zzd update, int durationMs, zzb callback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(update != null ? update.asBinder() : null);
                    obtain.writeInt(durationMs);
                    if (callback != null) {
                        iBinder = callback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzoo.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final IBinder asBinder() {
                return this.zzoo;
            }

            public final void clear() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final CameraPosition getCameraPosition() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    CameraPosition zzfm;
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        com.google.android.gms.maps.model.zza com_google_android_gms_maps_model_zza = CameraPosition.CREATOR;
                        zzfm = com.google.android.gms.maps.model.zza.zzfm(obtain2);
                    } else {
                        zzfm = null;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return zzfm;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final com.google.android.gms.maps.model.internal.zzd getFocusedBuilding() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(44, obtain, obtain2, 0);
                    obtain2.readException();
                    com.google.android.gms.maps.model.internal.zzd zzdb = com.google.android.gms.maps.model.internal.zzd.zza.zzdb(obtain2.readStrongBinder());
                    return zzdb;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void getMapAsync(zzl callback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.zzoo.transact(53, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final int getMapType() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final float getMaxZoomLevel() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final float getMinZoomLevel() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final Location getMyLocation() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                    Location location = obtain2.readInt() != 0 ? (Location) Location.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return location;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final IProjectionDelegate getProjection() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                    IProjectionDelegate zzcS = com.google.android.gms.maps.internal.IProjectionDelegate.zza.zzcS(obtain2.readStrongBinder());
                    return zzcS;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final IUiSettingsDelegate getUiSettings() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                    IUiSettingsDelegate zzcX = com.google.android.gms.maps.internal.IUiSettingsDelegate.zza.zzcX(obtain2.readStrongBinder());
                    return zzcX;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final boolean isBuildingsEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(40, obtain, obtain2, 0);
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

            public final boolean isIndoorEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
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

            public final boolean isMyLocationEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(21, obtain, obtain2, 0);
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

            public final boolean isTrafficEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
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

            public final void moveCamera(zzd update) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(update != null ? update.asBinder() : null);
                    this.zzoo.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void onCreate(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(54, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void onDestroy() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(57, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (ambientDetails != null) {
                        obtain.writeInt(1);
                        ambientDetails.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(81, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(82, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(58, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(56, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(55, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (outState != null) {
                        obtain.writeInt(1);
                        outState.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoo.transact(60, obtain, obtain2, 0);
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

            public final void setBuildingsEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (enabled) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoo.transact(41, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setContentDescription(String description) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeString(description);
                    this.zzoo.transact(61, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final boolean setIndoorEnabled(boolean enabled) throws RemoteException {
                boolean z = true;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeInt(enabled ? 1 : 0);
                    this.zzoo.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z = false;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setInfoWindowAdapter(zzd adapter) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(adapter != null ? adapter.asBinder() : null);
                    this.zzoo.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setLocationSource(ILocationSourceDelegate source) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(source != null ? source.asBinder() : null);
                    this.zzoo.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setMapType(int type) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeInt(type);
                    this.zzoo.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setMyLocationEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (enabled) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzoo.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setOnCameraChangeListener(zze listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoo.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setOnIndoorStateChangeListener(zzf listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoo.transact(45, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setOnInfoWindowClickListener(zzg listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoo.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setOnMapClickListener(zzi listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoo.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setOnMapLoadedCallback(zzj callback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.zzoo.transact(42, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setOnMapLongClickListener(zzk listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoo.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setOnMarkerClickListener(zzm listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoo.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setOnMarkerDragListener(zzn listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoo.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setOnMyLocationButtonClickListener(zzo listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoo.transact(37, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setOnMyLocationChangeListener(zzp listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoo.transact(36, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setOnPoiClickListener(zzq listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.zzoo.transact(80, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setPadding(int left, int top, int right, int bottom) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeInt(left);
                    obtain.writeInt(top);
                    obtain.writeInt(right);
                    obtain.writeInt(bottom);
                    this.zzoo.transact(39, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void setTrafficEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
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

            public final void snapshot(zzw callback, zzd bitmap) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (bitmap != null) {
                        iBinder = bitmap.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzoo.transact(38, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void stopAnimation() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final boolean useViewLifecycleWhenInFragment() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    this.zzoo.transact(59, obtain, obtain2, 0);
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

        public static IGoogleMapDelegate zzcv(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IGoogleMapDelegate)) ? new zza(iBinder) : (IGoogleMapDelegate) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            zzq com_google_android_gms_maps_internal_zzq = null;
            float maxZoomLevel;
            IBinder asBinder;
            boolean isTrafficEnabled;
            boolean z;
            IBinder readStrongBinder;
            IInterface queryLocalInterface;
            switch (code) {
                case Logger.SEVERE /*1*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    CameraPosition cameraPosition = getCameraPosition();
                    reply.writeNoException();
                    if (cameraPosition != null) {
                        reply.writeInt(1);
                        cameraPosition.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case Logger.WARNING /*2*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    maxZoomLevel = getMaxZoomLevel();
                    reply.writeNoException();
                    reply.writeFloat(maxZoomLevel);
                    return true;
                case Logger.INFO /*3*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    maxZoomLevel = getMinZoomLevel();
                    reply.writeNoException();
                    reply.writeFloat(maxZoomLevel);
                    return true;
                case Logger.CONFIG /*4*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    moveCamera(com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Logger.FINE /*5*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    animateCamera(com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Logger.FINER /*6*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    animateCameraWithCallback(com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()), com.google.android.gms.maps.internal.zzb.zza.zzct(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Logger.FINEST /*7*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    animateCameraWithDurationAndCallback(com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()), data.readInt(), com.google.android.gms.maps.internal.zzb.zza.zzct(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    stopAnimation();
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    PolylineOptions zzfu;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (data.readInt() != 0) {
                        zzi com_google_android_gms_maps_model_zzi = PolylineOptions.CREATOR;
                        zzfu = zzi.zzfu(data);
                    } else {
                        zzfu = null;
                    }
                    IPolylineDelegate addPolyline = addPolyline(zzfu);
                    reply.writeNoException();
                    if (addPolyline != null) {
                        asBinder = addPolyline.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    zzg addPolygon = addPolygon(data.readInt() != 0 ? PolygonOptions.CREATOR.zzft(data) : null);
                    reply.writeNoException();
                    if (addPolygon != null) {
                        asBinder = addPolygon.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case C0473R.styleable.SwitchButton_offColor /*11*/:
                    MarkerOptions zzfr;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (data.readInt() != 0) {
                        com.google.android.gms.maps.model.zzf com_google_android_gms_maps_model_zzf = MarkerOptions.CREATOR;
                        zzfr = com.google.android.gms.maps.model.zzf.zzfr(data);
                    } else {
                        zzfr = null;
                    }
                    zzf addMarker = addMarker(zzfr);
                    reply.writeNoException();
                    if (addMarker != null) {
                        asBinder = addMarker.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case C0473R.styleable.SwitchButton_thumbColor /*12*/:
                    GroundOverlayOptions zzfo;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (data.readInt() != 0) {
                        com.google.android.gms.maps.model.zzc com_google_android_gms_maps_model_zzc = GroundOverlayOptions.CREATOR;
                        zzfo = com.google.android.gms.maps.model.zzc.zzfo(data);
                    } else {
                        zzfo = null;
                    }
                    zzc addGroundOverlay = addGroundOverlay(zzfo);
                    reply.writeNoException();
                    if (addGroundOverlay != null) {
                        asBinder = addGroundOverlay.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case C0473R.styleable.SwitchButton_thumbPressedColor /*13*/:
                    TileOverlayOptions zzfA;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (data.readInt() != 0) {
                        zzo com_google_android_gms_maps_model_zzo = TileOverlayOptions.CREATOR;
                        zzfA = zzo.zzfA(data);
                    } else {
                        zzfA = null;
                    }
                    zzh addTileOverlay = addTileOverlay(zzfA);
                    reply.writeNoException();
                    if (addTileOverlay != null) {
                        asBinder = addTileOverlay.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case C0473R.styleable.SwitchButton_animationVelocity /*14*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    clear();
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_radius /*15*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    int mapType = getMapType();
                    reply.writeNoException();
                    reply.writeInt(mapType);
                    return true;
                case C0473R.styleable.SwitchButton_measureFactor /*16*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setMapType(data.readInt());
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_insetLeft /*17*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = isTrafficEnabled();
                    reply.writeNoException();
                    reply.writeInt(isTrafficEnabled ? 1 : 0);
                    return true;
                case C0473R.styleable.SwitchButton_insetRight /*18*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setTrafficEnabled(z);
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_insetTop /*19*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = isIndoorEnabled();
                    reply.writeNoException();
                    if (isTrafficEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case HttpEngine.MAX_FOLLOW_UPS /*20*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = setIndoorEnabled(data.readInt() != 0);
                    reply.writeNoException();
                    if (isTrafficEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case C0170R.styleable.Toolbar_navigationContentDescription /*21*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = isMyLocationEnabled();
                    reply.writeNoException();
                    if (isTrafficEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case C0170R.styleable.Toolbar_logoDescription /*22*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setMyLocationEnabled(z);
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Toolbar_titleTextColor /*23*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    Location myLocation = getMyLocation();
                    reply.writeNoException();
                    if (myLocation != null) {
                        reply.writeInt(1);
                        myLocation.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case MessageDetailsActivity.REQUEST_EDIT_TEXT /*24*/:
                    ILocationSourceDelegate com_google_android_gms_maps_internal_ILocationSourceDelegate_zza_zza;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    readStrongBinder = data.readStrongBinder();
                    if (readStrongBinder != null) {
                        queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.ILocationSourceDelegate");
                        com_google_android_gms_maps_internal_ILocationSourceDelegate_zza_zza = (queryLocalInterface == null || !(queryLocalInterface instanceof ILocationSourceDelegate)) ? new zza(readStrongBinder) : (ILocationSourceDelegate) queryLocalInterface;
                    }
                    setLocationSource(com_google_android_gms_maps_internal_ILocationSourceDelegate_zza_zza);
                    reply.writeNoException();
                    return true;
                case MainChatItemsFragment.REQUEST_NEW_IMAGES /*25*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    IUiSettingsDelegate uiSettings = getUiSettings();
                    reply.writeNoException();
                    if (uiSettings != null) {
                        asBinder = uiSettings.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case FavoriteMessagesActivity.REQUEST_IMAGE_CAPTURE /*26*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    IProjectionDelegate projection = getProjection();
                    reply.writeNoException();
                    if (projection != null) {
                        asBinder = projection.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case C0170R.styleable.Theme_actionModeStyle /*27*/:
                    zze com_google_android_gms_maps_internal_zze_zza_zza;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    readStrongBinder = data.readStrongBinder();
                    if (readStrongBinder != null) {
                        queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IOnCameraChangeListener");
                        com_google_android_gms_maps_internal_zze_zza_zza = (queryLocalInterface == null || !(queryLocalInterface instanceof zze)) ? new zza(readStrongBinder) : (zze) queryLocalInterface;
                    }
                    setOnCameraChangeListener(com_google_android_gms_maps_internal_zze_zza_zza);
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_actionModeCloseButtonStyle /*28*/:
                    zzi com_google_android_gms_maps_internal_zzi_zza_zza;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    readStrongBinder = data.readStrongBinder();
                    if (readStrongBinder != null) {
                        queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IOnMapClickListener");
                        com_google_android_gms_maps_internal_zzi_zza_zza = (queryLocalInterface == null || !(queryLocalInterface instanceof zzi)) ? new zza(readStrongBinder) : (zzi) queryLocalInterface;
                    }
                    setOnMapClickListener(com_google_android_gms_maps_internal_zzi_zza_zza);
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_actionModeBackground /*29*/:
                    zzk com_google_android_gms_maps_internal_zzk_zza_zza;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    readStrongBinder = data.readStrongBinder();
                    if (readStrongBinder != null) {
                        queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IOnMapLongClickListener");
                        com_google_android_gms_maps_internal_zzk_zza_zza = (queryLocalInterface == null || !(queryLocalInterface instanceof zzk)) ? new zza(readStrongBinder) : (zzk) queryLocalInterface;
                    }
                    setOnMapLongClickListener(com_google_android_gms_maps_internal_zzk_zza_zza);
                    reply.writeNoException();
                    return true;
                case MqttConnectOptions.CONNECTION_TIMEOUT_DEFAULT /*30*/:
                    zzm com_google_android_gms_maps_internal_zzm_zza_zza;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    readStrongBinder = data.readStrongBinder();
                    if (readStrongBinder != null) {
                        queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IOnMarkerClickListener");
                        com_google_android_gms_maps_internal_zzm_zza_zza = (queryLocalInterface == null || !(queryLocalInterface instanceof zzm)) ? new zza(readStrongBinder) : (zzm) queryLocalInterface;
                    }
                    setOnMarkerClickListener(com_google_android_gms_maps_internal_zzm_zza_zza);
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_actionModeCloseDrawable /*31*/:
                    zzn com_google_android_gms_maps_internal_zzn_zza_zza;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    readStrongBinder = data.readStrongBinder();
                    if (readStrongBinder != null) {
                        queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IOnMarkerDragListener");
                        com_google_android_gms_maps_internal_zzn_zza_zza = (queryLocalInterface == null || !(queryLocalInterface instanceof zzn)) ? new zza(readStrongBinder) : (zzn) queryLocalInterface;
                    }
                    setOnMarkerDragListener(com_google_android_gms_maps_internal_zzn_zza_zza);
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_actionModeCutDrawable /*32*/:
                    zzg com_google_android_gms_maps_internal_zzg_zza_zza;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    readStrongBinder = data.readStrongBinder();
                    if (readStrongBinder != null) {
                        queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IOnInfoWindowClickListener");
                        com_google_android_gms_maps_internal_zzg_zza_zza = (queryLocalInterface == null || !(queryLocalInterface instanceof zzg)) ? new zza(readStrongBinder) : (zzg) queryLocalInterface;
                    }
                    setOnInfoWindowClickListener(com_google_android_gms_maps_internal_zzg_zza_zza);
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_actionModeCopyDrawable /*33*/:
                    zzd com_google_android_gms_maps_internal_zzd_zza_zza;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    readStrongBinder = data.readStrongBinder();
                    if (readStrongBinder != null) {
                        queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                        com_google_android_gms_maps_internal_zzd_zza_zza = (queryLocalInterface == null || !(queryLocalInterface instanceof zzd)) ? new zza(readStrongBinder) : (zzd) queryLocalInterface;
                    }
                    setInfoWindowAdapter(com_google_android_gms_maps_internal_zzd_zza_zza);
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_actionModeSelectAllDrawable /*35*/:
                    CircleOptions zzfn;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (data.readInt() != 0) {
                        com.google.android.gms.maps.model.zzb com_google_android_gms_maps_model_zzb = CircleOptions.CREATOR;
                        zzfn = com.google.android.gms.maps.model.zzb.zzfn(data);
                    } else {
                        zzfn = null;
                    }
                    zzb addCircle = addCircle(zzfn);
                    reply.writeNoException();
                    if (addCircle != null) {
                        asBinder = addCircle.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case C0170R.styleable.Theme_actionModeShareDrawable /*36*/:
                    zzp com_google_android_gms_maps_internal_zzp_zza_zza;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    readStrongBinder = data.readStrongBinder();
                    if (readStrongBinder != null) {
                        queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IOnMyLocationChangeListener");
                        com_google_android_gms_maps_internal_zzp_zza_zza = (queryLocalInterface == null || !(queryLocalInterface instanceof zzp)) ? new zza(readStrongBinder) : (zzp) queryLocalInterface;
                    }
                    setOnMyLocationChangeListener(com_google_android_gms_maps_internal_zzp_zza_zza);
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_actionModeFindDrawable /*37*/:
                    zzo com_google_android_gms_maps_internal_zzo_zza_zza;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    readStrongBinder = data.readStrongBinder();
                    if (readStrongBinder != null) {
                        queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IOnMyLocationButtonClickListener");
                        com_google_android_gms_maps_internal_zzo_zza_zza = (queryLocalInterface == null || !(queryLocalInterface instanceof zzo)) ? new zza(readStrongBinder) : (zzo) queryLocalInterface;
                    }
                    setOnMyLocationButtonClickListener(com_google_android_gms_maps_internal_zzo_zza_zza);
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_actionModeWebSearchDrawable /*38*/:
                    zzw com_google_android_gms_maps_internal_zzw_zza_zza;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    readStrongBinder = data.readStrongBinder();
                    if (readStrongBinder != null) {
                        queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.ISnapshotReadyCallback");
                        com_google_android_gms_maps_internal_zzw_zza_zza = (queryLocalInterface == null || !(queryLocalInterface instanceof zzw)) ? new zza(readStrongBinder) : (zzw) queryLocalInterface;
                    }
                    snapshot(com_google_android_gms_maps_internal_zzw_zza_zza, com.google.android.gms.dynamic.zzd.zza.zzbs(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_actionModePopupWindowStyle /*39*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setPadding(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_textAppearanceLargePopupMenu /*40*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = isBuildingsEnabled();
                    reply.writeNoException();
                    if (isTrafficEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case C0170R.styleable.Theme_textAppearanceSmallPopupMenu /*41*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setBuildingsEnabled(z);
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_dialogTheme /*42*/:
                    zzj com_google_android_gms_maps_internal_zzj_zza_zza;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    readStrongBinder = data.readStrongBinder();
                    if (readStrongBinder != null) {
                        queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IOnMapLoadedCallback");
                        com_google_android_gms_maps_internal_zzj_zza_zza = (queryLocalInterface == null || !(queryLocalInterface instanceof zzj)) ? new zza(readStrongBinder) : (zzj) queryLocalInterface;
                    }
                    setOnMapLoadedCallback(com_google_android_gms_maps_internal_zzj_zza_zza);
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_listDividerAlertDialog /*44*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    com.google.android.gms.maps.model.internal.zzd focusedBuilding = getFocusedBuilding();
                    reply.writeNoException();
                    if (focusedBuilding != null) {
                        asBinder = focusedBuilding.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case C0170R.styleable.Theme_actionDropDownStyle /*45*/:
                    zzf com_google_android_gms_maps_internal_zzf_zza_zza;
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    readStrongBinder = data.readStrongBinder();
                    if (readStrongBinder != null) {
                        queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IOnIndoorStateChangeListener");
                        com_google_android_gms_maps_internal_zzf_zza_zza = (queryLocalInterface == null || !(queryLocalInterface instanceof zzf)) ? new zza(readStrongBinder) : (zzf) queryLocalInterface;
                    }
                    setOnIndoorStateChangeListener(com_google_android_gms_maps_internal_zzf_zza_zza);
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_selectableItemBackgroundBorderless /*53*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    getMapAsync(com.google.android.gms.maps.internal.zzl.zza.zzcH(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_borderlessButtonStyle /*54*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    onCreate(data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_dividerVertical /*55*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    onResume();
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_dividerHorizontal /*56*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    onPause();
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_activityChooserViewStyle /*57*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    onDestroy();
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_toolbarStyle /*58*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    onLowMemory();
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_toolbarNavigationButtonStyle /*59*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = useViewLifecycleWhenInFragment();
                    reply.writeNoException();
                    if (isTrafficEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case MqttConnectOptions.KEEP_ALIVE_INTERVAL_DEFAULT /*60*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
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
                case C0170R.styleable.Theme_popupWindowStyle /*61*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setContentDescription(data.readString());
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_panelMenuListTheme /*80*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    readStrongBinder = data.readStrongBinder();
                    if (readStrongBinder != null) {
                        queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IOnPoiClickListener");
                        com_google_android_gms_maps_internal_zzq = (queryLocalInterface == null || !(queryLocalInterface instanceof zzq)) ? new zza(readStrongBinder) : (zzq) queryLocalInterface;
                    }
                    setOnPoiClickListener(com_google_android_gms_maps_internal_zzq);
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_listChoiceBackgroundIndicator /*81*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    onEnterAmbient(data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case C0170R.styleable.Theme_colorPrimary /*82*/:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    onExitAmbient();
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    zzb addCircle(CircleOptions circleOptions) throws RemoteException;

    zzc addGroundOverlay(GroundOverlayOptions groundOverlayOptions) throws RemoteException;

    zzf addMarker(MarkerOptions markerOptions) throws RemoteException;

    zzg addPolygon(PolygonOptions polygonOptions) throws RemoteException;

    IPolylineDelegate addPolyline(PolylineOptions polylineOptions) throws RemoteException;

    zzh addTileOverlay(TileOverlayOptions tileOverlayOptions) throws RemoteException;

    void animateCamera(zzd com_google_android_gms_dynamic_zzd) throws RemoteException;

    void animateCameraWithCallback(zzd com_google_android_gms_dynamic_zzd, zzb com_google_android_gms_maps_internal_zzb) throws RemoteException;

    void animateCameraWithDurationAndCallback(zzd com_google_android_gms_dynamic_zzd, int i, zzb com_google_android_gms_maps_internal_zzb) throws RemoteException;

    void clear() throws RemoteException;

    CameraPosition getCameraPosition() throws RemoteException;

    com.google.android.gms.maps.model.internal.zzd getFocusedBuilding() throws RemoteException;

    void getMapAsync(zzl com_google_android_gms_maps_internal_zzl) throws RemoteException;

    int getMapType() throws RemoteException;

    float getMaxZoomLevel() throws RemoteException;

    float getMinZoomLevel() throws RemoteException;

    Location getMyLocation() throws RemoteException;

    IProjectionDelegate getProjection() throws RemoteException;

    IUiSettingsDelegate getUiSettings() throws RemoteException;

    boolean isBuildingsEnabled() throws RemoteException;

    boolean isIndoorEnabled() throws RemoteException;

    boolean isMyLocationEnabled() throws RemoteException;

    boolean isTrafficEnabled() throws RemoteException;

    void moveCamera(zzd com_google_android_gms_dynamic_zzd) throws RemoteException;

    void onCreate(Bundle bundle) throws RemoteException;

    void onDestroy() throws RemoteException;

    void onEnterAmbient(Bundle bundle) throws RemoteException;

    void onExitAmbient() throws RemoteException;

    void onLowMemory() throws RemoteException;

    void onPause() throws RemoteException;

    void onResume() throws RemoteException;

    void onSaveInstanceState(Bundle bundle) throws RemoteException;

    void setBuildingsEnabled(boolean z) throws RemoteException;

    void setContentDescription(String str) throws RemoteException;

    boolean setIndoorEnabled(boolean z) throws RemoteException;

    void setInfoWindowAdapter(zzd com_google_android_gms_maps_internal_zzd) throws RemoteException;

    void setLocationSource(ILocationSourceDelegate iLocationSourceDelegate) throws RemoteException;

    void setMapType(int i) throws RemoteException;

    void setMyLocationEnabled(boolean z) throws RemoteException;

    void setOnCameraChangeListener(zze com_google_android_gms_maps_internal_zze) throws RemoteException;

    void setOnIndoorStateChangeListener(zzf com_google_android_gms_maps_internal_zzf) throws RemoteException;

    void setOnInfoWindowClickListener(zzg com_google_android_gms_maps_internal_zzg) throws RemoteException;

    void setOnMapClickListener(zzi com_google_android_gms_maps_internal_zzi) throws RemoteException;

    void setOnMapLoadedCallback(zzj com_google_android_gms_maps_internal_zzj) throws RemoteException;

    void setOnMapLongClickListener(zzk com_google_android_gms_maps_internal_zzk) throws RemoteException;

    void setOnMarkerClickListener(zzm com_google_android_gms_maps_internal_zzm) throws RemoteException;

    void setOnMarkerDragListener(zzn com_google_android_gms_maps_internal_zzn) throws RemoteException;

    void setOnMyLocationButtonClickListener(zzo com_google_android_gms_maps_internal_zzo) throws RemoteException;

    void setOnMyLocationChangeListener(zzp com_google_android_gms_maps_internal_zzp) throws RemoteException;

    void setOnPoiClickListener(zzq com_google_android_gms_maps_internal_zzq) throws RemoteException;

    void setPadding(int i, int i2, int i3, int i4) throws RemoteException;

    void setTrafficEnabled(boolean z) throws RemoteException;

    void snapshot(zzw com_google_android_gms_maps_internal_zzw, zzd com_google_android_gms_dynamic_zzd) throws RemoteException;

    void stopAnimation() throws RemoteException;

    boolean useViewLifecycleWhenInFragment() throws RemoteException;
}
