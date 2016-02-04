package com.shamchat.androidclient.util;

import com.shamchat.androidclient.exceptions.ZaminXMPPAdressMalformedException;
import gnu.inet.encoding.Stringprep;

public final class XMPPHelper {
    public static String verifyJabberID(String jid) throws ZaminXMPPAdressMalformedException {
        try {
            String[] parts = jid.split("@");
            if (parts.length != 2 || parts[0].length() == 0 || parts[1].length() == 0) {
                throw new ZaminXMPPAdressMalformedException("Configured Jabber-ID is incorrect!");
            }
            StringBuilder sb = new StringBuilder();
            sb.append(Stringprep.nodeprep(parts[0]));
            sb.append("@");
            sb.append(Stringprep.nameprep(parts[1]));
            return sb.toString();
        } catch (Throwable spe) {
            throw new ZaminXMPPAdressMalformedException(spe);
        } catch (NullPointerException e) {
            throw new ZaminXMPPAdressMalformedException("Jabber-ID wasn't set!");
        }
    }

    public static int tryToParseInt(String value, int defVal) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defVal;
        }
    }
}
