
package jetserver.server;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.server.web.WebServer;
import jetserver.server.naming.JetServerContext;
import jetserver.util.Log;

import javax.naming.Context;

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

        initializeNaming();
        Runtime.getRuntime().addShutdownHook(new ShutdownManager());

        log.info("started");
    }

    private void initializeNaming() {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                           "jetserver.server.naming.JetServerContextFactory");
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
