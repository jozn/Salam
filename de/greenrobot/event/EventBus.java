package de.greenrobot.event;

import android.os.Looper;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class EventBus {
    private static final EventBusBuilder DEFAULT_BUILDER;
    public static String TAG;
    static volatile EventBus defaultInstance;
    private static final Map<Class<?>, List<Class<?>>> eventTypesCache;
    private final AsyncPoster asyncPoster;
    private final BackgroundPoster backgroundPoster;
    private final ThreadLocal<PostingThreadState> currentPostingThreadState;
    private final boolean eventInheritance;
    final ExecutorService executorService;
    private final boolean logNoSubscriberMessages;
    private final boolean logSubscriberExceptions;
    private final HandlerPoster mainThreadPoster;
    private final boolean sendNoSubscriberEvent;
    private final boolean sendSubscriberExceptionEvent;
    private final Map<Class<?>, Object> stickyEvents;
    private final SubscriberMethodFinder subscriberMethodFinder;
    private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
    private final boolean throwSubscriberException;
    private final Map<Object, List<Class<?>>> typesBySubscriber;

    /* renamed from: de.greenrobot.event.EventBus.1 */
    class C12531 extends ThreadLocal<PostingThreadState> {
        C12531() {
        }

        protected final /* bridge */ /* synthetic */ Object initialValue() {
            return new PostingThreadState();
        }
    }

    /* renamed from: de.greenrobot.event.EventBus.2 */
    static /* synthetic */ class C12542 {
        static final /* synthetic */ int[] $SwitchMap$de$greenrobot$event$ThreadMode;

        static {
            $SwitchMap$de$greenrobot$event$ThreadMode = new int[ThreadMode.values().length];
            try {
                $SwitchMap$de$greenrobot$event$ThreadMode[ThreadMode.PostThread.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$de$greenrobot$event$ThreadMode[ThreadMode.MainThread.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$de$greenrobot$event$ThreadMode[ThreadMode.BackgroundThread.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$de$greenrobot$event$ThreadMode[ThreadMode.Async.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    static final class PostingThreadState {
        boolean canceled;
        Object event;
        final List<Object> eventQueue;
        boolean isMainThread;
        boolean isPosting;
        Subscription subscription;

        PostingThreadState() {
            this.eventQueue = new ArrayList();
        }
    }

    static {
        TAG = "Event";
        DEFAULT_BUILDER = new EventBusBuilder();
        eventTypesCache = new HashMap();
    }

    public static EventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (EventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventBus();
                }
            }
        }
        return defaultInstance;
    }

    public EventBus() {
        this(DEFAULT_BUILDER);
    }

    private EventBus(EventBusBuilder builder) {
        this.currentPostingThreadState = new C12531();
        this.subscriptionsByEventType = new HashMap();
        this.typesBySubscriber = new HashMap();
        this.stickyEvents = new ConcurrentHashMap();
        this.mainThreadPoster = new HandlerPoster(this, Looper.getMainLooper());
        this.backgroundPoster = new BackgroundPoster(this);
        this.asyncPoster = new AsyncPoster(this);
        this.subscriberMethodFinder = new SubscriberMethodFinder(builder.skipMethodVerificationForClasses);
        this.logSubscriberExceptions = builder.logSubscriberExceptions;
        this.logNoSubscriberMessages = builder.logNoSubscriberMessages;
        this.sendSubscriberExceptionEvent = builder.sendSubscriberExceptionEvent;
        this.sendNoSubscriberEvent = builder.sendNoSubscriberEvent;
        this.throwSubscriberException = builder.throwSubscriberException;
        this.eventInheritance = builder.eventInheritance;
        this.executorService = builder.executorService;
    }

    public final synchronized void register(Object subscriber, boolean sticky, int priority) {
        for (SubscriberMethod subscriberMethod : this.subscriberMethodFinder.findSubscriberMethods(subscriber.getClass())) {
            CopyOnWriteArrayList copyOnWriteArrayList;
            Class cls = subscriberMethod.eventType;
            CopyOnWriteArrayList copyOnWriteArrayList2 = (CopyOnWriteArrayList) this.subscriptionsByEventType.get(cls);
            Subscription subscription = new Subscription(subscriber, subscriberMethod, priority);
            if (copyOnWriteArrayList2 == null) {
                copyOnWriteArrayList2 = new CopyOnWriteArrayList();
                this.subscriptionsByEventType.put(cls, copyOnWriteArrayList2);
                copyOnWriteArrayList = copyOnWriteArrayList2;
            } else if (copyOnWriteArrayList2.contains(subscription)) {
                throw new EventBusException("Subscriber " + subscriber.getClass() + " already registered to event " + cls);
            } else {
                copyOnWriteArrayList = copyOnWriteArrayList2;
            }
            int size = copyOnWriteArrayList.size();
            int i = 0;
            while (i <= size) {
                if (i == size || subscription.priority > ((Subscription) copyOnWriteArrayList.get(i)).priority) {
                    copyOnWriteArrayList.add(i, subscription);
                    break;
                }
                i++;
            }
            List list = (List) this.typesBySubscriber.get(subscriber);
            if (list == null) {
                list = new ArrayList();
                this.typesBySubscriber.put(subscriber, list);
            }
            list.add(cls);
            if (sticky) {
                Object obj;
                synchronized (this.stickyEvents) {
                    obj = this.stickyEvents.get(cls);
                }
                if (obj != null) {
                    postToSubscription(subscription, obj, Looper.getMainLooper() == Looper.myLooper());
                } else {
                    continue;
                }
            }
        }
    }

    public final synchronized void unregister(Object subscriber) {
        List<Class<?>> subscribedTypes = (List) this.typesBySubscriber.get(subscriber);
        if (subscribedTypes != null) {
            for (Class<?> eventType : subscribedTypes) {
                List list = (List) this.subscriptionsByEventType.get(eventType);
                if (list != null) {
                    int size = list.size();
                    int i = 0;
                    while (i < size) {
                        int i2;
                        Subscription subscription = (Subscription) list.get(i);
                        if (subscription.subscriber == subscriber) {
                            subscription.active = false;
                            list.remove(i);
                            i2 = i - 1;
                            i = size - 1;
                        } else {
                            i2 = i;
                            i = size;
                        }
                        size = i;
                        i = i2 + 1;
                    }
                }
            }
            this.typesBySubscriber.remove(subscriber);
        } else {
            Log.w(TAG, "Subscriber to unregister was not registered before: " + subscriber.getClass());
        }
    }

    public final void post(Object event) {
        PostingThreadState postingState = (PostingThreadState) this.currentPostingThreadState.get();
        List<Object> eventQueue = postingState.eventQueue;
        eventQueue.add(event);
        if (!postingState.isPosting) {
            boolean z;
            if (Looper.getMainLooper() == Looper.myLooper()) {
                z = true;
            } else {
                z = false;
            }
            postingState.isMainThread = z;
            postingState.isPosting = true;
            if (postingState.canceled) {
                throw new EventBusException("Internal error. Abort state was not reset");
            }
            while (!eventQueue.isEmpty()) {
                Object remove = eventQueue.remove(0);
                Class cls = remove.getClass();
                if (this.eventInheritance) {
                    List lookupAllEventTypes = lookupAllEventTypes(cls);
                    boolean z2 = false;
                    for (int i = 0; i < lookupAllEventTypes.size(); i++) {
                        z2 |= postSingleEventForEventType(remove, postingState, (Class) lookupAllEventTypes.get(i));
                    }
                    z = z2;
                } else {
                    z = postSingleEventForEventType(remove, postingState, cls);
                }
                if (!z) {
                    try {
                        if (this.logNoSubscriberMessages) {
                            Log.d(TAG, "No subscribers registered for event " + cls);
                        }
                        if (!(!this.sendNoSubscriberEvent || cls == NoSubscriberEvent.class || cls == SubscriberExceptionEvent.class)) {
                            post(new NoSubscriberEvent(this, remove));
                        }
                    } catch (Throwable th) {
                        postingState.isPosting = false;
                        postingState.isMainThread = false;
                    }
                }
            }
            postingState.isPosting = false;
            postingState.isMainThread = false;
        }
    }

    public final void postSticky(Object event) {
        synchronized (this.stickyEvents) {
            this.stickyEvents.put(event.getClass(), event);
        }
        post(event);
    }

    private boolean postSingleEventForEventType(Object event, PostingThreadState postingState, Class<?> eventClass) {
        synchronized (this) {
            CopyOnWriteArrayList<Subscription> subscriptions = (CopyOnWriteArrayList) this.subscriptionsByEventType.get(eventClass);
        }
        if (subscriptions == null || subscriptions.isEmpty()) {
            return false;
        }
        Iterator i$ = subscriptions.iterator();
        loop0:
        while (i$.hasNext()) {
            Subscription subscription = (Subscription) i$.next();
            postingState.event = event;
            postingState.subscription = subscription;
            try {
                postToSubscription(subscription, event, postingState.isMainThread);
                boolean z = postingState.canceled;
                continue;
            } finally {
                postingState.event = null;
                postingState.subscription = null;
                postingState.canceled = false;
            }
            if (z) {
                break loop0;
            }
        }
        return true;
    }

    private void postToSubscription(Subscription subscription, Object event, boolean isMainThread) {
        PendingPost obtainPendingPost;
        switch (C12542.$SwitchMap$de$greenrobot$event$ThreadMode[subscription.subscriberMethod.threadMode.ordinal()]) {
            case Logger.SEVERE /*1*/:
                invokeSubscriber(subscription, event);
            case Logger.WARNING /*2*/:
                if (isMainThread) {
                    invokeSubscriber(subscription, event);
                    return;
                }
                HandlerPoster handlerPoster = this.mainThreadPoster;
                obtainPendingPost = PendingPost.obtainPendingPost(subscription, event);
                synchronized (handlerPoster) {
                    handlerPoster.queue.enqueue(obtainPendingPost);
                    if (!handlerPoster.handlerActive) {
                        handlerPoster.handlerActive = true;
                        if (!handlerPoster.sendMessage(handlerPoster.obtainMessage())) {
                            throw new EventBusException("Could not send handler message");
                        }
                    }
                    break;
                }
            case Logger.INFO /*3*/:
                if (isMainThread) {
                    Runnable runnable = this.backgroundPoster;
                    obtainPendingPost = PendingPost.obtainPendingPost(subscription, event);
                    synchronized (runnable) {
                        runnable.queue.enqueue(obtainPendingPost);
                        if (!runnable.executorRunning) {
                            runnable.executorRunning = true;
                            runnable.eventBus.executorService.execute(runnable);
                        }
                        break;
                    }
                    return;
                }
                invokeSubscriber(subscription, event);
            case Logger.CONFIG /*4*/:
                Runnable runnable2 = this.asyncPoster;
                runnable2.queue.enqueue(PendingPost.obtainPendingPost(subscription, event));
                runnable2.eventBus.executorService.execute(runnable2);
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscription.subscriberMethod.threadMode);
        }
    }

    private static List<Class<?>> lookupAllEventTypes(Class<?> eventClass) {
        List<Class<?>> eventTypes;
        synchronized (eventTypesCache) {
            eventTypes = (List) eventTypesCache.get(eventClass);
            if (eventTypes == null) {
                eventTypes = new ArrayList();
                for (Class<?> clazz = eventClass; clazz != null; clazz = clazz.getSuperclass()) {
                    eventTypes.add(clazz);
                    addInterfaces(eventTypes, clazz.getInterfaces());
                }
                eventTypesCache.put(eventClass, eventTypes);
            }
        }
        return eventTypes;
    }

    private static void addInterfaces(List<Class<?>> eventTypes, Class<?>[] interfaces) {
        Class<?>[] arr$ = interfaces;
        int len$ = interfaces.length;
        for (int i$ = 0; i$ < len$; i$++) {
            Class<?> interfaceClass = arr$[i$];
            if (!eventTypes.contains(interfaceClass)) {
                eventTypes.add(interfaceClass);
                addInterfaces(eventTypes, interfaceClass.getInterfaces());
            }
        }
    }

    final void invokeSubscriber(PendingPost pendingPost) {
        Object event = pendingPost.event;
        Subscription subscription = pendingPost.subscription;
        PendingPost.releasePendingPost(pendingPost);
        if (subscription.active) {
            invokeSubscriber(subscription, event);
        }
    }

    private void invokeSubscriber(Subscription subscription, Object event) {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber, new Object[]{event});
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (event instanceof SubscriberExceptionEvent) {
                if (this.logSubscriberExceptions) {
                    Log.e(TAG, "SubscriberExceptionEvent subscriber " + subscription.subscriber.getClass() + " threw an exception", cause);
                    SubscriberExceptionEvent subscriberExceptionEvent = (SubscriberExceptionEvent) event;
                    Log.e(TAG, "Initial event " + subscriberExceptionEvent.causingEvent + " caused exception in " + subscriberExceptionEvent.causingSubscriber, subscriberExceptionEvent.throwable);
                }
            } else if (this.throwSubscriberException) {
                throw new EventBusException("Invoking subscriber failed", cause);
            } else {
                if (this.logSubscriberExceptions) {
                    Log.e(TAG, "Could not dispatch event: " + event.getClass() + " to subscribing class " + subscription.subscriber.getClass(), cause);
                }
                if (this.sendSubscriberExceptionEvent) {
                    post(new SubscriberExceptionEvent(this, cause, event, subscription.subscriber));
                }
            }
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException("Unexpected exception", e2);
        }
    }
}
