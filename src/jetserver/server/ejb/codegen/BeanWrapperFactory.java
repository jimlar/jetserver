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
        BufferedWriter sourceWriter = new BufferedWriter(new FileWriter(new File(ejbJar.getConfig().getEjbJarRoot(), "Temp.java")));
        implementRemoteInterface(entityBean, sourceWriter);
        sourceWriter.close();
    }


    private void implementRemoteInterface(EntityBeanDefinition entityBean,
                                     BufferedWriter sourceWriter)
            throws IOException
    {
        sourceWriter.write("public class Temp implements " + entityBean.getRemoteClass().getName() + " {");
        sourceWriter.newLine();
        sourceWriter.newLine();

        Method[] remoteMethods = entityBean.getRemoteClass().getDeclaredMethods();
        if (remoteMethods != null) {
            for (int i = 0; i < remoteMethods.length; i++) {
                implementRemoteInterfaceMethod(remoteMethods[i], sourceWriter);
            }
        }

        sourceWriter.newLine();
        sourceWriter.write("}");
    }

    private void implementRemoteInterfaceMethod(Method interfaceMethod,
                                           BufferedWriter sourceWriter)
            throws IOException
    {
        sourceWriter.write("public " + classToString(interfaceMethod.getReturnType()) + " ");
        sourceWriter.write(interfaceMethod.getName() + "(");

        Class[] parameterTypes = interfaceMethod.getParameterTypes();
        if (parameterTypes != null) {
            for (int i = 0; i < parameterTypes.length; i++) {
                sourceWriter.write(classToString(parameterTypes[i]) + " arg" + i);
                if (i < parameterTypes.length - 1) {
                    sourceWriter.write(", ");
                }
            }
        }

        sourceWriter.write(") ");

        Class[] exceptionTypes = interfaceMethod.getExceptionTypes();
        if (exceptionTypes != null && exceptionTypes.length > 0) {
            sourceWriter.write("throws ");
            for (int i = 0; i < exceptionTypes.length; i++) {
                sourceWriter.write(classToString(exceptionTypes[i]));
                if (i < exceptionTypes.length - 1) {
                    sourceWriter.write(", ");
                }
            }
            sourceWriter.write(" ");
        }

        sourceWriter.write("{");
        sourceWriter.newLine();


        sourceWriter.newLine();
        sourceWriter.write("}");
        sourceWriter.newLine();
        sourceWriter.newLine();
    }

    private String classToString(Class clazz) {
        return clazz.getName();
    }
}
