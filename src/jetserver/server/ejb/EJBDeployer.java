package jetserver.server.ejb;

import jetserver.server.ejb.config.EJBJarConfig;
import jetserver.server.ejb.config.EntityBeanDefinition;
import jetserver.server.ejb.codegen.BeanWrapperFactory;
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

        EJBJar ejbJar = new EJBJar(ejbJarRoot);

        /* Verfiy the validness of the EJB jar */
        validateBeans(ejbJar);

        /* Generate wrappers and proxies for all beans */
        wrapBeans(ejbJar);

        /* Create bean caches */
        createCaches(ejbJar);

        /* Bind homes to the JNDI */
        bindBeans(ejbJar);
    }

    private void validateBeans(EJBJar ejbJar) throws IOException {
        log.debug("Validating beans");
    }

    private void wrapBeans(EJBJar ejbJar) throws IOException {
        log.debug("Wrapping beans");
        BeanWrapperFactory beanWrapperFactory = new BeanWrapperFactory(ejbJar);
        Iterator entities = ejbJar.getConfig().getEntityBeans().iterator();
        while (entities.hasNext()) {
            EntityBeanDefinition entityBean = (EntityBeanDefinition) entities.next();
            beanWrapperFactory.wrapEntity(entityBean);
        }
    }

    private void createCaches(EJBJar ejbJar) throws IOException {
        log.debug("Creating bean caches");
    }

    private void bindBeans(EJBJar ejbJar) throws IOException {
        log.debug("Binding beans to JNDI");
    }
}