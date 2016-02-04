package de.greenrobot.event;

final class AsyncPoster implements Runnable {
    final EventBus eventBus;
    final PendingPostQueue queue;

    AsyncPoster(EventBus eventBus) {
        this.eventBus = eventBus;
        this.queue = new PendingPostQueue();
    }

    public final void run() {
        PendingPost pendingPost = this.queue.poll();
        if (pendingPost == null) {
            throw new IllegalStateException("No pending post available");
        }
        this.eventBus.invokeSubscriber(pendingPost);
    }
}
