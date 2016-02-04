package cz.msebera.android.httpclient.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class VersionInfo {
    private final String infoClassloader;
    private final String infoModule;
    private final String infoPackage;
    public final String infoRelease;
    private final String infoTimestamp;

    private VersionInfo(String pckg, String module, String release, String time, String clsldr) {
        Args.notNull(pckg, "Package identifier");
        this.infoPackage = pckg;
        if (module == null) {
            module = "UNAVAILABLE";
        }
        this.infoModule = module;
        if (release == null) {
            release = "UNAVAILABLE";
        }
        this.infoRelease = release;
        if (time == null) {
            time = "UNAVAILABLE";
        }
        this.infoTimestamp = time;
        if (clsldr == null) {
            clsldr = "UNAVAILABLE";
        }
        this.infoClassloader = clsldr;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder(((((this.infoPackage.length() + 20) + this.infoModule.length()) + this.infoRelease.length()) + this.infoTimestamp.length()) + this.infoClassloader.length());
        sb.append("VersionInfo(").append(this.infoPackage).append(':').append(this.infoModule);
        if (!"UNAVAILABLE".equals(this.infoRelease)) {
            sb.append(':').append(this.infoRelease);
        }
        if (!"UNAVAILABLE".equals(this.infoTimestamp)) {
            sb.append(':').append(this.infoTimestamp);
        }
        sb.append(')');
        if (!"UNAVAILABLE".equals(this.infoClassloader)) {
            sb.append('@').append(this.infoClassloader);
        }
        return sb.toString();
    }

    public static VersionInfo loadVersionInfo(String pckg, ClassLoader clsldr) {
        ClassLoader cl;
        InputStream is;
        String str = null;
        Args.notNull(pckg, "Package identifier");
        if (clsldr != null) {
            cl = clsldr;
        } else {
            cl = Thread.currentThread().getContextClassLoader();
        }
        Properties vip = null;
        try {
            is = cl.getResourceAsStream(pckg.replace('.', '/') + "/version.properties");
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                vip = props;
                is.close();
            }
        } catch (IOException e) {
        } catch (Throwable th) {
            is.close();
        }
        if (vip == null) {
            return null;
        }
        String str2;
        String str3;
        String str4;
        Args.notNull(pckg, "Package identifier");
        if (vip != null) {
            String str5 = (String) vip.get("info.module");
            if (str5 == null || str5.length() > 0) {
                str2 = str5;
            } else {
                str2 = null;
            }
            str5 = (String) vip.get("info.release");
            if (str5 == null || (str5.length() > 0 && !str5.equals("${pom.version}"))) {
                str3 = str5;
            } else {
                str3 = null;
            }
            str5 = (String) vip.get("info.timestamp");
            str4 = (str5 == null || (str5.length() > 0 && !str5.equals("${mvn.timestamp}"))) ? str5 : null;
        } else {
            str4 = null;
            str3 = null;
            str2 = null;
        }
        if (cl != null) {
            str = cl.toString();
        }
        return new VersionInfo(pckg, str2, str3, str4, str);
    }
}
