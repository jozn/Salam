package org.jivesoftware.smack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.compression.Java7ZlibInputOutputStream;
import org.jivesoftware.smack.initializer.SmackInitializer;
import org.jivesoftware.smack.util.FileUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public final class SmackInitialization {
    private static final Logger LOGGER;
    static final String SMACK_VERSION;

    static {
        String smackVersion;
        int i$;
        int len$;
        LOGGER = Logger.getLogger(SmackInitialization.class.getName());
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.getStreamForUrl("classpath:org.jivesoftware.smack/version", null)));
            smackVersion = reader.readLine();
            try {
                reader.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "IOException closing stream", e);
            }
        } catch (Exception e2) {
            LOGGER.log(Level.SEVERE, "Could not determine Smack version", e2);
            smackVersion = "unkown";
        }
        SMACK_VERSION = smackVersion;
        String disabledClasses = System.getProperty("smack.disabledClasses");
        if (disabledClasses != null) {
            for (String s : disabledClasses.split(",")) {
                SmackConfiguration.disabledSmackClasses.add(s);
            }
        }
        try {
            FileUtils.addLines("classpath:org.jivesoftware.smack/disabledClasses", SmackConfiguration.disabledSmackClasses);
            try {
                String[] sa = (String[]) Class.forName("org.jivesoftware.smack.CustomSmackConfiguration").getField("DISABLED_SMACK_CLASSES").get(null);
                if (sa != null) {
                    LOGGER.warning("Using CustomSmackConfig is deprecated and will be removed in a future release");
                    String[] arr$ = sa;
                    len$ = sa.length;
                    for (i$ = 0; i$ < len$; i$++) {
                        SmackConfiguration.disabledSmackClasses.add(arr$[i$]);
                    }
                }
            } catch (ClassNotFoundException e3) {
            } catch (NoSuchFieldException e4) {
            } catch (SecurityException e5) {
            } catch (IllegalArgumentException e6) {
            } catch (IllegalAccessException e7) {
            }
            try {
                try {
                    processConfigFile$48d0c56f(FileUtils.getStreamForUrl("classpath:org.jivesoftware.smack/smack-config.xml", null));
                    SmackConfiguration.compressionHandlers.add(new Java7ZlibInputOutputStream());
                    try {
                        SmackConfiguration.DEBUG_ENABLED = Boolean.getBoolean("smack.debugEnabled");
                    } catch (Exception e8) {
                    }
                    SmackConfiguration.smackInitialized = true;
                } catch (Exception e22) {
                    throw new IllegalStateException(e22);
                }
            } catch (Exception e222) {
                throw new IllegalStateException(e222);
            }
        } catch (Exception e2222) {
            throw new IllegalStateException(e2222);
        }
    }

    private static void processConfigFile$48d0c56f(InputStream cfgFileStream) throws Exception {
        processConfigFile(cfgFileStream, null, SmackInitialization.class.getClassLoader());
    }

    public static void processConfigFile(InputStream cfgFileStream, Collection<Exception> exceptions, ClassLoader classLoader) throws Exception {
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
        parser.setInput(cfgFileStream, "UTF-8");
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                if (parser.getName().equals("startupClasses")) {
                    parseClassesToLoad(parser, false, exceptions, classLoader);
                } else if (parser.getName().equals("optionalStartupClasses")) {
                    parseClassesToLoad(parser, true, exceptions, classLoader);
                }
            }
            eventType = parser.next();
        } while (eventType != 1);
        try {
            cfgFileStream.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while closing config file input stream", e);
        }
    }

    private static void parseClassesToLoad(XmlPullParser parser, boolean optional, Collection<Exception> exceptions, ClassLoader classLoader) throws XmlPullParserException, IOException, Exception {
        String startName = parser.getName();
        while (true) {
            int eventType = parser.next();
            String name = parser.getName();
            if (eventType == 2 && "className".equals(name)) {
                String classToLoad = parser.nextText();
                for (String disabledClassOrPackage : SmackConfiguration.disabledSmackClasses) {
                    if (!disabledClassOrPackage.equals(classToLoad)) {
                        int lastDotIndex = disabledClassOrPackage.lastIndexOf(46);
                        if (disabledClassOrPackage.length() > lastDotIndex && !Character.isUpperCase(disabledClassOrPackage.charAt(lastDotIndex + 1)) && classToLoad.startsWith(disabledClassOrPackage)) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                try {
                    Class cls = Class.forName(classToLoad, true, classLoader);
                    if (SmackInitializer.class.isAssignableFrom(cls)) {
                        List<Exception> initialize = ((SmackInitializer) cls.newInstance()).initialize();
                        if (initialize == null || initialize.size() == 0) {
                            LOGGER.log(Level.FINE, "Loaded SmackInitializer " + classToLoad);
                        } else {
                            for (Exception log : initialize) {
                                LOGGER.log(Level.SEVERE, "Exception in loadSmackClass", log);
                            }
                        }
                    } else {
                        LOGGER.log(Level.FINE, "Loaded " + classToLoad);
                    }
                } catch (ClassNotFoundException e) {
                    LOGGER.log(optional ? Level.FINE : Level.WARNING, "A startup class '" + classToLoad + "' could not be loaded.");
                    if (!optional) {
                        break;
                        throw e;
                    }
                } catch (Exception e2) {
                    if (exceptions != null) {
                        exceptions.add(e2);
                    } else {
                        break;
                        throw e2;
                    }
                }
            }
            if (eventType == 3 && startName.equals(name)) {
                return;
            }
        }
        throw e;
    }
}
