package jetserver.server.ejb;

import jetserver.server.ejb.config.EJBJarConfig;

import java.io.File;
import java.io.IOException;

/**
 * This class represents the contents of and ejb-jar file
 */
public class EJBJar {

    private EJBJarConfig config;
    private EJBClassLoader classLoader;

    /**
     * Create instance and parse configs (ejb-jar.xml)
     */
    EJBJar(File root) throws IOException {
        /* Decode ejb-jar.xml */
        this.config = new EJBJarConfig(root);
        this.config.parse();
    }

    public EJBJarConfig getConfig() {
        return config;
    }

    public EJBClassLoader getClassLoader() {
        return classLoader;
    }
}
