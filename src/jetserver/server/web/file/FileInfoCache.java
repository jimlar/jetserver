

package jetserver.server.web.file;

import java.io.*;
import java.util.*;

import jetserver.server.web.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * Caches info about a file to speed up request handling
 * caches files data if filesize smaller the the configured size
 *
 */

class FileInfoCache {

    private static final int MAX_FILE_SIZE = 50 * 1024;
    private static final int ENTRY_TIME_TO_LIVE = 5000; 


    private final WebApplication webApp;
    private final MimeTypes mimeTypes;
    private final Map fileInfoByRequestURI;


    public FileInfoCache(WebApplication webApp) {
        this.webApp = webApp;
        this.mimeTypes = new MimeTypes();
        this.fileInfoByRequestURI = Collections.synchronizedMap(new WeakHashMap());
    }

    /**
     * 
     * Fetch info about a file requested
     *
     */
    public FileInfo getFileInfo(HttpServletRequest request)
            throws IOException
    {
        FileInfo fileInfo = (FileInfo) fileInfoByRequestURI.get(request.getRequestURI());
        if (fileInfo == null || fileInfo.isOutDated()) {
            fileInfo = updateFileInfo(fileInfo, request);
            fileInfoByRequestURI.put(request.getRequestURI(),  fileInfo);
        }
        return fileInfo;
    }


    /**
     * Updated a fileinfo object if needed
     * (a new info object is created if oldInfo is null)
     */

    private FileInfo updateFileInfo(FileInfo oldInfo, HttpServletRequest request)
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
    private FileInfo createFileInfo(HttpServletRequest request)
            throws IOException
    {
        String localRequestURI = request.getRequestURI().substring(webApp.getConfig().getHttpRoot().length());
        File requestedFile = new File(webApp.getConfig().getFileRoot(), localRequestURI);
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

        Iterator iter = webApp.getConfig().getWelcomeFiles().iterator();
        while (iter.hasNext()) {
            File candidate = new File(requestedFile, (String) iter.next());
            if (candidate.exists() && candidate.isFile()) {
                return candidate;
            }
        }

        return null;
    }
}
