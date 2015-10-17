package util;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * class PrintOutput.
 */
public class PrintOutput {
    /**
     * DecimalFormat instance.
     */
    public static final DecimalFormat FORMAT = new DecimalFormat("###.######");

    /**
     * internal LineWriter.
     */
    private LineWriter lwe = null;
    /**
     * explained flag.
     */
    private boolean explained = true;
    /**
     * verbase flag.
     */
    private boolean verbose = true;

    /**
     * PrintOutput constructor.
     */
    public PrintOutput() {
        super();
        init("results_explained.txt");

    }

    /**
     * @param file path to print.
     */
    public PrintOutput(
            final String file) {
        super();
        init(file);
    }

    /**
     * @param file path to print.
     */
    private void init(
            final String file) {
        if (explained) {
            try {
                lwe = new LineWriter(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param s String to print.
     */
    public final void print(
            final String s) {
        if (verbose) {
            System.out.print(s);
        }
        if (explained) {
            try {
                lwe.putString(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param s String to print
     */
    public final void println(
            final String s) {
        println(s, verbose);
    }
    
    /**
     * @param s String to print
     */
    public final void println(
            final String s, final boolean verbose) {
        if (verbose) {
            System.out.println(s);
        }
        if (explained) {
            try {
                lwe.putLine(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * close line writer.
     */
    public final void close() {
        if (lwe != null) {
            try {
                lwe.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
