package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * class LineWriter.
 */
public class LineWriter {
    /** internal file writer. */
    private FileOutputStream writer;

    /**
     *
     * @param fileToWrite file path to write
     * @throws IOException error
     */
    public LineWriter(
            final String fileToWrite)
            throws IOException {
        String file = fileToWrite;
        writer = new FileOutputStream(new File(file));
    }

    /**
     *
     * @param str String to write with newline.
     * @throws IOException error
     */
    public final void putLine(
            final String str)
            throws IOException {
        writer.write((str + "\n").getBytes());
    }

    /**
     *
     * @param str String to write.
     * @throws IOException error
     */
    public final void putString(
            final String str)
            throws IOException {
        writer.write(str.getBytes());
    }

    /**
     *  close file writer.
     * @throws IOException error
     */
    public final void close()
            throws IOException {
        writer.close();
    }

    /**
     * flush file writer.
     * @throws IOException error
     */
    public final void flush()
            throws IOException {
        writer.flush();
    }
}
