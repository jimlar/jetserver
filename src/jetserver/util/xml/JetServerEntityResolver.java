package jetserver.util.xml;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;

import jetserver.util.Log;

/**
 * This lets us have the dtd for web.xml locally.
 *
 * Add it to your favourite Parser or DocumentBuilder with
 * setEntityResolver()
 */
public class JetServerEntityResolver implements EntityResolver {

    private static Map resourcesByPublicId;
    private Log log;

    static {
        resourcesByPublicId = new HashMap();

        resourcesByPublicId.put("-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN", "web-app_2_2.dtd");
        resourcesByPublicId.put("-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN", "web-app_2_3.dtd");

        resourcesByPublicId.put("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", "ejb-jar_1_1.dtd");
        resourcesByPublicId.put("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN", "ejb-jar_2_0.dtd");
    }

    public JetServerEntityResolver() {
        log = Log.getInstance(this);
    }


    /**
     *  @return null if we are not handling the entity to enable the default mechanism
     */
    public InputSource resolveEntity(String publicId,
                                     String systemId)
            throws SAXException, IOException
    {
        if (resourcesByPublicId.containsKey(publicId)) {
            String resourceName = (String) resourcesByPublicId.get(publicId);
            InputStream resource = getClass().getResourceAsStream(resourceName);
            if (resource == null) {
                log.error("Loading handled resource failed (not found) " + resourceName);
                return null;
            }
            log.debug("DTD loaded locally " + resourceName);
            return new InputSource(resource);
        }
        return null;
    }
}
