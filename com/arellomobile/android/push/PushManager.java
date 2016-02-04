package com.arellomobile.android.push;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.arellomobile.android.push.exception.PushWooshException;
import com.arellomobile.android.push.registrar.PushRegistrar;
import com.arellomobile.android.push.registrar.PushRegistrarADM;
import com.arellomobile.android.push.registrar.PushRegistrarGCM;
import com.arellomobile.android.push.request.RequestHelper;
import com.arellomobile.android.push.utils.GeneralUtils;
import com.arellomobile.android.push.utils.NetworkUtils;
import com.arellomobile.android.push.utils.NetworkUtils.NetworkResult;
import com.arellomobile.android.push.utils.PreferenceUtils;
import com.arellomobile.android.push.utils.WorkerTask;
import com.arellomobile.android.push.utils.executor.ExecutorHelper;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class PushManager {
    private static AsyncTask<Void, Void, Void> mRegistrationAsyncTask;
    private static final Object mSyncObj;
    public boolean forceRegister;
    private Context mContext;
    private Bundle mLastBundle;
    public PushRegistrar pushRegistrar;

    /* renamed from: com.arellomobile.android.push.PushManager.1 */
    static class C02261 implements Runnable {
        final /* synthetic */ SendPushTagsCallBack val$callBack;
        final /* synthetic */ Context val$context;
        final /* synthetic */ Map val$tags;

        C02261(Context context, SendPushTagsCallBack sendPushTagsCallBack, Map map) {
            this.val$context = context;
            this.val$callBack = sendPushTagsCallBack;
            this.val$tags = map;
        }

        public final void run() {
            new SendPushTagsAsyncTask(this.val$context, this.val$callBack).execute(new Map[]{this.val$tags});
        }
    }

    /* renamed from: com.arellomobile.android.push.PushManager.4 */
    class C02274 implements Runnable {
        final /* synthetic */ Context val$context;
        final /* synthetic */ String val$regId;

        C02274(Context context, String str) {
            this.val$context = context;
            this.val$regId = str;
        }

        public final void run() {
            PushManager.mRegistrationAsyncTask = new C02328(this.val$context, this.val$regId);
            ExecutorHelper.executeAsyncTask(PushManager.mRegistrationAsyncTask);
        }
    }

    /* renamed from: com.arellomobile.android.push.PushManager.5 */
    class C02295 implements Runnable {
        final /* synthetic */ Context val$context;
        final /* synthetic */ String val$hash;

        /* renamed from: com.arellomobile.android.push.PushManager.5.1 */
        class C02281 extends WorkerTask {
            C02281(Context context) {
                super(context);
            }

            protected final void doWork(Context context) {
                String str = C02295.this.val$hash;
                if (str != null) {
                    Map hashMap = new HashMap();
                    hashMap.putAll(RequestHelper.getSendPushStatData(context, str));
                    Log.w("PushWoosh DeviceFeature2_5", "Try To sent PushStat");
                    NetworkResult networkResult = new NetworkResult();
                    Throwable exception = new Exception();
                    NetworkResult networkResult2 = networkResult;
                    int i = 0;
                    while (i < 5) {
                        try {
                            networkResult2 = NetworkUtils.makeRequest(hashMap, "pushStat");
                            if (200 == networkResult2.mResultCode) {
                                Log.w("PushWoosh DeviceFeature2_5", "Send PushStat success");
                                return;
                            }
                            i++;
                        } catch (Exception e) {
                            exception = e;
                        }
                    }
                    Log.e("PushWoosh DeviceFeature2_5", "ERROR: Try To sent PushStat " + exception.getMessage() + ". Response = " + networkResult2.mResultData, exception);
                }
            }
        }

        C02295(Context context, String str) {
            this.val$context = context;
            this.val$hash = str;
        }

        public final void run() {
            ExecutorHelper.executeAsyncTask(new C02281(this.val$context));
        }
    }

    /* renamed from: com.arellomobile.android.push.PushManager.6 */
    class C02316 implements Runnable {
        final /* synthetic */ Context val$context;

        /* renamed from: com.arellomobile.android.push.PushManager.6.1 */
        class C02301 extends WorkerTask {
            C02301(Context context) {
                super(context);
            }

            protected final void doWork(Context context) {
                Map hashMap = new HashMap();
                Map hashMap2 = new HashMap();
                hashMap2.put("application", PreferenceUtils.getApplicationId(context));
                hashMap2.put("hwid", GeneralUtils.getDeviceUUID(context));
                hashMap.putAll(hashMap2);
                Log.w("PushWoosh DeviceFeature2_5", "Try To sent AppOpen");
                NetworkResult networkResult = new NetworkResult();
                Throwable exception = new Exception();
                NetworkResult networkResult2 = networkResult;
                int i = 0;
                while (i < 5) {
                    try {
                        networkResult2 = NetworkUtils.makeRequest(hashMap, "applicationOpen");
                        if (200 != networkResult2.mResultCode) {
                            continue;
                            i++;
                        } else if (200 != networkResult2.mPushwooshCode) {
                            break;
                        } else {
                            Log.w("PushWoosh DeviceFeature2_5", "Send AppOpen success");
                            return;
                        }
                    } catch (Exception e) {
                        exception = e;
                    }
                }
                Log.e("PushWoosh DeviceFeature2_5", "ERROR: Try To sent AppOpen " + exception.getMessage() + ". Response = " + networkResult2.mResultData, exception);
            }
        }

        public C02316(Context context) {
            this.val$context = context;
        }

        public final void run() {
            ExecutorHelper.executeAsyncTask(new C02301(this.val$context));
        }
    }

    /* renamed from: com.arellomobile.android.push.PushManager.8 */
    class C02328 extends WorkerTask {
        final /* synthetic */ String val$regId;

        C02328(Context context, String str) {
            this.val$regId = str;
            super(context);
        }

        protected final void doWork(Context context) {
            DeviceRegistrar.registerWithServer(PushManager.this.mContext, this.val$regId);
        }
    }

    static {
        mSyncObj = new Object();
    }

    PushManager(Context context) {
        this.forceRegister = false;
        GeneralUtils.checkNotNull(context, "context");
        this.mContext = context;
        if (GeneralUtils.isAmazonDevice()) {
            this.pushRegistrar = new PushRegistrarADM(context);
        } else {
            this.pushRegistrar = new PushRegistrarGCM();
        }
    }

    public PushManager(Context context, String str, String str2) {
        this(context);
        if (!PreferenceUtils.getApplicationId(context).equals(str)) {
            this.forceRegister = true;
        }
        Editor edit = context.getSharedPreferences("com.pushwoosh.pushnotifications", 0).edit();
        edit.putString("dm_pwapp", str);
        edit.commit();
        edit = context.getSharedPreferences("com.pushwoosh.pushnotifications", 0).edit();
        edit.putString("dm_sender_id", str2);
        edit.commit();
    }

    public static Map<String, Object> incrementalTag(Integer num) {
        Map<String, Object> hashMap = new HashMap();
        hashMap.put("operation", "increment");
        hashMap.put("value", num);
        return hashMap;
    }

    public static void sendTags(Context context, Map<String, Object> map, SendPushTagsCallBack sendPushTagsCallBack) {
        new Handler(context.getMainLooper()).post(new C02261(context, sendPushTagsCallBack, map));
    }

    public static Map<String, String> sendTagsFromBG(Context context, Map<String, Object> map) throws PushWooshException {
        Map<String, String> hashMap = new HashMap();
        try {
            JSONArray sendTags = DeviceFeature2_5.sendTags(context, map);
            for (int i = 0; i < sendTags.length(); i++) {
                JSONObject jSONObject = sendTags.getJSONObject(i);
                hashMap.put(jSONObject.getString("tag"), jSONObject.getString("reason"));
            }
            return hashMap;
        } catch (Exception e) {
            throw new PushWooshException(e);
        }
    }

    final boolean onHandlePush(Activity activity) {
        Bundle bundleExtra = activity.getIntent().getBundleExtra("pushBundle");
        if (bundleExtra == null || this.mContext == null) {
            return false;
        }
        String format;
        this.mLastBundle = bundleExtra;
        JSONObject jSONObject = new JSONObject();
        for (String format2 : bundleExtra.keySet()) {
            if (format2.equals("u")) {
                try {
                    jSONObject.put("userdata", bundleExtra.get("u"));
                } catch (JSONException e) {
                }
            }
            try {
                jSONObject.put(format2, bundleExtra.get(format2));
            } catch (JSONException e2) {
            }
        }
        PushEventsTransmitter.onMessageReceive(this.mContext, jSONObject.toString(), bundleExtra);
        if (((String) bundleExtra.get("h")) != null) {
            format2 = String.format("https://cp.pushwoosh.com/content/%s", new Object[]{(String) bundleExtra.get("h")});
            Intent intent = new Intent(activity, PushWebview.class);
            intent.putExtra("url", format2);
            activity.startActivity(intent);
        }
        format2 = (String) bundleExtra.get("r");
        if (format2 != null) {
            intent = new Intent(activity, PushWebview.class);
            intent.putExtra("url", format2);
            activity.startActivity(intent);
        }
        bundleExtra.get("l");
        sendPushStat(this.mContext, bundleExtra.getString("p"));
        return true;
    }

    public final void registerOnPushWoosh(Context context, String str) {
        synchronized (mSyncObj) {
            if (mRegistrationAsyncTask != null) {
                mRegistrationAsyncTask.cancel(true);
            }
            mRegistrationAsyncTask = null;
        }
        new Handler(context.getMainLooper()).post(new C02274(context, str));
    }

    final void sendPushStat(Context context, String str) {
        new Handler(context.getMainLooper()).post(new C02295(context, str));
    }
}
