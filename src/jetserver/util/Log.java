

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
    private static PrintWriter infoWriter;
    private static PrintWriter debugWriter;

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
		ServerConfig config = ServerConfig.getInstance();

		File infoFile = config.getFile("jetserver.logs.info-log.file");
		infoFile.getParentFile().mkdirs();
		infoWriter = new PrintWriter(new FileOutputStream(infoFile), true);

		File errorFile = config.getFile("jetserver.logs.error-log.file");
		errorFile.getParentFile().mkdirs();
		errorWriter = new PrintWriter(new FileOutputStream(errorFile));

		if (config.getString("jetserver.logs.debug-log.enabled").equals("true")) {
		    File debugFile = config.getFile("jetserver.logs.debug-log.file");
		    debugFile.getParentFile().mkdirs();
		    debugWriter = new PrintWriter(new FileOutputStream(debugFile));
		} else {
		    debugWriter = null;
		}

	    } catch (IOException e) {
		System.err.println("Cant initialize log system!");
		e.printStackTrace();
	    }
	    isConfigured = true;
	}
    }


    private Log(String category) {
	this.category = category;
    }

    public void error(String message) {
	error(message, null);
    }

    public void error(String message, Throwable throwable) {
	printMessage(message, throwable, errorWriter);
    }

    public void info(String message) {
	info(message, null);
    }

    public void info(String message, Throwable throwable) {
	printMessage(message, throwable, infoWriter);
    }

    public void debug(String message) {
	debug(message, null);
    }

    public void debug(String message, Throwable throwable) {
	printMessage(message, throwable, debugWriter);
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
