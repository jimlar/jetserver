
package jetserver.server.deploymanager;

import java.io.*;
import java.util.*;

import jetserver.config.ServerConfig;


public class DeployManager {
    
    private DropZoneWatch dropZoneWatch;
    private File dropZone;
    private Collection dropZoneItems;
    

    public DeployManager() {
	ServerConfig config = ServerConfig.getInstance();
	this.dropZone = config.getFile("jetserver.deploymanager.dropzone");
	this.dropZoneItems = new ArrayList();
	this.dropZoneWatch = new DropZoneWatch();
	System.out.println("Deplymanager initialized, dropZone=" + dropZone.getAbsolutePath());
    }
    
    private void deploy(DropZoneItem item) {
	System.out.println("Deploying " + item.getFile().getAbsolutePath());
    }

    private void redeploy(DropZoneItem item) {
	System.out.println("Re-deploying " + item.getFile().getAbsolutePath());
    }

    private void undeploy(DropZoneItem item) {
	System.out.println("undeploying " + item.getFile().getAbsolutePath());
    }

    private void scanDropZone() {
	
	/* Check already deployed items */
	Iterator iter = dropZoneItems.iterator();
	while (iter.hasNext()) {
	    DropZoneItem item = (DropZoneItem) iter.next();

	    /* if the item has been removed, undeploy */	    
	    if (!item.getFile().exists()) {
		iter.remove();
		undeploy(item);
	    
	    } else if (item.hasChanged()) {
		
		/* if file has been updated, redeploy it */
		item.resetHasChanged();
		redeploy(item);
	    }
	}

	/* Check for new files */
	File[] files = dropZone.listFiles();
	if (files != null) {
	    for (int i = 0; i < files.length; i++) {
		boolean isDeployed = false;
		iter = dropZoneItems.iterator();
		while (iter.hasNext()) {
		    DropZoneItem item = (DropZoneItem) iter.next();
		    if (item.getFile().equals(files[i])) {
			isDeployed = true;
		    }
		}

		if (!isDeployed) {
		    DropZoneItem newItem = new DropZoneItem(files[i]);
		    dropZoneItems.add(newItem);
		    deploy(newItem);
		}
	    } 
	}
    }

    private class DropZoneItem {
	private File file;
	private long lastModified;
	
	public DropZoneItem(File file) {
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

    private class DropZoneWatch extends Thread {
	public DropZoneWatch() {
	    this.start();
        }

        public void run() {
	    while (true) {

		scanDropZone();

		try {
		    Thread.sleep(1000);
		} catch (InterruptedException e) {}
	    }
        }
    }
}
