

package jetserver.util;


/**
 * This is a logging service for the JetServer system
 */

public class Log {

    private String category;


    public static Log getInstance(Object caller) {

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

    private Log(String category) {
	this.category = category;
    }

    public void error(String message) {
	error(message, null);
    }

    public void error(String message, Throwable throwable) {
	printMessage(message, throwable);
    }

    public void info(String message) {
	info(message, null);
    }

    public void info(String message, Throwable throwable) {
	printMessage(message, throwable);
    }

    public void debug(String message) {
	debug(message, null);
    }

    public void debug(String message, Throwable throwable) {
	printMessage(message, throwable);
    }

    private void printMessage(String message, Throwable throwable) {
	
	if (throwable != null) {
	    System.out.print("[" + category + "]: " + message + ": ");
	    throwable.printStackTrace();
	} else {
	    System.out.println("[" + category + "]: " + message);
	}
    }
}
