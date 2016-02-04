package org.jivesoftware.smack.packet;

import android.support.v7.appcompat.BuildConfig;
import com.shamchat.activity.AddFavoriteTextActivity;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class Message extends Packet {
    private final Set<Body> bodies;
    private final Set<Subject> subjects;
    public String thread;
    public Type type;

    public static class Body {
        String language;
        String message;

        private Body(String language, String message) {
            if (language == null) {
                throw new NullPointerException("Language cannot be null.");
            } else if (message == null) {
                throw new NullPointerException("Message cannot be null.");
            } else {
                this.language = language;
                this.message = message;
            }
        }

        public final int hashCode() {
            return ((this.language.hashCode() + 31) * 31) + this.message.hashCode();
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Body other = (Body) obj;
            if (this.language.equals(other.language) && this.message.equals(other.message)) {
                return true;
            }
            return false;
        }
    }

    public static class Subject {
        String language;
        String subject;

        private Subject(String language, String subject) {
            if (language == null) {
                throw new NullPointerException("Language cannot be null.");
            } else if (subject == null) {
                throw new NullPointerException("Subject cannot be null.");
            } else {
                this.language = language;
                this.subject = subject;
            }
        }

        public final int hashCode() {
            return ((this.language.hashCode() + 31) * 31) + this.subject.hashCode();
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Subject other = (Subject) obj;
            if (this.language.equals(other.language) && this.subject.equals(other.subject)) {
                return true;
            }
            return false;
        }
    }

    public enum Type {
        normal,
        chat,
        groupchat,
        headline,
        error;

        public static Type fromString(String string) {
            return valueOf(string.toLowerCase(Locale.US));
        }
    }

    public Message() {
        this.type = Type.normal;
        this.thread = null;
        this.subjects = new HashSet();
        this.bodies = new HashSet();
    }

    public Message(String to) {
        this.type = Type.normal;
        this.thread = null;
        this.subjects = new HashSet();
        this.bodies = new HashSet();
        this.to = to;
    }

    public Message(String to, Type type) {
        this.type = Type.normal;
        this.thread = null;
        this.subjects = new HashSet();
        this.bodies = new HashSet();
        this.to = to;
        setType(type);
    }

    public final void setType(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }
        this.type = type;
    }

    public final String getSubject(String language) {
        Subject subject = getMessageSubject(language);
        return subject == null ? null : subject.subject;
    }

    private Subject getMessageSubject(String language) {
        language = determineLanguage(language);
        for (Subject subject : this.subjects) {
            if (language.equals(subject.language)) {
                return subject;
            }
        }
        return null;
    }

    public final Subject addSubject(String language, String subject) {
        Subject messageSubject = new Subject(subject, (byte) 0);
        this.subjects.add(messageSubject);
        return messageSubject;
    }

    public final String getBody(String language) {
        Body body = getMessageBody(language);
        return body == null ? null : body.message;
    }

    private Body getMessageBody(String language) {
        language = determineLanguage(language);
        for (Body body : this.bodies) {
            if (language.equals(body.language)) {
                return body;
            }
        }
        return null;
    }

    public final void setBody(String body) {
        if (body == null) {
            String determineLanguage = determineLanguage(BuildConfig.VERSION_NAME);
            for (Body body2 : this.bodies) {
                if (determineLanguage.equals(body2.language)) {
                    this.bodies.remove(body2);
                    return;
                }
            }
            return;
        }
        addBody(null, body);
    }

    public final Body addBody(String language, String body) {
        Body messageBody = new Body(body, (byte) 0);
        this.bodies.add(messageBody);
        return messageBody;
    }

    private String determineLanguage(String language) {
        if (BuildConfig.VERSION_NAME.equals(language)) {
            language = null;
        }
        if (language == null && this.language != null) {
            return this.language;
        }
        if (language == null) {
            return Packet.getDefaultLanguage();
        }
        return language;
    }

    public final XmlStringBuilder toXML() {
        XmlStringBuilder buf = new XmlStringBuilder();
        buf.halfOpenElement(AddFavoriteTextActivity.EXTRA_MESSAGE);
        addCommonAttributes(buf);
        if (this.type != Type.normal) {
            buf.attribute("type", this.type);
        }
        buf.rightAngleBracket();
        Subject defaultSubject = getMessageSubject(null);
        if (defaultSubject != null) {
            buf.element("subject", defaultSubject.subject);
        }
        for (Subject subject : Collections.unmodifiableCollection(this.subjects)) {
            if (!subject.equals(defaultSubject)) {
                buf.halfOpenElement("subject").xmllangAttribute(subject.language).rightAngleBracket();
                buf.escape(subject.subject);
                buf.closeElement("subject");
            }
        }
        Body defaultBody = getMessageBody(null);
        if (defaultBody != null) {
            buf.element("body", defaultBody.message);
        }
        for (Body body : Collections.unmodifiableCollection(this.bodies)) {
            if (!body.equals(defaultBody)) {
                buf.halfOpenElement("body").xmllangAttribute(body.language).rightAngleBracket();
                buf.escape(body.message);
                buf.closeElement("body");
            }
        }
        buf.optElement("thread", this.thread);
        if (this.type == Type.error) {
            XMPPError error = this.error;
            if (error != null) {
                buf.append(error.toXML());
            }
        }
        buf.append(getExtensionsXML());
        buf.closeElement(AddFavoriteTextActivity.EXTRA_MESSAGE);
        return buf;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        if (!super.equals(message)) {
            return false;
        }
        if (this.bodies.size() != message.bodies.size() || !this.bodies.containsAll(message.bodies)) {
            return false;
        }
        if (this.language == null ? message.language != null : !this.language.equals(message.language)) {
            return false;
        }
        if (this.subjects.size() != message.subjects.size() || !this.subjects.containsAll(message.subjects)) {
            return false;
        }
        if (this.thread == null ? message.thread != null : !this.thread.equals(message.thread)) {
            return false;
        }
        if (this.type != message.type) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        int hashCode;
        int i = 0;
        if (this.type != null) {
            result = this.type.hashCode();
        } else {
            result = 0;
        }
        int hashCode2 = ((result * 31) + this.subjects.hashCode()) * 31;
        if (this.thread != null) {
            hashCode = this.thread.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (hashCode2 + hashCode) * 31;
        if (this.language != null) {
            i = this.language.hashCode();
        }
        return ((hashCode + i) * 31) + this.bodies.hashCode();
    }
}
