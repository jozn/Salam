package org.jivesoftware.smackx.delay.provider;

import java.text.ParseException;
import java.util.Date;
import org.jxmpp.util.XmppDateTime;

public class LegacyDelayInformationProvider extends AbstractDelayInformationProvider {
    protected final Date parseDate(String string) throws ParseException {
        return XmppDateTime.parseDate(string);
    }
}
