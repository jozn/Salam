package org.jivesoftware.smackx.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.Manager;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition;
import org.jivesoftware.smackx.commands.packet.AdHocCommandData;
import org.jivesoftware.smackx.commands.packet.AdHocCommandData.SpecificError;
import org.jivesoftware.smackx.disco.NodeInformationProvider;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.disco.packet.DiscoverItems.Item;

public class AdHocCommandManager extends Manager {
    private static Map<XMPPConnection, AdHocCommandManager> instances;
    private final Map<String, AdHocCommandInfo> commands;
    private final Map<String, LocalCommand> executingCommands;
    private final ServiceDiscoveryManager serviceDiscoveryManager;
    private Thread sessionsSweeper;

    /* renamed from: org.jivesoftware.smackx.commands.AdHocCommandManager.1 */
    static class C13141 implements ConnectionCreationListener {
        C13141() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            AdHocCommandManager.getAddHocCommandsManager(connection);
        }
    }

    /* renamed from: org.jivesoftware.smackx.commands.AdHocCommandManager.2 */
    class C13152 implements NodeInformationProvider {
        C13152() {
        }

        public final List<Item> getNodeItems() {
            List<Item> answer = new ArrayList();
            for (AdHocCommandInfo info : AdHocCommandManager.this.commands.values()) {
                Item item = new Item(info.ownerJID);
                item.name = info.name;
                item.node = info.node;
                answer.add(item);
            }
            return answer;
        }

        public final List<String> getNodeFeatures() {
            return null;
        }

        public final List<Identity> getNodeIdentities() {
            return null;
        }

        public final List<PacketExtension> getNodePacketExtensions() {
            return null;
        }
    }

    /* renamed from: org.jivesoftware.smackx.commands.AdHocCommandManager.3 */
    class C13163 implements PacketListener {
        C13163() {
        }

        public final void processPacket(Packet packet) {
            try {
                AdHocCommandManager.access$100(AdHocCommandManager.this, (AdHocCommandData) packet);
            } catch (SmackException e) {
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.commands.AdHocCommandManager.6 */
    class C13176 implements Runnable {
        C13176() {
        }

        public final void run() {
            while (true) {
                for (String sessionId : AdHocCommandManager.this.executingCommands.keySet()) {
                    LocalCommand command = (LocalCommand) AdHocCommandManager.this.executingCommands.get(sessionId);
                    if (command != null) {
                        if (System.currentTimeMillis() - command.creationDate > 240000) {
                            AdHocCommandManager.this.executingCommands.remove(sessionId);
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private static class AdHocCommandInfo {
        LocalCommandFactory factory;
        String name;
        String node;
        String ownerJID;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static /* synthetic */ void access$100(org.jivesoftware.smackx.commands.AdHocCommandManager r10, org.jivesoftware.smackx.commands.packet.AdHocCommandData r11) throws org.jivesoftware.smack.SmackException {
        /*
        r1 = 1;
        r0 = r11.type;
        r2 = org.jivesoftware.smack.packet.IQ.Type.set;
        if (r0 != r2) goto L_0x002f;
    L_0x0007:
        r3 = new org.jivesoftware.smackx.commands.packet.AdHocCommandData;
        r3.<init>();
        r0 = r11.from;
        r3.to = r0;
        r0 = r11.packetID;
        r3.packetID = r0;
        r0 = r11.node;
        r3.node = r0;
        r0 = r11.to;
        r3.id = r0;
        r4 = r11.sessionID;
        r0 = r11.node;
        if (r4 != 0) goto L_0x00ca;
    L_0x0022:
        r1 = r10.commands;
        r1 = r1.containsKey(r0);
        if (r1 != 0) goto L_0x0030;
    L_0x002a:
        r0 = org.jivesoftware.smack.packet.XMPPError.Condition.item_not_found;
        r10.respondError(r3, r0);
    L_0x002f:
        return;
    L_0x0030:
        r1 = 15;
        r1 = org.jivesoftware.smack.util.StringUtils.randomString(r1);
        r0 = r10.newInstanceOfCmd(r0, r1);	 Catch:{ XMPPErrorException -> 0x0050 }
        r2 = org.jivesoftware.smack.packet.IQ.Type.result;	 Catch:{ XMPPErrorException -> 0x0050 }
        r3.setType(r2);	 Catch:{ XMPPErrorException -> 0x0050 }
        r0.setData(r3);	 Catch:{ XMPPErrorException -> 0x0050 }
        r2 = r11.from;	 Catch:{ XMPPErrorException -> 0x0050 }
        r2 = r0.hasPermission$552c4dfd();	 Catch:{ XMPPErrorException -> 0x0050 }
        if (r2 != 0) goto L_0x006a;
    L_0x004a:
        r0 = org.jivesoftware.smack.packet.XMPPError.Condition.forbidden;	 Catch:{ XMPPErrorException -> 0x0050 }
        r10.respondError(r3, r0);	 Catch:{ XMPPErrorException -> 0x0050 }
        goto L_0x002f;
    L_0x0050:
        r0 = move-exception;
        r0 = r0.error;
        r2 = org.jivesoftware.smack.packet.XMPPError.Type.CANCEL;
        r4 = r0.type;
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0066;
    L_0x005d:
        r2 = org.jivesoftware.smackx.commands.AdHocCommand.Status.canceled;
        r3.status = r2;
        r2 = r10.executingCommands;
        r2.remove(r1);
    L_0x0066:
        r10.respondError(r3, r0);
        goto L_0x002f;
    L_0x006a:
        r2 = r11.action;	 Catch:{ XMPPErrorException -> 0x0050 }
        if (r2 == 0) goto L_0x007e;
    L_0x006e:
        r4 = org.jivesoftware.smackx.commands.AdHocCommand.Action.unknown;	 Catch:{ XMPPErrorException -> 0x0050 }
        r4 = r2.equals(r4);	 Catch:{ XMPPErrorException -> 0x0050 }
        if (r4 == 0) goto L_0x007e;
    L_0x0076:
        r0 = org.jivesoftware.smack.packet.XMPPError.Condition.bad_request;	 Catch:{ XMPPErrorException -> 0x0050 }
        r2 = org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition.malformedAction;	 Catch:{ XMPPErrorException -> 0x0050 }
        r10.respondError(r3, r0, r2);	 Catch:{ XMPPErrorException -> 0x0050 }
        goto L_0x002f;
    L_0x007e:
        if (r2 == 0) goto L_0x0090;
    L_0x0080:
        r4 = org.jivesoftware.smackx.commands.AdHocCommand.Action.execute;	 Catch:{ XMPPErrorException -> 0x0050 }
        r2 = r2.equals(r4);	 Catch:{ XMPPErrorException -> 0x0050 }
        if (r2 != 0) goto L_0x0090;
    L_0x0088:
        r0 = org.jivesoftware.smack.packet.XMPPError.Condition.bad_request;	 Catch:{ XMPPErrorException -> 0x0050 }
        r2 = org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition.badAction;	 Catch:{ XMPPErrorException -> 0x0050 }
        r10.respondError(r3, r0, r2);	 Catch:{ XMPPErrorException -> 0x0050 }
        goto L_0x002f;
    L_0x0090:
        r0.incrementStage();	 Catch:{ XMPPErrorException -> 0x0050 }
        r2 = r0.isLastStage();	 Catch:{ XMPPErrorException -> 0x0050 }
        if (r2 == 0) goto L_0x00a5;
    L_0x0099:
        r0 = org.jivesoftware.smackx.commands.AdHocCommand.Status.completed;	 Catch:{ XMPPErrorException -> 0x0050 }
        r3.status = r0;	 Catch:{ XMPPErrorException -> 0x0050 }
    L_0x009d:
        r0 = r10.connection();	 Catch:{ XMPPErrorException -> 0x0050 }
        r0.sendPacket(r3);	 Catch:{ XMPPErrorException -> 0x0050 }
        goto L_0x002f;
    L_0x00a5:
        r2 = org.jivesoftware.smackx.commands.AdHocCommand.Status.executing;	 Catch:{ XMPPErrorException -> 0x0050 }
        r3.status = r2;	 Catch:{ XMPPErrorException -> 0x0050 }
        r2 = r10.executingCommands;	 Catch:{ XMPPErrorException -> 0x0050 }
        r2.put(r1, r0);	 Catch:{ XMPPErrorException -> 0x0050 }
        r0 = r10.sessionsSweeper;	 Catch:{ XMPPErrorException -> 0x0050 }
        if (r0 != 0) goto L_0x009d;
    L_0x00b2:
        r0 = new java.lang.Thread;	 Catch:{ XMPPErrorException -> 0x0050 }
        r2 = new org.jivesoftware.smackx.commands.AdHocCommandManager$6;	 Catch:{ XMPPErrorException -> 0x0050 }
        r2.<init>();	 Catch:{ XMPPErrorException -> 0x0050 }
        r0.<init>(r2);	 Catch:{ XMPPErrorException -> 0x0050 }
        r10.sessionsSweeper = r0;	 Catch:{ XMPPErrorException -> 0x0050 }
        r0 = r10.sessionsSweeper;	 Catch:{ XMPPErrorException -> 0x0050 }
        r2 = 1;
        r0.setDaemon(r2);	 Catch:{ XMPPErrorException -> 0x0050 }
        r0 = r10.sessionsSweeper;	 Catch:{ XMPPErrorException -> 0x0050 }
        r0.start();	 Catch:{ XMPPErrorException -> 0x0050 }
        goto L_0x009d;
    L_0x00ca:
        r0 = r10.executingCommands;
        r0 = r0.get(r4);
        r0 = (org.jivesoftware.smackx.commands.LocalCommand) r0;
        if (r0 != 0) goto L_0x00dd;
    L_0x00d4:
        r0 = org.jivesoftware.smack.packet.XMPPError.Condition.bad_request;
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition.badSessionid;
        r10.respondError(r3, r0, r1);
        goto L_0x002f;
    L_0x00dd:
        r6 = r0.creationDate;
        r8 = java.lang.System.currentTimeMillis();
        r6 = r8 - r6;
        r8 = 120000; // 0x1d4c0 float:1.68156E-40 double:5.9288E-319;
        r2 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r2 <= 0) goto L_0x00fa;
    L_0x00ec:
        r0 = r10.executingCommands;
        r0.remove(r4);
        r0 = org.jivesoftware.smack.packet.XMPPError.Condition.not_allowed;
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition.sessionExpired;
        r10.respondError(r3, r0, r1);
        goto L_0x002f;
    L_0x00fa:
        monitor-enter(r0);
        r2 = r11.action;	 Catch:{ all -> 0x0111 }
        if (r2 == 0) goto L_0x0114;
    L_0x00ff:
        r5 = org.jivesoftware.smackx.commands.AdHocCommand.Action.unknown;	 Catch:{ all -> 0x0111 }
        r5 = r2.equals(r5);	 Catch:{ all -> 0x0111 }
        if (r5 == 0) goto L_0x0114;
    L_0x0107:
        r1 = org.jivesoftware.smack.packet.XMPPError.Condition.bad_request;	 Catch:{ all -> 0x0111 }
        r2 = org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition.malformedAction;	 Catch:{ all -> 0x0111 }
        r10.respondError(r3, r1, r2);	 Catch:{ all -> 0x0111 }
        monitor-exit(r0);	 Catch:{ all -> 0x0111 }
        goto L_0x002f;
    L_0x0111:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0111 }
        throw r1;
    L_0x0114:
        if (r2 == 0) goto L_0x011e;
    L_0x0116:
        r5 = org.jivesoftware.smackx.commands.AdHocCommand.Action.execute;	 Catch:{ all -> 0x0111 }
        r5 = r5.equals(r2);	 Catch:{ all -> 0x0111 }
        if (r5 == 0) goto L_0x0122;
    L_0x011e:
        r2 = r0.data;	 Catch:{ all -> 0x0111 }
        r2 = r2.executeAction;	 Catch:{ all -> 0x0111 }
    L_0x0122:
        r5 = r0.data;	 Catch:{ all -> 0x0111 }
        r5 = r5.actions;	 Catch:{ all -> 0x0111 }
        r5 = r5.contains(r2);	 Catch:{ all -> 0x0111 }
        if (r5 != 0) goto L_0x0134;
    L_0x012c:
        r5 = org.jivesoftware.smackx.commands.AdHocCommand.Action.cancel;	 Catch:{ all -> 0x0111 }
        r5 = r5.equals(r2);	 Catch:{ all -> 0x0111 }
        if (r5 == 0) goto L_0x0140;
    L_0x0134:
        if (r1 != 0) goto L_0x0142;
    L_0x0136:
        r1 = org.jivesoftware.smack.packet.XMPPError.Condition.bad_request;	 Catch:{ all -> 0x0111 }
        r2 = org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition.badAction;	 Catch:{ all -> 0x0111 }
        r10.respondError(r3, r1, r2);	 Catch:{ all -> 0x0111 }
        monitor-exit(r0);	 Catch:{ all -> 0x0111 }
        goto L_0x002f;
    L_0x0140:
        r1 = 0;
        goto L_0x0134;
    L_0x0142:
        r1 = org.jivesoftware.smack.packet.IQ.Type.result;	 Catch:{ XMPPErrorException -> 0x0175 }
        r3.setType(r1);	 Catch:{ XMPPErrorException -> 0x0175 }
        r0.setData(r3);	 Catch:{ XMPPErrorException -> 0x0175 }
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.Action.next;	 Catch:{ XMPPErrorException -> 0x0175 }
        r1 = r1.equals(r2);	 Catch:{ XMPPErrorException -> 0x0175 }
        if (r1 == 0) goto L_0x018f;
    L_0x0152:
        r0.incrementStage();	 Catch:{ XMPPErrorException -> 0x0175 }
        r1 = new org.jivesoftware.smackx.xdata.Form;	 Catch:{ XMPPErrorException -> 0x0175 }
        r2 = r11.form;	 Catch:{ XMPPErrorException -> 0x0175 }
        r1.<init>(r2);	 Catch:{ XMPPErrorException -> 0x0175 }
        r1 = r0.isLastStage();	 Catch:{ XMPPErrorException -> 0x0175 }
        if (r1 == 0) goto L_0x0170;
    L_0x0162:
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.Status.completed;	 Catch:{ XMPPErrorException -> 0x0175 }
        r3.status = r1;	 Catch:{ XMPPErrorException -> 0x0175 }
    L_0x0166:
        r1 = r10.connection();	 Catch:{ XMPPErrorException -> 0x0175 }
        r1.sendPacket(r3);	 Catch:{ XMPPErrorException -> 0x0175 }
    L_0x016d:
        monitor-exit(r0);	 Catch:{ all -> 0x0111 }
        goto L_0x002f;
    L_0x0170:
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.Status.executing;	 Catch:{ XMPPErrorException -> 0x0175 }
        r3.status = r1;	 Catch:{ XMPPErrorException -> 0x0175 }
        goto L_0x0166;
    L_0x0175:
        r1 = move-exception;
        r1 = r1.error;	 Catch:{ all -> 0x0111 }
        r2 = org.jivesoftware.smack.packet.XMPPError.Type.CANCEL;	 Catch:{ all -> 0x0111 }
        r5 = r1.type;	 Catch:{ all -> 0x0111 }
        r2 = r2.equals(r5);	 Catch:{ all -> 0x0111 }
        if (r2 == 0) goto L_0x018b;
    L_0x0182:
        r2 = org.jivesoftware.smackx.commands.AdHocCommand.Status.canceled;	 Catch:{ all -> 0x0111 }
        r3.status = r2;	 Catch:{ all -> 0x0111 }
        r2 = r10.executingCommands;	 Catch:{ all -> 0x0111 }
        r2.remove(r4);	 Catch:{ all -> 0x0111 }
    L_0x018b:
        r10.respondError(r3, r1);	 Catch:{ all -> 0x0111 }
        goto L_0x016d;
    L_0x018f:
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.Action.complete;	 Catch:{ XMPPErrorException -> 0x0175 }
        r1 = r1.equals(r2);	 Catch:{ XMPPErrorException -> 0x0175 }
        if (r1 == 0) goto L_0x01ab;
    L_0x0197:
        r0.incrementStage();	 Catch:{ XMPPErrorException -> 0x0175 }
        r1 = new org.jivesoftware.smackx.xdata.Form;	 Catch:{ XMPPErrorException -> 0x0175 }
        r2 = r11.form;	 Catch:{ XMPPErrorException -> 0x0175 }
        r1.<init>(r2);	 Catch:{ XMPPErrorException -> 0x0175 }
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.Status.completed;	 Catch:{ XMPPErrorException -> 0x0175 }
        r3.status = r1;	 Catch:{ XMPPErrorException -> 0x0175 }
        r1 = r10.executingCommands;	 Catch:{ XMPPErrorException -> 0x0175 }
        r1.remove(r4);	 Catch:{ XMPPErrorException -> 0x0175 }
        goto L_0x0166;
    L_0x01ab:
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.Action.prev;	 Catch:{ XMPPErrorException -> 0x0175 }
        r1 = r1.equals(r2);	 Catch:{ XMPPErrorException -> 0x0175 }
        if (r1 == 0) goto L_0x01ba;
    L_0x01b3:
        r1 = r0.currenStage;	 Catch:{ XMPPErrorException -> 0x0175 }
        r1 = r1 + -1;
        r0.currenStage = r1;	 Catch:{ XMPPErrorException -> 0x0175 }
        goto L_0x0166;
    L_0x01ba:
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.Action.cancel;	 Catch:{ XMPPErrorException -> 0x0175 }
        r1 = r1.equals(r2);	 Catch:{ XMPPErrorException -> 0x0175 }
        if (r1 == 0) goto L_0x0166;
    L_0x01c2:
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.Status.canceled;	 Catch:{ XMPPErrorException -> 0x0175 }
        r3.status = r1;	 Catch:{ XMPPErrorException -> 0x0175 }
        r1 = r10.executingCommands;	 Catch:{ XMPPErrorException -> 0x0175 }
        r1.remove(r4);	 Catch:{ XMPPErrorException -> 0x0175 }
        goto L_0x0166;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smackx.commands.AdHocCommandManager.access$100(org.jivesoftware.smackx.commands.AdHocCommandManager, org.jivesoftware.smackx.commands.packet.AdHocCommandData):void");
    }

    static {
        instances = Collections.synchronizedMap(new WeakHashMap());
        XMPPConnectionRegistry.addConnectionCreationListener(new C13141());
    }

    public static synchronized AdHocCommandManager getAddHocCommandsManager(XMPPConnection connection) {
        AdHocCommandManager ahcm;
        synchronized (AdHocCommandManager.class) {
            ahcm = (AdHocCommandManager) instances.get(connection);
            if (ahcm == null) {
                ahcm = new AdHocCommandManager(connection);
            }
        }
        return ahcm;
    }

    private AdHocCommandManager(XMPPConnection connection) {
        super(connection);
        this.commands = new ConcurrentHashMap();
        this.executingCommands = new ConcurrentHashMap();
        this.serviceDiscoveryManager = ServiceDiscoveryManager.getInstanceFor(connection);
        instances.put(connection, this);
        ServiceDiscoveryManager.getInstanceFor(connection).addFeature("http://jabber.org/protocol/commands");
        ServiceDiscoveryManager.getInstanceFor(connection).setNodeInformationProvider("http://jabber.org/protocol/commands", new C13152());
        connection.addPacketListener(new C13163(), new PacketTypeFilter(AdHocCommandData.class));
        this.sessionsSweeper = null;
    }

    private void respondError(AdHocCommandData response, Condition condition) throws NotConnectedException {
        respondError(response, new XMPPError(condition));
    }

    private void respondError(AdHocCommandData response, Condition condition, SpecificErrorCondition specificCondition) throws NotConnectedException {
        XMPPError error = new XMPPError(condition);
        error.addExtension(new SpecificError(specificCondition));
        respondError(response, error);
    }

    private void respondError(AdHocCommandData response, XMPPError error) throws NotConnectedException {
        response.setType(Type.error);
        response.error = error;
        connection().sendPacket(response);
    }

    private LocalCommand newInstanceOfCmd(String commandNode, String sessionID) throws XMPPErrorException {
        AdHocCommandInfo commandInfo = (AdHocCommandInfo) this.commands.get(commandNode);
        try {
            LocalCommand command = commandInfo.factory.getInstance();
            command.sessionID = sessionID;
            command.data.setSessionID(sessionID);
            command.data.name = commandInfo.name;
            command.data.node = commandInfo.node;
            return command;
        } catch (InstantiationException e) {
            throw new XMPPErrorException(new XMPPError(Condition.internal_server_error));
        } catch (IllegalAccessException e2) {
            throw new XMPPErrorException(new XMPPError(Condition.internal_server_error));
        }
    }
}
