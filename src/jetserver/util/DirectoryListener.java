package jetserver.util;

import java.io.File;

public interface DirectoryListener {

    /**
     * The watch found a file, this also happens for all files when the watch starts up
     */
    void fileFound(File file);

    /**
     * The watch found that a file was updated
     */
    void fileChanged(File file);

    /**
     * The watch noticed that a file was removed
     */
    void fileLost(File file);
}
