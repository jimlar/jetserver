

package jetserver.benchmark;

import java.net.*;
import java.io.*;

/**
 * This is a really small benchmark program
 *
 * It can only pump one URL for data.
 *
 * NOTE: this is not really a serious attempt on a good benchmark program
 *       it should not be considered to be correct. The only good thing to
 *       do is probably to rewrite this crap.... =)   /Jimmy
 */

public class BenchMark {

    private static final int DEFAULT_THREADS = 10;

    BenchThread threads[];
    private byte requestData[];

    private long startTime;
    private long totalTime;
    private int totalRequests;
    private long totalBytes;
    private int errors;

    private OutputThread outputThread;

    /**
     * Create and start benchmarking
     *
     */


    public BenchMark (URL destURL, int numThreads) 
	throws UnsupportedEncodingException
    {
	this(destURL, numThreads, null, null);
    }

    /**
     * Create and start benchmarking
     *
     */


    public BenchMark (URL destURL, int numThreads, String login, String password) 
	throws UnsupportedEncodingException
    {

	totalTime = 0;
	totalRequests = 0;
	totalBytes = 0;

	StringBuffer buffer = new StringBuffer(1024);
	buffer.append("GET " + destURL.getFile() + " HTTP/1.0\r\n");
	buffer.append("Host: "+ destURL.getHost() + "\r\n");

	if (login != null && password != null) {
	    buffer.append("Authorization: Basic " + base64Encode(login + ":" + password) + "\r\n");
	} 
	buffer.append("\r\n");

	requestData = buffer.toString().getBytes("ascii");
	threads = new BenchThread [numThreads];

	for (int i = 0; i < threads.length; i++) {	    
	    threads[i] = new BenchThread (destURL.getHost(), destURL.getPort() == -1 ? 80 : destURL.getPort(), requestData);
	}
	
	outputThread = new OutputThread();

	startTime = System.currentTimeMillis();	
	for (int i = 0; i < threads.length; i++) {	    
	    threads[i].start();
	}
    }

    /**
     * Each thread report to this method of their progress 
     *
     */

    private void report (long time, int bytes) {
	synchronized (this) {
	    ++totalRequests;
	    totalTime += time;
	    totalBytes += bytes;
	}
    }

    private void reportError () {
	synchronized (this) {
	    errors++;
	}
    }

    private void resetReports () {
	synchronized (this) {
	    totalRequests = 0;
	    totalTime = 0;
	    totalBytes = 0;
	    errors = 0;
	    startTime = System.currentTimeMillis();	
	}
    }


    /**
     * Base64 encoder
     * (We should remove this or write our own... =)
     */

    
    public String base64Encode(String string) {

	byte d[] = string.getBytes();
	byte data[] = new byte[d.length+2];
	System.arraycopy(d, 0, data, 0, d.length);
	byte dest[] = new byte[(data.length/3)*4];
	
	// 3-byte to 4-byte conversion
	for (int sidx = 0, didx=0; sidx < d.length; sidx += 3, didx += 4) {
	    dest[didx]   = (byte) ((data[sidx] >>> 2) & 077);
	    dest[didx+1] = (byte) ((data[sidx+1] >>> 4) & 017 |
				   (data[sidx] << 4) & 077);
	    dest[didx+2] = (byte) ((data[sidx+2] >>> 6) & 003 |
				   (data[sidx+1] << 2) & 077);
	    dest[didx+3] = (byte) (data[sidx+2] & 077);
	}
	
	// 0-63 to ascii printable conversion
	for (int idx = 0; idx <dest.length; idx++) {
	    if (dest[idx] < 26)     dest[idx] = (byte)(dest[idx] + 'A');
	    else if (dest[idx] < 52)  dest[idx] = (byte)(dest[idx] + 'a' - 26);
	    else if (dest[idx] < 62)  dest[idx] = (byte)(dest[idx] + '0' - 52);
	    else if (dest[idx] < 63)  dest[idx] = (byte)'+';
	    else            dest[idx] = (byte)'/';
	}
	
	// add padding
	for (int idx = dest.length-1; idx > (d.length*4)/3; idx--) {
	    dest[idx] = (byte)'=';
	}
	return new String(dest);
    }
    


