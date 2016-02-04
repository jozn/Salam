package cz.msebera.android.httpclient.cookie;

import java.util.Date;

public interface SetCookie extends Cookie {
    void setComment(String str);

    void setDomain(String str);

    void setExpiryDate(Date date);

    void setPath(String str);

    void setSecure$1385ff();

    void setVersion(int i);
}
