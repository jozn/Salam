package org.jivesoftware.smackx.xdata;

import android.support.v7.appcompat.BuildConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jivesoftware.smack.util.XmlStringBuilder;

public final class FormField {
    public String description;
    public String label;
    public final List<Option> options;
    public boolean required;
    public String type;
    final List<String> values;
    public String variable;

    public static class Option {
        String label;
        final String value;

        public Option(String label, String value) {
            this.label = label;
            this.value = value;
        }

        public final String toString() {
            return this.label;
        }

        public final boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }
            Option other = (Option) obj;
            if (!this.value.equals(other.value)) {
                return false;
            }
            if ((this.label == null ? BuildConfig.VERSION_NAME : this.label).equals(other.label == null ? BuildConfig.VERSION_NAME : other.label)) {
                return true;
            }
            return false;
        }

        public final int hashCode() {
            return (this.label == null ? 0 : this.label.hashCode()) + ((this.value.hashCode() + 37) * 37);
        }
    }

    public FormField(String variable) {
        this.required = false;
        this.options = new ArrayList();
        this.values = new ArrayList();
        this.variable = variable;
    }

    public FormField() {
        this.required = false;
        this.options = new ArrayList();
        this.values = new ArrayList();
        this.type = "fixed";
    }

    private List<Option> getOptions() {
        List<Option> unmodifiableList;
        synchronized (this.options) {
            unmodifiableList = Collections.unmodifiableList(new ArrayList(this.options));
        }
        return unmodifiableList;
    }

    public final List<String> getValues() {
        List<String> unmodifiableList;
        synchronized (this.values) {
            unmodifiableList = Collections.unmodifiableList(new ArrayList(this.values));
        }
        return unmodifiableList;
    }

    public final void addValue(String value) {
        synchronized (this.values) {
            this.values.add(value);
        }
    }

    protected final void resetValues() {
        synchronized (this.values) {
            this.values.removeAll(new ArrayList(this.values));
        }
    }

    public final XmlStringBuilder toXML() {
        XmlStringBuilder buf = new XmlStringBuilder();
        buf.halfOpenElement("field");
        buf.optAttribute("label", this.label);
        buf.optAttribute("var", this.variable);
        buf.optAttribute("type", this.type);
        buf.rightAngleBracket();
        buf.optElement("desc", this.description);
        buf.condEmptyElement(this.required, "required");
        for (String value : getValues()) {
            buf.element("value", value);
        }
        for (Option option : getOptions()) {
            XmlStringBuilder xmlStringBuilder = new XmlStringBuilder();
            xmlStringBuilder.halfOpenElement("option");
            xmlStringBuilder.optAttribute("label", option.label);
            xmlStringBuilder.rightAngleBracket();
            xmlStringBuilder.element("value", option.value);
            xmlStringBuilder.closeElement("option");
            buf.append(xmlStringBuilder);
        }
        buf.closeElement("field");
        return buf;
    }

    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FormField)) {
            return false;
        }
        return toXML().equals(((FormField) obj).toXML());
    }

    public final int hashCode() {
        return toXML().hashCode();
    }
}
