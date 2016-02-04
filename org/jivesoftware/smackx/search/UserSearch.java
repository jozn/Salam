package org.jivesoftware.smackx.search;

import android.support.v7.appcompat.BuildConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smackx.search.ReportedData.Column;
import org.jivesoftware.smackx.search.ReportedData.Field;
import org.jivesoftware.smackx.search.ReportedData.Row;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.xmlpull.v1.XmlPullParser;

public class UserSearch extends IQ {

    public static class Provider implements IQProvider {
        public final IQ parseIQ(XmlPullParser parser) throws Exception {
            UserSearch search = null;
            SimpleUserSearch simpleUserSearch = new SimpleUserSearch();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && parser.getName().equals("instructions")) {
                    UserSearch.access$000(simpleUserSearch, parser.nextText(), parser);
                    return simpleUserSearch;
                } else if (eventType == 2 && parser.getName().equals("item")) {
                    ReportedData reportedData = new ReportedData();
                    reportedData.addColumn(new Column("JID", "jid", "text-single"));
                    List arrayList = new ArrayList();
                    Object obj = null;
                    while (obj == null) {
                        String attributeValue;
                        if (parser.getAttributeCount() > 0) {
                            attributeValue = parser.getAttributeValue(BuildConfig.VERSION_NAME, "jid");
                            List arrayList2 = new ArrayList();
                            arrayList2.add(attributeValue);
                            arrayList.add(new Field("jid", arrayList2));
                        }
                        int next = parser.next();
                        if (next == 2 && parser.getName().equals("item")) {
                            arrayList = new ArrayList();
                        } else if (next == 3 && parser.getName().equals("item")) {
                            reportedData.rows.add(new Row(arrayList));
                        } else if (next == 2) {
                            String name = parser.getName();
                            attributeValue = parser.nextText();
                            List arrayList3 = new ArrayList();
                            arrayList3.add(attributeValue);
                            arrayList.add(new Field(name, arrayList3));
                            for (Column column : Collections.unmodifiableList(new ArrayList(reportedData.columns))) {
                                if (column.variable.equals(name)) {
                                    r4 = 1;
                                    break;
                                }
                            }
                            r4 = null;
                            if (r4 == null) {
                                reportedData.addColumn(new Column(name, name, "text-single"));
                            }
                        } else {
                            if (next == 3 && parser.getName().equals("query")) {
                                r4 = 1;
                            } else {
                                r4 = obj;
                            }
                            obj = r4;
                        }
                    }
                    simpleUserSearch.data = reportedData;
                    return simpleUserSearch;
                } else if (eventType == 2 && parser.getNamespace().equals("jabber:x:data")) {
                    search = new UserSearch();
                    search.addExtension(PacketParserUtils.parsePacketExtension(parser.getName(), parser.getNamespace(), parser));
                } else if (eventType == 3 && parser.getName().equals("query")) {
                    done = true;
                }
            }
            if (search != null) {
                return search;
            }
            return simpleUserSearch;
        }
    }

    static /* synthetic */ void access$000(SimpleUserSearch x0, String x1, XmlPullParser x2) throws Exception {
        PacketExtension dataForm = new DataForm("form");
        Object obj = null;
        dataForm.title = "User Search";
        dataForm.addInstruction(x1);
        while (obj == null) {
            int next = x2.next();
            if (next == 2 && !x2.getNamespace().equals("jabber:x:data")) {
                String name = x2.getName();
                FormField formField = new FormField(name);
                if (name.equals("first")) {
                    formField.label = "First Name";
                } else if (name.equals("last")) {
                    formField.label = "Last Name";
                } else if (name.equals(NotificationCompatApi21.CATEGORY_EMAIL)) {
                    formField.label = "Email Address";
                } else if (name.equals("nick")) {
                    formField.label = "Nickname";
                }
                formField.type = "text-single";
                dataForm.addField(formField);
            } else if (next == 3) {
                if (x2.getName().equals("query")) {
                    obj = 1;
                }
            } else if (next == 2 && x2.getNamespace().equals("jabber:x:data")) {
                x0.addExtension(PacketParserUtils.parsePacketExtension(x2.getName(), x2.getNamespace(), x2));
                obj = 1;
            }
        }
        if (x0.getExtension("x", "jabber:x:data") == null) {
            x0.addExtension(dataForm);
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder();
        xmlStringBuilder.halfOpenElement("query");
        xmlStringBuilder.xmlnsAttribute("jabber:iq:search");
        xmlStringBuilder.rightAngleBracket();
        xmlStringBuilder.append(getExtensionsXML());
        xmlStringBuilder.closeElement("query");
        return xmlStringBuilder;
    }
}
