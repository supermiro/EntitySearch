package util;

/**
 * class StringWithScore.
 */
public class StringWithScore  implements Comparable<StringWithScore> {
    /** String id. */
    private String id;
    /** internal score. */
    private Double w;

    /**
     * constructor.
     * @param val String
     * @param score double
     */
    public StringWithScore(
            final String val,
            final double score) {
        this.id = val;
        this.w = score;
    }

    /**
     *
     * @return String
     */
    public final String getVal() {
        return id;
    }

    /**
     *
     * @param val String
     */
    public final void setVal(
            final String val) {
        this.id = val;
    }

    /**
     *
     * @return double
     */
    public final double getScore() {
        return w;
    }

    /**
     * set score.
     * @param score double
     */
    public final void setScore(
            final double score) {
        this.w = score;
    }


    public final int compareTo(
            final StringWithScore swsIn) {
        if (swsIn == null) {
            throw new IllegalArgumentException(
                    "parameter cannot be null for comparison");
        }
        return this.w.compareTo(swsIn.getScore());
    }

    public final String toString() {
        return "(" + id + "," + w + ")";
    }
}
