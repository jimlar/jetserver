package jetserver.server.ejb.config;

import java.util.Collection;
import java.util.ArrayList;

/**
 * This represents the configuration of an entitybean (from ejb-jar.xml)
 *
 */
public class EntityBeanDefinition extends BeanDefinition {

    private Class remoteHomeClass;
    private Class remoteClass;

    private Class localHomeClass;
    private Class localClass;

    private String persistenceType;
    private String primKeyClass;
    private boolean isReentrant;

    private String cmpVersion;
    private String abstractSchemaName;

    private Collection cmpFields = new ArrayList();
    private String primKeyField;

    private Collection queries = new ArrayList();

    EntityBeanDefinition() {}

    public Class getRemoteHomeClass() {
        return remoteHomeClass;
    }

    public void setRemoteHomeClass(Class remoteHomeClass) {
        this.remoteHomeClass = remoteHomeClass;
    }

    public Class getRemoteClass() {
        return remoteClass;
    }

    public void setRemoteClass(Class remoteClass) {
        this.remoteClass = remoteClass;
    }

    public Class getLocalHomeClass() {
        return localHomeClass;
    }

    public void setLocalHomeClass(Class localHomeClass) {
        this.localHomeClass = localHomeClass;
    }

    public Class getLocalClass() {
        return localClass;
    }

    public void setLocalClass(Class localClass) {
        this.localClass = localClass;
    }

    public String getPersistenceType() {
        return persistenceType;
    }

    public void setPersistenceType(String persistenceType) {
        this.persistenceType = persistenceType;
    }

    public String getPrimKeyClass() {
        return primKeyClass;
    }

    public void setPrimKeyClass(String primKeyClass) {
        this.primKeyClass = primKeyClass;
    }

    public boolean isReentrant() {
        return isReentrant;
    }

    public void setReentrant(boolean reentrant) {
        isReentrant = reentrant;
    }

    public String getCmpVersion() {
        return cmpVersion;
    }

    public void setCmpVersion(String cmpVersion) {
        this.cmpVersion = cmpVersion;
    }

    public String getAbstractSchemaName() {
        return abstractSchemaName;
    }

    public void setAbstractSchemaName(String abstractSchemaName) {
        this.abstractSchemaName = abstractSchemaName;
    }

    public Collection getCmpFields() {
        return cmpFields;
    }

    public void setCmpFields(Collection cmpFields) {
        this.cmpFields = cmpFields;
    }

    public String getPrimKeyField() {
        return primKeyField;
    }

    public void setPrimKeyField(String primKeyField) {
        this.primKeyField = primKeyField;
    }

    public Collection getQueries() {
        return queries;
    }

    public void setQueries(Collection queries) {
        this.queries = queries;
    }
}

