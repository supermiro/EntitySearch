package util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

/**
 * class LineReader wrapper around java LineReader functionality.
 */
public class LineReader {
    /** Buffered reader. */
    private BufferedReader reader;
    /** BUFFER size of BufferedReader, need to be 128MB when using FUSE
     * block size on Hadoop cluster*/
    public static final int BUFFSIZE = 1024;

    /**
     * Set up the reader to read a plain, gzip or bzip2 compressed file.
     * @param fileToRead        file path to read, can be a .gz or .bz2 name
     * @throws IOException      on any issue reading data
     */
    public LineReader(final String fileToRead) throws IOException {
        BufferedInputStream fstream = new BufferedInputStream(
                new FileInputStream(fileToRead), BUFFSIZE);
        InputStreamReader isr = null;
        if (fileToRead.endsWith(".gz")) {
            try {
                isr = new InputStreamReader(new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP,
                        fstream));
            } catch (CompressorException e) {
                throw new IOException(e);
            }
        } else if (fileToRead.endsWith(".bz2")) {
            try {
                isr = new InputStreamReader(new CompressorStreamFactory()
                    .createCompressorInputStream(CompressorStreamFactory.BZIP2,
                            fstream));
            } catch (CompressorException e) {
                throw new IOException(e);
            }
        } else {
            isr = new InputStreamReader(new DataInputStream(fstream));
        }
        reader = new BufferedReader(isr, BUFFSIZE);
    }

    /**
     *
     * @return String line from reader
     * @throws IOException      on any issue reading data
     */
    public final String getLine() throws IOException {
        return reader.readLine();
    }

    /**
     *
     * @throws IOException      on any issue reading data
     */
    public final void close()
            throws IOException {
        reader.close();
    }
}
