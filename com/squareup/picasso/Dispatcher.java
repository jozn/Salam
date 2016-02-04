package com.squareup.picasso;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.appcompat.BuildConfig;
import com.kyleduo.switchbutton.C0473R;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class Dispatcher {
    boolean airplaneMode;
    final List<BitmapHunter> batch;
    final Cache cache;
    final Context context;
    public final DispatcherThread dispatcherThread;
    public final Downloader downloader;
    final Map<Object, Action> failedActions;
    final Handler handler;
    final Map<String, BitmapHunter> hunterMap;
    final Handler mainThreadHandler;
    final Map<Object, Action> pausedActions;
    final Set<Object> pausedTags;
    final NetworkBroadcastReceiver receiver;
    final boolean scansNetworkChanges;
    public final ExecutorService service;
    final Stats stats;

    /* renamed from: com.squareup.picasso.Dispatcher.1 */
    class C12321 implements Runnable {
        public final void run() {
            BroadcastReceiver broadcastReceiver = Dispatcher.this.receiver;
            broadcastReceiver.dispatcher.context.unregisterReceiver(broadcastReceiver);
        }
    }

    private static class DispatcherHandler extends Handler {
        private final Dispatcher dispatcher;

        /* renamed from: com.squareup.picasso.Dispatcher.DispatcherHandler.1 */
        class C12331 implements Runnable {
            final /* synthetic */ Message val$msg;

            C12331(Message message) {
                this.val$msg = message;
            }

            public final void run() {
                throw new AssertionError("Unknown handler message received: " + this.val$msg.what);
            }
        }

        public DispatcherHandler(Looper looper, Dispatcher dispatcher) {
            super(looper);
            this.dispatcher = dispatcher;
        }

        public final void handleMessage(Message msg) {
            BitmapHunter bitmapHunter;
            Dispatcher dispatcher;
            switch (msg.what) {
                case Logger.SEVERE /*1*/:
                    this.dispatcher.performSubmit(msg.obj, true);
                case Logger.WARNING /*2*/:
                    Action action = (Action) msg.obj;
                    Dispatcher dispatcher2 = this.dispatcher;
                    String str = action.key;
                    bitmapHunter = (BitmapHunter) dispatcher2.hunterMap.get(str);
                    if (bitmapHunter != null) {
                        bitmapHunter.detach(action);
                        if (bitmapHunter.cancel()) {
                            dispatcher2.hunterMap.remove(str);
                            if (action.picasso.loggingEnabled) {
                                Utils.log("Dispatcher", "canceled", action.request.logId());
                            }
                        }
                    }
                    if (dispatcher2.pausedTags.contains(action.tag)) {
                        dispatcher2.pausedActions.remove(action.getTarget());
                        if (action.picasso.loggingEnabled) {
                            Utils.log("Dispatcher", "canceled", action.request.logId(), "because paused request got canceled");
                        }
                    }
                    Action action2 = (Action) dispatcher2.failedActions.remove(action.getTarget());
                    if (action2 != null && action2.picasso.loggingEnabled) {
                        Utils.log("Dispatcher", "canceled", action2.request.logId(), "from replaying");
                    }
                case Logger.CONFIG /*4*/:
                    BitmapHunter hunter = msg.obj;
                    dispatcher = this.dispatcher;
                    if (MemoryPolicy.shouldWriteToMemoryCache(hunter.memoryPolicy)) {
                        dispatcher.cache.set(hunter.key, hunter.result);
                    }
                    dispatcher.hunterMap.remove(hunter.key);
                    dispatcher.batch(hunter);
                    if (hunter.picasso.loggingEnabled) {
                        Utils.log("Dispatcher", "batched", Utils.getLogIdsForHunter(hunter), "for completion");
                    }
                case Logger.FINE /*5*/:
                    this.dispatcher.performRetry((BitmapHunter) msg.obj);
                case Logger.FINER /*6*/:
                    this.dispatcher.performError((BitmapHunter) msg.obj, false);
                case Logger.FINEST /*7*/:
                    dispatcher = this.dispatcher;
                    List arrayList = new ArrayList(dispatcher.batch);
                    dispatcher.batch.clear();
                    dispatcher.mainThreadHandler.sendMessage(dispatcher.mainThreadHandler.obtainMessage(8, arrayList));
                    Dispatcher.logBatch(arrayList);
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    this.dispatcher.performNetworkStateChange(msg.obj);
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    this.dispatcher.airplaneMode = msg.arg1 == 1;
                case C0473R.styleable.SwitchButton_offColor /*11*/:
                    Object tag = msg.obj;
                    Dispatcher dispatcher3 = this.dispatcher;
                    if (dispatcher3.pausedTags.add(tag)) {
                        Iterator it = dispatcher3.hunterMap.values().iterator();
                        while (it.hasNext()) {
                            bitmapHunter = (BitmapHunter) it.next();
                            boolean z = bitmapHunter.picasso.loggingEnabled;
                            Action action3 = bitmapHunter.action;
                            List list = bitmapHunter.actions;
                            Object obj = (list == null || list.isEmpty()) ? null : 1;
                            if (action3 != null || obj != null) {
                                if (action3 != null && action3.tag.equals(tag)) {
                                    bitmapHunter.detach(action3);
                                    dispatcher3.pausedActions.put(action3.getTarget(), action3);
                                    if (z) {
                                        Utils.log("Dispatcher", "paused", action3.request.logId(), "because tag '" + tag + "' was paused");
                                    }
                                }
                                if (obj != null) {
                                    for (int size = list.size() - 1; size >= 0; size--) {
                                        Action action4 = (Action) list.get(size);
                                        if (action4.tag.equals(tag)) {
                                            bitmapHunter.detach(action4);
                                            dispatcher3.pausedActions.put(action4.getTarget(), action4);
                                            if (z) {
                                                Utils.log("Dispatcher", "paused", action4.request.logId(), "because tag '" + tag + "' was paused");
                                            }
                                        }
                                    }
                                }
                                if (bitmapHunter.cancel()) {
                                    it.remove();
                                    if (z) {
                                        Utils.log("Dispatcher", "canceled", Utils.getLogIdsForHunter(bitmapHunter), "all actions paused");
                                    }
                                }
                            }
                        }
                    }
                case C0473R.styleable.SwitchButton_thumbColor /*12*/:
                    this.dispatcher.performResumeTag(msg.obj);
                default:
                    Picasso.HANDLER.post(new C12331(msg));
            }
        }
    }

    static class DispatcherThread extends HandlerThread {
        DispatcherThread() {
            super("Picasso-Dispatcher", 10);
        }
    }

    static class NetworkBroadcastReceiver extends BroadcastReceiver {
        final Dispatcher dispatcher;

        NetworkBroadcastReceiver(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public final void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                Dispatcher dispatcher;
                if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    if (intent.hasExtra("state")) {
                        dispatcher = this.dispatcher;
                        dispatcher.handler.sendMessage(dispatcher.handler.obtainMessage(10, intent.getBooleanExtra("state", false) ? 1 : 0, 0));
                    }
                } else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) Utils.getService(context, "connectivity");
                    dispatcher = this.dispatcher;
                    dispatcher.handler.sendMessage(dispatcher.handler.obtainMessage(9, connectivityManager.getActiveNetworkInfo()));
                }
            }
        }
    }

    Dispatcher(Context context, ExecutorService service, Handler mainThreadHandler, Downloader downloader, Cache cache, Stats stats) {
        this.dispatcherThread = new DispatcherThread();
        this.dispatcherThread.start();
        Utils.flushStackLocalLeaks(this.dispatcherThread.getLooper());
        this.context = context;
        this.service = service;
        this.hunterMap = new LinkedHashMap();
        this.failedActions = new WeakHashMap();
        this.pausedActions = new WeakHashMap();
        this.pausedTags = new HashSet();
        this.handler = new DispatcherHandler(this.dispatcherThread.getLooper(), this);
        this.downloader = downloader;
        this.mainThreadHandler = mainThreadHandler;
        this.cache = cache;
        this.stats = stats;
        this.batch = new ArrayList(4);
        this.airplaneMode = Utils.isAirplaneModeOn(this.context);
        this.scansNetworkChanges = Utils.hasPermission(context, "android.permission.ACCESS_NETWORK_STATE");
        this.receiver = new NetworkBroadcastReceiver(this);
        BroadcastReceiver broadcastReceiver = this.receiver;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        if (broadcastReceiver.dispatcher.scansNetworkChanges) {
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        }
        broadcastReceiver.dispatcher.context.registerReceiver(broadcastReceiver, intentFilter);
    }

    final void dispatchRetry(BitmapHunter hunter) {
        this.handler.sendMessageDelayed(this.handler.obtainMessage(5, hunter), 500);
    }

    final void dispatchFailed(BitmapHunter hunter) {
        this.handler.sendMessage(this.handler.obtainMessage(6, hunter));
    }

    final void performSubmit(Action action, boolean dismissFailed) {
        if (this.pausedTags.contains(action.tag)) {
            this.pausedActions.put(action.getTarget(), action);
            if (action.picasso.loggingEnabled) {
                Utils.log("Dispatcher", "paused", action.request.logId(), "because tag '" + action.tag + "' is paused");
                return;
            }
            return;
        }
        BitmapHunter hunter = (BitmapHunter) this.hunterMap.get(action.key);
        if (hunter != null) {
            boolean z = hunter.picasso.loggingEnabled;
            Request request = action.request;
            if (hunter.action == null) {
                hunter.action = action;
                if (!z) {
                    return;
                }
                if (hunter.actions == null || hunter.actions.isEmpty()) {
                    Utils.log("Hunter", "joined", request.logId(), "to empty hunter");
                    return;
                } else {
                    Utils.log("Hunter", "joined", request.logId(), Utils.getLogIdsForHunter(hunter, "to "));
                    return;
                }
            }
            if (hunter.actions == null) {
                hunter.actions = new ArrayList(3);
            }
            hunter.actions.add(action);
            if (z) {
                Utils.log("Hunter", "joined", request.logId(), Utils.getLogIdsForHunter(hunter, "to "));
            }
            int i = action.request.priority$159b5429;
            if (i - 1 > hunter.priority$159b5429 - 1) {
                hunter.priority$159b5429 = i;
            }
        } else if (!this.service.isShutdown()) {
            hunter = BitmapHunter.forRequest(action.picasso, this, this.cache, this.stats, action);
            hunter.future = this.service.submit(hunter);
            this.hunterMap.put(action.key, hunter);
            if (dismissFailed) {
                this.failedActions.remove(action.getTarget());
            }
            if (action.picasso.loggingEnabled) {
                Utils.log("Dispatcher", "enqueued", action.request.logId());
            }
        } else if (action.picasso.loggingEnabled) {
            Utils.log("Dispatcher", "ignored", action.request.logId(), "because shut down");
        }
    }

    final void performResumeTag(Object tag) {
        if (this.pausedTags.remove(tag)) {
            List<Action> batch = null;
            Iterator<Action> i = this.pausedActions.values().iterator();
            while (i.hasNext()) {
                Action action = (Action) i.next();
                if (action.tag.equals(tag)) {
                    if (batch == null) {
                        batch = new ArrayList();
                    }
                    batch.add(action);
                    i.remove();
                }
            }
            if (batch != null) {
                this.mainThreadHandler.sendMessage(this.mainThreadHandler.obtainMessage(13, batch));
            }
        }
    }

    final void performRetry(BitmapHunter hunter) {
        if (!hunter.isCancelled()) {
            if (this.service.isShutdown()) {
                performError(hunter, false);
                return;
            }
            boolean z;
            boolean shouldRetryHunter;
            NetworkInfo networkInfo = null;
            if (this.scansNetworkChanges) {
                networkInfo = ((ConnectivityManager) Utils.getService(this.context, "connectivity")).getActiveNetworkInfo();
            }
            boolean hasConnectivity;
            if (networkInfo == null || !networkInfo.isConnected()) {
                hasConnectivity = false;
            } else {
                hasConnectivity = true;
            }
            if (hunter.retryCount > 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                hunter.retryCount--;
                shouldRetryHunter = hunter.requestHandler.shouldRetry$552f0f64(networkInfo);
            } else {
                shouldRetryHunter = false;
            }
            boolean supportsReplay = hunter.requestHandler.supportsReplay();
            if (!shouldRetryHunter) {
                boolean willReplay;
                if (this.scansNetworkChanges && supportsReplay) {
                    willReplay = true;
                } else {
                    willReplay = false;
                }
                performError(hunter, willReplay);
                if (willReplay) {
                    markForReplay(hunter);
                }
            } else if (!this.scansNetworkChanges || hasConnectivity) {
                if (hunter.picasso.loggingEnabled) {
                    Utils.log("Dispatcher", "retrying", Utils.getLogIdsForHunter(hunter));
                }
                if (hunter.exception instanceof ContentLengthException) {
                    hunter.networkPolicy |= NetworkPolicy.NO_CACHE.index;
                }
                hunter.future = this.service.submit(hunter);
            } else {
                performError(hunter, supportsReplay);
                if (supportsReplay) {
                    markForReplay(hunter);
                }
            }
        }
    }

    final void performError(BitmapHunter hunter, boolean willReplay) {
        if (hunter.picasso.loggingEnabled) {
            Utils.log("Dispatcher", "batched", Utils.getLogIdsForHunter(hunter), "for error" + (willReplay ? " (will replay)" : BuildConfig.VERSION_NAME));
        }
        this.hunterMap.remove(hunter.key);
        batch(hunter);
    }

    final void performNetworkStateChange(NetworkInfo info) {
        if (this.service instanceof PicassoExecutorService) {
            PicassoExecutorService picassoExecutorService = (PicassoExecutorService) this.service;
            if (info != null && info.isConnectedOrConnecting()) {
                switch (info.getType()) {
                    case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                        switch (info.getSubtype()) {
                            case Logger.SEVERE /*1*/:
                            case Logger.WARNING /*2*/:
                                picassoExecutorService.setThreadCount(1);
                                break;
                            case Logger.INFO /*3*/:
                            case Logger.CONFIG /*4*/:
                            case Logger.FINE /*5*/:
                            case Logger.FINER /*6*/:
                            case C0473R.styleable.SwitchButton_thumbColor /*12*/:
                                picassoExecutorService.setThreadCount(2);
                                break;
                            case C0473R.styleable.SwitchButton_thumbPressedColor /*13*/:
                            case C0473R.styleable.SwitchButton_animationVelocity /*14*/:
                            case C0473R.styleable.SwitchButton_radius /*15*/:
                                picassoExecutorService.setThreadCount(3);
                                break;
                            default:
                                picassoExecutorService.setThreadCount(3);
                                break;
                        }
                    case Logger.SEVERE /*1*/:
                    case Logger.FINER /*6*/:
                    case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                        picassoExecutorService.setThreadCount(4);
                        break;
                    default:
                        picassoExecutorService.setThreadCount(3);
                        break;
                }
            }
            picassoExecutorService.setThreadCount(3);
        }
        if (info != null && info.isConnected() && !this.failedActions.isEmpty()) {
            Iterator it = this.failedActions.values().iterator();
            while (it.hasNext()) {
                Action action = (Action) it.next();
                it.remove();
                if (action.picasso.loggingEnabled) {
                    Utils.log("Dispatcher", "replaying", action.request.logId());
                }
                performSubmit(action, false);
            }
        }
    }

    private void markForReplay(BitmapHunter hunter) {
        Action action = hunter.action;
        if (action != null) {
            markForReplay(action);
        }
        List<Action> joined = hunter.actions;
        if (joined != null) {
            int n = joined.size();
            for (int i = 0; i < n; i++) {
                markForReplay((Action) joined.get(i));
            }
        }
    }

    private void markForReplay(Action action) {
        Object target = action.getTarget();
        if (target != null) {
            action.willReplay = true;
            this.failedActions.put(target, action);
        }
    }

    final void batch(BitmapHunter hunter) {
        if (!hunter.isCancelled()) {
            this.batch.add(hunter);
            if (!this.handler.hasMessages(7)) {
                this.handler.sendEmptyMessageDelayed(7, 200);
            }
        }
    }

    static void logBatch(List<BitmapHunter> copy) {
        if (!copy.isEmpty() && ((BitmapHunter) copy.get(0)).picasso.loggingEnabled) {
            StringBuilder builder = new StringBuilder();
            for (BitmapHunter bitmapHunter : copy) {
                if (builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(Utils.getLogIdsForHunter(bitmapHunter));
            }
            Utils.log("Dispatcher", "delivered", builder.toString());
        }
    }
}
