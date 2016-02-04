package org.jivesoftware.smackx.vcardtemp.packet;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.util.StringUtils;

public class VCard extends IQ {
    private static final Logger LOGGER;
    public String emailHome;
    public String emailWork;
    public String firstName;
    public Map<String, String> homeAddr;
    public Map<String, String> homePhones;
    public String lastName;
    public String middleName;
    public String organization;
    public String organizationUnit;
    public Map<String, String> otherSimpleFields;
    private Map<String, String> otherUnescapableFields;
    public String photoBinval;
    public String photoMimeType;
    public Map<String, String> workAddr;
    public Map<String, String> workPhones;

    private interface ContentBuilder {
        void addTagContent();
    }

    private class VCardWriter {
        final StringBuilder sb;

        /* renamed from: org.jivesoftware.smackx.vcardtemp.packet.VCard.VCardWriter.1 */
        class C13471 implements ContentBuilder {
            C13471() {
            }

            public final void addTagContent() {
                VCardWriter vCardWriter = VCardWriter.this;
                if (VCard.this.hasNameField()) {
                    vCardWriter.appendTag$49b7ce91("N", new C13537());
                }
                if (VCard.this.hasOrganizationFields()) {
                    vCardWriter.appendTag$49b7ce91("ORG", new C13526());
                }
                for (Entry entry : VCard.this.otherSimpleFields.entrySet()) {
                    vCardWriter.appendTag(((String) entry.getKey()).toString(), StringUtils.escapeForXML((String) entry.getValue()));
                }
                for (Entry entry2 : VCard.this.otherUnescapableFields.entrySet()) {
                    vCardWriter.appendTag(((String) entry2.getKey()).toString(), (CharSequence) entry2.getValue());
                }
                if (VCard.this.photoBinval != null) {
                    vCardWriter.appendTag$49b7ce91("PHOTO", new C13482());
                }
                vCardWriter.appendEmail(VCard.this.emailWork, "WORK");
                vCardWriter.appendEmail(VCard.this.emailHome, "HOME");
                vCardWriter.appendPhones(VCard.this.workPhones, "WORK");
                vCardWriter.appendPhones(VCard.this.homePhones, "HOME");
                vCardWriter.appendAddress(VCard.this.workAddr, "WORK");
                vCardWriter.appendAddress(VCard.this.homeAddr, "HOME");
            }
        }

        /* renamed from: org.jivesoftware.smackx.vcardtemp.packet.VCard.VCardWriter.2 */
        class C13482 implements ContentBuilder {
            C13482() {
            }

            public final void addTagContent() {
                VCardWriter.this.appendTag("BINVAL", VCard.this.photoBinval);
                VCardWriter.this.appendTag("TYPE", StringUtils.escapeForXML(VCard.this.photoMimeType));
            }
        }

        /* renamed from: org.jivesoftware.smackx.vcardtemp.packet.VCard.VCardWriter.3 */
        class C13493 implements ContentBuilder {
            final /* synthetic */ String val$email;
            final /* synthetic */ String val$type;

            C13493(String str, String str2) {
                this.val$type = str;
                this.val$email = str2;
            }

            public final void addTagContent() {
                VCardWriter.this.sb.append('<').append(this.val$type).append("/>");
                VCardWriter.this.sb.append('<').append("INTERNET").append("/>");
                VCardWriter.this.sb.append('<').append("PREF").append("/>");
                VCardWriter.this.appendTag("USERID", StringUtils.escapeForXML(this.val$email));
            }
        }

        /* renamed from: org.jivesoftware.smackx.vcardtemp.packet.VCard.VCardWriter.4 */
        class C13504 implements ContentBuilder {
            final /* synthetic */ String val$code;
            final /* synthetic */ Entry val$entry;

            C13504(Entry entry, String str) {
                this.val$entry = entry;
                this.val$code = str;
            }

            public final void addTagContent() {
                VCardWriter.this.sb.append('<').append(this.val$entry.getKey()).append("/>");
                VCardWriter.this.sb.append('<').append(this.val$code).append("/>");
                VCardWriter.this.appendTag("NUMBER", StringUtils.escapeForXML((String) this.val$entry.getValue()));
            }
        }

        /* renamed from: org.jivesoftware.smackx.vcardtemp.packet.VCard.VCardWriter.5 */
        class C13515 implements ContentBuilder {
            final /* synthetic */ Map val$addr;
            final /* synthetic */ String val$code;

            C13515(String str, Map map) {
                this.val$code = str;
                this.val$addr = map;
            }

            public final void addTagContent() {
                VCardWriter.this.sb.append('<').append(this.val$code).append("/>");
                for (Entry<String, String> entry : this.val$addr.entrySet()) {
                    VCardWriter.this.appendTag((String) entry.getKey(), StringUtils.escapeForXML((String) entry.getValue()));
                }
            }
        }

        /* renamed from: org.jivesoftware.smackx.vcardtemp.packet.VCard.VCardWriter.6 */
        class C13526 implements ContentBuilder {
            C13526() {
            }

