package com.shamchat.jobs;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.appcompat.BuildConfig;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.events.FeedbackSendCompleteEvent;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import de.greenrobot.event.EventBus;
import java.io.IOException;

public final class SendFeedbackJob extends Job {
    private String feedbackText;
    private SharedPreferences preferences;

    public SendFeedbackJob(String feedbackText) {
        Params params = new Params(1);
        params.persistent = true;
        params.requiresNetwork = true;
        params.groupId = "send-feedback";
        super(params);
        this.feedbackText = feedbackText;
        System.out.println("SendFeedbackJob constructor ");
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(SHAMChatApplication.getMyApplicationContext());
        String mobileNumber = this.preferences.getString("user_mobileNo", BuildConfig.VERSION_NAME);
        Response response = new OkHttpClient().newCall(new Builder().url("http://static.rabtcdn.com/feedback.php").post(new FormEncodingBuilder().add("feedback_text", this.feedbackText.toString()).add("phone_number", mobileNumber).build()).build()).execute();
        if (response.isSuccessful()) {
            String result = response.body().string();
            response.body().close();
            System.out.println("SendFeedbackJob http got: " + result);
            EventBus.getDefault().post(new FeedbackSendCompleteEvent());
            return;
        }
        throw new IOException("SendFeedbackJob Unexpected code " + response);
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("SendFeedbackJob retry on throwable");
        return true;
    }
}
