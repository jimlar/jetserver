
package jetserver.server.web.servlet;


public class ServletDeclaration {
    private String name;
    private String className;

    public ServletDeclaration(String name, String className) {
        this.name = name;
        this.className = className;
    }

    public String getName() {
        return this.name;
    }

    public String getClassName() {
        return this.className;
    }
}

