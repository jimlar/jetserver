
package jetserver.web;

import java.net.*;
import java.util.*;

public class WebServerThreadPool {

    private int poolSize;
    private List freeThreads;

    public WebServerThreadPool(int poolSize) {
	this.poolSize = poolSize;
	this.freeThreads = new ArrayList();
	for (int i = 0; i < poolSize; i++) {
	    createNewThread();
	}
    }
    
    /**
     * Start a runnable with an available thread
     *
     */
    public synchronized void startThread(Socket socket) {

	if (freeThreads.isEmpty()) {
	    createNewThread();
	}
	
	WebServerThread thread = (WebServerThread) freeThreads.remove(0);
	thread.setSocketAndStart(socket);
    }

    public synchronized void returnThread(WebServerThread thread) {
	freeThreads.add(thread);
    }

    private void createNewThread() {
	new WebServerThread(this);
    }
}
