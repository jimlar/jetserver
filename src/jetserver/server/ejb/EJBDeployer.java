package jetserver.server.ejb;

import jetserver.server.ejb.config.EJBJarXMLParser;
import jetserver.server.ejb.config.EntityBeanDefinition;
import jetserver.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.lang.reflect.Method;

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

        EJBClassLoader classLoader = new EJBClassLoader(ejbJarRoot);

        Iterator entities = parser.getEntityBeans().iterator();
        while (entities.hasNext()) {
            EntityBeanDefinition entityBean = (EntityBeanDefinition) entities.next();
            verifyEntityBean(classLoader, entityBean);
        }
    }

    private void verifyEntityBean(EJBClassLoader classLoader, EntityBeanDefinition entityBean)
            throws IOException
    {
        try {
            Class ejbClass = classLoader.loadClass(entityBean.getEjbClass());
            Class remoteClass = classLoader.loadClass(entityBean.getRemoteClass());
            Class homeClass = classLoader.loadClass(entityBean.getRemoteHomeClass());

            Method[] homeMethods = homeClass.getDeclaredMethods();
            if (homeMethods != null) {
                for (int i = 0; i < homeMethods.length; i++) {
                    log.debug("Found home method " + entityBean.getEJBName()
                            + "." + homeMethods[i].getName());
                }
            }

            Method[] remoteMethods = remoteClass.getDeclaredMethods();
            if (remoteMethods != null) {
                for (int i = 0; i < remoteMethods.length; i++) {
                    log.debug("Found remote method " + entityBean.getEJBName()
                            + "." + remoteMethods[i].getName());
                }
            }
            Method[] beanMethods = ejbClass.getDeclaredMethods();
            if (beanMethods != null) {
                for (int i = 0; i < beanMethods.length; i++) {
                    log.debug("Found bean method " + entityBean.getEJBName()
                            + "." + beanMethods[i].getName());
                }
            }

        } catch (ClassNotFoundException e) {
            throw new IOException("EJB class not found " + entityBean.getEjbClass());
        }
    }
}