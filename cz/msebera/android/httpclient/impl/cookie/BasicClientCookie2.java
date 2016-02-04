package cz.msebera.android.httpclient.impl.cookie;

import cz.msebera.android.httpclient.cookie.SetCookie2;
import java.util.Date;

public final class BasicClientCookie2 extends BasicClientCookie implements SetCookie2 {
    private String commentURL;
    private boolean discard;
    int[] ports;

    public BasicClientCookie2(String name, String value) {
        super(name, value);
    }

    public final int[] getPorts() {
        return this.ports;
    }

    public final void setPorts(int[] ports) {
        this.ports = ports;
    }

    public final void setCommentURL(String commentURL) {
        this.commentURL = commentURL;
    }

    public final void setDiscard$1385ff() {
        this.discard = true;
    }

    public final boolean isExpired(Date date) {
        return this.discard || super.isExpired(date);
    }

    public final Object clone() throws CloneNotSupportedException {
        BasicClientCookie2 clone = (BasicClientCookie2) super.clone();
        if (this.ports != null) {
            clone.ports = (int[]) this.ports.clone();
        }
        return clone;
    }
}
