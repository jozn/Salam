package org.jivesoftware.smackx.xdata.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jivesoftware.smack.packet.Element;
import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smackx.xdata.FormField;

public final class DataForm implements PacketExtension {
    public final List<Element> extensionElements;
    private final List<FormField> fields;
    private List<String> instructions;
    public final List<Item> items;
    public ReportedData reportedData;
    public String title;
    public String type;

    public static class Item {
        private List<FormField> fields;

        public Item(List<FormField> fields) {
            this.fields = new ArrayList();
            this.fields = fields;
        }

        public final CharSequence toXML() {
            XmlStringBuilder buf = new XmlStringBuilder();
            buf.openElement("item");
            for (FormField field : Collections.unmodifiableList(new ArrayList(this.fields))) {
                buf.append(field.toXML());
            }
            buf.closeElement("item");
            return buf;
        }
    }

    public static class ReportedData {
        List<FormField> fields;

        public ReportedData(List<FormField> fields) {
            this.fields = new ArrayList();
            this.fields = fields;
        }
    }

    public DataForm(String type) {
        this.instructions = new ArrayList();
        this.items = new ArrayList();
        this.fields = new ArrayList();
        this.extensionElements = new ArrayList();
        this.type = type;
    }

    private List<String> getInstructions() {
        List<String> unmodifiableList;
        synchronized (this.instructions) {
            unmodifiableList = Collections.unmodifiableList(new ArrayList(this.instructions));
        }
        return unmodifiableList;
    }

    private List<Item> getItems() {
        List<Item> unmodifiableList;
        synchronized (this.items) {
            unmodifiableList = Collections.unmodifiableList(new ArrayList(this.items));
        }
        return unmodifiableList;
    }

    public final List<FormField> getFields() {
        List<FormField> unmodifiableList;
        synchronized (this.fields) {
            unmodifiableList = Collections.unmodifiableList(new ArrayList(this.fields));
        }
        return unmodifiableList;
    }

    public final String getElementName() {
        return "x";
    }

    public final String getNamespace() {
        return "jabber:x:data";
    }

    public final void addField(FormField field) {
        synchronized (this.fields) {
            this.fields.add(field);
        }
    }

    public final void addInstruction(String instruction) {
        synchronized (this.instructions) {
            this.instructions.add(instruction);
        }
    }

    public final boolean hasHiddenFormTypeField() {
        boolean found = false;
        for (FormField f : this.fields) {
            if (f.variable.equals("FORM_TYPE") && f.type != null && f.type.equals("hidden")) {
                found = true;
            }
        }
        return found;
    }

    public final XmlStringBuilder toXML() {
        XmlStringBuilder buf = new XmlStringBuilder((PacketExtension) this);
        buf.attribute("type", this.type);
        buf.rightAngleBracket();
        buf.optElement("title", this.title);
        for (String instruction : getInstructions()) {
            buf.element("instructions", instruction);
        }
        if (this.reportedData != null) {
            ReportedData reportedData = this.reportedData;
            CharSequence xmlStringBuilder = new XmlStringBuilder();
            xmlStringBuilder.openElement("reported");
            for (FormField toXML : Collections.unmodifiableList(new ArrayList(reportedData.fields))) {
                xmlStringBuilder.append(toXML.toXML());
            }
            xmlStringBuilder.closeElement("reported");
            buf.append(xmlStringBuilder);
        }
        for (Item item : getItems()) {
            buf.append(item.toXML());
        }
        for (FormField field : getFields()) {
            buf.append(field.toXML());
        }
        for (Element element : this.extensionElements) {
            buf.append(element.toXML());
        }
        buf.closeElement((NamedElement) this);
        return buf;
    }

    public static DataForm from(Packet packet) {
        return (DataForm) packet.getExtension("x", "jabber:x:data");
    }
}
