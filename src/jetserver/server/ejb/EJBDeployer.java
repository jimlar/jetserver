package jetserver.server.ejb;

import jetserver.server.ejb.config.EJBJarConfig;
import jetserver.server.ejb.config.EntityBeanDefinition;
import jetserver.server.ejb.codegen.EntityWrapperFactory;
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

        /* For each bean:
         *  1. generate wrapper classes
         *  2. create applicable caches
         *  3. Bind to JNDI
         */

        EntityWrapperFactory entityWrapperFactory = new EntityWrapperFactory(ejbJar);
        Iterator entities = ejbJar.getConfig().getEntityBeans().iterator();
        while (entities.hasNext()) {
            EntityBeanDefinition entityBean = (EntityBeanDefinition) entities.next();
            Class wrapper = entityWrapperFactory.createWrapper(entityBean);
            entityBean.setWrapperClass(wrapper);
        }
    }
}