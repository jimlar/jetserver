

package jetserver.util;

import jetserver.config.ServerConfig;

import java.io.*;
import java.util.*;
import java.text.*;

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

    private String category;


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

                File infoFile = config.getFile("jetserver.logs.message-log.file");
                infoFile.getParentFile().mkdirs();
                infoWriter = new PrintWriter(new FileOutputStream(infoFile.getAbsolutePath(), true), true);
                if (config.getString("jetserver.logs.message-log.stdout").equals("true")) {
                    secondInfoWriter = new PrintWriter(System.out);
                }

                File errorFile = config.getFile("jetserver.logs.error-log.file");
                errorFile.getParentFile().mkdirs();
                errorWriter = new PrintWriter(new FileOutputStream(errorFile.getAbsolutePath(), true));
                if (config.getString("jetserver.logs.error-log.stderr").equals("true")) {
                    secondErrorWriter = new PrintWriter(System.err);
                }

                if (config.getString("jetserver.logs.debug-log.enabled").equals("true")) {
                    File debugFile = config.getFile("jetserver.logs.debug-log.file");
                    debugFile.getParentFile().mkdirs();
                    debugWriter = new PrintWriter(new FileOutputStream(debugFile.getAbsolutePath(), true));
                    if (config.getString("jetserver.logs.debug-log.stdout").equals("true")) {
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
        this.category = category;
    }

    public void error(String message) {
        error(message, null);
    }

    public void error(String message, Throwable throwable) {
        printMessage(message, throwable, errorWriter, secondErrorWriter);
    }

    public void info(String message) {
        info(message, null);
    }

    public void info(String message, Throwable throwable) {
        printMessage(message, throwable, infoWriter, secondInfoWriter);
    }

    public void debug(String message) {
        debug(message, null);
    }

    public void debug(String message, Throwable throwable) {
        printMessage(message, throwable, debugWriter, secondDebugWriter);
    }

    private void printMessage(String message, Throwable throwable, PrintWriter writer, PrintWriter secondWriter) {
        printMessage(message, throwable, writer);
        printMessage(message, throwable, secondWriter);
    }

    private void printMessage(String message, Throwable throwable, PrintWriter writer) {
        if (writer != null) {
            synchronized (writer) {
                if (throwable != null) {
                    writer.print("[" + getDateLazy() + "] [" + category + "]: " + message + ": ");
                    throwable.printStackTrace(writer);
                } else {
                    writer.println("[" + getDateLazy() + "] [" + category + "]: " + message);
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
                || (lastDateUpdateMillis + DATE_UPDATE_INTERVAL) <  System.currentTimeMillis()) {

            lastDateUpdateMillis = System.currentTimeMillis();
            Date date = new Date(lastDateUpdateMillis);
            timeString = dateFormat.format(date);
        }
        return timeString;
    }
}
