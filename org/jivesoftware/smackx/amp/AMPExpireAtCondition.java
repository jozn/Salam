package org.jivesoftware.smackx.amp;

import org.jivesoftware.smackx.amp.packet.AMPExtension.Condition;

public final class AMPExpireAtCondition implements Condition {
    private final String value;

    public AMPExpireAtCondition(String utcDateTime) {
        if (utcDateTime == null) {
            throw new NullPointerException("Can't create AMPExpireAtCondition with null value");
        }
        this.value = utcDateTime;
    }

    public final String getName() {
        return "expire-at";
    }

    public final String getValue() {
        return this.value;
    }
}
