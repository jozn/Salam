package com.shamchat.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import com.shamchat.androidclient.listeners.OnLocationListener;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public final class GetAddressTask extends AsyncTask<Location, Void, String> {
    private OnLocationListener listener;
    private Location loc;
    Context mContext;
    String returnAddress;

    protected final /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
        String str = (String) obj;
        System.out.println("Current address " + str);
        this.returnAddress = str;
        this.listener.onAddressReceived(this.loc, str);
    }

    public GetAddressTask(Context context, OnLocationListener listener) {
        this.returnAddress = BuildConfig.VERSION_NAME;
        this.listener = listener;
        this.mContext = context;
    }

    private String doInBackground(Location... params) {
        Geocoder geocoder = new Geocoder(this.mContext, Locale.getDefault());
        this.loc = params[0];
        try {
            GetReverseGeoCoding geoCodingAPI = new GetReverseGeoCoding(this.loc.getLatitude(), this.loc.getLongitude());
            geoCodingAPI.getAddress();
            String addressAPI = geoCodingAPI.Address1;
            if (addressAPI != null) {
                return addressAPI;
            }
            List<Address> addresses = geocoder.getFromLocation(this.loc.getLatitude(), this.loc.getLongitude(), 1);
            if (addresses == null || addresses.size() <= 0) {
                return "No address found";
            }
            Address address = (Address) addresses.get(0);
            return String.format("%s, %s", new Object[]{address.getLocality(), address.getCountryName()});
        } catch (IOException e1) {
            Log.e("LocationSampleActivity", "IO Exception in getFromLocation()");
            e1.printStackTrace();
            return "Not found";
        } catch (IllegalArgumentException e2) {
            String errorString = "Illegal arguments " + Double.toString(this.loc.getLatitude()) + " , " + Double.toString(this.loc.getLongitude()) + " passed to address service";
            Log.e("LocationSampleActivity", errorString);
            e2.printStackTrace();
            return errorString;
        }
    }
}
