package org.jivesoftware.smack.util.dns;

import android.support.v4.internal.view.SupportMenu;

public final class SRVRecord extends HostAddress implements Comparable<SRVRecord> {
    public int priority;
    public int weight;

    public final /* bridge */ /* synthetic */ int compareTo(Object x0) {
        SRVRecord sRVRecord = (SRVRecord) x0;
        int i = sRVRecord.priority - this.priority;
        return i == 0 ? this.weight - sRVRecord.weight : i;
    }

    public SRVRecord(String fqdn, int port, int priority, int weight) {
        super(fqdn, port);
        if (weight < 0 || weight > SupportMenu.USER_MASK) {
            throw new IllegalArgumentException("DNS SRV records weight must be a 16-bit unsiged integer (i.e. between 0-65535. Weight was: " + weight);
        } else if (priority < 0 || priority > SupportMenu.USER_MASK) {
            throw new IllegalArgumentException("DNS SRV records priority must be a 16-bit unsiged integer (i.e. between 0-65535. Priority was: " + priority);
        } else {
            this.priority = priority;
            this.weight = weight;
        }
    }

    public final String toString() {
        return super.toString() + " prio:" + this.priority + ":w:" + this.weight;
    }
}
