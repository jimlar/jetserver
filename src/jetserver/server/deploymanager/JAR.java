package jetserver.server.deploymanager;

import java.io.*;
import java.util.jar.*;

public class JAR {

    private boolean isValid;
    private JarFile jarFile;

    public JAR(File file) {
	try {
	    this.jarFile = new JarFile(file);
	    isValid = true;
	} catch (IOException e) {
	    isValid = false;
	}
    }

    /** @return true if this is a valid jar file */
    public boolean isValid() {
	return isValid;
    }

    public boolean contains(String filename) {
	return jarFile.getJarEntry(filename) != null;
    }
}
