

package jetserver.web.services.file;

import java.io.*;
import java.util.*;

import jetserver.config.ServerConfig;

class MimeTypes {

    private static final String MIMETYPES_PROPERTY = "jetserver.webserver.mime-types-file";
    private static final String DEFAULTMIMETYPE_PROPERTY = "jetserver.webserver.default-mime-type";
    
    private Map typesByExtension;
    private String defaultMimeType;

    public MimeTypes() throws IOException {
	ServerConfig config = ServerConfig.getInstance();
	this.defaultMimeType = config.getProperty(DEFAULTMIMETYPE_PROPERTY);
	BufferedReader reader = new BufferedReader(new FileReader(config.getFileProperty(MIMETYPES_PROPERTY)));
	
	this.typesByExtension = new HashMap();

	String line = null;
	while ((line = reader.readLine()) != null) {
	    line = line.trim();
	    if (!line.startsWith("#") && !line.equals("")) {
		StringTokenizer st = new StringTokenizer(line, "\t ");
		if (st.countTokens() >= 2) {
		    String type = st.nextToken();
		    List extensions = new ArrayList();
		    while (st.hasMoreTokens()) {
			extensions.add(st.nextToken());
		    }

		    Iterator iter = extensions.iterator();
		    while (iter.hasNext()) {
			String ext = ((String) iter.next()).trim();
			typesByExtension.put(ext, type);
		    }
		}
	    }
	}
    }

    /* This extracts the extension of a file */
    public String getTypeByFileName(String filename) {
	int i = filename.lastIndexOf(".");
	if (i == -1) {
	    return defaultMimeType;
	}

	return getTypeByExtension(filename.substring(i + 1));
    }

    public String getTypeByExtension(String extension) {
	String type = (String) typesByExtension.get(extension);
	if (type == null) {
	    type = defaultMimeType;
	}
	return type;
    }
}
