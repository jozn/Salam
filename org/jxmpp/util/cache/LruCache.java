package org.jxmpp.util.cache;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public final class LruCache<K, V> extends LinkedHashMap<K, V> implements Cache<K, V> {
    private final AtomicLong cacheHits;
    private final AtomicLong cacheMisses;
    private int maxCacheSize;

    public LruCache(int maxSize) {
        int i = 50;
        if (maxSize < 50) {
            i = maxSize;
        }
        super(i, 0.75f, true);
        this.cacheHits = new AtomicLong();
        this.cacheMisses = new AtomicLong();
        if (maxSize == 0) {
            throw new IllegalArgumentException("Max cache size cannot be 0.");
        }
        this.maxCacheSize = maxSize;
    }

    protected final boolean removeEldestEntry(Entry<K, V> entry) {
        return size() > this.maxCacheSize;
    }

    public final synchronized V put(K key, V value) {
        return super.put(key, value);
    }

    public final V get(Object key) {
        synchronized (this) {
            V cacheObject = super.get(key);
        }
        if (cacheObject == null) {
            this.cacheMisses.incrementAndGet();
            return null;
        }
        this.cacheHits.incrementAndGet();
        return cacheObject;
    }

    public final synchronized V remove(Object key) {
        return super.remove(key);
    }

    public final void clear() {
        synchronized (this) {
            super.clear();
        }
        this.cacheHits.set(0);
        this.cacheMisses.set(0);
    }

    public final synchronized int size() {
        return super.size();
    }

    public final synchronized boolean isEmpty() {
        return super.isEmpty();
    }

    public final synchronized Collection<V> values() {
        return super.values();
    }

    public final synchronized boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    public final synchronized void putAll(Map<? extends K, ? extends V> m) {
        super.putAll(m);
    }

    public final synchronized boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    public final synchronized Set<Entry<K, V>> entrySet() {
        return super.entrySet();
    }

    public final synchronized Set<K> keySet() {
        return super.keySet();
    }
}
