package jetserver.server.ejb;

import jetserver.server.ejb.config.EJBJarConfig;
import jetserver.server.ejb.config.EntityBeanDefinition;
import jetserver.server.ejb.codegen.BeanWrapperFactory;
import jetserver.server.application.Application;
import jetserver.util.Log;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.lang.reflect.Method;

public class EJBDeployer {
    private Log log;

    public EJBDeployer() {
        log = Log.getInstance(this);
    }

    public void deploy(File ejbJarRoot,
                       Application application) throws IOException {
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

        /* Add the ejb jar to the application */
        application.addEJBJar(ejbJar);

        log.info("Deployment finished (" + ejbJarRoot + ")");
    }

    private void validateBeans(EJBJar ejbJar) throws IOException {
        log.debug("Validating beans (NOT IMPLEMENTED)");
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
        log.debug("Creating bean caches (NOT IMPLEMENTED)");
    }

    private void bindBeans(EJBJar ejbJar) throws IOException {
        log.debug("Binding beans homes to JNDI");

        try {
            InitialContext context = new InitialContext();
            Iterator ejbs = ejbJar.getConfig().getEntityBeans().iterator();
            while (ejbs.hasNext()) {
                EntityBeanDefinition ejb = (EntityBeanDefinition) ejbs.next();

                /* Instantiate the home and bind it */
                Object ejbHome = ejb.getRemoteHomeWrapper().newInstance();
                context.bind(ejb.getEJBName(), ejbHome);
            }
        } catch (NamingException e) {
            throw new IOException("Cant bind bean: " + e);
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new IOException("Cant instantiate bean home: " + e);
        } catch (IllegalAccessException e) {
            throw new IOException("Cant instantiate bean home: " + e);
        }
    }
}