            public final void addTagContent() {
                VCardWriter.this.appendTag("ORGNAME", StringUtils.escapeForXML(VCard.this.organization));
                VCardWriter.this.appendTag("ORGUNIT", StringUtils.escapeForXML(VCard.this.organizationUnit));
            }
        }

        /* renamed from: org.jivesoftware.smackx.vcardtemp.packet.VCard.VCardWriter.7 */
        class C13537 implements ContentBuilder {
            C13537() {
            }

            public final void addTagContent() {
                VCardWriter.this.appendTag("FAMILY", StringUtils.escapeForXML(VCard.this.lastName));
                VCardWriter.this.appendTag("GIVEN", StringUtils.escapeForXML(VCard.this.firstName));
                VCardWriter.this.appendTag("MIDDLE", StringUtils.escapeForXML(VCard.this.middleName));
            }
        }

        /* renamed from: org.jivesoftware.smackx.vcardtemp.packet.VCard.VCardWriter.8 */
        class C13548 implements ContentBuilder {
            final /* synthetic */ CharSequence val$tagText;

            C13548(CharSequence charSequence) {
                this.val$tagText = charSequence;
            }

            public final void addTagContent() {
                VCardWriter.this.sb.append(this.val$tagText.toString().trim());
            }
        }

        VCardWriter(StringBuilder sb) {
            this.sb = sb;
        }

        final void appendEmail(String email, String type) {
            if (email != null) {
                appendTag$49b7ce91("EMAIL", new C13493(type, email));
            }
        }

        final void appendPhones(Map<String, String> phones, String code) {
            for (Entry<String, String> entry : phones.entrySet()) {
                appendTag$49b7ce91("TEL", new C13504(entry, code));
            }
        }

        final void appendAddress(Map<String, String> addr, String code) {
            if (addr.size() > 0) {
                appendTag$49b7ce91("ADR", new C13515(code, addr));
            }
        }

        final void appendTag(String tag, String attr, String attrValue, boolean hasContent, ContentBuilder builder) {
            this.sb.append('<').append(tag);
            if (attr != null) {
                this.sb.append(' ').append(attr).append('=').append('\'').append(attrValue).append('\'');
            }
            if (hasContent) {
                this.sb.append('>');
                builder.addTagContent();
                this.sb.append("</").append(tag).append(">\n");
                return;
            }
            this.sb.append("/>\n");
        }

        final void appendTag$49b7ce91(String tag, ContentBuilder builder) {
            appendTag(tag, null, null, true, builder);
        }

        final void appendTag(String tag, CharSequence tagText) {
            if (tagText != null) {
                appendTag$49b7ce91(tag, new C13548(tagText));
            }
        }
    }

    static {
        LOGGER = Logger.getLogger(VCard.class.getName());
    }

    public VCard() {
        this.homePhones = new HashMap();
        this.workPhones = new HashMap();
        this.homeAddr = new HashMap();
        this.workAddr = new HashMap();
        this.otherSimpleFields = new HashMap();
        this.otherUnescapableFields = new HashMap();
    }

    public final void setField(String field, String value) {
        this.otherSimpleFields.put(field, value);
    }

    public final void setFirstName(String firstName) {
        this.firstName = firstName;
        updateFN();
    }

    public final void setMiddleName(String middleName) {
        this.middleName = middleName;
        updateFN();
    }

    public final String getNickName() {
        return (String) this.otherSimpleFields.get("NICKNAME");
    }

    public final void setNickName(String nickName) {
        this.otherSimpleFields.put("NICKNAME", nickName);
    }

    public final String getJabberId() {
        return (String) this.otherSimpleFields.get("JABBERID");
    }

    public final void setJabberId(String jabberId) {
        this.otherSimpleFields.put("JABBERID", jabberId);
    }

    public final void setAddressFieldHome(String addrField, String value) {
        this.homeAddr.put(addrField, value);
    }

    public final void setPhoneHome(String phoneType, String phoneNum) {
        this.homePhones.put(phoneType, phoneNum);
    }

    public final void setAvatar(String encodedImage, String mimeType) {
        this.photoBinval = encodedImage;
        this.photoMimeType = mimeType;
    }

    public final void updateFN() {
        StringBuilder sb = new StringBuilder();
        if (this.firstName != null) {
            sb.append(StringUtils.escapeForXML(this.firstName)).append(' ');
        }
        if (this.middleName != null) {
            sb.append(StringUtils.escapeForXML(this.middleName)).append(' ');
        }
        if (this.lastName != null) {
            sb.append(StringUtils.escapeForXML(this.lastName));
        }
        setField("FN", sb.toString());
    }

    public final void save(XMPPConnection connection) throws NoResponseException, XMPPErrorException, NotConnectedException {
        checkAuthenticated(connection, true);
        setType(Type.set);
        this.from = connection.getUser();
        connection.createPacketCollectorAndSend(this).nextResultOrThrow();
    }

