
package jetserver.server;

import java.io.*;
import java.util.*;

import jetserver.config.ServerConfig;
import jetserver.server.Deployer;
import jetserver.util.*;

public class DropZoneWatch implements DirectoryListener {
    
    private Deployer containerManager;
    private DirectoryWatch directoryWatch;
    private File dropZone;
    private File deployDir;

    private Log log;

    public DropZoneWatch(Deployer containerManager) {
        ServerConfig config = ServerConfig.getInstance();
        this.containerManager = containerManager;
        this.deployDir = config.getFile("jetserver.dropzonewatch.deploy-dir");
        this.dropZone = config.getFile("jetserver.dropzonewatch.dropzone");
        this.directoryWatch = new DirectoryWatch(dropZone, 1000);
        this.directoryWatch.addListener(this);
        this.log = Log.getInstance(this);
        log.info("initialized. Drop zone=" + dropZone.getAbsolutePath());
    }

    public void start() {
        this.directoryWatch.startWatching();
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

        } else if (jar.isEJBJar()) {

            try {
                File unpackDir = new File(deployDir, file.getName());
                jar.unpackTo(unpackDir);
                containerManager.deployEJBJar(unpackDir);

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
