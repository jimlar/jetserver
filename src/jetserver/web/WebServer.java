
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.util.*;

public class WebServer extends Thread {

    private int port;
    private ServerSocket serverSocket;
    private WebServerThreadPool threadPool;

    public static byte data[];

    public WebServer(int port) {
	this.port = port;
	this.start();
    }

    public void run() {

	try {
	    File file = new File("/home/jimmy/orion/default-web-app/index.html");
	    InputStream in = new FileInputStream(file);
	    data = new byte[(int) file.length()];
	    in.read(data);

	    this.serverSocket = new ServerSocket(port);
	    this.threadPool = new WebServerThreadPool(50);

	    while (true) {
		threadPool.startThread(serverSocket.accept());
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void main(String args[]) throws Exception {
	WebServer webServer = new WebServer(8080);
    }
}
