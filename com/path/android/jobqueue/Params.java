package com.path.android.jobqueue;

import java.util.HashSet;

public final class Params {
    public long delayMs;
    public String groupId;
    public boolean persistent;
    int priority;
    public boolean requiresNetwork;
    HashSet<String> tags;

    public Params(int priority) {
        this.requiresNetwork = false;
        this.groupId = null;
        this.persistent = false;
        this.priority = priority;
    }
}
