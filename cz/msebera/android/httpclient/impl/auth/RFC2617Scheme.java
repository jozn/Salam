package cz.msebera.android.httpclient.impl.auth;

import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.auth.MalformedChallengeException;
import cz.msebera.android.httpclient.message.BasicHeaderValueParser;
import cz.msebera.android.httpclient.message.ParserCursor;
import cz.msebera.android.httpclient.util.CharArrayBuffer;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class RFC2617Scheme extends AuthSchemeBase implements Serializable {
    private transient Charset credentialsCharset;
    final Map<String, String> params;

    public RFC2617Scheme(Charset credentialsCharset) {
        this.params = new HashMap();
        if (credentialsCharset == null) {
            credentialsCharset = Consts.ASCII;
        }
        this.credentialsCharset = credentialsCharset;
    }

    final String getCredentialsCharset(HttpRequest request) {
        String charset = (String) request.getParams().getParameter("http.auth.credential-charset");
        if (charset != null) {
            return charset;
        }
        return (this.credentialsCharset != null ? this.credentialsCharset : Consts.ASCII).name();
    }

    protected final void parseChallenge(CharArrayBuffer buffer, int pos, int len) throws MalformedChallengeException {
        HeaderElement[] elements = BasicHeaderValueParser.INSTANCE.parseElements(buffer, new ParserCursor(pos, buffer.length()));
        this.params.clear();
        for (HeaderElement element : elements) {
            this.params.put(element.getName().toLowerCase(Locale.ROOT), element.getValue());
        }
    }

    public final String getParameter(String name) {
        return (String) this.params.get(name.toLowerCase(Locale.ROOT));
    }

    public final String getRealm() {
        return getParameter("realm");
    }
}
