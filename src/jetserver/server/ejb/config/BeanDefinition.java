package jetserver.server.ejb.config;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This represents the superset of configuration
 * for all types of EJBs
 *
 */
public class BeanDefinition {

    private String description;
    private String displayName;
    private String smallIcon;
    private String largeIcon;

    private String ejbName;

    private Class ejbClass;
    private Class ejbWrapperClass;

    private Collection environmentEntries = new ArrayList();
    private Collection ejbReferences = new ArrayList();
    private Collection localEJBReferences = new ArrayList();
    private Collection roleReferences = new ArrayList();
    private String securityIdentity;
    private Collection resourceReferences = new ArrayList();
    private Collection resourceEnvReferences = new ArrayList();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
    }

    public String getEJBName() {
        return ejbName;
    }

    public void setEJBName(String ejbName) {
        this.ejbName = ejbName;
    }

    public Class getEjbClass() {
        return ejbClass;
    }

    public void setEjbClass(Class ejbClass) {
        this.ejbClass = ejbClass;
    }

    public Class getEJBWrapperClass() {
        return ejbWrapperClass;
    }

    public void setEJBWrapperClass(Class ejbWrapperClass) {
        this.ejbWrapperClass = ejbWrapperClass;
    }

    public Collection getEnvironmentEntries() {
        return environmentEntries;
    }

    public void setEnvironmentEntries(Collection environmentEntries) {
        this.environmentEntries = environmentEntries;
    }

    public Collection getEjbReferences() {
        return ejbReferences;
    }

    public void setEjbReferences(Collection ejbReferences) {
        this.ejbReferences = ejbReferences;
    }

    public Collection getLocalEJBReferences() {
        return localEJBReferences;
    }

    public void setLocalEJBReferences(Collection localEJBReferences) {
        this.localEJBReferences = localEJBReferences;
    }

    public Collection getRoleReferences() {
        return roleReferences;
    }

    public void setRoleReferences(Collection roleReferences) {
        this.roleReferences = roleReferences;
    }

    public String getSecurityIdentity() {
        return securityIdentity;
    }

    public void setSecurityIdentity(String securityIdentity) {
        this.securityIdentity = securityIdentity;
    }

    public Collection getResourceReferences() {
        return resourceReferences;
    }

    public void setResourceReferences(Collection resourceReferences) {
        this.resourceReferences = resourceReferences;
    }

    public Collection getResourceEnvReferences() {
        return resourceEnvReferences;
    }

    public void setResourceEnvReferences(Collection resourceEnvReferences) {
        this.resourceEnvReferences = resourceEnvReferences;
    }
}
