

package jetserver.web.services.file;

import java.io.*;
import java.util.*;

import jetserver.web.*;
import jetserver.config.ServerConfig;

/**
 * 
 * Caches info about a file to speed up request handling
 * caches files data if filesize smaller the the configured size
 *
 */

class FileInfoCache {

    private static final String ROOT_PROPERTY = "jetserver.webserver.root";
    private static final String WELCOMEFILES_PROPERTY = "jetserver.webserver.welcome-file-list.welcome-file";
    private static final String CACHEFILESIZE_PROPERTY = "jetserver.webserver.cache.max-file-size";
    private static final String CACHETTL_PROPERTY = "jetserver.webserver.cache.time-to-live";

    private final File baseDir;
    private final MimeTypes mimeTypes;
    private final String welcomeFiles[];
    private final Map fileInfoByRequestURI;
    private final int maxFileSize;

    /* Milliseconds */
    private final int entryTimeToLive;

    public FileInfoCache() throws IOException {
	ServerConfig config = ServerConfig.getInstance();
	this.baseDir = config.getFileProperty(ROOT_PROPERTY);
	if (config.getNumValues(WELCOMEFILES_PROPERTY) > 0) {
	    this.welcomeFiles = config.getPropertyValues(WELCOMEFILES_PROPERTY);
	} else {
	    this.welcomeFiles = null;
	}
	/* property given i kilobytes */
	this.maxFileSize = config.getIntProperty(CACHEFILESIZE_PROPERTY) * 1024;

	this.entryTimeToLive = config.getIntProperty(CACHETTL_PROPERTY);

	this.mimeTypes = new MimeTypes();
	this.fileInfoByRequestURI = Collections.synchronizedMap(new WeakHashMap());
    }

    /**
     * 
     * Fetch info about a file requested
     *
     */
    public FileInfo getFileInfo(HttpRequest request) 
	throws IOException
    {
	FileInfo fileInfo = (FileInfo) fileInfoByRequestURI.get(request.getURI());
	if (fileInfo == null || fileInfo.isOutDated()) {
	    fileInfo = updateFileInfo(fileInfo, request);
	    fileInfoByRequestURI.put(request.getURI(),  fileInfo);
	}	
	return fileInfo;
    }


    /**
     * Updated a fileinfo object if needed
     * (a new info object is created if oldInfo is null)
     */

    private FileInfo updateFileInfo(FileInfo oldInfo, HttpRequest request)
	throws IOException
    {
	if (oldInfo == null || oldInfo.hasChangedOnDisk()) {	   
	    return createFileInfo(request);
	} else {
	    return oldInfo;
	}
    }

    /**
     * 
     * Create new info from disk
     *
     */
    private FileInfo createFileInfo(HttpRequest request) 
	throws IOException
    {
	File requestedFile = new File(baseDir, request.getURI());
	boolean isDirectoryIndexRequest = false;
	boolean fileExists = requestedFile.exists();
	
	if (fileExists) {	    
	    /* Try welcomefiles if a directory is requested */
	    if (requestedFile.isDirectory()) {		
		File welcomeFile = getExistingWelcomeFile(requestedFile);
		if (welcomeFile == null) {
		    /* No welcomefile found, is a directory index request */
		    isDirectoryIndexRequest = true;			
		} else {		
		    requestedFile = welcomeFile;
		}
	    }
	} 

	int size = 0;
	byte data[] = null;

	if (fileExists && !isDirectoryIndexRequest) {
	    size = (int) requestedFile.length();

	    if (size <= maxFileSize) {
		data = new byte[size];
		InputStream in = new BufferedInputStream(new FileInputStream(requestedFile));
		if (in.read(data) != size) {
		    throw new IOException("Cant read filedata");
		}
		in.close();
	    }	    
	}

	return new FileInfo(requestedFile, 
			    mimeTypes.getTypeByFileName(requestedFile.getName()),
			    size,
			    fileExists, 
			    isDirectoryIndexRequest,
			    entryTimeToLive,
			    requestedFile.lastModified(),
			    data);
    }

    /*
     * Find valid welcome file
     *
     * @return file or null if no file exists
     */

    private File getExistingWelcomeFile(File requestedFile) {
	
	if (welcomeFiles != null) {
	    for (int i = 0; i < welcomeFiles.length; i++) {
		File candidate = new File(requestedFile, welcomeFiles[i]);
		if (candidate.exists() && candidate.isFile()) {
		    return candidate;
		}
	    }
	}

	return null;
    }
}
