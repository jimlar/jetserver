package jetserver.util;

import java.io.*;
import java.util.*;
import java.util.jar.*;


public class EnterpriseJar {

    private boolean isValid;
    private JarFile jarFile;

    public EnterpriseJar(File file) {
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

    public boolean isWebApplication() {
	return isValid() && contains("WEB-INF/web.xml");
    }
    
    public boolean isEJBJar() {
	return isValid() && contains("META-INF/ejb-jar.xml");
    }
    
    public boolean isJ2EEApplication() {
	return isValid() && contains("META-INF/application.xml");
    }

    public void unpackTo(File dir) throws IOException {
	Enumeration entries = jarFile.entries();
	while (entries.hasMoreElements()) {
	    JarEntry entry = (JarEntry) entries.nextElement();
	    File entryFile = new File(dir, entry.getName());
	    if (entry.isDirectory()) {
		entryFile.mkdirs();
	    } else {
		extractFile(entry, entryFile);
	    }
	}
    }

    private void extractFile(JarEntry entry, File outFile) 
	throws IOException
    {
	byte[] buffer = new byte[1024];
	InputStream in = jarFile.getInputStream(entry);
	OutputStream out = new FileOutputStream(outFile);
	
	int read = 0;
	while (read != -1) {
	    read = in.read(buffer);
	    if (read != -1) {
		out.write(buffer, 0, read);
	    }
	}
	in.close();
	out.close();
    }
}
