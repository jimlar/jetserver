package jetserver.server.ejb.codegen;

import jetserver.server.ejb.config.EntityBeanDefinition;
import jetserver.server.ejb.EJBJar;
import jetserver.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

public class BeanWrapperFactory {

    private Log log;
    private EJBJar ejbJar;

    public BeanWrapperFactory(EJBJar ejbJar) {
        this.ejbJar = ejbJar;
        this.log = Log.getInstance(this);
    }

    /**
     * Generate and compile wrapper for entity
     */
    public void wrapEntity(EntityBeanDefinition entityBean)
            throws IOException
    {
        log.debug("Generation wrappers for " + entityBean.getEJBName());
        SourceWriter sourceWriter = new SourceWriter(new FileWriter(new File(ejbJar.getConfig().getEjbJarRoot(), "Temp.java")));
        implementRemoteInterface(entityBean, sourceWriter);
        sourceWriter.close();
    }


    private void implementRemoteInterface(EntityBeanDefinition entityBean,
                                          SourceWriter sourceWriter)
            throws IOException
    {

        sourceWriter.startClass(entityBean.getEJBName() + "_wrapper ",
                                null,
                                new Class[] {entityBean.getRemoteClass()});

        Method[] remoteMethods = entityBean.getRemoteClass().getDeclaredMethods();
        if (remoteMethods != null) {
            for (int i = 0; i < remoteMethods.length; i++) {
                implementRemoteInterfaceMethod(remoteMethods[i], sourceWriter);
            }
        }
        sourceWriter.endClass();
    }

    private void implementRemoteInterfaceMethod(Method interfaceMethod,
                                                SourceWriter sourceWriter)
            throws IOException
    {
        sourceWriter.startMethod(interfaceMethod.getReturnType(),
                                 interfaceMethod.getName(),
                                 interfaceMethod.getParameterTypes(),
                                 interfaceMethod.getExceptionTypes());

        sourceWriter.write("System.out.println(\"" + interfaceMethod.getName() + " called\");");
        sourceWriter.endMethod();
    }
}
