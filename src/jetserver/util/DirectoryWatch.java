
package jetserver.util;

import java.io.*;
import java.util.*;

/**
 * This class can watch a directory add report changes to listeners
 *
 * It only reports changes of files in the watched directory, no subdirectory
 * support
 */

public class DirectoryWatch {
    
    private File watchDir;
    private long checkInterval;
    private WatchThread watchThread;
    private Collection watchDirItems;    
    private Collection listeners;

    public DirectoryWatch(File watchDir, long checkInterval) {
	this.watchDir = watchDir;
	this.checkInterval = checkInterval;
	this.watchDirItems = new ArrayList();
	this.listeners = new ArrayList();
    }
    
    public void addListener(DirectoryListener listener) {
	listeners.add(listener);
    }    

    public void startWatching() {
	this.watchThread = new WatchThread();
    }

    public void stopWatching() {
	if (this.watchThread != null) {
	    this.watchThread.shutdown();
	}
	this.watchThread = null;
    }

    private void fileFound(WatchDirItem item) {
	Iterator iter = listeners.iterator();
	while (iter.hasNext()) {
	    DirectoryListener listener = (DirectoryListener) iter.next();
	    listener.fileFound(item.getFile());
	}
    }

    private void fileChanged(WatchDirItem item) {
	Iterator iter = listeners.iterator();
	while (iter.hasNext()) {
	    DirectoryListener listener = (DirectoryListener) iter.next();
	    listener.fileChanged(item.getFile());
	}
    }

    private void fileLost(WatchDirItem item) {
	Iterator iter = listeners.iterator();
	while (iter.hasNext()) {
	    DirectoryListener listener = (DirectoryListener) iter.next();
	    listener.fileLost(item.getFile());
	}
    }

    /**
     * Scan the watched dir for changes 
     */
    private void scanWatchDir() {	
	/* Check already found items */
	Iterator iter = watchDirItems.iterator();
	while (iter.hasNext()) {
	    WatchDirItem item = (WatchDirItem) iter.next();

	    /* has the file been removed? */	    
	    if (!item.getFile().exists()) {
		iter.remove();
		fileLost(item);
	    
	    } else if (item.hasChanged()) {
		
		/* has file been updated? */
		item.resetHasChanged();
		fileChanged(item);
	    }
	}

	/* Check for new files */
	File[] files = watchDir.listFiles();
	if (files != null) {
	    for (int i = 0; i < files.length; i++) {
		boolean isAlreadyFound = false;
		iter = watchDirItems.iterator();
		while (iter.hasNext()) {
		    WatchDirItem item = (WatchDirItem) iter.next();
		    if (item.getFile().equals(files[i])) {
			isAlreadyFound = true;
		    }
		}

		if (!isAlreadyFound) {
		    WatchDirItem newItem = new WatchDirItem(files[i]);
		    watchDirItems.add(newItem);
		    fileFound(newItem);
		}
	    } 
	}
    }

    private class WatchDirItem {
	private File file;
	private long lastModified;
	
	public WatchDirItem(File file) {
	    this.file = file;
	    this.lastModified = file.lastModified();
	}

	public File getFile() {
	    return this.file;
	}

	public boolean hasChanged() {
	    return this.lastModified != file.lastModified();
	}

	public void resetHasChanged() {
	    this.lastModified = file.lastModified();
	}
    }

    private class WatchThread extends Thread {
	private boolean shutdown = false;
	
	public WatchThread() {
	    this.start();
        }
	public void shutdown() {
	    shutdown = true;
	}
        public void run() {
	    while (!shutdown) {
		scanWatchDir();
		try {
		    Thread.sleep(checkInterval);
		} catch (InterruptedException e) {}
	    }
        }
    }
}
