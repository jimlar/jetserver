
package jetserver.server.deploymanager;

import java.io.*;
import java.util.*;

import jetserver.config.ServerConfig;
import jetserver.server.ContainerManager;
import jetserver.util.Log;

public class DeployManager implements DirectoryListener {
    
    private ContainerManager containerManager;
    private DirectoryWatch dropZoneWatch;
    private File dropZone;
    private File deployDir;

    private Log log;

    public DeployManager(ContainerManager containerManager) {
	ServerConfig config = ServerConfig.getInstance();
	this.containerManager = containerManager;
	this.deployDir = config.getFile("jetserver.deploymanager.deploy-dir");
	this.dropZone = config.getFile("jetserver.deploymanager.dropzone");
	this.dropZoneWatch = new DirectoryWatch(dropZone, 1000);
	this.dropZoneWatch.addListener(this);
	this.log = Log.getInstance(this);
	log.info("Deplymanager initialized, dropZone=" + dropZone.getAbsolutePath());
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
		containerManager.deployWebApplication(unpackDir);
	    } catch (IOException e) {
		log.error("Cant unpack and deploy " + file, e);
	    }
	}
    }

    public void fileChanged(File file) {
	log.debug("fileChanged " + file);
    }

    public void fileLost(File file) {
	log.debug("fileLost " + file);
    }
}
