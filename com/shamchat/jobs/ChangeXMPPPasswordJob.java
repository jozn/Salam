package com.shamchat.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;

public final class ChangeXMPPPasswordJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;
    private String password;
    private String userId;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public ChangeXMPPPasswordJob(String userId, String password) {
        Params params = new Params(1);
        params.persistent = true;
        params.requiresNetwork = true;
        super(params);
        this.id = jobCounter.incrementAndGet();
        this.userId = userId;
        this.password = password;
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        System.out.println("ChangeXMPPPasswordJob start");
        if (this.id == jobCounter.get()) {
            try {
                OkHttpClient okClient = new OkHttpClient();
                okClient.setConnectTimeout(240, TimeUnit.SECONDS);
                okClient.setReadTimeout(240, TimeUnit.SECONDS);
                String urlSyncMoments = "http://sync.rabtcdn.com/testserver/passM.php?userId=" + this.userId + "&userPass=" + this.password;
                Response response = okClient.newCall(new Builder().url(urlSyncMoments).build()).execute();
                String result = response.body().string();
                response.body().close();
                System.out.println("Change password " + urlSyncMoments);
                System.out.println("Change password result " + result);
                if (result != null) {
                    System.out.println("change password " + new JSONObject(result));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("AAA download should re run on throwable");
        return true;
    }
}
