
package jetserver.server.web.services.servlet;

import java.io.*;

import jetserver.server.web.config.*;
import jetserver.util.Log;

/**
 * This is classloader for servlets
 */

class ServletClassLoader extends ClassLoader {

    private WebAppConfig config;
    private Log log;

    ServletClassLoader(WebAppConfig config) {
	super();
	this.config = config;
	this.log = Log.getInstance(this);
    }

    /**
     * Find a servlet class
     */
    protected Class findClass(String name) throws ClassNotFoundException {
	byte[] classData = loadClassData(name);
	log.debug("loaded class " + name);
	return defineClass(name, classData, 0, classData.length);
    }
    
    /**
     * Find and load classData
     */
    private byte[] loadClassData(String name) throws ClassNotFoundException {
	File classFile = new File(config.getFileRoot().getAbsolutePath() 
				  + File.separator + "WEB-INF" 
				  + File.separator + "classes" 
				  + File.separator + name.replace('.', File.separatorChar)
				  + ".class");

	log.debug("trying to load class from " + classFile.getAbsolutePath());
	
	if (!classFile.exists()) {
	    throw new ClassNotFoundException("cant find class "+ name);
	}

	try {
	    InputStream in = new FileInputStream(classFile);
	    byte[] classData = new byte[(int) classFile.length()];
	    
	    if (in.read(classData) != classFile.length()) {
		throw new IOException("error loading classdata (read too few bytes)");
	    }
	    
	    return classData;
	} catch (IOException e) {
	    throw new ClassNotFoundException("caught io error while reading class " 
					     + name + ": " + e);
	}
    }
    

}
