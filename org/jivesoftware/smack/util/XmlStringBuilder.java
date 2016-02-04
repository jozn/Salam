package org.jivesoftware.smack.util;

import java.io.IOException;
import org.jivesoftware.smack.packet.Element;
import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.packet.PacketExtension;

public class XmlStringBuilder implements Appendable, CharSequence {
    public static final /* synthetic */ boolean $assertionsDisabled;
    public static final String RIGHT_ANGLE_BRACKET;
    private final LazyStringBuilder sb;

    static {
        $assertionsDisabled = !XmlStringBuilder.class.desiredAssertionStatus();
        RIGHT_ANGLE_BRACKET = Character.toString('>');
    }

    public /* bridge */ /* synthetic */ Appendable append(CharSequence x0, int x1, int x2) throws IOException {
        if ($assertionsDisabled || x0 != null) {
            this.sb.append(x0, x1, x2);
            return this;
        }
        throw new AssertionError();
    }

    public XmlStringBuilder() {
        this.sb = new LazyStringBuilder();
    }

    public XmlStringBuilder(PacketExtension pe) {
        this();
        halfOpenElement(pe.getElementName());
        xmlnsAttribute(pe.getNamespace());
    }

    public XmlStringBuilder(NamedElement e) {
        this();
        halfOpenElement(e.getElementName());
    }

    public final XmlStringBuilder element(String name, String content) {
        if ($assertionsDisabled || content != null) {
            openElement(name);
            escape(content);
            closeElement(name);
            return this;
        }
        throw new AssertionError();
    }

    public final XmlStringBuilder optElement(String name, String content) {
        if (content != null) {
            element(name, content);
        }
        return this;
    }

    public final XmlStringBuilder optElement(Element element) {
        if (element != null) {
            append(element.toXML());
        }
        return this;
    }

    public final XmlStringBuilder optIntElement(String name, int value) {
        if (value >= 0) {
            element(name, String.valueOf(value));
        }
        return this;
    }

    public final XmlStringBuilder halfOpenElement(String name) {
        this.sb.append('<').append((CharSequence) name);
        return this;
    }

    public final XmlStringBuilder openElement(String name) {
        halfOpenElement(name).rightAngleBracket();
        return this;
    }

    public final XmlStringBuilder closeElement(String name) {
        this.sb.append((CharSequence) "</").append((CharSequence) name);
        rightAngleBracket();
        return this;
    }

    public final XmlStringBuilder closeElement(NamedElement e) {
        closeElement(e.getElementName());
        return this;
    }

    public final XmlStringBuilder closeEmptyElement() {
        this.sb.append((CharSequence) "/>");
        return this;
    }

    public final XmlStringBuilder rightAngleBracket() {
        this.sb.append(RIGHT_ANGLE_BRACKET);
        return this;
    }

    public final XmlStringBuilder attribute(String name, String value) {
        if ($assertionsDisabled || value != null) {
            this.sb.append(' ').append((CharSequence) name).append((CharSequence) "='");
            escape(value);
            this.sb.append('\'');
            return this;
        }
        throw new AssertionError();
    }

    public final XmlStringBuilder attribute(String name, Enum<?> value) {
        if ($assertionsDisabled || value != null) {
            attribute(name, value.name());
            return this;
        }
        throw new AssertionError();
    }

    public final XmlStringBuilder optAttribute(String name, String value) {
        if (value != null) {
            attribute(name, value);
        }
        return this;
    }

    public final XmlStringBuilder optAttribute(String name, Enum<?> value) {
        if (value != null) {
            attribute(name, value.name());
        }
        return this;
    }

    public final XmlStringBuilder optIntAttribute(String name, int value) {
        if (value >= 0) {
            attribute(name, Integer.toString(value));
        }
        return this;
    }

    public final XmlStringBuilder xmlnsAttribute(String value) {
        optAttribute("xmlns", value);
        return this;
    }

    public final XmlStringBuilder xmllangAttribute(String value) {
        optAttribute("xml:lang", value);
        return this;
    }

    public final XmlStringBuilder escape(String text) {
        if ($assertionsDisabled || text != null) {
            this.sb.append(StringUtils.escapeForXML(text));
            return this;
        }
        throw new AssertionError();
    }

    public final XmlStringBuilder optAppend(CharSequence csq) {
        if (csq != null) {
            append(csq);
        }
        return this;
    }

    public final XmlStringBuilder append(XmlStringBuilder xsb) {
        if ($assertionsDisabled || xsb != null) {
            LazyStringBuilder lazyStringBuilder = this.sb;
            lazyStringBuilder.list.addAll(xsb.sb.list);
            lazyStringBuilder.cache = null;
            return this;
        }
        throw new AssertionError();
    }

    public final XmlStringBuilder emptyElement(String element) {
        halfOpenElement(element);
        return closeEmptyElement();
    }

    public final XmlStringBuilder condEmptyElement(boolean condition, String element) {
        if (condition) {
            emptyElement(element);
        }
        return this;
    }

    public final XmlStringBuilder append(CharSequence csq) {
        if ($assertionsDisabled || csq != null) {
            this.sb.append(csq);
            return this;
        }
        throw new AssertionError();
    }

    public int length() {
        return this.sb.length();
    }

    public char charAt(int index) {
        return this.sb.charAt(index);
    }

    public CharSequence subSequence(int start, int end) {
        return this.sb.subSequence(start, end);
    }

    public String toString() {
        return this.sb.toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof CharSequence)) {
            return false;
        }
        return toString().equals(((CharSequence) other).toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }
}
