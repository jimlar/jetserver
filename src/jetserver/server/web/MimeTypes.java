
package jetserver.server.web;

import jetserver.server.config.ServerConfig;
import jetserver.server.config.WebConfig;

import java.io.*;
import java.util.*;

class MimeTypes {
    private Map typesByExtension;
    private String defaultMimeType;

    public MimeTypes() {

        try {
            ServerConfig config = ServerConfig.getInstance();
            WebConfig webConfig = config.getWeb();
            this.defaultMimeType = webConfig.getDefaultMimeType();

            BufferedReader reader = new BufferedReader(new FileReader(webConfig.getMimeTypesFile()));

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
        } catch (IOException e) {
            throw new RuntimeException("Cant initialize mimetypes: " + e);
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
