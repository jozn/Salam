package com.shamchat.map;

import android.support.v7.appcompat.BuildConfig;
import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public final class GetReverseGeoCoding {
    String Address1;
    private String Address2;
    private String City;
    private String Country;
    private String County;
    private String PIN;
    private String State;
    double latitude;
    double longitude;

    public GetReverseGeoCoding(double latitude, double longitude) {
        this.Address1 = BuildConfig.VERSION_NAME;
        this.Address2 = BuildConfig.VERSION_NAME;
        this.City = BuildConfig.VERSION_NAME;
        this.State = BuildConfig.VERSION_NAME;
        this.Country = BuildConfig.VERSION_NAME;
        this.County = BuildConfig.VERSION_NAME;
        this.PIN = BuildConfig.VERSION_NAME;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public final void getAddress() {
        this.Address1 = BuildConfig.VERSION_NAME;
        this.Address2 = BuildConfig.VERSION_NAME;
        this.City = BuildConfig.VERSION_NAME;
        this.State = BuildConfig.VERSION_NAME;
        this.Country = BuildConfig.VERSION_NAME;
        this.County = BuildConfig.VERSION_NAME;
        this.PIN = BuildConfig.VERSION_NAME;
        try {
            JSONObject jsonObj = Parser_Json.getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + this.latitude + "," + this.longitude + "&sensor=true");
            if (jsonObj.getString(NotificationCompatApi21.CATEGORY_STATUS).equalsIgnoreCase("OK")) {
                JSONArray address_components = jsonObj.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");
                    String Type = zero2.getJSONArray("types").getString(0);
                    if (!TextUtils.isEmpty(long_name) || !long_name.equals(null) || long_name.length() > 0 || long_name != BuildConfig.VERSION_NAME) {
                        if (Type.equalsIgnoreCase("street_number")) {
                            this.Address1 = long_name + " ";
                        } else if (Type.equalsIgnoreCase("route")) {
                            this.Address1 += long_name;
                        } else if (Type.equalsIgnoreCase("sublocality")) {
                            this.Address2 = long_name;
                        } else if (Type.equalsIgnoreCase("locality")) {
                            this.City = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                            this.County = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                            this.State = long_name;
                        } else if (Type.equalsIgnoreCase("country")) {
                            this.Country = long_name;
                        } else if (Type.equalsIgnoreCase("postal_code")) {
                            this.PIN = long_name;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
