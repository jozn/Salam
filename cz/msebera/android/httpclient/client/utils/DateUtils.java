package cz.msebera.android.httpclient.client.utils;

import cz.msebera.android.httpclient.util.Args;
import java.lang.ref.SoftReference;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public final class DateUtils {
    private static final String[] DEFAULT_PATTERNS;
    private static final Date DEFAULT_TWO_DIGIT_YEAR_START;
    public static final TimeZone GMT;

    static final class DateFormatHolder {
        private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS;

        /* renamed from: cz.msebera.android.httpclient.client.utils.DateUtils.DateFormatHolder.1 */
        static class C12441 extends ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> {
            C12441() {
            }

            protected final /* bridge */ /* synthetic */ Object initialValue() {
                return new SoftReference(new HashMap());
            }
        }

        static {
            THREADLOCAL_FORMATS = new C12441();
        }

        public static SimpleDateFormat formatFor(String pattern) {
            Map<String, SimpleDateFormat> formats = (Map) ((SoftReference) THREADLOCAL_FORMATS.get()).get();
            if (formats == null) {
                formats = new HashMap();
                THREADLOCAL_FORMATS.set(new SoftReference(formats));
            }
            SimpleDateFormat format = (SimpleDateFormat) formats.get(pattern);
            if (format != null) {
                return format;
            }
            format = new SimpleDateFormat(pattern, Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            formats.put(pattern, format);
            return format;
        }
    }

    static {
        DEFAULT_PATTERNS = new String[]{"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy"};
        GMT = TimeZone.getTimeZone("GMT");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(GMT);
        calendar.set(2000, 0, 1, 0, 0, 0);
        calendar.set(14, 0);
        DEFAULT_TWO_DIGIT_YEAR_START = calendar.getTime();
    }

    public static Date parseDate(String dateValue, String[] dateFormats) {
        Args.notNull(dateValue, "Date value");
        if (dateFormats == null) {
            dateFormats = DEFAULT_PATTERNS;
        }
        Date date = DEFAULT_TWO_DIGIT_YEAR_START;
        if (dateValue.length() > 1 && dateValue.startsWith("'") && dateValue.endsWith("'")) {
            dateValue = dateValue.substring(1, dateValue.length() - 1);
        }
        for (String formatFor : r7) {
            SimpleDateFormat formatFor2 = DateFormatHolder.formatFor(formatFor);
            formatFor2.set2DigitYearStart(date);
            ParsePosition parsePosition = new ParsePosition(0);
            Date parse = formatFor2.parse(dateValue, parsePosition);
            if (parsePosition.getIndex() != 0) {
                return parse;
            }
        }
        return null;
    }
}
