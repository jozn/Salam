package com.squareup.okhttp.internal.spdy;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.Arrays;

public final class Settings {
    static final int CLIENT_CERTIFICATE_VECTOR_SIZE = 8;
    static final int COUNT = 10;
    static final int CURRENT_CWND = 5;
    static final int DEFAULT_INITIAL_WINDOW_SIZE = 65536;
    static final int DOWNLOAD_BANDWIDTH = 2;
    static final int DOWNLOAD_RETRANS_RATE = 6;
    static final int ENABLE_PUSH = 2;
    static final int FLAG_CLEAR_PREVIOUSLY_PERSISTED_SETTINGS = 1;
    static final int FLOW_CONTROL_OPTIONS = 10;
    static final int FLOW_CONTROL_OPTIONS_DISABLED = 1;
    static final int HEADER_TABLE_SIZE = 1;
    static final int INITIAL_WINDOW_SIZE = 7;
    static final int MAX_CONCURRENT_STREAMS = 4;
    static final int MAX_FRAME_SIZE = 5;
    static final int MAX_HEADER_LIST_SIZE = 6;
    static final int PERSISTED = 2;
    static final int PERSIST_VALUE = 1;
    static final int ROUND_TRIP_TIME = 3;
    static final int UPLOAD_BANDWIDTH = 1;
    private int persistValue;
    private int persisted;
    private int set;
    private final int[] values;

    public Settings() {
        this.values = new int[FLOW_CONTROL_OPTIONS];
    }

    final void clear() {
        this.persisted = 0;
        this.persistValue = 0;
        this.set = 0;
        Arrays.fill(this.values, 0);
    }

    final Settings set(int id, int idFlags, int value) {
        if (id < this.values.length) {
            int bit = UPLOAD_BANDWIDTH << id;
            this.set |= bit;
            if ((idFlags & UPLOAD_BANDWIDTH) != 0) {
                this.persistValue |= bit;
            } else {
                this.persistValue &= bit ^ -1;
            }
            if ((idFlags & PERSISTED) != 0) {
                this.persisted |= bit;
            } else {
                this.persisted &= bit ^ -1;
            }
            this.values[id] = value;
        }
        return this;
    }

    final boolean isSet(int id) {
        if ((this.set & (UPLOAD_BANDWIDTH << id)) != 0) {
            return true;
        }
        return false;
    }

    final int get(int id) {
        return this.values[id];
    }

    final int flags(int id) {
        int result = 0;
        if (isPersisted(id)) {
            result = PERSISTED;
        }
        if (persistValue(id)) {
            return result | UPLOAD_BANDWIDTH;
        }
        return result;
    }

    final int size() {
        return Integer.bitCount(this.set);
    }

    final int getUploadBandwidth(int defaultValue) {
        return (this.set & PERSISTED) != 0 ? this.values[UPLOAD_BANDWIDTH] : defaultValue;
    }

    final int getHeaderTableSize() {
        return (this.set & PERSISTED) != 0 ? this.values[UPLOAD_BANDWIDTH] : -1;
    }

    final int getDownloadBandwidth(int defaultValue) {
        return (this.set & MAX_CONCURRENT_STREAMS) != 0 ? this.values[PERSISTED] : defaultValue;
    }

    final boolean getEnablePush(boolean defaultValue) {
        int i;
        if ((this.set & MAX_CONCURRENT_STREAMS) != 0) {
            i = this.values[PERSISTED];
        } else if (defaultValue) {
            boolean z = true;
        } else {
            i = 0;
        }
        return i == UPLOAD_BANDWIDTH;
    }

    final int getRoundTripTime(int defaultValue) {
        return (this.set & CLIENT_CERTIFICATE_VECTOR_SIZE) != 0 ? this.values[ROUND_TRIP_TIME] : defaultValue;
    }

    final int getMaxConcurrentStreams(int defaultValue) {
        return (this.set & 16) != 0 ? this.values[MAX_CONCURRENT_STREAMS] : defaultValue;
    }

    final int getCurrentCwnd(int defaultValue) {
        return (this.set & 32) != 0 ? this.values[MAX_FRAME_SIZE] : defaultValue;
    }

    final int getMaxFrameSize(int defaultValue) {
        return (this.set & 32) != 0 ? this.values[MAX_FRAME_SIZE] : defaultValue;
    }

    final int getDownloadRetransRate(int defaultValue) {
        return (this.set & 64) != 0 ? this.values[MAX_HEADER_LIST_SIZE] : defaultValue;
    }

    final int getMaxHeaderListSize(int defaultValue) {
        return (this.set & 64) != 0 ? this.values[MAX_HEADER_LIST_SIZE] : defaultValue;
    }

    final int getInitialWindowSize(int defaultValue) {
        return (this.set & AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) != 0 ? this.values[INITIAL_WINDOW_SIZE] : defaultValue;
    }

    final int getClientCertificateVectorSize(int defaultValue) {
        return (this.set & AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY) != 0 ? this.values[CLIENT_CERTIFICATE_VECTOR_SIZE] : defaultValue;
    }

    final boolean isFlowControlDisabled() {
        int i;
        if ((this.set & AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT) != 0) {
            i = this.values[FLOW_CONTROL_OPTIONS];
        } else {
            i = 0;
        }
        if ((i & UPLOAD_BANDWIDTH) != 0) {
            return true;
        }
        return false;
    }

    final boolean persistValue(int id) {
        if ((this.persistValue & (UPLOAD_BANDWIDTH << id)) != 0) {
            return true;
        }
        return false;
    }

    final boolean isPersisted(int id) {
        if ((this.persisted & (UPLOAD_BANDWIDTH << id)) != 0) {
            return true;
        }
        return false;
    }

    final void merge(Settings other) {
        for (int i = 0; i < FLOW_CONTROL_OPTIONS; i += UPLOAD_BANDWIDTH) {
            if (other.isSet(i)) {
                set(i, other.flags(i), other.get(i));
            }
        }
    }
}
