package jetserver.util.xml;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.util.List;

/**
 * A nodelist implementation based on a colleaction list
 */
public class ListNodeList implements NodeList {

    private List nodes;

    public ListNodeList(List nodes) {
        this.nodes = nodes;
    }

    public Node item(int index) {
        try {
            return (Node) nodes.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public int getLength() {
        return nodes.size();
    }
}
