
package jetserver.server.web;

import java.io.*;
import java.net.*;
import java.util.*;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

import jetserver.server.config.ServerConfig;
import jetserver.server.Deployer;
import jetserver.util.Log;

public class WebServer extends Thread {
    
    private Deployer deployer;

    private int port;
    private int socketTimeout;
    private ServerSocket serverSocket;

    private PooledExecutor executor;

    public WebServer(Deployer deployer)
            throws IOException
    {
        this.deployer = deployer;

        ServerConfig config = ServerConfig.getInstance();
        this.socketTimeout = config.getInteger("jetserver.webcontainer.socket.timeout") * 1000;
        this.port = config.getInteger("jetserver.webcontainer.socket.port");
        this.serverSocket = new ServerSocket(port);

        int maxThreads = config.getInteger("jetserver.webcontainer.threads.max");
        int minThreads = config.getInteger("jetserver.webcontainer.threads.min");
        int startThreads = config.getInteger("jetserver.webcontainer.threads.start");
        int keepAliveTime = config.getInteger("jetserver.webcontainer.threads.keep-alive-time") * 1000;

        this.executor = new PooledExecutor(maxThreads);
        this.executor.runWhenBlocked();
        this.executor.setMinimumPoolSize(minThreads);
        this.executor.setKeepAliveTime(keepAliveTime);
        this.executor.createThreads(startThreads);
    }

    public int getPort() {
        return this.port;
    }

    public void run() {
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();

                if (socket != null) {
                    socket.setSoTimeout(socketTimeout);
                    executor.execute(new WebServerConnection(socket, deployer));
                }

            } catch (Exception e) {
                Log.getInstance(this).error("error accepting connection", e);
                hardKillSocket(socket);
            }
        }
    }

    private void hardKillSocket(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {}
    }
}
