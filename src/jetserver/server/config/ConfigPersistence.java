package jetserver.server.config;

import jetserver.util.xml.XMLUtilities;
import org.w3c.dom.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.io.*;

class ConfigPersistence {

    public static void load(ServerConfig config, InputStream in) throws IOException {
        try {
            Document document = XMLUtilities.loadDocument(in);

            Element configNode = document.getDocumentElement();
            extractBlock(configNode, config);

        } catch (NoSuchMethodException e) {
            throw new IOException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IOException(e.getMessage());
        } catch (InvocationTargetException e) {
            throw new IOException(e.getMessage());
        } catch (InstantiationException e) {
            throw new IOException(e.getMessage());
        }
    }

    private static void extractBlock(Element blockElement, ConfigBlock block) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        if (block != null) {
            extractBooleans(blockElement, block);
            extractInts(blockElement, block);
            extractStrings(blockElement, block);
            extractFiles(blockElement, block);

            for (Iterator i = getSettersWithArgumentType(block.getClass(), ConfigBlock.class).iterator(); i.hasNext();) {
                Method setter = (Method) i.next();

                String xmlName = getXmlNameFromSetter(setter);
                Element subBlockElement = XMLUtilities.getSingleSubElement(blockElement, xmlName);

                ConfigBlock subBlock = (ConfigBlock) setter.getParameterTypes()[0].newInstance();
                setter.invoke(block, new Object[] { subBlock });

                extractBlock(subBlockElement, subBlock);
            }
        }
    }

    private static void extractBooleans(Element blockElement, ConfigBlock block) throws IllegalAccessException, InvocationTargetException, IOException {
        for (Iterator i = getSettersWithArgumentType(block.getClass(), Boolean.TYPE).iterator(); i.hasNext();) {
            Method setter = (Method) i.next();

            String xmlName = getXmlNameFromSetter(setter);
            if (!blockElement.hasAttribute(xmlName)) {
                throw new IOException("Element " + blockElement.getNodeName() + " is missing attribute " + xmlName);
            }
            Boolean value = Boolean.valueOf(blockElement.getAttribute(xmlName));
            setter.invoke(block, new Object[] { value });
        }
    }

    private static void extractInts(Element blockElement, ConfigBlock block) throws IllegalAccessException, InvocationTargetException, IOException {
        for (Iterator i = getSettersWithArgumentType(block.getClass(), Integer.TYPE).iterator(); i.hasNext();) {
            Method setter = (Method) i.next();

            String xmlName = getXmlNameFromSetter(setter);
            if (!blockElement.hasAttribute(xmlName)) {
                throw new IOException("Element " + blockElement.getNodeName() + " is missing attribute " + xmlName);
            }
            Integer value = new Integer(blockElement.getAttribute(xmlName));
            setter.invoke(block, new Object[] { value });
        }
    }

    private static void extractStrings(Element blockElement, ConfigBlock block) throws IllegalAccessException, InvocationTargetException {
        for (Iterator i = getSettersWithArgumentType(block.getClass(), String.class).iterator(); i.hasNext();) {
            Method setter = (Method) i.next();

            String xmlName = getXmlNameFromSetter(setter);
            Element valueElement = XMLUtilities.getSingleSubElement(blockElement, xmlName);
            String value = XMLUtilities.getNodeBodyValue(valueElement);

            setter.invoke(block, new Object[] { value });
        }
    }

    private static void extractFiles(Element blockElement, ConfigBlock block) throws IllegalAccessException, InvocationTargetException {
        for (Iterator i = getSettersWithArgumentType(block.getClass(), File.class).iterator(); i.hasNext();) {
            Method setter = (Method) i.next();

            String xmlName = getXmlNameFromSetter(setter);
            Element valueElement = XMLUtilities.getSingleSubElement(blockElement, xmlName);
            String value = XMLUtilities.getNodeBodyValue(valueElement);

            setter.invoke(block, new Object[] { new File(ServerConfig.CONFIG_DIR, value) });
        }
    }

    private static String getXmlNameFromSetter(Method setter) {
        if (!setter.getName().startsWith("set")) {
            throw new RuntimeException("Method " + setter.getName() + " is not a setter");
        }
        StringBuffer result = new StringBuffer(setter.getName());
        result.delete(0, 3);

        result.setCharAt(0, Character.toLowerCase(result.charAt(0)));
        for (int i = 0; i < result.length(); i++) {
            if (Character.isUpperCase(result.charAt(i))) {
                result.setCharAt(i, Character.toLowerCase(result.charAt(i)));
                result.insert(i, "-");
                i++;
            }
        }
        return result.toString();
    }

    private static Collection getSettersWithArgumentType(Class targetClass, Class argumentType) {
        Collection result = new ArrayList();
        Method[] methods = targetClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.getName().startsWith("set")) {
                Class[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1) {
                    Class parameterType = parameterTypes[0];
                    if (argumentType.isAssignableFrom(parameterType)) {
                        result.add(method);
                    }
                }
            }
        }
        return result;
    }
}
