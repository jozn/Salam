package com.kbeanie.imagechooser.factory;

public class DateFactory {
    public static String TAG;
    private static DateFactory instance;
    public Long timeInMillis;

    static {
        TAG = DateFactory.class.getSimpleName();
    }

    private DateFactory() {
    }

    public static DateFactory getInstance() {
        if (instance == null) {
            instance = new DateFactory();
        }
        return instance;
    }
}
