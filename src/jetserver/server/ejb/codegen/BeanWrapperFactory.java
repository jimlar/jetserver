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
import java.util.Arrays;

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
        createRemoteProxy(entityBean);
        log.debug(" - remote proxy created");
        createRemoteHome(entityBean);
        log.debug(" - remote home created");
        createBeanSubClass(entityBean);
        log.debug(" - bean class subclassed");
    }


    private void createRemoteProxy(EntityBeanDefinition entityBean)
            throws IOException
    {
        String className = entityBean.getEJBName() + "_JetServerRemoteProxy";
        File sourceFile = new File(ejbJar.getConfig().getTempDir(), className + ".java");
        sourceFile.getParentFile().mkdirs();
        SourceWriter sourceWriter = new SourceWriter(new FileWriter(sourceFile));

        sourceWriter.startClass(className,
                                null,
                                new Class[] {entityBean.getRemoteClass()});

        Method[] remoteMethods = entityBean.getRemoteClass().getDeclaredMethods();
        if (remoteMethods != null) {
            for (int i = 0; i < remoteMethods.length; i++) {
                sourceWriter.startMethod(remoteMethods[i].getReturnType(),
                                         remoteMethods[i].getName(),
                                         remoteMethods[i].getParameterTypes(),
                                         remoteMethods[i].getExceptionTypes());

                sourceWriter.write("System.out.println(\"" + remoteMethods[i].getName() + " called\");");
                sourceWriter.endMethod();
            }
        }
        sourceWriter.endClass();
        sourceWriter.close();
    }

    private void createRemoteHome(EntityBeanDefinition entityBean)
            throws IOException
    {
        String className = entityBean.getEJBName() + "_JetServerRemoteHome";
        File sourceFile = new File(ejbJar.getConfig().getTempDir(), className + ".java");
        sourceFile.getParentFile().mkdirs();
        SourceWriter sourceWriter = new SourceWriter(new FileWriter(sourceFile));

        sourceWriter.startClass(className,
                                null,
                                new Class[] {entityBean.getRemoteHomeClass()});

        Method[] homeMethods = entityBean.getRemoteHomeClass().getDeclaredMethods();
        if (homeMethods != null) {
            for (int i = 0; i < homeMethods.length; i++) {
                sourceWriter.startMethod(homeMethods[i].getReturnType(),
                                         homeMethods[i].getName(),
                                         homeMethods[i].getParameterTypes(),
                                         homeMethods[i].getExceptionTypes());

                sourceWriter.write("System.out.println(\"" + homeMethods[i].getName() + " called\");");
                sourceWriter.endMethod();
            }
        }
        sourceWriter.endClass();
        sourceWriter.close();
    }

    private void createBeanSubClass(EntityBeanDefinition entityBean)
            throws IOException
    {
        String className = entityBean.getEJBName() + "_JetServerEJBWrapper";
        File sourceFile = new File(ejbJar.getConfig().getTempDir(), className + ".java");
        sourceFile.getParentFile().mkdirs();
        SourceWriter sourceWriter = new SourceWriter(new FileWriter(sourceFile));

        sourceWriter.startClass(className,
                                entityBean.getEjbClass(),
                                null);

        /*
         * We only need to subclass the methods that appear
         * in the remote interface
         */
        Method[] ejbMethods = entityBean.getEjbClass().getDeclaredMethods();
        if (ejbMethods != null) {
            for (int i = 0; i < ejbMethods.length; i++) {
                if (isBusinessMethod(ejbMethods[i], entityBean.getRemoteClass())) {
                    sourceWriter.startMethod(ejbMethods[i].getReturnType(),
                                             ejbMethods[i].getName(),
                                             ejbMethods[i].getParameterTypes(),
                                             ejbMethods[i].getExceptionTypes());

                    sourceWriter.write("System.out.println(\"" + ejbMethods[i].getName() + " called\");");
                    sourceWriter.endMethod();
                }
            }
        }
        sourceWriter.endClass();
        sourceWriter.close();
    }

    /**
     * @return true if the method matches the signature of a businenss method
     * in the ejbInterface class
     */
    private boolean isBusinessMethod(Method method, Class ejbInterface) {
        Method[] businessMethods = ejbInterface.getDeclaredMethods();
        if (businessMethods != null) {
            for (int i = 0; i < businessMethods.length; i++) {
                if (method.getReturnType().equals(businessMethods[i].getReturnType())
                        && method.getName().equals(businessMethods[i].getName())
                        && Arrays.equals(method.getParameterTypes(), businessMethods[i].getParameterTypes())) {
                    return true;
                }
            }
        }
        return false;
    }
}
