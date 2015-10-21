package DP_entity_linking;

/**
 * Created by miroslav.kudlac on 10/3/2015.
 */
public class Record {
    private String utterance;
    private String url;
    private String targetValue;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public String getUtterance() {
        return utterance;
    }

    public void setUtterance(String utterance) {
        this.utterance = utterance;
    }


    @Override
    public String toString() {
        url = url.replaceFirst(".*/(\\w+).*","$1").replace("_", " ");
        utterance = utterance.replaceAll("\\b[\\w']{1,3}\\b", "").replaceAll("[^a-z0-9]", " ").replaceAll("( )+", " ");
        return "Record{" +
                "utterance='" + utterance + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
