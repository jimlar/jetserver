package jetserver.util.xml;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.parsers.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class XMLUtilities {

    /**
     * @return the child element, throws exception if the parent does not
     *          have exactly one sub element with name
     */
    public static Element getSingleSubElement(Element parentElement, String name) {
        NodeList nodes = parentElement.getElementsByTagName(name);
        if (nodes.getLength() != 1) {
            throw new IllegalArgumentException("Expected the element "
                                               + parentElement.getNodeName()
                                               + " to have one child called "
                                               + name
                                               + " but found " + nodes.getLength());
        }
        return (Element) nodes.item(0);
    }

    /**
     * @return true if the node has a child named "childName"
     */
    public static boolean hasChildElement(Node node, String childName) {
        return findFirstChildElement(node, childName) != null;
    }

    /**
     * Find the child elements of a node with the given name
     *
     * @return a NodeList with the found elements, never returns null.
     */
    public static NodeList findChildElements(Node node, String childName) {
        List result = new ArrayList();

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE
                    && child.getNodeName().equals(childName)) {
                result.add(child);
            }
        }
        return new ListNodeList(result);
    }

    /**
     * Find the first found named child of a node.
     * Note that a Document is also a node, so this is very useful for
     * finding the root node of a document
     *
     * @return null if not found
     */
    public static Node findFirstChildElement(Node node, String childName) {
        NodeList children = findChildElements(node, childName);
        if (children.getLength() == 0) {
            return null;
        } else {
            return children.item(0);
        }
    }

    /**
     * Fetch the value of a named child or attribute.
     * For childs, the body of the child is returned.
     * (Child nodes have predecence over attributes)
     * @param node the node
     * @param propertyName name of child or attribute to find.
     * @return the value or null if not found
     */
    public static String findValue(Node node, String propertyName) {
        /* Try children */
        Node child = findFirstChildElement(node, propertyName);
        if (child != null) {
            /* Extract the body of the child */
            return getNodeBodyValue(child);
        }

        /* Try attributes */
        if (node.getAttributes() != null) {
            Node attribute = node.getAttributes().getNamedItem(propertyName);
            if (attribute != null) {
                return attribute.getNodeValue();
            }
        }
        return null;
    }

    /**
     * Get the body value of a node
     */
    public static String getNodeBodyValue(Node child) {
        String bodyValue = "";
        NodeList bodies = child.getChildNodes();
        for (int j = 0; j < bodies.getLength(); j++) {
            bodyValue += bodies.item(j).getNodeValue();
        }
        return bodyValue;
    }

    /**
     * Fetch the value of a named child or attribute.
     * For childs, the body of the child is returned.
     * (Child nodes have predecence over attributes)
     * @param node the node
     * @param propertyName name of child or attribute to find.
     * @return the value or null if not found or value not integer
     */
    public static Integer findIntegerValue(Node node, String propertyName) {
        String value = findValue(node, propertyName);
        if (value != null) {
            try {
                return new Integer(value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Create a new DOM document
     */
    public static Document newDocument() throws IOException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Load a DOM document
     */
    public static Document loadDocument(InputStream in) throws IOException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.parse(in);
        } catch (ParserConfigurationException e) {
            throw new IOException(e.getMessage());
        } catch (SAXException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Save a DOM document
     */
    public static void saveDocument(Document document, OutputStream out, boolean indent) throws IOException {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            if (indent) {
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            }
            transformer.transform(new DOMSource(document), new StreamResult(out));
        } catch (TransformerException e) {
            throw new IOException(e.getMessage());
        }
    }
}

