package org.jivesoftware.smackx.pubsub.provider;

import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.EmbeddedExtensionProvider;
import org.jivesoftware.smackx.pubsub.Affiliation;
import org.jivesoftware.smackx.pubsub.Affiliation.Type;

public class AffiliationProvider extends EmbeddedExtensionProvider {
    protected final PacketExtension createReturnExtension(String currentElement, String currentNamespace, Map<String, String> attributeMap, List<? extends PacketExtension> list) {
        return new Affiliation((String) attributeMap.get("node"), Type.valueOf((String) attributeMap.get("affiliation")));
    }
}