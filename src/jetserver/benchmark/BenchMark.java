

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

    private long totalTime = 0;
    private int totalRequests = 0;
    private long totalBytes = 0;

    private long startTime = System.currentTimeMillis();

    private OutputThread outputThread;

    /** HTTP BASIC string */
    private String authorizationHeader;

    /**
     * Create and start benchmarking
     *
     */


    public BenchMark (URL destURL, int numThreads) {
	this(destURL, numThreads, null, null);
    }

    /**
     * Create and start benchmarking
     *
     */


    public BenchMark (URL destURL, int numThreads, String login, String password) {

	totalTime = 0;
	totalRequests = 0;
	totalBytes = 0;

	if (login != null && password != null) {
	    authorizationHeader = "Basic " + base64Encode(login + ":" + password);;
	} else {
	    authorizationHeader = null;
	}
	
	threads = new BenchThread [numThreads];

	for (int i = 0; i < threads.length; i++) {
	    
	    threads[i] = new BenchThread (i, destURL);
	}
	
	startTime = System.currentTimeMillis();
	

	outputThread = new OutputThread();

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
	
	private int rowNum;
	private URL url;
	private byte buf[] = new byte [1024 * 80];
	

	public BenchThread (int rowNum, URL url) {
	    
	    this.rowNum = rowNum;
	    this.url = url;
	}

	public void run () {

	    
	    try {
		while (true) {
		    
		    long startTime = System.currentTimeMillis();
		    
		    URLConnection con = url.openConnection();
		    con.setUseCaches (false);

		    if (authorizationHeader != null)
			con.setRequestProperty("Authorization", authorizationHeader);

		    InputStream in = con.getInputStream();

		    int totalRead = 0;
		    int read = in.read (buf);
		    
		    while (read != -1) {
			
			totalRead += read;
			
			read = in.read (buf);				    
		    }

		    in.close();

		    report (System.currentTimeMillis() - startTime, totalRead);
		}
		
	    } catch (IOException e) {

		e.printStackTrace();
	    }
	}
    }

    /**
     * This thread takes the care of the console output, to keep the 
     * benchmarking threads free for doing HTTP requests 
     */

    private class OutputThread extends Thread {

	public OutputThread () {

	    this.start();
	}

	public void run () {

	    while (true) {

		try {
		    Thread.sleep (1000);
		} catch (InterruptedException e) {}


		int totReq;
		long totTime;
		long totBytes;
		
		synchronized (this) {
		    totReq = totalRequests;
		    totTime = totalTime;
		    totBytes = totalBytes;
		}
		
		if (totReq > 0) {
		    
		    long runTime = System.currentTimeMillis() - startTime;
		    
		    String mess = totReq + " requests, avg. req time: " + (totTime / totReq) 
			+ " ms, run time: " + runTime + " ms, " 
			+ (runTime < 1000 ? "N/A" : ("" + ((totBytes / 1024) / (runTime / 1000)))) 
			+ " Kb/s, "
			+ (totBytes / 1024) + " kB total, req/second: ";
		    
		    if (runTime == 0)
			mess += "N/A";
		    else
			mess += totReq / (runTime / 1000.0);
		    
		    System.out.println (mess);
		}
	    }
	}
    } 

    /**
     * The main method
     *
     */

    public static void main (String args[]) throws MalformedURLException {

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

	try {
	    Thread.sleep(20000);
	} catch (InterruptedException e) {}
    }
}

 
