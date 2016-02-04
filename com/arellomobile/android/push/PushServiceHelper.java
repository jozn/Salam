package com.arellomobile.android.push;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.arellomobile.android.push.utils.GeneralUtils;
import org.json.JSONException;
import org.json.JSONObject;

public final class PushServiceHelper {
    public static void generateBroadcast(Context context, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(context.getPackageName() + ".action.PUSH_MESSAGE_RECEIVE");
        intent.putExtras(bundle);
        JSONObject jSONObject = new JSONObject();
        for (String str : bundle.keySet()) {
            if (str.equals("u")) {
                try {
                    jSONObject.put("userdata", bundle.get("u"));
                } catch (JSONException e) {
                }
            }
            try {
                jSONObject.put(str, bundle.get(str));
            } catch (JSONException e2) {
            }
        }
        intent.putExtra("pw_data_json_string", jSONObject.toString());
        if (GeneralUtils.isAmazonDevice()) {
            context.sendBroadcast(intent, context.getPackageName() + ".permission.RECEIVE_ADM_MESSAGE");
        } else {
            context.sendBroadcast(intent, context.getPackageName() + ".permission.C2D_MESSAGE");
        }
    }
}
