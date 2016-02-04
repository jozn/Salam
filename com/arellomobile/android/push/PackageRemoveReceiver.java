package com.arellomobile.android.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import com.arellomobile.android.push.utils.GeneralUtils;
import com.arellomobile.android.push.utils.NetworkUtils;
import com.arellomobile.android.push.utils.NetworkUtils.NetworkResult;
import com.arellomobile.android.push.utils.PreferenceUtils;
import com.arellomobile.android.push.utils.WorkerTask;
import com.arellomobile.android.push.utils.executor.ExecutorHelper;
import java.util.HashMap;
import java.util.Map;

public class PackageRemoveReceiver extends BroadcastReceiver {

    /* renamed from: com.arellomobile.android.push.PackageRemoveReceiver.1 */
    class C02251 implements Runnable {
        final /* synthetic */ Context val$context;
        final /* synthetic */ String val$packageName;

        /* renamed from: com.arellomobile.android.push.PackageRemoveReceiver.1.1 */
        class C02241 extends WorkerTask {
            C02241(Context context) {
                super(context);
            }

            protected final void doWork(Context context) {
                try {
                    String str = C02251.this.val$packageName;
                    Map hashMap = new HashMap();
                    Map hashMap2 = new HashMap();
                    hashMap2.put("application", PreferenceUtils.getApplicationId(context));
                    hashMap2.put("android_package", str);
                    hashMap2.put("hwid", GeneralUtils.getDeviceUUID(context));
                    hashMap.putAll(hashMap2);
                    Log.w("PushWoosh DeviceFeature2_5", "Try To sent AppRemoved");
                    Throwable exception = new Exception();
                    NetworkResult networkResult = new NetworkResult();
                    int i = 0;
                    while (i < 5) {
                        try {
                            networkResult = NetworkUtils.makeRequest(hashMap, "androidPackageRemoved");
                            if (200 != networkResult.mResultCode) {
                                continue;
                                i++;
                            } else if (200 == networkResult.mPushwooshCode) {
                                Log.w("PushWoosh DeviceFeature2_5", "Send AppRemoved success");
                                return;
                            }
                        } catch (Exception e) {
                            exception = e;
                        }
                    }
                    break;
                    Log.e("PushWoosh DeviceFeature2_5", "ERROR: Try To sent AppRemoved " + exception.getMessage() + ". Response = " + networkResult.mResultData, exception);
                } catch (Exception e2) {
                }
            }
        }

        C02251(Context context, String str) {
            this.val$context = context;
            this.val$packageName = str;
        }

        public final void run() {
            ExecutorHelper.executeAsyncTask(new C02241(this.val$context));
        }
    }

    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.PACKAGE_REMOVED".equals(intent.getAction())) {
            Uri data = intent.getData();
            String schemeSpecificPart = data != null ? data.getSchemeSpecificPart() : null;
            if (schemeSpecificPart != null) {
                new Handler(context.getMainLooper()).post(new C02251(context, schemeSpecificPart));
            }
        }
    }
}
