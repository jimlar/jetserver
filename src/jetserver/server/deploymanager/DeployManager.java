
package jetserver.server.deploymanager;

import java.io.*;
import java.util.*;

import jetserver.config.ServerConfig;
import jetserver.server.web.WebContainerManager;


public class DeployManager implements DirectoryListener {
    
    private WebContainerManager webContainerManager;
    private DirectoryWatch dropZoneWatch;
    private File dropZone;
    private File deployDir;

    public DeployManager(WebContainerManager webContainerManager) {
	ServerConfig config = ServerConfig.getInstance();
	this.webContainerManager = webContainerManager;
	this.deployDir = config.getFile("jetserver.deploymanager.deploy-dir");
	this.dropZone = config.getFile("jetserver.deploymanager.dropzone");
	this.dropZoneWatch = new DirectoryWatch(dropZone, 1000);
	this.dropZoneWatch.addListener(this);
	System.out.println("Deplymanager initialized, dropZone=" + dropZone.getAbsolutePath());
    }
    
    public void start() {
	this.dropZoneWatch.startWatching();
    }

    public void fileFound(File file) {
	EnterpriseJar jar = new EnterpriseJar(file);
	if (jar.isWebApplication()) {

	    try {
		File unpackDir = new File(deployDir, file.getName());
		jar.unpackTo(unpackDir);
		webContainerManager.deployWebApplication(unpackDir);
	    } catch (IOException e) {
		System.out.println("Cant unpack and deploy " + file);
		e.printStackTrace();
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
