package org.eclipse.paho.client.mqttv3.logging;

import android.support.v7.appcompat.BuildConfig;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SimpleLogFormatter extends Formatter {
    private static final String LS;

    static {
        LS = System.getProperty("line.separator");
    }

    public String format(LogRecord r) {
        Throwable th;
        StringBuffer sb = new StringBuffer();
        sb.append(r.getLevel().getName()).append("\t");
        sb.append(new StringBuffer(String.valueOf(MessageFormat.format("{0, date, yy-MM-dd} {0, time, kk:mm:ss.SSSS} ", new Object[]{new Date(r.getMillis())}))).append("\t").toString());
        String cnm = r.getSourceClassName();
        String cn = BuildConfig.VERSION_NAME;
        if (cnm != null) {
            int cnl = cnm.length();
            if (cnl > 20) {
                cn = r.getSourceClassName().substring(cnl - 19);
            } else {
                cn = new StringBuffer().append(cnm).append(new char[]{' '}, 0, 1).toString();
            }
        }
        sb.append(cn).append("\t ");
        sb.append(left(r.getSourceMethodName(), 23, ' ')).append("\t");
        sb.append(r.getThreadID()).append("\t");
        sb.append(formatMessage(r)).append(LS);
        if (r.getThrown() != null) {
            sb.append("Throwable occurred: ");
            Throwable t = r.getThrown();
            PrintWriter pw = null;
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw2 = new PrintWriter(sw);
                try {
                    t.printStackTrace(pw2);
                    sb.append(sw.toString());
                    if (pw2 != null) {
                        try {
                            pw2.close();
                        } catch (Exception e) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    pw = pw2;
                    if (pw != null) {
                        try {
                            pw.close();
                        } catch (Exception e2) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (pw != null) {
                    pw.close();
                }
                throw th;
            }
        }
        return sb.toString();
    }

    public static String left(String s, int width, char fillChar) {
        if (s.length() >= width) {
            return s;
        }
        StringBuffer sb = new StringBuffer(width);
        sb.append(s);
        int i = width - s.length();
        while (true) {
            i--;
            if (i < 0) {
                return sb.toString();
            }
            sb.append(fillChar);
        }
    }
}
