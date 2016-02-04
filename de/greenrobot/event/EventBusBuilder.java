package de.greenrobot.event;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class EventBusBuilder {
    private static final ExecutorService DEFAULT_EXECUTOR_SERVICE;
    boolean eventInheritance;
    ExecutorService executorService;
    boolean logNoSubscriberMessages;
    boolean logSubscriberExceptions;
    boolean sendNoSubscriberEvent;
    boolean sendSubscriberExceptionEvent;
    List<Class<?>> skipMethodVerificationForClasses;
    boolean throwSubscriberException;

    static {
        DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    }

    EventBusBuilder() {
        this.logSubscriberExceptions = true;
        this.logNoSubscriberMessages = true;
        this.sendSubscriberExceptionEvent = true;
        this.sendNoSubscriberEvent = true;
        this.eventInheritance = true;
        this.executorService = DEFAULT_EXECUTOR_SERVICE;
    }
}
