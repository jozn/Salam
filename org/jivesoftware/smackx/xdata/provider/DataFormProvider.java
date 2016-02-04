package org.jivesoftware.smackx.xdata.provider;

import android.support.v7.appcompat.BuildConfig;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.FormField.Option;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jivesoftware.smackx.xdata.packet.DataForm.Item;
import org.jivesoftware.smackx.xdata.packet.DataForm.ReportedData;
import org.xmlpull.v1.XmlPullParser;

public class DataFormProvider implements PacketExtensionProvider {
    public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        boolean done = false;
        DataForm dataForm = new DataForm(parser.getAttributeValue(BuildConfig.VERSION_NAME, "type"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("instructions")) {
                    dataForm.addInstruction(parser.nextText());
                } else if (parser.getName().equals("title")) {
                    dataForm.title = parser.nextText();
                } else if (parser.getName().equals("field")) {
                    dataForm.addField(parseField(parser));
                } else if (parser.getName().equals("item")) {
                    Object obj = null;
                    List arrayList = new ArrayList();
                    while (obj == null) {
                        int next = parser.next();
                        if (next == 2) {
                            if (parser.getName().equals("field")) {
                                arrayList.add(parseField(parser));
                            }
                        } else if (next == 3 && parser.getName().equals("item")) {
                            obj = 1;
                        }
                    }
                    Item item = new Item(arrayList);
                    synchronized (dataForm.items) {
                        dataForm.items.add(item);
                    }
                } else if (parser.getName().equals("reported")) {
                    dataForm.reportedData = parseReported(parser);
                } else if (parser.getName().equals("query") && parser.getNamespace().equals("jabber:iq:roster")) {
                    dataForm.extensionElements.add(PacketParserUtils.parseRoster(parser));
                }
            } else if (eventType == 3 && parser.getName().equals("x")) {
                done = true;
            }
        }
        return dataForm;
    }

    private static FormField parseField(XmlPullParser parser) throws Exception {
        boolean done = false;
        FormField formField = new FormField(parser.getAttributeValue(BuildConfig.VERSION_NAME, "var"));
        formField.label = parser.getAttributeValue(BuildConfig.VERSION_NAME, "label");
        formField.type = parser.getAttributeValue(BuildConfig.VERSION_NAME, "type");
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("desc")) {
                    formField.description = parser.nextText();
                } else if (parser.getName().equals("value")) {
                    formField.addValue(parser.nextText());
                } else if (parser.getName().equals("required")) {
                    formField.required = true;
                } else if (parser.getName().equals("option")) {
                    String attributeValue = parser.getAttributeValue(BuildConfig.VERSION_NAME, "label");
                    boolean z = false;
                    Object obj = null;
                    while (!z) {
                        int next = parser.next();
                        if (next == 2) {
                            if (parser.getName().equals("value")) {
                                obj = new Option(attributeValue, parser.nextText());
                            }
                        } else if (next == 3 && parser.getName().equals("option")) {
                            z = true;
                        }
                    }
                    synchronized (formField.options) {
                        formField.options.add(obj);
                    }
                } else {
                    continue;
                }
            } else if (eventType == 3 && parser.getName().equals("field")) {
                done = true;
            }
        }
        return formField;
    }

    private static ReportedData parseReported(XmlPullParser parser) throws Exception {
        boolean done = false;
        List<FormField> fields = new ArrayList();
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("field")) {
                    fields.add(parseField(parser));
                }
            } else if (eventType == 3 && parser.getName().equals("reported")) {
                done = true;
            }
        }
        return new ReportedData(fields);
    }
}
