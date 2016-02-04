package org.jivesoftware.smackx.amp;

import org.jivesoftware.smackx.amp.packet.AMPExtension.Condition;

public final class AMPDeliverCondition implements Condition {
    private final Value value;

    public enum Value {
        direct,
        forward,
        gateway,
        none,
        stored
    }

    public AMPDeliverCondition(Value value) {
        if (value == null) {
            throw new NullPointerException("Can't create AMPDeliverCondition with null value");
        }
        this.value = value;
    }

    public final String getName() {
        return "deliver";
    }

    public final String getValue() {
        return this.value.toString();
    }
}
