package org.jivesoftware.smackx.pubsub;

import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.packet.DataForm;

public final class ConfigureForm extends Form {
    public ConfigureForm(DataForm configDataForm) {
        super(configDataForm);
    }

    public final String toString() {
        StringBuilder result = new StringBuilder(getClass().getName() + " Content [");
        for (FormField formField : this.dataForm.getFields()) {
            result.append('(');
            result.append(formField.variable);
            result.append(':');
            StringBuilder valuesBuilder = new StringBuilder();
            for (String value : formField.getValues()) {
                if (valuesBuilder.length() > 0) {
                    result.append(',');
                }
                valuesBuilder.append(value);
            }
            if (valuesBuilder.length() == 0) {
                valuesBuilder.append("NOT SET");
            }
            result.append(valuesBuilder);
            result.append(')');
        }
        result.append(']');
        return result.toString();
    }
}
