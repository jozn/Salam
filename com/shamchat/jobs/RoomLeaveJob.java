package com.shamchat.jobs;

import android.util.Log;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.activity.ChatActivity;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.models.User;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONObject;

public final class RoomLeaveJob extends Job {
    private static final AtomicInteger jobCounter;
    private String groupId;
    private final int id;
    private String threadId;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public RoomLeaveJob(String threadId, String groupId) {
        Params params = new Params(1000);
        params.persistent = true;
        params.requiresNetwork = true;
        super(params);
        this.id = jobCounter.incrementAndGet();
        this.threadId = threadId;
        this.groupId = groupId;
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        UserProvider userProvider = new UserProvider();
        User userObject = UserProvider.getCurrentUserForMyProfile();
        String URL = "http://social.rabtcdn.com/groups/api/v1/topics/left/" + this.groupId + MqttTopic.TOPIC_LEVEL_SEPARATOR;
        Log.e("TOPIC SHOULD LEAVE", URL);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(60, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        Response response = client.newCall(new Builder().url(URL).post(new FormEncodingBuilder().add(ChatActivity.INTENT_EXTRA_USER_ID, userObject.userId).build()).build()).execute();
        if (response.isSuccessful()) {
            String stringResponse = response.body().string();
            response.body().close();
            System.out.println(stringResponse);
            String status = new JSONObject(stringResponse).getString(NotificationCompatApi21.CATEGORY_STATUS);
            if (status.equals("200")) {
                Log.e("ROOM LEFT RESULT", "WAS OK!!");
                return;
            }
            throw new IOException("Unexpected reponse code " + status);
        }
        throw new IOException("Unexpected code " + response);
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("Room Restore Job run again");
        return true;
    }
}
