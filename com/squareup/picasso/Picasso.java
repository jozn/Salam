package com.squareup.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.internal.view.SupportMenu;
import android.widget.ImageView;
import com.kyleduo.switchbutton.C0473R;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class Picasso {
    public static final Handler HANDLER;
    public static volatile Picasso singleton;
    public final Cache cache;
    public final CleanupThread cleanupThread;
    final Context context;
    final Config defaultBitmapConfig;
    public final Dispatcher dispatcher;
    boolean indicatorsEnabled;
    private final Listener listener;
    volatile boolean loggingEnabled;
    final ReferenceQueue<Object> referenceQueue;
    final List<RequestHandler> requestHandlers;
    final RequestTransformer requestTransformer;
    public boolean shutdown;
    public final Stats stats;
    final Map<Object, Action> targetToAction;
    public final Map<ImageView, DeferredRequestCreator> targetToDeferredRequestCreator;

    /* renamed from: com.squareup.picasso.Picasso.1 */
    static class C12341 extends Handler {
        C12341(Looper x0) {
            super(x0);
        }

        public final void handleMessage(Message msg) {
            Action action;
            int n;
            int i;
            switch (msg.what) {
                case Logger.INFO /*3*/:
                    action = msg.obj;
                    if (action.picasso.loggingEnabled) {
                        Utils.log("Main", "canceled", action.request.logId(), "target got garbage collected");
                    }
                    action.picasso.cancelExistingRequest(action.getTarget());
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    List<BitmapHunter> batch = msg.obj;
                    n = batch.size();
                    for (i = 0; i < n; i++) {
                        BitmapHunter hunter = (BitmapHunter) batch.get(i);
                        Picasso picasso = hunter.picasso;
                        Action action2 = hunter.action;
                        List list = hunter.actions;
                        Object obj = (list == null || list.isEmpty()) ? null : 1;
                        Object obj2 = (action2 == null && obj == null) ? null : 1;
                        if (obj2 != null) {
                            Uri uri = hunter.data.uri;
                            Exception exception = hunter.exception;
                            Bitmap bitmap = hunter.result;
                            LoadedFrom loadedFrom = hunter.loadedFrom;
                            if (action2 != null) {
                                picasso.deliverAction(bitmap, loadedFrom, action2);
                            }
                            if (obj != null) {
                                int size = list.size();
                                for (int i2 = 0; i2 < size; i2++) {
                                    picasso.deliverAction(bitmap, loadedFrom, (Action) list.get(i2));
                                }
                            }
                        }
                    }
                case C0473R.styleable.SwitchButton_thumbPressedColor /*13*/:
                    List<Action> batch2 = msg.obj;
                    n = batch2.size();
                    for (i = 0; i < n; i++) {
                        action = (Action) batch2.get(i);
                        Picasso picasso2 = action.picasso;
                        Bitmap bitmap2 = null;
                        if (MemoryPolicy.shouldReadFromMemoryCache(action.memoryPolicy)) {
                            bitmap2 = picasso2.quickMemoryCacheCheck(action.key);
                        }
                        if (bitmap2 != null) {
                            picasso2.deliverAction(bitmap2, LoadedFrom.MEMORY, action);
                            if (picasso2.loggingEnabled) {
                                Utils.log("Main", "completed", action.request.logId(), "from " + LoadedFrom.MEMORY);
                            }
                        } else {
                            picasso2.enqueueAndSubmit(action);
                            if (picasso2.loggingEnabled) {
                                Utils.log("Main", "resumed", action.request.logId());
                            }
                        }
                    }
                default:
                    throw new AssertionError("Unknown handler message received: " + msg.what);
            }
        }
    }

    public static class Builder {
        private Cache cache;
        private final Context context;
        private Config defaultBitmapConfig;
        private Downloader downloader;
        private boolean indicatorsEnabled;
        private Listener listener;
        private boolean loggingEnabled;
        private List<RequestHandler> requestHandlers;
        private ExecutorService service;
        private RequestTransformer transformer;

        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }

        public final Builder addRequestHandler(RequestHandler requestHandler) {
            if (this.requestHandlers == null) {
                this.requestHandlers = new ArrayList();
            }
            if (this.requestHandlers.contains(requestHandler)) {
                throw new IllegalStateException("RequestHandler already registered.");
            }
            this.requestHandlers.add(requestHandler);
            return this;
        }

        public final Picasso build() {
            Context context = this.context;
            if (this.downloader == null) {
                this.downloader = Utils.createDefaultDownloader(context);
            }
            if (this.cache == null) {
                this.cache = new LruCache(context);
            }
            if (this.service == null) {
                this.service = new PicassoExecutorService();
            }
            if (this.transformer == null) {
                this.transformer = RequestTransformer.IDENTITY;
            }
            Stats stats = new Stats(this.cache);
            Dispatcher dispatcher = new Dispatcher(context, this.service, Picasso.HANDLER, this.downloader, this.cache, stats);
            return new Picasso(context, dispatcher, this.cache, this.listener, this.transformer, this.requestHandlers, stats, this.defaultBitmapConfig, this.indicatorsEnabled, this.loggingEnabled);
        }
    }

    private static class CleanupThread extends Thread {
        private final Handler handler;
        private final ReferenceQueue<Object> referenceQueue;

        /* renamed from: com.squareup.picasso.Picasso.CleanupThread.1 */
        class C12351 implements Runnable {
            final /* synthetic */ Exception val$e;

            C12351(Exception exception) {
                this.val$e = exception;
            }

            public final void run() {
                throw new RuntimeException(this.val$e);
            }
        }

        CleanupThread(ReferenceQueue<Object> referenceQueue, Handler handler) {
            this.referenceQueue = referenceQueue;
            this.handler = handler;
            setDaemon(true);
            setName("Picasso-refQueue");
        }

        public final void run() {
            Process.setThreadPriority(10);
            while (true) {
                try {
                    RequestWeakReference<?> remove = (RequestWeakReference) this.referenceQueue.remove(1000);
                    Message message = this.handler.obtainMessage();
                    if (remove != null) {
                        message.what = 3;
                        message.obj = remove.action;
                        this.handler.sendMessage(message);
                    } else {
                        message.recycle();
                    }
                } catch (InterruptedException e) {
                    return;
                } catch (Exception e2) {
                    this.handler.post(new C12351(e2));
                    return;
                }
            }
        }
    }

    public interface Listener {
    }

    public enum LoadedFrom {
        MEMORY(-16711936),
        DISK(-16776961),
        NETWORK(SupportMenu.CATEGORY_MASK);
        
        final int debugColor;

        private LoadedFrom(int debugColor) {
            this.debugColor = debugColor;
        }
    }

    public enum Priority {
        ;

        static {
            LOW$159b5429 = 1;
            NORMAL$159b5429 = 2;
            HIGH$159b5429 = 3;
            $VALUES$1df6b4e4 = new int[]{LOW$159b5429, NORMAL$159b5429, HIGH$159b5429};
        }
    }

    public interface RequestTransformer {
        public static final RequestTransformer IDENTITY;

        /* renamed from: com.squareup.picasso.Picasso.RequestTransformer.1 */
        static class C12361 implements RequestTransformer {
            C12361() {
            }

            public final Request transformRequest(Request request) {
                return request;
            }
        }

        Request transformRequest(Request request);

        static {
            IDENTITY = new C12361();
        }
    }

    static {
        HANDLER = new C12341(Looper.getMainLooper());
        singleton = null;
    }

    Picasso(Context context, Dispatcher dispatcher, Cache cache, Listener listener, RequestTransformer requestTransformer, List<RequestHandler> extraRequestHandlers, Stats stats, Config defaultBitmapConfig, boolean indicatorsEnabled, boolean loggingEnabled) {
        this.context = context;
        this.dispatcher = dispatcher;
        this.cache = cache;
        this.listener = listener;
        this.requestTransformer = requestTransformer;
        this.defaultBitmapConfig = defaultBitmapConfig;
        List<RequestHandler> allRequestHandlers = new ArrayList((extraRequestHandlers != null ? extraRequestHandlers.size() : 0) + 7);
        allRequestHandlers.add(new ResourceRequestHandler(context));
        if (extraRequestHandlers != null) {
            allRequestHandlers.addAll(extraRequestHandlers);
        }
        allRequestHandlers.add(new ContactsPhotoRequestHandler(context));
        allRequestHandlers.add(new MediaStoreRequestHandler(context));
        allRequestHandlers.add(new ContentStreamRequestHandler(context));
        allRequestHandlers.add(new AssetRequestHandler(context));
        allRequestHandlers.add(new FileRequestHandler(context));
        allRequestHandlers.add(new NetworkRequestHandler(dispatcher.downloader, stats));
        this.requestHandlers = Collections.unmodifiableList(allRequestHandlers);
        this.stats = stats;
        this.targetToAction = new WeakHashMap();
        this.targetToDeferredRequestCreator = new WeakHashMap();
        this.indicatorsEnabled = indicatorsEnabled;
        this.loggingEnabled = loggingEnabled;
        this.referenceQueue = new ReferenceQueue();
        this.cleanupThread = new CleanupThread(this.referenceQueue, HANDLER);
        this.cleanupThread.start();
    }

    public final void pauseTag(Object tag) {
        Dispatcher dispatcher = this.dispatcher;
        dispatcher.handler.sendMessage(dispatcher.handler.obtainMessage(11, tag));
    }

    public final void resumeTag(Object tag) {
        Dispatcher dispatcher = this.dispatcher;
        dispatcher.handler.sendMessage(dispatcher.handler.obtainMessage(12, tag));
    }

    public final RequestCreator load(Uri uri) {
        return new RequestCreator(this, uri);
    }

    public final RequestCreator load(String path) {
        if (path == null) {
            return new RequestCreator(this, null);
        }
        if (path.trim().length() != 0) {
            return load(Uri.parse(path));
        }
        throw new IllegalArgumentException("Path must not be empty.");
    }

    final void enqueueAndSubmit(Action action) {
        Object target = action.getTarget();
        if (!(target == null || this.targetToAction.get(target) == action)) {
            cancelExistingRequest(target);
            this.targetToAction.put(target, action);
        }
        Dispatcher dispatcher = this.dispatcher;
        dispatcher.handler.sendMessage(dispatcher.handler.obtainMessage(1, action));
    }

    final Bitmap quickMemoryCacheCheck(String key) {
        Bitmap cached = this.cache.get(key);
        if (cached != null) {
            this.stats.dispatchCacheHit();
        } else {
            this.stats.handler.sendEmptyMessage(1);
        }
        return cached;
    }

    final void deliverAction(Bitmap result, LoadedFrom from, Action action) {
        if (!action.cancelled) {
            if (!action.willReplay) {
                this.targetToAction.remove(action.getTarget());
            }
            if (result == null) {
                action.error();
                if (this.loggingEnabled) {
                    Utils.log("Main", "errored", action.request.logId());
                }
            } else if (from == null) {
                throw new AssertionError("LoadedFrom cannot be null.");
            } else {
                action.complete(result, from);
                if (this.loggingEnabled) {
                    Utils.log("Main", "completed", action.request.logId(), "from " + from);
                }
            }
        }
    }

    public final void cancelExistingRequest(Object target) {
        Utils.checkMain();
        Action action = (Action) this.targetToAction.remove(target);
        if (action != null) {
            action.cancel();
            Dispatcher dispatcher = this.dispatcher;
            dispatcher.handler.sendMessage(dispatcher.handler.obtainMessage(2, action));
        }
        if (target instanceof ImageView) {
            DeferredRequestCreator deferredRequestCreator = (DeferredRequestCreator) this.targetToDeferredRequestCreator.remove((ImageView) target);
            if (deferredRequestCreator != null) {
                deferredRequestCreator.cancel();
            }
        }
    }

    public static Picasso with(Context context) {
        if (singleton == null) {
            synchronized (Picasso.class) {
                if (singleton == null) {
                    singleton = new Builder(context).build();
                }
            }
        }
        return singleton;
    }
}
