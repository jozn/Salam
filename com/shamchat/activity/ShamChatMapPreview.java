package com.shamchat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shamchat.utils.Utils;

public class ShamChatMapPreview extends AppCompatActivity {
    private String address;
    private GoogleMap map;

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(2130903090);
        initializeActionBar();
        loadData();
    }

    private void initializeActionBar() {
        getSupportActionBar().setDisplayOptions(31);
    }

    private void loadData() {
        if (Utils.checkPlayServices(this)) {
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
            return;
        }
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                setResult(0);
                finish();
                return true;
            default:
                return false;
        }
    }
}
