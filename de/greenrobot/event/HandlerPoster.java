package de.greenrobot.event;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

final class HandlerPoster extends Handler {
    private final EventBus eventBus;
    boolean handlerActive;
    private final int maxMillisInsideHandleMessage;
    final PendingPostQueue queue;

    HandlerPoster(EventBus eventBus, Looper looper) {
        super(looper);
        this.eventBus = eventBus;
        this.maxMillisInsideHandleMessage = 10;
        this.queue = new PendingPostQueue();
    }

    public final void handleMessage(Message msg) {
        try {
            long started = SystemClock.uptimeMillis();
            do {
                PendingPost pendingPost = this.queue.poll();
                if (pendingPost == null) {
                    synchronized (this) {
                        pendingPost = this.queue.poll();
                        if (pendingPost == null) {
                            this.handlerActive = false;
                            this.handlerActive = false;
                            return;
                        }
                    }
                }
                this.eventBus.invokeSubscriber(pendingPost);
            } while (SystemClock.uptimeMillis() - started < ((long) this.maxMillisInsideHandleMessage));
            if (sendMessage(obtainMessage())) {
                this.handlerActive = true;
                return;
            }
            throw new EventBusException("Could not send handler message");
        } catch (Throwable th) {
            this.handlerActive = false;
        }
    }
}
