
package jetserver.web;

import java.net.*;
import java.util.*;

public class WebServerThreadPool {

    private ServerSocket serverSocket;
    private int poolSize;
    private WebServerThread[] threads;
    private boolean[] threadFreeMark;


    public WebServerThreadPool(int poolSize, ServerSocket serverSocket) {
	this.poolSize = poolSize;
	this.serverSocket = serverSocket;
	this.threads = new WebServerThread[poolSize];
	this.threadFreeMark = new boolean[poolSize];

	/* Create all Threads */
	for (int i = 0; i < poolSize; i++) {
	    threads[i] = createNewThread(i);
	    threadFreeMark[i] = true;
	}
    }
    
    public synchronized void markThreadBusy(WebServerThread thread) {
	this.threadFreeMark[thread.getThreadNumber()] = false;
    }

    public synchronized void releaseThread(WebServerThread thread) {
	this.threadFreeMark[thread.getThreadNumber()] = true;
    }

    private WebServerThread createNewThread(int threadNumber) {
	WebServerThread thread = new WebServerThread(this, threadNumber, serverSocket);
	thread.start();
	return thread;
    }
}
