package org.jxmpp.util.cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class ExpirationCache<K, V> implements Map<K, V>, Cache<K, V> {
    private final LruCache<K, ExpireElement<V>> cache;
    private long defaultExpirationTime;

    private static class EntryImpl<K, V> implements Entry<K, V> {
        private final K key;
        private V value;

        public EntryImpl(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public final K getKey() {
            return this.key;
        }

        public final V getValue() {
            return this.value;
        }

        public final V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    private static class ExpireElement<V> {
        public final V element;
        public final long expirationTimestamp;

        public ExpireElement(V element, long expirationTime) {
            this.element = element;
            this.expirationTimestamp = System.currentTimeMillis() + expirationTime;
        }

        public final int hashCode() {
            return this.element.hashCode();
        }

        public final boolean equals(Object other) {
            if (!(other instanceof ExpireElement)) {
                return false;
            }
            return this.element.equals(((ExpireElement) other).element);
        }
    }

    public ExpirationCache(int maxSize, long defaultExpirationTime) {
        this.cache = new LruCache(maxSize);
        if (defaultExpirationTime <= 0) {
            throw new IllegalArgumentException();
        }
        this.defaultExpirationTime = defaultExpirationTime;
    }

    public final V put(K key, V value) {
        return put(key, value, this.defaultExpirationTime);
    }

    public final V put(K key, V value, long expirationTime) {
        ExpireElement<V> eOld = (ExpireElement) this.cache.put(key, new ExpireElement(value, expirationTime));
        if (eOld == null) {
            return null;
        }
        return eOld.element;
    }

    public final V get(Object key) {
        ExpireElement<V> v = (ExpireElement) this.cache.get(key);
        if (v == null) {
            return null;
        }
        if ((System.currentTimeMillis() > v.expirationTimestamp ? 1 : null) == null) {
            return v.element;
        }
        remove(key);
        return null;
    }

    public final V remove(Object key) {
        ExpireElement<V> e = (ExpireElement) this.cache.remove(key);
        if (e == null) {
            return null;
        }
        return e.element;
    }

    public final int size() {
        return this.cache.size();
    }

    public final boolean isEmpty() {
        return this.cache.isEmpty();
    }

    public final boolean containsKey(Object key) {
        return this.cache.containsKey(key);
    }

    public final boolean containsValue(Object value) {
        return this.cache.containsValue(value);
    }

    public final void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public final void clear() {
        this.cache.clear();
    }

    public final Set<K> keySet() {
        return this.cache.keySet();
    }

    public final Collection<V> values() {
        Set<V> res = new HashSet();
        for (ExpireElement<V> value : this.cache.values()) {
            res.add(value.element);
        }
        return res;
    }

    public final Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> res = new HashSet();
        for (Entry<K, ExpireElement<V>> entry : this.cache.entrySet()) {
            res.add(new EntryImpl(entry.getKey(), ((ExpireElement) entry.getValue()).element));
        }
        return res;
    }
}
