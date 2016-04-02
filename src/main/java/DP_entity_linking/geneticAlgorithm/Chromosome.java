package DP_entity_linking.geneticAlgorithm;

import DP_entity_linking.geneticAlgorithm.similarity.LmJelink;
import DP_entity_linking.search.Configuration;
import org.apache.lucene.search.similarities.Similarity;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;

/**
 * Created by miroslav.kudlac on 11/9/2015.
 */
public final class Chromosome implements GenSequence<Configuration>, Serializable {

    private GenSequence<Map<String, Float>> fields;
    private GenSequence<Similarity> similarity;

    public Chromosome() {
        fields = new BoostParameters();
        similarity = new LmJelink();
    }

    @Override
    public void create(Random rnd) {
        fields.create(rnd);
        similarity.create(rnd);
    }

    @Override
    public void mutate(Random rnd) {
        fields.mutate(rnd);
        similarity.mutate(rnd);
    }

    @Override
    public Configuration get() {
        Configuration conf = new Configuration();
        conf.setBoost( fields.get() );
        Similarity[] sims = new Similarity[]{ similarity.get()};
        conf.setSims(sims);
        return conf;
    }

    void cross() {
        // TODO: neskor
    }


    public GenSequence<Map<String, Float>> getFields() {
        return fields;
    }

    public GenSequence<Similarity> getSimilarity() {
        return similarity;
    }

    @Override
    public String toString() {
        return "Chromosome{" +
                "fields=" + fields +
                ", similarity=" + similarity +
                '}';
    }
}
