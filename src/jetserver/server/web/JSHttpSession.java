package jetserver.server.web;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.*;

class JSHttpSession implements HttpSession {

    private String id;
    private boolean isValid;
    private boolean isNew;

    private long creationTime;
    private long lastAccessedTime;
    private int maxInactiveSeconds;

    private Map attributes;


    public JSHttpSession(String id) {
        this.id = id;
        this.creationTime = System.currentTimeMillis();
        this.maxInactiveSeconds = 3600;
        this.isValid = true;
        this.isNew = true;
        this.attributes = new HashMap();
    }

    public void touch() {
        this.isNew = false;
        this.lastAccessedTime = System.currentTimeMillis();
    }


    /*== HttpSession implementation ==*/
    public long getCreationTime() {
        return this.creationTime;
    }

    public String getId() {
        return this.id;
    }

    public long getLastAccessedTime() {
        return this.lastAccessedTime;
    }

    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveSeconds = interval;
    }

    public int getMaxInactiveInterval() {
        return this.maxInactiveSeconds;
    }

    public HttpSessionContext getSessionContext() {
        return null;
    }

    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    public Object getValue(String name) {
        return getAttribute(name);
    }

    public Enumeration getAttributeNames() {
        return Collections.enumeration(this.attributes.keySet());
    }

    public String[] getValueNames() {
        List names = new ArrayList();
        for (Enumeration e = getAttributeNames(); e.hasMoreElements();) {
            String name = (String) e.nextElement();
            names.add(name);
        }
        return (String[]) names.toArray(new String[names.size()]);
    }

    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }

    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    public void removeValue(String name) {
        removeAttribute(name);
    }

    public void invalidate() {
        this.isValid = false;
    }

    public boolean isNew() {
        return this.isNew;
    }
}
