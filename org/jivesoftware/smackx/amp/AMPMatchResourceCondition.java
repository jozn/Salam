package org.jivesoftware.smackx.amp;

import org.jivesoftware.smackx.amp.packet.AMPExtension.Condition;

public final class AMPMatchResourceCondition implements Condition {
    private final Value value;

    public enum Value {
        any,
        exact,
        other
    }

    public AMPMatchResourceCondition(Value value) {
        if (value == null) {
            throw new NullPointerException("Can't create AMPMatchResourceCondition with null value");
        }
        this.value = value;
    }

    public final String getName() {
        return "match-resource";
    }

    public final String getValue() {
        return this.value.toString();
    }
}
