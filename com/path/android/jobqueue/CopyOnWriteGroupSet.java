package com.path.android.jobqueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

public final class CopyOnWriteGroupSet {
    private final TreeSet<String> internalSet;
    private ArrayList<String> publicClone;

    public CopyOnWriteGroupSet() {
        this.internalSet = new TreeSet();
    }

    public final synchronized Collection<String> getSafe() {
        if (this.publicClone == null) {
            this.publicClone = new ArrayList(this.internalSet);
        }
        return this.publicClone;
    }

    public final synchronized void add(String group) {
        if (this.internalSet.add(group)) {
            this.publicClone = null;
        }
    }

    public final synchronized void remove(String group) {
        if (this.internalSet.remove(group)) {
            this.publicClone = null;
        }
    }
}
