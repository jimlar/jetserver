/*
 * Created by IntelliJ IDEA.
 * User: jimmy
 * Date: Dec 12, 2001
 * Time: 10:42:46 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package jetserver.server.ejb.config;


public class SessionBeanDefinition extends BeanDefinition {

    private Class remoteHomeClass;
    private Class remoteClass;

    private Class localHomeClass;
    private Class localClass;

    private String sessionType;
    private String transactionType;

    public SessionBeanDefinition() {
    }

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

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
