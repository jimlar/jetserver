package jetserver.server.ejb.codegen;

import jetserver.server.ejb.config.EntityBeanDefinition;
import jetserver.server.ejb.config.CMPField;
import jetserver.server.ejb.EJBJar;
import jetserver.util.Log;

import javax.ejb.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.rmi.RemoteException;

public class BeanWrapperFactory {

    private Log log;
    private EJBJar ejbJar;

    private File wrappersDir;
    private Compiler compiler;

    public BeanWrapperFactory(EJBJar ejbJar) {
        this.ejbJar = ejbJar;
        this.log = Log.getInstance(this);
        this.wrappersDir =  ejbJar.getConfig().getWrappersDir();
        this.compiler = new Compiler(this.wrappersDir,
                                     "..:../../../jetserver.jar");
    }

    /**
     * Generate and compile wrapper for entity
     */
    public void wrapEntity(EntityBeanDefinition entityBean)
            throws IOException
    {
        log.debug("Generating wrappers for " + entityBean.getEJBName());
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
        File sourceFile = new File(this.wrappersDir,
                                   className + ".java");
        sourceFile.getParentFile().mkdirs();
        SourceWriter sourceWriter = new SourceWriter(new FileWriter(sourceFile));

        sourceWriter.startClass(className,
                                null,
                                new Class[] {entityBean.getRemoteClass()});

        /*** Implement the business mtehods ***/
        Method[] remoteMethods = entityBean.getRemoteClass().getDeclaredMethods();
        if (remoteMethods != null) {
            for (int i = 0; i < remoteMethods.length; i++) {
                sourceWriter.startMethod(remoteMethods[i].getReturnType(),
                                         remoteMethods[i].getName(),
                                         remoteMethods[i].getParameterTypes(),
                                         remoteMethods[i].getExceptionTypes());

                sourceWriter.write("System.out.println(\"" + remoteMethods[i].getName() + " called\");");
                Class returnType = remoteMethods[i].getReturnType();
                if (!returnType.equals(Void.TYPE)) {
                    sourceWriter.newLine();
                    if (returnType.isInstance(new Object())) {
                        sourceWriter.write("return null;");
                    } else if (returnType.equals(Boolean.TYPE)) {
                        sourceWriter.write("return false;");
                    } else if (returnType.equals(Integer.TYPE)
                            || returnType.equals(Long.TYPE)) {
                        sourceWriter.write("return -1;");
                    }
                }
                sourceWriter.endMethod();
            }
        }

        /*** Implement EJBObject ***/

        /* public EJBHome getEJBHome() throws RemoteException;*/
        sourceWriter.startMethod(EJBHome.class,
                                 "getEJBHome",
                                 null,
                                 new Class[] { RemoteException.class });
        sourceWriter.write("System.out.println(\"getEJBHome called\");");
        sourceWriter.newLine();
        sourceWriter.write("return null;");
        sourceWriter.endMethod();

        /*public Object getPrimaryKey() throws RemoteException;*/
        sourceWriter.startMethod(Object.class,
                                 "getPrimaryKey",
                                 null,
                                 new Class[] { RemoteException.class });
        sourceWriter.write("System.out.println(\"getPrimaryKey called\");");
        sourceWriter.newLine();
        sourceWriter.write("return null;");
        sourceWriter.endMethod();

        /*public void remove() throws RemoteException, RemoveException;*/
        sourceWriter.startMethod(Void.TYPE,
                                 "remove",
                                 null,
                                 new Class[] { RemoteException.class, RemoveException.class });
        sourceWriter.write("System.out.println(\"remove called\");");
        sourceWriter.endMethod();

        /*public Handle getHandle() throws RemoteException;*/
        sourceWriter.startMethod(Handle.class,
                                 "getHandle",
                                 null,
                                 new Class[] { RemoteException.class });
        sourceWriter.write("System.out.println(\"getHandle called\");");
        sourceWriter.newLine();
        sourceWriter.write("return null;");
        sourceWriter.endMethod();

        /*public boolean isIdentical(EJBObject ejbobject_0_) throws RemoteException;*/
        sourceWriter.startMethod(Boolean.TYPE,
                                 "isIdentical",
                                 new Class[] { EJBObject.class },
                                 new Class[] { RemoteException.class });
        sourceWriter.write("System.out.println(\"isIdentical called\");");
        sourceWriter.newLine();
        sourceWriter.write("return false;");
        sourceWriter.endMethod();

        sourceWriter.endClass();
        sourceWriter.close();

        compiler.compile(sourceFile);
        try {
            Class proxyClass = ejbJar.getConfig().getClassLoader().loadClass(className);
            entityBean.setRemoteProxy(proxyClass);
        } catch (ClassNotFoundException e) {
            throw new IOException("Class could not be loaded" + e);
        }
    }

