package org.jivesoftware.smackx.pubsub;

import org.jivesoftware.smackx.xdata.Form;

public final class FormNode extends NodeExtension {
    private Form configForm;

    public FormNode(FormNodeType formType, String nodeId, Form submitForm) {
        super(PubSubElementType.valueOf(formType.toString()), nodeId);
        this.configForm = submitForm;
    }

    public final CharSequence toXML() {
        if (this.configForm == null) {
            return super.toXML();
        }
        StringBuilder builder = new StringBuilder("<");
        builder.append(this.element.eName);
        if (this.node != null) {
            builder.append(" node='");
            builder.append(this.node);
            builder.append("'>");
        } else {
            builder.append('>');
        }
        builder.append(this.configForm.getDataFormToSend().toXML());
        builder.append("</");
        builder.append(this.element.eName + '>');
        return builder.toString();
    }
}
