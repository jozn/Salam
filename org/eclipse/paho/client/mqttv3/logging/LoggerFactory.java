package org.eclipse.paho.client.mqttv3.logging;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LoggerFactory {
    private static final String CLASS_NAME;
    public static final String MQTT_CLIENT_MSG_CAT = "org.eclipse.paho.client.mqttv3.internal.nls.logcat";
    static Class class$0;
    static Class class$1;
    private static String jsr47LoggerClassName;
    private static String overrideloggerClassName;

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.logging.LoggerFactory");
                class$0 = cls;
            } catch (Throwable e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
        overrideloggerClassName = null;
        jsr47LoggerClassName = "org.eclipse.paho.client.mqttv3.logging.JSR47Logger";
    }

    public static Logger getLogger(String messageCatalogName, String loggerID) {
        String loggerClassName = overrideloggerClassName;
        if (loggerClassName == null) {
            loggerClassName = jsr47LoggerClassName;
        }
        Logger logger = getLogger(loggerClassName, ResourceBundle.getBundle(messageCatalogName), loggerID, null);
        if (logger != null) {
            return logger;
        }
        throw new MissingResourceException("Error locating the logging class", CLASS_NAME, loggerID);
    }

    private static Logger getLogger(String loggerClassName, ResourceBundle messageCatalog, String loggerID, String resourceName) {
        Logger logger = null;
        try {
            Class logClass = Class.forName(loggerClassName);
            if (logClass != null) {
                try {
                    logger = (Logger) logClass.newInstance();
                    logger.initialise(messageCatalog, loggerID, resourceName);
                } catch (IllegalAccessException e) {
                    return null;
                } catch (InstantiationException e2) {
                    return null;
                } catch (ExceptionInInitializerError e3) {
                    return null;
                } catch (SecurityException e4) {
                    return null;
                }
            }
            return logger;
        } catch (NoClassDefFoundError e5) {
            return null;
        } catch (ClassNotFoundException e6) {
            return null;
        }
    }

    public static String getLoggingProperty(String name) {
        try {
            Class logManagerClass = Class.forName("java.util.logging.LogManager");
            Object logManagerInstance = logManagerClass.getMethod("getLogManager", new Class[0]).invoke(null, null);
            String str = "getProperty";
            Class[] clsArr = new Class[1];
            Class cls = class$1;
            if (cls == null) {
                cls = Class.forName("java.lang.String");
                class$1 = cls;
            }
            clsArr[0] = cls;
            return (String) logManagerClass.getMethod(str, clsArr).invoke(logManagerInstance, new Object[]{name});
        } catch (Throwable e) {
            throw new NoClassDefFoundError(e.getMessage());
        } catch (Exception e2) {
            return null;
        }
    }

    public static void setLogger(String loggerClassName) {
        overrideloggerClassName = loggerClassName;
    }
}
