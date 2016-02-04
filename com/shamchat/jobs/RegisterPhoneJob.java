package com.shamchat.jobs;

import android.util.Log;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.events.RegisterServerResponseFailed;
import com.shamchat.events.RegisterServerResponseSuccess;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import de.greenrobot.event.EventBus;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;

public final class RegisterPhoneJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;
    private String phoneNumber;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public RegisterPhoneJob(String phoneNumber) {
        Params params = new Params(1000);
        params.persistent = true;
        params.requiresNetwork = true;
        super(params);
        this.id = jobCounter.incrementAndGet();
        this.phoneNumber = phoneNumber;
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
        EventBus.getDefault().postSticky(new RegisterServerResponseFailed());
    }

    protected final int getRetryLimit() {
        return 3;
    }

    public final void onRun() throws Throwable {
        String encodedMobileNo = null;
        try {
            encodedMobileNo = URLEncoder.encode(this.phoneNumber, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String URL = SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493167) + "registerUser.htm?mobileNo=" + encodedMobileNo;
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(240, TimeUnit.SECONDS);
        client.setReadTimeout(240, TimeUnit.SECONDS);
        Response response = client.newCall(new Builder().url(URL).build()).execute();
        if (response.isSuccessful()) {
            String stringResponse = response.body().string();
            response.body().close();
            JSONObject serverResponseJsonObject = new JSONObject(stringResponse);
            String status = serverResponseJsonObject.getString(NotificationCompatApi21.CATEGORY_STATUS);
            if (status.equals(MqttServiceConstants.TRACE_EXCEPTION)) {
                Log.e("RegisterPhoneJob", "Status exception: " + stringResponse);
                throw new IOException("Status exception: " + stringResponse);
            } else if (stringResponse != null) {
                EventBus.getDefault().postSticky(new RegisterServerResponseSuccess(this.phoneNumber, serverResponseJsonObject, status));
                return;
            } else {
                EventBus.getDefault().postSticky(new RegisterServerResponseFailed());
                return;
            }
        }
        throw new IOException("Unexpected code " + response);
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("Register phone run again - true");
        return true;
    }
}