    private void createRemoteHome(EntityBeanDefinition entityBean)
            throws IOException
    {
        String className = entityBean.getEJBName() + "_JetServerRemoteHome";
        File sourceFile = new File(this.wrappersDir,
                                   className + ".java");
        sourceFile.getParentFile().mkdirs();
        SourceWriter sourceWriter = new SourceWriter(new FileWriter(sourceFile));

        sourceWriter.startClass(className,
                                null,
                                new Class[] {entityBean.getRemoteHomeClass()});

        /*** Implement userdefined methods ***/
        Method[] homeMethods = entityBean.getRemoteHomeClass().getDeclaredMethods();
        if (homeMethods != null) {
            for (int i = 0; i < homeMethods.length; i++) {
                sourceWriter.startMethod(homeMethods[i].getReturnType(),
                                         homeMethods[i].getName(),
                                         homeMethods[i].getParameterTypes(),
                                         homeMethods[i].getExceptionTypes());

                sourceWriter.write("System.out.println(\"" + homeMethods[i].getName() + " called\");");
                if (!homeMethods[i].getReturnType().equals(Void.TYPE)) {
                    sourceWriter.newLine();
                    sourceWriter.write("return null;");
                }
                sourceWriter.endMethod();
            }
        }

        /*** implement EJBHome ***/

        /*public void remove(Handle handle) throws RemoteException, RemoveException;*/
        sourceWriter.startMethod(Void.TYPE,
                                 "remove",
                                 new Class[] { Handle.class },
                                 new Class[] { RemoteException.class, RemoveException.class });
        sourceWriter.write("System.out.println(\"remove(Handle) called\");");
        sourceWriter.endMethod();

        /*public void remove(Object object) throws RemoteException, RemoveException;*/
        sourceWriter.startMethod(Void.TYPE,
                                 "remove",
                                 new Class[] { Object.class },
                                 new Class[] { RemoteException.class, RemoveException.class });
        sourceWriter.write("System.out.println(\"remove(Object) called\");");
        sourceWriter.endMethod();

        /*public EJBMetaData getEJBMetaData() throws RemoteException;*/
        sourceWriter.startMethod(EJBMetaData.class,
                                 "getEJBMetaData",
                                 null,
                                 new Class[] { RemoteException.class });
        sourceWriter.write("System.out.println(\"getEJBMetaData called\");");
        sourceWriter.newLine();
        sourceWriter.write("return null;");
        sourceWriter.endMethod();

        /*public HomeHandle getHomeHandle() throws RemoteException;*/
        sourceWriter.startMethod(HomeHandle.class,
                                 "getHomeHandle",
                                 null,
                                 new Class[] { RemoteException.class });
        sourceWriter.write("System.out.println(\"getHomeHandle called\");");
        sourceWriter.newLine();
        sourceWriter.write("return null;");
        sourceWriter.endMethod();

        sourceWriter.endClass();
        sourceWriter.close();

        compiler.compile(sourceFile);
        try {
            Class homeClass = ejbJar.getConfig().getClassLoader().loadClass(className);
            entityBean.setRemoteHomeWrapper(homeClass);
        } catch (ClassNotFoundException e) {
            throw new IOException("Class could not be loaded" + e);
        }
    }

    private void createBeanSubClass(EntityBeanDefinition entityBean)
            throws IOException
    {
        String className = entityBean.getEJBName() + "_JetServerEJBWrapper";
        File sourceFile = new File(this.wrappersDir,
                                   className + ".java");
        sourceFile.getParentFile().mkdirs();
        SourceWriter sourceWriter = new SourceWriter(new FileWriter(sourceFile));

        sourceWriter.startClass(className,
                                entityBean.getEjbClass(),
                                null);

        implementCMPFields(entityBean, sourceWriter);

        sourceWriter.endClass();
        sourceWriter.close();

        compiler.compile(sourceFile);
        try {
            Class wrapperClass = ejbJar.getConfig().getClassLoader().loadClass(className);
            entityBean.setEJBWrapperClass(wrapperClass);
        } catch (ClassNotFoundException e) {
            throw new IOException("Class could not be loaded" + e);
        }
    }

    private void implementCMPFields(EntityBeanDefinition entityBean, SourceWriter sourceWriter) throws IOException {
        Iterator cmpFields = entityBean.getCmpFields().iterator();
        while (cmpFields.hasNext()) {
            CMPField field = (CMPField) cmpFields.next();

            Method getMethod = getMethod(fieldToGetterName(field),
                                         entityBean.getEjbClass());
            if (getMethod == null) {
                throw new IOException("field " + field + " has no getter method");
            }

            Class returnType = getMethod.getReturnType();

            /* Implement the getter method */
            sourceWriter.startMethod(returnType, getMethod.getName(), null, null);
            sourceWriter.write("System.out.println(\""
                               + getMethod.getName()
                               + " called\");");

            if (!returnType.equals(Void.TYPE)) {
                if (returnType.isInstance(new Object())) {
                    sourceWriter.write("return null;");
                } else if (returnType.equals(Boolean.TYPE)) {
                    sourceWriter.write("return false;");
                } else if (returnType.equals(Integer.TYPE)
                        || returnType.equals(Long.TYPE)) {
                    sourceWriter.write("return -1;");
                }
            }
            sourceWriter.endMethod();


            /* Implement the setter method */
            Method setMethod = getMethod(fieldToSetterName(field),
                                         entityBean.getEjbClass());
            if (setMethod == null) {
                throw new IOException("field " + field + " has no setter method");
            }
            sourceWriter.startMethod(setMethod.getReturnType(),
                                     setMethod.getName(),
                                     setMethod.getParameterTypes(),
                                     null);
            sourceWriter.write("System.out.println(\""
                               + setMethod.getName()
                               + " called\");");
            sourceWriter.endMethod();
        }
    }

    private String fieldToGetterName(CMPField field) {
        return "get"
                + field.getName().substring(0, 1).toUpperCase()
                + field.getName().substring(1);
    }

    private String fieldToSetterName(CMPField field) {
        return "set"
                + field.getName().substring(0, 1).toUpperCase()
                + field.getName().substring(1);
    }

    /**
     * Return the first method in the class with the given name
     * or null if no method found
     */
    private Method getMethod(String methodName, Class clazz) {
        Method[] methods = clazz.getMethods();
        if (methods != null) {
            for (int i = 0; i < methods.length; i++) {
               if (methods[i].getName().equals(methodName)) {
                   return methods[i];
               }
            }
        }
        return null;
    }
}
