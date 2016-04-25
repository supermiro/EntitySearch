package DP_entity_linking.dataset;

import java.net.URI;

/**
 * Created by miroslav.kudlac on 10/3/2015.
 */
public class Record {
    private String utterance;
    private URI url;
    private String targetValue;

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
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

    public String getAnswer() {
        if (getUrl() == null) {
            return " ";
        }
        String path = this.getUrl().getPath();
        path = path.substring(path.lastIndexOf('/') + 1);
        String answer = path.replace("_", " ");
        return answer;
    }

    public String getQuestion() {
        return getUtterance();
    }

    @Override
    public String toString() {
        return "Record{" +
                "utterance='" + utterance + '\'' +
                ", url=" + url +
                ", targetValue='" + targetValue + '\'' +
                '}';
    }
}
