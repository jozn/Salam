package de.measite.minidns;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import de.measite.minidns.Record.CLASS;
import de.measite.minidns.Record.TYPE;
import de.measite.minidns.util.NameUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public final class DNSMessage {
    protected Record[] additionalResourceRecords;
    protected Record[] answers;
    protected boolean authenticData;
    protected boolean authoritativeAnswer;
    protected boolean checkDisabled;
    protected int id;
    protected Record[] nameserverRecords;
    protected OPCODE opcode;
    protected boolean query;
    protected Question[] questions;
    protected long receiveTimestamp;
    protected boolean recursionAvailable;
    protected boolean recursionDesired;
    protected RESPONSE_CODE responseCode;
    protected boolean truncated;

    public enum OPCODE {
        QUERY(0),
        INVERSE_QUERY(1),
        STATUS(2),
        NOTIFY(4),
        UPDATE(5);
        
        private static final OPCODE[] INVERSE_LUT;
        final byte value;

        static {
            INVERSE_LUT = new OPCODE[]{QUERY, INVERSE_QUERY, STATUS, null, NOTIFY, UPDATE, null, null, null, null, null, null, null, null, null};
        }

        private OPCODE(int value) {
            this.value = (byte) value;
        }

        public static OPCODE getOpcode(int value) {
            if (value >= 0 && value <= 15) {
                return INVERSE_LUT[value];
            }
            throw new IllegalArgumentException();
        }
    }

    public enum RESPONSE_CODE {
        NO_ERROR(0),
        FORMAT_ERR(1),
        SERVER_FAIL(2),
        NX_DOMAIN(3),
        NO_IMP(4),
        REFUSED(5),
        YXDOMAIN(6),
        YXRRSET(7),
        NXRRSET(8),
        NOT_AUTH(9),
        NOT_ZONE(10);
        
        private static final RESPONSE_CODE[] INVERSE_LUT;
        final byte value;

        static {
            INVERSE_LUT = new RESPONSE_CODE[]{NO_ERROR, FORMAT_ERR, SERVER_FAIL, NX_DOMAIN, NO_IMP, REFUSED, YXDOMAIN, YXRRSET, NXRRSET, NOT_AUTH, NOT_ZONE, null, null, null, null, null};
        }

        private RESPONSE_CODE(int value) {
            this.value = (byte) value;
        }

        public static RESPONSE_CODE getResponseCode(int value) {
            if (value >= 0 && value <= 15) {
                return INVERSE_LUT[value];
            }
            throw new IllegalArgumentException();
        }
    }

    public final int getId() {
        return this.id;
    }

    public final void setId(int id) {
        this.id = SupportMenu.USER_MASK & id;
    }

    public final void setRecursionDesired$1385ff() {
        this.recursionDesired = true;
    }

    public final byte[] toArray() throws IOException {
        int i = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY);
        DataOutputStream dos = new DataOutputStream(baos);
        int header = 0;
        if (this.query) {
            header = AccessibilityNodeInfoCompat.ACTION_PASTE;
        }
        if (this.opcode != null) {
            header += this.opcode.value << 11;
        }
        if (this.authoritativeAnswer) {
            header += AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT;
        }
        if (this.truncated) {
            header += AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY;
        }
        if (this.recursionDesired) {
            header += AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
        }
        if (this.recursionAvailable) {
            header += AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS;
        }
        if (this.authenticData) {
            header += 32;
        }
        if (this.checkDisabled) {
            header += 16;
        }
        if (this.responseCode != null) {
            header += this.responseCode.value;
        }
        dos.writeShort((short) this.id);
        dos.writeShort((short) header);
        if (this.questions == null) {
            dos.writeShort(0);
        } else {
            dos.writeShort((short) this.questions.length);
        }
        if (this.answers == null) {
            dos.writeShort(0);
        } else {
            dos.writeShort((short) this.answers.length);
        }
        if (this.nameserverRecords == null) {
            dos.writeShort(0);
        } else {
            dos.writeShort((short) this.nameserverRecords.length);
        }
        if (this.additionalResourceRecords == null) {
            dos.writeShort(0);
        } else {
            dos.writeShort((short) this.additionalResourceRecords.length);
        }
        Question[] questionArr = this.questions;
        int length = questionArr.length;
        while (i < length) {
            dos.write(questionArr[i].toByteArray());
            i++;
        }
        dos.flush();
        return baos.toByteArray();
    }

    public static DNSMessage parse(byte[] data) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        DNSMessage message = new DNSMessage();
        message.id = dis.readUnsignedShort();
        int header = dis.readUnsignedShort();
        message.query = ((header >> 15) & 1) == 0;
        message.opcode = OPCODE.getOpcode((header >> 11) & 15);
        message.authoritativeAnswer = ((header >> 10) & 1) == 1;
        message.truncated = ((header >> 9) & 1) == 1;
        message.recursionDesired = ((header >> 8) & 1) == 1;
        message.recursionAvailable = ((header >> 7) & 1) == 1;
        message.authenticData = ((header >> 5) & 1) == 1;
        message.checkDisabled = ((header >> 4) & 1) == 1;
        message.responseCode = RESPONSE_CODE.getResponseCode(header & 15);
        message.receiveTimestamp = System.currentTimeMillis();
        int questionCount = dis.readUnsignedShort();
        int answerCount = dis.readUnsignedShort();
        int nameserverCount = dis.readUnsignedShort();
        int additionalResourceRecordCount = dis.readUnsignedShort();
        message.questions = new Question[questionCount];
        int questionCount2 = questionCount;
        while (true) {
            questionCount = questionCount2 - 1;
            if (questionCount2 <= 0) {
                break;
            }
            message.questions[questionCount] = new Question(NameUtil.parse(dis, data), TYPE.getType(dis.readUnsignedShort()), CLASS.getClass(dis.readUnsignedShort()));
            questionCount2 = questionCount;
        }
        message.answers = new Record[answerCount];
        int answerCount2 = answerCount;
        while (true) {
            answerCount = answerCount2 - 1;
            if (answerCount2 <= 0) {
                break;
            }
            Record rr = new Record();
            rr.parse(dis, data);
            message.answers[answerCount] = rr;
            answerCount2 = answerCount;
        }
        message.nameserverRecords = new Record[nameserverCount];
        int nameserverCount2 = nameserverCount;
        while (true) {
            nameserverCount = nameserverCount2 - 1;
            if (nameserverCount2 <= 0) {
                break;
            }
            rr = new Record();
            rr.parse(dis, data);
            message.nameserverRecords[nameserverCount] = rr;
            nameserverCount2 = nameserverCount;
        }
        message.additionalResourceRecords = new Record[additionalResourceRecordCount];
        int additionalResourceRecordCount2 = additionalResourceRecordCount;
        while (true) {
            additionalResourceRecordCount = additionalResourceRecordCount2 - 1;
            if (additionalResourceRecordCount2 <= 0) {
                return message;
            }
            rr = new Record();
            rr.parse(dis, data);
            message.additionalResourceRecords[additionalResourceRecordCount] = rr;
            additionalResourceRecordCount2 = additionalResourceRecordCount;
        }
    }

    public final void setQuestions(Question... questions) {
        this.questions = questions;
    }

    public final RESPONSE_CODE getResponseCode() {
        return this.responseCode;
    }

    public final Record[] getAnswers() {
        return this.answers;
    }

    public final String toString() {
        return "-- DNSMessage " + this.id + " --\n" + Arrays.toString(this.answers);
    }
}
