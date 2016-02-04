package com.shamchat.map;

import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
import com.shamchat.activity.ProgressBarLoadingDialog;
import com.shamchat.androidclient.listeners.OnLocationListener;

public class GPSTracker extends Service implements LocationListener {
    boolean canGetLocation;
    Builder dialog;
    double latitude;
    private OnLocationListener listener;
    Location location;
    protected LocationManager locationManager;
    double longitude;
    final Context mContext;

    /* renamed from: com.shamchat.map.GPSTracker.1 */
    class C10941 implements OnClickListener {
        C10941() {
        }

        public final void onClick(DialogInterface paramDialogInterface, int paramInt) {
            paramDialogInterface.cancel();
            GPSTracker gPSTracker = GPSTracker.this;
            Builder builder = new Builder(gPSTracker.mContext);
            builder.setTitle("GPS is settings");
            builder.setMessage("GPS is not enabled. Do you want to go to settings menu?");
            builder.setPositiveButton("Settings", new C10963());
            builder.setNegativeButton("Cancel", new C10974());
            builder.show();
        }
    }

    /* renamed from: com.shamchat.map.GPSTracker.2 */
    class C10952 implements OnClickListener {
        C10952() {
        }

        public final void onClick(DialogInterface paramDialogInterface, int paramInt) {
            paramDialogInterface.cancel();
        }
    }

    /* renamed from: com.shamchat.map.GPSTracker.3 */
    class C10963 implements OnClickListener {
        C10963() {
        }

        public final void onClick(DialogInterface dialog, int which) {
            GPSTracker.this.mContext.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        }
    }

    /* renamed from: com.shamchat.map.GPSTracker.4 */
    class C10974 implements OnClickListener {
        C10974() {
        }

        public final void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    public GPSTracker(Context context, OnLocationListener listener) {
        this.canGetLocation = false;
        this.mContext = context;
        this.listener = listener;
    }

    public final Location getLocation() {
        try {
            boolean isProviderEnabled;
            if (this.locationManager == null) {
                this.locationManager = (LocationManager) this.mContext.getSystemService("location");
            }
            try {
                isProviderEnabled = this.locationManager.isProviderEnabled("gps");
            } catch (Exception e) {
                isProviderEnabled = false;
            }
            boolean isProviderEnabled2;
            try {
                isProviderEnabled2 = this.locationManager.isProviderEnabled("network");
            } catch (Exception e2) {
                isProviderEnabled2 = false;
            }
            if (!(isProviderEnabled || r0)) {
                ProgressBarLoadingDialog.getInstance().dismiss();
                this.dialog = new Builder(this.mContext);
                this.dialog.setMessage(this.mContext.getResources().getString(2131493155));
                this.dialog.setPositiveButton(this.mContext.getResources().getString(2131493244), new C10941());
                this.dialog.setNegativeButton(this.mContext.getString(2131492872), new C10952());
                this.dialog.show();
            }
            this.canGetLocation = true;
            Criteria criteria = new Criteria();
            criteria.setAccuracy(2);
            criteria.setPowerRequirement(1);
            String locationProviderName = this.locationManager.getBestProvider(criteria, true);
            if (locationProviderName == null || locationProviderName.length() <= 0) {
                return null;
            }
            this.locationManager.requestLocationUpdates(locationProviderName, 3000, 1.0f, this);
            this.location = this.locationManager.getLastKnownLocation("network");
            if (this.location != null) {
                this.latitude = this.location.getLatitude();
                this.longitude = this.location.getLongitude();
            } else {
                this.locationManager.requestLocationUpdates("gps", 3000, 1.0f, this);
                this.location = this.locationManager.getLastKnownLocation("gps");
                this.latitude = this.location.getLatitude();
                this.longitude = this.location.getLongitude();
            }
            if (this.location != null) {
                new GetAddressTask(this.mContext, this.listener).execute(new Location[]{this.location});
            }
            return this.location;
        } catch (Exception e3) {
            e3.printStackTrace();
            ProgressBarLoadingDialog.getInstance().dismiss();
            Toast.makeText(this.mContext, 2131493187, 0).show();
        }
    }

    public void onLocationChanged(Location location) {
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}
