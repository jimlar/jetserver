package jetserver.server.web;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import jetserver.server.Deployer;
import jetserver.server.config.ServerConfig;
import jetserver.server.config.ThreadsConfig;
import jetserver.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer extends Thread {

    private Deployer deployer;

    private int port;
    private int socketTimeout;
    private ServerSocket serverSocket;

    private PooledExecutor executor;

    public WebServer(Deployer deployer)
            throws IOException {
        this.deployer = deployer;

        ServerConfig config = ServerConfig.getInstance();
        this.socketTimeout = config.getWeb().getSocket().getTimeout() * 1000;
        this.port = config.getWeb().getSocket().getPort();
        this.serverSocket = new ServerSocket(port);

        ThreadsConfig threadsConfig = config.getWeb().getThreads();
        int maxThreads = threadsConfig.getMax();
        int minThreads = threadsConfig.getMin();
        int startThreads = threadsConfig.getStart();
        int keepAliveTime = threadsConfig.getKeepAliveTime() * 1000;

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
        } catch (IOException e) {
        }
    }
}
