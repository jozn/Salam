package com.shamchat.androidclient.chat.extension;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.TimeZone;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;

public final class ProviderUtils {
    private static final DateFormat XEP_0082_UTC_FORMAT_WITHOUT_MILLIS;
    private static final Pattern pattern;

    static {
        pattern = Pattern.compile("^(\\d+-\\d+-\\d+T\\d+:\\d+:\\d+\\.\\d{1,3})\\d+(Z)$");
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        XEP_0082_UTC_FORMAT_WITHOUT_MILLIS = simpleDateFormat;
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static void skipTag(XmlPullParser parser) throws IllegalStateException, Exception {
        LinkedList<String> tags = new LinkedList();
        tags.add(parser.getName());
        while (!tags.isEmpty()) {
            int eventType = parser.next();
            if (eventType == 2) {
                tags.add(parser.getName());
            } else if (eventType == 3) {
                if (!((String) tags.removeLast()).equals(parser.getName())) {
                    throw new IllegalStateException();
                }
            } else if (eventType == 1) {
                return;
            }
        }
    }
}
