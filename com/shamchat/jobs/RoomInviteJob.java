package com.shamchat.jobs;

import android.util.Log;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONObject;

public final class RoomInviteJob extends Job {
    private static final AtomicInteger jobCounter;
    private String groupId;
    private final int id;
    private String inviteJsonString;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public RoomInviteJob(String inviteJsonString, String groupId) {
        Params params = new Params(1000);
        params.persistent = true;
        params.requiresNetwork = true;
        super(params);
        this.id = jobCounter.incrementAndGet();
        this.inviteJsonString = inviteJsonString;
        this.groupId = groupId;
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        String URL = "http://social.rabtcdn.com/groups/api/v1/topics/invite/" + this.groupId + MqttTopic.TOPIC_LEVEL_SEPARATOR;
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(60, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        RequestBody formBody = RequestBody.create(JSON, this.inviteJsonString);
        Request request = new Builder().url(URL).post(formBody).build();
        Log.e("RoomInvite Request", formBody.toString());
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String stringResponse = response.body().string();
            response.body().close();
            System.out.println(stringResponse);
            String status = new JSONObject(stringResponse).getString(NotificationCompatApi21.CATEGORY_STATUS);
            if (status.equals("200")) {
                Log.e("Invite Response", stringResponse);
                return;
            }
            throw new IOException("Unexpected reponse code " + status);
        }
        throw new IOException("Unexpected code " + response);
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("Room kick Job run again");
        return true;
    }
}
