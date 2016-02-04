package org.jxmpp.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

public final class XmppDateTime {
    private static final List<PatternCouplings> couplings;
    private static final DateFormatType dateFormatter;
    private static final Pattern datePattern;
    private static final DateFormatType dateTimeFormatter;
    private static final DateFormatType dateTimeNoMillisFormatter;
    private static final Pattern dateTimeNoMillisPattern;
    private static final Pattern dateTimePattern;
    private static final DateFormatType timeFormatter;
    private static final DateFormatType timeNoMillisFormatter;
    private static final DateFormatType timeNoMillisNoZoneFormatter;
    private static final Pattern timeNoMillisNoZonePattern;
    private static final Pattern timeNoMillisPattern;
    private static final DateFormatType timeNoZoneFormatter;
    private static final Pattern timeNoZonePattern;
    private static final Pattern timePattern;
    private static final DateFormat xep0091Date6DigitFormatter;
    private static final DateFormat xep0091Date7Digit1MonthFormatter;
    private static final DateFormat xep0091Date7Digit2MonthFormatter;
    private static final DateFormat xep0091Formatter;
    private static final Pattern xep0091Pattern;

    /* renamed from: org.jxmpp.util.XmppDateTime.1 */
    static class C13561 implements Comparator<Calendar> {
        final /* synthetic */ Calendar val$now;

        C13561(Calendar calendar) {
            this.val$now = calendar;
        }

        public final /* bridge */ /* synthetic */ int compare(Object x0, Object x1) {
            return new Long(this.val$now.getTimeInMillis() - ((Calendar) x0).getTimeInMillis()).compareTo(new Long(this.val$now.getTimeInMillis() - ((Calendar) x1).getTimeInMillis()));
        }
    }

    public enum DateFormatType {
        XEP_0082_DATE_PROFILE("yyyy-MM-dd"),
        XEP_0082_DATETIME_PROFILE("yyyy-MM-dd'T'HH:mm:ssZ"),
        XEP_0082_DATETIME_MILLIS_PROFILE("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
        XEP_0082_TIME_PROFILE("hh:mm:ss"),
        XEP_0082_TIME_ZONE_PROFILE("hh:mm:ssZ"),
        XEP_0082_TIME_MILLIS_PROFILE("hh:mm:ss.SSS"),
        XEP_0082_TIME_MILLIS_ZONE_PROFILE("hh:mm:ss.SSSZ"),
        XEP_0091_DATETIME("yyyyMMdd'T'HH:mm:ss");
        
        private final boolean CONVERT_TIMEZONE;
        private final DateFormat FORMATTER;
        private final String FORMAT_STRING;

        private DateFormatType(String dateFormat) {
            this.FORMAT_STRING = dateFormat;
            this.FORMATTER = new SimpleDateFormat(this.FORMAT_STRING);
            this.FORMATTER.setTimeZone(TimeZone.getTimeZone("UTC"));
            this.CONVERT_TIMEZONE = dateFormat.charAt(dateFormat.length() + -1) == 'Z';
        }

        public final String format(Date date) {
            synchronized (this.FORMATTER) {
                String res = this.FORMATTER.format(date);
            }
            if (this.CONVERT_TIMEZONE) {
                return XmppDateTime.convertRfc822TimezoneToXep82(res);
            }
            return res;
        }

        public final Date parse(String dateString) throws ParseException {
            Date parse;
            if (this.CONVERT_TIMEZONE) {
                dateString = XmppDateTime.convertXep82TimezoneToRfc822(dateString);
            }
            synchronized (this.FORMATTER) {
                parse = this.FORMATTER.parse(dateString);
            }
            return parse;
        }
    }

    private static class PatternCouplings {
        final DateFormatType formatter;
        final Pattern pattern;

        public PatternCouplings(Pattern datePattern, DateFormatType dateFormat) {
            this.pattern = datePattern;
            this.formatter = dateFormat;
        }
    }

