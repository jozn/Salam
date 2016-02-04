package com.nostra13.universalimageloader.core.assist.deque;

public final class LIFOLinkedBlockingDeque<T> extends LinkedBlockingDeque<T> {
    public final boolean offer(T e) {
        return super.offerFirst(e);
    }

    public final T remove() {
        return super.removeFirst();
    }
}
