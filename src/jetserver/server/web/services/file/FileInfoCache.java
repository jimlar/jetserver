

package jetserver.server.web.services.file;

import java.io.*;
import java.util.*;

import jetserver.server.web.*;
import jetserver.config.ServerConfig;

/**
 * 
 * Caches info about a file to speed up request handling
 * caches files data if filesize smaller the the configured size
 *
 */

class FileInfoCache {

    private static final int MAX_FILE_SIZE = 50 * 1024;
    private static final int ENTRY_TIME_TO_LIVE = 5000; 

    private final File baseDir;
    private final MimeTypes mimeTypes;
    private final Collection welcomeFiles;
    private final Map fileInfoByRequestURI;


    public FileInfoCache(File baseDir, Collection welcomeFiles) throws IOException {
	this.baseDir = baseDir;
	this.welcomeFiles = welcomeFiles;
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

	    if (size <= MAX_FILE_SIZE) {
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
			    ENTRY_TIME_TO_LIVE,
			    requestedFile.lastModified(),
			    data);
    }

    /*
     * Find valid welcome file
     *
     * @return file or null if no file exists
     */

    private File getExistingWelcomeFile(File requestedFile) {
	
	Iterator iter = welcomeFiles.iterator();
	while (iter.hasNext()) {
	    File candidate = new File(requestedFile, (String) iter.next());
	    if (candidate.exists() && candidate.isFile()) {
		return candidate;
	    }
	}

	return null;
    }
}