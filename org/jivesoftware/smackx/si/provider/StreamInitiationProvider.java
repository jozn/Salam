package org.jivesoftware.smackx.si.provider;

import android.support.v7.appcompat.BuildConfig;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.si.packet.StreamInitiation;
import org.jivesoftware.smackx.si.packet.StreamInitiation.Feature;
import org.jivesoftware.smackx.si.packet.StreamInitiation.File;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jivesoftware.smackx.xdata.provider.DataFormProvider;
import org.jxmpp.util.XmppDateTime;
import org.xmlpull.v1.XmlPullParser;

public class StreamInitiationProvider implements IQProvider {
    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(StreamInitiationProvider.class.getName());
    }

    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        boolean done = false;
        String id = parser.getAttributeValue(BuildConfig.VERSION_NAME, "id");
        String mimeType = parser.getAttributeValue(BuildConfig.VERSION_NAME, "mime-type");
        StreamInitiation initiation = new StreamInitiation();
        String name = null;
        String size = null;
        String hash = null;
        String date = null;
        String desc = null;
        boolean isRanged = false;
        DataForm form = null;
        DataFormProvider dataFormProvider = new DataFormProvider();
        while (!done) {
            int eventType = parser.next();
            String elementName = parser.getName();
            String namespace = parser.getNamespace();
            if (eventType == 2) {
                if (elementName.equals("file")) {
                    name = parser.getAttributeValue(BuildConfig.VERSION_NAME, "name");
                    size = parser.getAttributeValue(BuildConfig.VERSION_NAME, "size");
                    hash = parser.getAttributeValue(BuildConfig.VERSION_NAME, "hash");
                    date = parser.getAttributeValue(BuildConfig.VERSION_NAME, "date");
                } else {
                    if (elementName.equals("desc")) {
                        desc = parser.nextText();
                    } else {
                        if (elementName.equals("range")) {
                            isRanged = true;
                        } else {
                            if (elementName.equals("x")) {
                                if (namespace.equals("jabber:x:data")) {
                                    form = (DataForm) dataFormProvider.parseExtension(parser);
                                }
                            }
                        }
                    }
                }
            } else if (eventType == 3) {
                if (elementName.equals("si")) {
                    done = true;
                } else {
                    if (elementName.equals("file")) {
                        long fileSize = 0;
                        if (!(size == null || size.trim().length() == 0)) {
                            try {
                                fileSize = Long.parseLong(size);
                            } catch (NumberFormatException e) {
                                LOGGER.log(Level.SEVERE, "Failed to parse file size from 0", e);
                            }
                        }
                        Date fileDate = new Date();
                        if (date != null) {
                            try {
                                fileDate = XmppDateTime.parseDate(date);
                            } catch (ParseException e2) {
                            }
                        }
                        File file = new File(name, fileSize);
                        file.hash = hash;
                        file.date = fileDate;
                        file.desc = desc;
                        file.isRanged = isRanged;
                        initiation.file = file;
                    }
                }
            }
        }
        initiation.id = id;
        initiation.mimeType = mimeType;
        initiation.featureNegotiation = new Feature(form);
        return initiation;
    }
}
