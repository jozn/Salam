package com.path.android.jobqueue.nonPersistentQueue;

import com.path.android.jobqueue.JobHolder;
import com.path.android.jobqueue.log.JqLog;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public final class NonPersistentJobSet implements JobSet {
    private final Map<String, Integer> existingGroups;
    private final Map<Long, JobHolder> idCache;
    private final TreeSet<JobHolder> set;
    private final Map<String, List<JobHolder>> tagCache;

    public NonPersistentJobSet(Comparator<JobHolder> comparator) {
        this.set = new TreeSet(comparator);
        this.existingGroups = new HashMap();
        this.idCache = new HashMap();
        this.tagCache = new HashMap();
    }

    public final JobHolder peek(Collection<String> excludeGroupIds) {
        if (excludeGroupIds != null && excludeGroupIds.size() != 0) {
            Iterator i$ = this.set.iterator();
            while (i$.hasNext()) {
                JobHolder holder = (JobHolder) i$.next();
                if (holder.getGroupId() == null) {
                    return holder;
                }
                if (!excludeGroupIds.contains(holder.getGroupId())) {
                    return holder;
                }
            }
            return null;
        } else if (this.set.size() <= 0) {
            return null;
        } else {
            return (JobHolder) this.set.first();
        }
    }

    public final boolean offer(JobHolder holder) {
        if (holder.getId() == null) {
            throw new RuntimeException("cannot add job holder w/o an ID");
        }
        boolean result = this.set.add(holder);
        if (!result) {
            remove(holder);
            result = this.set.add(holder);
        }
        if (result) {
            this.idCache.put(holder.getId(), holder);
            addToTagCache(holder);
            if (holder.getGroupId() != null) {
                String groupId = holder.getGroupId();
                if (this.existingGroups.containsKey(groupId)) {
                    this.existingGroups.put(groupId, Integer.valueOf(((Integer) this.existingGroups.get(groupId)).intValue() + 1));
                } else {
                    this.existingGroups.put(groupId, Integer.valueOf(1));
                }
            }
        }
        return result;
    }

    private void addToTagCache(JobHolder holder) {
        Set<String> tags = holder.getTags();
        if (tags != null && tags.size() != 0) {
            for (String tag : tags) {
                List<JobHolder> jobs = (List) this.tagCache.get(tag);
                if (jobs == null) {
                    jobs = new LinkedList();
                    this.tagCache.put(tag, jobs);
                }
                jobs.add(holder);
            }
        }
    }

    public final boolean remove(JobHolder holder) {
        boolean removed = this.set.remove(holder);
        if (removed) {
            this.idCache.remove(holder.getId());
            Set<String> tags = holder.getTags();
            if (!(tags == null || tags.size() == 0)) {
                for (String str : tags) {
                    List list = (List) this.tagCache.get(str);
                    if (list == null) {
                        JqLog.m39e("trying to remove job from tag cache but cannot find the tag cache", new Object[0]);
                        break;
                    } else if (!list.remove(holder)) {
                        JqLog.m39e("trying to remove job from tag cache but cannot find it in the cache", new Object[0]);
                    } else if (list.size() == 0) {
                        this.tagCache.remove(str);
                    }
                }
            }
            if (holder.getGroupId() != null) {
                String groupId = holder.getGroupId();
                Integer num = (Integer) this.existingGroups.get(groupId);
                if (num == null || num.intValue() <= 0) {
                    JqLog.m39e("detected inconsistency in NonPersistentJobSet's group id hash. Please report a bug", new Object[0]);
                    this.existingGroups.remove(groupId);
                } else {
                    num = Integer.valueOf(num.intValue() - 1);
                    if (num.intValue() == 0) {
                        this.existingGroups.remove(groupId);
                    } else {
                        this.existingGroups.put(groupId, num);
                    }
                }
            }
        }
        return removed;
    }

    public final int size() {
        return this.set.size();
    }

    public final CountWithGroupIdsResult countReadyJobs(long now, Collection<String> excludeGroups) {
        int total = 0;
        int groupCnt = this.existingGroups.keySet().size();
        Set<String> groupIdSet = null;
        if (groupCnt > 0) {
            groupIdSet = new HashSet();
        }
        Iterator i$ = this.set.iterator();
        while (i$.hasNext()) {
            JobHolder holder = (JobHolder) i$.next();
            if (holder.getDelayUntilNs() < now && (holder.getGroupId() == null || ((excludeGroups == null || !excludeGroups.contains(holder.getGroupId())) && groupCnt > 0 && groupIdSet.add(holder.getGroupId())))) {
                total++;
            }
        }
        return new CountWithGroupIdsResult(total, groupIdSet);
    }

    public final CountWithGroupIdsResult countReadyJobs(Collection<String> excludeGroups) {
        if (this.existingGroups.size() == 0) {
            return new CountWithGroupIdsResult(this.set.size(), null);
        }
        int total = 0;
        Set<String> existingGroupIds = null;
        Iterator i$ = this.set.iterator();
        while (i$.hasNext()) {
            JobHolder holder = (JobHolder) i$.next();
            if (holder.getGroupId() != null) {
                if (excludeGroups == null || !excludeGroups.contains(holder.getGroupId())) {
                    if (existingGroupIds == null) {
                        existingGroupIds = new HashSet();
                        existingGroupIds.add(holder.getGroupId());
                    } else if (existingGroupIds.add(holder.getGroupId())) {
                    }
                }
            }
            total++;
        }
        return new CountWithGroupIdsResult(total, existingGroupIds);
    }
}
