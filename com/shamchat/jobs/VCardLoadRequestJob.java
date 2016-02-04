package com.shamchat.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.service.SmackableImp;
import java.util.concurrent.atomic.AtomicInteger;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

public final class VCardLoadRequestJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;
    private String jabberId;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public VCardLoadRequestJob(String jabberId) {
        Params params = new Params(1);
        params.persistent = true;
        params.requiresNetwork = true;
        super(params);
        this.id = jobCounter.incrementAndGet();
        this.jabberId = jabberId;
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        System.out.println("VCardLoad Request start");
        if (this.id == jobCounter.get() && SmackableImp.getXmppConnection().authenticated) {
            try {
                new VCard().load(SmackableImp.getXmppConnection(), this.jabberId);
            } catch (Exception e) {
            }
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("Join To Chat room shouldReRunOnThrowable true");
        return true;
    }
}
