package jetserver.server.ejb.codegen;

import jetserver.server.ejb.config.EntityBeanDefinition;
import jetserver.server.ejb.EJBJar;
import jetserver.util.Log;

import java.lang.reflect.Method;
import java.io.IOException;

public class EntityWrapperFactory {

    private Log log;
    private EJBJar ejbJar;

    public EntityWrapperFactory(EJBJar ejbJar) {
        this.ejbJar = ejbJar;
        this.log = Log.getInstance(this);
    }


    /**
     * Generate and compile wrapper for entity
     */
    public Class createWrapper(EntityBeanDefinition entityBean)
            throws IOException
    {
        log.debug("Generation wrapper for " + entityBean.getEJBName());

        String methods = "";
        Method[] beanMethods = entityBean.getEjbClass().getDeclaredMethods();
        if (beanMethods != null) {
            for (int i = 0; i < beanMethods.length; i++) {
                methods += beanMethods[i].getName();
                if (i < beanMethods.length - 1) {
                    methods += ", ";
                }
            }
        }
        log.debug(" - found methods " + methods);
        return null;
    }
}
