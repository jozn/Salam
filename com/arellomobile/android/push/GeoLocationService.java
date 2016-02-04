package com.arellomobile.android.push;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import com.arellomobile.android.push.data.PushZoneLocation;
import com.arellomobile.android.push.utils.WorkerTask;
import com.arellomobile.android.push.utils.executor.ExecutorHelper;

public class GeoLocationService extends Service {
    private boolean mIfUpdating;
    private final LocationListener mListener;
    private LocationManager mLocationManager;
    private long mMinDistance;
    private Location mOldLocation;
    private final Object mSyncObject;
    private WakeLock mWakeLock;

    /* renamed from: com.arellomobile.android.push.GeoLocationService.1 */
    class C02221 extends WorkerTask {
        protected PushZoneLocation mZoneLocation;
        final /* synthetic */ Location val$location;

        C02221(Context context, Location location) {
            this.val$location = location;
            super(context);
        }

        protected final void doWork(Context context) throws Exception {
            this.mZoneLocation = DeviceFeature2_5.getNearestZone(context, this.val$location);
        }

        protected final /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
            super.onPostExecute((Void) obj);
            synchronized (GeoLocationService.this.mSyncObject) {
                long j = 0;
                if (this.mZoneLocation != null) {
                    j = this.mZoneLocation.mDistanceTo;
                }
                j = Math.max(10, j);
                GeoLocationService.this.requestUpdatesFromProvider("gps", j);
                GeoLocationService.this.requestUpdatesFromProvider("network", j);
                GeoLocationService.this.mIfUpdating = false;
            }
        }
    }

    /* renamed from: com.arellomobile.android.push.GeoLocationService.2 */
    class C02232 implements LocationListener {
        C02232() {
        }

        public final void onLocationChanged(Location location) {
            GeoLocationService.this.updateLocation(location);
        }

        public final void onProviderDisabled(String str) {
        }

        public final void onProviderEnabled(String str) {
        }

        public final void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    public GeoLocationService() {
        this.mSyncObject = new Object();
        this.mIfUpdating = false;
        this.mListener = new C02232();
    }

    private Location requestUpdatesFromProvider(String str, long j) {
        this.mMinDistance = j;
        try {
            if (this.mLocationManager.isProviderEnabled(str)) {
                this.mLocationManager.requestLocationUpdates(str, 10000, (float) j, this.mListener);
                return this.mLocationManager.getLastKnownLocation(str);
            }
        } catch (Throwable e) {
            Log.e(getClass().getName(), "Check ACCESS_FINE_LOCATION permission", e);
        }
        return null;
    }

    private void updateLocation(Location location) {
        synchronized (this.mSyncObject) {
            if (!this.mIfUpdating) {
                boolean z = getSharedPreferences("com.google.android.gcm", 0).getBoolean("onServer", false);
                Log.v("GCMRegistrar", "Is registered on server: " + z);
                if (z) {
                    if (this.mOldLocation == null || location.distanceTo(this.mOldLocation) >= ((float) this.mMinDistance)) {
                        this.mOldLocation = location;
                        this.mLocationManager.removeUpdates(this.mListener);
                        this.mIfUpdating = true;
                        ExecutorHelper.executeAsyncTask(new C02221(this, location));
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
        this.mLocationManager.removeUpdates(this.mListener);
        this.mWakeLock.release();
        this.mWakeLock = null;
        this.mLocationManager = null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        boolean z = false;
        super.onStartCommand(intent, i, i2);
        this.mWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, getClass().getName());
        this.mWakeLock.acquire();
        this.mLocationManager = (LocationManager) getSystemService("location");
        synchronized (this.mSyncObject) {
            this.mLocationManager.removeUpdates(this.mListener);
            Location requestUpdatesFromProvider = requestUpdatesFromProvider("gps", 10);
            Location requestUpdatesFromProvider2 = requestUpdatesFromProvider("network", 10);
        }
        if (requestUpdatesFromProvider != null && requestUpdatesFromProvider2 != null) {
            Location location;
            if (requestUpdatesFromProvider2 == null) {
                location = requestUpdatesFromProvider;
            } else {
                int i3;
                boolean z2;
                int i4;
                boolean z3;
                int i5;
                long time = requestUpdatesFromProvider.getTime() - requestUpdatesFromProvider2.getTime();
                if (time > 120000) {
                    i3 = 1;
                } else {
                    z2 = false;
                }
                if (time < -120000) {
                    i4 = 1;
                } else {
                    z3 = false;
                }
                if (time > 0) {
                    i5 = 1;
                } else {
                    boolean z4 = false;
                }
                if (i3 != 0) {
                    location = requestUpdatesFromProvider;
                } else {
                    if (i4 == 0) {
                        i4 = (int) (requestUpdatesFromProvider.getAccuracy() - requestUpdatesFromProvider2.getAccuracy());
                        if (i4 > 0) {
                            int i6 = 1;
                        } else {
                            boolean z5 = false;
                        }
                        if (i4 < 0) {
                            i3 = 1;
                        } else {
                            z2 = false;
                        }
                        if (i4 > 200) {
                            i4 = 1;
                        } else {
                            z3 = false;
                        }
                        String provider = requestUpdatesFromProvider.getProvider();
                        String provider2 = requestUpdatesFromProvider2.getProvider();
                        if (provider != null) {
                            z = provider.equals(provider2);
                        } else if (provider2 == null) {
                            z = true;
                        }
                        if (i3 != 0) {
                            location = requestUpdatesFromProvider;
                        } else if (i5 != 0 && r6 == 0) {
                            location = requestUpdatesFromProvider;
                        } else if (i5 != 0 && r0 == 0 && r2) {
                            location = requestUpdatesFromProvider;
                        }
                    }
                    location = requestUpdatesFromProvider2;
                }
            }
            updateLocation(location);
        } else if (requestUpdatesFromProvider != null) {
            updateLocation(requestUpdatesFromProvider);
        } else if (requestUpdatesFromProvider2 != null) {
            updateLocation(requestUpdatesFromProvider2);
        }
        return 1;
    }
}
