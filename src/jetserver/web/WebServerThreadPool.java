
package jetserver.web;

import java.net.*;
import java.util.*;

public class WebServerThreadPool {

    private int poolSize;
    private WebServerThread[] threads;
    private boolean[] threadFreeMark;


    public WebServerThreadPool(int poolSize) {
	this.poolSize = poolSize;
	this.threads = new WebServerThread[poolSize];
	this.threadFreeMark = new boolean[poolSize];

	/* Create all Threads */
	for (int i = 0; i < poolSize; i++) {
	    threads[i] = createNewThread(i);

	    /* Threads will return themselves to the pool */
	    threadFreeMark[i] = false;

	    threads[i].start();
	}
    }
    
    /**
     * Start a runnable with an available thread
     */
    public synchronized void startThread(Socket socket) {

	for (int i = 0; i < threads.length; i++) {
	    if (threadFreeMark[i]) {
		threadFreeMark[i] = false;
		threads[i].setSocketAndStart(socket);
		return;
	    }
	}
	throw new RuntimeException("no free thread");
    }

    public synchronized void returnThread(WebServerThread thread) {
	this.threadFreeMark[thread.getThreadNumber()] = true;
    }

    private WebServerThread createNewThread(int threadNumber) {
	return new WebServerThread(this, threadNumber);
    }
}
