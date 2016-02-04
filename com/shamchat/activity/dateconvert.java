package com.shamchat.activity;

import android.support.v7.appcompat.BuildConfig;
import com.kyleduo.switchbutton.C0473R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class dateconvert {

    public class SolarCalendar {
        int date;
        SimpleDateFormat formatter;
        int month;
        public String strMonth;
        public String strWeekDay;
        int year;

        public SolarCalendar(String clr) throws ParseException {
            this.strWeekDay = BuildConfig.VERSION_NAME;
            this.strMonth = BuildConfig.VERSION_NAME;
            this.formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date a = this.formatter.parse(clr);
            int year = a.getYear() + 1900;
            int month = a.getMonth() + 1;
            int date = a.getDate();
            int day = a.getDay();
            r5 = new int[12];
            int[] iArr = new int[]{0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
            iArr[0] = 0;
            iArr[1] = 31;
            iArr[2] = 60;
            iArr[3] = 91;
            iArr[4] = 121;
            iArr[5] = 152;
            iArr[6] = 182;
            iArr[7] = 213;
            iArr[8] = 244;
            iArr[9] = 274;
            iArr[10] = 305;
            iArr[11] = 335;
            if (year % 4 != 0) {
                this.date = r5[month - 1] + date;
                if (this.date > 79) {
                    this.date -= 79;
                    if (this.date <= 186) {
                        switch (this.date % 31) {
                            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                                this.month = this.date / 31;
                                this.date = 31;
                                break;
                            default:
                                this.month = (this.date / 31) + 1;
                                this.date %= 31;
                                break;
                        }
                        this.year = year - 621;
                    } else {
                        this.date -= 186;
                        switch (this.date % 30) {
                            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                                this.month = (this.date / 30) + 6;
                                this.date = 30;
                                break;
                            default:
                                this.month = (this.date / 30) + 7;
                                this.date %= 30;
                                break;
                        }
                        this.year = year - 621;
                    }
                } else {
                    month = (year <= 1996 || year % 4 != 1) ? 10 : 11;
                    this.date = month + this.date;
                    switch (this.date % 30) {
                        case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                            this.month = (this.date / 30) + 9;
                            this.date = 30;
                            break;
                        default:
                            this.month = (this.date / 30) + 10;
                            this.date %= 30;
                            break;
                    }
                    this.year = year - 622;
                }
            } else {
                this.date = iArr[month - 1] + date;
                month = year >= 1996 ? 79 : 80;
                if (this.date > month) {
                    this.date -= month;
                    if (this.date <= 186) {
                        switch (this.date % 31) {
                            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                                this.month = this.date / 31;
                                this.date = 31;
                                break;
                            default:
                                this.month = (this.date / 31) + 1;
                                this.date %= 31;
                                break;
                        }
                        this.year = year - 621;
                    } else {
                        this.date -= 186;
                        switch (this.date % 30) {
                            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                                this.month = (this.date / 30) + 6;
                                this.date = 30;
                                break;
                            default:
                                this.month = (this.date / 30) + 7;
                                this.date %= 30;
                                break;
                        }
                        this.year = year - 621;
                    }
                } else {
                    this.date += 10;
                    switch (this.date % 30) {
                        case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                            this.month = (this.date / 30) + 9;
                            this.date = 30;
                            break;
                        default:
                            this.month = (this.date / 30) + 10;
                            this.date %= 30;
                            break;
                    }
                    this.year = year - 622;
                }
            }
            switch (this.month) {
                case Logger.SEVERE /*1*/:
                    this.strMonth = "\u0641\u0631\u0648\u0631\u062f\u06cc\u0646";
                    break;
                case Logger.WARNING /*2*/:
                    this.strMonth = "\u0627\u0631\u062f\u06cc\u0628\u0647\u0634\u062a";
                    break;
                case Logger.INFO /*3*/:
                    this.strMonth = "\u062e\u0631\u062f\u0627\u062f";
                    break;
                case Logger.CONFIG /*4*/:
                    this.strMonth = "\u062a\u06cc\u0631";
                    break;
                case Logger.FINE /*5*/:
                    this.strMonth = "\u0645\u0631\u062f\u0627\u062f";
                    break;
                case Logger.FINER /*6*/:
                    this.strMonth = "\u0634\u0647\u0631\u06cc\u0648\u0631";
                    break;
                case Logger.FINEST /*7*/:
                    this.strMonth = "\u0645\u0647\u0631";
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    this.strMonth = "\u0622\u0628\u0627\u0646";
                    break;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    this.strMonth = "\u0622\u0630\u0631";
                    break;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    this.strMonth = "\u062f\u06cc";
                    break;
                case C0473R.styleable.SwitchButton_offColor /*11*/:
                    this.strMonth = "\u0628\u0647\u0645\u0646";
                    break;
                case C0473R.styleable.SwitchButton_thumbColor /*12*/:
                    this.strMonth = "\u0627\u0633\u0641\u0646\u062f";
                    break;
            }
            switch (day) {
                case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                    this.strWeekDay = "\u06cc\u06a9\u0634\u0646\u0628\u0647";
                case Logger.SEVERE /*1*/:
                    this.strWeekDay = "\u062f\u0648\u0634\u0646\u0628\u0647";
                case Logger.WARNING /*2*/:
                    this.strWeekDay = "\u0633\u0647 \u0634\u0646\u0628\u0647";
                case Logger.INFO /*3*/:
                    this.strWeekDay = "\u0686\u0647\u0627\u0631\u0634\u0646\u0628\u0647";
                case Logger.CONFIG /*4*/:
                    this.strWeekDay = "\u067e\u0646\u062c \u0634\u0646\u0628\u0647";
                case Logger.FINE /*5*/:
                    this.strWeekDay = "\u062c\u0645\u0639\u0647";
                case Logger.FINER /*6*/:
                    this.strWeekDay = "\u0634\u0646\u0628\u0647";
                default:
            }
        }
    }

    public static String getCurrentShamsidate(String clr) {
        Locale loc = new Locale("en_US");
        dateconvert util = new dateconvert();
        try {
            util.getClass();
            return String.valueOf(new SolarCalendar(clr).year) + MqttTopic.TOPIC_LEVEL_SEPARATOR + String.format(loc, "%02d", new Object[]{Integer.valueOf(sc.month)}) + MqttTopic.TOPIC_LEVEL_SEPARATOR + String.format(loc, "%02d", new Object[]{Integer.valueOf(sc.date)});
        } catch (ParseException e) {
            return BuildConfig.VERSION_NAME;
        }
    }
}
