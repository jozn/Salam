package com.shamchat.jobs;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.rokhgroup.mqtt.Connections;
import com.rokhgroup.utils.RokhPref;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.models.FriendGroup;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

public final class SubscribeToAllTopicsJob extends Job {
    private static final AtomicInteger jobCounter;
    String[] allTopics;
    private final int id;
    private boolean isError;

    /* renamed from: com.shamchat.jobs.SubscribeToAllTopicsJob.1 */
    class C10901 implements IMqttActionListener {
        final /* synthetic */ String val$clientHandle;
        final /* synthetic */ Context val$context;

        C10901(Context context, String str) {
            this.val$context = context;
            this.val$clientHandle = str;
        }

        public final void onSuccess(IMqttToken arg0) {
            Connections.getInstance(this.val$context).getConnection(this.val$clientHandle).addAction(this.val$context.getString(2131493422, (Object[]) SubscribeToAllTopicsJob.this.allTopics));
        }

        public final void onFailure(IMqttToken arg0, Throwable arg1) {
            Connections.getInstance(this.val$context).getConnection(this.val$clientHandle).addAction(this.val$context.getString(2131493421, (Object[]) SubscribeToAllTopicsJob.this.allTopics));
            SubscribeToAllTopicsJob.this.isError = true;
        }
    }

    static {
        jobCounter = new AtomicInteger(0);
    }

    public SubscribeToAllTopicsJob() {
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
            ContentResolver mContentResolver = SHAMChatApplication.getInstance().getContentResolver();
            String clientHandle = new RokhPref(SHAMChatApplication.getInstance().getApplicationContext()).getClientHandle();
            Context context = SHAMChatApplication.getInstance().getApplicationContext();
            Cursor cursor = mContentResolver.query(UserProvider.CONTENT_URI_GROUP, null, null, null, null);
            int groupCount = cursor.getCount();
            this.allTopics = new String[groupCount];
            this.isError = false;
            int[] qosArray = new int[groupCount];
            int i = 0;
            if (groupCount > 0) {
                while (cursor.moveToNext()) {
                    String hashcode = cursor.getString(cursor.getColumnIndex(FriendGroup.CHAT_ROOM_NAME));
                    String groupAlias = cursor.getString(cursor.getColumnIndex(FriendGroup.DB_NAME));
                    if (hashcode == null) {
                        hashcode = "nothing";
                    }
                    Log.e("Subscribe", "groups/" + hashcode + "  groupAlias:" + groupAlias);
                    this.allTopics[i] = "groups/" + hashcode;
                    qosArray[i] = 1;
                    i++;
                }
                cursor.close();
                Connections.getInstance(SHAMChatApplication.getInstance().getApplicationContext()).getConnection(clientHandle).client.subscribe(this.allTopics, qosArray, null, new C10901(context, clientHandle));
                if (this.isError) {
                    throw new IOException("Unexpected code");
                }
            }
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        Log.e("Subscribe", "to all topics failed: " + throwable.getCause());
        System.out.println("subscribe to events topic failed - retry");
        return true;
    }
}
