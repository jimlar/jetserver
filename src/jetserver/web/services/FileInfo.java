
package jetserver.web.services;

import java.io.*;

class FileInfo {

    private File requestedFile;
    private String mimeType;
    private int size;
    private boolean fileExists;
    private boolean isDirectoryIndexRequest;
    private byte fileData[];
    
    public FileInfo(File    requestedFile,
		    String  mimeType,
		    int     size,
		    boolean fileExists, 
		    boolean isDirectoryIndexRequest,
		    byte    fileData[]) {
	
	this.requestedFile = requestedFile;
	this.mimeType = mimeType;
	this.size = size;
	this.fileExists = fileExists;
	this.isDirectoryIndexRequest = isDirectoryIndexRequest;
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
}
