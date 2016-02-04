package de.measite.minidns.record;

import de.measite.minidns.util.NameUtil;
import java.io.DataInputStream;
import java.io.IOException;

public final class SRV implements Data {
    protected String name;
    protected int port;
    protected int priority;
    protected int weight;

    public final int getPriority() {
        return this.priority;
    }

    public final int getWeight() {
        return this.weight;
    }

    public final int getPort() {
        return this.port;
    }

    public final String getName() {
        return this.name;
    }

    public final void parse$4e8e5594(DataInputStream dis, byte[] data) throws IOException {
        this.priority = dis.readUnsignedShort();
        this.weight = dis.readUnsignedShort();
        this.port = dis.readUnsignedShort();
        this.name = NameUtil.parse(dis, data);
    }

    public final String toString() {
        return "SRV " + this.name + ":" + this.port + " p:" + this.priority + " w:" + this.weight;
    }
}