    static {
        dateFormatter = DateFormatType.XEP_0082_DATE_PROFILE;
        datePattern = Pattern.compile("^\\d+-\\d+-\\d+$");
        timeFormatter = DateFormatType.XEP_0082_TIME_MILLIS_ZONE_PROFILE;
        timePattern = Pattern.compile("^(\\d+:){2}\\d+.\\d+(Z|([+-](\\d+:\\d+)))$");
        timeNoZoneFormatter = DateFormatType.XEP_0082_TIME_MILLIS_PROFILE;
        timeNoZonePattern = Pattern.compile("^(\\d+:){2}\\d+.\\d+$");
        timeNoMillisFormatter = DateFormatType.XEP_0082_TIME_ZONE_PROFILE;
        timeNoMillisPattern = Pattern.compile("^(\\d+:){2}\\d+(Z|([+-](\\d+:\\d+)))$");
        timeNoMillisNoZoneFormatter = DateFormatType.XEP_0082_TIME_PROFILE;
        timeNoMillisNoZonePattern = Pattern.compile("^(\\d+:){2}\\d+$");
        dateTimeFormatter = DateFormatType.XEP_0082_DATETIME_MILLIS_PROFILE;
        dateTimePattern = Pattern.compile("^\\d+(-\\d+){2}+T(\\d+:){2}\\d+.\\d+(Z|([+-](\\d+:\\d+)))?$");
        dateTimeNoMillisFormatter = DateFormatType.XEP_0082_DATETIME_PROFILE;
        dateTimeNoMillisPattern = Pattern.compile("^\\d+(-\\d+){2}+T(\\d+:){2}\\d+(Z|([+-](\\d+:\\d+)))?$");
        xep0091Formatter = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
        xep0091Date6DigitFormatter = new SimpleDateFormat("yyyyMd'T'HH:mm:ss");
        xep0091Date7Digit1MonthFormatter = new SimpleDateFormat("yyyyMdd'T'HH:mm:ss");
        xep0091Date7Digit2MonthFormatter = new SimpleDateFormat("yyyyMMd'T'HH:mm:ss");
        xep0091Pattern = Pattern.compile("^\\d+T\\d+:\\d+:\\d+$");
        couplings = new ArrayList();
        TimeZone utc = TimeZone.getTimeZone("UTC");
        xep0091Formatter.setTimeZone(utc);
        xep0091Date6DigitFormatter.setTimeZone(utc);
        xep0091Date7Digit1MonthFormatter.setTimeZone(utc);
        xep0091Date7Digit1MonthFormatter.setLenient(false);
        xep0091Date7Digit2MonthFormatter.setTimeZone(utc);
        xep0091Date7Digit2MonthFormatter.setLenient(false);
        couplings.add(new PatternCouplings(datePattern, dateFormatter));
        couplings.add(new PatternCouplings(dateTimePattern, dateTimeFormatter));
        couplings.add(new PatternCouplings(dateTimeNoMillisPattern, dateTimeNoMillisFormatter));
        couplings.add(new PatternCouplings(timePattern, timeFormatter));
        couplings.add(new PatternCouplings(timeNoZonePattern, timeNoZoneFormatter));
        couplings.add(new PatternCouplings(timeNoMillisPattern, timeNoMillisFormatter));
        couplings.add(new PatternCouplings(timeNoMillisNoZonePattern, timeNoMillisNoZoneFormatter));
    }

    public static Date parseXEP0082Date(String dateString) throws ParseException {
        Date parse;
        for (PatternCouplings coupling : couplings) {
            if (coupling.pattern.matcher(dateString).matches()) {
                return coupling.formatter.parse(dateString);
            }
        }
        synchronized (dateTimeNoMillisFormatter) {
            parse = dateTimeNoMillisFormatter.parse(dateString);
        }
        return parse;
    }

    public static Date parseDate(String dateString) throws ParseException {
        if (xep0091Pattern.matcher(dateString).matches()) {
            int length = dateString.split("T")[0].length();
            Date date;
            if (length < 8) {
                date = handleDateWithMissingLeadingZeros(dateString, length);
                if (date != null) {
                    return date;
                }
            }
            synchronized (xep0091Formatter) {
                date = xep0091Formatter.parse(dateString);
            }
            return date;
        }
        return parseXEP0082Date(dateString);
    }

    public static String formatXEP0082Date(Date date) {
        String format;
        synchronized (dateTimeFormatter) {
            format = dateTimeFormatter.format(date);
        }
        return format;
    }

    public static String convertXep82TimezoneToRfc822(String dateString) {
        if (dateString.charAt(dateString.length() - 1) == 'Z') {
            return dateString.replace("Z", "+0000");
        }
        return dateString.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)", "$1$2");
    }

    public static String convertRfc822TimezoneToXep82(String dateString) {
        int length = dateString.length();
        return (dateString.substring(0, length - 2) + ':') + dateString.substring(length - 2, length);
    }

    public static String asString(TimeZone timeZone) {
        int rawOffset = timeZone.getRawOffset();
        int minutes = Math.abs((rawOffset / 60000) - ((rawOffset / 3600000) * 60));
        return String.format("%+d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes)});
    }

    private static Date handleDateWithMissingLeadingZeros(String stampString, int dateLength) throws ParseException {
        if (dateLength == 6) {
            Date parse;
            synchronized (xep0091Date6DigitFormatter) {
                parse = xep0091Date6DigitFormatter.parse(stampString);
            }
            return parse;
        }
        Calendar now = Calendar.getInstance();
        Calendar oneDigitMonth = parseXEP91Date(stampString, xep0091Date7Digit1MonthFormatter);
        Calendar twoDigitMonth = parseXEP91Date(stampString, xep0091Date7Digit2MonthFormatter);
        Calendar[] calendarArr = new Calendar[]{oneDigitMonth, twoDigitMonth};
        List<Calendar> dates = new ArrayList();
        for (int i = 0; i < 2; i++) {
            Calendar calendar = calendarArr[i];
            if (calendar != null && calendar.before(now)) {
                dates.add(calendar);
            }
        }
        if (dates.isEmpty()) {
            return null;
        }
        Collections.sort(dates, new C13561(now));
        return ((Calendar) dates.get(0)).getTime();
    }

    private static Calendar parseXEP91Date(String stampString, DateFormat dateFormat) {
        try {
            Calendar calendar;
            synchronized (dateFormat) {
                dateFormat.parse(stampString);
                calendar = dateFormat.getCalendar();
            }
            return calendar;
        } catch (ParseException e) {
            return null;
        }
    }
}
