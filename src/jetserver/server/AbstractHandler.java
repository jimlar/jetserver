
package jetserver.server;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;

/**
 * To use the Reactor you subclass this class and implement
 * the abstract methods, then use the new class for the Reactor constructor
 *
 * Note: this class assumes that you are needing a request/response type of service
 */

public abstract class AbstractHandler implements Runnable {
    
    private static final int READING = 0;
    private static final int SENDING = 1;

    private final SocketChannel socket;
    private final SelectionKey selectionKey;
    private int state = READING;
    
    /**
     * Register ourselves and set us up to wait for incoming data
     */
    public AbstractHandler(Selector selector, Socket c) throws IOException {
	this.socket = c.getChannel();
	this.selectionKey = socket.register(selector, 0);
	this.selectionKey.attach(this);
	this.selectionKey.interestOps(SelectionKey.OP_READ);
	selector.wakeup();
    }
    
    /**
     * Run service
     */
    public void run() {
	try { 
	    if (state == READING) {
		read();
	    } else if (state == SENDING) {
		send();
	    }
	} catch (IOException e) { 
	    System.err.println("i/o error: " + e);
	}
    }

    /**
     * Read incoming data and call prepareForOutput if all data received
     */ 
    private void read() throws IOException {
	socket.read(getInputBuffer());
	if (isInputComplete()) {
	    prepareForOutput(); 
	    state = SENDING;
  	    selectionKey.interestOps(SelectionKey.OP_WRITE);
	}
    }

    /**
     * Send buffered data and close socket when done
     * - also deregisters this handler
     */
    private void send() throws IOException {
	socket.write(getOutputBuffer());
	if (isOutputComplete()) { 
	    selectionKey.cancel();
	    socket.close();
	}
    }

    /**
     * This should return a buffer used for inputtting data
     * (it's ok to not return the same instance on subsequent calls)
     */
    abstract ByteBuffer getOutputBuffer();
    /**
     * This should return a buffer used for outputtting data
     * (it's ok to not return the same instance on subsequent calls)
     */
    abstract ByteBuffer getInputBuffer();
    /**
     * @returns true if all needed data is read
     */
    abstract boolean isInputComplete();
    /**
     * The reason for this method instead of using the limit on the
     * output buffer is that its possible that you cant or dont want to 
     * put all your output data in a buffer at once
     *
     * @returns true if all data has been outputted 
     */
    abstract boolean isOutputComplete();

    /**
     * This is called when all input has been read, its used so that 
     * the handler can prepare whatever needs preparing before stating to output data
     * (fill a buffer etc.)
     */
    abstract void prepareForOutput();
}
