package com.nostra13.universalimageloader.core;

import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

final class ImageLoaderEngine {
    final Map<Integer, String> cacheKeysForImageAwares;
    final ImageLoaderConfiguration configuration;
    final AtomicBoolean networkDenied;
    final Object pauseLock;
    final AtomicBoolean paused;
    final AtomicBoolean slowNetwork;
    ExecutorService taskDistributor;
    Executor taskExecutor;
    Executor taskExecutorForCachedImages;
    private final Map<String, ReentrantLock> uriLocks;

    /* renamed from: com.nostra13.universalimageloader.core.ImageLoaderEngine.1 */
    class C04911 implements Runnable {
        final /* synthetic */ LoadAndDisplayImageTask val$task;

        C04911(LoadAndDisplayImageTask loadAndDisplayImageTask) {
            this.val$task = loadAndDisplayImageTask;
        }

        public final void run() {
            boolean isImageCachedOnDisc = ImageLoaderEngine.this.configuration.discCache.get$3b83896e().exists();
            ImageLoaderEngine.this.initExecutorsIfNeed();
            if (isImageCachedOnDisc) {
                ImageLoaderEngine.this.taskExecutorForCachedImages.execute(this.val$task);
            } else {
                ImageLoaderEngine.this.taskExecutor.execute(this.val$task);
            }
        }
    }

    final void initExecutorsIfNeed() {
        if (!this.configuration.customExecutor && ((ExecutorService) this.taskExecutor).isShutdown()) {
            this.taskExecutor = createTaskExecutor();
        }
        if (!this.configuration.customExecutorForCachedImages && ((ExecutorService) this.taskExecutorForCachedImages).isShutdown()) {
            this.taskExecutorForCachedImages = createTaskExecutor();
        }
    }

    private Executor createTaskExecutor() {
        int i = this.configuration.threadPoolSize;
        return new ThreadPoolExecutor(i, i, 0, TimeUnit.MILLISECONDS, (this.configuration.tasksProcessingType$2bbc75bd == QueueProcessingType.LIFO$2bbc75bd ? 1 : null) != null ? new LIFOLinkedBlockingDeque() : new LinkedBlockingQueue(), new DefaultThreadFactory(this.configuration.threadPriority));
    }

    final String getLoadingUriForView(ImageAware imageAware) {
        return (String) this.cacheKeysForImageAwares.get(Integer.valueOf(imageAware.getId()));
    }

    final void cancelDisplayTaskFor(ImageAware imageAware) {
        this.cacheKeysForImageAwares.remove(Integer.valueOf(imageAware.getId()));
    }

    final ReentrantLock getLockForUri(String uri) {
        ReentrantLock lock = (ReentrantLock) this.uriLocks.get(uri);
        if (lock != null) {
            return lock;
        }
        lock = new ReentrantLock();
        this.uriLocks.put(uri, lock);
        return lock;
    }
}
