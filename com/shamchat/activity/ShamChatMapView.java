package com.shamchat.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.C04645;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.shamchat.utils.Utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShamChatMapView extends AppCompatActivity {
    private String address;
    private String fullFilePath;
    private GoogleMap map;

    /* renamed from: com.shamchat.activity.ShamChatMapView.1 */
    class C08911 implements SnapshotReadyCallback {
        C08911() {
        }

        public final void onSnapshotReady(Bitmap bitmap) {
            try {
                FileOutputStream fOut = new FileOutputStream(new File(ShamChatMapView.this.fullFilePath));
                bitmap.compress(CompressFormat.PNG, 85, fOut);
                fOut.flush();
                fOut.close();
                Intent data = new Intent();
                data.putExtra("description", ShamChatMapView.this.address);
                data.putExtra("type", 1);
                ShamChatMapView.this.setResult(-1, data);
                ShamChatMapView.this.finish();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(2130903090);
        initializeActionBar();
        if (checkPlayServices()) {
            loadData();
            ProgressBarLoadingDialog.getInstance().dismiss();
        }
    }

    private void initializeActionBar() {
        getSupportActionBar().setDisplayOptions(31);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131623945, menu);
        return true;
    }

    private void loadData() {
        this.fullFilePath = getIntent().getStringExtra("fullFilePath");
        double latitude = getIntent().getDoubleExtra("latitude", 0.0d);
        double longitude = getIntent().getDoubleExtra("longitude", 0.0d);
        this.address = getIntent().getStringExtra("address");
        LatLng mapLocation = new LatLng(latitude, longitude);
        this.map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(2131362036)).getMap();
        GoogleMap googleMap = this.map;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.zzaPH = mapLocation;
        markerOptions.zzank = this.address;
        markerOptions.zzaQB = Utils.formatDate(System.currentTimeMillis(), "hh:mm a, MMMM dd, yyyy");
        googleMap.addMarker(markerOptions).showInfoWindow();
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom$6c32fdd3(mapLocation));
        this.map.animateCamera$ee6a4a2(CameraUpdateFactory.zoomTo$44fa1c82());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                setResult(0);
                finish();
                return true;
            case 2131362566:
                if (!checkPlayServices()) {
                    return true;
                }
                GoogleMap googleMap = this.map;
                try {
                    googleMap.zzaOy.snapshot(new C04645(googleMap, new C08911()), null);
                    return true;
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                }
            default:
                return false;
        }
    }

    private boolean checkPlayServices() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status == 0) {
            return true;
        }
        if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            Toast.makeText(this, 2131493154, 0).show();
            ProgressBarLoadingDialog.getInstance().dismiss();
            return false;
        }
        Toast.makeText(this, 2131493098, 1).show();
        ProgressBarLoadingDialog.getInstance().dismiss();
        finish();
        return false;
    }
}
