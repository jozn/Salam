package com.path.android.jobqueue.cachedQueue;

import com.path.android.jobqueue.JobHolder;
import com.path.android.jobqueue.JobQueue;
import java.util.Collection;

public final class CachedJobQueue implements JobQueue {
    private Cache cache;
    JobQueue delegate;

    private static class Cache {
        Integer count;
        DelayUntil delayUntil;

        private static class DelayUntil {
            boolean hasNetwork;
            Long value;

            private DelayUntil(boolean hasNetwork, Long value) {
                this.value = value;
                this.hasNetwork = hasNetwork;
            }
        }

        private Cache() {
        }

        public final void invalidateAll() {
            this.count = null;
            this.delayUntil = null;
        }
    }

    public CachedJobQueue(JobQueue delegate) {
        this.delegate = delegate;
        this.cache = new Cache();
    }

    public final long insert(JobHolder jobHolder) {
        this.cache.invalidateAll();
        return this.delegate.insert(jobHolder);
    }

    public final long insertOrReplace(JobHolder jobHolder) {
        this.cache.invalidateAll();
        return this.delegate.insertOrReplace(jobHolder);
    }

    public final void remove(JobHolder jobHolder) {
        this.cache.invalidateAll();
        this.delegate.remove(jobHolder);
    }

    public final int count() {
        if (this.cache.count == null) {
            this.cache.count = Integer.valueOf(this.delegate.count());
        }
        return this.cache.count.intValue();
    }

    public final int countReadyJobs(boolean hasNetwork, Collection<String> excludeGroups) {
        if (this.cache.count != null && this.cache.count.intValue() <= 0) {
            return 0;
        }
        int count = this.delegate.countReadyJobs(hasNetwork, excludeGroups);
        if (count != 0) {
            return count;
        }
        count();
        return count;
    }

    public final JobHolder nextJobAndIncRunCount(boolean hasNetwork, Collection<String> excludeGroups) {
        if (this.cache.count != null && this.cache.count.intValue() <= 0) {
            return null;
        }
        JobHolder holder = this.delegate.nextJobAndIncRunCount(hasNetwork, excludeGroups);
        if (holder == null) {
            count();
            return holder;
        } else if (this.cache.count == null) {
            return holder;
        } else {
            Cache cache = this.cache;
            cache.count = Integer.valueOf(cache.count.intValue() - 1);
            return holder;
        }
    }

    public final Long getNextJobDelayUntilNs(boolean hasNetwork) {
        byte b = (byte) 0;
        if (this.cache.delayUntil == null) {
            this.cache.delayUntil = new DelayUntil(this.delegate.getNextJobDelayUntilNs(hasNetwork), (byte) 0);
        } else {
            if (this.cache.delayUntil.hasNetwork == hasNetwork) {
                b = (byte) 1;
            }
            if (b == null) {
                DelayUntil delayUntil = this.cache.delayUntil;
                delayUntil.value = this.delegate.getNextJobDelayUntilNs(hasNetwork);
                delayUntil.hasNetwork = hasNetwork;
            }
        }
        return this.cache.delayUntil.value;
    }
}
