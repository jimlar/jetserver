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
                if (isInterfaceMethod(beanMethods[i], entityBean)) {
                    methods += beanMethods[i].getName();
                    if (i < beanMethods.length - 1) {
                        methods += ", ";
                    }
                }
            }
        }
        log.debug(" - wrapping methods " + methods);
        return null;
    }

    private boolean isEJBMethod(Method method, EntityBeanDefinition bean) {
        return method.getName().startsWith("ejb");
    }

    private boolean isInterfaceMethod(Method method, EntityBeanDefinition bean) {
        Method[] intfMethods = bean.getRemoteClass().getDeclaredMethods();
        if (intfMethods != null) {
            for (int i = 0; i < intfMethods.length; i++) {
                if (intfMethods[i].getName().equals(method.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
