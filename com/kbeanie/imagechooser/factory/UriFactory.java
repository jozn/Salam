package com.kbeanie.imagechooser.factory;

public class UriFactory {
    public static String TAG;
    private static UriFactory instance;
    public String filePathOriginal;

    static {
        TAG = UriFactory.class.getSimpleName();
    }

    private UriFactory() {
    }

    public static UriFactory getInstance() {
        if (instance == null) {
            instance = new UriFactory();
        }
        return instance;
    }
}
