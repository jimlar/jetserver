package jetserver.server.ejb;

import java.io.File;
import java.io.IOException;

public class EJBContainer {

    public EJBContainer() {
    }

    public void deploy(File ejbJarRoot) throws IOException {
        System.out.println("Deploying ejb jar");
    }
}
