
package jetserver.server;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.server.web.WebServer;
import jetserver.util.Log;

public class JetServer {

    private Log log;
    private DropZoneWatch dropZoneWatch;
    private Deployer containerManager;

    public JetServer()
            throws IOException
    {
        this.log = Log.getInstance(this);
        this.containerManager = new Deployer();
        this.dropZoneWatch = new DropZoneWatch(containerManager);
        this.dropZoneWatch.start();

        Runtime.getRuntime().addShutdownHook(new ShutdownManager());

        log.info("started");
    }

    public static void main(String args[]) throws Exception {

        JetServer jetServer = new JetServer();

        while (true) {
            try {
                Thread.sleep(3600000);
            } catch (InterruptedException e) {}
        }
    }

    private class ShutdownManager extends Thread {
        public void run() {
            log.getInstance(this).info("shutdown");
        }
    }
}
