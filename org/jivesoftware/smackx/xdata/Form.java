package org.jivesoftware.smackx.xdata;

import android.support.v7.appcompat.BuildConfig;
import java.util.List;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.xdata.packet.DataForm;

public class Form {
    public DataForm dataForm;

    public static Form getFormFrom(Packet packet) {
        DataForm dataForm = DataForm.from(packet);
        if (dataForm == null || dataForm.reportedData != null) {
            return null;
        }
        return new Form(dataForm);
    }

    public Form(DataForm dataForm) {
        this.dataForm = dataForm;
    }

    public Form(String type) {
        this.dataForm = new DataForm(type);
    }

    public final void setAnswer(FormField field, Object value) {
        if (isSubmitType()) {
            field.resetValues();
            field.addValue(value.toString());
            return;
        }
        throw new IllegalStateException("Cannot set an answer if the form is not of type \"submit\"");
    }

    public final void setAnswer(String variable, List<String> values) {
        if (isSubmitType()) {
            FormField field = getField(variable);
            if (field == null) {
                throw new IllegalArgumentException("Couldn't find a field for the specified variable.");
            } else if ("jid-multi".equals(field.type) || "list-multi".equals(field.type) || "list-single".equals(field.type) || "text-multi".equals(field.type) || "hidden".equals(field.type)) {
                field.resetValues();
                synchronized (field.values) {
                    field.values.addAll(values);
                }
                return;
            } else {
                throw new IllegalArgumentException("This field only accept list of values.");
            }
        }
        throw new IllegalStateException("Cannot set an answer if the form is not of type \"submit\"");
    }

    public final void setDefaultAnswer(String variable) {
        if (isSubmitType()) {
            FormField field = getField(variable);
            if (field != null) {
                field.resetValues();
                for (String value : field.getValues()) {
                    field.addValue(value);
                }
                return;
            }
            throw new IllegalArgumentException("Couldn't find a field for the specified variable.");
        }
        throw new IllegalStateException("Cannot set an answer if the form is not of type \"submit\"");
    }

    public final FormField getField(String variable) {
        if (variable == null || variable.equals(BuildConfig.VERSION_NAME)) {
            throw new IllegalArgumentException("Variable must not be null or blank.");
        }
        for (FormField field : this.dataForm.getFields()) {
            if (variable.equals(field.variable)) {
                return field;
            }
        }
        return null;
    }

    public final DataForm getDataFormToSend() {
        if (!isSubmitType()) {
            return this.dataForm;
        }
        DataForm dataForm = new DataForm(this.dataForm.type);
        for (FormField field : this.dataForm.getFields()) {
            if (!field.getValues().isEmpty()) {
                dataForm.addField(field);
            }
        }
        return dataForm;
    }

    private boolean isSubmitType() {
        return "submit".equals(this.dataForm.type);
    }
}
