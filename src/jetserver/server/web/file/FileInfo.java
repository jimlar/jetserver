
package jetserver.server.web.file;

import java.io.*;

import jetserver.util.*;
import jetserver.server.web.*;
import jetserver.server.web.HttpResponse;

class FileInfo {

    private File requestedFile;
    private String mimeType;
    private int size;
    private boolean fileExists;
    private boolean isDirectoryIndexRequest;
    private byte fileData[];
    private long outDatedOn;
    private long lastChanged;
    private byte headerBytes[];
    
    public FileInfo(File    requestedFile,
		    String  mimeType,
		    int     size,
		    boolean fileExists, 
		    boolean isDirectoryIndexRequest,
		    int     timeToLive,
		    long    lastChanged,
		    byte    fileData[]) {
	
	this.requestedFile = requestedFile;
	this.mimeType = mimeType;
	this.size = size;
	this.fileExists = fileExists;
	this.isDirectoryIndexRequest = isDirectoryIndexRequest;
	this.outDatedOn = System.currentTimeMillis() + timeToLive;
	this.lastChanged = lastChanged;
	this.fileData = fileData;

	/* Setup header bytes */
	StringBuffer buffer = new StringBuffer(256);
	buffer.append("Content-type: ");
	buffer.append(mimeType);
	buffer.append(HttpResponse.HEADER_NEWLINE);
	buffer.append("Content-length: ");
	buffer.append(size);
	buffer.append(HttpResponse.HEADER_NEWLINE);
	this.headerBytes = Strings.getAsciiBytes(buffer.toString());
    }
    
    public boolean fileExists() {
	return this.fileExists;
    }

    public boolean isDirectoryIndexRequest() {
	return this.isDirectoryIndexRequest;
    }

    public InputStream getInputStream() 
	throws IOException
    {
	if (fileData != null) {
	    return new ByteArrayInputStream(fileData);
	} else {
	    return new FileInputStream(requestedFile);
	}
    }

    public String getMimeType() {
	return this.mimeType;
    }

    public int getSize() {
	return this.size;
    }

    public boolean isOutDated() {
	return System.currentTimeMillis() > outDatedOn;
    }
    
    public boolean hasChangedOnDisk() {
	return requestedFile.lastModified() != lastChanged;
    }

    public byte[] getHeaderBytes() {
	return headerBytes;
    }
}
