package de.greenrobot.event;

final class BackgroundPoster implements Runnable {
    final EventBus eventBus;
    volatile boolean executorRunning;
    final PendingPostQueue queue;

    BackgroundPoster(EventBus eventBus) {
        this.eventBus = eventBus;
        this.queue = new PendingPostQueue();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r6 = this;
        r5 = 0;
    L_0x0001:
        r2 = r6.queue;	 Catch:{ InterruptedException -> 0x0020 }
        r1 = r2.poll$2bd60b69();	 Catch:{ InterruptedException -> 0x0020 }
        if (r1 != 0) goto L_0x001a;
    L_0x0009:
        monitor-enter(r6);	 Catch:{ InterruptedException -> 0x0020 }
        r2 = r6.queue;	 Catch:{ all -> 0x0044 }
        r1 = r2.poll();	 Catch:{ all -> 0x0044 }
        if (r1 != 0) goto L_0x0019;
    L_0x0012:
        r2 = 0;
        r6.executorRunning = r2;	 Catch:{ all -> 0x0044 }
        monitor-exit(r6);	 Catch:{ all -> 0x0044 }
        r6.executorRunning = r5;
    L_0x0018:
        return;
    L_0x0019:
        monitor-exit(r6);	 Catch:{ all -> 0x0044 }
    L_0x001a:
        r2 = r6.eventBus;	 Catch:{ InterruptedException -> 0x0020 }
        r2.invokeSubscriber(r1);	 Catch:{ InterruptedException -> 0x0020 }
        goto L_0x0001;
    L_0x0020:
        r0 = move-exception;
        r2 = "Event";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0047 }
        r3.<init>();	 Catch:{ all -> 0x0047 }
        r4 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0047 }
        r4 = r4.getName();	 Catch:{ all -> 0x0047 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x0047 }
        r4 = " was interruppted";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0047 }
        r3 = r3.toString();	 Catch:{ all -> 0x0047 }
        android.util.Log.w(r2, r3, r0);	 Catch:{ all -> 0x0047 }
        r6.executorRunning = r5;
        goto L_0x0018;
    L_0x0044:
        r2 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0044 }
        throw r2;	 Catch:{ InterruptedException -> 0x0020 }
    L_0x0047:
        r2 = move-exception;
        r6.executorRunning = r5;
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: de.greenrobot.event.BackgroundPoster.run():void");
    }
}
