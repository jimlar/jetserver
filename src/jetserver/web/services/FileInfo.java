
package jetserver.web.services;

import java.io.*;

class FileInfo {

    private File requestedFile;
    private String mimeType;
    private int size;
    private boolean fileExists;
    private boolean isDirectoryIndexRequest;
    private byte fileData[];
    private long outDatedOn;
    private long lastChanged;
    
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
}
