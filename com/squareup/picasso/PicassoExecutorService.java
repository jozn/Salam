package com.squareup.picasso;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class PicassoExecutorService extends ThreadPoolExecutor {

    private static final class PicassoFutureTask extends FutureTask<BitmapHunter> implements Comparable<PicassoFutureTask> {
        private final BitmapHunter hunter;

        public final /* bridge */ /* synthetic */ int compareTo(Object obj) {
            PicassoFutureTask picassoFutureTask = (PicassoFutureTask) obj;
            int i = this.hunter.priority$159b5429;
            int i2 = picassoFutureTask.hunter.priority$159b5429;
            return i == i2 ? this.hunter.sequence - picassoFutureTask.hunter.sequence : (i2 - 1) - (i - 1);
        }

        public PicassoFutureTask(BitmapHunter hunter) {
            super(hunter, null);
            this.hunter = hunter;
        }
    }

    PicassoExecutorService() {
        super(3, 3, 0, TimeUnit.MILLISECONDS, new PriorityBlockingQueue(), new PicassoThreadFactory());
    }

    final void setThreadCount(int threadCount) {
        setCorePoolSize(threadCount);
        setMaximumPoolSize(threadCount);
    }

    public final Future<?> submit(Runnable task) {
        PicassoFutureTask ftask = new PicassoFutureTask((BitmapHunter) task);
        execute(ftask);
        return ftask;
    }
}
