package jetserver.server.ejb.codegen;

import java.io.*;
import java.util.*;

/**
 * This is an interface the compiler
 */
public class Compiler {
    private String classPath;
    private File sourceDir;

    public Compiler(File sourceDir, String classPath) {
        this.sourceDir = sourceDir;
        this.classPath = classPath;
    }


    public void compile(File sourceFile) throws IOException {
        Collection files = new ArrayList();
        files.add(sourceFile);
        compile(files);
    }

    /**
     * @param sourceFiles a collection of File instances
     *        (has to reside in the sourceDir).
     */
    public void compile(Collection sourceFiles) throws IOException {

        String[] environment = new String[]{};

        List commands = new ArrayList();
        commands.add("javac");
        commands.add("-classpath");
        commands.add(classPath);

        Iterator iter = sourceFiles.iterator();
        while (iter.hasNext()) {
            commands.add(getRelativeSourceFilename((File) iter.next()));
        }

        Process process = Runtime.getRuntime().exec((String[]) commands.toArray(new String[0]),
                                                    environment,
                                                    sourceDir);

        new StreamPump(process.getErrorStream(), System.err);
        new StreamPump(process.getInputStream(), System.out);
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("compile was interrupted " + e);
        }
    }

    private String getRelativeSourceFilename(File sourceFile) {
        String result = sourceFile.getAbsolutePath();
        result = result.substring(sourceDir.getAbsolutePath().length());
        if (result.startsWith(File.separator)) {
            result = result.substring(1);
        }
        return result;
    }

    /**
     * Copy a stream to another
     */
    private class StreamPump extends Thread {
        private InputStream in;
        private OutputStream out;

        public StreamPump(InputStream in,
                          OutputStream out) {
            this.in = in;
            this.out = out;
            this.start();
        }

        public void run() {
            try {
                int read = 0;
                while ((read = in.read()) != -1) {
                    out.write(read);
                }
            } catch (IOException e) {

            }
        }
    }
}