    /**
     * This is the benchmarking thread
     *
     */

    private class BenchThread extends Thread {
	
	private byte buffer[] = new byte[100 * 1024];
	private byte requestData[];
	private String host;
	private int port;

	public BenchThread (String host, int port, byte requestData[]) {
	    this.host = host;	    
	    this.port = port;
	    this.requestData = requestData;
	}

	public void run () {	    
	    while (true) {
		
		try {
		    long st = System.currentTimeMillis();
		    
		    Socket socket = new Socket(host, port);
		    OutputStream out = socket.getOutputStream();
		    out.write(requestData);
		    out.flush();

		    InputStream in = socket.getInputStream();

		    int totalRead = 0;
		    int read = in.read (buffer);
		    
		    while (read != -1) {			
			totalRead += read;			
			read = in.read (buffer);				    
		    }

		    socket.close();

		    report (System.currentTimeMillis() - st, totalRead);
		
		} catch (IOException e) {
		    reportError();
		    System.err.println("Error: " + e);
		}
	    }
	}
    }

    /**
     * This thread takes the care of the console output, to keep the 
     * benchmarking threads free for doing HTTP requests 
     */

    private class OutputThread extends Thread {

	private boolean hasResetted = false;

	public OutputThread () {
	    this.start();
	}

	public void run () {

	    while (true) {

		int err;
		int totReq;
		long totTime;
		long totBytes;

		synchronized (this) {
		    err = errors;
		    totReq = totalRequests;
		    totTime = totalTime;
		    totBytes = totalBytes;
		}
		
		long runTime = System.currentTimeMillis() - startTime;
		
		/* Restart afer 2 secs */
		if (!hasResetted && totReq > 3000) {
		    resetReports();
		    hasResetted = true;
		    System.out.println("-- Statistics resetted --");
		    
		    synchronized (this) {
			err = errors;
			totReq = totalRequests;
			totTime = totalTime;
			totBytes = totalBytes;
		    }
		}
		
		if (totReq > 0) {
		    
		    String mess = totReq + " requests, " + errors + " errors, avg. req time: " 
			+ (totTime / totReq) 
			+ " ms, run time: " + runTime + " ms, " 
			+ (runTime < 1000 ? "N/A" : ("" + ((totBytes / 1024) / (runTime / 1000)))) 
			+ " Kb/s, "
			+ (totBytes / 1024) + " kB total, req/second: ";
		    
		    if (runTime == 0) {
			mess += "N/A";
		    } else {
			mess += totReq / (runTime / 1000.0);
		    }		    

		    System.out.println (mess);
		}

		try {
		    Thread.sleep (1000);
		} catch (InterruptedException e) {}		
	    }
	}
    } 

    /**
     * The main method
     *
     */

    public static void main (String args[]) 
	throws MalformedURLException, UnsupportedEncodingException 
    {
	if (args.length < 1 || args.length > 4) {
	    System.out.println ("Usage: java com.lektor.benchmark.BenchMark <url> [threads]");
	    System.out.println ("   or: java com.lektor.benckmark.BenchMark <url> <login> <password> [threads]");
	    System.out.println ("       The latter will user HTTP_BASIC authentication.");
	    System.exit(1);
	}
	
	URL url = new URL (args[0]);

	int numThreads = DEFAULT_THREADS;

	if (args.length == 2 || args.length == 4) {

	    String threadNumberStr;
	    if (args.length == 2)
		threadNumberStr = args[1];
	    else
		threadNumberStr = args[3];
		
	    try {
		numThreads = Integer.parseInt (threadNumberStr);
	    } catch (NumberFormatException e) {

		System.out.println ("Illegal thread number " + threadNumberStr);		
		System.exit(1);
	    }
	}

	System.out.println ("Running with " + numThreads + " thread(s).");

	BenchMark bench;

	if (args.length > 2) {
	    /* With 'http basic' */
	    bench = new BenchMark (url, numThreads, args[1], args[2]);
	} else {
	    bench = new BenchMark (url, numThreads);
	}

	while (true) {
	    try {
		Thread.sleep(20000);
	    } catch (InterruptedException e) {}
	}
    }
}

 
