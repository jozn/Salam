package org.jivesoftware.smackx.commands;

public final class AdHocCommandNote {
    public Type type;
    public String value;

    public enum Type {
        info,
        warn,
        error
    }

    public AdHocCommandNote(Type type, String value) {
        this.type = type;
        this.value = value;
    }
}
