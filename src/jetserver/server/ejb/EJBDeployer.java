package jetserver.server.ejb;

import jetserver.server.ejb.parsing.EJBJarXMLParser;
import jetserver.util.Log;

import java.io.File;
import java.io.IOException;

public class EJBDeployer {
    private Log log;

    public EJBDeployer() {
        log = Log.getInstance(this);
    }

    public void deploy(File ejbJarRoot) throws IOException {
        log.info("Deploying ejb jar (" + ejbJarRoot + ")");

        /* Decode ejb-jar.xml */
        EJBJarXMLParser parser = new EJBJarXMLParser(new File(ejbJarRoot,
                "META-INF" + File.separator + "ejb-jar.xml"));

        parser.parse();

        /* For each bean:
         *  1. generate wrapper classes
         *  2. compile them
         *  3. create applicable caches
         *  4. Bind to JNDI
         */
    }
}
