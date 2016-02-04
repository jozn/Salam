package com.shamchat.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.events.MessageStateChangedEvent;
import com.shamchat.models.ChatMessage.MessageStatusType;
import de.greenrobot.event.EventBus;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class MessageStateChangedJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;
    private int newStatus;
    private String packetID;
    private String threadId;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public MessageStateChangedJob(String threadId, String packetID, int newStatus) {
        Params params = new Params(1000);
        params.persistent = true;
        params.groupId = "LoadDB_then_MessageStatusChange";
        super(params);
        this.id = jobCounter.incrementAndGet();
        this.threadId = threadId;
        this.packetID = packetID;
        this.newStatus = newStatus;
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        MessageStateChangedEvent messageStateChangedEvent = new MessageStateChangedEvent();
        messageStateChangedEvent.threadId = this.threadId;
        messageStateChangedEvent.packetId = this.packetID;
        MessageStatusType messageStatusType = null;
        switch (this.newStatus) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                messageStatusType = MessageStatusType.QUEUED;
                break;
            case Logger.SEVERE /*1*/:
                messageStatusType = MessageStatusType.SENDING;
                break;
            case Logger.WARNING /*2*/:
                messageStatusType = MessageStatusType.SENT;
                break;
            case Logger.INFO /*3*/:
                messageStatusType = MessageStatusType.DELIVERED;
                break;
            case Logger.CONFIG /*4*/:
                messageStatusType = MessageStatusType.SEEN;
                break;
            case Logger.FINE /*5*/:
                messageStatusType = MessageStatusType.FAILED;
                break;
        }
        messageStateChangedEvent.messageStatusType = messageStatusType;
        EventBus.getDefault().postSticky(messageStateChangedEvent);
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("AAA download should re run on throwable");
        return false;
    }
}
