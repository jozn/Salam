package com.path.android.jobqueue.persistentQueue.sqlite;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class QueryCache {
    private final Map<String, String> cache;
    private final StringBuilder reusedBuilder;

    public QueryCache() {
        this.reusedBuilder = new StringBuilder();
        this.cache = new HashMap();
    }

    public final synchronized String get(boolean hasNetwork, Collection<String> excludeGroups) {
        return (String) this.cache.get(cacheKey(hasNetwork, excludeGroups));
    }

    public final synchronized void set(String query, boolean hasNetwork, Collection<String> excludeGroups) {
        this.cache.put(cacheKey(hasNetwork, excludeGroups), query);
    }

    private String cacheKey(boolean hasNetwork, Collection<String> excludeGroups) {
        if (excludeGroups == null || excludeGroups.size() == 0) {
            return hasNetwork ? "w_n" : "wo_n";
        } else {
            this.reusedBuilder.setLength(0);
            this.reusedBuilder.append(hasNetwork ? "X" : "Y");
            for (String group : excludeGroups) {
                this.reusedBuilder.append("-").append(group);
            }
            return this.reusedBuilder.toString();
        }
    }
}
