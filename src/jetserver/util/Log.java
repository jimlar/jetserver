
package jetserver.util;

import jetserver.server.config.ServerConfig;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is a logging service for the JetServer system
 */

public class Log {

    private static final int DATE_UPDATE_INTERVAL = 2000;

    private static boolean isConfigured = false;
    private static PrintWriter errorWriter;
    private static PrintWriter secondErrorWriter;
    private static PrintWriter infoWriter;
    private static PrintWriter secondInfoWriter;
    private static PrintWriter debugWriter;
    private static PrintWriter secondDebugWriter;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String timeString;
    private static long lastDateUpdateMillis = 0;

    private String className;


    public static Log getInstance(Object caller) {

        checkConfig();
        String category;
        if (caller instanceof Class) {
            category = ((Class) caller).getName();
        } else {
            category = caller.getClass().getName();
        }

        if (category.indexOf(".") != -1) {
            category = category.substring(category.lastIndexOf(".") + 1);
        }

        return new Log(category);
    }

    /**
     * Check and possibly load configuration
     */
    private static void checkConfig() {
        if (!isConfigured) {
            try {
                isConfigured = true;
                ServerConfig config = ServerConfig.getInstance();

                File infoFile = config.getLog().getInfoLog().getLogFile();
                infoFile.getParentFile().mkdirs();
                infoWriter = new PrintWriter(new FileOutputStream(infoFile.getAbsolutePath(), true), true);
                if (config.getLog().getInfoLog().getCopyToStandardOut()) {
                    secondInfoWriter = new PrintWriter(System.out);
                }

                File errorFile = config.getLog().getErrorLog().getLogFile();
                errorFile.getParentFile().mkdirs();
                errorWriter = new PrintWriter(new FileOutputStream(errorFile.getAbsolutePath(), true));
                if (config.getLog().getErrorLog().getCopyToStandardOut()) {
                    secondErrorWriter = new PrintWriter(System.err);
                }

                if (config.getLog().getDebugLog().getEnabled()) {
                    File debugFile = config.getLog().getDebugLog().getLogFile();
                    debugFile.getParentFile().mkdirs();
                    debugWriter = new PrintWriter(new FileOutputStream(debugFile.getAbsolutePath(), true));
                    if (config.getLog().getDebugLog().getCopyToStandardOut()) {
                        secondDebugWriter = new PrintWriter(System.out);
                    }
                }

            } catch (IOException e) {
                System.err.println("Cant initialize log system!");
                e.printStackTrace();
            }
        }
    }


    private Log(String category) {
        this.className = category;
    }

    public void error(String message) {
        error(message, null);
    }

    public void error(String message, Throwable throwable) {
        printMessage("Error", message, throwable, errorWriter, secondErrorWriter);
    }

    public void info(String message) {
        info(message, null);
    }

    public void info(String message, Throwable throwable) {
        printMessage("Info ", message, throwable, infoWriter, secondInfoWriter);
    }

    public void debug(String message) {
        debug(message, null);
    }

    public void debug(String message, Throwable throwable) {
        printMessage("Debug", message, throwable, debugWriter, secondDebugWriter);
    }

    private void printMessage(String level, String message, Throwable throwable, PrintWriter writer, PrintWriter secondWriter) {
        printMessage(level, message, throwable, writer);
        printMessage(level, message, throwable, secondWriter);
    }

    private void printMessage(String level, String message, Throwable throwable, PrintWriter writer) {
        if (writer != null) {
            synchronized (writer) {
                if (throwable != null) {
                    writer.print("[" + getDateLazy() + "] ["
                                 + level + "] ["
                                 + className + "]: "
                                 + message + ": ");
                    throwable.printStackTrace(writer);
                } else {
                    writer.println("[" + getDateLazy() + "] ["
                                   + level + "] ["
                                   + className + "]: "
                                   + message);
                }
                writer.flush();
            }
        }
    }

    /**
     * Fetch a date string in a cacheing way, beacuse constructing and formatting dates
     * is very time consuming
     */
    private static String getDateLazy() {
        if (timeString == null
                || (lastDateUpdateMillis + DATE_UPDATE_INTERVAL) < System.currentTimeMillis()) {

            lastDateUpdateMillis = System.currentTimeMillis();
            Date date = new Date(lastDateUpdateMillis);
            timeString = dateFormat.format(date);
        }
        return timeString;
    }
}
