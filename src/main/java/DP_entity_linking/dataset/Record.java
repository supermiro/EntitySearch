package DP_entity_linking.dataset;

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

    public String getAnswer() {
        if (getUrl() == null) {
            return " ";
        }
        return getUrl();
       // String path = this.getUrl().getPath();
       // path = path.substring(path.lastIndexOf('/') + 1);
       // String answer = path.replace("_", " ");
      //  return answer;
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
