
package jetserver.server.web.services.servlet;

import java.io.*;
import java.util.Iterator;

import jetserver.server.web.config.*;
import jetserver.server.web.WebApplication;
import jetserver.server.ejb.EJBJar;
import jetserver.util.Log;

/**
 * This is classloader for servlets
 */

class ServletClassLoader extends ClassLoader {

    private WebApplication webApp;
    private Log log;

    ServletClassLoader(WebApplication webApp) {
        super();
        this.webApp = webApp;
        this.log = Log.getInstance(this);
    }

    /**
     * Find a servlet class, has the EJB loaders of the application as parent loaders
     */
    protected Class findClass(String name) throws ClassNotFoundException {

        try {
            byte[] classData = loadClassData(name);
            log.debug("loaded class " + name);
            return defineClass(name, classData, 0, classData.length);
        } catch (ClassNotFoundException ignored) {
            /* Ok, now try the ejb loaders */
            Iterator ejbJars = webApp.getApplication().getEJBJars().iterator();
            while (ejbJars.hasNext()) {
                EJBJar ejbJar = (EJBJar) ejbJars.next();
                ClassLoader loader = ejbJar.getClassLoader();
                try {
                    return loader.loadClass(name);
                } catch (ClassNotFoundException e) {
                    if (!ejbJars.hasNext()) {
                        /* Class not found there either */
                         throw e;
                    }
                }
            }
            throw new ClassNotFoundException("Cant find class " + name);
        }
    }

    /**
     * Find and load classData
     */
    private byte[] loadClassData(String name) throws ClassNotFoundException {
        File classFile = new File(webApp.getConfig().getFileRoot().getAbsolutePath()
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
