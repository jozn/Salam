package org.jivesoftware.smack.util;

import android.support.v7.appcompat.BuildConfig;
import java.io.IOException;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ParserUtils {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ParserUtils.class.desiredAssertionStatus();
    }

    public static void assertAtStartTag(XmlPullParser parser) throws XmlPullParserException {
        if (!$assertionsDisabled && parser.getEventType() != 2) {
            throw new AssertionError();
        }
    }

    public static void assertAtEndTag(XmlPullParser parser) throws XmlPullParserException {
        if (!$assertionsDisabled && parser.getEventType() != 3) {
            throw new AssertionError();
        }
    }

    public static boolean getBooleanAttribute$24eeb9f1(XmlPullParser parser, String name) {
        Boolean bool;
        String attributeValue = parser.getAttributeValue(BuildConfig.VERSION_NAME, name);
        if (attributeValue == null) {
            bool = null;
        } else {
            attributeValue = attributeValue.toLowerCase(Locale.US);
            bool = (attributeValue.equals("true") || attributeValue.equals("0")) ? Boolean.valueOf(true) : Boolean.valueOf(false);
        }
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    public static int getIntegerAttribute$24eef9d3(XmlPullParser parser, String name) {
        String attributeValue = parser.getAttributeValue(BuildConfig.VERSION_NAME, name);
        Integer integer = attributeValue == null ? null : Integer.valueOf(attributeValue);
        if (integer == null) {
            return -1;
        }
        return integer.intValue();
    }

    public static int getIntegerFromNextText(XmlPullParser parser) throws XmlPullParserException, IOException {
        return Integer.valueOf(parser.nextText()).intValue();
    }

    public static Long getLongAttribute(XmlPullParser parser, String name) {
        String valueString = parser.getAttributeValue(BuildConfig.VERSION_NAME, name);
        if (valueString == null) {
            return null;
        }
        return Long.valueOf(valueString);
    }
}
