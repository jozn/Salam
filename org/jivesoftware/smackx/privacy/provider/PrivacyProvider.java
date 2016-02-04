package org.jivesoftware.smackx.privacy.provider;

import android.support.v7.appcompat.BuildConfig;
import com.shamchat.activity.AddFavoriteTextActivity;
import java.util.ArrayList;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.privacy.packet.Privacy;
import org.jivesoftware.smackx.privacy.packet.PrivacyItem;
import org.jivesoftware.smackx.privacy.packet.PrivacyItem.Type;
import org.xmlpull.v1.XmlPullParser;

public class PrivacyProvider implements IQProvider {
    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        Privacy privacy = new Privacy();
        privacy.addExtension(new DefaultPacketExtension(parser.getName(), parser.getNamespace()));
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("active")) {
                    String activeName = parser.getAttributeValue(BuildConfig.VERSION_NAME, "name");
                    if (activeName == null) {
                        privacy.declineActiveList = true;
                    } else {
                        privacy.activeName = activeName;
                    }
                } else if (parser.getName().equals("default")) {
                    String defaultName = parser.getAttributeValue(BuildConfig.VERSION_NAME, "name");
                    if (defaultName == null) {
                        privacy.declineDefaultList = true;
                    } else {
                        privacy.defaultName = defaultName;
                    }
                } else if (parser.getName().equals("list")) {
                    boolean z = false;
                    String attributeValue = parser.getAttributeValue(BuildConfig.VERSION_NAME, "name");
                    ArrayList arrayList = new ArrayList();
                    while (!z) {
                        int next = parser.next();
                        if (next == 2) {
                            if (parser.getName().equals("item")) {
                                arrayList.add(parseItem(parser));
                            }
                        } else if (next == 3 && parser.getName().equals("list")) {
                            z = true;
                        }
                    }
                    privacy.itemLists.put(attributeValue, arrayList);
                }
            } else if (eventType == 3 && parser.getName().equals("query")) {
                done = true;
            }
        }
        return privacy;
    }

    private static PrivacyItem parseItem(XmlPullParser parser) throws Exception {
        boolean done = false;
        String actionValue = parser.getAttributeValue(BuildConfig.VERSION_NAME, "action");
        String orderValue = parser.getAttributeValue(BuildConfig.VERSION_NAME, "order");
        String type = parser.getAttributeValue(BuildConfig.VERSION_NAME, "type");
        boolean allow = true;
        if ("allow".equalsIgnoreCase(actionValue)) {
            allow = true;
        } else if ("deny".equalsIgnoreCase(actionValue)) {
            allow = false;
        }
        int order = Integer.parseInt(orderValue);
        if (type == null) {
            return new PrivacyItem(allow, order);
        }
        PrivacyItem item = new PrivacyItem(Type.valueOf(type), parser.getAttributeValue(BuildConfig.VERSION_NAME, "value"), allow, order);
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("iq")) {
                    item.filterIQ = true;
                }
                if (parser.getName().equals(AddFavoriteTextActivity.EXTRA_MESSAGE)) {
                    item.filterMessage = true;
                }
                if (parser.getName().equals("presence-in")) {
                    item.filterPresenceIn = true;
                }
                if (parser.getName().equals("presence-out")) {
                    item.filterPresenceOut = true;
                }
            } else if (eventType == 3 && parser.getName().equals("item")) {
                done = true;
            }
        }
        return item;
    }
}
