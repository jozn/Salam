package org.jivesoftware.smackx.bytestreams.socks5.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class Bytestream extends IQ {
    public Mode mode;
    public String sessionID;
    private final List<StreamHost> streamHosts;
    public Activate toActivate;
    public StreamHostUsed usedHost;

    /* renamed from: org.jivesoftware.smackx.bytestreams.socks5.packet.Bytestream.1 */
    static /* synthetic */ class C13031 {
        static final /* synthetic */ int[] $SwitchMap$org$jivesoftware$smack$packet$IQ$Type;

        static {
            $SwitchMap$org$jivesoftware$smack$packet$IQ$Type = new int[Type.values().length];
            try {
                $SwitchMap$org$jivesoftware$smack$packet$IQ$Type[Type.set.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$jivesoftware$smack$packet$IQ$Type[Type.result.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$jivesoftware$smack$packet$IQ$Type[Type.get.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public static class Activate implements NamedElement {
        public static String ELEMENTNAME;
        private final String target;

        static {
            ELEMENTNAME = "activate";
        }

        public Activate(String target) {
            this.target = target;
        }

        public final String getElementName() {
            return ELEMENTNAME;
        }

        public final XmlStringBuilder toXML() {
            XmlStringBuilder xml = new XmlStringBuilder((NamedElement) this);
            xml.rightAngleBracket();
            xml.escape(this.target);
            xml.closeElement((NamedElement) this);
            return xml;
        }
    }

    public enum Mode {
        tcp,
        udp;

        public static Mode fromName(String name) {
            try {
                return valueOf(name);
            } catch (Exception e) {
                return tcp;
            }
        }
    }

    public static class StreamHost implements NamedElement {
        public static String ELEMENTNAME;
        private final String JID;
        private final String addy;
        private final int port;

        static {
            ELEMENTNAME = "streamhost";
        }

        public StreamHost(String JID, String address, int port) {
            this.JID = JID;
            this.addy = address;
            this.port = port;
        }

        public final String getElementName() {
            return ELEMENTNAME;
        }

        public final XmlStringBuilder toXML() {
            XmlStringBuilder xml = new XmlStringBuilder((NamedElement) this);
            xml.attribute("jid", this.JID);
            xml.attribute("host", this.addy);
            if (this.port != 0) {
                xml.attribute("port", Integer.toString(this.port));
            } else {
                xml.attribute("zeroconf", "_jabber.bytestreams");
            }
            xml.closeEmptyElement();
            return xml;
        }
    }

    public static class StreamHostUsed implements NamedElement {
        public static String ELEMENTNAME;
        private final String JID;

        static {
            ELEMENTNAME = "streamhost-used";
        }

        public StreamHostUsed(String JID) {
            this.JID = JID;
        }

        public final String getElementName() {
            return ELEMENTNAME;
        }

        public final XmlStringBuilder toXML() {
            XmlStringBuilder xml = new XmlStringBuilder((NamedElement) this);
            xml.attribute("jid", this.JID);
            xml.closeEmptyElement();
            return xml;
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        XmlStringBuilder xmlStringBuilder = new XmlStringBuilder();
        xmlStringBuilder.halfOpenElement("query");
        xmlStringBuilder.xmlnsAttribute("http://jabber.org/protocol/bytestreams");
        switch (C13031.$SwitchMap$org$jivesoftware$smack$packet$IQ$Type[this.type.ordinal()]) {
            case Logger.SEVERE /*1*/:
                xmlStringBuilder.optAttribute("sid", this.sessionID);
                xmlStringBuilder.optAttribute("mode", this.mode);
                xmlStringBuilder.rightAngleBracket();
                if (this.toActivate != null) {
                    xmlStringBuilder.append(this.toActivate.toXML());
                    break;
                }
                for (StreamHost toXML : Collections.unmodifiableList(this.streamHosts)) {
                    xmlStringBuilder.append(toXML.toXML());
                }
                break;
            case Logger.WARNING /*2*/:
                xmlStringBuilder.rightAngleBracket();
                if (this.usedHost == null) {
                    if (this.streamHosts.size() > 0) {
                        for (StreamHost toXML2 : this.streamHosts) {
                            xmlStringBuilder.append(toXML2.toXML());
                        }
                        break;
                    }
                }
                xmlStringBuilder.append(this.usedHost.toXML());
                break;
                break;
            case Logger.INFO /*3*/:
                xmlStringBuilder.closeEmptyElement();
                return xmlStringBuilder;
            default:
                throw new IllegalStateException();
        }
        xmlStringBuilder.closeElement("query");
        return xmlStringBuilder;
    }

    public Bytestream() {
        this.mode = Mode.tcp;
        this.streamHosts = new ArrayList();
    }

    public final StreamHost addStreamHost(String JID, String address, int port) {
        StreamHost host = new StreamHost(JID, address, port);
        this.streamHosts.add(host);
        return host;
    }
}
