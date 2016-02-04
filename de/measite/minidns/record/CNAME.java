package de.measite.minidns.record;

import de.measite.minidns.util.NameUtil;
import java.io.DataInputStream;
import java.io.IOException;

public class CNAME implements Data {
    protected String name;

    public final void parse$4e8e5594(DataInputStream dis, byte[] data) throws IOException {
        this.name = NameUtil.parse(dis, data);
    }

    public String toString() {
        return "to \"" + this.name + "\"";
    }
}
