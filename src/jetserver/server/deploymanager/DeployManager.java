
package jetserver.server.deploymanager;

import java.io.*;
import java.util.*;

import jetserver.config.ServerConfig;


public class DeployManager implements DirectoryListener {
    
    private DirectoryWatch dropZoneWatch;
    private File dropZone;

    public DeployManager() {
	ServerConfig config = ServerConfig.getInstance();
	this.dropZone = config.getFile("jetserver.deploymanager.dropzone");
	this.dropZoneWatch = new DirectoryWatch(dropZone, 1000);
	this.dropZoneWatch.addListener(this);
	this.dropZoneWatch.startWatching();
	System.out.println("Deplymanager initialized, dropZone=" + dropZone.getAbsolutePath());
    }
    

    public void fileFound(File file) {
	JAR jar = new JAR(file);
	if (!jar.isValid()) {
	    System.out.println("Found non-jar file: " + file);
	} else {
	    if (jar.contains("META-INF/ejb-jar.xml")) {
		System.out.println("Found EJB jar: " + file);
	    } else if (jar.contains("WEB-INF/web.xml")) {
		System.out.println("Found web application: " + file);
	    } else if (jar.contains("META-INF/application.xml")) {
		System.out.println("Found J2EE application EAR: " + file);
	    } else {
		System.out.println("Found unhandled file: " + file);
	    }
	}
    }

    public void fileChanged(File file) {
	System.out.println("fileChanged " + file);
    }

    public void fileLost(File file) {
	System.out.println("fileLost " + file);
    }
}
