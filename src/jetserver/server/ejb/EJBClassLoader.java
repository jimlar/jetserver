/*
 * Created by IntelliJ IDEA.
 * User: jimmy
 * Date: Dec 12, 2001
 * Time: 11:27:14 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package jetserver.server.ejb;

import jetserver.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * The classloader used foe EJBs
 */
public class EJBClassLoader extends ClassLoader {

    private EJBJar ejbJar;
    private Log log;

    public EJBClassLoader(EJBJar ejbJar) {
        super();
        this.ejbJar = ejbJar;
        this.log = Log.getInstance(this);
    }

    /**
     * Find a servlet class
     */
    protected Class findClass(String name) throws ClassNotFoundException {
        byte[] classData = loadClassData(name);
        log.debug("Loaded class " + name);
        return defineClass(name, classData, 0, classData.length);
    }

    /**
     * Find and load classData
     */
    private byte[] loadClassData(String name) throws ClassNotFoundException {
        File classFile = new File(ejbJar.getDeployDir().getAbsolutePath()
                                  + File.separator + name.replace('.', File.separatorChar)
                                  + ".class");

        if (!classFile.exists()) {
            /* Now try the wrappers dir */
            classFile = new File(ejbJar.getWrappersDir().getAbsolutePath()
                                 + File.separator + name.replace('.', File.separatorChar)
                                 + ".class");

            if (!classFile.exists()) {
                throw new ClassNotFoundException("cant find class "+ name);
            }
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
