package de.greenrobot.event;

import android.util.Log;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class SubscriberMethodFinder {
    private static final Map<String, List<SubscriberMethod>> methodCache;
    private final Map<Class<?>, Class<?>> skipMethodVerificationForClasses;

    static {
        methodCache = new HashMap();
    }

    SubscriberMethodFinder(List<Class<?>> skipMethodVerificationForClassesList) {
        this.skipMethodVerificationForClasses = new ConcurrentHashMap();
        if (skipMethodVerificationForClassesList != null) {
            for (Class<?> clazz : skipMethodVerificationForClassesList) {
                this.skipMethodVerificationForClasses.put(clazz, clazz);
            }
        }
    }

    final List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass) {
        String key = subscriberClass.getName();
        synchronized (methodCache) {
            List<SubscriberMethod> subscriberMethods = (List) methodCache.get(key);
        }
        if (subscriberMethods != null) {
            return subscriberMethods;
        }
        subscriberMethods = new ArrayList();
        Class<?> clazz = subscriberClass;
        HashSet<String> eventTypesFound = new HashSet();
        StringBuilder methodKeyBuilder = new StringBuilder();
        while (clazz != null) {
            String name = clazz.getName();
            if (!name.startsWith("java.")) {
                if (!name.startsWith("javax.")) {
                    if (name.startsWith("android.")) {
                        break;
                    }
                    for (Method method : clazz.getDeclaredMethods()) {
                        String methodName = method.getName();
                        if (methodName.startsWith("onEvent")) {
                            int modifiers = method.getModifiers();
                            if ((modifiers & 1) == 0 || (modifiers & 5192) != 0) {
                                if (!this.skipMethodVerificationForClasses.containsKey(clazz)) {
                                    Log.d(EventBus.TAG, "Skipping method (not public, static or abstract): " + clazz + "." + methodName);
                                }
                            } else {
                                Class<?>[] parameterTypes = method.getParameterTypes();
                                int length = parameterTypes.length;
                                if (r0 == 1) {
                                    ThreadMode threadMode;
                                    String modifierString = methodName.substring(7);
                                    if (modifierString.length() == 0) {
                                        threadMode = ThreadMode.PostThread;
                                    } else {
                                        if (modifierString.equals("MainThread")) {
                                            threadMode = ThreadMode.MainThread;
                                        } else {
                                            if (modifierString.equals("BackgroundThread")) {
                                                threadMode = ThreadMode.BackgroundThread;
                                            } else {
                                                if (modifierString.equals("Async")) {
                                                    threadMode = ThreadMode.Async;
                                                } else {
                                                    if (!this.skipMethodVerificationForClasses.containsKey(clazz)) {
                                                        throw new EventBusException("Illegal onEvent method, check for typos: " + method);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Class<?> eventType = parameterTypes[0];
                                    methodKeyBuilder.setLength(0);
                                    methodKeyBuilder.append(methodName);
                                    methodKeyBuilder.append('>').append(eventType.getName());
                                    if (eventTypesFound.add(methodKeyBuilder.toString())) {
                                        subscriberMethods.add(new SubscriberMethod(method, threadMode, eventType));
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }
                    }
                    clazz = clazz.getSuperclass();
                } else {
                    break;
                }
            }
            break;
        }
        if (subscriberMethods.isEmpty()) {
            throw new EventBusException("Subscriber " + subscriberClass + " has no public methods called onEvent");
        }
        synchronized (methodCache) {
            methodCache.put(key, subscriberMethods);
        }
        return subscriberMethods;
    }
}
