package cz.msebera.android.httpclient.impl.client;

import java.net.URI;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class RedirectLocations extends AbstractList<Object> {
    final List<URI> all;
    final Set<URI> unique;

    public final /* bridge */ /* synthetic */ Object get(int i) {
        return (URI) this.all.get(i);
    }

    public final /* bridge */ /* synthetic */ Object remove(int i) {
        URI uri = (URI) this.all.remove(i);
        this.unique.remove(uri);
        if (this.all.size() != this.unique.size()) {
            this.unique.addAll(this.all);
        }
        return uri;
    }

    public RedirectLocations() {
        this.unique = new HashSet();
        this.all = new ArrayList();
    }

    public final int size() {
        return this.all.size();
    }

    public final Object set(int index, Object element) {
        URI removed = (URI) this.all.set(index, (URI) element);
        this.unique.remove(removed);
        this.unique.add((URI) element);
        if (this.all.size() != this.unique.size()) {
            this.unique.addAll(this.all);
        }
        return removed;
    }

    public final void add(int index, Object element) {
        this.all.add(index, (URI) element);
        this.unique.add((URI) element);
    }

    public final boolean contains(Object o) {
        return this.unique.contains(o);
    }
}