    public final void load(XMPPConnection connection, String user) throws NoResponseException, XMPPErrorException, NotConnectedException {
        int i = 0;
        checkAuthenticated(connection, false);
        this.to = user;
        setType(Type.get);
        VCard vCard = (VCard) connection.createPacketCollectorAndSend(this).nextResultOrThrow();
        Field[] declaredFields = VCard.class.getDeclaredFields();
        int length = declaredFields.length;
        while (i < length) {
            Field field = declaredFields[i];
            if (field.getDeclaringClass() == VCard.class && !Modifier.isFinal(field.getModifiers())) {
                try {
                    field.setAccessible(true);
                    field.set(this, field.get(vCard));
                } catch (Throwable e) {
                    throw new RuntimeException("This cannot happen:" + field, e);
                }
            }
            i++;
        }
    }

    private String getChildElementXML() {
        StringBuilder sb = new StringBuilder();
        VCardWriter vCardWriter = new VCardWriter(sb);
        String str = "vCard";
        String str2 = "xmlns";
        String str3 = "vcard-temp";
        VCard vCard = vCardWriter.this$0;
        boolean z = vCard.hasNameField() || vCard.hasOrganizationFields() || vCard.emailHome != null || vCard.emailWork != null || vCard.otherSimpleFields.size() > 0 || vCard.otherUnescapableFields.size() > 0 || vCard.homeAddr.size() > 0 || vCard.homePhones.size() > 0 || vCard.workAddr.size() > 0 || vCard.workPhones.size() > 0 || vCard.photoBinval != null;
        vCardWriter.appendTag(str, str2, str3, z, new C13471());
        return sb.toString();
    }

    private static void checkAuthenticated(XMPPConnection connection, boolean checkForAnonymous) {
        if (connection == null) {
            throw new IllegalArgumentException("No connection was provided");
        } else if (!connection.isAuthenticated()) {
            throw new IllegalArgumentException("XMPPConnection is not authenticated");
        } else if (checkForAnonymous && connection.isAnonymous()) {
            throw new IllegalArgumentException("XMPPConnection cannot be anonymous");
        }
    }

    private boolean hasNameField() {
        return (this.firstName == null && this.lastName == null && this.middleName == null) ? false : true;
    }

    private boolean hasOrganizationFields() {
        return (this.organization == null && this.organizationUnit == null) ? false : true;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VCard vCard = (VCard) o;
        if (this.emailHome != null) {
            if (!this.emailHome.equals(vCard.emailHome)) {
                return false;
            }
        } else if (vCard.emailHome != null) {
            return false;
        }
        if (this.emailWork != null) {
            if (!this.emailWork.equals(vCard.emailWork)) {
                return false;
            }
        } else if (vCard.emailWork != null) {
            return false;
        }
        if (this.firstName != null) {
            if (!this.firstName.equals(vCard.firstName)) {
                return false;
            }
        } else if (vCard.firstName != null) {
            return false;
        }
        if (!this.homeAddr.equals(vCard.homeAddr) || !this.homePhones.equals(vCard.homePhones)) {
            return false;
        }
        if (this.lastName != null) {
            if (!this.lastName.equals(vCard.lastName)) {
                return false;
            }
        } else if (vCard.lastName != null) {
            return false;
        }
        if (this.middleName != null) {
            if (!this.middleName.equals(vCard.middleName)) {
                return false;
            }
        } else if (vCard.middleName != null) {
            return false;
        }
        if (this.organization != null) {
            if (!this.organization.equals(vCard.organization)) {
                return false;
            }
        } else if (vCard.organization != null) {
            return false;
        }
        if (this.organizationUnit != null) {
            if (!this.organizationUnit.equals(vCard.organizationUnit)) {
                return false;
            }
        } else if (vCard.organizationUnit != null) {
            return false;
        }
        if (!this.otherSimpleFields.equals(vCard.otherSimpleFields) || !this.workAddr.equals(vCard.workAddr)) {
            return false;
        }
        if (this.photoBinval != null) {
            if (!this.photoBinval.equals(vCard.photoBinval)) {
                return false;
            }
        } else if (vCard.photoBinval != null) {
            return false;
        }
        return this.workPhones.equals(vCard.workPhones);
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        int hashCode2 = ((((((((this.homePhones.hashCode() * 29) + this.workPhones.hashCode()) * 29) + this.homeAddr.hashCode()) * 29) + this.workAddr.hashCode()) * 29) + (this.firstName != null ? this.firstName.hashCode() : 0)) * 29;
        if (this.lastName != null) {
            hashCode = this.lastName.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode2 + hashCode) * 29;
        if (this.middleName != null) {
            hashCode = this.middleName.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode2 + hashCode) * 29;
        if (this.emailHome != null) {
            hashCode = this.emailHome.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode2 + hashCode) * 29;
        if (this.emailWork != null) {
            hashCode = this.emailWork.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode2 + hashCode) * 29;
        if (this.organization != null) {
            hashCode = this.organization.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode2 + hashCode) * 29;
        if (this.organizationUnit != null) {
            hashCode = this.organizationUnit.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (((hashCode2 + hashCode) * 29) + this.otherSimpleFields.hashCode()) * 29;
        if (this.photoBinval != null) {
            i = this.photoBinval.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        return getChildElementXML();
    }
}
