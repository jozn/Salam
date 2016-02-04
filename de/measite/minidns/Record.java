package de.measite.minidns;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.C0170R;
import de.measite.minidns.record.AAAA;
import de.measite.minidns.record.C1256A;
import de.measite.minidns.record.CNAME;
import de.measite.minidns.record.Data;
import de.measite.minidns.record.NS;
import de.measite.minidns.record.SRV;
import de.measite.minidns.util.NameUtil;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class Record {
    protected CLASS clazz;
    protected String name;
    protected Data payloadData;
    protected long ttl;
    protected TYPE type;

    /* renamed from: de.measite.minidns.Record.1 */
    static /* synthetic */ class C12551 {
        static final /* synthetic */ int[] $SwitchMap$de$measite$minidns$Record$TYPE;

        static {
            $SwitchMap$de$measite$minidns$Record$TYPE = new int[TYPE.values().length];
            try {
                $SwitchMap$de$measite$minidns$Record$TYPE[TYPE.SRV.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$de$measite$minidns$Record$TYPE[TYPE.AAAA.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$de$measite$minidns$Record$TYPE[TYPE.A.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$de$measite$minidns$Record$TYPE[TYPE.NS.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$de$measite$minidns$Record$TYPE[TYPE.CNAME.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public enum CLASS {
        IN(1),
        CH(3),
        HS(4),
        NONE(254),
        ANY(MotionEventCompat.ACTION_MASK);
        
        private static final HashMap<Integer, CLASS> INVERSE_LUT;
        final int value;

        static {
            INVERSE_LUT = new HashMap();
            CLASS[] values = values();
            int length = values.length;
            int i;
            while (i < length) {
                CLASS c = values[i];
                INVERSE_LUT.put(Integer.valueOf(c.value), c);
                i++;
            }
        }

        private CLASS(int value) {
            this.value = value;
        }

        public static CLASS getClass(int value) {
            return (CLASS) INVERSE_LUT.get(Integer.valueOf(value));
        }
    }

    public enum TYPE {
        A(1),
        NS(2),
        MD(3),
        MF(4),
        CNAME(5),
        SOA(6),
        MB(7),
        MG(8),
        MR(9),
        NULL(10),
        WKS(11),
        PTR(12),
        HINFO(13),
        MINFO(14),
        MX(15),
        TXT(16),
        RP(17),
        AFSDB(18),
        X25(19),
        ISDN(20),
        RT(21),
        NSAP(22),
        NSAP_PTR(23),
        SIG(24),
        KEY(25),
        PX(26),
        GPOS(27),
        AAAA(28),
        LOC(29),
        NXT(30),
        EID(31),
        NIMLOC(32),
        SRV(33),
        ATMA(34),
        NAPTR(35),
        KX(36),
        CERT(37),
        A6(38),
        DNAME(39),
        SINK(40),
        OPT(41),
        APL(42),
        DS(43),
        SSHFP(44),
        IPSECKEY(45),
        RRSIG(46),
        NSEC(47),
        DNSKEY(48),
        DHCID(49),
        NSEC3(50),
        NSEC3PARAM(51),
        HIP(55),
        NINFO(56),
        RKEY(57),
        TALINK(58),
        SPF(99),
        UINFO(100),
        UID(C0170R.styleable.Theme_buttonStyleSmall),
        GID(C0170R.styleable.Theme_checkboxStyle),
        TKEY(249),
        TSIG(250),
        IXFR(251),
        AXFR(252),
        MAILB(253),
        MAILA(254),
        ANY(MotionEventCompat.ACTION_MASK),
        TA(AccessibilityNodeInfoCompat.ACTION_PASTE),
        DLV(32769);
        
        private static final HashMap<Integer, TYPE> INVERSE_LUT;
        final int value;

        static {
            INVERSE_LUT = new HashMap();
            TYPE[] values = values();
            int length = values.length;
            int i;
            while (i < length) {
                TYPE t = values[i];
                INVERSE_LUT.put(Integer.valueOf(t.value), t);
                i++;
            }
        }

        private TYPE(int value) {
            this.value = value;
        }

        public static TYPE getType(int value) {
            return (TYPE) INVERSE_LUT.get(Integer.valueOf(value));
        }
    }

    public final void parse(DataInputStream dis, byte[] data) throws IOException {
        this.name = NameUtil.parse(dis, data);
        this.type = TYPE.getType(dis.readUnsignedShort());
        this.clazz = CLASS.getClass(dis.readUnsignedShort());
        this.ttl = (((long) dis.readUnsignedShort()) << 32) + ((long) dis.readUnsignedShort());
        int payloadLength = dis.readUnsignedShort();
        switch (C12551.$SwitchMap$de$measite$minidns$Record$TYPE[this.type.ordinal()]) {
            case Logger.SEVERE /*1*/:
                this.payloadData = new SRV();
                break;
            case Logger.WARNING /*2*/:
                this.payloadData = new AAAA();
                break;
            case Logger.INFO /*3*/:
                this.payloadData = new C1256A();
                break;
            case Logger.CONFIG /*4*/:
                this.payloadData = new NS();
                break;
            case Logger.FINE /*5*/:
                this.payloadData = new CNAME();
                break;
            default:
                System.out.println("Unparsed type " + this.type);
                this.payloadData = null;
                for (int i = 0; i < payloadLength; i++) {
                    dis.readByte();
                }
                break;
        }
        if (this.payloadData != null) {
            this.payloadData.parse$4e8e5594(dis, data);
        }
    }

    public final String toString() {
        if (this.payloadData == null) {
            return "RR " + this.type + MqttTopic.TOPIC_LEVEL_SEPARATOR + this.clazz;
        }
        return "RR " + this.type + MqttTopic.TOPIC_LEVEL_SEPARATOR + this.clazz + ": " + this.payloadData.toString();
    }

    public final boolean isAnswer(Question q) {
        return (q.type == this.type || q.type == TYPE.ANY) && ((q.clazz == this.clazz || q.clazz == CLASS.ANY) && q.name.equals(this.name));
    }

    public final Data getPayload() {
        return this.payloadData;
    }

    public final long getTtl() {
        return this.ttl;
    }
}
