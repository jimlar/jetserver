package jetserver.server.ejb.codegen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

class SourceWriter extends BufferedWriter {

    public static final String INDENT_STRING = "\t";

    /** This changes when entering methods and stuff like that */
    private int indentLevel = 0;

    public SourceWriter(Writer out) {
        super(out);
    }

    public SourceWriter(Writer out, int sz) {
        super(out, sz);
    }

    public void startClass(String name,
                           Class extendsClass,
                           Class[] implementsInterfaces)
            throws IOException
    {
        write("public class " + name + " ");
        if (extendsClass != null) {
            write("extends " + extendsClass.getName() + " ");
        }

        if (implementsInterfaces != null) {
            write("implements ");
            for (int i = 0; i < implementsInterfaces.length; i++) {
                write(implementsInterfaces[i].getName());
                if (i < implementsInterfaces.length - 1) {
                    write(", ");
                }
            }
            write(" ");
        }

        startBlock();
    }

    public void endClass() throws IOException {
        endBlock();
    }

    public void startMethod(Class returnType,
                            String name,
                            Class[] parameterTypes,
                            Class[] exceptionTypes                            ) throws IOException {

        write("public " + returnType.getName() + " ");
        write(name + "(");

        /* Output method parameter declarations */
        if (parameterTypes != null) {
            for (int i = 0; i < parameterTypes.length; i++) {
                write(parameterTypes[i].getName() + " arg" + i);
                if (i < parameterTypes.length - 1) {
                    write(", ");
                }
            }
        }

        write(") ");

        /* Output throws clause */
        if (exceptionTypes != null && exceptionTypes.length > 0) {
            write("throws ");
            for (int i = 0; i < exceptionTypes.length; i++) {
                write(exceptionTypes[i].getName());
                if (i < exceptionTypes.length - 1) {
                    write(", ");
                }
            }
            write(" ");
        }

        startBlock();
    }

    public void endMethod() throws IOException {
        endBlock();
    }

    /**
     * Add a "{" , increase indentation and add a new line
     */
    public void startBlock() throws IOException {
        write("{");
        indentLevel++;
        newLine();
    }

    /**
     * Decrease indentation and add a newline, a '}' followed by another newline
     */
    public void endBlock() throws IOException {
        indentLevel--;
        newLine();
        write("}");
        newLine();
    }

    public void newLine() throws IOException {
        super.newLine();
        writeIndentString();
    }

    private void writeIndentString() throws IOException {
        for (int i = 0; i < indentLevel; i++) {
            write(INDENT_STRING);
        }
    }
}
