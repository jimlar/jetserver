package jetserver.util.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class XMLUtilities {

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
            String bodyValue = "";
            NodeList bodies = child.getChildNodes();
            for (int j = 0; j < bodies.getLength(); j++) {
                bodyValue += bodies.item(j).getNodeValue();
            }
            return bodyValue;
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

}

