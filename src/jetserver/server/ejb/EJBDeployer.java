package jetserver.server.ejb;

import java.io.File;
import java.io.IOException;

public class EJBDeployer {

    public EJBDeployer() {}

    public void deploy(File ejbJarRoot) throws IOException {
        System.out.println("Deploying ejb jar");

        /* Decode ejb-jar.xml */

        /* For each bean:
         *  1. generate wrapper classes
         *  2. compile them
         *  3. create applicable caches
         *  4. Bind to JNDI
         */
    }
}
