package jetserver.util.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtilities {

    /**
     * Fetch the value of a named child or attribute.
     * For childs, the body of the child is returned.
     * (Child nodes have predecence over attributes)
     * @param node the node
     * @param propertyName name of child or attribute to find.
     * @return the value or null if not found
     */
    public static String findProperty(Node node, String propertyName) {
       /* Try children */
       NodeList children = node.getChildNodes();
       for (int i = 0; i < children.getLength(); i++) {
           if (children.item(i).getNodeName().equals(propertyName)) {
               /* Extract the body of the child */
               String bodyValue = "";
               NodeList bodies = children.item(i).getChildNodes();
               for (int j = 0; j < bodies.getLength(); j++) {
                   bodyValue += bodies.item(j).getNodeValue();
               }
               return bodyValue;
           }
       }

       /* Try attributes */
       Node attribute = node.getAttributes().getNamedItem(propertyName);
       if (attribute != null) {
           return attribute.getNodeValue();
       } else {
           return null;
       }
    }
}
