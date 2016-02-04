package org.jivesoftware.smack;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.PlainStreamElement;
import org.jivesoftware.smack.rosterstore.RosterStore;

public interface XMPPConnection {

    public enum FromMode {
        ;

        public static int[] values$3ec90f06() {
            return (int[]) $VALUES$7f4eafdb.clone();
        }

        static {
            UNCHANGED$569b0e80 = 1;
            OMITTED$569b0e80 = 2;
            USER$569b0e80 = 3;
            $VALUES$7f4eafdb = new int[]{UNCHANGED$569b0e80, OMITTED$569b0e80, USER$569b0e80};
        }
    }

    void addConnectionListener(ConnectionListener connectionListener);

    void addPacketInterceptor(PacketInterceptor packetInterceptor, PacketFilter packetFilter);

    void addPacketListener(PacketListener packetListener, PacketFilter packetFilter);

    void addPacketSendingListener(PacketListener packetListener, PacketFilter packetFilter);

    PacketCollector createPacketCollector(PacketFilter packetFilter);

    PacketCollector createPacketCollectorAndSend(IQ iq) throws NotConnectedException;

    int getConnectionCounter();

    <F extends PacketExtension> F getFeature(String str, String str2);

    long getLastStanzaReceived();

    long getPacketReplyTimeout();

    RosterStore getRosterStore();

    String getServiceName();

    String getUser();

    boolean hasFeature(String str, String str2);

    boolean isAnonymous();

    boolean isAuthenticated();

    boolean isRosterLoadedAtLogin();

    void removeConnectionListener(ConnectionListener connectionListener);

    void removePacketCollector(PacketCollector packetCollector);

    boolean removePacketListener(PacketListener packetListener);

    void send(PlainStreamElement plainStreamElement) throws NotConnectedException;

    void sendIqWithResponseCallback(IQ iq, PacketListener packetListener) throws NotConnectedException;

    void sendIqWithResponseCallback(IQ iq, PacketListener packetListener, ExceptionCallback exceptionCallback) throws NotConnectedException;

    void sendPacket(Packet packet) throws NotConnectedException;
}
