package com.squareup.okhttp;

import com.squareup.okhttp.internal.Util;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public final class FormEncodingBuilder {
    private static final MediaType CONTENT_TYPE;
    private final StringBuilder content;

    public FormEncodingBuilder() {
        this.content = new StringBuilder();
    }

    static {
        CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded");
    }

    public final FormEncodingBuilder add(String name, String value) {
        if (this.content.length() > 0) {
            this.content.append('&');
        }
        try {
            this.content.append(URLEncoder.encode(name, "UTF-8")).append('=').append(URLEncoder.encode(value, "UTF-8"));
            return this;
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public final RequestBody build() {
        if (this.content.length() == 0) {
            throw new IllegalStateException("Form encoded body must have at least one part.");
        }
        return RequestBody.create(CONTENT_TYPE, this.content.toString().getBytes(Util.UTF_8));
    }
}
