
package jetserver.config;

import java.io.*;
import java.util.*;

import org.xml.sax.*;

class ServerConfigHandler extends HandlerBase {

    private Map properties = new HashMap();
    private Stack elementStack = new Stack();
    private StringBuffer characterBuffer = new StringBuffer();

    
    public Map getProperties() {
	return properties;
    }
    
    /* Set value of the property define by the stack state */
    private void setElementValue(String value) {
	
	String propertyName = "";
	Iterator iter = elementStack.iterator();
	while (iter.hasNext()) {
	    propertyName += (String) iter.next();
	    if (iter.hasNext()) {
		propertyName += ".";
	    }
	}
	properties.put(propertyName, value);
    }

    /* -- SAX interface implementation -- */

    public void startElement(String        name,
			     AttributeList attributes)
	throws SAXException
    {
	elementStack.push(name);

	for (int i = 0; i < attributes.getLength(); i++) {
	    String attributeName = attributes.getName(i);
	    String attributeValue = attributes.getValue(i);

	    elementStack.push(attributeName);
	    setElementValue(attributeValue);
	    elementStack.pop();
	}
	
	/* Clear charater buffer to receive new data */
	characterBuffer = new StringBuffer();	
    }

    public void endElement(String name)
	throws SAXException
    {
	if (characterBuffer.length() > 0) {
	    setElementValue(characterBuffer.toString());
	}

	/* Clear charater buffer to receive new data */
	characterBuffer = new StringBuffer();	
    }

    public void characters(char[] ch,
			   int start,
			   int length)
	throws SAXException
    {
	characterBuffer.append(ch, start, length);
    }
}
