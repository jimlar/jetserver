
package jetserver.server;

import java.io.*;
import java.util.*;

import jetserver.server.config.ServerConfig;
import jetserver.server.Deployer;
import jetserver.util.*;

public class DropZoneWatch implements DirectoryListener {
    
    private Deployer deployer;
    private DirectoryWatch directoryWatch;
    private File dropZone;
    private File deployDir;

    private Log log;

    public DropZoneWatch(Deployer deployer) {
        ServerConfig config = ServerConfig.getInstance();
        this.deployer = deployer;
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
        deployer.deploy(file);
    }

    public void fileChanged(File file) {
        log.debug("fileChanged " + file);
    }

    public void fileLost(File file) {
        log.debug("fileLost " + file);
    }
}
