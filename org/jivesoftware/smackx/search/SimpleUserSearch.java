package org.jivesoftware.smackx.search;

import android.support.v7.appcompat.BuildConfig;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;

class SimpleUserSearch extends IQ {
    ReportedData data;
    private Form form;

    SimpleUserSearch() {
    }

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<query xmlns=\"jabber:iq:search\">");
        stringBuilder.append(getItemsToSearch());
        stringBuilder.append("</query>");
        return stringBuilder.toString();
    }

    private String getItemsToSearch() {
        StringBuilder buf = new StringBuilder();
        if (this.form == null) {
            this.form = Form.getFormFrom(this);
        }
        if (this.form == null) {
            return BuildConfig.VERSION_NAME;
        }
        for (FormField field : this.form.dataForm.getFields()) {
            String name = field.variable;
            List values = field.getValues();
            String value = values.isEmpty() ? BuildConfig.VERSION_NAME : (String) values.get(0);
            if (value.trim().length() > 0) {
                buf.append("<").append(name).append(">").append(value).append("</").append(name).append(">");
            }
        }
        return buf.toString();
    }
}
