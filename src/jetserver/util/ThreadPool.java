
package jetserver.util;

import java.util.*;

public class ThreadPool {

    private int poolSize;
    private List freeThreads;

    public ThreadPool(int poolSize) {
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
    public synchronized void startRunnable(Runnable runnable) {

	if (freeThreads.isEmpty()) {
	    createNewThread();
	}
	
	PoolThread thread = (PoolThread) freeThreads.remove(0);
	thread.setRunnableAndStart(runnable);
    }

    private synchronized void returnThread(PoolThread thread) {
	freeThreads.add(thread);
    }

    private void createNewThread() {
	new PoolThread(this);
    }

    private class PoolThread extends Thread {

	private Runnable runnable;
	private ThreadPool threadPool;

	public PoolThread(ThreadPool threadPool) {
	    this.threadPool = threadPool;
	    this.start();
	}

	public void setRunnableAndStart(Runnable runnable) {
	    this.runnable = runnable;
	    synchronized (this) {
		this.notify();
	    }
	}

	public void run() {
	    
	    while (true) {
		if (runnable != null) {
		    runnable.run();
		}
		
		threadPool.returnThread(this);

		try {
		    synchronized (this) {
			this.wait();
		    }
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
    }
}
