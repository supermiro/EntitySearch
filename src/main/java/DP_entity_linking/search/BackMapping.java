package DP_entity_linking.search;

/**
 * Created by miroslav.kudlac on 2/27/2016.
 */
public class BackMapping {
    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public boolean isMapped() {
        return isMapped;
    }

    public void setIsMapped(boolean isMapped) {
        this.isMapped = isMapped;
    }

    private String words;
    private boolean isMapped;

}
