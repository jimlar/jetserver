package jetserver.util;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;

import java.lang.reflect.*;

/**
 * This is a general implementation of the Reactor patterna for
 * asyncronous socket communication
 *
 */

public class Reactor extends Thread {

    private final Selector selector;
    private final ServerSocketChannel serverSocket;
    private final Constructor handlerConstructor;

    public Reactor(int port, Class handlerClass) throws IOException {

	if (!AbstractHandler.class.isAssignableFrom(handlerClass)) {
	    throw new IllegalArgumentException("handlerclass has to be sub class of the AbstractHandler");
	}
	try {
	    this.handlerConstructor = handlerClass.getConstructor(new Class[] {Selector.class,
									  Socket.class});
	} catch (NoSuchMethodException e) {
	    throw new IllegalArgumentException("handlerclass has to have a costructor liek the one in AbstractHandler");
	}

	this.selector = Selector.open();
	this.serverSocket = ServerSocketChannel.open();
	this.serverSocket.socket().bind(new InetSocketAddress(port));
	this.serverSocket.configureBlocking(false);
	SelectionKey sk = serverSocket.register(selector, 
						SelectionKey.OP_ACCEPT);
	sk.attach(new Acceptor()); 
    }

    public void run() {
	try {
	    while (!Thread.interrupted()) {
		selector.select();
		Set selected = selector.selectedKeys();
		Iterator it = selected.iterator();
		while (it.hasNext()) {
		    dispatch((SelectionKey)(it.next()));
		    selected.clear(); 
		}
	    }
	} catch (IOException e) { 
	    System.err.println("Error running handler: " + e);
	    System.err.println("Reactor stopped");
	}
    }

    private void dispatch(SelectionKey k) {
	Runnable r = (Runnable)(k.attachment());
	if (r != null) {
	    r.run();
	} 
    }

    private class Acceptor implements Runnable {
	public void run() {
	    try {
		Socket connection = serverSocket.accept();
		if (connection != null) {		    
		    try {

			//Is this time consuming???

			handlerConstructor.newInstance(new Object[] {selector, connection});
		    } catch (InstantiationException e) {
			System.err.println("Cant create handler: " + e);
		    } catch (IllegalAccessException e) {
			System.err.println("Cant create handler: " + e);
		    } catch (InvocationTargetException e) {
			System.err.println("Cant create handler: " + e);
		    }			
		}		
	    } catch(IOException e) { 
		System.err.println("Cant accept() client connection: " + e);
	    }
	}  
    }
}
