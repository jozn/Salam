package org.jivesoftware.smackx.sharedgroups.packet;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class SharedGroupsInfo extends IQ {
    List<String> groups;

    public static class Provider implements IQProvider {
        public final IQ parseIQ(XmlPullParser parser) throws Exception {
            SharedGroupsInfo groupsInfo = new SharedGroupsInfo();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && parser.getName().equals("group")) {
                    groupsInfo.groups.add(parser.nextText());
                } else if (eventType == 3 && parser.getName().equals("sharedgroup")) {
                    done = true;
                }
            }
            return groupsInfo;
        }
    }

    public SharedGroupsInfo() {
        this.groups = new ArrayList();
    }

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<sharedgroup xmlns=\"http://www.jivesoftware.org/protocol/sharedgroup\">");
        for (String append : this.groups) {
            stringBuilder.append("<group>").append(append).append("</group>");
        }
        stringBuilder.append("</sharedgroup>");
        return stringBuilder.toString();
    }
}
