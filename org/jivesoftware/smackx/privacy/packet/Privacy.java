package org.jivesoftware.smackx.privacy.packet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jivesoftware.smack.packet.IQ;

public class Privacy extends IQ {
    public String activeName;
    public boolean declineActiveList;
    public boolean declineDefaultList;
    public String defaultName;
    public Map<String, List<PrivacyItem>> itemLists;

    public Privacy() {
        this.declineActiveList = false;
        this.declineDefaultList = false;
        this.itemLists = new HashMap();
    }

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<query xmlns=\"jabber:iq:privacy\">");
        if (this.declineActiveList) {
            stringBuilder.append("<active/>");
        } else if (this.activeName != null) {
            stringBuilder.append("<active name=\"").append(this.activeName).append("\"/>");
        }
        if (this.declineDefaultList) {
            stringBuilder.append("<default/>");
        } else if (this.defaultName != null) {
            stringBuilder.append("<default name=\"").append(this.defaultName).append("\"/>");
        }
        for (Entry entry : this.itemLists.entrySet()) {
            String str = (String) entry.getKey();
            List<PrivacyItem> list = (List) entry.getValue();
            if (list.isEmpty()) {
                stringBuilder.append("<list name=\"").append(str).append("\"/>");
            } else {
                stringBuilder.append("<list name=\"").append(str).append("\">");
            }
            for (PrivacyItem privacyItem : list) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("<item");
                if (privacyItem.allow) {
                    stringBuilder2.append(" action=\"allow\"");
                } else {
                    stringBuilder2.append(" action=\"deny\"");
                }
                stringBuilder2.append(" order=\"").append(privacyItem.order).append("\"");
                if (privacyItem.type != null) {
                    stringBuilder2.append(" type=\"").append(privacyItem.type).append("\"");
                }
                if (privacyItem.value != null) {
                    stringBuilder2.append(" value=\"").append(privacyItem.value).append("\"");
                }
                Object obj = (privacyItem.filterIQ || privacyItem.filterMessage || privacyItem.filterPresenceIn || privacyItem.filterPresenceOut) ? null : 1;
                if (obj != null) {
                    stringBuilder2.append("/>");
                } else {
                    stringBuilder2.append(">");
                    if (privacyItem.filterIQ) {
                        stringBuilder2.append("<iq/>");
                    }
                    if (privacyItem.filterMessage) {
                        stringBuilder2.append("<message/>");
                    }
                    if (privacyItem.filterPresenceIn) {
                        stringBuilder2.append("<presence-in/>");
                    }
                    if (privacyItem.filterPresenceOut) {
                        stringBuilder2.append("<presence-out/>");
                    }
                    stringBuilder2.append("</item>");
                }
                stringBuilder.append(stringBuilder2.toString());
            }
            if (!list.isEmpty()) {
                stringBuilder.append("</list>");
            }
        }
        stringBuilder.append(getExtensionsXML());
        stringBuilder.append("</query>");
        return stringBuilder.toString();
    }
}
