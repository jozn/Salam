package com.shamchat.jobs;

import android.content.Context;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.rokhgroup.mqtt.Connections;
import com.rokhgroup.utils.RokhPref;
import com.shamchat.androidclient.SHAMChatApplication;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

public final class SubscribeToEventsJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;
    boolean isError;
    String[] topics;

    /* renamed from: com.shamchat.jobs.SubscribeToEventsJob.1 */
    class C10911 implements IMqttActionListener {
        final /* synthetic */ String val$clientHandle;
        final /* synthetic */ Context val$context;

        C10911(Context context, String str) {
            this.val$context = context;
            this.val$clientHandle = str;
        }

        public final void onSuccess(IMqttToken arg0) {
            Connections.getInstance(this.val$context).getConnection(this.val$clientHandle).addAction(this.val$context.getString(2131493422, (Object[]) SubscribeToEventsJob.this.topics));
        }

        public final void onFailure(IMqttToken arg0, Throwable arg1) {
            Connections.getInstance(this.val$context).getConnection(this.val$clientHandle).addAction(this.val$context.getString(2131493421, (Object[]) SubscribeToEventsJob.this.topics));
            SubscribeToEventsJob.this.isError = true;
        }
    }

    static {
        jobCounter = new AtomicInteger(0);
    }

    public SubscribeToEventsJob() {
        Params params = new Params(1000);
        params.persistent = true;
        params.requiresNetwork = true;
        super(params);
        this.isError = false;
        this.id = jobCounter.incrementAndGet();
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        if (this.id == jobCounter.get()) {
            String clientHandle = new RokhPref(SHAMChatApplication.getInstance().getApplicationContext()).getClientHandle();
            String topic = "events/" + SHAMChatApplication.getConfig().userId;
            Context context = SHAMChatApplication.getInstance().getApplicationContext();
            this.topics = new String[1];
            this.topics[0] = topic;
            Connections.getInstance(SHAMChatApplication.getInstance().getApplicationContext()).getConnection(clientHandle).client.subscribe(topic, 1, null, new C10911(context, clientHandle));
            if (this.isError) {
                throw new IOException("Unexpected code");
            }
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("********subscribe to events topic failed - retry******8");
        return true;
    }
}
