package com.arellomobile.android.push;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import com.arellomobile.android.push.data.PushZoneLocation;
import com.arellomobile.android.push.request.RequestHelper;
import com.arellomobile.android.push.utils.GeneralUtils;
import com.arellomobile.android.push.utils.NetworkUtils;
import com.arellomobile.android.push.utils.NetworkUtils.NetworkResult;
import com.arellomobile.android.push.utils.PreferenceUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class DeviceFeature2_5 {
    public static PushZoneLocation getNearestZone(Context context, Location location) throws Exception {
        Map hashMap = new HashMap();
        Map hashMap2 = new HashMap();
        hashMap2.put("application", PreferenceUtils.getApplicationId(context));
        hashMap2.put("hwid", GeneralUtils.getDeviceUUID(context));
        hashMap2.put("lat", Double.valueOf(location.getLatitude()));
        hashMap2.put("lng", Double.valueOf(location.getLongitude()));
        hashMap.putAll(hashMap2);
        Log.w("PushWoosh DeviceFeature2_5", "Try To Sent Nearest Zone");
        NetworkResult networkResult = new NetworkResult();
        Throwable exception = new Exception();
        Object obj = networkResult;
        int i = 0;
        while (i < 5) {
            try {
                obj = NetworkUtils.makeRequest(hashMap, "getNearestZone");
                if (200 != obj.mResultCode) {
                    continue;
                    i++;
                } else if (200 != obj.mPushwooshCode) {
                    break;
                } else {
                    Log.w("PushWoosh DeviceFeature2_5", "Send Nearest Zone success");
                    JSONObject jSONObject = obj.mResultData.getJSONObject("response");
                    PushZoneLocation pushZoneLocation = new PushZoneLocation();
                    pushZoneLocation.mName = jSONObject.getString("name");
                    pushZoneLocation.mLat = jSONObject.getDouble("lat");
                    pushZoneLocation.mLng = jSONObject.getDouble("lng");
                    pushZoneLocation.mDistanceTo = jSONObject.getLong("distance");
                    return pushZoneLocation;
                }
            } catch (Exception e) {
                exception = e;
            }
        }
        Log.e("PushWoosh DeviceFeature2_5", "ERROR: sent Nearest Zone " + exception.getMessage() + ". Response = " + obj, exception);
        throw exception;
    }

    private static JSONObject jsonObjectFromTagMap(Map<String, Object> map) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        for (String str : map.keySet()) {
            Object obj = map.get(str);
            if (obj instanceof String) {
                String str2 = (String) obj;
                if (str2.startsWith("#pwinc#")) {
                    jSONObject.put(str, jsonObjectFromTagMap(PushManager.incrementalTag(Integer.valueOf(Integer.parseInt(str2.substring(7))))));
                } else {
                    jSONObject.put(str, obj);
                }
            } else if (obj instanceof Integer) {
                jSONObject.put(str, obj);
            } else if (obj instanceof String[]) {
                r6 = new JSONArray();
                for (Object valueOf : (String[]) obj) {
                    r6.put(String.valueOf(valueOf));
                }
                jSONObject.put(str, r6);
            } else if (obj instanceof Integer[]) {
                r6 = new JSONArray();
                for (Object valueOf2 : (Integer[]) obj) {
                    r6.put(String.valueOf(valueOf2));
                }
                jSONObject.put(str, r6);
            } else if (obj instanceof int[]) {
                r6 = new JSONArray();
                for (int valueOf3 : (int[]) obj) {
                    r6.put(String.valueOf(valueOf3));
                }
                jSONObject.put(str, r6);
            } else if (obj instanceof Collection) {
                JSONArray jSONArray = new JSONArray();
                for (Object next : (Collection) obj) {
                    if ((next instanceof String) || (next instanceof Integer)) {
                        jSONArray.put(String.valueOf(next));
                    } else {
                        throw new RuntimeException("wrong type for tag: " + str);
                    }
                }
                jSONObject.put(str, jSONArray);
            } else if (obj instanceof JSONArray) {
                jSONObject.put(str, (JSONArray) obj);
            } else if (obj instanceof Map) {
                jSONObject.put(str, jsonObjectFromTagMap((Map) obj));
            } else {
                throw new RuntimeException("wrong type for tag: " + str);
            }
        }
        return jSONObject;
    }

    public static void sendMessageDeliveryEvent(Context context, String str) {
        if (str != null) {
            Map hashMap = new HashMap();
            hashMap.putAll(RequestHelper.getSendPushStatData(context, str));
            Log.w("PushWoosh DeviceFeature2_5", "Try To sent MsgDelivered");
            NetworkResult networkResult = new NetworkResult();
            Throwable exception = new Exception();
            NetworkResult networkResult2 = networkResult;
            int i = 0;
            while (i < 5) {
                try {
                    networkResult2 = NetworkUtils.makeRequest(hashMap, "messageDeliveryEvent");
                    if (200 != networkResult2.mResultCode) {
                        continue;
                        i++;
                    } else if (200 != networkResult2.mPushwooshCode) {
                        break;
                    } else {
                        Log.w("PushWoosh DeviceFeature2_5", "Send MsgDelivered success");
                        return;
                    }
                } catch (Exception e) {
                    exception = e;
                }
            }
            Log.e("PushWoosh DeviceFeature2_5", "ERROR: Try To sent MsgDelivered " + exception.getMessage() + ". Response = " + networkResult2.mResultData, exception);
        }
    }

    public static JSONArray sendTags(Context context, Map<String, Object> map) throws Exception {
        Map hashMap = new HashMap();
        Map hashMap2 = new HashMap();
        hashMap2.put("application", PreferenceUtils.getApplicationId(context));
        hashMap2.put("hwid", GeneralUtils.getDeviceUUID(context));
        hashMap.putAll(hashMap2);
        hashMap.put("tags", jsonObjectFromTagMap(map));
        Log.w("PushWoosh DeviceFeature2_5", "Try To sent Tags");
        NetworkResult networkResult = new NetworkResult();
        Throwable exception = new Exception();
        Object obj = networkResult;
        int i = 0;
        while (i < 5) {
            try {
                obj = NetworkUtils.makeRequest(hashMap, "setTags");
                if (200 != obj.mResultCode) {
                    continue;
                    i++;
                } else if (200 != obj.mPushwooshCode) {
                    break;
                } else {
                    Log.w("PushWoosh DeviceFeature2_5", "Send Tags success");
                    JSONObject jSONObject = obj.mResultData.getJSONObject("response");
                    return jSONObject == null ? new JSONArray() : jSONObject.getJSONArray("skipped");
                }
            } catch (Exception e) {
                exception = e;
            }
        }
        Log.e("PushWoosh DeviceFeature2_5", "ERROR: sent Tags " + exception.getMessage() + ". Response = " + obj, exception);
        throw exception;
    }
}
