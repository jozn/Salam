package org.jivesoftware.smack.tcp.sm.packet;

import org.jivesoftware.smack.packet.FullStreamElement;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.util.XmlStringBuilder;

public final class StreamManagement {

    private static abstract class AbstractEnable extends FullStreamElement {
        protected int max;
        protected boolean resume;

        private AbstractEnable() {
            this.max = -1;
            this.resume = false;
        }

        protected final void maybeAddResumeAttributeTo(XmlStringBuilder xml) {
            if (this.resume) {
                xml.attribute("resume", "true");
            }
        }

        protected final void maybeAddMaxAttributeTo(XmlStringBuilder xml) {
            if (this.max > 0) {
                xml.attribute("max", Integer.toString(this.max));
            }
        }

        public boolean isResumeSet() {
            return this.resume;
        }

        public int getMaxResumptionTime() {
            return this.max;
        }

        public final String getNamespace() {
            return "urn:xmpp:sm:3";
        }
    }

    private static abstract class AbstractResume extends FullStreamElement {
        private final long handledCount;
        private final String previd;

        public final /* bridge */ /* synthetic */ CharSequence toXML() {
            CharSequence xmlStringBuilder = new XmlStringBuilder((PacketExtension) this);
            xmlStringBuilder.attribute("h", Long.toString(this.handledCount));
            xmlStringBuilder.attribute("previd", this.previd);
            xmlStringBuilder.closeEmptyElement();
            return xmlStringBuilder;
        }

        public AbstractResume(long handledCount, String previd) {
            this.handledCount = handledCount;
            this.previd = previd;
        }

        public long getHandledCount() {
            return this.handledCount;
        }

        public String getPrevId() {
            return this.previd;
        }

        public final String getNamespace() {
            return "urn:xmpp:sm:3";
        }
    }

    public static class AckAnswer extends FullStreamElement {
        public final long handledCount;

        public AckAnswer(long handledCount) {
            this.handledCount = handledCount;
        }

        public final CharSequence toXML() {
            XmlStringBuilder xml = new XmlStringBuilder((PacketExtension) this);
            xml.attribute("h", Long.toString(this.handledCount));
            xml.closeEmptyElement();
            return xml;
        }

        public final String getNamespace() {
            return "urn:xmpp:sm:3";
        }

        public final String getElementName() {
            return "a";
        }
    }

    public static class AckRequest extends FullStreamElement {
        public static final AckRequest INSTANCE;

        static {
            INSTANCE = new AckRequest();
        }

        private AckRequest() {
        }

        public final CharSequence toXML() {
            return "<r xmlns='urn:xmpp:sm:3'/>";
        }

        public final String getNamespace() {
            return "urn:xmpp:sm:3";
        }

        public final String getElementName() {
            return "r";
        }
    }

    public static class Enable extends AbstractEnable {
        public static final Enable INSTANCE;

        public final /* bridge */ /* synthetic */ int getMaxResumptionTime() {
            return super.getMaxResumptionTime();
        }

        public final /* bridge */ /* synthetic */ boolean isResumeSet() {
            return super.isResumeSet();
        }

        static {
            INSTANCE = new Enable();
        }

        private Enable() {
            super();
        }

        private Enable(boolean resume) {
            super();
            this.resume = resume;
        }

        public Enable(boolean resume, int max) {
            this(resume);
            this.max = max;
        }

        public final CharSequence toXML() {
            XmlStringBuilder xml = new XmlStringBuilder((PacketExtension) this);
            maybeAddResumeAttributeTo(xml);
            maybeAddMaxAttributeTo(xml);
            xml.closeEmptyElement();
            return xml;
        }

        public final String getElementName() {
            return "enable";
        }
    }

    public static class Enabled extends AbstractEnable {
        public final String id;
        private final String location;

        public final /* bridge */ /* synthetic */ int getMaxResumptionTime() {
            return super.getMaxResumptionTime();
        }

        public final /* bridge */ /* synthetic */ boolean isResumeSet() {
            return super.isResumeSet();
        }

        public Enabled(String id, boolean resume, String location, int max) {
            super();
            this.id = id;
            this.resume = resume;
            this.location = location;
            this.max = max;
        }

        public final CharSequence toXML() {
            XmlStringBuilder xml = new XmlStringBuilder((PacketExtension) this);
            xml.optAttribute("id", this.id);
            maybeAddResumeAttributeTo(xml);
            xml.optAttribute("location", this.location);
            maybeAddMaxAttributeTo(xml);
            xml.closeEmptyElement();
            return xml;
        }

        public final String getElementName() {
            return "enabled";
        }
    }

    public static class Failed extends FullStreamElement {
        public XMPPError error;

        public Failed(XMPPError error) {
            this.error = error;
        }

        public final CharSequence toXML() {
            XmlStringBuilder xml = new XmlStringBuilder((PacketExtension) this);
            if (this.error != null) {
                xml.rightAngleBracket();
                xml.append(this.error.toXML());
                xml.closeElement("failed");
            } else {
                xml.closeEmptyElement();
            }
            return xml;
        }

        public final String getNamespace() {
            return "urn:xmpp:sm:3";
        }

        public final String getElementName() {
            return "failed";
        }
    }

    public static class Resume extends AbstractResume {
        public final /* bridge */ /* synthetic */ long getHandledCount() {
            return super.getHandledCount();
        }

        public final /* bridge */ /* synthetic */ String getPrevId() {
            return super.getPrevId();
        }

        public Resume(long handledCount, String previd) {
            super(handledCount, previd);
        }

        public final String getElementName() {
            return "resume";
        }
    }

    public static class Resumed extends AbstractResume {
        public final /* bridge */ /* synthetic */ long getHandledCount() {
            return super.getHandledCount();
        }

        public final /* bridge */ /* synthetic */ String getPrevId() {
            return super.getPrevId();
        }

        public Resumed(long handledCount, String previd) {
            super(handledCount, previd);
        }

        public final String getElementName() {
            return "resumed";
        }
    }

    public static class StreamManagementFeature implements PacketExtension {
        public static final StreamManagementFeature INSTANCE;

        static {
            INSTANCE = new StreamManagementFeature();
        }

        private StreamManagementFeature() {
        }

        public final String getElementName() {
            return "sm";
        }

        public final String getNamespace() {
            return "urn:xmpp:sm:3";
        }

        public final CharSequence toXML() {
            XmlStringBuilder xml = new XmlStringBuilder((PacketExtension) this);
            xml.closeEmptyElement();
            return xml;
        }
    }
}
