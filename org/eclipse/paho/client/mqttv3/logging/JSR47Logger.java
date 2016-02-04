package org.eclipse.paho.client.mqttv3.logging;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.MemoryHandler;

public class JSR47Logger implements Logger {
    private String catalogID;
    private Logger julLogger;
    private ResourceBundle logMessageCatalog;
    private String loggerName;
    private String resourceName;
    private ResourceBundle traceMessageCatalog;

    public JSR47Logger() {
        this.julLogger = null;
        this.logMessageCatalog = null;
        this.traceMessageCatalog = null;
        this.catalogID = null;
        this.resourceName = null;
        this.loggerName = null;
    }

    public void initialise(ResourceBundle logMsgCatalog, String loggerID, String resourceContext) {
        this.traceMessageCatalog = this.logMessageCatalog;
        this.resourceName = resourceContext;
        this.loggerName = loggerID;
        this.julLogger = Logger.getLogger(this.loggerName);
        this.logMessageCatalog = logMsgCatalog;
        this.traceMessageCatalog = logMsgCatalog;
        this.catalogID = this.logMessageCatalog.getString("0");
    }

    public void setResourceName(String logContext) {
        this.resourceName = logContext;
    }

    public boolean isLoggable(int level) {
        return this.julLogger.isLoggable(mapJULLevel(level));
    }

    public void severe(String sourceClass, String sourceMethod, String msg) {
        log(1, sourceClass, sourceMethod, msg, null, null);
    }

    public void severe(String sourceClass, String sourceMethod, String msg, Object[] inserts) {
        log(1, sourceClass, sourceMethod, msg, inserts, null);
    }

    public void severe(String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable thrown) {
        log(1, sourceClass, sourceMethod, msg, inserts, thrown);
    }

    public void warning(String sourceClass, String sourceMethod, String msg) {
        log(2, sourceClass, sourceMethod, msg, null, null);
    }

    public void warning(String sourceClass, String sourceMethod, String msg, Object[] inserts) {
        log(2, sourceClass, sourceMethod, msg, inserts, null);
    }

    public void warning(String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable thrown) {
        log(2, sourceClass, sourceMethod, msg, inserts, thrown);
    }

    public void info(String sourceClass, String sourceMethod, String msg) {
        log(3, sourceClass, sourceMethod, msg, null, null);
    }

    public void info(String sourceClass, String sourceMethod, String msg, Object[] inserts) {
        log(3, sourceClass, sourceMethod, msg, inserts, null);
    }

    public void info(String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable thrown) {
        log(3, sourceClass, sourceMethod, msg, inserts, thrown);
    }

    public void config(String sourceClass, String sourceMethod, String msg) {
        log(4, sourceClass, sourceMethod, msg, null, null);
    }

    public void config(String sourceClass, String sourceMethod, String msg, Object[] inserts) {
        log(4, sourceClass, sourceMethod, msg, inserts, null);
    }

    public void config(String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable thrown) {
        log(4, sourceClass, sourceMethod, msg, inserts, thrown);
    }

    public void log(int level, String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable thrown) {
        Level julLevel = mapJULLevel(level);
        if (this.julLogger.isLoggable(julLevel)) {
            logToJsr47(julLevel, sourceClass, sourceMethod, this.catalogID, this.logMessageCatalog, msg, inserts, thrown);
        }
    }

    public void fine(String sourceClass, String sourceMethod, String msg) {
        trace(5, sourceClass, sourceMethod, msg, null, null);
    }

    public void fine(String sourceClass, String sourceMethod, String msg, Object[] inserts) {
        trace(5, sourceClass, sourceMethod, msg, inserts, null);
    }

    public void fine(String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable ex) {
        trace(5, sourceClass, sourceMethod, msg, inserts, ex);
    }

    public void finer(String sourceClass, String sourceMethod, String msg) {
        trace(6, sourceClass, sourceMethod, msg, null, null);
    }

    public void finer(String sourceClass, String sourceMethod, String msg, Object[] inserts) {
        trace(6, sourceClass, sourceMethod, msg, inserts, null);
    }

    public void finer(String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable ex) {
        trace(6, sourceClass, sourceMethod, msg, inserts, ex);
    }

    public void finest(String sourceClass, String sourceMethod, String msg) {
        trace(7, sourceClass, sourceMethod, msg, null, null);
    }

    public void finest(String sourceClass, String sourceMethod, String msg, Object[] inserts) {
        trace(7, sourceClass, sourceMethod, msg, inserts, null);
    }

    public void finest(String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable ex) {
        trace(7, sourceClass, sourceMethod, msg, inserts, ex);
    }

    public void trace(int level, String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable ex) {
        Level julLevel = mapJULLevel(level);
        if (this.julLogger.isLoggable(julLevel)) {
            logToJsr47(julLevel, sourceClass, sourceMethod, this.catalogID, this.traceMessageCatalog, msg, inserts, ex);
        }
    }

    private String getResourceMessage(ResourceBundle messageCatalog, String msg) {
        try {
            return messageCatalog.getString(msg);
        } catch (MissingResourceException e) {
            return msg;
        }
    }

    private void logToJsr47(Level julLevel, String sourceClass, String sourceMethod, String catalogName, ResourceBundle messageCatalog, String msg, Object[] inserts, Throwable thrown) {
        String formattedWithArgs = msg;
        if (msg.indexOf("=====") == -1) {
            formattedWithArgs = MessageFormat.format(getResourceMessage(messageCatalog, msg), inserts);
        }
        LogRecord logRecord = new LogRecord(julLevel, new StringBuffer(String.valueOf(this.resourceName)).append(": ").append(formattedWithArgs).toString());
        logRecord.setSourceClassName(sourceClass);
        logRecord.setSourceMethodName(sourceMethod);
        logRecord.setLoggerName(this.loggerName);
        if (thrown != null) {
            logRecord.setThrown(thrown);
        }
        this.julLogger.log(logRecord);
    }

    private Level mapJULLevel(int level) {
        switch (level) {
            case Logger.SEVERE /*1*/:
                return Level.SEVERE;
            case Logger.WARNING /*2*/:
                return Level.WARNING;
            case Logger.INFO /*3*/:
                return Level.INFO;
            case Logger.CONFIG /*4*/:
                return Level.CONFIG;
            case Logger.FINE /*5*/:
                return Level.FINE;
            case Logger.FINER /*6*/:
                return Level.FINER;
            case Logger.FINEST /*7*/:
                return Level.FINEST;
            default:
                return null;
        }
    }

    public String formatMessage(String msg, Object[] inserts) {
        try {
            return this.logMessageCatalog.getString(msg);
        } catch (MissingResourceException e) {
            return msg;
        }
    }

    public void dumpTrace() {
        dumpMemoryTrace47(this.julLogger);
    }

    protected static void dumpMemoryTrace47(Logger logger) {
        while (logger != null) {
            Handler[] handlers = logger.getHandlers();
            int i = 0;
            while (i < handlers.length) {
                if (handlers[i] instanceof MemoryHandler) {
                    synchronized (handlers[i]) {
                        ((MemoryHandler) handlers[i]).push();
                    }
                    return;
                }
                i++;
            }
            logger = logger.getParent();
        }
    }
}
